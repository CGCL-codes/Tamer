package org.gvsig.topology;

import java.io.File;
import java.net.URL;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import junit.framework.TestCase;
import org.cresques.cts.IProjection;
import org.gvsig.topology.topologyrules.MustBeLargerThanClusterTolerance;
import org.gvsig.topology.topologyrules.MustNotHaveRepeatedPoints;
import org.gvsig.topology.topologyrules.jtsisvalidrules.GeometryMustHaveValidCoordinates;
import org.gvsig.topology.util.LayerFactory;
import com.iver.cit.gvsig.exceptions.layers.LoadLayerException;
import com.iver.cit.gvsig.fmap.MapContext;
import com.iver.cit.gvsig.fmap.ViewPort;
import com.iver.cit.gvsig.fmap.core.FShape;
import com.iver.cit.gvsig.fmap.crs.CRSFactory;
import com.iver.cit.gvsig.fmap.layers.FLayer;
import com.iver.cit.gvsig.fmap.layers.FLayers;
import com.iver.cit.gvsig.fmap.layers.FLyrVect;

/**
 * Unit tests for Topology class.
 * 
 * @author azabala
 *
 */
public class TopologyTest extends TestCase {

    File baseDataPath;

    File baseDriversPath;

    IProjection PROJECTION_DEFAULT;

    ViewPort VIEWPORT;

    FLayers ROOT;

    SimpleTopologyErrorContainer errorContainer;

    Topology topology = null;

    Topology topology2 = null;

    FLyrVect multiPointLayer;

    FLyrVect lineLyrWithCollapsedCoords;

    FLyrVect shapeBasedLinearLyr;

    MapContext mapContext;

    public void setUp() throws Exception {
        super.setUp();
        URL url = TopologyTest.class.getResource("testdata");
        if (url == null) throw new Exception("No se encuentra el directorio con datos de prueba");
        baseDataPath = new File(url.getFile());
        if (!baseDataPath.exists()) throw new Exception("No se encuentra el directorio con datos de prueba");
        baseDriversPath = new File("../_fwAndami/gvSIG/extensiones/com.iver.cit.gvsig/drivers");
        if (!baseDriversPath.exists()) throw new Exception("Can't find drivers path ");
        com.iver.cit.gvsig.fmap.layers.LayerFactory.setDriversPath(baseDriversPath.getAbsolutePath());
        PROJECTION_DEFAULT = CRSFactory.getCRS("EPSG:23030");
        VIEWPORT = new ViewPort(PROJECTION_DEFAULT);
        mapContext = new MapContext(VIEWPORT);
        ROOT = mapContext.getLayers();
        errorContainer = new SimpleTopologyErrorContainer();
        topology = new Topology(mapContext, ROOT, 0.01, 1000, errorContainer);
        topology2 = new Topology(mapContext, ROOT, 0.01, 1000, errorContainer);
        multiPointLayer = LayerFactory.getLyrWithRepeatedCoords();
        lineLyrWithCollapsedCoords = LayerFactory.getLineLayerWithCollapsedCoords();
        shapeBasedLinearLyr = (FLyrVect) newLayer("vc1-1500.shp", "gvSIG shp driver");
    }

    public void tearDown() throws Exception {
        super.tearDown();
        topology = null;
        topology2 = null;
        multiPointLayer = null;
        lineLyrWithCollapsedCoords = null;
    }

    public FLayer newLayer(String fileName, String driverName) throws LoadLayerException {
        FLayer solution = null;
        File file = new File(baseDataPath, fileName);
        solution = com.iver.cit.gvsig.fmap.layers.LayerFactory.createLayer(fileName, driverName, file, PROJECTION_DEFAULT);
        solution.setAvailable(true);
        return solution;
    }

