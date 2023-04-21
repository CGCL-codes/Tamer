package seqSamoa;

import java.io.File;
import java.io.IOException;
import java.io.OutputStream;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.logging.ConsoleHandler;
import java.util.logging.FileHandler;
import java.util.logging.Handler;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.logging.SimpleFormatter;
import java.util.logging.StreamHandler;
import org.jdom.Document;
import org.jdom.Element;
import org.jdom.JDOMException;
import org.jdom.input.SAXBuilder;
import seqSamoa.exceptions.AlreadyBoundServiceException;
import seqSamoa.exceptions.AlreadyExistingProtocolModuleException;
import seqSamoa.exceptions.AlreadyExistingServiceException;
import seqSamoa.exceptions.SamoaClassException;
import seqSamoa.protocols.pt2pt.ProtocolPT2PT;
import seqSamoa.protocols.rpt2pt.ProtocolRPT2PT;
import seqSamoa.protocols.udp.ProtocolUDP;
import seqSamoa.services.monitoring.ProcessSuspicion;
import seqSamoa.services.pt2pt.PT2PT;
import seqSamoa.services.rpt2pt.RPT2PT;
import seqSamoa.services.udp.UDP;
import uka.transport.Transportable;
import framework.PID;
import framework.libraries.BinaryStableStorage;
import framework.libraries.StableStorage;
import framework.libraries.serialization.TList;

/**
 * <CODE>ProtocolStack</CODE> is the base class for all protocol stacks.
 */
public class ProtocolStack {

    protected boolean isReconfigured;

    protected SamoaFlowControl fc;

    protected SamoaScheduler scheduler;

    private Callback callback;

    private PID myself;

    private TList processes;

    private StableStorage storage;

    private String recoverLogFileName;

    private Logger logger;

    protected HashMap<String, ProtocolModule> allProtocols = new HashMap<String, ProtocolModule>();

    @SuppressWarnings("unchecked")
    protected HashMap<String, Service> allServices = new HashMap<String, Service>();

    @SuppressWarnings("unchecked")
    private HashMap<String, Service.Listener> allFinalListeners = new HashMap<String, Service.Listener>();

    protected ProtocolModule pFake;

    protected UDP udp;

    protected ProtocolUDP pUDP;

    protected RPT2PT rpt2pt;

    protected ProcessSuspicion processSuspicion;

    protected ProtocolRPT2PT pRPT2PT;

    protected PT2PT pt2pt;

    protected ProtocolPT2PT pPT2PT;

    @SuppressWarnings("unchecked")
    protected void registerService(Service s) throws AlreadyExistingServiceException {
        if (!this.allServices.containsKey(s.name)) this.allServices.put(s.name, s); else throw new AlreadyExistingServiceException(s);
    }

    protected void registerProtocolModule(ProtocolModule p) throws AlreadyExistingProtocolModuleException {
        if (!this.allProtocols.containsKey(p.name)) this.allProtocols.put(p.name, p); else throw new AlreadyExistingProtocolModuleException(p);
    }

    @SuppressWarnings("unchecked")
    protected void registerFinalListener(Service.Listener l) {
        this.allFinalListeners.put(l.getService().name, l);
    }

    /**
	 * Constructor for stack in a crash-stop model with default values:
	 * with udp and rpt2pt, new scheduler, new flow control, log on System.out and log producer crash-stop protocols 
	 *
	 * 
	 * @param myself
	 *            the Process ID of the stack (should be unique)
	 * @param processes
	 * 			  the list of processes that run the same stack
	 * @param callback
	 * 			  the {@link seqSamoa.Callback callback} that gets the responses of this stack
	 */
    public ProtocolStack(PID myself, TList processes, Callback callback) {
        create_stack(myself, processes, new SamoaScheduler(new SequentialManager()), new SamoaFlowControl(100), callback, null, "groupcomm", true, true, false);
        this.recoverLogFileName = null;
        this.storage = null;
    }

