package playground.jjoubert.CommercialModel.Listeners;

import org.apache.log4j.Logger;
import org.matsim.core.controler.events.ScoringEvent;
import org.matsim.core.controler.listener.ScoringListener;

public class MyScoringListener implements ScoringListener {

    private final Logger log = Logger.getLogger(MyScoringListener.class);

    public void notifyScoring(ScoringEvent event) {
        log.info("  --> And look, my ScoringListener works too");
    }
}
