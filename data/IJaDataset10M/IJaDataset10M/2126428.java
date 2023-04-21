package playground.johannes.studies.ivt;

import java.io.IOException;
import java.util.HashSet;
import java.util.Set;
import org.apache.commons.math.stat.descriptive.DescriptiveStatistics;
import org.apache.log4j.Logger;
import org.geotools.feature.Feature;
import org.geotools.referencing.CRS;
import org.geotools.referencing.crs.DefaultGeographicCRS;
import org.opengis.referencing.FactoryException;
import playground.johannes.sna.gis.CRSUtils;
import playground.johannes.sna.gis.Zone;
import playground.johannes.sna.gis.ZoneLayer;
import playground.johannes.sna.graph.GraphBuilder;
import playground.johannes.sna.graph.spatial.SpatialSparseGraph;
import playground.johannes.sna.graph.spatial.SpatialVertex;
import playground.johannes.sna.math.BoundedLinearDiscretizer;
import playground.johannes.sna.math.Discretizer;
import playground.johannes.sna.math.LinearDiscretizer;
import playground.johannes.socialnetworks.gis.CartesianDistanceCalculator;
import playground.johannes.socialnetworks.gis.DistanceCalculator;
import playground.johannes.socialnetworks.gis.PointUtils;
import playground.johannes.socialnetworks.gis.SpatialGrid;
import playground.johannes.socialnetworks.gis.io.FeatureSHP;
import playground.johannes.socialnetworks.gis.io.ZoneLayerSHP;
import playground.johannes.socialnetworks.graph.spatial.analysis.SpatialFilter;
import playground.johannes.socialnetworks.graph.spatial.analysis.ZoneUtils;
import playground.johannes.socialnetworks.graph.spatial.io.Population2SpatialGraph;
import playground.johannes.socialnetworks.snowball2.social.SocialSampledGraphProjection;
import playground.johannes.socialnetworks.snowball2.social.SocialSampledGraphProjectionBuilder;
import playground.johannes.socialnetworks.survey.ivt2009.graph.SocialSparseEdge;
import playground.johannes.socialnetworks.survey.ivt2009.graph.SocialSparseGraph;
import playground.johannes.socialnetworks.survey.ivt2009.graph.SocialSparseVertex;
import playground.johannes.socialnetworks.survey.ivt2009.graph.io.GraphReaderFacade;
import com.vividsolutions.jts.geom.Coordinate;
import com.vividsolutions.jts.geom.Envelope;
import com.vividsolutions.jts.geom.Geometry;
import com.vividsolutions.jts.geom.GeometryFactory;
import com.vividsolutions.jts.geom.Point;

/**
 * @author illenberger
 *
 */
public class DensityPlotBiTree {

    private static final Logger logger = Logger.getLogger(DensityPlotBiTree.class);

    private static GeometryFactory geoFactory = new GeometryFactory();

