package hu.schmidtsoft.map.model;

/**
 * Represents a polygon on the map layer. Polygons are areas
 * The following objects can be represented by a polygon:
 * - terrain type
 * - cities
 * @author rizsi
 *
 */
public class MPolyGon extends MPolyAbstract {

    public MPolyGon(long id, MCoordinateArray coordinates) {
        super(id, coordinates);
    }

    public MPolyGon(long id) {
        super(id);
    }

    public static final int TYPE_CODE = 2;

    @Override
    public int getTypeCode() {
        return TYPE_CODE;
    }

    @Override
    public String toString() {
        return "poligon: " + getLabel() + " " + getType();
    }

    @Override
    public MObject getClone() {
        MPolyGon ret = new MPolyGon(getId(), getAllCoordinates());
        ret.setType(getType());
        ret.setLabel(getLabel());
        return ret;
    }
}
