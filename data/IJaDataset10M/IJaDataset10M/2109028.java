package org.jdesktop.mtgame;

import org.jdesktop.mtgame.processor.AWTEventProcessorComponent;
import com.jme.renderer.Camera;
import java.util.ArrayList;
import javolution.util.FastList;
import com.jme.math.Vector3f;

/**
 * This is the controller for all processor components.  It handles triggers and
 * schedules the processes on the various threads.  Processes can be scheduled
 * on the entity processors, or the render thread.
 * 
 * @author Doug Twilleager
 */
class ProcessorManager extends Thread {

    /**
     * The number of Processor Threads
     */
    private int numProcessorThreads = 0;

    /**
     * The number of processors on the client machine.
     */
    private int numProcessors = 1;

    /**
     * The array of ProcessorThreads
     */
    private ProcessorThread[] processorThreads = null;

    /**
     * The list of entities that wish to be triggered on every render frame
     */
    private ArrayList newFrameArmed = new ArrayList();

    /**
     * The list of entities that wish to be triggered on awt events
     */
    private ArrayList awtEventsArmed = new ArrayList();

    /**
     * The list of entities that with to be triggered after an amount of 
     * time has elapsed.
     */
    private ArrayList timeElapseArmed = new ArrayList();

    /**
     * Two lists to keep track of post event processing
     */
    private ArrayList postEventArmed = new ArrayList();

    private ArrayList postEventListeners = new ArrayList();

    /**
     * The current list of triggered processors
     */
    private ArrayList processorsTriggered = new ArrayList();

    /**
     * The systems WorldManager
     */
    WorldManager worldManager = null;

    /**
     * A flag to indicate whether we should run
     */
    private boolean done = false;

    /**
     * This flag tells the renderer whether or not to run
     */
    private boolean running = true;

    /**
     * A flag to say whether or not we are waiting for work
     */
    private boolean waiting = false;

    /**
     * A flag which tells us to run all processors in the render thread
     */
    private boolean runSingleThreaded = false;

    /**
     * The number of available processors.  
     */
    private int availableProcessors = 0;

    /**
     * The list of processors interested in being notified of lod changes
     */
    private FastList<ProcessorComponentLODObject> processorLODList = new FastList<ProcessorComponentLODObject>();

    /**
     * The array of increasing distances to use for processor component lod's
     */
    private float[] processorComponentLODLevels = new float[0];

    /**
     * The list of all processors
     */
    private FastList<ProcessorComponent> processorComponents = new FastList<ProcessorComponent>();

    /**
     * A class to hold ProcessorComponent LOD info
     */
    class ProcessorComponentLODObject {

        ProcessorComponent pc = null;

        ProcessorComponentLOD pclod = null;

        Object obj = null;

        ProcessorComponentLODObject(ProcessorComponentLOD pclod, ProcessorComponent pc, Object obj) {
            this.pclod = pclod;
            this.pc = pc;
            this.obj = obj;
        }
    }

    /**
     * The default constructor
     */
    ProcessorManager(WorldManager wm) {
        setName("Processor Manager Thread");
        worldManager = wm;
        numProcessors = Runtime.getRuntime().availableProcessors();
        if (System.getProperty("mtgame.runSingleThreaded") != null) {
            runSingleThreaded = true;
            System.out.println("MT Game Info: Running Processors Single Threaded");
        }
        numProcessorThreads = 2 * numProcessors;
        processorThreads = new ProcessorThread[numProcessorThreads];
        for (int i = 0; i < numProcessorThreads; i++) {
            processorThreads[i] = new ProcessorThread(this, i);
            processorThreads[i].initialize();
        }
    }

    /**
     * Initialize the process controller
     */
    synchronized void initialize() {
        this.start();
        try {
            wait();
        } catch (InterruptedException e) {
            System.out.println(e);
        }
    }

    /**
     * Initialize the controller
     */
    synchronized void initController() {
        notify();
    }

    void setRunning(boolean flag) {
        running = flag;
    }

    boolean getRunning() {
        return (running);
    }

    /**
     * Add a listener for processor component lod changes
     */
    void addProcessorComponentLOD(ProcessorComponentLOD lod, ProcessorComponent pc, Object obj) {
        synchronized (processorLODList) {
            processorLODList.add(new ProcessorComponentLODObject(lod, pc, obj));
        }
    }

