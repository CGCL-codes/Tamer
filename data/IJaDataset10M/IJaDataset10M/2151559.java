package CADI.Server.Core;

import CADI.Common.Network.TrafficShaping;
import CADI.Common.Network.JPIP.JPIPRequestDecoder;
import java.io.PrintStream;
import java.net.Socket;
import CADI.Common.Log.CADILog;
import CADI.Common.Network.HTTP.HTTPRequest;
import CADI.Common.Network.HTTP.StatusCodes;
import CADI.Common.Network.JPIP.JPIPRequestFields;
import CADI.Server.LogicalTarget.ServerLogicalTargetList;
import CADI.Server.Network.*;
import CADI.Server.Request.*;
import CADI.Server.Session.ServerClientSessions;
import GiciException.ErrorException;
import GiciException.WarningException;

/**
 * This class performs the control of the listener/s and workers threads.
 * <p>
 * The listener/s are the thread/s where the client request are received, and
 * workers are a thread pool that process the requests and send the response to
 * the client.
 * <p>
 * Usage example:<br>
 * &nbsp; construct<br>
 * &nbsp; setParameters<br>
 * &nbsp; run<br>
 * 
 * @author Group on Interactive Coding of Images (GICI)
 * @version 1.1.4 2011/03/03
 */
public class Scheduler extends Thread {

    /**
   * Ports where the server is listening to the client requests.
   */
    private int[] ports;

    /**
   * Indicates the number of threads of the <code>Worker</code> that will
   * be running.
   */
    private int numOfWorkers = 0;

    /**
   * It is the file name where the server logs are stored.
   */
    private String logFile = null;

    /**
   * Indicates whether the log information is stored in XML format or simple
   * text format.
   */
    private boolean XMLLogFormat;

    /**
   * Indicates whether the log is enabled or disabled.
   */
    private boolean logEnabled;

    /**
   * It is the timeout (in milliseconds) that will be used to wait for a new
   * client request when the HTTP keep-alive mode is set.
   */
    private int keepAliveTimeout = 0;

    /**
   * Is the maximum rate (bytes per second) which will be used to delivery
   * data from the server to the client. 0 value means unlimited.
   * <p>
   * Only positive values are allowed.
   */
    private long maxTxRate = 0L;

    /**
   *
   */
    private int trafficShaping = TrafficShaping.NONE;

    /**
   * Definition in {@link CADI.Server.Network.JPIPMessageEncoder#independentForm}.
   */
    private boolean independentMessageHeaders = true;

    /**
   * This attribute indicates whether a predictive model is being used to
   * deliver the image data.
   */
    private boolean predictiveModel = false;

    /**
   * Is the path directory where the logical targets are stored.
   */
    private String targetsPath = null;

    /**
   * Is the path directory that will be used to save the temporary files
   * used to cache client data.
   */
    private String cachePath = null;

    /**
   * Further information, see {@link CADI.Server.ServerParser#serverArguments}.
   */
    private int deliveringMode = -1;

    /**
   * Indicates a subtype of the {@link  #deliveringMode} attribute. Thus, its
   * value will depend on the value taken by {@link  #deliveringMode}.
   * <p>
   * Further information, see {@link CADI.Server.ServerParser#serverArguments}.
   *
   * {@see #rateDistortionMethod} value.
   */
    private int deliveringSubtype = -1;

    /**
   * This thread pool will listen to in a port to receive client requests.
   */
    private RequestListener[] requestListenersPool = null;

    /**
   * This thread pool will process the client requests and will send the
   * server responses to the client.
   */
    private Worker[] workersPool = null;

    /**
   * It is a queue where the client request are stored. This queue is shared
   * memory between the <data>daemon</data> which stored the client requests
   * and the <data>RequestDispatcher</data> which gets them to process.
   */
    private RequestQueue requestQueue = null;

    /**
   * It is a list that contais information about the logical targets
   * (typically an image) that are being served.
   */
    private ServerLogicalTargetList logicalTargetList = null;

    /**
   * It is a list that contains information about the clients that are being
   * served.
   */
    private ServerClientSessions clientSessions = null;

    /**
   * Is an array of queues to storage the tasks for each {@link
   * CADI.Server.Core.Worker} thread. Therefore, each queue of the array
   * belongs to a <code>Workder</code>.
   */
    private TasksQueue[] tasksQueue = null;

    /**
   * It is an object that will be used to log the server process
   */
    private CADILog log = null;

    /**
   * Indicates when the scheduler has to finish
   */
    private boolean finish = false;

