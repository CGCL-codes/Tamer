package air;

import org.matsim.api.core.v01.Scenario;
import org.matsim.api.core.v01.network.Link;
import org.matsim.api.core.v01.network.Network;
import org.matsim.core.network.MatsimNetworkReader;
import org.matsim.core.scenario.ScenarioUtils;
import org.matsim.core.utils.geometry.geotools.MGC;
import org.matsim.core.utils.geometry.transformations.TransformationFactory;
import org.matsim.core.utils.misc.ConfigUtils;
import org.matsim.utils.gis.matsim2esri.network.FeatureGenerator;
import org.matsim.utils.gis.matsim2esri.network.FeatureGeneratorBuilder;
import org.matsim.utils.gis.matsim2esri.network.LineStringBasedFeatureGenerator;
import org.matsim.utils.gis.matsim2esri.network.Links2ESRIShape;
import org.matsim.utils.gis.matsim2esri.network.WidthCalculator;

public class DgNet2Shape {

    public static void main(String[] args) {
        String netFile = "/home/soeren/workspace/germanAirNetwork.xml";
        Scenario scenario = ScenarioUtils.createScenario(ConfigUtils.createConfig());
        Network net = scenario.getNetwork();
        new MatsimNetworkReader(scenario).readFile(netFile);
        final WidthCalculator wc = new WidthCalculator() {

            @Override
            public double getWidth(Link link) {
                return 1.0;
            }
        };
        FeatureGeneratorBuilder builder = new FeatureGeneratorBuilder() {

            @Override
            public FeatureGenerator createFeatureGenerator() {
                FeatureGenerator fg = new LineStringBasedFeatureGenerator(wc, MGC.getCRS(TransformationFactory.WGS84));
                return fg;
            }
        };
        new Links2ESRIShape(net, "/home/soeren/workspace/networkDE.shp", builder).write();
    }
}
