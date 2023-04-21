package playground.wrashid.oswald;

import java.util.HashMap;
import org.matsim.api.core.v01.Coord;
import org.matsim.api.core.v01.Id;
import org.matsim.api.core.v01.network.Network;
import org.matsim.core.api.experimental.events.ActivityEndEvent;
import org.matsim.core.api.experimental.events.ActivityStartEvent;
import org.matsim.core.api.experimental.events.AgentArrivalEvent;
import org.matsim.core.api.experimental.events.AgentDepartureEvent;
import org.matsim.core.api.experimental.events.AgentMoneyEvent;
import org.matsim.core.api.experimental.events.AgentStuckEvent;
import org.matsim.core.api.experimental.events.AgentWait2LinkEvent;
import org.matsim.core.api.experimental.events.EventsManager;
import org.matsim.core.api.experimental.events.LinkEnterEvent;
import org.matsim.core.api.experimental.events.LinkLeaveEvent;
import org.matsim.core.api.experimental.events.handler.ActivityEndEventHandler;
import org.matsim.core.api.experimental.events.handler.ActivityStartEventHandler;
import org.matsim.core.api.experimental.events.handler.AgentArrivalEventHandler;
import org.matsim.core.api.experimental.events.handler.AgentDepartureEventHandler;
import org.matsim.core.api.experimental.events.handler.AgentMoneyEventHandler;
import org.matsim.core.api.experimental.events.handler.AgentStuckEventHandler;
import org.matsim.core.api.experimental.events.handler.AgentWait2LinkEventHandler;
import org.matsim.core.api.experimental.events.handler.LinkEnterEventHandler;
import org.matsim.core.api.experimental.events.handler.LinkLeaveEventHandler;
import org.matsim.core.events.EventsReaderXMLv1;
import org.matsim.core.events.EventsUtils;
import playground.wrashid.lib.GeneralLib;

/**
 * write out first non-stationary position of agents after
 * 'simulationStartTimeInSeconds' and last position before
 * 'simulationEndTimeInSeconds' furthermore specify, if last position was for
 * arrival event or not.
 * 
 * @author wrashid
 * 
 */
public class TravelDemand {

    public static void main(String[] args) {
        String eventsFile = "H:/data/experiments/TRBAug2011/runs/ktiRun24/output/ITERS/it.50/50.events.xml.gz";
        double simulationStartTimeInSeconds = 8 * 3600;
        double simulationEndTimeInSeconds = simulationStartTimeInSeconds + 10 * 60;
        EventsManager events = (EventsManager) EventsUtils.createEventsManager();
        CollectPositionOfVehicles collectPositionOfVehicles = new CollectPositionOfVehicles(simulationStartTimeInSeconds, simulationEndTimeInSeconds);
        events.addHandler(collectPositionOfVehicles);
        EventsReaderXMLv1 reader = new EventsReaderXMLv1(events);
        reader.parse(eventsFile);
        HashMap<Id, Id> startPositions = collectPositionOfVehicles.getStartPositions();
        HashMap<Id, Id> endPositions = collectPositionOfVehicles.getEndPositions();
        HashMap<Id, Boolean> lastEventWasArrivalEvent = collectPositionOfVehicles.getLastEventWasArrivalEvent();
        Network network = GeneralLib.readNetwork("H:/data/experiments/TRBAug2011/runs/ktiRun24/output/output_network.xml.gz");
        System.out.println("personId\tstartPos-x\tstartPos-y\tendPos-x\tendPos-y\tdidArrive");
        for (Id personId : startPositions.keySet()) {
            Coord startCoord = network.getLinks().get(startPositions.get(personId)).getCoord();
            if (endPositions.get(personId) != null) {
                Coord endCoord = network.getLinks().get(endPositions.get(personId)).getCoord();
                System.out.println(personId + "\t" + startCoord.getX() + "\t" + startCoord.getY() + "\t" + endCoord.getX() + "\t" + endCoord.getY() + "\t" + (lastEventWasArrivalEvent.get(personId) ? "1" : "0"));
            }
        }
    }

    private static class CollectPositionOfVehicles implements AgentArrivalEventHandler, LinkEnterEventHandler, LinkLeaveEventHandler {

        private HashMap<Id, Id> startPositions = new HashMap<Id, Id>();

        private HashMap<Id, Id> endPosition = new HashMap<Id, Id>();

        private HashMap<Id, Boolean> lastEventWasArrivalEvent = new HashMap<Id, Boolean>();

        private final double simulationStartTimeInSeconds;

        private final double simulationEndTimeInSeconds;

        public CollectPositionOfVehicles(double simulationStartTimeInSeconds, double simulationEndTimeInSeconds) {
            this.simulationStartTimeInSeconds = simulationStartTimeInSeconds;
            this.simulationEndTimeInSeconds = simulationEndTimeInSeconds;
        }

        public HashMap<Id, Boolean> getLastEventWasArrivalEvent() {
            return lastEventWasArrivalEvent;
        }

        public HashMap<Id, Id> getEndPositions() {
            return endPosition;
        }

        public HashMap<Id, Id> getStartPositions() {
            return startPositions;
        }

        @Override
        public void reset(int iteration) {
        }

        @Override
        public void handleEvent(LinkLeaveEvent event) {
            double time = event.getTime();
            Id personId = event.getPersonId();
            Id linkId = event.getLinkId();
            boolean isArrival = false;
            updatePosition(time, personId, linkId, isArrival);
        }

        private void updatePosition(double time, Id personId, Id linkId, boolean isArrival) {
            if (time > simulationStartTimeInSeconds && time < simulationEndTimeInSeconds) {
                if (!startPositions.containsKey(personId)) {
                    startPositions.put(personId, linkId);
                } else {
                    endPosition.put(personId, linkId);
                    lastEventWasArrivalEvent.put(personId, isArrival);
                }
            }
        }

        @Override
        public void handleEvent(LinkEnterEvent event) {
            double time = event.getTime();
            Id personId = event.getPersonId();
            Id linkId = event.getLinkId();
            boolean isArrival = false;
            updatePosition(time, personId, linkId, isArrival);
        }

        @Override
        public void handleEvent(AgentArrivalEvent event) {
            double time = event.getTime();
            Id personId = event.getPersonId();
            Id linkId = event.getLinkId();
            boolean isArrival = true;
            updatePosition(time, personId, linkId, isArrival);
        }
    }
}