    /**
	 * Constructor for stack in a crash-stop model.
	 * 
	 * @param myself
	 *            the Process ID of the stack (should be unique)
	 * @param processes
	 * 			  the list of processes that run the same stack
	 * @param scheduler
	 * 			  the {@link seqSamoa.SamoaScheduler scheduler} that manages executions in the stack
	 * @param fc
	 * 			  the {@link seqSamoa.SamoaFlowControl flowcontrol} dedicated this stack
	 * @param callback
	 * 			  the {@link seqSamoa.Callback callback} that gets the responses of this stack
	 * @param logFile
	 * 			  name of the file where to log the infos
	 * @param logName
	 * 			  name of the log producer
	 * @param udp
	 * 			  true, if the stack uses UDP network
	 * @param rp2p
	 * 			  true, if the stack uses reliable point to point channels 	 */
    public ProtocolStack(PID myself, TList processes, SamoaScheduler scheduler, SamoaFlowControl fc, Callback callback, String logFile, String logName, boolean udp, boolean rp2p) {
        create_stack(myself, processes, scheduler, fc, callback, logFile, logName, udp, rp2p, false);
        this.recoverLogFileName = null;
        this.storage = null;
    }

    /**
	 * Constructor for stack in a crash-recovery model with default values:
	 * with udp and pt2pt, new scheduler, new flowcontrol, log on System.out and log producer crash-stop protocols 
	 *
	 * 
	 * @param myself
	 *            the Process ID of the stack (should be unique)
	 * @param processes
	 * 			  the list of processes that run the same stack
	 * @param callback
	 * 			  the {@link seqSamoa.Callback callback} that gets the responses of this stack
	 * @param recoverLogName
	 * 			  name of the log file that contains info about recovery
	 * @param recoverFileName
	 *  		  name of the file that contains info logged by protocols for recovery
	 */
    public ProtocolStack(PID myself, TList processes, Callback callback, String recoverLogName, String recoverFileName) {
        create_stack(myself, processes, new SamoaScheduler(new SequentialManager()), new SamoaFlowControl(100), callback, null, "static_recovery", true, false, true);
        this.recoverLogFileName = recoverLogName;
        this.storage = new BinaryStableStorage(recoverFileName);
    }

    /**
	 * Constructor for stack in a crash-recovery model.
	 * 
	 * @param myself
	 *            the Process ID of the stack (should be unique)
	 * @param processes
	 * 			  the list of processes that run the same stack
	 * @param scheduler
	 * 			  the {@link seqSamoa.SamoaScheduler scheduler} that manages executions in the stack
	 * @param fc
	 * 			  the {@link seqSamoa.SamoaFlowControl flowcontrol} dedicated this stack
	 * @param callback
	 * 			  the {@link seqSamoa.Callback callback} that gets the responses of this stack
	 * @param logFile
	 * 			  name of the file where to log the infos
	 * @param logName
	 * 			  name of the log producer
	 * @param recoverLogName
	 * 			  name of the log file that contains info about recovery
	 * @param recoverFileName
	 *  		  name of the file that contains info logged by protocols for recovery
	 * @param udp
	 * 			  true, if the stack uses UDP network
	 * @param rp2p
	 * 			  true, if the stack uses reliable point to point channels (assumes a crash-stop model)
	 */
    public ProtocolStack(PID myself, TList processes, SamoaScheduler scheduler, SamoaFlowControl fc, Callback callback, String logFile, String logName, String recoverLogName, String recoverFileName, boolean udp, boolean rp2p) {
        create_stack(myself, processes, scheduler, fc, callback, logFile, logName, udp, false, rp2p);
        this.recoverLogFileName = recoverLogName;
        this.storage = new BinaryStableStorage(recoverFileName);
    }

    /**
	 * Constructor for stack according to an XMLfile with default values:
	 * with udp and pt2pt, new scheduler, new flowcontrol, log on System.out and log producer crash-stop protocols 
	 * 
	 * @param myself
	 *            the Process ID of the stack (should be unique)
	 * @param processes
	 * 			  the list of processes that run the same stack
	 * @param callback
	 * 			  the {@link seqSamoa.Callback callback} that gets the responses of this stack
	 * @param recoverLogName
	 * 			  name of the log file that contains info about recovery
	 * @param recoverFileName
	 *  		  name of the file that contains info logged by protocols for recovery
	 * @param XMLfile
	 * 			  name of the file that contains the description of the stack
	 */
    public ProtocolStack(PID myself, TList processes, Callback callback, String recoverLogName, String recoverFileName, String XMLfile) throws IOException, JDOMException, SamoaClassException, AlreadyExistingServiceException, AlreadyExistingProtocolModuleException {
        create_stack_from_xml(myself, processes, new SamoaScheduler(new SequentialManager()), new SamoaFlowControl(100), callback, null, null, recoverLogName, recoverFileName, XMLfile);
    }

