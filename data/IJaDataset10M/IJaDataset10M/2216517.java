package org.tm4j.tologx.parser;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.tm4j.tologx.TologProcessingException;
import org.tm4j.tologx.utils.TologContext;
import org.tm4j.tologx.utils.VariableSet;
import org.tm4j.topicmap.Topic;

/**
 * 
 * @author Kal Ahmed (kal@techquila.com)
 */
public class ClauseList implements Clause {

    private ArrayList m_clauses;

    private ArrayList m_vars;

    private Log m_log = LogFactory.getLog(ClauseList.class);

    public ClauseList() {
        m_clauses = new ArrayList();
        m_vars = new ArrayList();
    }

    public VariableSet execute(VariableSet params, TologContext context) throws TologProcessingException {
        m_log.debug("Start ClauseList " + toString());
        for (Iterator it = m_clauses.iterator(); (params.size() > 0) && (it.hasNext()); ) {
            Object o = it.next();
            if (o instanceof Predicate) {
                params = executePredicate((Predicate) o, params, context);
            } else if (o instanceof Clause) {
                params = ((Clause) o).execute(params, context);
            }
        }
        m_log.debug("Clause list results:");
        params.dump(m_log);
        m_log.debug("End ClauseList " + toString());
        return params;
    }

    private VariableSet executePredicate(Predicate p, VariableSet params, TologContext context) throws TologProcessingException {
        m_log.debug("Invoking " + p.toString() + " with parameters:");
        params.dump(m_log);
        List predicateParams = new ArrayList(5);
        if (params.size() > 0) {
            int max = params.size();
            for (int i = 0; i < max; i++) {
                List vsRow = params.removeRow(0);
                Iterator predParamsIt = p.getParameters().iterator();
                while (predParamsIt.hasNext()) {
                    Object o = predParamsIt.next();
                    if (o instanceof Variable) {
                        int ix = params.indexOf((Variable) o);
                        if (ix < 0) {
                            ix = params.addColumn(o);
                            vsRow.add(o);
                        }
                        Object v = vsRow.get(ix);
                        if (v == null) {
                            v = o;
                        }
                        predicateParams.add(v);
                    } else if (o instanceof PlayerRoleVarPair) {
                        PlayerRoleVarPair src = (PlayerRoleVarPair) o;
                        PlayerRoleVarPair cp = new PlayerRoleVarPair();
                        if (src.getPlayerTopic() != null) {
                            cp.setPlayerTopic(src.getPlayerTopic());
                        } else {
                            int ix = params.indexOf(src.getPlayerVar());
                            if (ix < 0) {
                                throw new TologProcessingException("Unrecognised variable " + src.getPlayerVar().toString());
                            }
                            Object v = vsRow.get(ix);
                            if (v == null) {
                                cp.setPlayerVar(src.getPlayerVar());
                            } else if (v instanceof Variable) {
                                cp.setPlayerVar((Variable) v);
                            } else if (v instanceof Topic) {
                                cp.setPlayerTopic((Topic) v);
                            } else {
                                throw new TologProcessingException("Invalid object bound to " + src.getPlayerVar());
                            }
                        }
                        if (src.getRoleTopic() != null) {
                            cp.setRoleTopic(src.getRoleTopic());
                        } else if (src.getRoleVar() != null) {
                            int ix = params.indexOf(src.getRoleVar());
                            if (ix < 0) {
                                throw new TologProcessingException("Unrecognised variable " + src.getRoleVar().toString());
                            }
                            Object v = vsRow.get(ix);
                            if (v == null) {
                                cp.setRoleVar(src.getRoleVar());
                            } else if (v instanceof Variable) {
                                cp.setRoleVar((Variable) v);
                            } else if (v instanceof Topic) {
                                cp.setRoleTopic((Topic) v);
                            } else {
                                throw new TologProcessingException("Invalid object bound to " + src.getRoleVar());
                            }
                        }
                        predicateParams.add(cp);
                    } else {
                        predicateParams.add(o);
                    }
                }
                params.addMatchResults(vsRow, p.matches(predicateParams, context));
                predicateParams.clear();
            }
        } else {
            m_log.error("ClauseList attempted to invoke predicate with no parameters!");
            predicateParams.addAll(p.getParameters());
            params.add(p.matches(predicateParams, context));
        }
        if (m_log.isDebugEnabled()) {
            m_log.debug("After " + p.toString() + " results are");
            params.dump(m_log);
        }
        return params;
    }

    public void addPredicate(Predicate p) {
        m_clauses.add(p);
        Iterator it = p.getParameters().iterator();
        while (it.hasNext()) {
            Object o = it.next();
            if (o instanceof Variable) {
                addVariable((Variable) o);
            } else if (o instanceof PlayerRoleVarPair) {
                PlayerRoleVarPair pr = (PlayerRoleVarPair) o;
                if (pr.getPlayerVar() != null) addVariable(pr.getPlayerVar());
                if (pr.getRoleVar() != null) addVariable(pr.getRoleVar());
            }
        }
    }

    private void addVariable(Variable v) {
        if (!m_vars.contains(v)) {
            m_vars.add(v);
        }
    }

    public void addClause(Clause c) {
        m_clauses.add(c);
        Iterator it = c.getVariables().iterator();
        while (it.hasNext()) {
            addVariable((Variable) it.next());
        }
    }

    public void optimize() {
    }

    public int size() {
        return m_clauses.size();
    }

    public String toString() {
        StringBuffer sb = new StringBuffer();
        Iterator it = m_clauses.iterator();
        while (it.hasNext()) {
            sb.append(it.next().toString());
            if (it.hasNext()) sb.append(", ");
        }
        return sb.toString();
    }

    /**
	 * 
	 */
    public List getVariables() {
        return m_vars;
    }

    public void doReplacement(Object[] params) throws TologProcessingException {
        Iterator it = m_clauses.iterator();
        while (it.hasNext()) {
            Object o = it.next();
            if (o instanceof Predicate) {
                List l = ((Predicate) o).getParameters();
                for (int i = 0; i < l.size(); i++) {
                    Object item = l.get(i);
                    if (item instanceof Placeholder) {
                        Placeholder ph = (Placeholder) l.get(i);
                        l.set(i, ph.getReplacement(params));
                    } else if (item instanceof PlayerRoleVarPair) {
                        PlayerRoleVarPair prvp = (PlayerRoleVarPair) item;
                        if (prvp.getPlayerPlaceholder() != null) {
                            Object rep = prvp.getPlayerPlaceholder().getReplacement(params);
                            if (rep instanceof Topic) {
                                prvp.setPlayerTopic((Topic) rep);
                            } else {
                                throw new TologProcessingException("Type mismatch in replacement %" + i + ". A Topic was expected.");
                            }
                        }
                        if (prvp.getRolePlaceholder() != null) {
                            Object rep = prvp.getRolePlaceholder().getReplacement(params);
                            if (rep instanceof Topic) {
                                prvp.setRoleTopic((Topic) rep);
                            } else {
                                throw new TologProcessingException("Type mismatch in replacement %" + i + ". A Topic was expected.");
                            }
                        }
                    }
                }
            } else if (o instanceof Clause) {
                ((Clause) o).doReplacement(params);
            }
        }
    }
}
