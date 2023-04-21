package megamek.common.weapons;

import megamek.common.IGame;
import megamek.common.ToHitData;
import megamek.common.actions.WeaponAttackAction;
import megamek.server.Server;

/**
 * @author Jay Lawson
 */
public class BarracudaTHandler extends TeleMissileHandler {

    /**
     * 
     */
    private static final long serialVersionUID = -2536312899803153911L;

    /**
     * @param t
     * @param w
     * @param g
     * @param s
     */
    public BarracudaTHandler(ToHitData t, WeaponAttackAction w, IGame g, Server s) {
        super(t, w, g, s);
    }

    @Override
    protected int getCapMisMod() {
        return 11;
    }
}
