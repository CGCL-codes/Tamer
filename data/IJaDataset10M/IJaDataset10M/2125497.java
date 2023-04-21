package net.disy.ogc.gml.v_3_1_1.dwr;

import net.opengis.gml.v_3_1_1.MultiPointType;
import net.opengis.gml.v_3_1_1.PointPropertyType;
import net.opengis.gml.v_3_1_1.PointType;
import org.directwebremoting.extend.MarshallException;
import org.jvnet.jaxb2_commons.locator.DefaultRootObjectLocator;
import org.jvnet.ogc.gml.v_3_1_1.jts.ConversionFailedException;
import com.vividsolutions.jts.geom.MultiPoint;
import com.vividsolutions.jts.geom.Point;

public class MultiPointTypeConverter extends AbstractGeometryTypeConverter<MultiPointType, double[][]> {

    private PointTypeConverter pointTypeConverter = new PointTypeConverter();

    @Override
    protected double[][] createCoordinates(MultiPointType multiPointType) throws MarshallException {
        try {
            final MultiPoint multiPoint = (MultiPoint) getConverter().createGeometry(new DefaultRootObjectLocator(multiPointType), multiPointType);
            int numGeometries = multiPoint.getNumGeometries();
            double[][] coordinates = new double[numGeometries][];
            for (int i = 0; i < numGeometries; i++) {
                coordinates[i] = pointTypeConverter.createCoordinate((Point) multiPoint.getGeometryN(i));
            }
            return coordinates;
        } catch (ConversionFailedException cfex) {
            throw new MarshallException(PointType.class, cfex);
        }
    }

    @Override
    protected String getGeometryType() {
        return "MultiPoint";
    }

    @Override
    protected MultiPointType createGeometry(double[][] coordinates) {
        final MultiPointType multiPoint = new MultiPointType();
        for (int index = 0; index < coordinates.length; index++) {
            final double[] pointCoordinates = coordinates[index];
            PointType pointType = pointTypeConverter.createGeometry(pointCoordinates);
            PointPropertyType pointPropertyType = new PointPropertyType();
            pointPropertyType.setPoint(pointType);
            multiPoint.getPointMember().add(pointPropertyType);
        }
        return multiPoint;
    }

    @Override
    protected Class<double[][]> getCoordinatesType() {
        return double[][].class;
    }
}
