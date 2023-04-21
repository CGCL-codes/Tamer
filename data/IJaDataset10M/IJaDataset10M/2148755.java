package net.sf.parser4j.generator.entity.grammardefnode;

import java.util.Map;
import java.util.Set;
import java.util.TreeMap;
import net.sf.parser4j.generator.service.GrammarDefNodeVisitException;
import net.sf.parser4j.generator.service.grammardefnode.IGrammarDefVisitor;
import net.sf.parser4j.kernelgenerator.service.GeneratorException;
import net.sf.parser4j.parser.entity.parsenode.data.AbstractParserNodeData;

/**
 * 
 * @author luc peuvrier
 * 
 */
public class RulesGroupListDef extends AbstractParserNodeData implements IGrammarDefNode {

    private Map<String, RulesDef> rulesDefByWhiteSpaceNameMap = new TreeMap<String, RulesDef>();

    private Map<String, StatementDef> statementDefByNonTerminalNameMap = new TreeMap<String, StatementDef>();

    /**
	 * construct an empty rules group
	 */
    public RulesGroupListDef() {
        super();
    }

    public RulesGroupListDef(final RulesGroupListDef rulesGroupListDef, final RulesGroupDef rulesGroupDef) throws GeneratorException {
        super();
        rulesDefByWhiteSpaceNameMap.putAll(rulesGroupListDef.rulesDefByWhiteSpaceNameMap);
        final String whiteSpaceNonTerminalName = rulesGroupDef.getWhiteSpaceNonTerminalName();
        RulesDef existing = rulesDefByWhiteSpaceNameMap.get(whiteSpaceNonTerminalName);
        final RulesDef toAdd = rulesGroupDef.getRulesDef();
        toAdd.setWhiteSpaceNonTerminalName(whiteSpaceNonTerminalName);
        if (existing == null) {
            rulesDefByWhiteSpaceNameMap.put(whiteSpaceNonTerminalName, toAdd);
        } else {
            existing.addAll(toAdd);
        }
        statementDefByNonTerminalNameMap.putAll(rulesGroupListDef.statementDefByNonTerminalNameMap);
        for (Map.Entry<String, StatementDef> entry : rulesGroupDef.getStatementDefByNonTerminalNameMap().entrySet()) {
            if (statementDefByNonTerminalNameMap.put(entry.getKey(), entry.getValue()) != null) {
                throw new GeneratorException("duplicate definition of " + entry.getKey());
            }
        }
        final Set<RuleDef> terminalRuleDefSet = rulesGroupDef.getTerminalRuleDefSet();
        if (!terminalRuleDefSet.isEmpty()) {
            final RulesDef terminalRulesDef = new RulesDef(terminalRuleDefSet);
            terminalRulesDef.setWhiteSpaceNonTerminalName("");
            existing = rulesDefByWhiteSpaceNameMap.get("");
            if (existing == null) {
                rulesDefByWhiteSpaceNameMap.put("", terminalRulesDef);
            } else {
                existing.addAll(terminalRulesDef);
            }
            for (Map.Entry<String, StatementDef> entry : terminalRulesDef.getStatementDefByNonTerminalNameMap().entrySet()) {
                if (statementDefByNonTerminalNameMap.put(entry.getKey(), entry.getValue()) != null) {
                    throw new GeneratorException("duplicate definition of " + entry.getKey());
                }
            }
        }
    }

    public RulesGroupListDef(final RulesGroupDef rulesGroupDef) throws GeneratorException {
        super();
        final RulesDef toAdd = rulesGroupDef.getRulesDef();
        final String whiteSpaceNonTerminalName = rulesGroupDef.getWhiteSpaceNonTerminalName();
        toAdd.setWhiteSpaceNonTerminalName(whiteSpaceNonTerminalName);
        rulesDefByWhiteSpaceNameMap.put(whiteSpaceNonTerminalName, toAdd);
        statementDefByNonTerminalNameMap.putAll(rulesGroupDef.getStatementDefByNonTerminalNameMap());
        final Set<RuleDef> terminalRuleDefSet = rulesGroupDef.getTerminalRuleDefSet();
        if (!terminalRuleDefSet.isEmpty()) {
            final RulesDef terminalRulesDef = new RulesDef(terminalRuleDefSet);
            terminalRulesDef.setWhiteSpaceNonTerminalName("");
            final RulesDef existing = rulesDefByWhiteSpaceNameMap.get("");
            if (existing == null) {
                rulesDefByWhiteSpaceNameMap.put("", terminalRulesDef);
            } else {
                existing.addAll(terminalRulesDef);
            }
            statementDefByNonTerminalNameMap.putAll(terminalRulesDef.getStatementDefByNonTerminalNameMap());
        }
    }

    public Map<String, RulesDef> getRulesDefByWhiteSpaceNameMap() {
        return rulesDefByWhiteSpaceNameMap;
    }

    /**
	 * @return the statementDefByNonTerminalNameMap
	 */
    public Map<String, StatementDef> getStatementDefByNonTerminalNameMap() {
        return statementDefByNonTerminalNameMap;
    }

    public void accept(final IGrammarDefVisitor visitor) throws GrammarDefNodeVisitException {
        visitor.beginVisit(this);
        for (RulesDef rulesDef : rulesDefByWhiteSpaceNameMap.values()) {
            rulesDef.accept(visitor);
        }
        visitor.endVisit(this);
    }

    @Override
    public String toString() {
        final StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("rulesDefByWhiteSpaceNameMap=\n");
        for (Map.Entry<String, RulesDef> entry : rulesDefByWhiteSpaceNameMap.entrySet()) {
            stringBuilder.append(entry.getKey());
            stringBuilder.append(" : ");
            stringBuilder.append(entry.getValue());
            stringBuilder.append('\n');
        }
        stringBuilder.append("statementDefByNonTerminalNameMap=\n");
        for (Map.Entry<String, StatementDef> entry : statementDefByNonTerminalNameMap.entrySet()) {
            stringBuilder.append(entry.getKey());
            stringBuilder.append(" : ");
            stringBuilder.append(entry.getValue());
            stringBuilder.append('\n');
        }
        return stringBuilder.toString();
    }
}
