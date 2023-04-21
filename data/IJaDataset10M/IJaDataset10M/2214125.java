package org.hip.vif.core.util;

import java.sql.SQLException;
import java.util.Collection;
import java.util.Vector;
import org.hip.kernel.exc.VException;
import org.hip.kernel.workflow.WorkflowException;
import org.hip.vif.core.bom.Question;
import org.hip.vif.core.bom.QuestionHierarchyHome;
import org.hip.vif.core.bom.QuestionHome;
import org.hip.vif.core.bom.impl.BOMHelper;
import org.hip.vif.core.bom.impl.QuestionHierarchyVisitor;

/**
 * Iterator climbing the Questions hierarchy up to the root.
 * 
 * @author Benno Luthiger
 * Created on Feb 15, 2004
 */
public class QuestionTreeIterator {

    private Long startID = null;

    private Collection<Long> memory = new Vector<Long>();

    /**
	 * QuestionTreeIterator constuctor
	 */
    public QuestionTreeIterator() {
        super();
    }

    /**
	 * QuestionTreeIterator constuctor with specified ID of the node the iterator has to start.
	 * 
	 * @param inStartID Long
	 */
    public QuestionTreeIterator(Long inStartID) {
        super();
        startID = inStartID;
    }

    /**
	 * Starts the iteration at the specified node id.
	 * 
	 * @param inStartID Long ID of the node the iteration starts.
	 * @param inStartHere boolean true if the iterator has to process the start node too, false if processing begins at parent node.
	 * @param inVisitor DomainObjectVisitor used for processing the iterated nodes.
	 * @throws VException
	 * @throws SQLException
	 */
    public void start(Long inStartID, boolean inStartHere, QuestionHierarchyVisitor inVisitor) throws VException, SQLException {
        startID = inStartID;
        start(inStartHere, inVisitor);
    }

    /**
	 * Starts the iteration.
	 * 
	 * @param inStartHere boolean true if the iterator has to process the start node too, false if processing begins at parent node.
	 * @param inVisitor DomainObjectVisitor used for processing the iterated nodes.
	 * @throws VException
	 * @throws SQLException
	 */
    public void start(boolean inStartHere, QuestionHierarchyVisitor inVisitor) throws VException, SQLException {
        if (startID == null) {
            return;
        }
        Long lCurrent = startID;
        if (inStartHere) {
            if (iteratedNodeBefore(lCurrent)) return;
            QuestionHierarchyEntry lQuestion = (QuestionHierarchyEntry) ((QuestionHome) BOMHelper.getQuestionHome()).getQuestion(lCurrent.toString());
            lQuestion.accept(inVisitor);
            memory.add(lCurrent);
        }
        QuestionHierarchyHome lHierarchyHome = (QuestionHierarchyHome) BOMHelper.getQuestionHierarchyHome();
        while (lHierarchyHome.hasParent(lCurrent)) {
            Question lQuestion = lHierarchyHome.getParentQuestion(lCurrent.toString());
            lCurrent = new Long(lQuestion.get(QuestionHome.KEY_ID).toString());
            if (iteratedNodeBefore(lCurrent)) return;
            ((QuestionHierarchyEntry) lQuestion).accept(inVisitor);
            memory.add(lCurrent);
        }
    }

    /**
	 * Starts the iteration using INodeCheckedProcessor.
	 * This visitor doesn't visit the questions found, instead, it processes them.
	 * 
	 * @param inStartHere boolean true if the iterator has to process the start node too, false if processing begins at parent node.
	 * @param inVisitor IQuestionLevelVisitor
	 * @return Long ID of the last node processed, <code>null</code> if no node processed.
	 * @throws WorkflowException
	 * @throws VException
	 * @throws SQLException
	 */
    public Long start(boolean inStartHere, INodeCheckedProcessor inVisitor) throws WorkflowException, VException, SQLException {
        Long lPreceding = null;
        if (startID == null) {
            return lPreceding;
        }
        Long lCurrent = startID;
        if (inStartHere) {
            if (!inVisitor.checkPreCondition(lCurrent)) return lPreceding;
            lPreceding = lCurrent;
            inVisitor.doAction(lCurrent);
        }
        QuestionHierarchyHome lHierarchyHome = (QuestionHierarchyHome) BOMHelper.getQuestionHierarchyHome();
        while (lHierarchyHome.hasParent(lCurrent)) {
            lCurrent = new Long(lHierarchyHome.getParent(lCurrent.toString()).get(QuestionHierarchyHome.KEY_PARENT_ID).toString());
            if (!inVisitor.checkPreCondition(lCurrent)) return lPreceding;
            lPreceding = lCurrent;
            inVisitor.doAction(lCurrent);
        }
        return lPreceding;
    }

    private boolean iteratedNodeBefore(Long inIDtoTest) {
        return memory.contains(inIDtoTest);
    }
}
