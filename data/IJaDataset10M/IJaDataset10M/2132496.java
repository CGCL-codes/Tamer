package org.vizzini.game.boardgame.chess.standardtoken;

import org.vizzini.game.IPosition;
import org.vizzini.game.ITeam;
import org.vizzini.game.IToken;
import org.vizzini.util.IProvider;

/**
 * Provides a rook provider.
 *
 * @author   Jeffrey M. Thompson
 * @version  v0.4
 * @since    v0.4
 */
public class RookProvider implements IProvider<IToken> {

    /** Serial version UID. */
    private static final long serialVersionUID = 1L;

    /** Exemplar. */
    private final Rook _exemplar;

    /**
     * Construct this object with the given parameter.
     *
     * @param  exemplar  Exemplar.
     *
     * @since  v0.4
     */
    public RookProvider(Rook exemplar) {
        _exemplar = exemplar;
    }

    /**
     * Construct this object with the given parameters.
     *
     * @param  position  Position.
     * @param  team      Team.
     *
     * @since  v0.4
     */
    public RookProvider(IPosition position, ITeam team) {
        _exemplar = new Rook(position, team);
    }

    /**
     * @see  org.vizzini.util.IProvider#create()
     */
    public IToken create() {
        IToken answer = new Rook(_exemplar.getPosition(), _exemplar.getTeam());
        return answer;
    }
}
