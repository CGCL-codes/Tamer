package org.matsim.core.mobsim.jdeqsim;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import org.matsim.api.core.v01.Id;
import org.matsim.api.core.v01.Scenario;
import org.matsim.api.core.v01.TransportMode;
import org.matsim.api.core.v01.population.Activity;
import org.matsim.api.core.v01.population.Leg;
import org.matsim.api.core.v01.population.Person;
import org.matsim.api.core.v01.population.Plan;
import org.matsim.api.core.v01.population.PlanElement;
import org.matsim.api.core.v01.population.Population;
import org.matsim.core.api.experimental.events.PersonEvent;
import org.matsim.core.api.experimental.events.handler.PersonEventHandler;
import org.matsim.core.events.ActivityEndEventImpl;
import org.matsim.core.events.ActivityStartEventImpl;
import org.matsim.core.events.AgentArrivalEventImpl;
import org.matsim.core.events.AgentDepartureEventImpl;
import org.matsim.core.events.AgentWait2LinkEventImpl;
import org.matsim.core.events.EventsManagerImpl;
import org.matsim.core.events.LinkEnterEventImpl;
import org.matsim.core.events.LinkLeaveEventImpl;
import org.matsim.core.events.parallelEventsHandler.ParallelEventsManagerImpl;
import org.matsim.core.mobsim.jdeqsim.util.CppEventFileParser;
import org.matsim.core.mobsim.jdeqsim.util.EventLibrary;
import org.matsim.core.population.routes.NetworkRoute;
import org.matsim.testcases.MatsimTestCase;

public abstract class AbstractJDEQSimTest extends MatsimTestCase {

    protected HashMap<Id, LinkedList<PersonEvent>> eventsByPerson = null;

    public LinkedList<PersonEvent> allEvents = null;

    @Override
    protected void setUp() throws Exception {
        super.setUp();
        this.eventsByPerson = new HashMap<Id, LinkedList<PersonEvent>>();
        this.allEvents = new LinkedList<PersonEvent>();
    }

    @Override
    protected void tearDown() throws Exception {
        this.eventsByPerson = null;
        this.allEvents = null;
        SimulationParameters.reset();
        super.tearDown();
    }

    public void runJDEQSim(Scenario scenario) {
        EventsManagerImpl events = new ParallelEventsManagerImpl(1);
        events.addHandler(new PersonEventCollector(this.eventsByPerson, this.allEvents));
        events.initProcessing();
        new JDEQSimulation(scenario, events).run();
        events.finishProcessing();
    }

    protected void checkAscendingTimeStamps() {
        double lastTimeStamp;
        for (LinkedList<PersonEvent> list : eventsByPerson.values()) {
            lastTimeStamp = Double.NEGATIVE_INFINITY;
            for (int i = 0; i < list.size(); i++) {
                if (lastTimeStamp > list.get(i).getTime()) {
                    for (int j = 0; j < list.size(); j++) {
                        System.out.println(list.get(j).toString());
                    }
                    System.out.println(lastTimeStamp);
                    System.out.println(list.get(i).getTime());
                    fail("Messages are not arriving in a consistent manner.");
                }
                assertTrue(lastTimeStamp <= list.get(i).getTime());
                lastTimeStamp = list.get(i).getTime();
            }
        }
    }

