package org.mitre.ocil.refimpl.gui.components;

import javax.swing.tree.DefaultMutableTreeNode;
import org.apache.log4j.Logger;
import gov.nist.scap.schema.ocil.x20.OperatorType;
import gov.nist.scap.schema.ocil.x20.QuestionTestActionType;
import org.mitre.ocil.refimpl.gui.common.UIUtilities;
import gov.nist.scap.schema.ocil.x20.QuestionnaireType;

/**
 * The UITreeNode class extends DefaultMutableTreeNode to support
 * nodes that contain either a Questionnaire or QuestionTestActionType.
 * 
 * @author mcasipe
 */
public class UITreeNode extends DefaultMutableTreeNode {

    private static Logger logger = Logger.getLogger(UITreeNode.class.getPackage().getName());

    private String text = "";

    /**
     * UITreeNode constructor.
     * 
     * @param qn
     */
    public UITreeNode(QuestionnaireType qn) {
        setUserObject(qn);
        setText();
    }

    /**
     * UITreeNode constructor.
     * 
     * @param qta
     */
    public UITreeNode(QuestionTestActionType qta) {
        setUserObject(qta);
        setText();
    }

    /**
     * This method returns a boolean indicating whether the user object is
     * a Questionnaire.
     * 
     * @return  boolean
     */
    public boolean isQuestionnaire() {
        return getUserObject() instanceof QuestionnaireType;
    }

    /**
     * This method returns a boolean indicating whether the user object is
     * a QuestionTestActionType.
     * 
     * @return  boolean
     */
    public boolean isQuestionTestActionType() {
        return getUserObject() instanceof QuestionTestActionType;
    }

    /**
     * This method returns the text to display in the tree.
     * 
     * @return  String
     */
    @Override
    public String toString() {
        return text;
    }

    /**
     * This method should be called if operation must be displayed.
     * 
     * @param negate
     * @param operation
     */
    public void addOperation(boolean negate, OperatorType.Enum operation) {
        String opInfo = ((negate) ? "NEGATE" : "") + ((negate && operation != null) ? ", " : "") + ((operation == null) ? "" : operation.toString());
        if (opInfo.length() > 0) {
            setText();
            text += " (" + opInfo + ")";
        }
    }

    /**
     * This private method is a helper for setting up the text to display
     * for this tree node.
     * 
     */
    private void setText() {
        if (isQuestionnaire()) {
            QuestionnaireType qn = (QuestionnaireType) getUserObject();
            text = UIUtilities.getInnerText(qn.getTitle(), qn.getId());
        } else if (isQuestionTestActionType()) {
            QuestionTestActionType qta = (QuestionTestActionType) getUserObject();
            text = UIUtilities.getInnerText(qta.getTitle(), qta.getId());
        } else logger.debug("Unsupported object: " + getUserObject().getClass().getName());
    }

    public String getId() {
        if (isQuestionnaire()) return ((QuestionnaireType) getUserObject()).getId(); else if (isQuestionTestActionType()) return ((QuestionTestActionType) getUserObject()).getId();
        return null;
    }
}
