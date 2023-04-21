package GenService;

import view.simView.*;
import model.modeling.*;
import model.simulation.*;
import view.modeling.*;
import GenCol.*;
import util.*;

/** 
 * @author skim109
 * @version 1.0
 * The network receives an input and send output out after taking certain amount of time
 * currently 0.2 +- 0.1
 */
public class ServiceRouter extends ViewableAtomic {

    protected double trasmitionTime;

    protected double network_traffic;

    protected double mean = 0.2;

    protected double sig = 0.02;

    protected String outputPort;

    protected Queue inQ;

    protected entity packet, currPacket;

    protected rand r;

    public ServiceRouter() {
        this("Network", 0);
    }

    public ServiceRouter(String name, double traffic) {
        super(name);
        addInport("in");
        network_traffic = traffic;
    }

    public void initialize() {
        trasmitionTime = 0;
        inQ = new Queue();
        passivate();
        super.initialize();
    }

    public void deltext(double e, message x) {
        Continue(e);
        if (phaseIs("passive")) {
            for (int i = 0; i < x.getLength(); i++) if (messageOnPort(x, "in", i)) {
                packet = x.getValOnPort("in", i);
                r = new rand(System.currentTimeMillis());
                holdIn("transmitting", trasmitionTime);
                inQ.add(packet);
            }
            currPacket = (entity) inQ.first();
        } else {
            for (int i = 0; i < x.getLength(); i++) if (messageOnPort(x, "in", i)) {
                packet = x.getValOnPort("in", i);
                inQ.add(packet);
            }
        }
    }

    public void deltint() {
        inQ.remove();
        if (!inQ.isEmpty()) {
            currPacket = (entity) inQ.first();
            r = new rand(System.currentTimeMillis());
            holdIn("transmitting", trasmitionTime);
        } else passivate();
    }

    public void deltcon(double e, message x) {
        deltext(e, x);
        deltint();
    }

    public message out() {
        message m = new message();
        outputPort = ((ServiceCallMessage) currPacket).getReceiver();
        if (!this.getOutportNames().contains(outputPort)) outputPort = ((ServiceCallMessage) currPacket).getPublisher();
        if (phaseIs("transmitting")) m.add(makeContent(outputPort, currPacket));
        return m;
    }
}