    /**
     * Remove a ProcessorComponent to be tracked by the LOD system
     */
    void removeProcessorComponentLOD(ProcessorComponentLOD lod) {
        ProcessorComponentLODObject lodobj = null;
        synchronized (processorLODList) {
            for (int i = 0; i < processorLODList.size(); i++) {
                lodobj = processorLODList.get(i);
                if (lodobj.pclod == lod) {
                    processorLODList.remove(i);
                    break;
                }
            }
        }
    }

    /**
     * Get the processor level, given the point and camera
     */
    private int getProcessorLevel(Vector3f loc, Camera camera) {
        int level = 0;
        synchronized (processorComponentLODLevels) {
            if (processorComponentLODLevels == null) {
                return (level);
            }
            float dist = loc.distance(camera.getLocation());
            for (level = 0; level < processorComponentLODLevels.length; level++) {
                if (dist >= processorComponentLODLevels[level]) {
                    continue;
                } else {
                    break;
                }
            }
        }
        return (level);
    }

    /**
     * This goes through all the processors and notifies them when their
     * lod level has changed.
     */
    void updateProcessorComponentLODs(Camera camera) {
        Vector3f loc = new Vector3f();
        int level = -1;
        int lastLevel = -1;
        synchronized (processorLODList) {
            for (int i = 0; i < processorLODList.size(); i++) {
                ProcessorComponentLODObject lodobj = processorLODList.get(i);
                lodobj.pc.getLocation(loc);
                level = getProcessorLevel(loc, camera);
                lastLevel = lodobj.pc.getLODLevel();
                if (level != lastLevel) {
                    lodobj.pc.setLODLevel(level);
                    if (lodobj != null) {
                        lodobj.pclod.updateLOD(lodobj.pc, lastLevel, level, lodobj.obj);
                    }
                }
            }
        }
    }

    /**
     * Set the levels to be used for processor component lod's
     */
    void setProcessorComponentLODLevels(float[] levels) {
        float[] newLevels = null;
        if (levels != null) {
            newLevels = new float[levels.length];
        }
        System.arraycopy(levels, 0, newLevels, 0, levels.length);
        synchronized (processorComponentLODLevels) {
            processorComponentLODLevels = newLevels;
        }
    }

    /**
     * The main run loop
     */
    public void run() {
        ProcessorComponent[] runList = null;
        initController();
        while (!done) {
            while (!running) {
                try {
                    Thread.sleep(333, 0);
                } catch (InterruptedException e) {
                    System.out.println(e);
                }
            }
            runList = waitForProcessorsTriggered();
            dispatchTasks(runList);
            worldManager.runCommitList(runList);
            armProcessors(runList);
        }
        for (int i = 0; i < processorThreads.length; i++) {
            processorThreads[i].quit();
        }
    }

    void quit() {
        done = true;
    }

    /**
     * This method hands runList work off to the worker threads unit it is done.
     */
    synchronized void dispatchTasks(ProcessorComponent[] pcs) {
        int i = 0;
        for (int j = 0; j < pcs.length; j++) {
            ProcessorComponent pc = pcs[j];
            if (availableProcessors == 0) {
                try {
                    wait();
                } catch (InterruptedException e) {
                    System.out.println(e);
                }
            }
            for (i = 0; i < processorThreads.length; i++) {
                if (processorThreads[i].isAvailable()) {
                    processorThreads[i].setAvailable(false);
                    processorThreads[i].runTask(pc);
                    availableProcessors--;
                    break;
                }
            }
        }
        while (availableProcessors != numProcessorThreads) {
            try {
                wait();
            } catch (InterruptedException e) {
                System.out.println(e);
            }
        }
    }

    /**
     * This simply tells us that the processor is ready for work.
     */
    synchronized void notifyDone(ProcessorThread ep) {
        ep.setAvailable(true);
        availableProcessors++;
        notify();
    }

    /**
     * Add a component to be potentially processed
     */
    void addComponent(EntityComponent c) {
        c.setLive(true);
        if (c instanceof ProcessorComponent) {
            ProcessorComponent pc = (ProcessorComponent) c;
            pc.setEntityProcessController(this);
            synchronized (processorComponents) {
                processorComponents.add(pc);
            }
            if (pc.getArmingCondition() != null) {
                armProcessorComponent(pc.getArmingCondition());
            }
            pc.initialize();
        }
        if (c instanceof ProcessorCollectionComponent) {
            ProcessorCollectionComponent pcc = (ProcessorCollectionComponent) c;
            ProcessorComponent[] procs = pcc.getProcessors();
            for (int i = 0; i < procs.length; i++) {
                procs[i].setEntityProcessController(this);
                synchronized (processorComponents) {
                    processorComponents.add(procs[i]);
                }
                if (procs[i].getArmingCondition() != null) {
                    armProcessorComponent(procs[i].getArmingCondition());
                }
                procs[i].initialize();
            }
        }
    }

