package geovista.jts.feature;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;
import com.vividsolutions.jts.geom.Geometry;

/**
 * Useful utility functions for working with Features.
 * @see Feature
 */
public class FeatureUtil {

    /**
	 * Creates a new Feature from the given Geometry, with nominal values for
	 * the attributes.
	 * @param g the Geometry to convert
	 * @param schema metadata for the Feature to create
	 * @return a new Feature containing the Geometry and default values for the
	 * attributes
	 */
    public static Feature toFeature(Geometry g, FeatureSchema schema) {
        Feature feature = new BasicFeature(schema);
        feature.setGeometry(g);
        return feature;
    }

    /**
     * Returns the n Geometries extracted from the given n Features
     */
    public static List toGeometries(Collection features) {
        ArrayList list = new ArrayList();
        for (Iterator i = features.iterator(); i.hasNext(); ) {
            Feature feature = (Feature) i.next();
            list.add(feature.getGeometry());
        }
        return list;
    }

    /**
     * Compares two Features for order based on their feature ID.
     * @see Feature#getID()
     */
    public static class IDComparator implements Comparator {

        public int compare(Object o1, Object o2) {
            Feature f1 = (Feature) o1;
            Feature f2 = (Feature) o2;
            if (f1.getID() < f2.getID()) {
                return -1;
            }
            if (f1.getID() > f2.getID()) {
                return 1;
            }
            return 0;
        }
    }

    private static int lastID = 0;

    /**
     * Increments and returns the feature-ID counter
     * @see Feature#getID()
     */
    public static int nextID() {
        return ++lastID;
    }

    /**
     * Although Feature implements Cloneable, this method is useful
     * when the two Features are implemented with different classes.
     */
    public static void copyAttributes(Feature a, Feature b) {
        for (int i = 0; i < a.getSchema().getAttributeCount(); i++) {
            b.setAttribute(i, a.getAttribute(i));
        }
    }

    /**
	 * Returns whether all attributes are null (other than the Geometry
	 * attribute, which is not checked)
	 */
    public static boolean areAllNonSpatialAttributesNull(Feature feature) {
        for (int i = 0; i < feature.getSchema().getAttributeCount(); i++) {
            if (AttributeType.GEOMETRY == feature.getSchema().getAttributeType(i)) {
                continue;
            }
            if (feature.getAttribute(i) != null) {
                return false;
            }
        }
        return true;
    }
}
