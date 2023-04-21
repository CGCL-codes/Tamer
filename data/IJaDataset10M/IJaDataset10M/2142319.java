package playground.dgrether.cmcf;

import org.apache.log4j.Logger;
import org.matsim.api.core.v01.network.Link;
import org.matsim.api.core.v01.network.Network;
import org.matsim.contrib.otfvis.OTFVis;
import org.matsim.core.basic.v01.IdImpl;
import org.matsim.core.config.Config;
import org.matsim.core.controler.Controler;
import org.matsim.core.controler.events.IterationEndsEvent;
import org.matsim.core.controler.events.IterationStartsEvent;
import org.matsim.core.controler.events.StartupEvent;
import org.matsim.core.controler.listener.IterationEndsListener;
import org.matsim.core.controler.listener.IterationStartsListener;
import org.matsim.core.controler.listener.StartupListener;
import playground.dgrether.linkanalysis.InOutGraphWriter;
import playground.dgrether.linkanalysis.TTGraphWriter;
import playground.dgrether.linkanalysis.TTInOutflowEventHandler;
import playground.dgrether.utils.MatsimIo;

/**
 * @author dgrether
 *
 */
public class CMCFRunnerNoReroute {

    private static final Logger log = Logger.getLogger(CMCFRunnerNoReroute.class);

    private static final boolean visualizationOnly = false;

    private class MyIterationStartsEndsListener implements IterationEndsListener, IterationStartsListener {

        TTInOutflowEventHandler handler3 = new TTInOutflowEventHandler(new IdImpl("3"), new IdImpl("5"));

        TTInOutflowEventHandler handler4 = new TTInOutflowEventHandler(new IdImpl("4"));

        public void notifyIterationStarts(IterationStartsEvent event) {
            if (event.getIteration() == event.getControler().getConfig().controler().getLastIteration()) {
                event.getControler().getEvents().addHandler(handler3);
                event.getControler().getEvents().addHandler(handler4);
            }
        }

        public void notifyIterationEnds(IterationEndsEvent event) {
            if (event.getIteration() == event.getControler().getConfig().controler().getLastIteration()) {
            }
        }
    }

    ;

    public CMCFRunnerNoReroute() {
        Config config = null;
        CMCFScenarioGeneratorNoReroute.main(null);
        final TTInOutflowEventHandler myHandler = new TTInOutflowEventHandler(new IdImpl("4"));
        if (!visualizationOnly) {
            Controler controler = new Controler(CMCFScenarioGeneratorNoReroute.configOut);
            controler.setOverwriteFiles(true);
            final TTInOutflowEventHandler handler3 = new TTInOutflowEventHandler(new IdImpl("3"), new IdImpl("5"));
            final TTInOutflowEventHandler handler4 = new TTInOutflowEventHandler(new IdImpl("4"));
            controler.addControlerListener(new StartupListener() {

                public void notifyStartup(StartupEvent e) {
                    e.getControler().getEvents().addHandler(handler3);
                    e.getControler().getEvents().addHandler(handler4);
                }
            });
            controler.addControlerListener(new IterationEndsListener() {

                public void notifyIterationEnds(IterationEndsEvent event) {
                    TTGraphWriter ttWriter = new TTGraphWriter();
                    ttWriter.addTTEventHandler(handler3);
                    ttWriter.addTTEventHandler(handler4);
                    ttWriter.writeTTChart(event.getControler().getControlerIO().getIterationPath(event.getIteration()), event.getIteration());
                    InOutGraphWriter inoutWriter = new InOutGraphWriter();
                    inoutWriter.addInOutEventHandler(handler3);
                    inoutWriter.addInOutEventHandler(handler4);
                    inoutWriter.writeInOutChart(event.getControler().getControlerIO().getIterationPath(event.getIteration()), event.getIteration());
                }
            });
            controler.run();
            Network net = controler.getNetwork();
            Link link2 = net.getLinks().get(new IdImpl("2"));
            Link link3 = net.getLinks().get(new IdImpl("3"));
            Link link4 = net.getLinks().get(new IdImpl("4"));
            Link link5 = net.getLinks().get(new IdImpl("5"));
            for (int i = 1; i <= 3600; i++) {
                double tt = controler.getTravelTimeCalculator().getLinkTravelTime(link4, i);
                double tt3 = controler.getTravelTimeCalculator().getLinkTravelTime(link3, i);
                double tt5 = controler.getTravelTimeCalculator().getLinkTravelTime(link5, i);
                log.info("tt on link 4 at time: " + i + " " + tt);
                log.info("tt on link 3 at time: " + i + " " + tt3);
                log.info("tt on link 5 at time: " + i + " " + tt5);
            }
            config = controler.getConfig();
        }
        if (config == null) {
            config = new Config();
            config.addCoreModules();
            config = MatsimIo.loadConfig(config, CMCFScenarioGeneratorNoReroute.configOut);
        }
        System.out.println(config.controler());
        String[] args = { config.controler().getOutputDirectory() + "/ITERS/it." + config.controler().getLastIteration() + "/" + config.controler().getLastIteration() + ".otfvis.mvi" };
        OTFVis.main(args);
    }

    /**
	 * @param args
	 */
    public static void main(String[] args) {
        new CMCFRunnerNoReroute();
    }
}
