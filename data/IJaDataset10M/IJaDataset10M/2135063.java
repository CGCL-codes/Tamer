package games.midhedava.client.entity;

public class PlantGrower extends Entity {

    /**
	 * Transition method. Create the screen view for this entity.
	 *
	 * @return	The on-screen view of this entity.
	 */
    @Override
    protected Entity2DView createView() {
        return new PlantGrower2DView(this);
    }

    /**
	 * Determine if this is an obstacle for another entity.
	 *
	 * @param	entity		The entity to check against.
	 *
	 * @return	<code>true</code> the entity can not enter this
	 *		entity's area.
	 */
    @Override
    public boolean isObstacle(final Entity entity) {
        return false;
    }
}
