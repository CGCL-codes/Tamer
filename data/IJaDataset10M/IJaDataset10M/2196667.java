package tryouts.multiagentsimulation.hw6;

import org.matsim.core.api.experimental.events.LinkLeaveEvent;
import org.matsim.core.api.experimental.events.handler.LinkLeaveEventHandler;

/**
 * @author thomas
 *
 */
public class Plotter {

    /**
	 * @param args
	 */
    public static void main(String[] args) {
    }

    public class Before implements LinkLeaveEventHandler {

        @Override
        public void handleEvent(LinkLeaveEvent event) {
        }

        @Override
        public void reset(int iteration) {
        }
    }

    public class After implements LinkLeaveEventHandler {

        @Override
        public void handleEvent(LinkLeaveEvent event) {
        }

        @Override
        public void reset(int iteration) {
        }
    }
}