    /**
     * Add an entity to be potentially processed
     */
    void removeComponent(EntityComponent c) {
        if (c instanceof ProcessorComponent) {
            ProcessorComponent pc = (ProcessorComponent) c;
            if (pc.getArmingCondition() != null) {
                disarmProcessorComponent(pc.getArmingCondition());
            }
            pc.setEntityProcessController(null);
            synchronized (processorComponents) {
                processorComponents.remove(pc);
            }
        }
        if (c instanceof ProcessorCollectionComponent) {
            ProcessorCollectionComponent pcc = (ProcessorCollectionComponent) c;
            ProcessorComponent[] procs = pcc.getProcessors();
            for (int i = 0; i < procs.length; i++) {
                if (procs[i].getArmingCondition() != null) {
                    disarmProcessorComponent(procs[i].getArmingCondition());
                }
                procs[i].setEntityProcessController(null);
                synchronized (processorComponents) {
                    processorComponents.remove(procs[i]);
                }
            }
        }
        c.setLive(false);
    }

    /**
     * Arm a new frame condition
     */
    void armNewFrame(NewFrameCondition condition) {
        ProcessorComponent pc = condition.getProcessorComponent();
        synchronized (newFrameArmed) {
            if (!newFrameArmed.contains(pc)) {
                newFrameArmed.add(pc);
            }
        }
    }

    /**
     * Arm a timer expired condition
     */
    synchronized void armTimerExpired(TimerExpiredCondition condition) {
        ProcessorComponent pc = condition.getProcessorComponent();
        synchronized (timeElapseArmed) {
            if (!timeElapseArmed.contains(pc)) {
                condition.setStartTime(System.currentTimeMillis());
                timeElapseArmed.add(pc);
                notify();
            }
        }
    }

    /**
     * Arm an awt event condition
     */
    void armAwtEvent(AwtEventCondition condition) {
        boolean pendingTrigger = false;
        ProcessorComponent pc = condition.getProcessorComponent();
        synchronized (awtEventsArmed) {
            if (!awtEventsArmed.contains(pc)) {
                if (pc instanceof AWTEventProcessorComponent) {
                    AWTEventProcessorComponent apc = (AWTEventProcessorComponent) pc;
                    if (apc.eventsPending()) {
                        pendingTrigger = true;
                    }
                }
                awtEventsArmed.add(pc);
            }
        }
        if (pendingTrigger) {
            triggerAWTEvent();
        }
    }

    /**
     * Arm a post event condition
     */
    void armPostEvent(PostEventCondition condition) {
        boolean pendingTrigger = false;
        ProcessorComponent pc = condition.getProcessorComponent();
        synchronized (postEventArmed) {
            if (!postEventArmed.contains(pc)) {
                if (condition.eventsPending()) {
                    pendingTrigger = true;
                }
                postEventArmed.add(pc);
            }
            if (!postEventListeners.contains(condition)) {
                postEventListeners.add(condition);
            }
        }
        if (pendingTrigger) {
            triggerPostEvent();
        }
    }

    /**
     * Disarm a new frame condition
     */
    void disarmNewFrame(NewFrameCondition condition) {
        ProcessorComponent pc = condition.getProcessorComponent();
        synchronized (newFrameArmed) {
            newFrameArmed.remove(pc);
        }
    }

    /**
     * Disarm a timer expired condition
     */
    void disarmTimerExpired(TimerExpiredCondition condition) {
        ProcessorComponent pc = condition.getProcessorComponent();
        synchronized (timeElapseArmed) {
            timeElapseArmed.remove(pc);
        }
    }

    /**
     * Arm an awt event condition
     */
    void disarmAwtEvent(AwtEventCondition condition) {
        ProcessorComponent pc = condition.getProcessorComponent();
        synchronized (awtEventsArmed) {
            if (awtEventsArmed.contains(pc)) {
                if (pc instanceof AWTEventProcessorComponent) {
                    AWTEventProcessorComponent apc = (AWTEventProcessorComponent) pc;
                    if (apc.eventsPending()) {
                        apc.getEvents();
                    }
                }
                awtEventsArmed.remove(pc);
            }
        }
    }

