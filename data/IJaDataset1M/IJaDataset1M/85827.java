package org.roober.hedgehog;

import java.util.*;
import javax.jmdns.*;
import org.apache.xmlrpc.WebServer;
import com.thoughtworks.xstream.XStream;

/**
 * The JobServer class is Hedgehog's main server. It 
 * takes jobs using the {@link #postJob(Job,JobListener)} method and farms
 * them out to clients. Once a job result is returned to
 * the server by a client, the server notifies the {@link JobListener}
 * passed in postJob.
 * <p>
 * When created, the server announces itself via Rendezvous 
 * using JmDNS, starts an XML-RPC server on port 8080, 
 * and starts a {@link ClassServer} on port 1234. The client uses
 * XML-RPC to find out what jobs are available, take a job,
 * and return the results.
 * <p>
 * The server wraps a Job in a {@link JobWrapper}, serializes it to
 * XML using XStream {@link "http://xstream.codehaus.org/"}, and then
 * sends it as a string to to the client as a response to 
 * an XML-RPC call to {@link #getAJobXML}.
 * <p>
 * TODO: add zip compression to xml strings sent over XML-RPC
 * <p>
 * TODO: allow user to change ports
 * <p>
 * TODO: make sure that submitted Jobs are not modified once they are posted.
 * <p>
 * TODO: add an intelligent scheduling mechanism that sends batches of jobs to
 * the client to save time
 * <p>
 * @author Reuben Grinberg
 */
public class JobServer {

    private static final int FILE_SERVER_PORT = 1234;

    private static final int XML_RPC_PORT = 8080;

    /** for serializing Jobs to and from xml */
    private static XStream xstream = new XStream();

    /** Jobs that haven't been started */
    private static LinkedList availableJobs = new LinkedList();

    /** Jobs that have been farmed out but not yet finished. Hashtable
	 * where keys are the IDs of the Jobs and the values are the JobResults */
    private static Hashtable delegatedJobs = new Hashtable();

    private ClassServer classServer;

    /** 
	 * Constructs a JobServer with no jobs.
	 *  Starts a ClassServer on port 1234,
	 * an XML-RPC server on port 8080, and announces the server
	 * using rendezvous. */
    public JobServer() {
        startClassServer();
        startRPCServer();
        announceServer();
    }

