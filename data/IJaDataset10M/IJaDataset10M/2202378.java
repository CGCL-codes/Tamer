package mobility.fixed;

import simulator.*;
import mobility.MobileNode;
import event.*;

/**
 * Node which implements a fixed node without mobility
 * @author psommer
 *
 */
public class NodeFixed extends MobileNode {

    /**
	 * Creates a new fixed node without any mobility
	 * @param id unique node identifier
	 */
    public NodeFixed(int id) {
        super(id);
    }

    public void init() {
        boolean reject = true;
        while (reject) {
            double x1 = Simulator.rng.nextDouble();
            double x2 = Simulator.rng.nextDouble();
            double y1 = Simulator.rng.nextDouble();
            double y2 = Simulator.rng.nextDouble();
            double r = Math.sqrt((x2 - x1) * (x2 - x1) + (y2 - y1) * (y2 - y1)) / Math.sqrt(2);
            double u = Simulator.rng.nextDouble();
            if (u < r) {
                reject = false;
                u = Simulator.rng.nextDouble();
                double initX = u * x1 + (1 - u) * x2;
                double initY = u * y1 + (1 - u) * y2;
                u = Simulator.rng.nextDouble();
                Join join = new Join(this, 0.0, Simulator.size * initX, Simulator.size * initY);
                addEvent(join);
                addEvent(new Pause(this, 0.0, Simulator.duration, join.x, join.y));
            }
        }
    }

    public void prepare() {
    }

    public boolean next() {
        return super.next();
    }

    public void finish() {
        addEvent(new Leave(this, Simulator.duration, x, y));
        Simulator.removeNode(Simulator.duration, this);
        super.finish();
    }
}
