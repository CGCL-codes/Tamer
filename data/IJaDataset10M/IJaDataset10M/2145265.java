package freestyleLearningGroup.freestyleLearning.learningUnitViewManagers.textStudy.data.xmlBindingSubclasses;

import java.util.Date;
import freestyleLearning.learningUnitViewAPI.FSLLearningUnitViewElement;
import freestyleLearning.learningUnitViewAPI.FSLLearningUnitViewElementLink;
import freestyleLearning.learningUnitViewAPI.FSLLearningUnitViewElementsManager;
import freestyleLearningGroup.freestyleLearning.learningUnitViewManagers.textStudy.data.xmlBinding.ViewElement;
import freestyleLearningGroup.independent.gui.FLGHtmlUtilities;

public class FLGTextStudyElement extends ViewElement implements FSLLearningUnitViewElement {

    public static String ELEMENT_TYPE_FOLDER = "folder";

    public static String ELEMENT_TYPE_TEXT = "text";

    private boolean modified;

    public boolean isModified() {
        return modified;
    }

    public void setModified(boolean modified) {
        this.modified = modified;
    }

    public FSLLearningUnitViewElement deepCopy() {
        FLGTextStudyElement copy = new FLGTextStudyElement();
        FSLLearningUnitViewElementsManager.copyLearningUnitViewElement(this, copy);
        copy.setHtmlFileName(getHtmlFileName());
        return copy;
    }

    public String[] getLearningUnitViewElementExternalFilesRelativePaths(FSLLearningUnitViewElementsManager learningUnitViewElementsManager) {
        if (!getFolder() && getHtmlFileName() != null) {
            return FLGHtmlUtilities.getAllRelativeFileNamesToHtmlFile(getHtmlFileName(), learningUnitViewElementsManager.resolveRelativeFileName(getHtmlFileName(), this));
        } else return null;
    }

    public FSLLearningUnitViewElementLink getLearningUnitViewElementLink(String learningUnitViewElementLinkId) {
        for (int i = 0; i < getLearningUnitViewElementLinks().size(); i++) {
            FSLLearningUnitViewElementLink learningUnitViewElementLink = (FSLLearningUnitViewElementLink) getLearningUnitViewElementLinks().get(i);
            if (learningUnitViewElementLink.getId().equals(learningUnitViewElementLinkId)) return learningUnitViewElementLink;
        }
        return null;
    }

    public FSLLearningUnitViewElementLink addNewLearningUnitViewElementLink() {
        FLGTextStudyElementLink learningUnitViewElementLink = new FLGTextStudyElementLink();
        learningUnitViewElementLink.emptyLearningUnitViewElementLinkTargets();
        return FSLLearningUnitViewElementsManager.addLearningUnitViewElementLink(learningUnitViewElementLink, this);
    }

    public void setType(String type) {
        if (!type.equals(getType())) {
            this.emptyLearningUnitViewElementLinks();
            if (type.equals(ELEMENT_TYPE_FOLDER)) {
                this.setHtmlFileName(null);
            }
            super.setType(type);
        }
    }
}
