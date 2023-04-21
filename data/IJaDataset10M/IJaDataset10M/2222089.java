package org.jrandomize.model;

/**
 * @author Jorge
 *
 */
public class Character {

    private Attribute intelligence;

    private Attribute perception;

    private Attribute presence;

    private Attribute communication;

    private Attribute strength;

    private Attribute stamina;

    private Attribute dexterity;

    private Attribute quickness;

    /**
	 * @return Returns the communication.
	 */
    public Attribute getCommunication() {
        return communication;
    }

    /**
	 * @param communication The communication to set.
	 */
    public void setCommunication(Attribute communication) {
        this.communication = communication;
    }

    /**
	 * @return Returns the dexterity.
	 */
    public Attribute getDexterity() {
        return dexterity;
    }

    /**
	 * @param dexterity The dexterity to set.
	 */
    public void setDexterity(Attribute dexterity) {
        this.dexterity = dexterity;
    }

    /**
	 * @return Returns the intelligence.
	 */
    public Attribute getIntelligence() {
        return intelligence;
    }

    /**
	 * @param intelligence The intelligence to set.
	 */
    public void setIntelligence(Attribute intelligence) {
        this.intelligence = intelligence;
    }

    /**
	 * @return Returns the perception.
	 */
    public Attribute getPerception() {
        return perception;
    }

    /**
	 * @param perception The perception to set.
	 */
    public void setPerception(Attribute perception) {
        this.perception = perception;
    }

    /**
	 * @return Returns the presence.
	 */
    public Attribute getPresence() {
        return presence;
    }

    /**
	 * @param presence The presence to set.
	 */
    public void setPresence(Attribute presence) {
        this.presence = presence;
    }

    /**
	 * @return Returns the quickness.
	 */
    public Attribute getQuickness() {
        return quickness;
    }

    /**
	 * @param quickness The quickness to set.
	 */
    public void setQuickness(Attribute quickness) {
        this.quickness = quickness;
    }

    /**
	 * @return Returns the stamina.
	 */
    public Attribute getStamina() {
        return stamina;
    }

    /**
	 * @param stamina The stamina to set.
	 */
    public void setStamina(Attribute stamina) {
        this.stamina = stamina;
    }

    /**
	 * @return Returns the strength.
	 */
    public Attribute getStrength() {
        return strength;
    }

    /**
	 * @param strength The strength to set.
	 */
    public void setStrength(Attribute strength) {
        this.strength = strength;
    }
}
