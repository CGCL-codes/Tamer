package mou.core.starmap;

import java.awt.Point;
import mou.core.db.DBObjectImpl;
import mou.core.db.ObjectChangedEvent;
import mou.core.ship.Ship;

/**
 * @author PB
 */
public class PositionChangedEvent extends ObjectChangedEvent {

    /**
	 * @param Ursprungsevent
	 */
    public PositionChangedEvent(DBObjectImpl obj, Point oldPos, Point newPos) {
        super(obj, Ship.ATTR_POSITION, oldPos, newPos);
    }

    public Point getOldPosition() {
        return (Point) getOldValue();
    }

    public Point getNewPosition() {
        return (Point) getNewValue();
    }
}
