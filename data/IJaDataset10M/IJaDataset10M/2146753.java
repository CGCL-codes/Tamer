package playground.dgrether.signalsystems.otfvis;

import org.matsim.api.core.v01.Scenario;
import org.matsim.contrib.otfvis.OTFVis;
import org.matsim.core.api.experimental.events.EventsManager;
import org.matsim.core.events.EventsUtils;
import org.matsim.core.mobsim.qsim.QSim;
import org.matsim.core.scenario.ScenarioLoaderImpl;
import org.matsim.vis.otfvis.OTFClientLive;
import org.matsim.vis.otfvis.OnTheFlyServer;
import playground.dgrether.DgPaths;

/**
 * @author dgrether
 *
 */
public class DenverStarter {

    /**
	 * @param args
	 */
    public static void main(String[] args) {
        String configFile = DgPaths.STUDIESDG + "denver/dgConfig.xml";
        ScenarioLoaderImpl scl = ScenarioLoaderImpl.createScenarioLoaderImplAndResetRandomSeed(configFile);
        Scenario sc = scl.loadScenario();
        EventsManager e = (EventsManager) EventsUtils.createEventsManager();
        QSim otfVisQSim = QSim.createQSimAndAddAgentSource(sc, e);
        OnTheFlyServer server = OTFVis.startServerAndRegisterWithQSim(sc.getConfig(), sc, e, otfVisQSim);
        OTFClientLive.run(sc.getConfig(), server);
        otfVisQSim.run();
    }
}
