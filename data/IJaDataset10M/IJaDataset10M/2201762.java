package ws.prova.reference2;

import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import ws.prova.kernel2.ProvaConstant;
import ws.prova.kernel2.ProvaDerivationNode;
import ws.prova.kernel2.ProvaKnowledgeBase;
import ws.prova.kernel2.ProvaList;
import ws.prova.kernel2.ProvaListPtr;
import ws.prova.kernel2.ProvaLiteral;
import ws.prova.kernel2.ProvaObject;
import ws.prova.kernel2.ProvaPredicate;
import ws.prova.kernel2.ProvaRule;
import ws.prova.kernel2.ProvaUnification;
import ws.prova.kernel2.ProvaVariable;
import ws.prova.kernel2.ProvaVariablePtr;

public class ProvaUnificationImpl implements ProvaUnification {

    private ProvaRule source;

    private ProvaRule target;

    private long sourceRuleId;

    private long targetRuleId;

    private List<ProvaVariable> sourceVariables;

    private List<ProvaVariable> targetVariables;

    private List<ProvaList> meta;

    public ProvaUnificationImpl(ProvaRule source, ProvaRule target) {
        init(source, target, true);
    }

    public ProvaUnificationImpl(ProvaRule source, ProvaRule target, boolean cloneTarget) {
        init(source, target, cloneTarget);
    }

    private void init(ProvaRule source, ProvaRule target, boolean cloneTarget) {
        this.source = source;
        this.target = target;
        this.sourceRuleId = source.getRuleId();
        this.targetRuleId = target.getRuleId();
        this.sourceVariables = source.getVariables();
        if (cloneTarget) this.targetVariables = target.cloneVariables(); else this.targetVariables = target.getVariables();
    }

    public void setSource(ProvaRule source) {
        this.source = source;
    }

    @Override
    public ProvaRule getSource() {
        return source;
    }

    public void setTarget(ProvaRule target) {
        this.target = target;
    }

    @Override
    public ProvaRule getTarget() {
        return target;
    }

    public void setSourceRuleId(long sourceRuleId) {
        this.sourceRuleId = sourceRuleId;
    }

    @Override
    public long getSourceRuleId() {
        return sourceRuleId;
    }

    public void setTargetRuleId(long targetRuleId) {
        this.targetRuleId = targetRuleId;
    }

    @Override
    public long getTargetRuleId() {
        return targetRuleId;
    }

    public void setSourceVariables(List<ProvaVariable> sourceVariables) {
        this.sourceVariables = sourceVariables;
    }

    @Override
    public List<ProvaVariable> getSourceVariables() {
        return sourceVariables;
    }

    public void setTargetVariables(List<ProvaVariable> targetVariables) {
        this.targetVariables = targetVariables;
    }

    @Override
    public List<ProvaVariable> getTargetVariables() {
        return targetVariables;
    }

    @Override
    public boolean unify() {
        ProvaLiteral[] sourceLiterals = source.getBody();
        ProvaLiteral sourceLiteral = sourceLiterals[0];
        if (!matchMetadata(sourceLiteral, target)) return false;
        ProvaLiteral targetLiteral = target.getHead();
        ProvaPredicate sourcePredicate = sourceLiteral.getPredicate();
        ProvaPredicate targetPredicate = targetLiteral.getPredicate();
        return sourcePredicate.equals(targetPredicate) && sourceLiteral.unify(targetLiteral, this);
    }

    private boolean matchMetadata(ProvaLiteral sourceLiteral, ProvaRule target) {
        Map<String, List<Object>> sourceMetadata = sourceLiteral.getMetadata();
        if (sourceMetadata == null || sourceMetadata.size() == 0) return true;
        Map<String, List<Object>> targetMetadata = target.getMetadata();
        if (targetMetadata == null) return false;
        for (Entry<String, List<Object>> s : sourceMetadata.entrySet()) {
            List<Object> value = targetMetadata.get(s.getKey());
            List<Object> sValue = s.getValue();
            if (value == null) return false;
            boolean matched = false;
            for (Object vo : value) {
                if (!(vo instanceof String)) continue;
                String v = (String) vo;
                for (Object sVo : sValue) {
                    if (!(sVo instanceof String)) continue;
                    String sV = (String) sVo;
                    if (sV.length() != 0 && Character.isUpperCase(sV.charAt(0))) {
                        if (meta == null) return false;
                        for (ProvaList m : meta) {
                            ProvaObject[] mo = m.getFixed();
                            String varName = (String) ((ProvaConstant) mo[0]).getObject();
                            ProvaObject var = mo[1];
                            if (varName.equals(sV)) {
                                if (mo[1] instanceof ProvaVariablePtr) {
                                    ProvaVariablePtr varPtr = (ProvaVariablePtr) var;
                                    var = sourceVariables.get(varPtr.getIndex()).getRecursivelyAssigned();
                                }
                                if (var instanceof ProvaVariable) {
                                    ((ProvaVariable) var).setAssigned(ProvaConstantImpl.create(v));
                                    matched = true;
                                    break;
                                } else if (var instanceof ProvaConstant) {
                                    sV = (String) ((ProvaConstant) var).getObject();
                                    break;
                                }
                            }
                        }
                    }
                    if (matched) break;
                    if (v.equals(sV)) {
                        matched = true;
                        break;
                    }
                }
                if (matched) break;
            }
            if (!matched) return false;
        }
        return true;
    }

    @Override
    public ProvaLiteral[] rebuildNewGoals() {
        final ProvaLiteral[] body = target.getBody();
        int bodyLength = body == null ? 0 : body.length;
        ProvaLiteral[] goals = new ProvaLiteralImpl[bodyLength];
        for (int i = 0; i < bodyLength; i++) {
            goals[i] = body[i].rebuild(this);
        }
        return goals;
    }

