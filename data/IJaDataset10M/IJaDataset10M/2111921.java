package GenService;

import view.simView.*;
import model.modeling.*;
import model.simulation.*;
import view.modeling.*;
import GenCol.*;
import util.*;
import java.util.*;
import java.lang.*;

/** 
 * @author Sungung Kim
 * @version 1.0
 * This class is available for a given time.
 * If inputs arrive before being available, then return "Not avail" message to subscribers and publishers
 * This class will receive inputs from the services for the purpose of publication
 * and return the matched service to the subscriber for subscription
 */
public class ServiceBroker extends ViewableAtomic {

    protected double previous = 0;

    protected double proc_time = 0;

    protected double start;

    protected double available_time;

    protected double clock;

    protected String pub = "publish";

    protected String sub = "subscribe";

    protected String subscriber;

    protected String endpt;

    protected String serviceName;

    protected ArrayList<ServiceInfoMessage> UDDI = new ArrayList<ServiceInfoMessage>();

    ;

    protected GenCol.Queue inQ;

    protected entity packet;

    protected ServiceInfoMessage returnMsg;

    public ServiceBroker() {
        this("Broker", 0.0, 0.0);
    }

    public ServiceBroker(String name, double avail_time, double startTime) {
        super(name);
        available_time = avail_time;
        start = startTime;
        addInport(pub);
        addInport(sub);
        addOutport("active");
        initialize();
    }

    public void initialize() {
        clock = 0;
        inQ = new GenCol.Queue();
        holdIn("passive", start);
        super.initialize();
    }

    public void deltext(double e, message x) {
        Continue(e);
        clock = clock + e;
        if (phaseIs("active")) {
            for (int i = 0; i < x.getLength(); i++) if (messageOnPort(x, pub, i)) {
                packet = x.getValOnPort(pub, i);
                publish(packet);
                holdIn("publishing", proc_time);
                inQ.add(packet);
            } else if (messageOnPort(x, sub, i)) {
                packet = x.getValOnPort(sub, i);
                holdIn("subscribing", proc_time);
                inQ.add(packet);
            }
            packet = (entity) inQ.first();
        } else if (phaseIs("passive")) {
            for (int i = 0; i < x.getLength(); i++) if (messageOnPort(x, pub, i)) {
                packet = x.getValOnPort(pub, i);
                holdIn("Not Avail", 0);
                inQ.add(packet);
            } else if (messageOnPort(x, sub, i)) {
                packet = x.getValOnPort(sub, i);
                holdIn("Not Avail", 0);
                inQ.add(packet);
            }
            packet = (entity) inQ.first();
        } else {
            for (int i = 0; i < x.getLength(); i++) if (messageOnPort(x, pub, i)) {
                packet = x.getValOnPort(pub, i);
                inQ.add(packet);
            } else if (messageOnPort(x, sub, i)) {
                packet = x.getValOnPort(sub, i);
                inQ.add(packet);
            }
        }
    }

    public void deltint() {
        clock = clock + sigma;
        if (inQ != null) {
            inQ.remove();
        }
        if (phaseIs("passive")) {
            holdIn("active", available_time);
        } else if (phaseIs("Not Avail")) {
            if (!inQ.isEmpty()) {
                packet = (entity) inQ.first();
                holdIn("Not Avail", 0);
            } else holdIn("passive", start - clock);
        } else if (!inQ.isEmpty()) {
            packet = (entity) inQ.first();
            if (packet instanceof ServiceInfoMessage) {
                publish(packet);
                holdIn("publishing", proc_time);
            } else holdIn("subscribing", proc_time);
        } else if (phaseIs("publishing") || phaseIs("subscribing")) {
            available_time = available_time - (clock - previous);
            holdIn("active", available_time);
            previous = clock;
        } else passivate();
    }

    public void deltcon(double e, message x) {
        deltext(e, x);
        deltint();
    }

    public message out() {
        message m = new message();
        if (phaseIs("subscribing")) {
            int index = subscribe(packet);
            if (index >= 0) {
                returnMsg = UDDI.get(index);
                if (returnMsg.getServiceType().equalsIgnoreCase("atomic")) returnMsg.setBindingInfo(new Pair(returnMsg.getServiceName(), endpt));
            } else returnMsg = new ServiceInfoMessage("No Found", null, null, null);
            returnMsg.setReceiver(subscriber);
            m.add(makeContent(subscriber, returnMsg));
        } else if (phaseIs("passive")) {
            m.add(makeContent("active", new entity("start")));
        } else if (phaseIs("active")) {
            m.add(makeContent("active", new entity("end")));
        } else if (phaseIs("Not Avail")) {
            if (packet instanceof ServiceInfoMessage) {
                ServiceInfoMessage tmp = (ServiceInfoMessage) packet;
                returnMsg = new ServiceInfoMessage("Not Avail", null, null, null);
                returnMsg.setReceiver(tmp.getServiceName());
                m.add(makeContent(tmp.getServiceName(), returnMsg));
            } else {
                ServiceLookupMessage tmp = (ServiceLookupMessage) packet;
                returnMsg = new ServiceInfoMessage("Not Avail", null, null, null);
                returnMsg.setReceiver(tmp.getSubscriber());
                m.add(makeContent(tmp.getSubscriber(), returnMsg));
            }
        }
        return m;
    }

    /**
	 * Store the service information into the UDDI
	 **/
    private void publish(entity msg) {
        ServiceInfoMessage Content = (ServiceInfoMessage) msg;
        UDDI.add(Content);
    }

    /**
	 * this method iterates UDDI until the matched service is found and return the index,
	 * otherwise return -1
	 **/
    private int subscribe(entity msg) {
        int index = 0;
        boolean isFound = false;
        System.out.println("********************** Start lookup");
        ServiceLookupMessage subscribeMsg = (ServiceLookupMessage) msg;
        subscriber = subscribeMsg.getSubscriber();
        endpt = subscribeMsg.getEndpoint();
        serviceName = subscribeMsg.getServiceName();
        while (index < UDDI.size()) {
            ServiceInfoMessage currService = UDDI.get(index);
            if (serviceName.equalsIgnoreCase(currService.getServiceName())) {
                for (int i = 0; i < currService.getEndpoints().size(); i++) {
                    if (endpt.equalsIgnoreCase(currService.getEndpoints().get(i).getKey().toString())) {
                        isFound = true;
                        break;
                    }
                }
            }
            if (isFound) break; else index++;
        }
        if (!isFound) index = -1;
        System.out.println("********************** End lookup");
        return index;
    }

    public void publishCompositeService(ServiceInfoMessage compositeService) {
        UDDI.add(compositeService);
        System.out.println("*********************************************************************                " + UDDI);
    }
}
