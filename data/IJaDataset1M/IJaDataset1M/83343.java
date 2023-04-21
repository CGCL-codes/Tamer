package org.matsim.utils.gis.matsim2esri.network;

import java.util.ArrayList;
import java.util.Collection;
import org.apache.log4j.Logger;
import org.geotools.feature.Feature;
import org.matsim.api.core.v01.network.Link;
import org.matsim.api.core.v01.network.Network;
import org.matsim.core.config.ConfigUtils;
import org.matsim.core.network.MatsimNetworkReader;
import org.matsim.core.scenario.ScenarioImpl;
import org.matsim.core.scenario.ScenarioUtils;
import org.matsim.core.utils.geometry.geotools.MGC;
import org.matsim.core.utils.gis.ShapeFileWriter;
import org.matsim.core.utils.misc.NetworkUtils;
import org.opengis.referencing.crs.CoordinateReferenceSystem;

/**
 * Simple class to convert the links of MATSim network files to ESRI shape files. The network could be written either
 * as line strings or as polygons. Furthermore the width of the links could be calculated according to
 * freespeed, lanes or capacity. For some basic examples please have a look at the <code>main</code> method.
 * Can also be called as Links2ESRIShape inputNetwork.xml outputAsLines.shp outputAsPolygons.shp .
 *
 * <p> <strong>Keywords:</strong> converter, network, links, esri, shp, matsim </p>
 *
 * @author laemmel
 */
public class Links2ESRIShape {

    private static Logger log = Logger.getLogger(Links2ESRIShape.class);

    private final FeatureGenerator featureGenerator;

    private final Network network;

    private final String filename;

    public Links2ESRIShape(final Network network, final String filename, final String coordinateSystem) {
        this(network, filename, new FeatureGeneratorBuilderImpl(network, coordinateSystem));
    }

    public Links2ESRIShape(final Network network, final String filename, final FeatureGeneratorBuilder builder) {
        this.network = network;
        this.filename = filename;
        this.featureGenerator = builder.createFeatureGenerator();
    }

    public void write() {
        log.info("creating features...");
        Collection<Feature> features = new ArrayList<Feature>();
        for (Link link : NetworkUtils.getSortedLinks(this.network)) {
            features.add(this.featureGenerator.getFeature(link));
        }
        log.info("writing features to shape file... " + this.filename);
        ShapeFileWriter.writeGeometries(features, this.filename);
        log.info("done writing shape file.");
    }

    public static void main(final String[] args) {
        String netfile = null;
        String outputFileLs = null;
        String outputFileP = null;
        String defaultCRS = "DHDN_GK4";
        boolean commonWealth = false;
        if (args.length == 0) {
            netfile = "./examples/equil/network.xml";
            outputFileLs = "./plans/networkLs.shp";
            outputFileP = "./plans/networkP.shp";
        } else if (args.length == 3) {
            netfile = args[0];
            outputFileLs = args[1];
            outputFileP = args[2];
        } else if (args.length == 4) {
            netfile = args[0];
            outputFileLs = args[1];
            outputFileP = args[2];
            defaultCRS = args[3];
        } else if (args.length == 5) {
            netfile = args[0];
            outputFileLs = args[1];
            outputFileP = args[2];
            defaultCRS = args[3];
            commonWealth = Boolean.parseBoolean(args[4]);
        } else {
            log.error("Arguments cannot be interpreted.  Aborting ...");
            System.exit(-1);
        }
        ScenarioImpl scenario = (ScenarioImpl) ScenarioUtils.createScenario(ConfigUtils.createConfig());
        scenario.getConfig().global().setCoordinateSystem(defaultCRS);
        log.info("loading network from " + netfile);
        final Network network = scenario.getNetwork();
        new MatsimNetworkReader(scenario).readFile(netfile);
        log.info("done.");
        FeatureGeneratorBuilderImpl builder = new FeatureGeneratorBuilderImpl(network, defaultCRS);
        builder.setFeatureGeneratorPrototype(LineStringBasedFeatureGenerator.class);
        builder.setWidthCoefficient(0.5);
        builder.setWidthCalculatorPrototype(LanesBasedWidthCalculator.class);
        new Links2ESRIShape(network, outputFileLs, builder).write();
        CoordinateReferenceSystem crs = MGC.getCRS(defaultCRS);
        builder.setWidthCoefficient((commonWealth ? -1 : 1) * 0.003);
        builder.setFeatureGeneratorPrototype(PolygonFeatureGenerator.class);
        builder.setWidthCalculatorPrototype(CapacityBasedWidthCalculator.class);
        builder.setCoordinateReferenceSystem(crs);
        new Links2ESRIShape(network, outputFileP, builder).write();
    }
}
