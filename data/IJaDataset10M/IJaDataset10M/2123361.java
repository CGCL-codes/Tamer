package su.msk.dunno.blame.decisions;

import su.msk.dunno.blame.prototypes.ADecision;
import su.msk.dunno.blame.prototypes.ALiving;

public class OpenWeapon extends ADecision {

    public OpenWeapon(ALiving al) {
        super(al);
    }

    @Override
    public void doAction(int actionMoment) {
        al.getWeapon().process();
        wasExecuted = true;
    }
}
