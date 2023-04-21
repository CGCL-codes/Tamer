package freestyleLearningGroup.freestyleLearning.learningUnitViewManagers.learningByDoing.statusPanel;

import freestyleLearning.learningUnitViewAPI.FSLLearningUnitViewManager;
import freestyleLearning.learningUnitViewAPI.events.learningUnitEvent.FSLLearningUnitEventGenerator;
import freestyleLearning.learningUnitViewAPI.events.learningUnitViewEvent.FSLLearningUnitViewAdapter;
import freestyleLearning.learningUnitViewAPI.events.learningUnitViewEvent.FSLLearningUnitViewEvent;
import freestyleLearning.learningUnitViewAPI.statusPanel.FSLAbstractLearningUnitViewStatusPanel;
import freestyleLearningGroup.freestyleLearning.learningUnitViewManagers.learningByDoing.data.xmlBindingSubclasses.FLGLearningByDoingElement;
import freestyleLearningGroup.independent.util.FLGInternationalization;

public class FLGLearningByDoingStatusPanel extends FSLAbstractLearningUnitViewStatusPanel {

    private FLGInternationalization internationalization;

    public FLGLearningByDoingStatusPanel() {
        internationalization = new FLGInternationalization("freestyleLearningGroup.freestyleLearning.learningUnitViewManagers.learningByDoing.statusPanel.internationalization", getClass().getClassLoader());
    }

    public void init(FSLLearningUnitViewManager learningUnitViewManager, FSLLearningUnitEventGenerator learningUnitEventGenerator, boolean editMode) {
        super.init(learningUnitViewManager, learningUnitEventGenerator, editMode);
        learningUnitViewManager.addLearningUnitViewListener(new FLGLearningByDoingStatusPanel_LearningUnitViewAdapter());
    }

    public void buildDependentUI() {
        if (activeLearningUnitViewElementId == null) setText(internationalization.getString("text.welcome")); else {
            setText(internationalization.getString("text.hint"));
        }
    }

    class FLGLearningByDoingStatusPanel_LearningUnitViewAdapter extends FSLLearningUnitViewAdapter {

        public void learningUnitViewElementActivated(FSLLearningUnitViewEvent event) {
            FLGLearningByDoingElement learningUnitViewElement = (FLGLearningByDoingElement) learningUnitViewElementsManager.getLearningUnitViewElement(event.getActiveLearningUnitViewElementId(), false);
            if (learningUnitViewElement == null || learningUnitViewElement.getFolder()) {
                setText(internationalization.getString("text.welcome"));
            } else {
                setText(internationalization.getString("text.hint"));
            }
        }
    }
}