    /**
	 * Compares plan and events for each agent.
	 * Checks the type of the event and the linkId.
	 */
    protected void checkEventsCorrespondToPlans(final Population population) {
        for (LinkedList<PersonEvent> list : eventsByPerson.values()) {
            Person p = population.getPersons().get(list.get(0).getPersonId());
            Plan plan = p.getSelectedPlan();
            int index = 0;
            Activity act = null;
            Leg leg = null;
            for (PlanElement pe : plan.getPlanElements()) {
                if (pe instanceof Activity) {
                    act = (Activity) pe;
                    if (leg != null) {
                        if (leg.getMode().equals(TransportMode.car) && ((NetworkRoute) leg.getRoute()).getLinkIds().size() > 0) {
                            assertTrue(list.get(index) instanceof LinkEnterEventImpl);
                            assertTrue(act.getLinkId().toString().equalsIgnoreCase(((LinkEnterEventImpl) list.get(index)).getLinkId().toString()));
                            index++;
                        }
                        assertTrue(list.get(index) instanceof AgentArrivalEventImpl);
                        assertTrue(act.getLinkId().toString().equalsIgnoreCase(((AgentArrivalEventImpl) list.get(index)).getLinkId().toString()));
                        index++;
                        assertTrue(list.get(index) instanceof ActivityStartEventImpl);
                        assertEquals(act.getLinkId(), ((ActivityStartEventImpl) list.get(index)).getLinkId());
                        index++;
                    }
                } else if (pe instanceof Leg) {
                    leg = (Leg) pe;
                    assertTrue(list.get(index) instanceof ActivityEndEventImpl);
                    assertEquals(act.getLinkId(), ((ActivityEndEventImpl) list.get(index)).getLinkId());
                    index++;
                    assertTrue(list.get(index) instanceof AgentDepartureEventImpl);
                    assertTrue(act.getLinkId().toString().equalsIgnoreCase(((AgentDepartureEventImpl) list.get(index)).getLinkId().toString()));
                    index++;
                    if (leg.getMode().equals(TransportMode.car)) {
                        if (((NetworkRoute) leg.getRoute()).getLinkIds().size() > 0) {
                            assertTrue(list.get(index) instanceof AgentWait2LinkEventImpl);
                            assertTrue(act.getLinkId().toString().equalsIgnoreCase(((AgentWait2LinkEventImpl) list.get(index)).getLinkId().toString()));
                            index++;
                            assertTrue(list.get(index) instanceof LinkLeaveEventImpl);
                            assertTrue(act.getLinkId().toString().equalsIgnoreCase(((LinkLeaveEventImpl) list.get(index)).getLinkId().toString()));
                            index++;
                        }
                        for (Id linkId : ((NetworkRoute) leg.getRoute()).getLinkIds()) {
                            assertTrue(list.get(index) instanceof LinkEnterEventImpl);
                            assertTrue(linkId.equals(((LinkEnterEventImpl) list.get(index)).getLinkId()));
                            index++;
                            assertTrue(list.get(index) instanceof LinkLeaveEventImpl);
                            assertTrue(linkId.equals(((LinkLeaveEventImpl) list.get(index)).getLinkId()));
                            index++;
                        }
                    }
                }
            }
        }
    }

    /**
	 * Compare events to deq event file. The order of events must also be the
	 * same. (this test will only succeed for simple tests with one car
	 * often!!!) => reason: at junctions the order of cars can change + stuck
	 * vehicles are dealt with in different ways
	 */
    protected void compareToDEQSimEvents(final String deqsimEventsFile) {
        LinkedList<PersonEvent> copyEventList = new LinkedList<PersonEvent>();
        for (int i = 0; i < allEvents.size(); i++) {
            if (!(allEvents.get(i) instanceof ActivityStartEventImpl || allEvents.get(i) instanceof ActivityEndEventImpl)) {
                copyEventList.add(allEvents.get(i));
            }
        }
        ArrayList<EventLog> deqSimLog = CppEventFileParser.parseFile(deqsimEventsFile);
        for (int i = 0; i < copyEventList.size(); i++) {
            assertTrue("events not equal.", CppEventFileParser.equals(copyEventList.get(i), deqSimLog.get(i)));
        }
    }

    /**
	 * Compares the sum of all travel times with the sum of all travel times generated by the C++DEQSim.
	 * As {@link #compareToDEQSimEvents(String)} does not function for most comparisons of the JavaDEQSim and C++DEQSim model,
	 * we need to compare the time each car was on the road and take its average. This figure should with in a small interval
	 * for both simulations.
	 * Attention: Still when vehicles are stuck, this comparison can be off by larger number, because unstucking the vehicles is
	 * done in different ways by the two simulations
	 */
    protected void compareToDEQSimTravelTimes(final String deqsimEventsFile, final double tolerancePercentValue) {
        ArrayList<EventLog> deqSimLog = CppEventFileParser.parseFile(deqsimEventsFile);
        double deqSimTravelSum = EventLog.getSumTravelTime(deqSimLog);
        double javaSimTravelSum = EventLibrary.getSumTravelTime(allEvents);
        assertTrue((Math.abs(deqSimTravelSum - javaSimTravelSum) / deqSimTravelSum) < tolerancePercentValue);
    }

    private static class PersonEventCollector implements PersonEventHandler {

        private final Map<Id, LinkedList<PersonEvent>> eventsByPerson;

        private final List<PersonEvent> allEvents;

        PersonEventCollector(Map<Id, LinkedList<PersonEvent>> eventsByPerson, List<PersonEvent> allEvents) {
            this.eventsByPerson = eventsByPerson;
            this.allEvents = allEvents;
        }

        public void handleEvent(PersonEvent event) {
            if (!eventsByPerson.containsKey(event.getPersonId())) {
                eventsByPerson.put(event.getPersonId(), new LinkedList<PersonEvent>());
            }
            eventsByPerson.get(event.getPersonId()).add(event);
            allEvents.add(event);
        }

        public void reset(int iteration) {
        }
    }
}
