package com.global360.sketchpadbpmn.actions;

import java.awt.event.ActionEvent;
import java.awt.event.InputEvent;
import java.awt.event.KeyEvent;
import java.awt.geom.Point2D;
import javax.swing.KeyStroke;
import com.global360.sketchpadbpmn.SketchManager;
import com.global360.sketchpadbpmn.SketchSelectionManager;
import com.global360.sketchpadbpmn.SketchpadException;
import com.global360.sketchpadbpmn.documents.BPMNWorkflowDocument;
import com.global360.sketchpadbpmn.documents.GraphicFilter;
import com.global360.sketchpadbpmn.graphic.BPMNPageGraphic;
import com.global360.sketchpadbpmn.i18n.Messages;

/**
 * <p>Title: </p>
 * <p>Description: </p>
 * <p>Copyright: Copyright (c) 2004</p>
 * <p>Company: </p>
 * @author not attributable
 * @version 1.0
 */
public class NudgeRightFineAction extends SketchAction {

    private static final long serialVersionUID = 1L;

    public NudgeRightFineAction(SketchManager owner) {
        super(owner, Messages.getString("NudgeRightFineAction.0"), KeyStroke.getKeyStroke(KeyEvent.VK_RIGHT, (InputEvent.CTRL_MASK)));
    }

    public void actionPerformed(ActionEvent e) {
        try {
            int delta = this.getOwner().getCommandManager().getDisplaceFineDelta();
            this.getOwner().getCommandManager().displaceSelectionForDocument(new Point2D.Double(delta, 0), null);
        } catch (SketchpadException e1) {
            e1.printStackTrace();
        }
    }

    public void selectionChanged(SketchSelectionManager selection) {
        this.setEnabled((!selection.isEmpty() && !selection.isPage()) ? true : false);
    }

    public void setIsSelectionChanging(boolean bChanging) {
    }

    public boolean isSelectionChanging() {
        throw new java.lang.UnsupportedOperationException("Method isSelectionChanging() not yet implemented.");
    }

    public void pageChanged(BPMNPageGraphic pageGraphic) {
    }

    public void documentChanged(BPMNWorkflowDocument document) {
    }

    @Override
    public void filterChanged(GraphicFilter filter) {
    }
}
