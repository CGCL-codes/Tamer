package freestyleLearningGroup.freestyleLearning.learningUnitViewManagers.checkUp.elementsContentsPanel;

import freestyleLearning.learningUnitViewAPI.FSLLearningUnitViewElementsManager;
import freestyleLearning.learningUnitViewAPI.FSLLearningUnitViewManager;
import freestyleLearning.learningUnitViewAPI.elementsContentsPanel.FSLAbstractLearningUnitViewElementContentPanel;
import freestyleLearning.learningUnitViewAPI.elementsContentsPanel.FSLAbstractLearningUnitViewElementsContentsPanel;
import freestyleLearning.learningUnitViewAPI.events.learningUnitEvent.FSLLearningUnitEventGenerator;
import freestyleLearning.learningUnitViewAPI.events.learningUnitViewEvent.FSLLearningUnitViewEvent;
import freestyleLearning.learningUnitViewAPI.events.learningUnitViewEvent.FSLLearningUnitViewVetoableAdapter;
import freestyleLearning.learningUnitViewAPI.util.FSLLearningUnitViewUtilities;
import freestyleLearningGroup.freestyleLearning.learningUnitViewManagers.checkUp.data.xmlBindingSubclasses.FLGCheckUpElement;
import freestyleLearningGroup.freestyleLearning.learningUnitViewManagers.checkUp.data.xmlBindingSubclasses.FLGCheckUpElementMultipleChoiceAnswer;
import freestyleLearningGroup.freestyleLearning.learningUnitViewManagers.checkUp.data.xmlBindingSubclasses.FLGCheckUpElementRelatorEndPoint;
import freestyleLearningGroup.freestyleLearning.learningUnitViewManagers.checkUp.data.xmlBindingSubclasses.FLGCheckUpElementRelatorStartPoint;
import freestyleLearningGroup.freestyleLearning.learningUnitViewManagers.checkUp.events.learningUnitViewEvent.FLGCheckUpEvent;

public class FLGCheckUpElementsContentsPanel extends FSLAbstractLearningUnitViewElementsContentsPanel {

    private boolean evaluationMode;

    private FLGCheckUpGapTextElementContentPanel gapTextElementContentPanel;

    private FLGCheckUpMultipleChoiceElementContentPanel multipleChoiceElementContentPanel;

    private FLGCheckUpRelatorElementContentPanel relatorElementContentPanel;

    private FLGCheckUpEvaluationManagerContentPanel evaluationManagerContentPanel;

    private boolean editMode;

    String learningUnitViewManagerId, activeCheckUpElementId;

    public void init(FSLLearningUnitViewManager learningUnitViewManager, FSLLearningUnitEventGenerator learningUnitEventGenerator, boolean editMode) {
        super.init(learningUnitViewManager, learningUnitEventGenerator, editMode);
        learningUnitViewManager.addLearningUnitViewListener(new FLGCheckUpElementsContentsPanel_LearningUnitViewAdapter());
        learningUnitViewManagerId = learningUnitViewManager.getLearningUnitViewManagerId();
        gapTextElementContentPanel = new FLGCheckUpGapTextElementContentPanel();
        gapTextElementContentPanel.init(learningUnitViewManager, learningUnitEventGenerator, editMode);
        multipleChoiceElementContentPanel = new FLGCheckUpMultipleChoiceElementContentPanel();
        multipleChoiceElementContentPanel.init(learningUnitViewManager, learningUnitEventGenerator, editMode);
        relatorElementContentPanel = new FLGCheckUpRelatorElementContentPanel();
        relatorElementContentPanel.init(learningUnitViewManager, learningUnitEventGenerator, editMode);
        evaluationManagerContentPanel = new FLGCheckUpEvaluationManagerContentPanel();
        evaluationManagerContentPanel.init(learningUnitViewManager, learningUnitEventGenerator, editMode);
        evaluationMode = false;
    }

    public void setLearningUnitViewElementsManager(FSLLearningUnitViewElementsManager learningUnitViewElementsManager) {
        gapTextElementContentPanel.setLearningUnitViewElementsManager(learningUnitViewElementsManager);
        multipleChoiceElementContentPanel.setLearningUnitViewElementsManager(learningUnitViewElementsManager);
        relatorElementContentPanel.setLearningUnitViewElementsManager(learningUnitViewElementsManager);
        evaluationManagerContentPanel.setLearningUnitViewElementsManager(learningUnitViewElementsManager);
        super.setLearningUnitViewElementsManager(learningUnitViewElementsManager);
    }

    public void updateUI() {
        if (gapTextElementContentPanel != null) gapTextElementContentPanel.updateUI();
        if (multipleChoiceElementContentPanel != null) multipleChoiceElementContentPanel.updateUI();
        if (relatorElementContentPanel != null) relatorElementContentPanel.updateUI();
        if (evaluationManagerContentPanel != null) evaluationManagerContentPanel.updateUI();
        super.updateUI();
    }

    protected FSLAbstractLearningUnitViewElementContentPanel getElementContentPanel(int index, String learningUnitViewElementId) {
        FLGCheckUpElement learningUnitViewElement = (FLGCheckUpElement) learningUnitViewElementsManager.getLearningUnitViewElement(learningUnitViewElementId, false);
        if (learningUnitViewElement != null) {
            if (evaluationMode) {
                return evaluationManagerContentPanel;
            } else {
                if (learningUnitViewElement.getFolder()) {
                    return null;
                }
                if (learningUnitViewElement.getType().equals(FLGCheckUpElement.ELEMENT_TYPE_GAPTEXT)) {
                    return gapTextElementContentPanel;
                }
                if (learningUnitViewElement.getType().equals(FLGCheckUpElement.ELEMENT_TYPE_MULTIPLECHOICE)) {
                    return multipleChoiceElementContentPanel;
                }
                if (learningUnitViewElement.getType().equals(FLGCheckUpElement.ELEMENT_TYPE_RELATOR)) {
                    return relatorElementContentPanel;
                }
            }
        }
        return null;
    }

