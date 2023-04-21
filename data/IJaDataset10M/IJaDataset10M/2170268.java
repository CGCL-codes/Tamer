package hokutonorogue.object;

import java.util.*;
import hokutonorogue.character.action.*;

/**
 * <p>Title: </p>
 *
 * <p>Description: </p>
 *
 * <p>Copyright: Copyright (c) 2007</p>
 *
 * <p>Company: </p>
 *
 * @author Alessio Carotenuto
 * @version 1.0
 */
public class Knife extends CutWeapon {

    public Knife() {
        this.name = "KNIFE";
        index = 94;
    }

    public int requiredConcentration() {
        return 0;
    }

    public int calculateWounds(AttackTarget target, int hitFactor) {
        int wounds = character.getStrenght().getValue() + (int) (hitFactor / 3);
        System.out.println(this.getName() + " inflict wounds before soaking: " + wounds);
        return wounds;
    }

    public float getMinRange() {
        return 1f;
    }

    public float getMaxRange() {
        return 1f;
    }

    public List<AttackReport> execute(Attack attack, AttackTarget target, List params) throws ExecutionException {
        List<AttackReport> ret = new ArrayList<AttackReport>();
        try {
            character.getCharacterSprite().punch();
            ret.add(attack.attack(target));
        } catch (InterruptedAttackException ex) {
        }
        return ret;
    }

    public boolean canExecute(AttackTarget target, List params) {
        return true;
    }
}
