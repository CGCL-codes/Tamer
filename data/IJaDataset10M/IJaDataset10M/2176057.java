package model.co;

import model.Color;
import model.GameData;
import model.unit.Unit;
import controller.DefaultCOPowerManager;
import controller.DefaultCombatManager;
import controller.DefaultEventManager;

/**
 * A {@link ICO} factory.
 * 
 * @28 juil. 2010
 * @author Gronowski St�phane stephane.gronowski@gmail.com
 */
public class COFactory {

    /**
     * Create a {@link ICO} based on the {@link COId}
     * 
     * @param co
     *            the {@link COId}
     * @param data
     *            the {@link GameData}
     * @param color
     *            {@link Color} used to create the {@link Unit}.
     * @return the Co
     */
    public ICO createCO(COId co, GameData data, Color color) {
        ICO res = null;
        switch(co) {
            case ZAKK:
                res = new CO(co, new DefaultCombatManager(), new DefaultEventManager(data), new DefaultCOPowerManager(co.getFirstPower(), co.getSecondPower(), data), new DefaultCOUnitFactorie(color));
                break;
        }
        return res;
    }
}
