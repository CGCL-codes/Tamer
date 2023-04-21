package org.qtitools.qti.node.test.flow;

import org.qtitools.qti.exception.QTIItemFlowException;
import org.qtitools.qti.node.XmlNode;
import org.qtitools.qti.node.test.AbstractPart;
import org.qtitools.qti.node.test.AssessmentItemRef;
import org.qtitools.qti.node.test.AssessmentTest;
import org.qtitools.qti.node.test.BranchRule;
import org.qtitools.qti.node.test.ControlObject;
import org.qtitools.qti.node.test.NavigationMode;
import org.qtitools.qti.node.test.PreCondition;
import org.qtitools.qti.node.test.SubmissionMode;
import org.qtitools.qti.node.test.TestPart;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Default implementation of item flow.
 * 
 * @author Jiri Kajaba
 */
public class DefaultItemFlow implements ItemFlow {

    private static final long serialVersionUID = 1L;

    /** Logger. */
    private static Logger logger = LoggerFactory.getLogger(DefaultItemFlow.class);

    protected StartNode start;

    protected ItemRefNode current;

    /**
	 * Constructs item flow.
	 *
	 * @param test assessment test
	 */
    public DefaultItemFlow(AssessmentTest test) {
        start = initItemFlow(test);
        logger.debug("Item flow created.");
        Node node = start;
        while (node != null) {
            logger.debug(node.toString());
            node = node.getNext();
        }
    }

    private StartNode initItemFlow(AssessmentTest test) {
        StartNode start = new StartNode(test);
        Node last = start;
        for (TestPart child : test.getTestParts()) last = initItemFlow(last, child);
        last = new EndNode(last, test);
        processJumps(start);
        return start;
    }

    private Node initItemFlow(Node last, AbstractPart parent) {
        last = new StartNode(last, parent);
        for (PreCondition preCondition : parent.getPreConditions()) last = new PreConditionNode(last, preCondition);
        if (parent instanceof AssessmentItemRef) {
            last = new ItemRefNode(last, (AssessmentItemRef) parent);
        } else {
            for (AbstractPart child : parent.getChildren()) last = initItemFlow(last, child);
        }
        for (BranchRule branchRule : parent.getBranchRules()) last = new BranchRuleNode(last, branchRule);
        last = new EndNode(last, parent);
        return last;
    }

    private void processJumps(StartNode start) {
        Node node = start;
        while (node != null) {
            if (node.isPreCondition()) {
                PreConditionNode condition = (PreConditionNode) node;
                Node target = findPreConditionTarget(condition);
                condition.setTarget(target);
            } else if (node.isBranchRule()) {
                BranchRuleNode branch = (BranchRuleNode) node;
                Node target = findBranchRuleTarget(branch);
                branch.setTarget(target);
            }
            node = node.getNext();
        }
    }

    private Node findPreConditionTarget(PreConditionNode preCondition) {
        AbstractPart parent = preCondition.getPreCondition().getParent();
        Node node = preCondition.getNext();
        while (node.getNext() != null) {
            if (node.isEnd()) {
                EndNode end = (EndNode) node;
                if (end.getObject() == parent) break;
            }
            node = node.getNext();
        }
        return node;
    }

    private Node findBranchRuleTarget(BranchRuleNode branchRule) {
        boolean isSpecial = branchRule.getBranchRule().isSpecial();
        ControlObject target = branchRule.getBranchRule().getTargetControlObject();
        Node node = branchRule.getNext();
        while (node.getNext() != null) {
            if (isSpecial) {
                if (node.isBranchRule()) {
                    BranchRuleNode branch = (BranchRuleNode) node;
                    if (branch.getBranchRule().getParent() == target) break;
                } else if (node.isEnd()) {
                    EndNode end = (EndNode) node;
                    if (end.getObject() == target) break;
                }
            } else {
                if (node.isStart()) {
                    StartNode start = (StartNode) node;
                    if (start.getObject() == target) break;
                }
            }
            node = node.getNext();
        }
        return node;
    }

    public boolean isFinished() {
        return ((AssessmentTest) start.getObject()).isFinished();
    }

    public AssessmentTest getTest() {
        AssessmentTest test = (AssessmentTest) start.getObject();
        logger.debug("Assessment test is {}.", test.getIdentifier());
        return test;
    }

    public TestPart getCurrentTestPart() {
        TestPart testPart = (current != null) ? current.getItemRef().getParentTestPart() : null;
        logger.debug("Current test part is {}.", (testPart != null) ? testPart.getIdentifier() : "NULL");
        return testPart;
    }

    public AssessmentItemRef getCurrentItemRef() {
        logger.debug("Current item ref is {} in node {}.", (current != null) ? current.getItemRef().getIdentifier() : "NULL", (current != null) ? current.getIndex() : "NULL");
        return (current != null) ? current.getItemRef() : null;
    }

    public boolean hasPrevItemRef(boolean includeFinished) {
        logger.debug("Has previous item ref requested. Include finished: {}", includeFinished);
        ItemRefNode node = findPrevItemRef(includeFinished);
        logger.info("Previous item ref is {} in node {}.", (node != null) ? node.getItemRef().getIdentifier() : "NULL", (node != null) ? node.getIndex() : "NULL");
        return node != null;
    }

