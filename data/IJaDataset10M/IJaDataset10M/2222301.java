package ch.nostromo.tiffanys.engines;

import ch.nostromo.tiffanys.game.GameControllerException;

public class EngineException extends GameControllerException {

    private static final long serialVersionUID = 1L;

    public EngineException() {
        super();
    }

    public EngineException(String msg) {
        super(msg);
    }

    public EngineException(Throwable throwable) {
        super(throwable);
    }

    public EngineException(String msg, Throwable throwable) {
        super(msg, throwable);
    }
}
