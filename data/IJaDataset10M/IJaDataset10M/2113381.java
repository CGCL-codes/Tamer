package model.co;

import controller.COPowerManager;
import controller.ICOCombatManager;
import controller.ICOEventManager;

/**
 * A CO in the game.
 * 
 * @21 juil. 2010
 * @author Gronowski St�phane stephane.gronowski@gmail.com
 */
public interface ICO {

    /**
     * Return the {@link ICOEventManager} of the CO.
     * 
     * @return the {@link ICOEventManager} of the CO.
     */
    public ICOEventManager getEventManager();

    /**
     * Return the {@link ICOCombatManager} of the CO.
     * 
     * @return the {@link ICOCombatManager} of the CO.
     */
    public ICOCombatManager getCombatManager();

    /**
     * Return the {@link COPowerManager}.
     * 
     * @return the {@link COPowerManager}
     */
    public COPowerManager getPowerManager();

    /**
     * Return the {@link COId} of the CO.
     * 
     * @return the {@link COId} of the CO
     */
    public COId getCOId();

    /**
     * Return the {@link ICOUnitFactory}.
     * 
     * @return the {@link ICOUnitFactory}
     */
    public ICOUnitFactory getUnitFactory();
}
