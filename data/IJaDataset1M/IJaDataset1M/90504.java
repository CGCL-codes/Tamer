package com.global360.sketchpadbpmn.actions;

import java.awt.event.ActionEvent;
import com.global360.sketchpadbpmn.SketchManager;
import com.global360.sketchpadbpmn.SketchSelectionManager;
import com.global360.sketchpadbpmn.documents.BPMNWorkflowDocument;
import com.global360.sketchpadbpmn.documents.GraphicFilter;
import com.global360.sketchpadbpmn.graphic.BPMNPageGraphic;

/**
 * @author andya
 *
 */
public class TestFlagAction extends SketchAction {

    private static final long serialVersionUID = 1L;

    boolean state = false;

    /**
   * @param owner
   * @param name
   */
    public TestFlagAction(SketchManager owner, String name) {
        super(owner, name);
        this.setMenuItemHasCheckBox(true);
    }

    public void actionPerformed(ActionEvent arg0) {
        this.state = !this.state;
        this.setChecked(this.state);
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

    /**
   * @return Returns the state.
   */
    public boolean getState() {
        return this.state;
    }

    /**
   * @param state The state to set.
   */
    public void setState(boolean state) {
        this.state = state;
    }

    @Override
    public void filterChanged(GraphicFilter filter) {
    }
}
