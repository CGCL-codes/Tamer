package org.gvsig.jts.voronoi;

import java.awt.geom.Point2D;
import java.rmi.server.UID;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import org.geotools.referencefork.geometry.DirectPosition2D;
import org.geotools.referencefork.referencing.operation.builder.MapTriangulationFactory;
import org.geotools.referencefork.referencing.operation.builder.Quadrilateral;
import org.geotools.referencefork.referencing.operation.builder.TINTriangle;
import org.gvsig.exceptions.BaseException;
import org.gvsig.referencing.VoronoiLyrAdapter;
import org.gvsig.topology.Messages;
import org.opengis.spatialschema.geometry.DirectPosition;
import com.hardcode.gdbms.engine.values.Value;
import com.hardcode.gdbms.engine.values.ValueFactory;
import com.iver.utiles.swing.threads.CancellableProgressTask;
import com.vividsolutions.jts.geom.Coordinate;
import com.vividsolutions.jts.geom.Polygon;

/**
 * Voronoi strategy that uses an incremental insertion algorithm whose
 * first geometries are two triangles derived from a rectangular bounding box
 * of the original mess.
 * @author Alvaro Zabala
 *
 */
public class IncrementalRectBoundsVoronoiStrategy extends AbstractVoronoiStrategy {

    public List<TriangleFeature> createTin(VoronoiAndTinInputLyr inputLyr, boolean onlySelection, CancellableProgressTask progressMonitor) throws BaseException {
        List<TriangleFeature> solution = new ArrayList<TriangleFeature>();
        Polygon bbox = (Polygon) Voronoier.getThiessenBoundingBox(inputLyr, onlySelection);
        Coordinate[] coords = bbox.getCoordinates();
        DirectPosition a = new DirectPosition2D(coords[0].x, coords[0].y);
        DirectPosition b = new DirectPosition2D(coords[1].x, coords[1].y);
        DirectPosition c = new DirectPosition2D(coords[2].x, coords[2].y);
        DirectPosition d = new DirectPosition2D(coords[3].x, coords[3].y);
        Quadrilateral quad = new Quadrilateral(a, b, c, d);
        VoronoiLyrAdapter pointsList = new VoronoiLyrAdapter(inputLyr);
        MapTriangulationFactory triangulator = new MapTriangulationFactory(quad, pointsList);
        Map<TINTriangle, TINTriangle> triangleMap = triangulator.getTriangleMap();
        Iterator<TINTriangle> triangles = triangleMap.values().iterator();
        int idx = 0;
        while (triangles.hasNext()) {
            TINTriangle triangle = triangles.next();
            Point2D ta = new Point2D.Double(triangle.p0.getOrdinate(0), triangle.p0.getOrdinate(1));
            Point2D tb = new Point2D.Double(triangle.p1.getOrdinate(0), triangle.p1.getOrdinate(1));
            Point2D tc = new Point2D.Double(triangle.p2.getOrdinate(0), triangle.p2.getOrdinate(1));
            FTriangle ftriangle = new FTriangle(ta, tb, tc);
            Value fid = ValueFactory.createValue(idx);
            Value[] values = new Value[] { fid };
            TriangleFeature feature = new TriangleFeature(ftriangle, values, new UID().toString());
            solution.add(feature);
            idx++;
        }
        return solution;
    }

    public String getName() {
        return Messages.getText("Incremental_DT_based_in_a_rectangular_bounding_box");
    }
}