    /**
     * Disarm a post event condition
     */
    void disarmPostEvent(PostEventCondition condition) {
        ProcessorComponent pc = condition.getProcessorComponent();
        synchronized (postEventArmed) {
            if (postEventArmed.contains(pc)) {
                postEventArmed.remove(pc);
            }
            if (postEventListeners.contains(condition)) {
                postEventListeners.remove(condition);
            }
            if (condition.eventsPending()) {
                condition.getTriggerEvents();
            }
        }
    }

    /**
     * Arm a condition
     */
    void armCondition(ProcessorArmingCondition armingCondition) {
        if (armingCondition instanceof NewFrameCondition) {
            armNewFrame((NewFrameCondition) armingCondition);
        }
        if (armingCondition instanceof TimerExpiredCondition) {
            armTimerExpired((TimerExpiredCondition) armingCondition);
        }
        if (armingCondition instanceof AwtEventCondition) {
            armAwtEvent((AwtEventCondition) armingCondition);
        }
        if (armingCondition instanceof PostEventCondition) {
            armPostEvent((PostEventCondition) armingCondition);
        }
    }

    /**
     * Disarm a condition
     */
    void disarmCondition(ProcessorArmingCondition armingCondition) {
        if (armingCondition instanceof NewFrameCondition) {
            disarmNewFrame((NewFrameCondition) armingCondition);
        }
        if (armingCondition instanceof TimerExpiredCondition) {
            disarmTimerExpired((TimerExpiredCondition) armingCondition);
        }
        if (armingCondition instanceof AwtEventCondition) {
            disarmAwtEvent((AwtEventCondition) armingCondition);
        }
        if (armingCondition instanceof PostEventCondition) {
            disarmPostEvent((PostEventCondition) armingCondition);
        }
    }

    /**
     * Add a processor component to the appropriate lists of possible arms
     */
    void armProcessorComponent(ProcessorArmingCondition armingCondition) {
        if (armingCondition instanceof ProcessorArmingCollection) {
            ProcessorArmingCollection pac = (ProcessorArmingCollection) armingCondition;
            for (int i = 0; i < pac.size(); i++) {
                armProcessorComponent(pac.get(i));
            }
        } else {
            armCondition(armingCondition);
        }
    }

    /**
     * Add a processor component to the appropriate lists of possible arms
     */
    void disarmProcessorComponent(ProcessorArmingCondition armingCondition) {
        if (armingCondition instanceof ProcessorArmingCollection) {
            ProcessorArmingCollection pac = (ProcessorArmingCollection) armingCondition;
            for (int i = 0; i < pac.size(); i++) {
                disarmProcessorComponent(pac.get(i));
            }
        } else {
            disarmCondition(armingCondition);
        }
    }

    /**
     * This method waits for processors to trigger
     */
    synchronized ProcessorComponent[] waitForProcessorsTriggered() {
        ProcessorComponent[] runList = new ProcessorComponent[0];
        long waitTime = checkTimerConditions();
        while (processorsTriggered.size() == 0) {
            waiting = true;
            try {
                wait(waitTime);
            } catch (InterruptedException e) {
                System.out.println(e);
            }
            waiting = false;
            waitTime = checkTimerConditions();
        }
        runList = (ProcessorComponent[]) processorsTriggered.toArray(runList);
        processorsTriggered.clear();
        return (runList);
    }

    /**
     * Checks to see if any timer conditions have expired.
     * If yes, then it triggers them.  If not, it returns
     * the number of miliseconds before the closest one will
     * trigger
     */
    long checkTimerConditions() {
        long waitTime = Long.MAX_VALUE;
        long elapsedTime = Long.MAX_VALUE;
        int index = 0;
        synchronized (timeElapseArmed) {
            if (timeElapseArmed.size() == 0) {
                return (0);
            }
            long currentTime = System.currentTimeMillis();
            int length = timeElapseArmed.size();
            for (int i = 0; i < length; i++) {
                ProcessorComponent pc = (ProcessorComponent) timeElapseArmed.get(index);
                TimerExpiredCondition tec = (TimerExpiredCondition) findCondition(TimerExpiredCondition.class, pc.getArmingCondition());
                if (pc.isEnabled()) {
                    elapsedTime = currentTime - tec.getStartTime();
                    long timeLeft = tec.getTime() - elapsedTime;
                    if (timeLeft < 0) {
                        pc.addTriggerCondition(tec);
                        addToTriggered(pc);
                        timeElapseArmed.remove(index);
                    } else {
                        if (timeLeft < waitTime) {
                            waitTime = timeLeft;
                        }
                        index++;
                    }
                } else {
                    index++;
                }
            }
            if (waitTime <= 0) {
                waitTime = 1;
            }
        }
        return (waitTime);
    }

