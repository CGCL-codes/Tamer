package org.gvsig.topology;

import java.awt.geom.Point2D;
import java.io.File;
import java.net.URL;
import java.util.List;
import junit.framework.TestCase;
import org.cresques.cts.IProjection;
import org.gvsig.exceptions.BaseException;
import org.gvsig.jts.voronoi.TriangleFeature;
import org.gvsig.jts.voronoi.VoronoiAndTinInputLyr;
import org.gvsig.jts.voronoi.Voronoier;
import com.hardcode.gdbms.driver.exceptions.ReadDriverException;
import com.iver.cit.gvsig.exceptions.expansionfile.ExpansionFileReadException;
import com.iver.cit.gvsig.exceptions.layers.LoadLayerException;
import com.iver.cit.gvsig.fmap.core.IGeometry;
import com.iver.cit.gvsig.fmap.crs.CRSFactory;
import com.iver.cit.gvsig.fmap.layers.FLayer;
import com.iver.cit.gvsig.fmap.layers.FLyrVect;
import com.iver.cit.gvsig.fmap.layers.LayerFactory;
import com.iver.cit.gvsig.fmap.layers.ReadableVectorial;

public class VoronoiGeoprocessTest extends TestCase {

    private File baseDataPath;

    private File baseDriversPath;

    private IProjection PROJECTION_DEFAULT;

    private FLyrVect pointLyr;

    public void setUp() throws Exception {
        super.setUp();
        URL url = VoronoiGeoprocessTest.class.getResource("testdata/testvoronoi");
        if (url == null) throw new Exception("No se encuentra el directorio con datos de prueba");
        baseDataPath = new File(url.getFile());
        if (!baseDataPath.exists()) throw new Exception("No se encuentra el directorio con datos de prueba");
        baseDriversPath = new File("../_fwAndami/gvSIG/extensiones/com.iver.cit.gvsig/drivers");
        if (!baseDriversPath.exists()) throw new Exception("Can't find drivers path ");
        com.iver.cit.gvsig.fmap.layers.LayerFactory.setDriversPath(baseDriversPath.getAbsolutePath());
        LayerFactory.setWritersPath(baseDriversPath.getAbsolutePath());
        if (LayerFactory.getDM().getDriverNames().length < 1) throw new Exception("Can't find drivers in path: " + baseDriversPath);
        PROJECTION_DEFAULT = CRSFactory.getCRS("EPSG:23030");
        pointLyr = (FLyrVect) newLayer("SAIH.shp", "gvSIG shp driver");
    }

    public FLayer newLayer(String fileName, String driverName) throws LoadLayerException {
        FLayer solution = null;
        File file = new File(baseDataPath, fileName);
        solution = com.iver.cit.gvsig.fmap.layers.LayerFactory.createLayer(fileName, driverName, file, PROJECTION_DEFAULT);
        solution.setAvailable(true);
        return solution;
    }

    public void test1() throws ReadDriverException {
        VoronoiAndTinInputLyr inputLyr = new VoronoiAndTinInputLyr() {

            private final FLyrVect lyr = pointLyr;

            public ReadableVectorial getSource() {
                return lyr.getSource();
            }

            public Point2D getPoint(int geometryIndex) {
                try {
                    IGeometry shape = lyr.getSource().getShape(geometryIndex);
                    com.iver.cit.gvsig.fmap.core.Handler[] handlers = shape.getHandlers(IGeometry.SELECTHANDLER);
                    return handlers[0].getPoint();
                } catch (ExpansionFileReadException e) {
                    e.printStackTrace();
                    return null;
                } catch (ReadDriverException e) {
                    e.printStackTrace();
                    return null;
                }
            }
        };
        List<TriangleFeature> featureCollection;
        try {
            featureCollection = Voronoier.createTIN(inputLyr, false, "CHEN", null);
            assertTrue(featureCollection.size() != 0);
            featureCollection = Voronoier.createTIN(inputLyr, false, "CHEW", null);
            assertTrue(featureCollection.size() != 0);
        } catch (BaseException e) {
            e.printStackTrace();
        }
    }
}
