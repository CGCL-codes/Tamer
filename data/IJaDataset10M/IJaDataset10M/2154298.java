package net.assimilator.tools.deploymentdirectory.watcher;

import net.assimilator.tools.deploymentdirectory.commands.CopyOarFileCommand;
import net.assimilator.tools.deploymentdirectory.commands.RemoveOarFileNameCommand;
import net.assimilator.tools.deploymentdirectory.events.FileChangeEvent;
import net.assimilator.tools.deploymentdirectory.events.ProvisionMonitorChangeEvent;
import net.assimilator.utility.eventbus.EventService;
import net.assimilator.utility.eventbus.event.Event;
import net.assimilator.utility.microservices.EventCapableService;
import net.assimilator.utility.task.AbstractTaskObject;
import java.io.File;
import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.HashMap;
import java.util.logging.Logger;

/**
 * This is a class used to watch a file or directory.
 *
 * @author Larry Mitchell
 * @version $Id: DirectoryWatcherTaskImpl.java 344 2007-10-09 23:11:43Z khartig $
 */
public class DirectoryWatcherTaskImpl extends AbstractTaskObject implements DirectoryWatcherTask, EventCapableService {

    /**
     * minimun watch period -- less than this and the vfs will trash too much
     */
    public static final int MINIMUN_WATCH_PERIOD = 500;

    /**
     * default watch period -- 5 seconds between runs
     */
    public static final int DEFAULT_WATCH_PERIOD = 1000;

    /**
     * the logger for this class
     */
    private static final Logger logger = Logger.getLogger("net.assimilator.tools.deploymentdirectory.watcher");

    /**
     * directory to watch
     */
    private File directoryToWatch;

    /**
     * the watch period to scan directories for changes set a default time of 15 seconds
     */
    private int watchPeriod = MINIMUN_WATCH_PERIOD;

    /**
     * hash map of oars located and managed
     */
    private List<String> oarsFound = new ArrayList<String>();

    /**
     * hash map of oars seen and waiting for stabilization
     */
    private Map<String, FileInfo> oarsSeen = new HashMap<String, FileInfo>();

    /**
     * synchronization lock for threading
     */
    private final Object taskLock = new Object();

    /**
     * the event service
     */
    private EventService eventService;

    /**
     * the watcher is ready to run only if a provision monitor is available,
     * otherwise it should go quiet
     */
    private boolean readyToRun = false;

    /**
     * the file state of files we observe on scanning
     */
    public enum FileStateType {

        /**
         * this means a file was seen on initial scanning
         */
        FILE_SEEN, /**
         * a file that was seen is now considered stable and we can depend on it remaining unchanged for reading
         */
        FILE_STABLE, /**
         * a previously tracked file is marked as removed from the directory we are scanning
         */
        FILE_REMOVED
    }

    /**
     * ctor for the file watcher
     * <p/>
     * use this cotr if you want to watch a file
     *
     * @param dirToWatch file to watch
     */
    public DirectoryWatcherTaskImpl(String dirToWatch) {
        this.directoryToWatch = new File(dirToWatch);
    }

    /**
     * ctor for the directory watcher
     * <p/>
     * use this ctor if you want to watch a file
     *
     * @param dirToWatch file to watch
     * @param period     the watch period to manage the file update
     * @throws WatcherParmException if the wather dir is null
     */
    public DirectoryWatcherTaskImpl(String dirToWatch, int period) throws WatcherParmException {
        this.watchPeriod = period;
        if (dirToWatch == null) {
            throw new WatcherParmException("watch directory cannot be null");
        }
        this.directoryToWatch = new File(dirToWatch);
    }

    /**
     * do failure recovery if possible
     *
     * @param t the generalized exception we caught
     */
    protected void doFailRecovery(Throwable t) {
        if (taskFailPolicy != null) {
            if (taskFailPolicy.performFailAnalysis(t, this)) {
                runFlag = false;
                logger.warning(MessageFormat.format("Task: {0} is terminating due to {1}", getName(), t));
            }
        }
    }

    /**
     * the event service which is used for asynchronous comms
     * <p/>
     * We also do registration/subscription for events here
     *
     * @param service ref to the even service
     */
    public void setEventService(EventService service) throws Exception {
        this.eventService = service;
        if (eventService == null) {
            throw new WatcherParmException("event service setting cannot be null");
        }
        eventService.addListener(this, ProvisionMonitorChangeEvent.class);
    }

    /**
     * get the ready to run state
     *
     * @return ready to run if everythign this there
     */
    public boolean getReadyToRun() {
        return readyToRun;
    }

    /**
     * get the name of the directory we are watching
     *
     * @return directory name
     */
    public String getDirectoryToWatch() {
        if (directoryToWatch == null) {
            return null;
        }
        return directoryToWatch.getPath();
    }

    /**
     * add an oar file to the deployment directory
     * <p/>
     * This will start the deployment of the designated oar file
     *
     * @param pathToOarFile the oar file
     */
    public void addOarFile(String pathToOarFile) throws Exception {
        CopyOarFileCommand copier = new CopyOarFileCommand(getDirectoryToWatch(), pathToOarFile);
        copier.execute();
    }

