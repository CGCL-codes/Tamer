package ru.amse.tsyganov.jumleditor.commands;

import ru.amse.tsyganov.jumleditor.model.State;
import ru.amse.tsyganov.jumleditor.model.Transition;
import ru.amse.tsyganov.jumleditor.view.StateView;
import ru.amse.tsyganov.jumleditor.view.TransitionView;

class NewTransitionCommand extends Command {

    private final StateView<? extends State> sourceStateView;

    private final StateView<? extends State> targetStateView;

    private TransitionView<Transition> transitionView;

    private Transition transition;

    public NewTransitionCommand(StateView<? extends State> sourceStateView, StateView<? extends State> targetStateView) {
        this.sourceStateView = sourceStateView;
        this.targetStateView = targetStateView;
    }

    @Override
    public void execute() {
        if (transition == null) {
            transition = new Transition(sourceStateView.getModel().getStateVertex(), targetStateView.getModel().getStateVertex());
            transitionView = new TransitionView<Transition>(transition, sourceStateView.getStateVertex(), targetStateView.getStateVertex());
        } else {
            transition.setSource(sourceStateView.getModel().getStateVertex());
            transition.setTarget(targetStateView.getModel().getStateVertex());
            transitionView.setSource(sourceStateView.getStateVertex());
            transitionView.setTarget(targetStateView.getStateVertex());
        }
    }

    @Override
    public void unexecute() {
        transitionView.delete();
        transition.delete();
    }
}