    /**
   * Constructor.
   *
   * @param ports they are the ports where the server will listen to the
   * 	client request.
   * @param numOfWorkers they are the number of workers that will be launched to
   * 	process the client request.
   * @param logEnabled definition in {@link #logEnabled}
   * @param logLevel definition in {@link CADI.Common.Log.CADILog#logLevel}
   * @param logFile definition in {@link #logFile}.
   * @param XMLLogFormat definition in {@link #XMLLogFormat}.
   *
   * @throws ErrorException when the server can not be launched because the
   * 	server socket can not be opened it will be thrown.
   */
    public Scheduler(int[] ports, int numOfWorkers, boolean logEnabled, int logLevel, String logFile, boolean XMLLogFormat) {
        this.ports = ports;
        this.numOfWorkers = numOfWorkers;
        this.logFile = logFile;
        setName("Scheduler");
        requestQueue = new RequestQueue();
        logicalTargetList = new ServerLogicalTargetList();
        clientSessions = new ServerClientSessions();
        workersPool = new Worker[numOfWorkers];
        requestListenersPool = new RequestListener[ports.length];
        tasksQueue = new TasksQueue[numOfWorkers];
        log = new CADILog(logFile, XMLLogFormat);
        log.setEnabled(logEnabled);
        log.setLogLevel(logLevel);
    }

    /**
   * Sets the {@link #targetsPath} attribute.
   *
   * @param targetsPath definition in {@link #targetsPath}.
   */
    public void setTargetsPath(String targetsPath) {
        this.targetsPath = targetsPath;
    }

    /**
   * Sets the {@link #cachePath} attribute.
   *
   * @param cachePath definition in {@link #cachePath}.
   */
    public void setCachePath(String cachePath) {
        this.cachePath = cachePath;
    }

    /**
   * Sets the mode used to deliver precinct data to the client.
   *
   * @param deliveringMode definition in {@link #deliveringMode}.
   * @param deliveringSubtype definition in {@link #deliveringSubtype}.
   */
    public void setDeliveringMode(int deliveringMode, int deliveringSubtype) {
        this.deliveringMode = deliveringMode;
        this.deliveringSubtype = deliveringSubtype;
    }

    /**
   * Sets the {@link #keepAliveTimeout} attribute.
   *
   * @param keepAliveTimeout definition in {@link #keepAliveTimeout}.
   */
    public void setKeepAliveTimeout(int keepAliveTimeout) {
        this.keepAliveTimeout = keepAliveTimeout;
    }

    /**
   * Sets the {@link #maxTxRate} attribute.
   *
   * @param maxTxRate definition in {@link #maxTxRate} attribute.
   */
    public void setMaxTxRate(long maxTxRate) {
        this.maxTxRate = maxTxRate;
    }

    /**
   * Sets the {@link #trafficShaping} attribute.
   *
   * @param maxTxRate definition in {@link #trafficShaping} attribute.
   */
    public void setTrafficShaping(int trafficShaping) {
        this.trafficShaping = trafficShaping;
    }

    /**
   * Sets the {@link #independentMesssageHeaders} attribute.
   *
   * @param independentMesssageHeaders definition in {@link #independentMesssageHeaders} attribute.
   */
    public void setIndependentMessageHeaders(boolean independentMessageHeaders) {
        this.independentMessageHeaders = independentMessageHeaders;
    }

    /**
   * Sets the {@link #predictiveModel} attribute.
   *
   * @param predictiveModel definition in {@link #predictiveModel}.
   */
    public void setPredictiveModel(boolean predictiveModel) {
        this.predictiveModel = predictiveModel;
    }

