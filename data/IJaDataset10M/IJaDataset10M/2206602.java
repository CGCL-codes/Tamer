package edu.uncw.cs.primality.impl;

import java.rmi.RemoteException;
import org.globus.wsrf.Resource;
import org.globus.wsrf.ResourceProperties;
import org.globus.wsrf.ResourceProperty;
import org.globus.wsrf.ResourcePropertySet;
import org.globus.wsrf.impl.ResourcePropertyTopic;
import org.globus.wsrf.impl.SimpleResourcePropertySet;
import org.globus.wsrf.impl.SimpleResourceProperty;
import org.globus.wsrf.impl.SimpleTopicList;
import org.globus.wsrf.Topic;
import org.globus.wsrf.TopicList;
import org.globus.wsrf.TopicListAccessor;
import edu.uncw.cs.primality.stubs.Primality_instance.GiveWorkResponse;
import edu.uncw.cs.primality.stubs.Primality_instance.*;
import org.globus.wsrf.ResourceContext;
import org.globus.wsrf.Constants;
import javax.naming.Context;
import javax.naming.InitialContext;
import java.util.Vector;
import java.util.Hashtable;
import org.w3c.dom.Document;
import org.w3c.dom.*;
import javax.xml.parsers.*;
import org.jxpl.*;
import org.jxpl.bindings.*;
import org.apache.commons.logging.LogFactory;
import org.apache.commons.logging.Log;

/**
* Basic distributed service for primatlity proving
* @author Eric Harris
* @version 0.1
*/
public class PrimalityService extends Thread implements Resource, ResourceProperties, TopicListAccessor {

    static Log logger = LogFactory.getLog(PrimalityService.class.getName());

    private TopicList topicList;

    private Vector queue;

    private Hashtable checkedIn;

    private boolean isReady;

    private boolean isPaused;

    private static int ticketNumber = 0;

    private ResourceProperty newWorkRP;

    private ResourceProperty workRP;

    private ResourceProperty statusRP;

    private ResourceProperty resultRP;

    private ResourceProperty requestUpdateRP;

    private ResourcePropertySet propSet;

    private Processor environment;

    private long startTime = 0;

    public PrimalityService() {
        queue = new Vector();
        checkedIn = new Hashtable();
        environment = new Processor();
        propSet = new SimpleResourcePropertySet(PrimalityQNames.RESOURCE_PROPERTIES);
        try {
            newWorkRP = new SimpleResourceProperty(PrimalityQNames.RP_NEW_WORK);
            newWorkRP.add(new Integer(0));
            workRP = new SimpleResourceProperty(PrimalityQNames.RP_WORK);
            workRP.add(new Vector());
            statusRP = new SimpleResourceProperty(PrimalityQNames.RP_STATUS);
            statusRP.add(new Integer(0));
            resultRP = new SimpleResourceProperty(PrimalityQNames.RP_RESULTS);
            requestUpdateRP = new SimpleResourceProperty(PrimalityQNames.RP_REQUEST_UPDATE);
            requestUpdateRP.add(new Integer(0));
            logger.info("Starting Listeneing Thread");
            start();
        } catch (Exception e) {
        }
        this.topicList = new SimpleTopicList(this);
        newWorkRP = new ResourcePropertyTopic(newWorkRP);
        statusRP = new ResourcePropertyTopic(statusRP);
        resultRP = new ResourcePropertyTopic(resultRP);
        requestUpdateRP = new ResourcePropertyTopic(requestUpdateRP);
        ((ResourcePropertyTopic) newWorkRP).setSendOldValue(true);
        ((ResourcePropertyTopic) statusRP).setSendOldValue(true);
        ((ResourcePropertyTopic) resultRP).setSendOldValue(false);
        ((ResourcePropertyTopic) requestUpdateRP).setSendOldValue(true);
        this.topicList.addTopic((Topic) newWorkRP);
        this.topicList.addTopic((Topic) statusRP);
        this.topicList.addTopic((Topic) resultRP);
        this.topicList.addTopic((Topic) requestUpdateRP);
        this.propSet.add(newWorkRP);
        this.propSet.add(statusRP);
        this.propSet.add(resultRP);
        this.propSet.add(requestUpdateRP);
        isReady = true;
    }

    public TopicList getTopicList() {
        return this.topicList;
    }

    public int giveWork(Document doc) throws Exception {
        logger.info("Receiving Work");
        JxplList list = new JxplList(doc.getDocumentElement());
        logger.debug("**********Got Document Work...");
        logger.debug("**********" + System.getProperty("java.library.path"));
        if (!isReady) throw new Exception("Service not yet ready...still working on pending job");
        logger.debug("Executing Distribution Script");
        setInQueue(list);
        logger.debug("Stored in queue");
        isReady = false;
        printQueue();
        newWorkRP.set(0, new Integer(ticketNumber));
        statusRP.set(0, new Integer(1));
        startTime = System.currentTimeMillis();
        logger.info("Work Loaded and Ready To Be Farmed");
        return ticketNumber;
    }

    public synchronized Document getWork(GetWork params) throws Exception {
        Document doc = getFromQueue();
        return doc;
    }

    private boolean checkComplete() {
        boolean done = true;
        for (int i = 0; i < queue.size(); i++) {
            if (((PrimalitySegment) queue.elementAt(i)).getStatus() != PrimalityConstants.FINISHED) {
                done = false;
                break;
            }
        }
        if (done) {
            setResults();
        }
        return done;
    }

    public synchronized void setResults() {
        try {
            JxplList result = new JxplList();
            result.addElement(new JxplString(Integer.toString(ticketNumber)));
            result.addElement(new JxplString(Long.toString((System.currentTimeMillis() - startTime) / 1000) + " Seconds"));
            result.addElement(Reduce.bool(queue));
            logger.info("Result was " + result);
            Document resDom = XmlUtil.getDocument(result);
            resultRP.add(resDom);
            cleanUp();
            ticketNumber++;
        } catch (Exception e) {
            logger.error(e.getMessage());
        }
    }