    public void testStatusAndCoherence() throws Exception {
        topology.addLayer(lineLyrWithCollapsedCoords);
        topology.validate();
        int status = topology.getStatus();
        assertTrue(status == Topology.VALIDATED);
        int numberOfErrors = topology.getNumberOfErrors();
        assertTrue(numberOfErrors == 0);
        topology.setClusterTolerance(12d);
        topology.validate();
        status = topology.getStatus();
        assertTrue(status == Topology.VALIDATED_WITH_ERRORS);
        numberOfErrors = topology.getNumberOfErrors();
        assertTrue(numberOfErrors == 1);
        TopologyError error = topology.getTopologyError(0);
        assertTrue(error.getViolatedRule().getClass().equals(MustBeLargerThanClusterTolerance.class));
        topology.addLayer(multiPointLayer);
        MustNotHaveRepeatedPoints rule1 = new MustNotHaveRepeatedPoints(multiPointLayer);
        topology.addRule(rule1);
        status = topology.getStatus();
        assertTrue(status == Topology.NOT_VALIDATED);
        int numberOfDirtyZones = topology.getNumberOfDirtyZones();
        assertTrue(numberOfDirtyZones == 0);
        topology.validate();
        status = topology.getStatus();
        assertTrue(status == Topology.VALIDATED_WITH_ERRORS);
        numberOfErrors = topology.getNumberOfErrors();
        assertTrue(numberOfErrors == 2);
        topology.resetStatus();
        topology.setMaxNumberOfErrors(1);
        topology.validate();
        assertTrue(topology.getNumberOfErrors() == 1);
        topology.resetStatus();
        topology.setMaxNumberOfErrors(1000);
        topology.validate();
        assertTrue(topology.getNumberOfErrors() == 2);
        for (int i = 0; i < numberOfErrors; i++) {
            error = topology.getTopologyError(i);
            topology.markAsTopologyException(error);
        }
        assertTrue(topology.getStatus() == Topology.VALIDATED);
    }

    public void testAddRuleWithLayersNotReferenced() {
        MustNotHaveRepeatedPoints rule = new MustNotHaveRepeatedPoints(multiPointLayer);
        boolean ok = false;
        try {
            topology2.addRule(rule);
            ok = true;
        } catch (RuleNotAllowedException e) {
            e.printStackTrace();
        } catch (TopologyRuleDefinitionException e) {
            e.printStackTrace();
        }
        assertTrue(!ok);
    }

    public void testDemoteExceptionsToErrors() throws RuleNotAllowedException, TopologyRuleDefinitionException {
        topology2.setClusterTolerance(12d);
        topology2.addLayer(multiPointLayer);
        topology2.addLayer(this.lineLyrWithCollapsedCoords);
        topology2.addRule(new MustNotHaveRepeatedPoints(multiPointLayer));
        topology2.validate();
        int numberOfErrors = topology2.getNumberOfErrors();
        assertTrue(numberOfErrors == 2);
        for (int i = 0; i < numberOfErrors; i++) {
            TopologyError error = topology2.getTopologyError(i);
            topology2.markAsTopologyException(error);
        }
        assertTrue(topology2.getStatus() == Topology.VALIDATED);
        for (int i = 0; i < numberOfErrors; i++) {
            TopologyError error = topology2.getTopologyError(i);
            topology2.demoteToError(error);
        }
        assertTrue(topology2.getStatus() == Topology.VALIDATED_WITH_ERRORS);
        List<TopologyError> errorList = topology2.getTopologyErrorsByLyr(lineLyrWithCollapsedCoords, null, true);
        assertTrue(errorList.size() == 1);
        errorList = topology.getTopologyErrorsByShapeType(FShape.LINE, null, true);
        assertTrue(errorList.size() == 1);
    }

    public void testTopologyPersistence() throws RuleNotAllowedException, TopologyRuleDefinitionException {
        topology2 = new Topology(mapContext, ROOT, 0.01, 1000, errorContainer);
        topology2.addLayer(this.shapeBasedLinearLyr);
        MustNotHaveRepeatedPoints ruleA = new MustNotHaveRepeatedPoints(topology2, shapeBasedLinearLyr);
        topology2.addRule(ruleA);
        GeometryMustHaveValidCoordinates ruleB = new GeometryMustHaveValidCoordinates(topology2, shapeBasedLinearLyr);
        topology2.addRule(ruleB);
        String fileToSave1 = "/testTopology.xml";
        Map<String, Object> storageParams = new HashMap<String, Object>();
        storageParams.put(TopologyPersister.FILE_PARAM_NAME, fileToSave1);
        TopologyPersister.persist(topology2, storageParams);
        Topology topologyA = TopologyPersister.load(mapContext, storageParams);
        assertTrue(topology2.getRuleCount() == topologyA.getRuleCount());
        assertTrue(topology2.getLayerCount() == topologyA.getLayerCount());
    }
}
