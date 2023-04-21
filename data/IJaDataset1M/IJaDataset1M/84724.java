package drcl.inet;

import java.util.LinkedList;
import drcl.ObjectCloneable;
import drcl.comp.*;

/**
The base class for modeling a physical link.
While one may extend this class to provide sophisticated link models,
this class implements a simple multi-endpoint link.
Data injected from one end point is propagated to all the other end points.
The propagation delay is the same for all the end points.
<p>The ports in the Link are numbered from 0, i.e., "0", "1", "2" etc.
 */
public class Link extends Component {

    Port[] endPoints = null;

    public Link() {
        super();
    }

    public Link(String id_) {
        super(id_);
    }

    public Link(double delay_) {
        setPropDelay(delay_);
    }

    protected void process(Object data_, Port inPort_) {
        if (endPoints == null) {
            setPortNotificationEnabled(true);
            Port[] pp = getAllPorts();
            LinkedList ll = new LinkedList();
            for (int i = 0; i < pp.length; i++) if (pp[i] != null && pp[i] != infoPort) ll.add(pp[i]);
            endPoints = new Port[ll.size()];
            ll.toArray(endPoints);
        }
        int len_ = endPoints.length;
        if (len_ == 0) return;
        if (!(data_ instanceof ObjectCloneable)) {
            for (int i = 0; i < len_; i++) if (endPoints[i] != inPort_) send(endPoints[i], data_, propDelay);
        } else if (endPoints[len_ - 1] == inPort_) {
            for (int i = 0; i < len_ - 2; i++) send(endPoints[i], ((ObjectCloneable) data_).clone(), propDelay);
            if (len_ >= 2) send(endPoints[len_ - 2], data_, propDelay);
        } else {
            for (int i = 0; i < len_ - 1; i++) if (endPoints[i] != inPort_) send(endPoints[i], ((ObjectCloneable) data_).clone(), propDelay);
            send(endPoints[len_ - 1], data_, propDelay);
        }
    }

    protected void portAdded(Port p) {
        endPoints = null;
    }

    public void duplicate(Object source_) {
        super.duplicate(source_);
        Link that_ = (Link) source_;
        propDelay = that_.propDelay;
    }

    public String info() {
        return "propagation delay = " + propDelay + "\n" + "End points=" + drcl.util.StringUtil.toString(endPoints) + "\n";
    }

    public void attach(Node n1_, Node n2_) {
        connect(n1_, false);
        connect(n2_, false);
    }

    public void attach(Port p1_, Port p2_) {
        Port mine1_ = findAvailable();
        mine1_.connectTo(p1_);
        p1_.connectTo(mine1_);
        Port mine2_ = findAvailable();
        mine2_.connectTo(p2_);
        p2_.connectTo(mine2_);
    }

    /** The propagation delay of the link.  */
    protected double propDelay = 0.0;

    /** Returns the propagation delay of the link.  */
    public double getPropDelay() {
        return propDelay;
    }

    /** Sets the propagation delay of the link.  */
    public void setPropDelay(double delay_) {
        propDelay = delay_;
    }
}
