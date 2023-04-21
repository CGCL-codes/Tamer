package org.vizzini.example.pong;

import org.vizzini.game.ITeam;
import org.vizzini.game.IToken;
import org.vizzini.game.IntegerPosition;
import org.vizzini.util.IProvider;
import java.awt.Dimension;

/**
 * Provides a ball provider.
 *
 * @author   Jeffrey M. Thompson
 * @version  v0.4
 * @since    v0.4
 */
public class PaddleProvider implements IProvider<IToken> {

    /** Serial version UID. */
    private static final long serialVersionUID = 1L;

    /** Exemplar. */
    private final Paddle _exemplar;

    /**
     * Construct this object with the given parameters.
     *
     * @param  position   Position.
     * @param  name       Name.
     * @param  value      Value.
     * @param  dimension  Dimension.
     * @param  speed      Speed.
     * @param  team       Team.
     *
     * @since  v0.4
     */
    public PaddleProvider(IntegerPosition position, String name, int value, Dimension dimension, int speed, ITeam team) {
        _exemplar = new Paddle(position, name, value, dimension, speed);
        _exemplar.setTeam(team);
    }

    /**
     * @see  org.vizzini.util.IProvider#create()
     */
    public IToken create() {
        Paddle answer = new Paddle(_exemplar.getPosition(), _exemplar.getName(), _exemplar.getValue(), _exemplar.getDimension(), _exemplar.getSpeed());
        answer.setTeam(_exemplar.getTeam());
        return answer;
    }
}
