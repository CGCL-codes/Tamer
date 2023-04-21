package de.sambalmueslie.ds.helptool.units;

import de.sambalmueslie.ds.helptool.misc.Ressources;

/**
 * class for an Sword unit.
 * 
 * @author Sambalmueslie
 * 
 */
public class Sword extends DSUnit {

    /** the attack strength. */
    private static final int ATTACK = 25;

    /** the defense strength. */
    private static final int DEFENSE = 50;

    /** the defense against archer. */
    private static final int DEFENSE_ARCHER = 40;

    /** the defense against cavalry. */
    private static final int DEFENSE_CAVALRY = 15;

    /** the goods the unit can carry. */
    private static final int GOODS = 15;

    /** the population the unit use. */
    private static final int POPULATION = 1;

    /** the iron resources the unit need to build. */
    private static final int RES_IRON = 70;

    /** the mud resources the unit need to build. */
    private static final int RES_MUD = 30;

    /** the wood resources the unit need to build. */
    private static final int RES_WOOD = 30;

    /** the speed of the unit. */
    private static final int SPEED = 22;

    /**
	 * constructor.
	 */
    public Sword() {
        super(new Ressources(RES_WOOD, RES_MUD, RES_IRON), POPULATION, SPEED, GOODS, ATTACK, DEFENSE, DEFENSE_CAVALRY, DEFENSE_ARCHER);
    }
}
