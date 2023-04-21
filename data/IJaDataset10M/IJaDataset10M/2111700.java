package com.global360.sketchpadbpmn.actions;

import javax.swing.KeyStroke;
import com.global360.sketchpadbpmn.*;
import com.global360.sketchpadbpmn.documents.*;
import java.awt.event.*;
import com.global360.sketchpadbpmn.graphic.*;
import com.global360.sketchpadbpmn.i18n.Messages;

/**
 * <p>Title: </p>
 * <p>Description: </p>
 * <p>Copyright: Copyright (c) 2004</p>
 * <p>Company: </p>
 * @author not attributable
 * @version 1.0
 */
public class ZoomInAction extends SketchAction {

    private static final long serialVersionUID = 1L;

    public ZoomInAction(SketchManager owner) {
        super(owner, Messages.getString("ZoomInAction.0"), getImageIcon(Messages.getString("ZoomInAction.1")), KeyStroke.getKeyStroke(KeyEvent.VK_EQUALS, (InputEvent.SHIFT_MASK | InputEvent.CTRL_MASK)));
    }

    public void actionPerformed(ActionEvent e) {
        try {
            this.getOwner().getCommandManager().zoomIn();
        } catch (SketchpadException e1) {
            e1.printStackTrace();
        }
    }

    public void documentChanged(BPMNWorkflowDocument document) {
    }

    public boolean isSelectionChanging() {
        return false;
    }

    public void pageChanged(BPMNPageGraphic pageGraphic) {
        this.setEnabled(pageGraphic != null && this.getOwner().getCommandManager().canZoomIn());
    }

    public void selectionChanged(SketchSelectionManager selection) {
    }

    public void setIsSelectionChanging(boolean bChanging) {
    }

    @Override
    public void filterChanged(GraphicFilter filter) {
    }
}