    public AssessmentItemRef getPrevItemRef(boolean includeFinished) {
        logger.debug("Previous item ref requested. Include finished: {}", includeFinished);
        ItemRefNode node = findPrevItemRef(includeFinished);
        if (node != null) {
            long currentTime = ((AssessmentTest) start.getObject()).getTimer().getCurrentTime();
            current.getItemRef().getTimeRecord().exit(currentTime);
            node.getItemRef().getTimeRecord().enter(currentTime);
            current = node;
        }
        logger.info("Previous item ref is {} in node {}.", (node != null) ? node.getItemRef().getIdentifier() : "NULL", (node != null) ? node.getIndex() : "NULL");
        return (node != null) ? node.getItemRef() : null;
    }

    protected ItemRefNode findPrevItemRef(boolean includeFinished) {
        ItemRefNode node = null;
        if (current != null) node = current.getPrevItemRef();
        while (node != null) {
            if (!node.getItemRef().isFinished() && node.getItemRef().passMaximumTimeLimit()) break;
            if (includeFinished && node.getItemRef().getItemSessionControl().getAllowReview()) break;
            node = node.getPrevItemRef();
        }
        if (node != null) {
            if (node.getItemRef().getParentTestPart() != current.getItemRef().getParentTestPart()) node = null;
        }
        return node;
    }

    public boolean hasNextItemRef(boolean includeFinished) {
        logger.debug("Has next item ref requested. Include finished: {}", includeFinished);
        ItemRefNode node = null;
        if (current != null) {
            node = findNextItemRef(includeFinished);
            if (node != null) {
                if (node.getItemRef().getParentTestPart() != current.getItemRef().getParentTestPart()) node = null;
            }
        }
        logger.info("Next item ref is {} in node {}.", (node != null) ? node.getItemRef().getIdentifier() : "NULL", (node != null) ? node.getIndex() : "NULL");
        return node != null;
    }

    public AssessmentItemRef getNextItemRef(boolean includeFinished) {
        logger.debug("Next item ref requested. Include finished: {}", includeFinished);
        ItemRefNode node = findNextItemRef(includeFinished);
        long currentTime = ((AssessmentTest) start.getObject()).getTimer().getCurrentTime();
        if (current != null) {
            current.getItemRef().getTimeRecord().exit(currentTime);
            if (node == null || node.getItemRef().getParentTestPart() != current.getItemRef().getParentTestPart()) current.getItemRef().getParentTestPart().setFinished();
        }
        current = node;
        if (current != null) {
            current.getItemRef().getTimeRecord().enter(currentTime);
            current.getItemRef().setPresented();
        } else ((AssessmentTest) start.getObject()).setFinished();
        logger.info("Next item ref is {} in node {}.", (current != null) ? current.getItemRef().getIdentifier() : "NULL", (current != null) ? current.getIndex() : "NULL");
        AssessmentItemRef itemRef = (current != null) ? current.getItemRef() : null;
        if (itemRef != null && itemRef.getItem() != null) itemRef.getItem().initialize(itemRef.getTemplateDefaults());
        return itemRef;
    }

    private ItemRefNode findNextItemRef(boolean includeFinished) {
        if (isFinished()) return null;
        ItemRefNode node = null;
        if (current != null) {
            checkUnfinished(current.getItemRef());
            node = current.getNextItemRef();
            while (node != null) {
                if (!node.getItemRef().isFinished() && node.getItemRef().passMaximumTimeLimit()) break;
                if (includeFinished && node.getItemRef().getItemSessionControl().getAllowReview()) break;
                node = node.getNextItemRef();
            }
            if (node == null) node = getNextItemRef(current);
        } else node = getNextItemRef(start);
        if (node != null && node.getPrevItemRef() == null) node.setPrevItemRef(current);
        if (current != null && current.getNextItemRef() == null) current.setNextItemRef(node);
        return node;
    }

    private void checkUnfinished(AssessmentItemRef itemRef) {
        if (!itemRef.isFinished()) {
            TestPart testPart = itemRef.getParentTestPart();
            if (testPart.getNavigationMode() == NavigationMode.LINEAR && testPart.getSubmissionMode() == SubmissionMode.INDIVIDUAL) {
                logger.error("Cannot leave unfinished item ref {} in node {}.", itemRef.getIdentifier(), current.getIndex());
                throw new QTIItemFlowException(itemRef, "Cannot leave unfinished item.");
            }
        }
    }

    private ItemRefNode getNextItemRef(Node start) {
        ItemRefNode result = null;
        Node node = start;
        while (node.getNext() != null) {
            if (node.isJump()) {
                JumpNode jump = (JumpNode) node;
                node = jump.evaluate();
            } else node = node.getNext();
            if (node.isItemRef()) {
                result = (ItemRefNode) node;
                break;
            }
        }
        return result;
    }

    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
        Node node = start;
        while (node != null) {
            builder.append(node.toString());
            builder.append(XmlNode.NEW_LINE);
            node = node.getNext();
        }
        return builder.toString();
    }
}