    private ProvaLiteral[] rebuildNewGoals(ProvaDerivationNode node) {
        final ProvaLiteral[] body = target.getGuardedBody(source.getBody()[0]);
        int bodyLength = body == null ? 0 : body.length;
        ProvaLiteral[] goals = new ProvaLiteralImpl[bodyLength];
        for (int i = 0; i < bodyLength; i++) {
            if ("cut".equals(body[i].getPredicate().getSymbol())) {
                ProvaVariablePtr any = (ProvaVariablePtr) body[i].getTerms().getFixed()[0];
                if (any.getRuleId() == source.getRuleId()) sourceVariables.get(any.getIndex()).setAssigned(ProvaConstantImpl.create(node)); else targetVariables.get(any.getIndex()).setAssigned(ProvaConstantImpl.create(node));
            }
            goals[i] = body[i].rebuild(this);
        }
        return goals;
    }

    @Override
    public ProvaLiteral[] rebuildOldGoals(ProvaLiteral[] body) {
        if (!isSourceSubstituted()) return body;
        ProvaLiteral[] goals = new ProvaLiteralImpl[body.length];
        for (int i = 1; i < body.length; i++) {
            goals[i] = body[i].rebuildSource(this);
        }
        return goals;
    }

    private ProvaLiteral[] rebuildOldGoals(ProvaLiteral[] body, int offset) {
        ProvaLiteral[] goals = new ProvaLiteralImpl[body.length];
        for (int i = 1 + offset; i < body.length; i++) {
            goals[i] = body[i].rebuildSource(this);
        }
        return goals;
    }

    private boolean isSourceSubstituted() {
        for (ProvaVariable variable : sourceVariables) if (variable.getAssigned() != null) return true;
        return false;
    }

    @Override
    public ProvaVariable getVariableFromVariablePtr(ProvaVariablePtr variablePtr) {
        if (variablePtr.getRuleId() == sourceRuleId) return sourceVariables.get(variablePtr.getIndex());
        return targetVariables.get(variablePtr.getIndex());
    }

    @Override
    public ProvaObject rebuild(ProvaVariablePtr variablePtr) {
        ProvaVariable variable = getVariableFromVariablePtr(variablePtr);
        ProvaObject assigned = variable.getRecursivelyAssigned();
        if (assigned instanceof ProvaVariable) {
            if (((ProvaVariable) assigned).getRuleId() == targetRuleId) {
                int index = assigned.collectVariables(targetRuleId, sourceVariables);
                return new ProvaVariablePtrImpl(sourceRuleId, index);
            } else {
                return new ProvaVariablePtrImpl(sourceRuleId, ((ProvaVariable) assigned).getIndex());
            }
        } else if (assigned instanceof ProvaList) {
            return ((ProvaList) assigned).rebuild(this);
        } else if (assigned instanceof ProvaListPtr) {
            return ((ProvaListPtr) assigned).rebuild(this);
        }
        return assigned;
    }

    @Override
    public ProvaObject rebuildSource(ProvaVariablePtr variablePtr) {
        ProvaVariable variable = getVariableFromVariablePtr(variablePtr);
        ProvaObject assigned = variable.getRecursivelyAssigned();
        if (assigned instanceof ProvaVariable) {
            if (((ProvaVariable) assigned).getRuleId() == targetRuleId) {
                int index = assigned.collectVariables(targetRuleId, sourceVariables);
                return new ProvaVariablePtrImpl(sourceRuleId, index);
            } else {
                return new ProvaVariablePtrImpl(sourceRuleId, ((ProvaVariable) assigned).getIndex());
            }
        } else if (assigned instanceof ProvaList) {
            return ((ProvaList) assigned).rebuildSource(this);
        } else if (assigned instanceof ProvaListPtr) {
            return ((ProvaListPtr) assigned).rebuildSource(this);
        }
        return assigned;
    }

    @Override
    public ProvaRule generateQuery(ProvaKnowledgeBase kb, ProvaRule query, ProvaDerivationNode node) {
        ProvaLiteral[] newGoals = rebuildNewGoals(node);
        ProvaLiteral[] oldGoals = rebuildOldGoals(query.getBody(), query.getOffset());
        ProvaRule newQuery = kb.generateRule(null, newGoals, oldGoals, query.getOffset());
        return rebuild(newQuery);
    }

    private ProvaRule rebuild(ProvaRule newQuery) {
        int size = sourceVariables.size();
        ProvaVariablePtr[] varsMap = new ProvaVariablePtr[size];
        List<ProvaVariable> newVariables = newQuery.getVariables();
        int index = 0;
        for (int i = 0; i < size; i++) {
            if (sourceVariables.get(i).getAssigned() == null) {
                varsMap[i] = new ProvaVariablePtrImpl(0, index);
                ProvaVariable newVariable = sourceVariables.get(i).clone();
                newVariable.setIndex(index++);
                newVariable.setRuleId(0);
                newVariables.add(newVariable);
            }
        }
        newQuery.substituteVariables(varsMap);
        return newQuery;
    }

    @Override
    public boolean targetUnchanged() {
        for (ProvaVariable var : targetVariables) {
            if (var.getAssigned() != null && (!(var.getAssigned() instanceof ProvaVariable) || var.getType() != ((ProvaVariable) var.getAssigned()).getType())) return false;
        }
        return true;
    }

    public void setMeta(List<ProvaList> meta) {
        if (meta != null) this.meta = meta;
    }
}
