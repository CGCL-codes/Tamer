package es.eucm.eadventure.editor.control.tools.structurepanel;

import javax.swing.JTable;
import es.eucm.eadventure.common.auxiliar.ReportDialog;
import es.eucm.eadventure.common.data.chapter.Chapter;
import es.eucm.eadventure.editor.control.Controller;
import es.eucm.eadventure.editor.control.controllers.adaptation.AdaptationProfileDataControl;
import es.eucm.eadventure.editor.control.controllers.assessment.AssessmentProfileDataControl;
import es.eucm.eadventure.editor.control.controllers.general.ChapterDataControl;
import es.eucm.eadventure.editor.control.tools.Tool;
import es.eucm.eadventure.editor.gui.structurepanel.StructureElement;

public class RemoveElementTool extends Tool {

    private StructureElement element;

    private ChapterDataControl chapterDataControl;

    private Chapter chapter;

    private JTable table;

    public RemoveElementTool(JTable table, StructureElement element) {
        this.element = element;
        this.table = table;
        chapterDataControl = Controller.getInstance().getSelectedChapterDataControl();
        try {
            chapter = (Chapter) (((Chapter) chapterDataControl.getContent()).clone());
        } catch (Exception e) {
            ReportDialog.GenerateErrorReport(e, true, "Could not clone chapter");
        }
    }

    @Override
    public boolean canRedo() {
        return true;
    }

    @Override
    public boolean canUndo() {
        return (chapter != null);
    }

    @Override
    public boolean combine(Tool other) {
        return false;
    }

    @Override
    public boolean doTool() {
        if (element.delete(true)) {
            table.clearSelection();
            removeSelectedProfile();
            Controller.getInstance().updateStructure();
            return true;
        }
        return false;
    }

    @Override
    public boolean redoTool() {
        Controller.getInstance().replaceSelectedChapter((Chapter) chapterDataControl.getContent());
        Controller.getInstance().reloadData();
        return true;
    }

    @Override
    public boolean undoTool() {
        Controller.getInstance().replaceSelectedChapter(chapter);
        Controller.getInstance().reloadData();
        return true;
    }

    public void removeSelectedProfile() {
        if (element.getDataControl() instanceof AdaptationProfileDataControl) {
            if (Controller.getInstance().getSelectedChapterDataControl().getAdaptationName().equals(((AdaptationProfileDataControl) element.getDataControl()).getName())) ((Chapter) Controller.getInstance().getSelectedChapterDataControl().getContent()).setAdaptationName("");
        }
        if (element.getDataControl() instanceof AssessmentProfileDataControl) {
            if (Controller.getInstance().getSelectedChapterDataControl().getAssessmentName().equals(((AssessmentProfileDataControl) element.getDataControl()).getName())) ((Chapter) Controller.getInstance().getSelectedChapterDataControl().getContent()).setAssessmentName("");
        }
    }
}
