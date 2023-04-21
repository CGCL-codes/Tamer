package playground.mrieser.core.mobsim.impl;

import org.junit.Assert;
import org.junit.Test;
import org.matsim.api.core.v01.Coord;
import org.matsim.api.core.v01.TransportMode;
import org.matsim.api.core.v01.population.Activity;
import org.matsim.api.core.v01.population.Leg;
import org.matsim.api.core.v01.population.Person;
import org.matsim.api.core.v01.population.Plan;
import org.matsim.core.basic.v01.IdImpl;
import org.matsim.core.population.ActivityImpl;
import org.matsim.core.population.LegImpl;
import org.matsim.core.population.PersonImpl;
import org.matsim.core.population.PlanImpl;
import org.matsim.core.population.routes.LinkNetworkRouteImpl;
import org.matsim.core.utils.geometry.CoordImpl;
import org.matsim.testcases.utils.EventsCollector;
import playground.mrieser.core.mobsim.api.DepartureHandler;
import playground.mrieser.core.mobsim.api.PlanAgent;
import playground.mrieser.core.mobsim.fakes.FakeSimEngine;

/**
 * @author mrieser
 */
public class LegHandlerTest {

    @Test
    public void testSetGetDepartureHandlers() {
        LegHandler legHandler = new LegHandler(new FakeSimEngine());
        CountingDepartureHandler carHandler = new CountingDepartureHandler();
        CountingDepartureHandler transitHandler = new CountingDepartureHandler();
        CountingDepartureHandler otherHandler = new CountingDepartureHandler();
        Assert.assertNull(legHandler.getDepartureHandler(TransportMode.car));
        Assert.assertNull(legHandler.getDepartureHandler(TransportMode.pt));
        Assert.assertNull(legHandler.getDepartureHandler(TransportMode.walk));
        Assert.assertNull(legHandler.getDepartureHandler(TransportMode.bike));
        legHandler.setDepartureHandler(TransportMode.car, carHandler);
        legHandler.setDepartureHandler(TransportMode.pt, transitHandler);
        legHandler.setDepartureHandler(TransportMode.walk, otherHandler);
        legHandler.setDepartureHandler(TransportMode.bike, otherHandler);
        Assert.assertEquals(carHandler, legHandler.getDepartureHandler(TransportMode.car));
        Assert.assertEquals(transitHandler, legHandler.getDepartureHandler(TransportMode.pt));
        Assert.assertEquals(otherHandler, legHandler.getDepartureHandler(TransportMode.walk));
        Assert.assertEquals(otherHandler, legHandler.getDepartureHandler(TransportMode.bike));
        Assert.assertNull(legHandler.getDepartureHandler("train"));
    }

    @Test
    public void testHandleStartHandleEnd() {
        Fixture f = new Fixture();
        FakeSimEngine engine = new FakeSimEngine();
        EventsCollector eventsCollector = new EventsCollector();
        engine.getEventsManager().addHandler(eventsCollector);
        LegHandler lh = new LegHandler(engine);
        CountingDepartureHandler depHandler = new CountingDepartureHandler();
        lh.setDepartureHandler(TransportMode.car, depHandler);
        Assert.assertEquals(0, eventsCollector.getEvents().size());
        Assert.assertEquals(0, engine.countHandleAgent);
        Assert.assertEquals(0, depHandler.count);
        f.agent1.useNextPlanElement();
        Assert.assertEquals(f.firstLeg, f.agent1.useNextPlanElement());
        lh.handleStart(f.agent1);
        Assert.assertEquals(1, eventsCollector.getEvents().size());
        Assert.assertEquals(0, engine.countHandleAgent);
        Assert.assertEquals(1, depHandler.count);
        lh.handleEnd(f.agent1);
        Assert.assertEquals(2, eventsCollector.getEvents().size());
        Assert.assertEquals(0, engine.countHandleAgent);
        Assert.assertEquals(1, depHandler.count);
        f.agent1.useNextPlanElement();
        Assert.assertEquals(f.secondLeg, f.agent1.useNextPlanElement());
        lh.handleStart(f.agent1);
        Assert.assertEquals(3, eventsCollector.getEvents().size());
        Assert.assertEquals(0, engine.countHandleAgent);
        Assert.assertEquals(2, depHandler.count);
        lh.handleEnd(f.agent1);
        Assert.assertEquals(4, eventsCollector.getEvents().size());
        Assert.assertEquals(0, engine.countHandleAgent);
        Assert.assertEquals(2, depHandler.count);
    }

    private static class Fixture {

        public final Person person1;

        public final Plan plan1;

        public final Leg firstLeg;

        public final Leg secondLeg;

        public final PlanAgent agent1;

        public Fixture() {
            this.person1 = new PersonImpl(new IdImpl("1"));
            this.plan1 = new PlanImpl();
            this.person1.addPlan(this.plan1);
            Coord c = new CoordImpl(0, 0);
            Activity act = new ActivityImpl("home", c);
            act.setEndTime(8.0 * 3600);
            this.plan1.addActivity(act);
            this.firstLeg = new LegImpl(TransportMode.car);
            this.firstLeg.setRoute(new LinkNetworkRouteImpl(new IdImpl("1"), new IdImpl("2")));
            this.plan1.addLeg(this.firstLeg);
            act = new ActivityImpl("work", c);
            act.setEndTime(17.0 * 3600);
            this.plan1.addActivity(act);
            this.secondLeg = new LegImpl(TransportMode.car);
            this.secondLeg.setRoute(new LinkNetworkRouteImpl(new IdImpl("2"), new IdImpl("1")));
            this.plan1.addLeg(this.secondLeg);
            act = new ActivityImpl("work", c);
            this.plan1.addActivity(act);
            this.agent1 = new DefaultPlanAgent(this.plan1, 1.0);
        }
    }

    private static class CountingDepartureHandler implements DepartureHandler {

        public int count = 0;

        @Override
        public void handleDeparture(final PlanAgent agent) {
            this.count++;
        }
    }
}
