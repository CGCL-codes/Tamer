package freestyleLearningGroup.freestyleLearning.learningUnitViewManagers.mediaPool.elementsContentsPanel;

import freestyleLearning.learningUnitViewAPI.*;
import freestyleLearning.learningUnitViewAPI.elementsContentsPanel.*;
import freestyleLearning.learningUnitViewAPI.events.learningUnitEvent.*;
import freestyleLearning.learningUnitViewAPI.events.learningUnitViewEvent.*;
import freestyleLearningGroup.freestyleLearning.learningUnitViewManagers.mediaPool.data.xmlBindingSubclasses.*;

public class FLGMediaPoolElementsContentsPanel extends FSLAbstractLearningUnitViewElementsContentsPanel {

    private FLGMediaPoolPictureElementContentPanel[] pictureElementContentPanels;

    private FLGMediaPoolPDFElementContentPanel pdfElementContentPanel;

    private FLGMediaPoolVideoElementContentPanel videoElementContentPanel;

    private FLGMediaPoolAudioElementContentPanel audioElementContentPanel;

    public void init(FSLLearningUnitViewManager learningUnitViewManager, FSLLearningUnitEventGenerator learningUnitEventGenerator, boolean editMode) {
        super.init(learningUnitViewManager, learningUnitEventGenerator, editMode);
        pictureElementContentPanels = new FLGMediaPoolPictureElementContentPanel[2];
        pictureElementContentPanels[0] = new FLGMediaPoolPictureElementContentPanel();
        pictureElementContentPanels[1] = new FLGMediaPoolPictureElementContentPanel();
        for (int i = 0; i < 2; i++) {
            pictureElementContentPanels[i].init(learningUnitViewManager, learningUnitEventGenerator, editMode);
        }
        videoElementContentPanel = new FLGMediaPoolVideoElementContentPanel();
        videoElementContentPanel.init(learningUnitViewManager, learningUnitEventGenerator, editMode);
        audioElementContentPanel = new FLGMediaPoolAudioElementContentPanel();
        audioElementContentPanel.init(learningUnitViewManager, learningUnitEventGenerator, editMode);
        pdfElementContentPanel = new FLGMediaPoolPDFElementContentPanel();
        pdfElementContentPanel.init(learningUnitViewManager, learningUnitEventGenerator, editMode);
    }

    public void setLearningUnitViewElementsManager(FSLLearningUnitViewElementsManager learningUnitViewElementsManager) {
        for (int i = 0; i < 2; i++) {
            pictureElementContentPanels[i].setLearningUnitViewElementsManager(learningUnitViewElementsManager);
        }
        videoElementContentPanel.setLearningUnitViewElementsManager(learningUnitViewElementsManager);
        audioElementContentPanel.setLearningUnitViewElementsManager(learningUnitViewElementsManager);
        pdfElementContentPanel.setLearningUnitViewElementsManager(learningUnitViewElementsManager);
        super.setLearningUnitViewElementsManager(learningUnitViewElementsManager);
    }

    public void updateUI() {
        for (int i = 0; i < 2; i++) {
            if (pictureElementContentPanels != null) pictureElementContentPanels[i].updateUI();
        }
        if (videoElementContentPanel != null) videoElementContentPanel.updateUI();
        if (audioElementContentPanel != null) audioElementContentPanel.updateUI();
        if (pdfElementContentPanel != null) pdfElementContentPanel.updateUI();
        super.updateUI();
    }

    protected FSLAbstractLearningUnitViewElementContentPanel getElementContentPanel(int index, String learningUnitViewElementId) {
        FLGMediaPoolElement mediaPoolElement = (FLGMediaPoolElement) learningUnitViewElementsManager.getLearningUnitViewElement(learningUnitViewElementId, false);
        if (mediaPoolElement != null) {
            if (mediaPoolElement.getFolder()) {
                return null;
            } else {
                if (mediaPoolElement.getType().equals("picture")) return pictureElementContentPanels[index];
                if (mediaPoolElement.getType().equals("pdf")) return pdfElementContentPanel;
                if (mediaPoolElement.getType().equals("video")) return videoElementContentPanel;
                if (mediaPoolElement.getType().equals("audio")) return audioElementContentPanel;
            }
        }
        return null;
    }

    public void saveUserChanges() {
        super.saveUserChanges();
    }
}
