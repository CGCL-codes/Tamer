package inputs;

import game.Player;
import logic.States;

public class PlayerStateEvent {

    public InputState inputState;

    public boolean isAddState;

    public States state;

    public Player player;

    public PlayerStateEvent(InputState setInputState, boolean addState, States setState, Player setPlayer) {
        inputState = setInputState;
        isAddState = addState;
        state = setState;
        player = setPlayer;
    }
}
