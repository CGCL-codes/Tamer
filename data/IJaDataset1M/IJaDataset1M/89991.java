package megamek.common.weapons;

import megamek.common.IGame;
import megamek.common.ToHitData;
import megamek.common.actions.WeaponAttackAction;
import megamek.server.Server;

/**
 * @author Jay Lawson
 */
public class SwordfishHandler extends AmmoWeaponHandler {

    /**
     * 
     */
    private static final long serialVersionUID = -2536312899903153911L;

    /**
     * @param t
     * @param w
     * @param g
     * @param s
     */
    public SwordfishHandler(ToHitData t, WeaponAttackAction w, IGame g, Server s) {
        super(t, w, g, s);
    }

    @Override
    protected int getCapMisMod() {
        return 11;
    }
}
