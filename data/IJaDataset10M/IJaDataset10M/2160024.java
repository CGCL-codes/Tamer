package net.sf.myway.gps.garmin.protocol;

import java.util.ArrayList;
import java.util.List;
import net.sf.myway.gps.datatypes.Route;
import net.sf.myway.gps.datatypes.RouteHeader;
import net.sf.myway.gps.datatypes.RouteLink;
import net.sf.myway.gps.datatypes.Waypoint;

/**
 * @author Andreas Beckers
 * @version $Revision: 1.1 $
 */
public class RoutesResultBuilder implements ResultBuilder {

    /**
	 * @see net.sf.myway.gps.garmin.protocol.ResultBuilder#buildResult(java.lang.Object[])
	 */
    @Override
    public Object buildResult(final Object[] raw) {
        final List<Route> routes = new ArrayList<Route>();
        Route t = null;
        for (final Object element : raw) if (element instanceof RouteHeader) {
            t = new Route((RouteHeader) element);
            routes.add(t);
        } else if (element instanceof Waypoint) t.add((Waypoint) element); else if (element instanceof RouteLink) t.add((RouteLink) element);
        return routes;
    }
}
