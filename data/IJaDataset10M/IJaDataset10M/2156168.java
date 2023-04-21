package com.iver.cit.gvsig.graphtests;

import java.io.File;
import junit.framework.TestCase;
import org.cresques.cts.IProjection;
import org.gvsig.exceptions.BaseException;
import org.gvsig.graph.core.GvFlag;
import org.gvsig.graph.core.IGraph;
import org.gvsig.graph.core.Network;
import org.gvsig.graph.core.loaders.NetworkRedLoader;
import org.gvsig.graph.solvers.CompactAreaExtractor;
import org.gvsig.graph.solvers.OneToManySolver;
import org.gvsig.graph.solvers.ServiceAreaExtractor2;
import com.iver.cit.gvsig.fmap.crs.CRSFactory;
import com.iver.cit.gvsig.fmap.layers.FLyrVect;
import com.iver.cit.gvsig.fmap.layers.LayerFactory;

/**
 * @author fjp
 *
 * A partir de un iterador de Features (de l�neas), implementamos probamos que
 * podemos generar un pol�gono (si puede ser, convexo) que las
 * englobe todas. 
 */
public class TestServiceAreaPolygon extends TestCase {

    FLyrVect lyr;

    Network net;

    IGraph g;

    protected void setUp() throws Exception {
        super.setUp();
        LayerFactory.setDriversPath("../_fwAndami/gvSIG/extensiones/com.iver.cit.gvsig/drivers");
        IProjection prj = CRSFactory.getCRS("EPSG:23030");
        File shpFile = new File("test_files/ejes.shp");
        lyr = (FLyrVect) LayerFactory.createLayer("Ejes", "gvSIG shp driver", shpFile, prj);
        NetworkRedLoader netLoader = new NetworkRedLoader();
        netLoader.setNetFile(new File("test_files/ejes.net"));
        g = netLoader.loadNetwork();
        net = new Network();
    }

    public void testCalculate() throws BaseException {
        OneToManySolver solver = new OneToManySolver();
        net.setLayer(lyr);
        net.setGraph(g);
        solver.setNetwork(net);
        ServiceAreaExtractor2 extractor = new ServiceAreaExtractor2(net);
        solver.addListener(extractor);
        CompactAreaExtractor compact = new CompactAreaExtractor(net);
        solver.addListener(compact);
        GvFlag sourceFlag = net.createFlag(441901, 4475977, 10);
        extractor.setIdFlag(0);
        net.addFlag(sourceFlag);
        solver.setSourceFlag(sourceFlag);
        long t1 = System.currentTimeMillis();
        solver.putDestinationsOnNetwork(net.getFlags());
        solver.setExploreAllNetwork(true);
        solver.setMaxDistance(1000.0);
        double[] costs = { 500.0, 1000.0 };
        extractor.setCosts(costs);
        solver.calculate();
        extractor.writeServiceArea();
        extractor.closeFiles();
        solver.removeDestinationsFromNetwork(net.getFlags());
        compact.writeServiceArea();
        compact.closeFiles();
        long t2 = System.currentTimeMillis();
        System.out.println("tiempo:" + (t2 - t1));
    }
}
