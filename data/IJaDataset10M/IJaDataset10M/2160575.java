package net.disy.ogc.gml.v_3_1_1.annotation.test;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import net.disy.ogc.gml.v_3_1_1.annotation.DefaultWKTGeometry;
import net.disy.ogc.wps.v_1_0_0.annotation.DefaultValue;
import net.disy.ogc.wps.v_1_0_0.annotation.DefaultValueFactory;
import org.junit.Test;
import com.vividsolutions.jts.geom.Coordinate;
import com.vividsolutions.jts.geom.Geometry;
import com.vividsolutions.jts.geom.GeometryFactory;
import com.vividsolutions.jts.geom.Polygon;

public class DefaultWKTGeometryTest {

    @Test
    public void annotatedWithDefaultValue() {
        final DefaultValue defaultValue = DefaultWKTGeometry.class.getAnnotation(DefaultValue.class);
        assertNotNull(defaultValue);
    }

    @Test
    public void correctGeometry() throws Exception {
        final GeometryFactory geometryFactory = new GeometryFactory();
        final Geometry alpha = geometryFactory.createPolygon(geometryFactory.createLinearRing(new Coordinate[] { new Coordinate(0, 0), new Coordinate(10, 0), new Coordinate(10, 10), new Coordinate(0, 10), new Coordinate(0, 0) }), null);
        Method method = getClass().getMethod("foo", new Class[] { Polygon.class });
        Annotation annotation = method.getParameterAnnotations()[0][0];
        DefaultValue defaultValue = annotation.annotationType().getAnnotation(DefaultValue.class);
        DefaultValueFactory<Annotation, Geometry> defaultValueFactory = (DefaultValueFactory<Annotation, Geometry>) defaultValue.createdBy().newInstance();
        final Geometry omega = defaultValueFactory.createDefaultValue(annotation);
        assertTrue(alpha.equals(omega));
    }

    public void foo(@DefaultWKTGeometry("POLYGON((0 0, 10 0, 10 10, 0 10, 0 0))") Polygon polygon) {
    }
}
