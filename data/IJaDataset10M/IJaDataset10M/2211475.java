package playground.balmermi.world;

import java.util.Stack;
import org.matsim.api.core.v01.Scenario;
import org.matsim.core.gbl.Gbl;
import org.matsim.core.scenario.ScenarioImpl;
import org.matsim.core.utils.io.MatsimXmlParser;
import org.xml.sax.Attributes;

/**
 * A reader for world files of MATSim according to <code>world_v2.dtd</code>.
 *
 * @author mrieser
 * @author balmermi
 */
public class WorldReaderMatsimV2 extends MatsimXmlParser {

    private static final String WORLD = "world";

    private static final String LAYER = "layer";

    private static final String MAPPING = "mapping";

    private static final String ZONE = "zone";

    private static final String REF = "ref";

    private Scenario scenario;

    private World world;

    private ZoneLayer currLayer = null;

    public WorldReaderMatsimV2(final ScenarioImpl scenario, World world) {
        this.scenario = scenario;
        this.world = world;
    }

    @Override
    public void startTag(final String name, final Attributes atts, final Stack<String> context) {
        if (LAYER.equals(name)) {
            startLayer(atts);
        } else if (ZONE.equals(name)) {
            startZone(atts);
        } else if (!WORLD.equals(name) && !MAPPING.equals(name) && !REF.equals(name)) {
            Gbl.errorMsg(this + "[tag=" + name + " not known or not supported]");
        }
    }

    @Override
    public void endTag(final String name, final String content, final Stack<String> context) {
        if (WORLD.equals(name)) {
            this.world = null;
        } else if (LAYER.equals(name)) {
            this.currLayer = null;
        }
    }

    private void startLayer(final Attributes meta) {
        this.currLayer = (ZoneLayer) this.world.createLayer(scenario.createId(meta.getValue("type")));
    }

    private void startZone(final Attributes atts) {
        this.currLayer.createZone(scenario.createId(atts.getValue("id")), atts.getValue("center_x"), atts.getValue("center_y"), atts.getValue("min_x"), atts.getValue("min_y"), atts.getValue("max_x"), atts.getValue("max_y"));
    }
}
