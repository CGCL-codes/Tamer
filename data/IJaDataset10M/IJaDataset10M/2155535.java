package org.matsim.network;

import org.apache.log4j.Logger;
import org.matsim.interfaces.basic.v01.population.BasicLeg;
import org.matsim.interfaces.core.v01.Link;
import org.matsim.interfaces.core.v01.Route;
import org.matsim.population.routes.AbstractRoute;
import org.matsim.population.routes.NodeCarRoute;
import org.matsim.population.routes.RouteFactory;
import org.matsim.testcases.MatsimTestCase;

/**
 * @author mrieser
 */
public class NetworkFactoryTest extends MatsimTestCase {

    private static final Logger log = Logger.getLogger(NetworkFactoryTest.class);

    public void testSetRouteFactory() {
        NetworkFactory factory = new NetworkFactory();
        Route carRoute = factory.createRoute(BasicLeg.Mode.car, null, null);
        assertTrue(carRoute instanceof NodeCarRoute);
        try {
            Route route = factory.createRoute(BasicLeg.Mode.pt, null, null);
            fail("expected IllegalArgumentException, but got route " + route.toString());
        } catch (IllegalArgumentException e) {
            log.info("Catched expected IllegalArgumentException: " + e.getMessage());
        }
        factory.setRouteFactory(BasicLeg.Mode.car, new CarRouteMockFactory());
        factory.setRouteFactory(BasicLeg.Mode.pt, new PtRouteMockFactory());
        carRoute = factory.createRoute(BasicLeg.Mode.car, null, null);
        assertTrue(carRoute instanceof CarRouteMock);
        Route ptRoute = factory.createRoute(BasicLeg.Mode.pt, null, null);
        assertTrue(ptRoute instanceof PtRouteMock);
        factory.setRouteFactory(BasicLeg.Mode.pt, null);
        try {
            Route route = factory.createRoute(BasicLeg.Mode.pt, null, null);
            fail("expected IllegalArgumentException, but got route " + route.toString());
        } catch (IllegalArgumentException e) {
            log.info("Catched expected IllegalArgumentException: " + e.getMessage());
        }
    }

    static class CarRouteMock extends AbstractRoute {

        CarRouteMock(Link startLink, Link endLink) {
            super(startLink, endLink);
        }
    }

    static class PtRouteMock extends AbstractRoute {

        PtRouteMock(Link startLink, Link endLink) {
            super(startLink, endLink);
        }
    }

    static class CarRouteMockFactory implements RouteFactory {

        public Route createRoute(Link startLink, Link endLink) {
            return new CarRouteMock(startLink, endLink);
        }
    }

    static class PtRouteMockFactory implements RouteFactory {

        public Route createRoute(Link startLink, Link endLink) {
            return new PtRouteMock(startLink, endLink);
        }
    }
}
