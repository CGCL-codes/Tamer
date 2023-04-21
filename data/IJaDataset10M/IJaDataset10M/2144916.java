package adlez.logic.events;

public class EventDevSpawnEntity extends Event {

    protected int entityX;

    protected int entityY;

    public EventDevSpawnEntity(int x, int y) {
        super();
        this.entityX = x;
        this.entityY = y;
    }

    /**
	 * @return the entityX
	 */
    public int getEntityX() {
        return entityX;
    }

    /**
	 * @return the entityY
	 */
    public int getEntityY() {
        return entityY;
    }
}
