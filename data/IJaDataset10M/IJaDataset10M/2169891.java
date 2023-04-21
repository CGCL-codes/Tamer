package org.perfectday.logicengine.model.minis.support.factory;

import org.perfectday.logicengine.combat.model.combatkeep.CloseCombatKeep;
import org.perfectday.logicengine.model.minis.Mini;
import org.perfectday.logicengine.model.minis.support.PassiveSupport;
import org.perfectday.logicengine.model.minis.support.Support;
import org.perfectday.logicengine.model.minis.support.SupportType;
import org.perfectday.logicengine.model.minis.support.modifiers.AtackerModifier;
import org.perfectday.logicengine.model.minis.support.modifiers.DefenseModifier;
import org.perfectday.logicengine.model.minis.support.modifiers.FireAtackModifier;

/**
 * Factoria de apoyos, todo mini capaza de hacer un apoyo tiene une referencia
 * a esta factoria y construlle el supor en función de las caracteristicas de
 * su apoyo
 * @author Miguel Angel Lopez Montellano ( alakat@gmail.com )
 */
public class SupportFactory {

    private static SupportFactory instance;

    private SupportFactory() {
    }

    public static SupportFactory getInstance() {
        if (instance == null) {
            instance = new SupportFactory();
        }
        return instance;
    }

    /**
     * Apoyo de escudo
     * @param mini
     * @return
     */
    public Support createShieldSupport(Mini mini) {
        return new PassiveSupport(SupportType.PASIVE_DEFENSIVE, new CloseCombatKeep(), false, mini, new DefenseModifier(new Integer(2)), " +2 Defense");
    }

    /**
     * 7
     * @param mini
     * @return
     */
    public Support createBladeSupport(Mini mini) {
        return new PassiveSupport(SupportType.PASIVE_OFENSIVE, new CloseCombatKeep(), false, mini, new AtackerModifier(new Integer(2)), " +2 Ataque");
    }

    /**
     * Support by Magican
     * @param mini
     * @return
     */
    public Support createFireMagicanSupport(Mini mini) {
        return new PassiveSupport(SupportType.PASIVE_OFENSIVE, new CloseCombatKeep(), false, mini, new FireAtackModifier(new Integer(new Double(mini.getMagicAfinity()).intValue())), " +" + new Double(mini.getMagicAfinity()).intValue() + " Fuerza");
    }
}
