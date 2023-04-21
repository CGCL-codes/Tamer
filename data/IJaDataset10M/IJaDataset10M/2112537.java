package com.global360.sketchpadbpmn.actions;

import java.awt.event.ActionEvent;
import com.global360.sketchpadbpmn.SketchManager;
import com.global360.sketchpadbpmn.SketchSelectionManager;
import com.global360.sketchpadbpmn.SketchpadException;
import com.global360.sketchpadbpmn.documents.BPMNWorkflowDocument;
import com.global360.sketchpadbpmn.documents.GraphicFilter;
import com.global360.sketchpadbpmn.graphic.BPMNLaneGraphic;
import com.global360.sketchpadbpmn.graphic.BPMNPageGraphic;
import com.global360.sketchpadbpmn.graphic.SketchGraphic;
import com.global360.sketchpadbpmn.i18n.Messages;

/**
 * @author andya
 *
 */
public class SplitLaneAction extends SketchAction {

    /**
   * 
   */
    private static final long serialVersionUID = 1L;

    private boolean bSplitBelowOrRightContent = false;

    /**
   * 
   */
    public static SplitLaneAction make(SketchManager owner, BPMNLaneGraphic lane, boolean bSplitBelowOrRightContent) {
        SplitLaneAction result = null;
        String name;
        if (bSplitBelowOrRightContent) {
            name = Messages.getString("SplitLaneAction.0");
        } else {
            name = Messages.getString("SplitLaneAction.1");
        }
        result = new SplitLaneAction(owner, name, bSplitBelowOrRightContent);
        result.setOperand(lane);
        return result;
    }

    /**
   * @param owner
   * @param name
   */
    private SplitLaneAction(SketchManager owner, String name, boolean bSplitBelowOrRightContent) {
        super(owner, name);
        this.bSplitBelowOrRightContent = bSplitBelowOrRightContent;
    }

    public void actionPerformed(ActionEvent arg0) {
        try {
            SketchGraphic operand = getOperand();
            if (operand == null) {
                operand = getSelectionManager().getFirstItem();
            }
            if ((operand != null) && (operand instanceof BPMNLaneGraphic)) {
                getCommandManager().splitLane((BPMNLaneGraphic) operand, this.bSplitBelowOrRightContent);
            }
        } catch (SketchpadException e) {
            e.printStackTrace();
        }
    }

    public void setIsSelectionChanging(boolean bChanging) {
    }

    public boolean isSelectionChanging() {
        return false;
    }

    public void selectionChanged(SketchSelectionManager selection) {
    }

    public void pageChanged(BPMNPageGraphic pageGraphic) {
    }

    public void documentChanged(BPMNWorkflowDocument document) {
    }

    @Override
    public void filterChanged(GraphicFilter filter) {
    }
}
