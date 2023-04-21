package source;

public class RangedUnit extends Unit {

    /**
	 * @param minRange
	 */
    protected int minRange;

    protected int maxRange;

    RangedUnit(UnitType u, PlayerStatus player, Tile tileOn) {
        super(u, player, tileOn);
        this.minRange = u.minRange();
        this.maxRange = u.maxRange();
    }
}
