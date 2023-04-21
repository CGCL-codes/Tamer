package playground.wdoering.debugvisualization;

import org.matsim.api.core.v01.Scenario;
import org.matsim.core.api.experimental.events.EventsManager;
import org.matsim.core.config.Config;
import org.matsim.core.config.ConfigUtils;
import org.matsim.core.events.EventsUtils;
import org.matsim.core.scenario.ScenarioUtils;
import playground.gregor.sim2d_v2.events.XYVxVyEventsFileReader;
import playground.wdoering.debugvisualization.controller.Console;
import playground.wdoering.debugvisualization.controller.ConsoleImpl;
import playground.wdoering.debugvisualization.controller.Controller;
import playground.wdoering.debugvisualization.controller.XYVxVyEventThread;

/**
 * Debug Visualization
 *
 * video editing akin exploration of events
 * displaying traces of agents.
 *
 * @author wdoering
 *
 */
public class DebugVisualization {

    public static void main(final String[] args) {
        Console console = new ConsoleImpl(false);
        Config c = ConfigUtils.createConfig();
        c.network().setInputFile(args[1]);
        Scenario sc = ScenarioUtils.loadScenario(c);
        EventsManager e = EventsUtils.createEventsManager();
        if ((args.length > 0) && (!args[0].equals(""))) {
            String eventFile = args[0];
            String shapeFile = args[2];
            XYVxVyEventsFileReader reader = new XYVxVyEventsFileReader(e);
            Thread readerThread = new Thread(new XYVxVyEventThread(reader, eventFile), "readerthread");
            Controller controller = new Controller(e, sc, console, readerThread, shapeFile);
        } else {
            console.println("Too few arguments.");
            System.exit(0);
        }
    }
}
