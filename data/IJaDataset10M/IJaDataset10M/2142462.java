package calclipse.lib.math.util.graph3d;

import java.awt.Graphics;
import java.awt.Point;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

/**
 * A visual 3D object made up by XYZ coordinates,
 * such as a surface.
 * @author T. Sommerland
 */
public abstract class Object3D implements Cloneable {

    private List<XYZ> coordinates;

    public Object3D(final XYZ... coords) {
        coordinates = new ArrayList<XYZ>(Arrays.asList(coords));
    }

    public List<XYZ> getCoordinates() {
        return Collections.unmodifiableList(coordinates);
    }

    /**
     * Paints this object.
     * @param points the XYZ-coordinates of this object as physical coordinates
     */
    protected abstract void paint(Graphics g, Point[] points);

    /**
     * Clips the given list of coordinates against the given volume.
     */
    protected abstract void clip(List<XYZ> coords, Volume volume);

    /**
     * Clones this object and clips the clone against the given volume.
     * @return null if the entire object is clipped.
     */
    public Object3D cloneAndClip(final Volume volume) {
        final Object3D obj = clone();
        clip(obj.coordinates, volume);
        if (obj.coordinates.isEmpty()) {
            return null;
        }
        return obj;
    }

    @Override
    public Object3D clone() {
        final List<XYZ> coords = new ArrayList<XYZ>(coordinates.size());
        for (final XYZ xyz : coordinates) {
            coords.add(new XYZ(xyz));
        }
        try {
            final Object3D obj = (Object3D) super.clone();
            obj.coordinates = coords;
            return obj;
        } catch (final CloneNotSupportedException ex) {
            throw new AssertionError(ex);
        }
    }
}
