package ring.effects.library;

import ring.effects.*;
import ring.mobiles.*;

public class HPChange extends EffectCreator {

    private static final long serialVersionUID = 1L;

    private int hpChange;

    public HPChange() {
        super();
    }

    public void effectLife(Affectable target) {
        if (!(target instanceof Mobile)) return;
        hpChange = super.params.getInt("amount");
        System.out.println("changing HP by " + hpChange);
        Mobile mob = (Mobile) target;
        mob.changeBonusHP(hpChange);
    }

    public void effectDeath(Affectable target) {
        if (!(target instanceof Mobile)) return;
        System.out.println("removing HP change of " + hpChange);
        Mobile mob = (Mobile) target;
        mob.changeBonusHP(hpChange * -1);
    }
}
