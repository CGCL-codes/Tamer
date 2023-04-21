package org.gridbus.workflow.scheduler;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.net.MalformedURLException;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;
import java.util.Map;
import java.util.TreeSet;
import org.apache.log4j.Logger;
import org.gridbus.broker.common.ApplicationContext;
import org.gridbus.broker.common.BrokerStorage;
import org.gridbus.broker.common.ComputeServer;
import org.gridbus.broker.common.Job;
import org.gridbus.broker.common.Service;
import org.gridbus.broker.constants.JobStatus;
import org.gridbus.broker.constants.LocationType;
import org.gridbus.broker.constants.MiddlewareType;
import org.gridbus.broker.constants.ServiceType;
import org.gridbus.broker.event.JobListener;
import org.gridbus.broker.exceptions.GridBrokerException;
import org.gridbus.broker.jobdescription.Task;
import org.gridbus.broker.jobdescription.commands.CopyCommand;
import org.gridbus.broker.jobdescription.commands.ExecuteCommand;
import org.gridbus.broker.services.application.ApplicationService;
import org.gridbus.broker.services.compute.ServerFactory;
import org.gridbus.broker.util.ReflectionUtil;
import org.gridbus.broker.util.SleepUtil;
import org.gridbus.gmd.soap.gmdqueryclient.GAMService;
import org.gridbus.gmd.soap.gmdqueryclient.GMDQuery;
import org.gridbus.gmd.soap.gmdqueryclient.GMDQueryException;
import org.gridbus.scs.common.SCSJob;
import org.gridbus.workflow.common.InOutData;
import org.gridbus.workflow.common.IoModelType;
import org.gridbus.workflow.common.OutData;
import org.gridbus.workflow.common.Queue;
import org.gridbus.workflow.common.TaskNode;
import org.gridbus.workflow.common.WfTask;
import org.gridbus.workflow.common.WfVariable;
import com.ibm.tspaces.Callback;
import com.ibm.tspaces.Field;
import com.ibm.tspaces.SuperTuple;
import com.ibm.tspaces.Tuple;
import com.ibm.tspaces.TupleSpace;
import com.ibm.tspaces.TupleSpaceException;

/**
 * @author Luyao Wu(wuluyao@gmail.com) Master of Engineering in Distributed
 *         Computing 15th Apr, 2007
 */
public class TaskManager extends Thread implements Callback, JobListener {

    private TaskNode node;

    private TupleSpace tupleSpace;

    private Queue inputQueue = new Queue();

    private Queue statusQueue = new Queue();

    private TreeSet inputEvents = new TreeSet();

    private int numberParents = 0;

    private int numberJobs = 1;

    private String accessPointUrl;

    private Hashtable outputTable;

    private boolean copyBack = false;

    private ComputeServer csnext = null;

    private boolean discoveryDone = false;

    private boolean done = false;

    private boolean isParameterSweep;

    private boolean isFristLevelNode;

    private boolean parentDone = false;

    private WorkflowCoordinator workflowCoordinator = null;

    private final Logger logger = Logger.getLogger(TaskManager.class);

    private Hashtable inputEventTuple = new Hashtable();

    private BrokerStorage store = null;

    public TaskManager(TaskNode node, TupleSpace tupleSpace, BrokerStorage store, WorkflowCoordinator workflowCoordinator, boolean isFristLevelNode, boolean copyBack) {
        this.node = node;
        this.tupleSpace = tupleSpace;
        this.store = store;
        this.workflowCoordinator = workflowCoordinator;
        this.isParameterSweep = node.getwTask().isParameterSweep();
        this.parentDone = isFristLevelNode;
        this.isFristLevelNode = isFristLevelNode;
        this.copyBack = copyBack;
        this.outputTable = new Hashtable();
    }

    /**
	 *  Tom:
	 *  Empty implementation?
	 */
    public void statusChanged(SCSJob j) {
        System.err.println("statusChanged Empty implemntation");
    }

