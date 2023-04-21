package com.idna.dm.dao.orm.converter;

import java.util.List;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import com.idna.dm.dao.orm.domain.impl.FlatRuleNode;
import com.idna.dm.dao.orm.domain.impl.FlatRule;
import com.idna.dm.dao.orm.domain.util.TempNodeRowList;
import com.idna.dm.domain.Concludable;
import com.idna.dm.domain.impl.Node;
import com.idna.dm.domain.impl.Rule;

public class FlatNodeConvertorImpl implements FlatDomainConvertor<Node, TempNodeRowList> {

    protected Log logger = LogFactory.getLog(this.getClass());

    /**
	 * TODO rename this method and class to better explain what it does
	 * @see {@link #extract(Integer, TempNodeRowList)}
	 */
    @Override
    public Node extract(Integer startNodeId, TempNodeRowList rows, List<FlatRule> ruleList) {
        FlatRuleNode tempStartNode = (FlatRuleNode) rows.getTempNodeWithId(startNodeId);
        Node startNode = createNode(tempStartNode, rows, ruleList);
        return startNode;
    }

    private Node createNode(FlatRuleNode tempStartNode, TempNodeRowList rows, List<FlatRule> ruleList) {
        Integer tnnId = tempStartNode.getTnnId();
        Integer fnnId = tempStartNode.getFnnId();
        Node node = new Node();
        node.setId(tempStartNode.getId());
        if (tnnId != null) {
            Node trueNextNode = createNode(rows.getTempNodeWithId(tnnId), rows, ruleList);
            node.setTrueNextNode(trueNextNode);
        }
        if (fnnId != null) {
            Node falseNextNode = createNode(rows.getTempNodeWithId(fnnId), rows, ruleList);
            node.setFalseNextNode(falseNextNode);
        }
        String subConclusionNodeType = tempStartNode.getSubConclusionNodeType();
        Concludable conclusion = null;
        if (subConclusionNodeType.equalsIgnoreCase("RULE_NODE")) {
            conclusion = createRule(tempStartNode, rows, ruleList);
        } else if (subConclusionNodeType.equalsIgnoreCase("EXPRESSION_NODE")) {
            conclusion = tempStartNode.getBasicExpression();
        }
        node.setSubConclusion(conclusion);
        return node;
    }

    private Rule createRule(FlatRuleNode tempNode, TempNodeRowList rows, List<FlatRule> ruleList) {
        for (FlatRule tempRule : ruleList) {
            if (tempRule.getId().equals(tempNode.getSubConclusionId())) {
                Rule rule = new Rule(tempRule);
                rule.setId(tempNode.getSubConclusionId());
                Node startNode = createNode(rows.getTempNodeWithId(tempNode.getNestedRuleStartNodeId()), rows, ruleList);
                rule.setStartNode(startNode);
                return rule;
            }
        }
        return null;
    }
}
