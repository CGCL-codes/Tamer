package interfaces;

import World.CntAgent;
import World.Lane;
import World.Project;

public class Event_LaneCamera extends Event {

    private double t;

    private int granularity;

    private Lane curLane;

    public Event_LaneCamera() {
        this.granularity = 3;
    }

    public Event_LaneCamera(int granularity) {
        this.granularity = granularity;
    }

    public double getT() {
        return t;
    }

    public void setT(double t) {
        this.t = t;
    }

    public Lane getCurLane() {
        return curLane;
    }

    @Override
    public boolean FillData(Project project, CntAgent agent) {
        curLane = agent.getCurLane();
        t = find_min(agent.getPosition(), 0, 1, granularity);
        System.out.println(String.format("%5.3f", t));
        return true;
    }

    private double find_min(Vector3d pt, double begin_t, double end_t, int granularity) {
        double step_t = (end_t - begin_t) / 10;
        double min_t = begin_t;
        double min_value = curLane.getPointOnLane(begin_t).AddVector(pt.Invert()).Length();
        double tmp;
        for (double t = begin_t; t <= end_t; t += step_t) {
            tmp = curLane.getPointOnLane(t).AddVector(pt.Invert()).Length();
            if (tmp < min_value) {
                min_value = tmp;
                min_t = t;
            }
        }
        System.out.println(String.format("find_min %5.3f %5.3f %5.3f", begin_t, end_t, min_t));
        if (granularity > 0) {
            return find_min(pt, min_t - step_t, min_t + step_t, granularity - 1);
        } else {
            return min_t;
        }
    }
}