    /**
	 * Announces this server using rendezvous as :
	 * "name_of_this_host._hedgehog._tcp.local." The port announced is
	 * the XML-RPC port.
	 */
    private void announceServer() {
        try {
            JmDNS jmdns = new JmDNS();
            jmdns.registerService(new ServiceInfo("_hedgehog._tcp.local.", java.net.InetAddress.getLocalHost().getHostName() + "._hedgehog._tcp.local.", XML_RPC_PORT, 0, 0, "path=/RPC2"));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /** 
	 * Start a @{link ClassServer} on FILE_SERVER_PORT
	 */
    private void startClassServer() {
        classServer = new ClassServer(FILE_SERVER_PORT);
    }

    /**
	 * Start an XML-RPC server on XML_RPC_PORT. Make this object
	 * the handler.
	 * <p>
	 * TODO: Make an inner class the handler so that we can
	 * get rid of the ugly XML postfix on the RPC functions.
	 * Then we can make getAJobXML into getAJob and it wouldn't
	 * interfere with ClassServer's getAJob.
	 */
    private void startRPCServer() {
        try {
            WebServer xmlrpcServer = new org.apache.xmlrpc.WebServer(XML_RPC_PORT);
            xmlrpcServer.addHandler("hedgehog", this);
            xmlrpcServer.start();
        } catch (Exception exception) {
            System.err.println("JavaServer: " + exception.toString());
        }
    }

    /**
	 * @return a Hashtable with two elements: "port"
	 * is the key to the port number of the ClassServer
	 * and "paths" is the list of paths from the ClassServer.
	 */
    public Hashtable getClassServerInfo() {
        System.err.println("Returning server info to client");
        Hashtable h = new Hashtable();
        h.put("port", new Integer(FILE_SERVER_PORT));
        h.put("paths", classServer.getPaths());
        return h;
    }

    public String getClassServerPort() {
        return FILE_SERVER_PORT + "";
    }

    /**
	 * Adds a Job to be computed to the queue and a JobListener
	 * that should be notified when the Job is finished.
	 * @param j Job to be computed
	 * @param jl JobListener that should be notified once the job is completed.
	 */
    public void postJob(Job j, JobListener jl) {
        JobWrapper w = new JobWrapper(j);
        JobStatus status = new JobStatus(w, jl);
        availableJobs.add(status);
        System.err.println("Job posted: " + j.description() + " " + j);
    }

    /**
	 * Returns an XML serialized version of the JobWrapper
	 * @param j The JobWrapper to serialize
	 * @return XML string representing the JobWrapper
	 */
    public String getJobXML(JobWrapper j) {
        return xstream.toXML(j);
    }

    /**
	 * If there are jobs in {@link JobServer#availableJobs}, popJob() removes the first job,
	 * adds it to {@link JobServer#delegatedJobs}, and returns it.
	 * @return a JobStatus representing the job to be computed.
	 */
    private JobStatus popJob() {
        JobStatus job;
        synchronized (availableJobs) {
            if (availableJobs.size() > 0) job = (JobStatus) availableJobs.removeFirst(); else return null;
        }
        job.delegate();
        synchronized (delegatedJobs) {
            delegatedJobs.put(new Integer(job.getID()), job);
        }
        return job;
    }

    /**
	 * Returns an available job as a JobWrapper serialized
	 * to an XML string. If none are available, returns null
	 * serialized to XML. This function is meant to be called through
	 * XML-RPC.
	 * @return an XML string that is the serialized JobWrapper
	 */
    public String getAJobXML() {
        JobStatus status = popJob();
        if (status != null) return getJobXML(status.getJobWrapper()); else return getJobXML((JobWrapper) null);
    }

    /**
	 * Client calls finishedJob when it has finished a Job.
	 * 
	 * @param xmlres XML serialized version of JobResults (that
	 * contains the result of the computed job.
	 * 
	 * @return String acknowledging reciept of the results.
	 */
    public String finishedJob(String xmlres) {
        JobResults results = (JobResults) xstream.fromXML(xmlres);
        Object res = results.getResults();
        JobStatus status = popDelegatedJob(results.getID());
        status.getListener().jobFinished(status.getJobWrapper().getJob(), res);
        return "Thanks!";
    }

    /**
	 * Returns the JobStatus of the Job with the given id
	 * from {@link JobServer#delegatedJobs} and removes it from delegatedJobs.
	 * @param id The unique id of the job to be popped
	 * @return The JobStatus of the Job associated with id
	 */
    private JobStatus popDelegatedJob(int id) {
        Integer i = new Integer(id);
        JobStatus status = (JobStatus) delegatedJobs.get(i);
        delegatedJobs.put(i, "empty");
        return status;
    }

    /**
	 * Shut down all local or connected clients and then quit the server.
	 * We don't know how many clients exist, so this function
	 * waits for JobClient.LONGEST_WAIT (the longest amount of
	 * time a client waits before polling the server) before
	 * giving up. So this function is guranteed to last
	 * JobClient.LONGEST_WAIT milliseconds.
	 * <p>
	 * TODO: Currently only kills first 100 clients. Fix this!
	 */
    public void killClients() {
        for (int i = 0; i < 100; i++) postJob(new KillClientJob(), new KillListener());
        try {
            Thread.sleep(JobClient.LONGEST_WAIT);
        } catch (InterruptedException e) {
        }
        System.exit(0);
    }

    /**
	 * Main function for testing purposes. However, if the first argument is
	 * "-killClients" then {@link #killClients} is called.
	 * @param args
	 */
    public static void main(String[] args) {
        JobServer server = new JobServer();
        if (args.length > 0 && args[0].equals("-killClients")) server.killClients();
        String classPath = System.getProperty("java.class.path", ".");
        System.err.println("Classpath:\t" + classPath);
        Vector v = new Vector();
        for (int i = 0; i < 10; i++) v.add(new Integer(i));
        Job j = new JobSum(v);
        server.postJob(j, (JobListener) j);
    }

    private static class JobSum implements Job, JobListener {

        private Vector numbers = null;

        JobSum(Vector numbers) {
            this.numbers = numbers;
        }

        public void jobFinished(Job j, Object results) {
            System.out.println("Sum is: " + results);
        }

        public Object run() {
            int sum = 0;
            if (numbers != null) {
                Iterator it = ((Vector) numbers).iterator();
                while (it.hasNext()) sum += ((Integer) it.next()).intValue();
            } else System.err.println("args is null!");
            return new Integer(sum);
        }

        public String description() {
            return "adds up numbers";
        }
    }

    /**
	 * For testing purposes.
	 * @return JobWrapper of a {@link PrintJob}
	 */
    private JobWrapper getPrintJobWrapper() {
        Vector v = new Vector();
        for (int i = 0; i < 10; i++) v.add(new Integer(i));
        return new JobWrapper(new PrintJob(v));
    }

    /**
	 * Is notified when a client is successfully killed.
	 * Adds a new KillJob to the queue. This should enable
	 * killing all the clients that are running.
	 * <p>
	 * TO DO: this listener is never called - why?
	 */
    class KillListener implements JobListener {

        public void jobFinished(Job j, Object results) {
            System.err.println("trying to post new job");
            postJob(new KillClientJob(), new KillListener());
        }
    }
}