    /**
     * This re-arms processors once they are done commiting
     */
    void armProcessors(ProcessorComponent[] runList) {
        for (int i = 0; i < runList.length; i++) {
            if (runList[i].getEntityProcessController() != null) {
                armProcessorComponent(runList[i].getArmingCondition());
            }
        }
    }

    /**
     * Find the specified condition in the (possibly) collection of conditions
     */
    ProcessorArmingCondition findCondition(Class conditionClass, ProcessorArmingCondition condition) {
        ProcessorArmingCondition newCondition = null;
        if (condition instanceof ProcessorArmingCollection) {
            ProcessorArmingCollection pac = (ProcessorArmingCollection) condition;
            for (int i = 0; i < pac.size(); i++) {
                newCondition = findCondition(conditionClass, pac.get(i));
                if (newCondition != null) {
                    return (newCondition);
                }
            }
        } else if (conditionClass.isInstance(condition)) {
            return (condition);
        }
        return (newCondition);
    }

    /**
     * This checks to see if this processor should be added to the triggered 
     * list.  If yes, then it returns true
     */
    boolean addToTriggered(ProcessorComponent pc) {
        if (pc.getRunInRenderer() || runSingleThreaded) {
            worldManager.addTriggeredProcessor(pc);
        } else {
            if (!processorsTriggered.contains(pc)) {
                processorsTriggered.add(pc);
                return (true);
            }
        }
        return (false);
    }

    /**
     * Trigger everyone waiting on a new frame
     */
    synchronized void triggerNewFrame() {
        int index = 0;
        ProcessorArmingCondition condition = null;
        ProcessorComponent pc = null;
        boolean anyTriggered = false;
        synchronized (newFrameArmed) {
            int length = newFrameArmed.size();
            for (int i = 0; i < length; i++) {
                pc = (ProcessorComponent) newFrameArmed.get(index);
                if (pc.isEnabled()) {
                    condition = findCondition(NewFrameCondition.class, pc.getArmingCondition());
                    pc.addTriggerCondition(condition);
                    if (addToTriggered(pc) && !anyTriggered) {
                        anyTriggered = true;
                    }
                    newFrameArmed.remove(index);
                } else {
                    index++;
                }
            }
            if (anyTriggered && waiting) {
                notify();
            }
        }
    }

    /**
     * Distribute a post event
     */
    void distributePostEvent(long event) {
        synchronized (postEventArmed) {
            for (int i = 0; i < postEventListeners.size(); i++) {
                PostEventCondition cond = (PostEventCondition) postEventListeners.get(i);
                if (cond.triggers(event)) {
                    cond.addTriggerEvent(event);
                }
            }
        }
    }

    /**
     * Trigger a post event
     */
    synchronized void triggerPostEvent() {
        int index = 0;
        PostEventCondition condition = null;
        ProcessorComponent pc = null;
        boolean anyTriggered = false;
        synchronized (postEventArmed) {
            int length = postEventArmed.size();
            for (int i = 0; i < length; i++) {
                pc = (ProcessorComponent) postEventArmed.get(index);
                condition = (PostEventCondition) findCondition(PostEventCondition.class, pc.getArmingCondition());
                if (pc.isEnabled() && condition != null && condition.eventsPending()) {
                    condition.freezeEvents();
                    pc.addTriggerCondition(condition);
                    if (addToTriggered(pc) && !anyTriggered) {
                        anyTriggered = true;
                    }
                    postEventArmed.remove(index);
                } else {
                    index++;
                }
            }
            if (anyTriggered && waiting) {
                notify();
            }
        }
    }

    /**
     * Trigger everyone waiting on a new frame
     */
    synchronized void triggerAWTEvent() {
        int index = 0;
        ProcessorArmingCondition condition = null;
        AWTEventProcessorComponent apc = null;
        boolean anyTriggered = false;
        synchronized (awtEventsArmed) {
            int length = awtEventsArmed.size();
            for (int i = 0; i < length; i++) {
                apc = (AWTEventProcessorComponent) awtEventsArmed.get(index);
                if (apc.isEnabled() && apc.eventsPending()) {
                    condition = findCondition(NewFrameCondition.class, apc.getArmingCondition());
                    ((ProcessorComponent) apc).addTriggerCondition(condition);
                    if (addToTriggered(apc) && !anyTriggered) {
                        anyTriggered = true;
                    }
                    awtEventsArmed.remove(index);
                } else {
                    index++;
                }
            }
            if (anyTriggered && waiting) {
                notify();
            }
        }
    }
}