    /**
	 * Constructor for stack according to an XMLfile.
	 * 
	 * @param myself
	 *            the Process ID of the stack (should be unique)
	 * @param processes
	 * 			  the list of processes that run the same stack
	 * @param scheduler
	 * 			  the {@link seqSamoa.SamoaScheduler scheduler} that manages executions in the stack
	 * @param fc
	 * 			  the {@link seqSamoa.SamoaFlowControl flowcontrol} dedicated this stack
	 * @param callback
	 * 			  the {@link seqSamoa.Callback callback} that gets the responses of this stack
	 * @param logFile
	 * 			  name of the file where to log the infos
	 * @param logName
	 * 			  name of the log producer
	 * @param recoverLogName
	 * 			  name of the log file that contains info about recovery
	 * @param recoverFileName
	 *  		  name of the file that contains info logged by protocols for recovery
	 * @param XMLfile
	 * 			  name of the file that contains the description of the stack
	 */
    public ProtocolStack(PID myself, TList processes, SamoaScheduler scheduler, SamoaFlowControl fc, Callback callback, String logFile, String logName, String recoverLogName, String recoverFileName, String XMLfile) throws IOException, JDOMException, SamoaClassException, AlreadyExistingServiceException, AlreadyExistingProtocolModuleException {
        create_stack_from_xml(myself, processes, scheduler, fc, callback, logFile, logName, recoverLogName, recoverFileName, XMLfile);
    }

    @SuppressWarnings("unchecked")
    private void create_stack_from_xml(PID myself, TList processes, SamoaScheduler scheduler, SamoaFlowControl fc, Callback callback, String logFile, String logName, String recoverLogName, String recoverFileName, String XMLfile) throws IOException, JDOMException, SamoaClassException, AlreadyExistingServiceException, AlreadyExistingProtocolModuleException {
        File xmlfile = new File(XMLfile);
        if (!xmlfile.exists()) throw new SamoaClassException("The stack description does not exist: " + XMLfile);
        SAXBuilder sxb = new SAXBuilder();
        Document document = sxb.build(xmlfile);
        List protocols = document.getRootElement().getChild("Protocols").getChildren("Protocol");
        List services = document.getRootElement().getChild("Services").getChildren("Service");
        for (int i = 0; i < protocols.size(); i++) {
            Element prot = (Element) protocols.get(i);
            String model = prot.getChildText("Model");
            if ((model != null) && (model.equals("Crash recovery"))) {
                this.recoverLogFileName = recoverLogName;
                this.storage = new BinaryStableStorage(recoverFileName);
            }
        }
        this.myself = myself;
        this.processes = processes;
        this.scheduler = scheduler;
        this.fc = fc;
        this.callback = callback;
        this.isReconfigured = false;
        if (logName != null) {
            try {
                Handler logHandler = null;
                logger = Logger.getLogger(logName);
                if (logFile == null) {
                    logHandler = new ConsoleHandler();
                } else if (logFile.equals("out")) {
                    logHandler = new StreamHandler(System.out, new SimpleFormatter());
                } else {
                    logHandler = new FileHandler(logFile);
                    logHandler.setFormatter(new SimpleFormatter());
                }
                logHandler.setLevel(Level.ALL);
                logger.setLevel(Level.OFF);
                logger.addHandler(logHandler);
            } catch (IOException ioe) {
                throw new RuntimeException("Wrong log file name: " + ioe.getMessage());
            }
        }
        if (!processes.isEmpty() && !processes.contains(myself)) throw new RuntimeException("Bad arguments: the local PID " + "is not contained in the group!!");
        try {
            this.pFake = new ProtocolModule("fake", this);
            this.allProtocols.remove("fake");
        } catch (AlreadyExistingProtocolModuleException aem) {
            throw new RuntimeException("Should not be possible! Bug in conception.");
        }
        Iterator itServices = services.iterator();
        while (itServices.hasNext()) {
            Element service = (Element) itServices.next();
            newService(service);
        }
        Iterator itProtocols = protocols.iterator();
        while (itProtocols.hasNext()) {
            Element module = (Element) itProtocols.next();
            newProtocolModule(module);
        }
    }

