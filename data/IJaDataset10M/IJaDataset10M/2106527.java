package saf.simulation.event;

import saf.simulation.Instance;
import saf.visualization.Visualizer;

public class PunchHigh extends Event {

    private final Instance subject;

    public PunchHigh(Instance subject) {
        this.subject = subject;
    }

    public Instance getSubject() {
        return subject;
    }

    @Override
    public void visualize(Visualizer visualizer) {
        visualizer.visualize(this);
    }
}