    public void cleanUp() {
        queue = new Vector();
        statusRP.set(0, new Integer(0));
        isReady = true;
    }

    public synchronized boolean done(Document doc) {
        try {
            JxplList lis = new JxplList(doc.getDocumentElement());
            NodeList segId = doc.getElementsByTagNameNS("*", "string");
            System.out.println(segId.getLength());
            int id = Integer.parseInt(new JxplString((Element) segId.item(segId.getLength() - 1)).getValue());
            PrimalitySegment seg = (PrimalitySegment) queue.elementAt(id);
            seg.setResult(lis.first());
            seg.setStatus(PrimalityConstants.FINISHED);
        } catch (Exception e) {
            logger.error(e);
        }
        return checkComplete();
    }

    public synchronized CheckInResponse checkIn(Integer segment) {
        logger.info("Check In for " + segment);
        checkedIn.put(segment, new Boolean(true));
        return new CheckInResponse();
    }

    public CancelResponse cancel(Cancel params) {
        cleanUp();
        return new CancelResponse();
    }

    public String pause(Pause params) {
        isPaused = true;
        return "Job is paused";
    }

    public String resume(Resume params) {
        if (!isPaused) return "Job is already active"; else {
            isPaused = false;
            newWorkRP.set(0, new Integer(ticketNumber));
        }
        return "Job is being resumed";
    }

    public String getStatus(GetStatus params) {
        String s = "Job Status\r\n----------";
        if (isPaused) {
            s += "\r\nIs Paused?\t" + isPaused;
        }
        if (!isReady) {
            s += "\r\nRunning " + ticketNumber + ": Current RunTime " + ((System.currentTimeMillis() - startTime) / 1000) + " seconds";
        } else {
            s += "\r\nIdle awaiting job";
        }
        return s;
    }

    public Document getResult(int ticket) {
        return (Document) resultRP.get(ticket);
    }

    public ResourcePropertySet getResourcePropertySet() {
        return propSet;
    }

    private void setInQueue(JxplList list) {
        while (list.size() > 0) {
            queue.addElement(new PrimalitySegment(list.first()));
            list = list.rest();
        }
    }

    private synchronized Document getFromQueue() throws Exception {
        for (int i = 0; i < queue.size(); i++) {
            PrimalitySegment seg = (PrimalitySegment) queue.elementAt(i);
            if (seg.getStatus() == PrimalityConstants.PENDING) {
                seg.incAssigned();
                seg.setStatus(PrimalityConstants.ACTIVE);
                return wrapWithIdInfo(seg.getWork(), i);
            }
        }
        return null;
    }

    private static Document wrapWithIdInfo(JxplElement elem, int id) throws Exception {
        return wrapWithIdInfo(XmlUtil.getDocument(elem), id);
    }

    private static Document wrapWithIdInfo(Document doc, int id) throws Exception {
        JxplList rootList = new JxplList();
        rootList.addElement(new JxplList(doc.getDocumentElement()));
        rootList.addElement(new JxplString(Integer.toString(id)));
        return XmlUtil.getDocument(rootList);
    }

    private static boolean isDistribute(JxplList list) throws RemoteException {
        if ((list.first() instanceof JxplPrimitive)) {
            JxplPrimitive distribute = (JxplPrimitive) list.first();
            if (distribute.getName().equalsIgnoreCase("Distribute")) return false; else return true;
        } else return false;
    }

    private void printQueue() {
        String s = "*****Queue****\n Size: " + queue.size() + "\r\n";
        for (int i = 0; i < queue.size(); i++) {
            s += ((PrimalitySegment) queue.elementAt(i)).getWork() + "\n----------\r\n";
        }
        logger.info(s);
        logger.debug("Blocking from new jobs?: " + !isReady);
    }

    private synchronized void checkForStrikes() {
        logger.info("Checking For Strikes");
        for (int i = 0; i < queue.size(); i++) {
            PrimalitySegment seg = (PrimalitySegment) queue.get(i);
            if (seg.getStatus() != PrimalityConstants.FINISHED) logger.debug(i + ": Hash Key Is " + checkedIn.get(new Integer(i)));
            if (seg.getStatus() == PrimalityConstants.ACTIVE && checkedIn.get(new Integer(i)) == null) {
                seg.setStrikes(seg.getStrikes() + 1);
                logger.debug("Strike " + seg.getStrikes() + " committed");
                if (seg.getStrikes() >= farmConf.getStrikes()) {
                    seg.setStatus(PrimalityConstants.PENDING);
                    seg.setStrikes(0);
                    logger.info("Stuck Out and Sending Notification to new clients");
                    newWorkRP.set(0, new Integer(ticketNumber));
                }
            } else {
                seg.setStrikes(0);
            }
        }
        checkedIn = new Hashtable();
    }

    private static FarmOptions farmConf;

    {
        try {
            ResourceContext ctx = ResourceContext.getResourceContext();
            Context initialContext = new InitialContext();
            String lookupString = Constants.JNDI_SERVICES_BASE_NAME + ctx.getService() + "/farmConfiguration";
            farmConf = (FarmOptions) initialContext.lookup(lookupString);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /** This method is responsible for handling the polling of workers by user specified time */
    public void run() {
        int poll = 0;
        while (true) {
            try {
                Thread.sleep(farmConf.getCheckInterval());
                if (!isReady) {
                    requestUpdateRP.set(0, new Integer(++poll));
                    Thread.sleep(farmConf.getAwaitResponse());
                    checkForStrikes();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}
