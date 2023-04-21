package SDG.agent2d;

import swarm.objectbase.SwarmImpl;
import swarm.objectbase.Swarm;
import swarm.activity.Activity;
import swarm.activity.Schedule;
import swarm.activity.ScheduleImpl;
import swarm.defobj.Zone;
import swarm.gui.Raster;
import java.util.Hashtable;
import java.util.Iterator;
import swarm.Selector;
import swarm.Globals;
import SDG.Organization;
import SDG.ObserverSwarm;

public class Alex2d extends SocialAgent2d {

    Hashtable people;

    public int xmean, ymean;

    public double xdeviation, ydeviation;

    class Location {

        int x;

        int y;

        Location(int x, int y) {
            this.x = x;
            this.y = y;
        }
    }

    public Alex2d(Zone aZone, Organization org, int x, int y) {
        super(aZone, org, x, y, 2, 4, 50, 10);
        name = "Alex";
        people = new Hashtable(10);
    }

    void noteOpinion(Agent2d neighbor) {
        Location location = (Location) people.get(neighbor);
        if (location != null) {
            location.x = neighbor.x;
            location.y = neighbor.y;
        } else {
            location = new Location(neighbor.x, neighbor.y);
            people.put(neighbor, location);
        }
    }

    int computeCentroidDirection() {
        Iterator iterator = people.values().iterator();
        int xsum = 0, ysum = 0, xsumsq = 0, ysumsq = 0;
        int count = 0;
        double xm, ym;
        while (iterator.hasNext()) {
            Location location = (Location) iterator.next();
            xsum += location.x;
            ysum += location.y;
            xsumsq += location.x * location.x;
            ysumsq += location.y * location.y;
            count++;
        }
        if (count == 0) xm = ym = 0; else {
            xm = (double) xsum / (double) count;
            ym = (double) ysum / (double) count;
        }
        xmean = (int) xm;
        ymean = (int) ym;
        if (count > 1) {
            xdeviation = Math.sqrt((double) count / (double) (count - 1) * ((double) xsumsq / (double) count - xm * xm));
            ydeviation = Math.sqrt((double) count / (double) (count - 1) * ((double) ysumsq / (double) count - ym * ym));
        } else {
            xdeviation = ydeviation = 0.0;
        }
        ;
        return (int) Math.toDegrees(Math.atan2((double) (ymean - y), (double) (xmean - x)));
    }

    public void stepSocialAgent(Agent2d neighbor) {
        super.stepSocialAgent(neighbor);
        if (neighbor == null) {
            color = ObserverSwarm.AlexTourColor;
            energy -= 40;
        } else {
            noteOpinion(neighbor);
            neighbor.frob(computeCentroidDirection());
            energy += 50;
            color = ObserverSwarm.AlexTalkColor;
        }
        randomWalk();
    }

    public Object drawSelfOn(Raster r) {
        r.ellipseX0$Y0$X1$Y1$Width$Color(xmean - (int) xdeviation, ymean - (int) ydeviation, xmean + (int) xdeviation, ymean + (int) ydeviation, 1, ObserverSwarm.AlexTargetColor);
        super.drawSelfOn(r);
        return this;
    }
}
