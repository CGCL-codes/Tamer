package gameserver.model.siege;

import gameserver.model.templates.siege.SiegeLocationTemplate;

public class SiegeLocation {

    public static final int INVULNERABLE = 0;

    public static final int VULNERABLE = 1;

    /**
	 * Unique id, defined by NCSoft
	 */
    private int locationId;

    private int worldId;

    private SiegeType type;

    private SiegeLocationTemplate template;

    private SiegeRace siegeRace = SiegeRace.BALAUR;

    private int legionId = 0;

    private boolean isVulnerable = false;

    private boolean isShieldActive = false;

    private int nextState = 1;

    public SiegeLocation() {
    }

    public SiegeLocation(SiegeLocationTemplate template) {
        this.template = template;
        this.locationId = template.getId();
        this.worldId = template.getWorldId();
        this.type = template.getType();
    }

    /**
	 * Returns unique LocationId of Siege Location
	 * @return Integer LocationId
	 */
    public int getLocationId() {
        return this.locationId;
    }

    public SiegeType getSiegeType() {
        return type;
    }

    public int getWorldId() {
        return this.worldId;
    }

    public SiegeLocationTemplate getLocationTemplate() {
        return this.template;
    }

    public SiegeRace getRace() {
        return this.siegeRace;
    }

    public void setRace(SiegeRace siegeRace) {
        this.siegeRace = siegeRace;
    }

    public int getLegionId() {
        return this.legionId;
    }

    public void setLegionId(int legionId) {
        this.legionId = legionId;
    }

    /**
	 * Next State:
	 * 		0 invulnerable
	 * 		1 vulnerable
	 * @return nextState
	 */
    public int getNextState() {
        return this.nextState;
    }

    /**
	 * @param nextState
	 */
    public void setNextState(Integer nextState) {
        this.nextState = nextState;
    }

    /**
	 * @return isVulnerable
	 */
    public boolean isVulnerable() {
        return this.isVulnerable;
    }

    /**
	 * @param new vulnerable value
	 */
    public void setVulnerable(boolean value) {
        this.isVulnerable = value;
        if (getSiegeType() == SiegeType.FORTRESS) this.isShieldActive = value;
    }

    /**
	 * @return the isShieldActive
	 */
    public boolean isShieldActive() {
        return isShieldActive;
    }

    /**
	 * @param new shield value
	 */
    public void setShieldActive(boolean value) {
        if (getSiegeType() == SiegeType.FORTRESS) this.isShieldActive = value;
    }

    /**
	 * @return
	 */
    public int getInfluenceValue() {
        return 0;
    }
}