    private void create_stack(PID myself, TList processes, SamoaScheduler scheduler, SamoaFlowControl fc, Callback callback, String logFile, String logName, boolean udp, boolean rp2p, boolean p2p) {
        this.myself = myself;
        this.processes = processes;
        this.scheduler = scheduler;
        this.fc = fc;
        this.callback = callback;
        this.isReconfigured = false;
        if (logName != null) {
            try {
                Handler logHandler = null;
                logger = Logger.getLogger(logName);
                if (logFile == null) {
                    logHandler = new ConsoleHandler();
                } else if (logFile.equals("out")) {
                    logHandler = new StreamHandler(System.out, new SimpleFormatter());
                } else {
                    logHandler = new FileHandler(logFile);
                    logHandler.setFormatter(new SimpleFormatter());
                }
                logHandler.setLevel(Level.ALL);
                logger.setLevel(Level.OFF);
                logger.addHandler(logHandler);
            } catch (IOException ioe) {
                throw new RuntimeException("Wrong log file name: " + ioe.getMessage());
            }
        }
        if (!processes.isEmpty() && !processes.contains(myself)) throw new RuntimeException("Bad arguments: the local PID " + "is not contained in the group!!");
        try {
            this.pFake = new ProtocolModule("fake", this);
            this.allProtocols.remove("fake");
            if (udp || p2p) {
                this.udp = new UDP("udp", this);
                this.pUDP = new ProtocolUDP(new String("udp"), this, this.udp);
            }
            if (rp2p) {
                this.rpt2pt = new RPT2PT("rpt2pt", this);
                this.processSuspicion = new ProcessSuspicion("processSuspicion", this);
                this.pRPT2PT = new ProtocolRPT2PT(new String("rpt2pt"), this, 3000, 30000, 5000, rpt2pt, processSuspicion);
            }
            if (p2p) {
                this.pt2pt = new PT2PT("pt2pt", this);
                this.pPT2PT = new ProtocolPT2PT(new String("pt2pt"), this, 100, pt2pt, this.udp);
            }
        } catch (AlreadyExistingServiceException aes) {
            throw new RuntimeException("Should not be possible! Bug in conception.");
        } catch (AlreadyExistingProtocolModuleException aem) {
            throw new RuntimeException("Should not be possible! Bug in conception.");
        }
    }

    /**
	 * Call a {@link seqSamoa.Service service}
	 * 
	 * @param serviceName
	 *            the name identifying the {@link seqSamoa.Service service} to call
	 * @param params
	 *            the parameters of the {@link seqSamoa.Service service} call
	 * @param toSend
	 *            the message of the {@link seqSamoa.Service service} call
	 */
    @SuppressWarnings("unchecked")
    public synchronized void serviceCall(final String serviceName, final Object params, final Transportable toSend) {
        Service service = allServices.get(serviceName);
        Message dmessage = null;
        if (toSend != null) dmessage = new Message(toSend, allFinalListeners.get(serviceName));
        fc.enter();
        long cid = service.externalCall(params, dmessage);
        this.scheduler.waitEnd(cid);
    }

    /**
	 * Return the {@link seqSamoa.SamoaFlowControl flowcontrol} dedicated to this stack
	 * 
	 * @return
	 * 		the {@link seqSamoa.SamoaFlowControl flowcontrol}
	 */
    public SamoaFlowControl getFlowControl() {
        return this.fc;
    }

    /**
	 * Return the {@link seqSamoa.SamoaScheduler scheduler} dedicated to this stack
	 * 
	 * @return
	 * 		the {@link seqSamoa.SamoaScheduler scheduler}
	 */
    public SamoaScheduler getScheduler() {
        return this.scheduler;
    }

