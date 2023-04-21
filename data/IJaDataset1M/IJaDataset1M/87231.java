package com.idna.dm.domain.impl;

import java.util.LinkedHashSet;
import java.util.LinkedList;
import java.util.Set;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import com.idna.dm.dao.orm.domain.impl.FlatRule;
import com.idna.dm.domain.Concludable;
import com.idna.dm.domain.Conclusion;
import com.idna.dm.domain.NodeTraversalStatus;
import com.idna.dm.domain.ThreadLocalShortCircuitReportStack;
import com.idna.dm.domain.Conclusion.ConclusionEnum;
import com.idna.dm.domain.helper.LogDebug;
import com.idna.dm.domain.input.DecisionInputData;
import com.idna.dm.logging.activity.Auditable;
import com.idna.dm.util.reflection.ObjectFieldParser;

/**
 * A {@code Rule} is a workflow composed of logically linked {@link Node}s and
 * resolves to <b>true</b> or <b>false</b> hence IS-A {@link Concludable}. In
 * terms of data structure design, a {@code Rule} does not have a collection of
 * {@link Node}s because its final conclusion can be retrieved from the start
 * node without knowledges of its underlying nodes topology.
 * 
 */
@SuppressWarnings("serial")
public class Rule extends BasicDomainObject implements Concludable, Auditable {

    @LogDebug
    private Node startNode;

    private String ruleCode;

    private String ruleDescription;

    private final Log logger = LogFactory.getLog(this.getClass());

    public Rule() {
    }

    public Rule(FlatRule tempRule) {
        if (tempRule == null) return;
        this.id = tempRule.getRuleId();
        this.name = tempRule.getName();
        this.ruleDescription = tempRule.getDescription();
        this.ruleCode = tempRule.getGroupName() + "-" + tempRule.getRuleId();
    }

    @Override
    public Conclusion resolveConclusion(DecisionInputData input) {
        NodeTraversalStatus status = NodeTraversalStatus.TRAVERSING;
        Conclusion subConclusion = startNode.resolveConclusion(input);
        status = this.establishFinalisedNodeTraversalStatus(subConclusion.getConclusion());
        Conclusion renderedConclusion = Conclusion.getRuleConclusion(status, subConclusion.getConclusion());
        if (status == NodeTraversalStatus.SHORT_CIRCUITED) {
            if (logger.isInfoEnabled()) {
                logger.info(String.format("RULE %s SHORT-CIRCUITED: %s", this.name, ObjectFieldParser.parseFieldValues(renderedConclusion)));
            }
            ThreadLocalShortCircuitReportStack.pushContextInfo(this.name);
        } else if (status == NodeTraversalStatus.TRAVERSAL_COMPLETED) {
            ThreadLocalShortCircuitReportStack.reset();
        }
        return renderedConclusion;
    }

    private NodeTraversalStatus establishFinalisedNodeTraversalStatus(ConclusionEnum c) {
        NodeTraversalStatus status = null;
        RuleType ruleType = getRuleType();
        if (ruleType == RuleType.AND && c == ConclusionEnum.TRUE) {
            status = NodeTraversalStatus.TRAVERSAL_COMPLETED;
        } else if (ruleType == RuleType.OR && c == ConclusionEnum.FALSE) {
            status = NodeTraversalStatus.TRAVERSAL_COMPLETED;
        } else if (ruleType == RuleType.BASIC_EXPRESSION_WRAPPER && c == ConclusionEnum.TRUE) {
            status = NodeTraversalStatus.TRAVERSAL_COMPLETED;
        } else {
            status = NodeTraversalStatus.SHORT_CIRCUITED;
        }
        return status;
    }

    @Override
    public String toString() {
        StringBuffer sb = new StringBuffer("[***| ");
        sb.append(super.toString());
        sb.append(" |***]");
        return sb.toString();
    }

    @Override
    public String getParameters() {
        return "StartNode: " + startNode;
    }

    public Node getStartNode() {
        return startNode;
    }

    public RuleType getRuleType() {
        if (startNode.getTrueNextNode() == null && startNode.getFalseNextNode() == null) return RuleType.BASIC_EXPRESSION_WRAPPER;
        return startNode.getNextLinkedBoolean() ? RuleType.AND : RuleType.OR;
    }

    /**
	 * <ul>
	 * <li>
	 * AND - Relates to Nodes which are wired with False nodes
	 * </li>
	 * <li>
	 * OR - Relates to Nodes which are wired with True nodes
	 * </li>
	 * <li>
	 * BASIC_EXPRESSION_WRAPPER - Relates to Nodes which are merely wrappers for a Basic Expression 
	 * and therefore do not have a node network. i.e. They are just a single node representing the start and the end of the network
	 * </li>
	 * </ul>
	 * @author matthew.cosgrove
	 *
	 */
    public enum RuleType {

        /**
		 * Relates to Nodes which are wired with True nodes
		 */
        AND("Relates to Nodes which are wired with True nodes"), /**
		 * Relates to Nodes which are wired with False nodes
		 */
        OR("Relates to Nodes which are wired with False nodes"), /**
		 * Relates to Nodes which are merely wrappers for a Basic Expression and therefore do not have a node network. i.e. They are just a single node representing the start and the end of the network
		 */
        BASIC_EXPRESSION_WRAPPER("Relates to Nodes which are merely wrappers for a Basic Expression and therefore do not have a node network");

        RuleType(String description) {
            this.description = description;
        }

        private String description;

        public String getDescription() {
            return description;
        }
    }

    public void setStartNode(Node startNode) {
        this.startNode = startNode;
    }

    public String getRuleCode() {
        return ruleCode;
    }

    public void setRuleCode(String ruleCode) {
        this.ruleCode = ruleCode;
    }

    public String getRuleDescription() {
        return ruleDescription;
    }

    public void setRuleDescription(String ruleDescription) {
        this.ruleDescription = ruleDescription;
    }

    @Override
    public Set<String> getIdentifiers() {
        Set<String> ids = new LinkedHashSet<String>();
        ids.addAll(startNode.getIdentifiers());
        return ids;
    }

    /**
	 * Convenience method intended for use with a top level Decision Rule which traverses all the Nodes looking for Rules.
	 * 
	 * @return The rules if they exist. An empty collection otherwise.
	 */
    public LinkedList<Rule> getNestedRulesInOrderOfAppearance() {
        LinkedList<Rule> set = new LinkedList<Rule>();
        LinkedList<Node> childNodes = getImmediateChildNodesInOrderOfAppearance();
        for (Node node : childNodes) {
            Concludable subConcludable = node.getSubConclusion();
            if (subConcludable instanceof Rule) {
                Rule r = (Rule) subConcludable;
                set.add(r);
                LinkedList<Rule> nestedRules = r.getNestedRulesInOrderOfAppearance();
                if (nestedRules.isEmpty()) continue;
                if (logger.isInfoEnabled()) {
                    logger.info(String.format("Detected Nested Rules For %s: %s", r.getRuleCode(), nestedRules));
                }
                set.addAll(nestedRules);
            }
        }
        return set;
    }

    private LinkedList<Node> getImmediateChildNodesInOrderOfAppearance() {
        LinkedList<Node> childNodes = new LinkedList<Node>();
        childNodes.add(startNode);
        for (Node nextNode = startNode.getNextWiredNode(); nextNode != null; nextNode = nextNode.getNextWiredNode()) {
            if (nextNode == null) break;
            childNodes.add(nextNode);
        }
        return childNodes;
    }
}
