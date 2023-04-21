package teach.multiagent07.population;

import java.util.ArrayList;
import org.matsim.basic.v01.BasicLegImpl;
import org.matsim.basic.v01.BasicRouteImpl;
import org.matsim.utils.misc.Time;

public class Leg extends BasicLegImpl {

    private double duration = Time.UNDEFINED_TIME;

    public Leg(String mode) {
        this.mode = mode;
    }

    public Leg(Leg leg) {
        this.mode = leg.getMode();
        this.duration = leg.getDuration();
        this.num = leg.getNum();
        this.route = new BasicRouteImpl();
        ArrayList newRoute = new ArrayList();
        for (Object node : leg.getRoute().getRoute()) {
            newRoute.add(node);
        }
        this.route.setRoute(newRoute);
    }

    /**
	 * @return the duration
	 */
    public double getDuration() {
        return duration;
    }

    /**
	 * @param duration the duration to set
	 */
    public void setDuration(double duration) {
        this.duration = duration;
    }
}