    /**
   * This method manages the <code>RequestListener</code> pool and the <code>
   * Worker</code> pool. It gets the client request (received by the <code>
   * RequestListener</code>) and assigns the tasks to the <code> Worker
   * </code>.
   * <p>
   * The method will be running until the <code>finish</code> attibute is set
   * to <code>false</code>.
   */
    @Override
    public void run() {
        if (log.isLog(CADILog.LEVEL_INFO)) {
            log.logInfo("CADI Server starting ...");
        }
        try {
            launchListeners();
            launchWorkers();
        } catch (ErrorException e2) {
            return;
        }
        int thread = 0;
        boolean error = false;
        JPIPRequestDecoder jpipRequestDecoder = new JPIPRequestDecoder();
        while (!finish) {
            error = false;
            RequestQueueNode nodeInfo = null;
            try {
                nodeInfo = requestQueue.get();
            } catch (InterruptedException e1) {
                nodeInfo = null;
            }
            if (nodeInfo != null) {
                Socket socket = nodeInfo.getSocket();
                HTTPRequest httpRequest = nodeInfo.getHTTPRequest();
                if (log.isLog(CADILog.LEVEL_INFO)) {
                    log.logInfo(getName() + "(Received request):\n" + nodeInfo.toString());
                }
                thread = -1;
                int works = Integer.MAX_VALUE;
                boolean found = false;
                for (int i = 0; i < workersPool.length && !found; i++) {
                    if ((tasksQueue[i].size() == 0)) {
                        thread = i;
                        found = true;
                    } else if (works > tasksQueue[i].size()) {
                        thread = i;
                        works = tasksQueue[i].size();
                    }
                }
                if (log.isLog(CADILog.LEVEL_INFO)) {
                    log.logInfo(getName() + ": task assigned to the Worker-" + thread);
                }
                if (!error) {
                    try {
                        jpipRequestDecoder.decoder(httpRequest.getRequestURI());
                    } catch (WarningException we) {
                        error = true;
                        tasksQueue[thread].add(new TasksQueueNode(socket, we.getErrorCode(), we.getMessage()));
                    }
                    if (!error) {
                        JPIPRequestFields jpipRequestFields = jpipRequestDecoder.getJPIPRequestFields();
                        if (jpipRequestFields.channelField.cid != null) {
                            if (log.isLog(CADILog.LEVEL_DEBUG)) {
                                log.logDebug(getName() + ": Request belongs to a sessions (cid=" + jpipRequestFields.channelField.cid + ")");
                            }
                            if (!clientSessions.contains(jpipRequestFields.channelField.cid)) {
                                error = true;
                                tasksQueue[thread].add(new TasksQueueNode(socket, StatusCodes.BAD_REQUEST, "The \"channel idenfier\" is wrong or from an old session"));
                            }
                        }
                        if (!error) {
                            if (log.isLog(CADILog.LEVEL_INFO)) {
                                log.logInfo(getName() + ": request data has been passed to the Worker-" + thread);
                            }
                            tasksQueue[thread].add(new TasksQueueNode(socket, httpRequest, jpipRequestFields));
                        }
                    }
                }
            }
        }
        if (log.isLog(CADILog.LEVEL_INFO)) {
            log.logInfo(getName() + ": shutting down...");
        }
        for (int i = 0; i < requestListenersPool.length; i++) {
            requestListenersPool[i].finish();
        }
        for (int i = 0; i < numOfWorkers; i++) {
            workersPool[i].finish();
        }
        try {
            for (int i = 0; i < requestListenersPool.length; i++) {
                requestListenersPool[i].join();
            }
            for (int i = 0; i < numOfWorkers; i++) {
                workersPool[i].join();
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        requestQueue = null;
        logicalTargetList = null;
        clientSessions = null;
        for (int i = 0; i < numOfWorkers; i++) {
            workersPool[i] = null;
        }
        if (log.isLog(CADILog.LEVEL_INFO)) {
            log.logInfo("CADI Server stopped!");
        }
    }

    /**
   * This method is used to indicate that the thread has to finish.
   */
    public synchronized void finish() {
        this.finish = true;
    }

    @Override
    public String toString() {
        String str = "";
        str = getClass().getName() + " [";
        str += "ports=";
        for (int i = 0; i < ports.length - 1; i++) {
            str += ports[i] + ",";
        }
        str += ports[ports.length - 1];
        str += ", numOfWorkers=" + numOfWorkers;
        str += ", logFile=" + logFile;
        str += ", XMLLogFormat=" + XMLLogFormat;
        str += ", logEnabled=" + logEnabled;
        str += ", keepAliveTimeout=" + keepAliveTimeout;
        str += ", maxTxRate=" + maxTxRate;
        str += ", targetsPath=" + targetsPath;
        str += ", cachePath=" + cachePath;
        str += ", deliveringMode=" + deliveringMode;
        str += ", deliveringSubtype=" + deliveringSubtype;
        str += "]";
        return str;
    }

    /**
   * Prints this Scheduler's fields to the specified output stream.
   * This method is useful for debugging.
   *
   * @param out an output stream.
   */
    public void list(PrintStream out) {
        out.println("-- Scheduler --");
        out.print("ports: ");
        for (int i = 0; i < ports.length - 1; i++) {
            out.print(ports[i] + ", ");
        }
        out.println(ports[ports.length - 1]);
        out.println("numOfWorkers: " + numOfWorkers);
        out.println("logFile: " + logFile);
        out.println("XMLLogFormat: " + XMLLogFormat);
        out.println("logEnabled: " + logEnabled);
        out.println("keepAliveTimeout: " + keepAliveTimeout);
        out.println("maxTxRate: " + maxTxRate);
        out.println("targetsPath: " + targetsPath);
        out.println("cachePath: " + cachePath);
        out.println("deliveringMode: " + deliveringMode);
        out.println("deliveringSubtype: " + deliveringSubtype);
        out.flush();
    }

    /**
   * @throws ErrorException
   *
   */
    private void launchListeners() throws ErrorException {
        for (int i = 0; i < ports.length; i++) {
            requestListenersPool[i] = new RequestListener("Listener-" + i, ports[i], requestQueue, log);
            requestListenersPool[i].start();
        }
    }

    private void launchWorkers() {
        for (int i = 0; i < numOfWorkers; i++) {
            tasksQueue[i] = new TasksQueue();
            workersPool[i] = new Worker("Worker-" + i, tasksQueue[i], logicalTargetList, clientSessions, log);
            workersPool[i].setTargetsPath(targetsPath);
            workersPool[i].setCachePath(cachePath);
            workersPool[i].setDeliveringMode(deliveringMode, deliveringSubtype);
            workersPool[i].setKeepAliveTimeout(keepAliveTimeout);
            workersPool[i].setMaxTxRate(maxTxRate);
            workersPool[i].setTrafficShaping(trafficShaping);
            workersPool[i].setIndependentMessageHeaders(independentMessageHeaders);
            workersPool[i].setPredictiveModel(predictiveModel);
            workersPool[i].start();
        }
    }
}
