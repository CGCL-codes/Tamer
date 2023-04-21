package hokutonorogue.object;

import hokutonorogue.character.*;
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
public class AttackScroll extends AbstractScroll {

    private Attack attack = null;

    public AttackScroll(Attack attack) {
        super();
        this.attack = attack;
        this.name = attack.getName() + " SCROLL";
        index = 101;
    }

    /**
     * canBeUseOn
     *
     * @param actionTarget ActionTarget
     * @return boolean
     * @todo Implement this hokutonorogue.object.UsableHokutoObject method
     */
    public boolean _canBeUseOn(CharacterModel user, ActionTarget actionTarget) {
        boolean ret = false;
        if (actionTarget instanceof CharacterModel) {
            CharacterModel target = (CharacterModel) actionTarget;
            if (!target.knowsAttack(attack)) {
                ret = true;
            }
        }
        return ret;
    }

    /**
     * useOn
     *
     * @param actionTarget ActionTarget
     * @todo Implement this hokutonorogue.object.UsableHokutoObject method
     */
    public boolean useOn(CharacterModel user, ActionTarget actionTarget) {
        CharacterModel target = (CharacterModel) actionTarget;
        target.learnAttack(attack);
        consumed = true;
        return true;
    }

    public static AttackScroll random() {
        return new AttackScroll(Attack.Attacks.randomNoBase().attack());
    }

    public FightingStyle getStyle() {
        return attack.getStyle();
    }
}
