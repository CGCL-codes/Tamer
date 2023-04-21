package ch.nostromo.tiffanys.game.player;

import ch.nostromo.tiffanys.game.GameControllerException;

public class PlayerException extends GameControllerException {

    private static final long serialVersionUID = 1L;

    public PlayerException() {
        super();
    }

    public PlayerException(String msg) {
        super(msg);
    }

    public PlayerException(Throwable throwable) {
        super(throwable);
    }

    public PlayerException(String msg, Throwable throwable) {
        super(msg, throwable);
    }
}
