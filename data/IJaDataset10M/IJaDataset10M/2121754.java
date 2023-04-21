package gtagame;

import java.awt.Point;

/**
 *
 * @author Miki
 */
public abstract class MapElement implements MapElementIF {

    protected CityMap cityMap;

    public MapElement(CityMap cityMap) {
        this.cityMap = cityMap;
    }

    protected MapElement() {
    }

    @Override
    public abstract String toString();

    /**
     *
     * @return
     */
    protected boolean isStreet() {
        return false;
    }

    @Override
    public MapElement getNeighbor(Direction direction) {
        return (MapElement) CityMap.getMap().getNeighbor(this, direction);
    }
}