    class FLGCheckUpElementsContentsPanel_LearningUnitViewAdapter extends FSLLearningUnitViewVetoableAdapter {

        public void learningUnitViewElementActivated(FSLLearningUnitViewEvent event) {
            activeCheckUpElementId = event.getActiveLearningUnitViewElementId();
        }

        public void learningUnitViewSpecificEventOccurred(FSLLearningUnitViewEvent event) {
            FLGCheckUpEvent checkUpEvent = (FLGCheckUpEvent) event;
            if (checkUpEvent.getEventSpecificType() == FLGCheckUpEvent.CHECKUP_EVALUATE_MODE_ENTERED) {
                evaluationMode = true;
                FSLLearningUnitViewEvent learningUnitEvent = FSLLearningUnitViewEvent.createElementActivatedEvent(learningUnitViewManagerId, activeCheckUpElementId, null, false);
                learningUnitViewManager.fireLearningUnitViewEvent(learningUnitEvent);
            }
            if (checkUpEvent.getEventSpecificType() == FLGCheckUpEvent.CHECKUP_EVALUATE_MODE_EXITED) {
                evaluationMode = false;
                FSLLearningUnitViewEvent learningUnitEvent = FSLLearningUnitViewEvent.createElementActivatedEvent(learningUnitViewManagerId, activeCheckUpElementId, null, false);
                learningUnitViewManager.fireLearningUnitViewEvent(learningUnitEvent);
            }
        }

        public void learningUnitViewElementLinksRemoved(FSLLearningUnitViewEvent event) {
            if (event.getLearningUnitViewManagerId().equals(learningUnitViewManager.getLearningUnitViewManagerId())) {
                FLGCheckUpElement element = (FLGCheckUpElement) learningUnitViewElementsManager.getLearningUnitViewElement(event.getLearningUnitViewElementId(), true);
                if (element.getGapText() != null && element.getGapText().getHtmlFormularFileName() != null) element.getGapText().setHtmlFormularFileName(FSLLearningUnitViewUtilities.updateExternalHtmlFilesOnLinksRemovedEvent(event, learningUnitViewElementsManager, element, element.getGapText().getHtmlFormularFileName(), FLGCheckUpElement.ELEMENT_TYPE_GAPTEXT, ".html"));
                if (element.getMultipleChoice() != null) {
                    String htmlFileName;
                    htmlFileName = element.getMultipleChoice().getQuestionHtmlFileName();
                    if (htmlFileName != null) element.getMultipleChoice().setQuestionHtmlFileName(FSLLearningUnitViewUtilities.updateExternalHtmlFilesOnLinksRemovedEvent(event, learningUnitViewElementsManager, element, htmlFileName, FLGCheckUpElement.ELEMENT_TYPE_MULTIPLECHOICE, ".html"));
                    for (int i = 0; i < element.getMultipleChoice().getMultipleChoiceAnswers().size(); i++) {
                        FLGCheckUpElementMultipleChoiceAnswer answer = (FLGCheckUpElementMultipleChoiceAnswer) element.getMultipleChoice().getMultipleChoiceAnswers().get(i);
                        if (answer.getHtmlFileName() != null) {
                            answer.setHtmlFileName(FSLLearningUnitViewUtilities.updateExternalHtmlFilesOnLinksRemovedEvent(event, learningUnitViewElementsManager, element, answer.getHtmlFileName(), FLGCheckUpElement.ELEMENT_TYPE_MULTIPLECHOICE, ".html"));
                        }
                    }
                }
                if (element.getRelator() != null) {
                    String htmlFileName;
                    htmlFileName = element.getRelator().getQuestionHtmlFileName();
                    if (htmlFileName != null) element.getRelator().setQuestionHtmlFileName(FSLLearningUnitViewUtilities.updateExternalHtmlFilesOnLinksRemovedEvent(event, learningUnitViewElementsManager, element, htmlFileName, FLGCheckUpElement.ELEMENT_TYPE_RELATOR, ".html"));
                    for (int i = 0; i < element.getRelator().getRelatorStartPoints().size(); i++) {
                        FLGCheckUpElementRelatorStartPoint startPoint = (FLGCheckUpElementRelatorStartPoint) element.getRelator().getRelatorStartPoints().get(i);
                        if (startPoint.getHtmlFileName() != null) {
                            startPoint.setHtmlFileName(FSLLearningUnitViewUtilities.updateExternalHtmlFilesOnLinksRemovedEvent(event, learningUnitViewElementsManager, element, startPoint.getHtmlFileName(), FLGCheckUpElement.ELEMENT_TYPE_RELATOR, ".html"));
                        }
                    }
                    for (int i = 0; i < element.getRelator().getRelatorEndPoints().size(); i++) {
                        FLGCheckUpElementRelatorEndPoint endPoint = (FLGCheckUpElementRelatorEndPoint) element.getRelator().getRelatorEndPoints().get(i);
                        if (endPoint.getHtmlFileName() != null) {
                            endPoint.setHtmlFileName(FSLLearningUnitViewUtilities.updateExternalHtmlFilesOnLinksRemovedEvent(event, learningUnitViewElementsManager, element, endPoint.getHtmlFileName(), FLGCheckUpElement.ELEMENT_TYPE_RELATOR, ".html"));
                        }
                    }
                }
            }
        }
    }
}
