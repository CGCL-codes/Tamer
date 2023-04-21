package ch.nostromo.tiffanys.game.pgn;

import ch.nostromo.tiffanys.game.GameException;

public class PGNFileIOExcpetion extends GameException {

    private static final long serialVersionUID = 1L;

    public PGNFileIOExcpetion() {
        super();
    }

    public PGNFileIOExcpetion(String msg) {
        super(msg);
    }

    public PGNFileIOExcpetion(Throwable throwable) {
        super(throwable);
    }

    public PGNFileIOExcpetion(String msg, Throwable throwable) {
        super(msg, throwable);
    }
}
