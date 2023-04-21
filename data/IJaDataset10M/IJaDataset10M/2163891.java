package freestyleLearning.learningUnitViewAPI.events.learningUnitViewEvent;

public class FSLLearningUnitViewVetoableEvent extends FSLLearningUnitViewEvent {

    public static final int ELEMENTS_REMOVING = 103;

    public static final int ELEMENT_ACTIVATING = 105;

    public static final int VIEW_DEACTIVATING = 107;

    public static final int VIEW_SPECIFIC_EVENT_OCCURRING = 110;

    protected boolean veto;

    public boolean isVeto() {
        return veto;
    }

    public void setVeto() {
        veto = true;
    }

    public static FSLLearningUnitViewVetoableEvent createElementActivatingEvent(String learningUnitViewManagerId, String activeLearningUnitViewElementId, String secondaryActiveLearningUnitViewElementId, boolean elementsSwitchedOnly) {
        FSLLearningUnitViewVetoableEvent event = new FSLLearningUnitViewVetoableEvent();
        event.eventType = ELEMENT_ACTIVATING;
        event.learningUnitViewManagerId = learningUnitViewManagerId;
        event.activeLearningUnitViewElementId = activeLearningUnitViewElementId;
        event.secondaryActiveLearningUnitViewElementId = secondaryActiveLearningUnitViewElementId;
        event.elementsSwitchedOnly = elementsSwitchedOnly;
        return event;
    }

    public static FSLLearningUnitViewVetoableEvent createElementsRemovingEvent(String learningUnitViewManagerId, String[] learningUnitViewElementIds) {
        FSLLearningUnitViewVetoableEvent event = new FSLLearningUnitViewVetoableEvent();
        event.eventType = ELEMENTS_REMOVING;
        event.learningUnitViewManagerId = learningUnitViewManagerId;
        event.learningUnitViewElementIds = learningUnitViewElementIds;
        return event;
    }

    public static FSLLearningUnitViewVetoableEvent createViewDeactivatingEvent() {
        FSLLearningUnitViewVetoableEvent event = new FSLLearningUnitViewVetoableEvent();
        event.eventType = VIEW_DEACTIVATING;
        return event;
    }
}