    public void run() {
        logger.debug("====================" + node.getName() + " Task manager Running =========================");
        ApplicationContext.addListener(node.getApplication().getId(), this);
        int inputEvent;
        Enumeration ins = node.getFromInputs().keys();
        while (ins.hasMoreElements()) {
            String taskPortNo = (String) ins.nextElement();
            try {
                int index = taskPortNo.indexOf(":");
                Tuple template = new Tuple(taskPortNo.substring(0, index), taskPortNo.substring(index + 1), new Field(String.class));
                inputEvent = tupleSpace.eventRegister(TupleSpace.WRITE, template, this, false);
                inputEvents.add(new Integer(inputEvent));
                Tuple template1 = new Tuple(taskPortNo.substring(0, index), "status", new Field(String.class));
                tupleSpace.eventRegister(TupleSpace.WRITE, template1, this, false);
                numberParents++;
            } catch (TupleSpaceException tse) {
                System.err.println(tse.getStackTrace());
            }
        }
        int totaljobs = (int) node.getwTask().getTotalJobs();
        numberJobs = totaljobs;
        if (node.getwTask().getIoModel() == IoModelType.MANY_MANY) {
            numberJobs = 0;
        }
        int waitInput = node.getFromInputs().size();
        logger.debug("number of inputs for " + node.getName() + " is " + waitInput);
        if (isParameterSweep) {
            getInputs(waitInput);
        }
        for (int j = 0; j < totaljobs; j++) {
            if (!isParameterSweep && node.getwTask().getIoModel() != IoModelType.SYCHRONIZING) {
                waitInput = node.getFromInputs().size();
                getInputs(waitInput);
            }
            logger.debug("**************" + node.getName() + " get all the resources and being executed *************");
            try {
                tupleSpace.write(node.getName(), "status", "executing");
            } catch (TupleSpaceException e) {
                e.printStackTrace();
            }
            WfTask tsk = node.getwTask();
            String serviceID = tsk.getServiceMappingId();
            if (serviceID == null) {
                GMDQuery g = null;
                try {
                    g = new GMDQuery();
                } catch (GMDQueryException e1) {
                    e1.printStackTrace();
                }
                ListIterator serverListFromGMD = null;
                GAMService gamService = null;
                try {
                    serverListFromGMD = g.getServiceListByGAM(tsk.getExecutableName());
                } catch (GMDQueryException e) {
                    System.err.println(e.getMessage());
                    this.notifyStatus("failed");
                }
                if (serverListFromGMD.hasNext()) {
                    gamService = selectGMDService(serverListFromGMD);
                    logger.debug("******** Using GMD ********");
                    accessPointUrl = gamService.getAP();
                    logger.debug("******* Accesspoint for " + node.getName() + " is: " + accessPointUrl + " ********");
                    csnext = null;
                    csnext = getComputeServerByHostname(gamService.getHost());
                    if (csnext == null) {
                        try {
                            Service s = createComputerServer(gamService);
                            if (s != null) workflowCoordinator.addService(node.getApplication().getId(), s);
                        } catch (Exception e) {
                        }
                        csnext = getComputeServerByHostname(gamService.getHost());
                    }
                } else {
                    logger.debug("no matching services!!");
                    System.exit(0);
                }
            } else {
                logger.debug("******** appServiceID: " + serviceID + " ********");
                Map.Entry accessPointEntry = getAccessPoint(serviceID);
                accessPointUrl = (String) accessPointEntry.getValue();
                logger.debug("******* Accesspoint for " + node.getName() + " is: " + accessPointUrl + " ********");
                String serviceName = (String) accessPointEntry.getKey();
                logger.debug("******* mapping cs id: " + serviceName + " ********");
                csnext = null;
                logger.debug("Getting computer server........");
                csnext = getComputeServer(serviceName);
            }
            waitingForComputerServer();
            if (csnext.getMiddlewareType() != MiddlewareType.FORK) {
                pathDiscovery();
            }
            logger.debug("Creating Broker Job........");
            Job job = new Job();
            String jobName;
            if (isParameterSweep || node.getwTask().getIoModel() == IoModelType.MANY_MANY) {
                jobName = node.getName() + "_" + String.valueOf(j);
            } else {
                jobName = node.getName();
            }
            Task mainTask = null;
            logger.debug("jobName is " + jobName);
            if (node.getwTask().getIoModel() == IoModelType.SYCHRONIZING) {
                try {
                    mainTask = createSychTask(node, accessPointUrl, jobName, csnext);
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
            } else {
                try {
                    mainTask = createTask(node, accessPointUrl, jobName, csnext);
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
            }
            job.setTask(mainTask);
            job.setName(jobName);
            job.setApplication(node.getApplication());
            logger.debug("Broker Job Creating successful!!");
            logger.debug("*********** begin to savemapping *************");
            while (true) {
                boolean suc = workflowCoordinator.saveMapping(job, csnext);
                if (suc == true) {
                    logger.debug("Successful for Job " + job.getName() + "  submited to " + csnext.getHostname());
                    break;
                }
                logger.debug("Fail to Job " + job.getName() + "  submited to " + csnext.getHostname());
                SleepUtil.safeSleep(workflowCoordinator.getPollTime());
            }
            if (!isParameterSweep && node.getwTask().getIoModel() != IoModelType.MANY_MANY) {
                while (!done || inputQueue.isEmpty()) {
                    try {
                        Thread.sleep(3000);
                    } catch (InterruptedException e3) {
                        e3.printStackTrace();
                    }
                    if (inputQueue.isEmpty() && parentDone && done) {
                        j++;
                        break;
                    }
                }
                done = false;
                j--;
            }
            if (node.getwTask().getIoModel() == IoModelType.MANY_MANY) {
                numberJobs++;
                while (inputQueue.isEmpty()) {
                    if (parentDone) {
                        break;
                    }
                    try {
                        Thread.sleep(3000);
                    } catch (InterruptedException e3) {
                        e3.printStackTrace();
                    }
                }
                if (!parentDone || !inputQueue.isEmpty()) {
                    totaljobs++;
                }
            }
        }
        if (isParameterSweep || node.getwTask().getIoModel() == IoModelType.MANY_MANY) {
            while (numberJobs > 0 || !parentDone) {
                try {
                    Thread.sleep(3000);
                } catch (InterruptedException e3) {
                    e3.printStackTrace();
                }
            }
        }
        notifyStatus("done");
        finalize();
    }

    private Task createSychTask(TaskNode node, String accessPointUrl, String jobName, ComputeServer cs) throws Exception {
        while (!parentDone) {
            try {
                Thread.sleep(3000);
            } catch (InterruptedException e3) {
                e3.printStackTrace();
            }
        }
        Task brokerTask = new Task();
        List arg = new ArrayList();
        InOutData tin = null;
        OutData tout = null;
        String type = null;
        String from = null;
        String toPort = null;
        String value = null;
        int numLoop = 1;
        logger.debug("Creating Broker Task......");
        while (!inputQueue.isEmpty()) {
            int waitInput = node.getFromInputs().size();
            getInputs(waitInput);
            ArrayList inputlist = node.getwTask().getInputList();
            Iterator iteratorInputs = inputlist.iterator();
            logger.debug("**********Frist copy command******");
            if (!inputlist.isEmpty()) {
                while (iteratorInputs.hasNext()) {
                    tin = (InOutData) iteratorInputs.next();
                    type = tin.getType();
                    if (type.equals(InOutData.MSG) && tin.getArg() == true && numLoop == 1) {
                        arg.add(tin.getPara());
                    } else {
                        if (type.equals(InOutData.FILE)) {
                            String fileName = tin.getFileName();
                            toPort = tin.getPortNo();
                            if (node.getToInputs().containsKey(toPort)) {
                                from = node.getToInput(toPort);
                                value = node.getFromInput(from);
                                CopyCommand copy = new CopyCommand();
                                if (fileName == null) {
                                    int outputFileNameIndex = value.lastIndexOf("/") + 1;
                                    fileName = value.substring(outputFileNameIndex);
                                }
                                if (value.startsWith("gsiftp")) {
                                    copy.setSource(LocationType.REMOTE, value);
                                } else {
                                    copy.setSource(LocationType.LOCAL, value);
                                }
                                copy.setDestination(LocationType.NODE, fileName);
                                brokerTask.addCommand(copy);
                                if (tin.getArg() == true) {
                                    arg.add(fileName);
                                }
                                logger.debug("command: " + copy.toString());
                            }
                        } else {
                        }
                    }
                }
            }
            numLoop++;
            logger.debug("**********Frist copy command Finished!!******");
        }
        ArrayList outputlist = node.getwTask().getOutputList();
        outputTable.put(jobName, outputlist);
        Iterator iteratorOutputs = outputlist.iterator();
        logger.debug("**********Ececute command*******");
        ExecuteCommand exec = new ExecuteCommand();
        exec.setExecutable(accessPointUrl);
        for (int i = 0; i < arg.size(); i++) {
            exec.addArgument((String) arg.get(i));
        }
        brokerTask.addCommand(exec);
        if (cs.getMiddlewareType() != MiddlewareType.FORK) {
            ExecuteCommand execDelete = new ExecuteCommand();
            execDelete.setExecutable("rm -r -f ../../../workflowFiles/" + node.getApplication().getId() + "/" + jobName);
            brokerTask.addCommand(execDelete);
            ExecuteCommand execCreate = new ExecuteCommand();
            execCreate.setExecutable("mkdir -p ../../../workflowFiles/" + node.getApplication().getId());
            brokerTask.addCommand(execCreate);
            ExecuteCommand execRemoteCopy = new ExecuteCommand();
            execRemoteCopy.setExecutable("cp -dir -u ../" + jobName + " ../../../workflowFiles/" + node.getApplication().getId());
            brokerTask.addCommand(execRemoteCopy);
        }
        logger.debug("command: " + exec.toString());
        logger.debug("**********Ececute command finished!!*******");
        logger.debug("**********Second copy command********");
        if (copyBack || cs.getMiddlewareType() == MiddlewareType.FORK) {
            if (!outputlist.isEmpty()) {
                while (iteratorOutputs.hasNext()) {
                    tout = (OutData) iteratorOutputs.next();
                    type = tout.getType();
                    if (type.equals(OutData.FILE)) {
                        CopyCommand result = new CopyCommand();
                        result.setSource(LocationType.NODE, tout.getFileName());
                        result.setDestination(LocationType.LOCAL, tout.getFileName());
                        brokerTask.addCommand(result);
                        logger.debug("command: " + result.toString());
                    }
                }
            }
        }
        logger.debug("**********Second copy command finished!!!********");
        return brokerTask;
    }

    private ComputeServer createComputerServer(GAMService gamService) throws MalformedURLException {
        ComputeServer cs = null;
        int middleWareTypeValue = MiddlewareType.intValue(gamService.getMt());
        switch(middleWareTypeValue) {
            case MiddlewareType.GLOBUS:
                cs = createGlobusComputeServer(gamService);
                break;
            case MiddlewareType.UNKNOWN:
                System.err.println("no matching middleware, exit!!");
                System.exit(0);
        }
        cs.setPricePerCpuSec((float) gamService.getHPrice());
        cs.setPricePerJob((float) gamService.getSPrice());
        cs.setName(node.getName() + "compute");
        return cs;
    }

    private GAMService selectGMDService(ListIterator serverListFromGMD) {
        GAMService gamService;
        gamService = (GAMService) serverListFromGMD.next();
        return gamService;
    }

    private void waitingForComputerServer() {
        while (true) {
            csnext = getComputeServer(csnext.getName());
            if (csnext.isAvailable()) {
                break;
            }
            logger.debug("*********************");
            logger.debug("server " + csnext.getHostname() + " is NOT available!");
            logger.debug("*********************");
            SleepUtil.safeSleep(workflowCoordinator.getPollTime());
        }
        logger.debug("Successful Getting computer server!!!");
    }

    private void pathDiscovery() {
        if (!workflowCoordinator.pathTable.containsKey(csnext.getName())) {
            Job pathDiscoveryJob = new Job();
            pathDiscoveryJob.setName("discovery_" + node.getName());
            pathDiscoveryJob.setApplication(node.getApplication());
            Task discoveryTask = null;
            discoveryTask = createDiscoveryTask();
            pathDiscoveryJob.setTask(discoveryTask);
            while (true) {
                boolean suc = workflowCoordinator.saveMapping(pathDiscoveryJob, csnext);
                if (suc == true) {
                    logger.debug("Successful for pathDiscoveryJob submited to " + csnext.getHostname());
                    break;
                }
                logger.debug("Fail to pathDiscoveryJob submited to " + csnext.getHostname());
                SleepUtil.safeSleep(workflowCoordinator.getPollTime());
            }
            while (!discoveryDone) {
                try {
                    Thread.sleep(3000);
                } catch (InterruptedException e3) {
                    e3.printStackTrace();
                }
            }
            try {
                BufferedReader in = new BufferedReader(new FileReader("GBB.TMP/" + node.getApplication().getId() + "/" + "discovery_" + node.getName() + "/path.txt"));
                String str;
                while ((str = in.readLine()) != null) {
                    int a = str.indexOf("REMOTE.GBB.TMP");
                    logger.debug("******* discovery path:  " + str.substring(0, a));
                    workflowCoordinator.pathTable.put(csnext.getName(), str.substring(0, a));
                }
            } catch (IOException e) {
                System.err.println("********No path file found");
                System.exit(0);
            }
        }
    }

    private void getInputs(int waitInput) {
        while (waitInput != 0) {
            try {
                Thread.sleep(100);
            } catch (InterruptedException e3) {
                e3.printStackTrace();
            }
            if (!inputQueue.isEmpty()) {
                Tuple t = (Tuple) inputQueue.get();
                try {
                    String taskPortNo = (String) t.getField(0).getValue() + ":" + (String) t.getField(1).getValue();
                    String value = (String) t.getField(2).getValue();
                    logger.debug("input-" + value);
                    node.getFromInputs().remove(taskPortNo);
                    node.getFromInputs().put(taskPortNo, value);
                    waitInput--;
                } catch (TupleSpaceException e1) {
                    e1.printStackTrace();
                }
            }
        }
    }

    private Task createDiscoveryTask() {
        Task discoveryTask = new Task();
        ExecuteCommand exec = new ExecuteCommand();
        exec.setExecutable("echo `pwd` > path.txt");
        discoveryTask.addCommand(exec);
        CopyCommand result = new CopyCommand();
        result.setSource(LocationType.NODE, "path.txt");
        result.setDestination(LocationType.LOCAL, "path.txt");
        discoveryTask.addCommand(result);
        return discoveryTask;
    }

    private ComputeServer getComputeServer(String serviceName) {
        ComputeServer cs = null;
        try {
            cs = (ComputeServer) store.getService(node.getApplication().getId(), ServiceType.COMPUTE, serviceName);
        } catch (GridBrokerException e) {
            e.printStackTrace();
        }
        return cs;
    }

    private ComputeServer getComputeServerByHostname(String hostname) {
        ComputeServer cs = null;
        try {
            cs = (ComputeServer) store.getServiceByHostname(node.getApplication().getId(), hostname);
        } catch (GridBrokerException e) {
            e.printStackTrace();
        }
        return cs;
    }

    private Map.Entry getAccessPoint(String serviceId) {
        Map.Entry accesssPointEntry = null;
        try {
            ApplicationService appService = (ApplicationService) store.getServiceByMappingId(node.getApplication().getId(), ServiceType.APPLICATION, serviceId);
            if (appService != null) {
                Map aps = appService.getAccessPoints();
                if (aps.size() > 0) {
                    int r = (int) (Math.random() * aps.size());
                    accesssPointEntry = (Map.Entry) aps.entrySet().toArray()[r];
                    logger.debug("++++++++++ access number:  " + r + "  total acess:  " + aps.size());
                }
            }
        } catch (GridBrokerException e) {
            e.printStackTrace();
        }
        return accesssPointEntry;
    }

    public boolean call(String eventName, String tsName, int seqNum, SuperTuple tuple, boolean isException) {
        try {
            if (isException) {
                Exception e = (Exception) tuple.getField(0).getValue();
                if (e.getMessage().equals("Normal Close")) {
                    System.err.println("Normal close event from server");
                } else System.err.println(e.getMessage());
            }
        } catch (Exception e) {
            System.err.println(e.getMessage());
        }
        if (inputEvents.contains(new Integer(seqNum))) {
            inputQueue.put(tuple);
            logger.debug("############" + node.getName() + "  get a INPUT");
            inputEventTuple.put(tuple, new Integer(seqNum));
        } else {
            statusQueue.put(tuple);
            setParentDone();
            logger.debug("############" + node.getName() + "  get a EVENT");
        }
        return false;
    }

    public void statusChanged(Job j) {
        if (belongsToJob(j.getName(), node.getName())) {
            TupleSpace ts = null;
            try {
                Tuple active = TupleSpace.status("localhost", TupleSpace.DEFAULTPORT);
                if ((active == null) || active.getField(0).getValue().equals("Not Running")) {
                    logger.debug("TSpaces server not running on");
                }
                ts = new TupleSpace("WorkflowEvent", "localhost", TupleSpace.DEFAULTPORT);
            } catch (TupleSpaceException tse) {
                System.err.println(tse.getStackTrace());
            }
            if (j.getStatus() != JobStatus.DONE) {
                if (j.getStatus() == JobStatus.STAGE_IN) {
                    DataBaseOperation.getLock();
                    DataBaseOperation.DBInsert(node.getName(), "executing");
                    DataBaseOperation.setLock();
                }
                if (j.getStatus() == JobStatus.FAILED) {
                    DataBaseOperation.getLock();
                    DataBaseOperation.DBInsert(node.getName(), "fail");
                    DataBaseOperation.setLock();
                }
                try {
                    ts.write(node.getName(), "status", JobStatus.stringValue(j.getStatus()));
                    logger.debug("*******  " + node.getName() + " send a " + JobStatus.stringValue(j.getStatus()));
                } catch (Exception e) {
                    System.err.println(e.getStackTrace());
                }
            }
            if (j.getStatus() == JobStatus.DONE) {
                ArrayList outputlist = (ArrayList) outputTable.get(j.getName());
                Iterator iterator2 = outputlist.iterator();
                DataBaseOperation.getLock();
                DataBaseOperation.DBInsert(node.getName(), "done");
                DataBaseOperation.setLock();
                while (iterator2.hasNext()) {
                    OutData tt = (OutData) iterator2.next();
                    String path;
                    if (j.getServer().getMiddlewareType() == MiddlewareType.FORK) {
                        path = "GBB.TMP/" + node.getApplication().getId() + "/" + j.getName() + "/";
                    } else {
                        path = "gsiftp://" + csnext.getHostname() + (String) workflowCoordinator.pathTable.get(csnext.getName()) + "workflowFiles/" + node.getApplication().getId() + "/" + j.getName() + "/";
                    }
                    notifyOutput(node.getName(), tt.getPortNo(), path + tt.getFileName());
                    logger.debug("*********Notice the output");
                }
                if (!isParameterSweep && node.getwTask().getIoModel() != IoModelType.MANY_MANY) {
                    try {
                        store.removeJob(j);
                    } catch (GridBrokerException ex) {
                        ex.printStackTrace();
                    }
                    done = true;
                } else {
                    numberJobs--;
                }
            }
        }
        if (j.getName().startsWith("discovery") && belongsToJob(j.getName(), node.getName()) && j.getStatus() == JobStatus.DONE) {
            discoveryDone = true;
        }
    }

    private boolean belongsToJob(String jName, String nodeName) {
        String tmpName = jName;
        if (jName.startsWith("discovery")) {
            int index = tmpName.lastIndexOf("_");
            tmpName = tmpName.substring(index + 1);
            if (tmpName.equals(nodeName)) {
                return true;
            } else {
                return false;
            }
        } else {
            int index = tmpName.indexOf("_");
            if (index > 0) {
                tmpName = tmpName.substring(0, index);
                if (nodeName.equals(tmpName)) {
                    return true;
                } else {
                    return false;
                }
            } else {
                if (jName.equals(nodeName)) {
                    return true;
                } else {
                    return false;
                }
            }
        }
    }

    private Task createTask(TaskNode node, String accessPointUrl, String jobName, ComputeServer cs) throws Exception {
        Task brokerTask = new Task();
        WfVariable rv = node.getwTask().getRangeVars();
        ArrayList inputlist = replaceInputList(rv, node.getwTask().getInputList());
        ArrayList outputlist = replaceOutputList(rv, node.getwTask().getOutputList());
        outputTable.put(jobName, outputlist);
        Iterator iteratorInputs = inputlist.iterator();
        Iterator iteratorOutputs = outputlist.iterator();
        ArrayList arg = new ArrayList();
        InOutData tin = null;
        OutData tout = null;
        String type = null;
        String from = null;
        String toPort = null;
        String value = null;
        logger.debug("Creating Broker Task......");
        logger.debug("**********Frist copy command******");
        if (!inputlist.isEmpty()) {
            while (iteratorInputs.hasNext()) {
                tin = (InOutData) iteratorInputs.next();
                type = tin.getType();
                if (type.equals(InOutData.MSG) && tin.getArg() == true) {
                    arg.add(tin.getPara());
                } else {
                    if (type.equals(InOutData.FILE)) {
                        String fileName = tin.getFileName();
                        if (!isFristLevelNode) {
                            toPort = tin.getPortNo();
                            if (node.getToInputs().containsKey(toPort)) {
                                from = node.getToInput(toPort);
                                value = node.getFromInput(from);
                                CopyCommand copy = new CopyCommand();
                                if (fileName == null) {
                                    int outputFileNameIndex = value.lastIndexOf("/") + 1;
                                    fileName = value.substring(outputFileNameIndex);
                                }
                                if (value.startsWith("gsiftp")) {
                                    copy.setSource(LocationType.REMOTE, value);
                                } else {
                                    copy.setSource(LocationType.LOCAL, value);
                                }
                                copy.setDestination(LocationType.NODE, fileName);
                                brokerTask.addCommand(copy);
                                if (tin.getArg() == true) {
                                    arg.add(fileName);
                                }
                                logger.debug("command: " + copy.toString());
                            }
                        } else {
                            CopyCommand copy = new CopyCommand();
                            copy.setSource(LocationType.LOCAL, fileName);
                            copy.setDestination(LocationType.NODE, fileName);
                            brokerTask.addCommand(copy);
                            if (tin.getArg() == true) {
                                arg.add(fileName);
                            }
                            logger.debug("command: " + copy.toString());
                        }
                    } else {
                    }
                }
            }
        }
        logger.debug("**********Frist copy command Finished!!******");
        logger.debug("**********Ececute command*******");
        ExecuteCommand exec = new ExecuteCommand();
        if (node.getwTask().isParameterSweep() == true) {
            exec.setExecutable(accessPointUrl);
            for (int i = 0; i < arg.size(); i++) {
                String argument = (String) arg.get(i);
                exec.addArgument(argument);
            }
        } else {
            exec.setExecutable(accessPointUrl);
            for (int i = 0; i < arg.size(); i++) {
                exec.addArgument((String) arg.get(i));
            }
        }
        brokerTask.addCommand(exec);
        if (cs.getMiddlewareType() != MiddlewareType.FORK) {
            ExecuteCommand execDelete = new ExecuteCommand();
            execDelete.setExecutable("rm -r -f ../../../workflowFiles/" + node.getApplication().getId() + "/" + jobName);
            brokerTask.addCommand(execDelete);
            ExecuteCommand execCreate = new ExecuteCommand();
            execCreate.setExecutable("mkdir -p ../../../workflowFiles/" + node.getApplication().getId());
            brokerTask.addCommand(execCreate);
            ExecuteCommand execRemoteCopy = new ExecuteCommand();
            execRemoteCopy.setExecutable("cp -dir -u ../" + jobName + " ../../../workflowFiles/" + node.getApplication().getId());
            brokerTask.addCommand(execRemoteCopy);
        }
        logger.debug("command: " + exec.toString());
        logger.debug("**********Ececute command finished!!*******");
        logger.debug("**********Second copy command********");
        if (copyBack || cs.getMiddlewareType() == MiddlewareType.FORK) {
            if (!outputlist.isEmpty()) {
                while (iteratorOutputs.hasNext()) {
                    tout = (OutData) iteratorOutputs.next();
                    type = tout.getType();
                    if (type.equals(OutData.FILE)) {
                        CopyCommand result = new CopyCommand();
                        result.setSource(LocationType.NODE, tout.getFileName());
                        result.setDestination(LocationType.LOCAL, tout.getFileName());
                        brokerTask.addCommand(result);
                        logger.debug("command: " + result.toString());
                    }
                }
            }
        }
        logger.debug("**********Second copy command finished!!!********");
        return brokerTask;
    }

    public ArrayList replaceInputList(WfVariable rv, ArrayList list) {
        ArrayList replacedList = new ArrayList();
        String type = null;
        for (int i = 0; i < list.size(); i++) {
            InOutData tintmp = (InOutData) list.get(i);
            InOutData tin;
            try {
                tin = tintmp.copy();
                type = tin.getType();
                if (type.equals(InOutData.MSG)) {
                    String argumentName = tin.getPara();
                    argumentName = constVariableReplace(node, argumentName);
                    argumentName = rangeVaribaleReplace(rv, argumentName);
                    tin.setPara(argumentName);
                } else {
                    if (type.equals(InOutData.FILE)) {
                        String fileName = tin.getFileName();
                        if (fileName != null) {
                            fileName = constVariableReplace(node, fileName);
                            fileName = rangeVaribaleReplace(rv, fileName);
                        }
                        tin.setFileName(fileName);
                    } else {
                        String dirName = tin.getDirName();
                        if (dirName != null) {
                            dirName = constVariableReplace(node, dirName);
                            dirName = rangeVaribaleReplace(rv, dirName);
                        }
                        tin.setDirName(dirName);
                    }
                }
                replacedList.add(tin);
            } catch (CloneNotSupportedException e) {
                e.printStackTrace();
            }
        }
        return replacedList;
    }

    public ArrayList replaceOutputList(WfVariable rv, ArrayList list) {
        ArrayList replacedList = new ArrayList();
        String type = null;
        for (int i = 0; i < list.size(); i++) {
            OutData tintmp = (OutData) list.get(i);
            OutData tin;
            try {
                tin = tintmp.copy();
                type = tin.getType();
                if (type.equals(OutData.MSG)) {
                    String argumentName = tin.getPara();
                    argumentName = constVariableReplace(node, argumentName);
                    argumentName = rangeVaribaleReplace(rv, argumentName);
                    tin.setPara(argumentName);
                } else {
                    if (type.equals(OutData.FILE)) {
                        String fileName = tin.getFileName();
                        fileName = constVariableReplace(node, fileName);
                        fileName = rangeVaribaleReplace(rv, fileName);
                        tin.setFileName(fileName);
                    } else {
                        String dirName = tin.getDirName();
                        dirName = constVariableReplace(node, dirName);
                        dirName = rangeVaribaleReplace(rv, dirName);
                        tin.setDirName(dirName);
                    }
                }
                replacedList.add(tin);
            } catch (CloneNotSupportedException e) {
                e.printStackTrace();
            }
        }
        return replacedList;
    }

    private String constVariableReplace(TaskNode node, String argumentName) {
        argumentName = node.getwTask().ConstVarialeReplace(argumentName);
        return argumentName;
    }

    private String rangeVaribaleReplace(WfVariable rv, String argument) {
        if (rv != null && !rv.getVarName().equals("") && argument.indexOf(rv.getVarName()) >= 0) {
            argument = argument.replace(rv.getVarName(), rv.getValue());
        }
        return argument;
    }

    public void finalize() {
        ApplicationContext.removeListener(node.getApplication().getId(), this);
        try {
            super.finalize();
        } catch (Throwable e3) {
            e3.printStackTrace();
        }
    }

    public void notifyOutput(String taskNo, String portNo, String value) {
        TupleSpace ts = null;
        try {
            Tuple active = TupleSpace.status("localhost", TupleSpace.DEFAULTPORT);
            if (active == null || active.getField(0).getValue().equals("Not Running")) {
                logger.debug("TSpaces server not running on");
            }
            ts = new TupleSpace("WorkflowEvent", "localhost", TupleSpace.DEFAULTPORT);
        } catch (TupleSpaceException tse) {
            System.err.println(tse.getStackTrace());
        }
        try {
            ts.write(taskNo, portNo, value);
            logger.debug("********Notice the OUTPUT " + taskNo + " " + portNo + " " + value);
        } catch (Exception e) {
            System.err.println(e.getStackTrace());
        }
    }

    public void notifyStatus(String status) {
        if (status.equals("failed")) {
            logger.debug("********  " + node.getName() + "  failed!");
            System.exit(0);
        }
        if (status.equals("done")) {
            TupleSpace ts = null;
            try {
                Tuple active = TupleSpace.status("localhost", TupleSpace.DEFAULTPORT);
                if ((active == null) || active.getField(0).getValue().equals("Not Running")) {
                    logger.debug("TSpaces server not running on");
                }
                ts = new TupleSpace("WorkflowEvent", "localhost", TupleSpace.DEFAULTPORT);
            } catch (TupleSpaceException tse) {
                System.err.println(tse.getStackTrace());
            }
            try {
                ts.write(node.getName(), "status", "done");
                logger.debug("############  job " + node.getName() + " sent done");
            } catch (Exception e) {
                System.err.println(e.getStackTrace());
            }
        }
    }

    public void jobReset(Job j) {
    }

    private void setParentDone() {
        while (!statusQueue.isEmpty()) {
            Tuple t = (Tuple) statusQueue.get();
            try {
                String v = (String) t.getField(2).getValue();
                if (v.equals("done")) {
                    numberParents--;
                } else if (v.equals("failed")) {
                    notifyStatus("failed");
                    break;
                }
            } catch (TupleSpaceException e1) {
                e1.printStackTrace();
            }
            if (numberParents == 0) {
                parentDone = true;
                break;
            }
        }
    }

    private ComputeServer createGlobusComputeServer(GAMService gamService) {
        ComputeServer gcs = null;
        String hostname = gamService.getHost();
        String fileStagingURL = "gsiftp://" + gamService.getHost() + "//tmp/";
        int middleware = MiddlewareType.GLOBUS;
        gcs = ServerFactory.getComputeServer(middleware, hostname);
        ReflectionUtil.setProperty(gcs, "fileStagingURL", fileStagingURL);
        return gcs;
    }
}