    /**
	 * Return the process ID attached to this stack
	 * 
	 * @return
	 * 		the process ID attached to this stack
	 */
    public PID getPID() {
        return this.myself;
    }

    /**
	 * Return the group of processes that run the same stack
	 * 
	 * @return
	 * 		the group of processes that run the same stack
	 */
    public TList getGroup() {
        return this.processes;
    }

    /**
	 * Set the group of processes that run the same stack
	 */
    public void setGroup(TList processes) {
        this.processes = processes;
    }

    /**
	 * Return the reconfiguration status of the stack. Note that
	 *  when the stack will be reconfigured (status = true),
	 *  there is no concurrency in the stack.
	 *  
	 *  @return
	 *  	the reconfiguration status of the stack
	 */
    public boolean getReconfigurationStatus() {
        return this.isReconfigured;
    }

    /**
	 * Set the reconfiguration status of the stack. Note that
	 *  when the stack will be reconfigured, there is no concurrency 
	 *  in the stack.
	 * 
	 * @param isReconfigured
	 * 		true, if the stack has to be reconfigured. False otherwise
	 */
    public void setReconfigurationStatus(boolean isReconfigured) {
        this.isReconfigured = isReconfigured;
        if (!this.isReconfigured) this.scheduler.stackReconfigured(this);
    }

    /**
	 * Return the storage for committing information
	 * 
	 * @return
	 * 		the storage for committing information
	 */
    public StableStorage getStorage() {
        return this.storage;
    }

    /**
	 * Return the {@link seqSamoa.Callback callback} attached to the stack 
	 * 
	 * @return
	 * 		the {@link seqSamoa.Callback callback} attached to the stack
	 */
    public Callback getCallback() {
        return this.callback;
    }

    /**
	 * Return the {@link seqSamoa.Service service} that corresponds to the given name.
	 * @param name
	 * 		the name of the {@link seqSamoa.Service service}
	 * @return
	 * 		the {@link seqSamoa.Service service}
	 */
    @SuppressWarnings("unchecked")
    public Service getService(String name) {
        return this.allServices.get(name);
    }

    /**
	 * Return the {@link seqSamoa.ProtocolModule protocol} that corresponds to the given name.
	 * @param name
	 * 		the name of the {@link seqSamoa.ProtocolModule protocol}
	 * @return
	 * 		the {@link seqSamoa.ProtocolModule protocol}
	 */
    public ProtocolModule getProtocol(String name) {
        return this.allProtocols.get(name);
    }

    /**
	 * Create a new Service and add it to the stack 
	 * 
	 * @param service
	 * 		The XML element that describes the service to be created
	 * 
	 * @return 
	 * 		the {@link seqSamoa.Service service} that is created		
	 * 
	 * @throws SamoaClassException, AlreadyBoundServiceException
	 */
    @SuppressWarnings("unchecked")
    public Service newService(Element service) throws SamoaClassException, AlreadyExistingServiceException {
        String className = service.getChild("Class").getChildText("Name");
        String packageName = service.getChild("Class").getChildText("Package");
        String name = service.getChildText("Name");
        Service result = null;
        if (allServices.containsKey(name)) throw new AlreadyExistingServiceException(name);
        try {
            Class classService = Class.forName(packageName + "." + className);
            Object[] paramsService = new Object[] { name, this };
            Constructor constructorObject = classService.getConstructors()[0];
            result = (Service) constructorObject.newInstance(paramsService);
        } catch (ClassNotFoundException ex) {
            throw new SamoaClassException(service.toString() + " class can not be found");
        } catch (InstantiationException ex) {
            throw new SamoaClassException(service.toString() + " class can't be instantiated");
        } catch (IllegalAccessException ex) {
            throw new SamoaClassException(service.toString() + " could not access the constructor of the class");
        } catch (InvocationTargetException ex) {
            throw new SamoaClassException(service.toString() + "results in InvocationTargetException(" + ex.getMessage() + ") caused by  " + ex.getCause());
        }
        if (Boolean.parseBoolean(service.getChildText("ServiceFinal"))) {
            result.new Listener(this, new LinkedList<ServiceCallOrResponse>()) {

                public synchronized void evaluate(Object infos, Transportable message) {
                    callback.serviceCallback(infos, message);
                }
            };
        }
        if (!Boolean.parseBoolean(service.getChildText("ServiceProvided"))) {
            Service.Executer executer = result.new Executer(this, new LinkedList<ServiceCallOrResponse>()) {

                public synchronized void evaluate(Object params, Message dmessage) {
                    callback.serviceCallback(params, dmessage);
                }
            };
            try {
                executer.link();
            } catch (AlreadyBoundServiceException aes) {
                throw new RuntimeException("Should not be possible! Error in the conception.");
            }
        }
        return result;
    }

