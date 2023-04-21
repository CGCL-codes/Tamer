package org.openmi.utilities.spatial;

import org.openmi.standard.IElementSet;
import static org.openmi.utilities.spatial.XYGeometryTools.*;

/**
 * MapPointToPolygon is an implementation of a MappingStrategy to be used by
 * the ElementMapper and maps XYPoints from the source element set on
 * XYPolygons of the target element set.
 */
public class MapPointToPolygon extends MappingStrategy {

    /**
     * Fill the mapping matrix with values according to the selected mapping
     * method and the source and target element sets.
     *
     * @param method The mapping method to use
     * @param source The element set to map from
     * @param target The element set to map to
     * @throws Exception
     */
    public void updateMappingMatrix(ElementMappingMethod method, IElementSet source, IElementSet target) throws Exception {
        XYPolygon polygon;
        int count;
        for (int i = 0; i < numberOfRows; i++) {
            polygon = createXYPolygon(target, i);
            count = 0;
            for (int n = 0; n < numberOfColumns; n++) if (isPointInPolygon(createXYPoint(source, n), polygon)) switch(method) {
                case POINT_TO_POLYGON_MEAN:
                    count++;
                    break;
                case POINT_TO_POLYGON_SUM:
                    count = 1;
                    break;
                default:
                    throw new Exception("Method unknown for point to polygon mapping");
            }
            for (int n = 0; n < numberOfColumns; n++) if (isPointInPolygon(createXYPoint(source, n), polygon)) mappingMatrix[i][n] = 1.0 / count;
        }
    }
}
