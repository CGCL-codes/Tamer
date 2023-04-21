package megamek.server.victory;

import java.io.Serializable;
import java.util.HashMap;
import megamek.common.IGame;

/**
 * implementation of a filter which will wait until the
 * game.gameTimerIsExpired() is true or option "check_victory" is set before
 * returning whatever the given victory returns. otherwise returns
 * SimpleNoResult
 */
public class CheckVictory implements Victory, Serializable {

    /**
     * 
     */
    private static final long serialVersionUID = -9146210812483189565L;

    protected Victory v;

    public CheckVictory(Victory v) {
        this.v = v;
        assert (v != null);
    }

    public Victory.Result victory(IGame game, HashMap<String, Object> ctx) {
        Victory.Result ret = v.victory(game, ctx);
        if (!game.gameTimerIsExpired() && !game.getOptions().booleanOption("check_victory")) {
            return new SimpleNoResult();
        }
        return ret;
    }
}