    /**
	 * Create a new ProtocolModule and add it to the stack
	 * 
	 * @param module
	 *            The XML element that describes the protocol module to be
	 *            created
	 *            
	 * @return
	 * 		the {@link seqSamoa.ProtocolModule protocol} that is created
	 * 
	 * @throws SamoaClassException,
	 *             AlreadyBoundServiceException
	 */
    @SuppressWarnings("unchecked")
    public ProtocolModule newProtocolModule(Element module) throws SamoaClassException, AlreadyExistingProtocolModuleException {
        String className = module.getChild("Class").getChildText("Name");
        String packageName = module.getChild("Class").getChildText("Package");
        String name = module.getChildText("Name");
        if (allProtocols.containsKey(module.getChildText("Name"))) throw new AlreadyExistingProtocolModuleException(module.getChildText("Name"));
        LinkedList<Object> protParams = new LinkedList<Object>();
        protParams.add(name);
        protParams.add(this);
        List params = module.getChild("Parameters").getChildren("Parameter");
        for (int i = 0; i < params.size(); i++) {
            Element param = (Element) params.get(i);
            String paramType = param.getChildText("Type");
            String paramValue = param.getChildText("Value");
            if (paramType.equals("java.lang.Integer")) {
                protParams.addLast(new Integer(paramValue));
            } else if (paramType.equals("java.lang.Boolean")) {
                protParams.addLast(new Boolean(paramValue));
            } else if (paramType.equals("java.lang.Long")) {
                protParams.addLast(new Long(param.getChildText("Value")));
            } else if (paramType.equals("java.lang.String")) {
                protParams.addLast(paramValue);
            }
        }
        List providedServices = module.getChild("ProvidedServices").getChildren("ProvidedService");
        for (int i = 0; i < providedServices.size(); i++) {
            String serviceName = ((Element) providedServices.get(i)).getChildText("Name");
            protParams.addLast(this.getService(serviceName));
        }
        List requiredServices = module.getChild("RequiredServices").getChildren("RequiredService");
        for (int i = 0; i < requiredServices.size(); i++) {
            String serviceName = ((Element) requiredServices.get(i)).getChildText("Name");
            protParams.addLast(this.getService(serviceName));
        }
        try {
            Class classModule = Class.forName(packageName + "." + className);
            Constructor constructorObject = classModule.getConstructors()[0];
            return (ProtocolModule) constructorObject.newInstance(protParams.toArray());
        } catch (ClassNotFoundException ex) {
            throw new SamoaClassException(module.toString() + " class can not be found");
        } catch (InstantiationException ex) {
            throw new SamoaClassException(module.toString() + " class can't be instantiated");
        } catch (IllegalAccessException ex) {
            throw new SamoaClassException(module.toString() + " could not access the constructor of the class");
        } catch (InvocationTargetException ex) {
            throw new SamoaClassException(module.toString() + "results in InvocationTargetException(" + ex.getMessage() + ") caused by  " + ex.getCause());
        }
    }

    @SuppressWarnings("unchecked")
    public String toString() {
        StringBuffer result = new StringBuffer();
        Iterator<String> it = allServices.keySet().iterator();
        while (it.hasNext()) {
            String nameService = it.next();
            Service service = allServices.get(nameService);
            String classService = service.getClass().toString();
            ProtocolModule prot = service.getProvider();
            if (prot != null) result.append(classService + " " + nameService + " provided by " + prot.getClass().toString() + " " + prot.name + " \n"); else result.append(classService + " " + nameService + " has no provider \n");
        }
        return result.toString();
    }

