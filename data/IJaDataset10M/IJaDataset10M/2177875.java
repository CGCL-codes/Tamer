package starcraft.gamemodel.states;

public class GameStateMachine implements IStateMachine {

    private final GameService game;

    private IState<GameService> state;

    private final IStateChangeListener<GameService> changeListener;

    public GameStateMachine(GameService game, IStateChangeListener<GameService> changeListener) {
        this.game = game;
        this.changeListener = changeListener;
        state = GameStates.InitializationPhase;
    }

    @Override
    public GameService getObject() {
        return game;
    }

    @Override
    public IState<?> getState() {
        return state;
    }

    @Override
    public void updateState() {
        for (IStateTransition<GameService> t : state.getTransitions()) {
            if (t.transitionConditionIsMet(game)) {
                if (t.transitionTo() == null) {
                    throw new IllegalStateException("Unable to transition to the next state. Current state is " + state);
                }
                IState<GameService> oldState = state;
                state = t.transitionTo();
                t.transitionHasTriggered(game);
                changeListener.stateChanged(oldState, state);
                updateState();
                break;
            }
        }
    }
}