    public static void main(String[] args) throws IOException, FactoryException {
        SocialSampledGraphProjection<SocialSparseGraph, SocialSparseVertex, SocialSparseEdge> graph = GraphReaderFacade.read("/Users/jillenberger/Work/socialnets/data/ivt2009/11-2011/graph/graph.graphml");
        SocialSampledGraphProjectionBuilder<SocialSparseGraph, SocialSparseVertex, SocialSparseEdge> builder = new SocialSampledGraphProjectionBuilder<SocialSparseGraph, SocialSparseVertex, SocialSparseEdge>();
        SpatialSparseGraph popData = new Population2SpatialGraph(CRSUtils.getCRS(21781)).read("/Users/jillenberger/Work/socialnets/data/schweiz/complete/plans/plans.0.10.xml");
        Feature feature = FeatureSHP.readFeatures("/Users/jillenberger/Work/socialnets/data/schweiz/complete/zones/G1L08.shp").iterator().next();
        Geometry chBorder = feature.getDefaultGeometry();
        chBorder.setSRID(21781);
        graph.getDelegate().transformToCRS(CRSUtils.getCRS(21781));
        logger.info("Applying spatial filter...");
        SpatialFilter filter = new SpatialFilter((GraphBuilder) builder, chBorder);
        graph = (SocialSampledGraphProjection<SocialSparseGraph, SocialSparseVertex, SocialSparseEdge>) filter.apply(graph);
        Set<Point> points = new HashSet<Point>();
        for (SpatialVertex v : graph.getVertices()) {
            points.add(v.getPoint());
        }
        GeometryFactory factory = new GeometryFactory();
        Point zrh = factory.createPoint(new Coordinate(8.55, 47.36));
        zrh = CRSUtils.transformPoint(zrh, CRS.findMathTransform(DefaultGeographicCRS.WGS84, CRSUtils.getCRS(21781)));
        logger.info("Segmenting tiles...");
        Envelope env = PointUtils.envelope(points);
        SpatialGrid<Double> sampleGrid = new SpatialGrid<Double>(env.getMinX(), env.getMinY(), env.getMaxX(), env.getMaxY(), 5000);
        DistanceCalculator calc = new CartesianDistanceCalculator();
        Discretizer disc = new LinearDiscretizer(1000.0);
        logger.info("Creating survey grid...");
        for (Point p : points) {
            Double data = sampleGrid.getValue(p);
            double val = 0;
            if (data != null) val = data.doubleValue();
            double d = disc.discretize(calc.distance(p, zrh));
            d = Math.max(1, d);
            double proba = Math.pow(d, -1.4);
            val++;
            sampleGrid.setValue(val, p);
        }
        logger.info("Creating population grid...");
        SpatialGrid<Integer> popGrid = new SpatialGrid<Integer>(sampleGrid);
        for (SpatialVertex v : popData.getVertices()) {
            Integer data = popGrid.getValue(v.getPoint());
            int val = 0;
            if (data != null) {
                val = data.intValue();
            }
            val++;
            popGrid.setValue(val, v.getPoint());
        }
        logger.info("Creating density grid...");
        SpatialGrid<Double> densityGrid = new SpatialGrid<Double>(sampleGrid);
        for (int row = 0; row < densityGrid.getNumRows(); row++) {
            for (int col = 0; col < densityGrid.getNumCols(row); col++) {
                Integer inhabitants = popGrid.getValue(row, col);
                Double samples = sampleGrid.getValue(row, col);
                if (inhabitants != null && samples != null) {
                    double density = samples / (double) inhabitants;
                    densityGrid.setValue(row, col, density);
                } else {
                    densityGrid.setValue(row, col, 0.0);
                }
            }
        }
        ZoneLayer<Double> layer = ZoneUtils.createGridLayer(5000, chBorder);
        layer.overwriteCRS(CRSUtils.getCRS(21781));
        DescriptiveStatistics stats = new DescriptiveStatistics();
        for (int row = 0; row < densityGrid.getNumRows(); row++) {
            for (int col = 0; col < densityGrid.getNumCols(row); col++) {
                Point p = makeCoordinate(densityGrid, row, col);
                p.setSRID(21781);
                Zone<Double> z = layer.getZone(p);
                if (z != null) {
                    double val = densityGrid.getValue(row, col);
                    if (val > 0) {
                        stats.addValue(val);
                    }
                    z.setAttribute(val);
                }
            }
        }
        ZoneLayerSHP.write(layer, "/Users/jillenberger/Work/socialnets/data/ivt2009/11-2011/graph/density.grid.shp");
    }

    private static Point makeCoordinate(SpatialGrid<?> grid, int row, int col) {
        double x = grid.getXmin() + (col * grid.getResolution()) + grid.getResolution() / 2.0;
        double y = grid.getYmax() - (row * grid.getResolution()) - grid.getResolution() / 2.0;
        Point point;
        point = geoFactory.createPoint(new Coordinate(x, y));
        return point;
    }
}