    /**
     * remove the specified oar file
     *
     * @param oarFileName the oar file name
     */
    public void removeOarFile(String oarFileName) throws Exception {
        RemoveOarFileNameCommand removeOar = new RemoveOarFileNameCommand(getDirectoryToWatch(), oarFileName);
        removeOar.execute();
    }

    /**
     * set the watch period for scanning the directory
     *
     * @param period the watch period for scanning the directory
     */
    public void setWatchPeriod(int period) {
        watchPeriod = period;
    }

    /**
     * get the watch period for the directory watcher
     *
     * @return the watch period
     */
    public int getWatchPeriod() {
        return watchPeriod;
    }

    /**
     * run the task
     * <p/>
     * When I see a a new oar file then create a deployment record
     * attach the record to a deployer and start the deployer to manage deployment and undeployment
     * then we add the deployment record to the map
     *
     * @throws Throwable if the task failed for some reason
     */
    public void runTask() throws Throwable {
        logger.finest("before lock");
        synchronized (taskLock) {
            logger.finest("loop");
            if (readyToRun) {
                logger.finest("scanning directory.");
                scanDirectory();
            } else {
                logger.finer("we have to pass since a provision monitor has not been found yet.");
            }
            taskLock.wait(watchPeriod);
        }
    }

    /**
     * scan the deployment directory for new files and deleted files
     */
    private void scanDirectory() {
        Map<String, FileInfo> originalList = new HashMap<String, FileInfo>(oarsSeen);
        File[] fileList = directoryToWatch.listFiles(new OarFileFilter());
        logger.finer("files found are " + fileList.length);
        logger.finer("before: originalList count is=" + originalList.size());
        for (File file : fileList) {
            String oarFileName = file.getName();
            if (!oarsFound.contains(oarFileName)) {
                if (checkForFileStability(file)) {
                    oarsFound.add(oarFileName);
                    sendFileChangeEvent(oarFileName, FileChangeEvent.FileChangeType.NEW);
                    logger.fine("\n>>>found and added a file:" + oarFileName + "\n");
                }
            }
            originalList.remove(oarFileName);
        }
        logger.finer("after: originalList count is=" + originalList.size());
        for (String deadName : originalList.keySet()) {
            logger.fine("\n>>>removed a dead file:" + deadName + "\n");
            if (oarsFound.contains(deadName)) {
                oarsFound.remove(deadName);
                sendFileChangeEvent(deadName, FileChangeEvent.FileChangeType.REMOVED);
            }
            oarsSeen.remove(deadName);
        }
        logger.finer("finished scan -----------------------------------");
    }

    private boolean checkForFileStability(File file) {
        if (oarsSeen.containsKey(file.getName())) {
            FileInfo fileInfo = oarsSeen.get(file.getName());
            if (fileInfo.checkForStability(file.length())) {
                return true;
            }
        } else {
            FileInfo fileInfo = new FileInfo(file);
            oarsSeen.put(fileInfo.getFileName(), fileInfo);
        }
        return false;
    }

    /**
     * send a file change event
     *
     * @param fileName      the file name
     * @param fileCondition the file condition
     */
    private void sendFileChangeEvent(String fileName, FileChangeEvent.FileChangeType fileCondition) {
        if (eventService != null) {
            FileChangeEvent fileChangeEvent = new FileChangeEvent(fileName, fileCondition);
            eventService.send(fileChangeEvent, getServiceId());
        }
    }

    /**
     * we extract the termination conditions so that these can be abstracted from the run loop and specified
     *
     * @return false means stop the run
     */
    public boolean terminationCheck() {
        return runFlag;
    }

    /**
     * retire the task
     */
    public void cancelTask() {
        synchronized (taskLock) {
            readyToRun = false;
            runFlag = false;
            if (eventService != null) {
                eventService.removeListener(this);
            }
        }
    }

    /**
     * get the task name
     *
     * @return name of the task
     */
    public String getName() {
        return "DirectoryWatcher";
    }

    /**
     * get the list of oars currently managed
     *
     * @return a list of ar files
     */
    public String[] getOarList() {
        return oarsFound.toArray(new String[oarsFound.size()]);
    }

    /**
     * this is a general gossip callback
     * for this class it is mostly to monitor provision disco events
     *
     * @param event the event object we pass back
     *              the Event here is a base object
     */
    public void notify(Event event) {
        logger.fine("got event:" + event);
        Object eventObj = event.getPayload();
        if (eventObj instanceof ProvisionMonitorChangeEvent) {
            ProvisionMonitorChangeEvent pmEvent = (ProvisionMonitorChangeEvent) eventObj;
            synchronized (taskLock) {
                readyToRun = pmEvent.getPMChangeType() == ProvisionMonitorChangeEvent.ProvisionMonitorChangeType.FOUND;
                logger.fine("got PM event, ready to run is:" + readyToRun);
            }
        }
    }
}
