package playground.yu.timeToll;

import org.matsim.analysis.CalcLegTimes;
import org.matsim.core.controler.Controler;
import org.matsim.core.controler.events.IterationEndsEvent;
import org.matsim.core.controler.events.IterationStartsEvent;
import org.matsim.core.controler.listener.IterationEndsListener;
import org.matsim.core.controler.listener.IterationStartsListener;
import org.matsim.testcases.MatsimTestCase;

/**
 * @author ychen
 *
 */
public class TimeTollTest extends MatsimTestCase {

    private static class TestControlerListener implements IterationEndsListener, IterationStartsListener {

        private CalcLegTimes clt = null;

        @Override
        public void notifyIterationEnds(final IterationEndsEvent event) {
            if (event.getIteration() == event.getControler().getLastIteration() && clt != null) {
                double traveling = event.getControler().getConfig().planCalcScore().getTraveling_utils_hr();
                double criterion = 0;
                if (traveling == -30.0) {
                    criterion = 1710.0;
                    assertEquals(clt.getAverageTripDuration() < criterion, true);
                } else if (traveling == -6.0) {
                    criterion = 1720.0;
                    assertEquals(clt.getAverageTripDuration() > criterion, true);
                }
            }
        }

        @Override
        public void notifyIterationStarts(final IterationStartsEvent event) {
            if (event.getIteration() == event.getControler().getLastIteration()) {
                clt = new CalcLegTimes();
                event.getControler().getEvents().addHandler(clt);
            }
        }
    }

    public void testBetaTraveling_6() {
        Controler ctl = new Controler(getInputDirectory() + "config.xml");
        ctl.addControlerListener(new TestControlerListener());
        ctl.setCreateGraphs(false);
        ctl.setOverwriteFiles(true);
        ctl.run();
    }

    public void testBetaTraveling_30() {
        Controler ctl = new Controler(getInputDirectory() + "config.xml");
        ctl.addControlerListener(new TestControlerListener());
        ctl.setCreateGraphs(false);
        ctl.setOverwriteFiles(true);
        ctl.run();
    }
}
