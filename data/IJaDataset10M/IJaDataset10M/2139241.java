package playground.yu.test;

import java.io.IOException;
import org.matsim.controler.Controler;
import org.matsim.controler.events.IterationEndsEvent;
import org.matsim.controler.events.IterationStartsEvent;
import org.matsim.controler.events.ShutdownEvent;
import org.matsim.controler.listener.IterationEndsListener;
import org.matsim.controler.listener.IterationStartsListener;
import org.matsim.controler.listener.ShutdownListener;
import playground.yu.analysis.PtCheck;
import playground.yu.analysis.TravelTimeModalSplit;

/**
 * @author yu
 * 
 */
public class TravelTimeRoadPricingControler extends Controler {

    private static class TTRPlistener implements IterationEndsListener, IterationStartsListener, ShutdownListener {

        private final PtCheck pc;

        private TravelTimeModalSplit ttms = null;

        public TTRPlistener(String ptCheckFilename) throws IOException {
            pc = new PtCheck(ptCheckFilename);
        }

        public void notifyIterationEnds(IterationEndsEvent event) {
            Controler ctl = event.getControler();
            int idx = event.getIteration();
            if (idx % 10 == 0) {
                pc.resetCnt();
                pc.run(ctl.getPopulation());
                try {
                    pc.write(idx);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            if (idx == ctl.getLastIteration()) {
                if (ttms != null) {
                    ttms.write(getOutputFilename("traveltimes.txt"));
                }
            }
        }

        public void notifyShutdown(ShutdownEvent event) {
            try {
                pc.writeEnd();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        public void notifyIterationStarts(IterationStartsEvent event) {
            Controler c = event.getControler();
            if (event.getIteration() == c.getLastIteration()) {
                ttms = new TravelTimeModalSplit(3600, c.getNetwork(), c.getPopulation());
                c.getEvents().addHandler(ttms);
            }
        }
    }

    /**
	 * @param configFileName
	 */
    public TravelTimeRoadPricingControler(String[] configFileName) {
        super(configFileName);
    }

    /**
	 * @param args
	 */
    public static void main(String[] args) {
        final TravelTimeRoadPricingControler c = new TravelTimeRoadPricingControler(args);
        try {
            c.addControlerListener(new TTRPlistener("test/yu/travelTimeRoadPricing/200-0.00005.txt"));
        } catch (IOException e) {
            e.printStackTrace();
        }
        c.run();
        System.exit(0);
    }
}
