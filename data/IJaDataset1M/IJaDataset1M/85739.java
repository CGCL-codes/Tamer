package ssv.interaction.model;

public interface InteractionModelView {

    void addModelObserver(InteractionModelObserver modelObserver);

    void notifySequenceStart();

    void notifySequenceEnd();
}