    /**
	 * Remove a protocol module from the stack
	 * 
	 * @param m
	 * 		the {@link seqSamoa.ProtocolModule protocol} to be removed
	 */
    public void removeProtocolModule(ProtocolModule m) {
        this.allProtocols.remove(m.name);
    }

    /**
	 * Initialize the stack: initialize the {@link seqSamoa.SamoaScheduler scheduler} 
	 * and all the {@link seqSamoa.ProtocolModule protocols} in the stack
	 * 
	 * @throws AlreadyBoundServiceException
	 */
    public void init() throws AlreadyBoundServiceException {
        Iterator<ProtocolModule> it = allProtocols.values().iterator();
        while (it.hasNext()) it.next().linkToService();
        it = allProtocols.values().iterator();
        while (it.hasNext()) it.next().bindInterceptors();
        this.scheduler.stackReconfigured(this);
        this.scheduler.start();
        AtomicTask initTask = new AtomicTask() {

            public void execute() {
                Iterator<ProtocolModule> it = allProtocols.values().iterator();
                while (it.hasNext()) it.next().init();
                if (recoverLogFileName != null) {
                    File recoveryLogFile = new File(recoverLogFileName);
                    if (recoveryLogFile.exists()) {
                        System.out.println("We have recovered, processing the recovery...");
                        it = allProtocols.values().iterator();
                        while (it.hasNext()) it.next().recovery(true);
                    } else {
                        try {
                            recoveryLogFile.createNewFile();
                        } catch (IOException ioe) {
                            throw new RuntimeException("ApiSamoaAbcastStack: IOException: " + " Impossible to create the recovery file !");
                        }
                        it = allProtocols.values().iterator();
                        while (it.hasNext()) it.next().recovery(false);
                    }
                }
                pFake.init();
            }
        };
        long cID = this.scheduler.schedule(initTask);
        this.scheduler.waitEnd(cID);
        it = allProtocols.values().iterator();
        while (it.hasNext()) {
            ProtocolModule prot = it.next();
            if (prot instanceof ProtocolUDP) ((ProtocolUDP) prot).startListener();
        }
    }

    /**
	 * Print the state of all {@link seqSamoa.ProtocolModule protocols} in the stack
	 * 
	 * @param stream
	 * 		the stream where the state is printed
	 */
    public void sendDump(OutputStream stream) {
        Iterator<ProtocolModule> it = allProtocols.values().iterator();
        while (it.hasNext()) it.next().dump(stream);
    }

    /**
	 * Set the mode for the log. 
	 * 
	 * @param mode
	 * 		If true, print all messages in the log. Otherwise, print nothing in the log.
	 */
    public void sendDebug(boolean mode) {
        if (mode) logger.setLevel(Level.ALL); else logger.setLevel(Level.OFF);
    }

    /**
	 * Commit the stack, i.e. all the {@link seqSamoa.ProtocolModule protocols} in the stack.
	 * This method also commit the state of the application by runnning a specified method
	 * dedicated to this task.
	 * 
	 * @param r
	 * 		the method that executes the commit of the application
	 */
    public synchronized void commit(final Runnable r) {
        AtomicTask task = new AtomicTask() {

            public void execute() {
                if (r != null) r.run();
                Iterator<ProtocolModule> it = allProtocols.values().iterator();
                while (it.hasNext()) {
                    it.next().commit();
                    it.remove();
                }
            }
        };
        long cID = this.scheduler.schedule(task);
        this.scheduler.waitEnd(cID);
    }

    /**
     * Close the stack, i.e. close all the {@link seqSamoa.ProtocolModule protocols}
     * of the stack and stop the {@link seqSamoa.SamoaScheduler scheduler} 
     */
    public void close() {
        this.finalize();
    }

    /**
	 * Finalize the satck. This method has the same effect as method close.
	 */
    public void finalize() {
        this.scheduler.close();
        Iterator<ProtocolModule> it = allProtocols.values().iterator();
        while (it.hasNext()) it.next().close();
        this.pFake.close();
    }
}
