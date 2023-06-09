package org.perfmon4j;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.Timer;
import org.perfmon4j.Appender.AppenderID;
import org.perfmon4j.util.FailSafeTimerTask;
import org.perfmon4j.util.GlobalClassLoader;
import org.perfmon4j.util.Logger;
import org.perfmon4j.util.LoggerFactory;
import org.perfmon4j.util.MiscHelper;

public class PerfMon {

    private static final Logger logger = LoggerFactory.initLogger(PerfMon.class);

    public static final long NOT_SET = -1;

    static boolean configured = false;

    public static final int MAX_APPENDERS_PER_MONITOR = Integer.getInteger(PerfMon.class.getName() + ".MAX_APPENDERS_PER_MONITOR", 10).intValue();

    public static final int MAX_EXTERNAL_ELEMENTS_PER_MONITOR = Integer.getInteger(PerfMon.class.getName() + ".MAX_EXTERNAL_ELEMENTS_PER_MONITOR", 10).intValue();

    public static final int MAX_ALLOWED_INTERNAL_THREAD_TRACE_ELEMENTS = Integer.getInteger(PerfMon.class.getName() + ".MAX_ALLOWED_INTERNAL_THREAD_TRACE_ELEMENTS", 100000).intValue();

    public static final int MAX_ALLOWED_EXTERNAL_THREAD_TRACE_ELEMENTS = Integer.getInteger(PerfMon.class.getName() + ".MAX_ALLOWED_EXTERNAL_THREAD_TRACE_ELEMENTS", 2500).intValue();

    public static final String ROOT_MONITOR_NAME;

    private final String name;

    public static final String APPENDER_PATTERN_NA = "";

    public static final String APPENDER_PATTERN_PARENT_ONLY = "./";

    public static final String APPENDER_PATTERN_PARENT_AND_CHILDREN_ONLY = "./*";

    public static final String APPENDER_PATTERN_CHILDREN_ONLY = "/*";

    public static final String APPENDER_PATTERN_ALL_DESCENDENTS = "/**";

    public static final String APPENDER_PATTERN_PARENT_AND_ALL_DESCENDENTS = "./**";

    private final Object startStopLockToken = new Object();

    /**
     * priorityTimer should be used exclusivly to push PerfMonData onto
     * the appenders... All timerTasks should be fast...
     */
    private static final Timer priorityTimer = new Timer("PerfMon.priorityTimer", true);

    public static final Timer utilityTimer = new Timer("PerfMon.utilityTimer", true);

    private static long nextMonitorID;

    private static final PerfMon rootMonitor;

    private static ClassLoader classLoader = GlobalClassLoader.getClassLoader();

    static {
        nextMonitorID = 0;
        ROOT_MONITOR_NAME = "<ROOT>";
        rootMonitor = new PerfMon(null, ROOT_MONITOR_NAME);
    }

    private final List<Appender> appenderList = Collections.synchronizedList(new ArrayList<Appender>());

    private final Map<Appender, String> appenderPatternMap = Collections.synchronizedMap(new HashMap<Appender, String>());

    private final Set<Appender> appendersAssociatedWithChildren = Collections.synchronizedSet(new HashSet<Appender>());

    private final Object dataArrayInsertLockToken = new Object();

    private final PushAppenderDataTask dataArray[] = new PushAppenderDataTask[MAX_APPENDERS_PER_MONITOR];

    /**
     * External elements are monitors that will be polled by external (outside of the JVM) monitoring
     * tools.
     */
    private int activeExternalElements = 0;

    private final Object externalElementArrayLockToken = new Object();

    private final IntervalData externalElementArray[] = new IntervalData[MAX_EXTERNAL_ELEMENTS_PER_MONITOR];

    /**
     * Make sure to synchronize on the mapMonitorLockToken when you access
     * the mapMonitors Map...
     */
    private static Map<String, PerfMon> mapMonitors = new HashMap<String, PerfMon>();

    private static final Object mapMonitorLockToken = new Object();

    private static final Set<String> monitorsWithThreadTraceConfigAttached = Collections.synchronizedSet(new HashSet<String>());

    private final Long monitorID;

    private Long startTime = null;

    private int totalHits = 0;

    private int activeThreadCount = 0;

    private int totalCompletions = 0;

    /** SQL/JDBC profiling START **/
    private long maxSQLDuration = 0;

    private long timeMaxSQLDurationSet = NOT_SET;

    private long minSQLDuration = NOT_SET;

    private long timeMinSQLDurationSet = NOT_SET;

    private long totalSQLDuration = 0;

    private long sumOfSQLSquares = 0;

    /** SQL/JDBC profiling END **/
    private long maxDuration = 0;

    private long timeMaxDurationSet = NOT_SET;

    long minDuration = NOT_SET;

    private long timeMinDurationSet = NOT_SET;

    private long totalDuration = 0;

    private long sumOfSquares = 0;

    private MaxThroughput maxThroughputPerMinute = null;

    private final PerfMon parent;

    private int maxActiveThreadCount = 0;

    private long timeMaxActiveThreadCountSet = NOT_SET;

    private PerfMonTimer cachedPerfMonTimer = null;

    private Set<PerfMon> childMonitors = Collections.synchronizedSet(new HashSet<PerfMon>());

    private ThreadTraceConfig internalThreadTraceConfig = null;

    private final ExternalThreadTraceConfig.Queue externalThreadTraceQueue = new ExternalThreadTraceConfig.Queue();

    private static ThreadLocal<Map<Long, ReferenceCount>> activeMonitors = new ThreadLocal<Map<Long, ReferenceCount>>() {

        protected synchronized Map<Long, ReferenceCount> initialValue() {
            return new HashMap<Long, ReferenceCount>();
        }
    };

    private PerfMon(PerfMon parent, String name) {
        this.name = parent == null ? ROOT_MONITOR_NAME : name;
        monitorID = new Long(++nextMonitorID);
        this.parent = parent;
        if (parent != null) {
            parent.childMonitors.add(this);
        }
        if (parent != null) {
            Iterator<Appender> itr = parent.appenderList.iterator();
            while (itr.hasNext()) {
                Appender appender = itr.next();
                String childPattern = parentToChildConversion(parent.appenderPatternMap.get(appender));
                if (!APPENDER_PATTERN_NA.equals(childPattern)) {
                    addAppender(appender, true, childPattern);
                }
            }
        }
    }

    static Timer getUtilityTimer() {
        return utilityTimer;
    }

    static Timer getPriorityTimer() {
        return priorityTimer;
    }

    public static int getNumMonitors() {
        int result = 0;
        synchronized (mapMonitorLockToken) {
            result = mapMonitors.size();
        }
        return result;
    }

    public static List<String> getMonitorNames() {
        List<String> result = new ArrayList<String>(getNumMonitors());
        synchronized (mapMonitorLockToken) {
            result.addAll(mapMonitors.keySet());
        }
        return result;
    }

    private static boolean isAppenderInUseByAnyMonitor(Appender appender) {
        boolean result = false;
        synchronized (mapMonitorLockToken) {
            Iterator<PerfMon> itr = mapMonitors.values().iterator();
            while (itr.hasNext() && !result) {
                result = itr.next().appenderList.contains(appender);
            }
        }
        return result;
    }

    /**
     * Package level for testing....
     */
    boolean hasAppenderWithTask(String className, long interval) {
        boolean result = false;
        Appender.AppenderID id = new Appender.AppenderID(className, interval);
        for (int i = 0; (i < dataArray.length && !result); i++) {
            PushAppenderDataTask task = dataArray[i];
            if (task != null) {
                result = task.appender.getMyAppenderID().equals(id);
            }
        }
        return result;
    }

    private PerfMon[] getChildMonitors() {
        return childMonitors.toArray(new PerfMon[] {});
    }

    public static PerfMon getRootMonitor() {
        return rootMonitor;
    }

    public static PerfMon getMonitorNoCreate_TESTONLY(String key) {
        synchronized (mapMonitorLockToken) {
            return mapMonitors.get(key);
        }
    }

    public static PerfMon getMonitor(String key) {
        return getMonitor(key, false);
    }

    /**
     * The isDynamicPath is used to limit the number of monitors
     * that are created and maintiained in memory.
     * 
     * For most purposed you want to call this with a value of false
     * however if you are calling with a "dynamically" generated key
     * value you would only want the monitor created an appender exists.
     */
    public static PerfMon getMonitor(String key, boolean isDynamicPath) {
        PerfMon result = null;
        synchronized (mapMonitorLockToken) {
            if (ROOT_MONITOR_NAME.equals(key)) {
                result = rootMonitor;
            } else {
                result = mapMonitors.get(key);
            }
            if (result == null) {
                PerfMon parent = null;
                String[] h = parseMonitorHirearchy(key);
                if (h.length > 1) {
                    parent = getMonitor(h[h.length - 2], isDynamicPath);
                } else {
                    parent = rootMonitor;
                }
                if (!isDynamicPath || parent.shouldChildBeDynamicallyCreated()) {
                    result = new PerfMon(parent, key);
                    mapMonitors.put(key, result);
                } else {
                    result = parent;
                }
            }
        }
        return result;
    }

    /**
     * Package level...
     * Applications should use PerfMonTimer.start() to start a timer
     * using this monitor!
     */
    void start(long systemTime) {
        ReferenceCount count = getThreadLocalReferenceCount();
        if (count.inc(systemTime) == 1) {
            if (externalThreadTraceQueue.hasPendingElements()) {
                ExternalThreadTraceConfig externalConfig = externalThreadTraceQueue.assignToThread();
                if (externalConfig != null) {
                    count.hasExternalThreadTrace = true;
                    ThreadTraceMonitor.ThreadTracesOnStack tOnStack = ThreadTraceMonitor.getExternalThreadTracesOnStack();
                    tOnStack.start(getName(), externalConfig.getMaxDepth(), externalConfig.getMinDurationToCapture(), systemTime);
                    tOnStack.setExternalConfig(externalConfig);
                }
            }
            ThreadTraceConfig internalConfig = internalThreadTraceConfig;
            if (internalConfig != null && internalConfig.shouldTrace()) {
                count.hasInternalThreadTrace = true;
                ThreadTraceMonitor.ThreadTracesOnStack tOnStack = ThreadTraceMonitor.getInternalThreadTracesOnStack();
                tOnStack.start(getName(), internalConfig.getMaxDepth(), internalConfig.getMinDurationToCapture(), systemTime);
            }
            synchronized (startStopLockToken) {
                activeThreadCount++;
                if (isActive()) {
                    totalHits++;
                    if (activeThreadCount >= maxActiveThreadCount) {
                        maxActiveThreadCount = activeThreadCount;
                        timeMaxActiveThreadCountSet = systemTime;
                    }
                    for (int i = 0; i < dataArray.length; i++) {
                        PushAppenderDataTask data = dataArray[i];
                        if (data != null) {
                            data.perfMonData.start(activeThreadCount, systemTime);
                        }
                    }
                    if (hasExternalElement()) {
                        for (int i = 0; i < externalElementArray.length; i++) {
                            IntervalData data = externalElementArray[i];
                            if (data != null) {
                                data.start(activeThreadCount, systemTime);
                            }
                        }
                    }
                }
            }
        }
    }

    void stop(long systemTime, boolean abort) {
        ReferenceCount count = getThreadLocalReferenceCount();
        if (count.dec() == 0) {
            if (count.hasExternalThreadTrace) {
                ThreadTraceMonitor.ThreadTracesOnStack tOnStack = ThreadTraceMonitor.getExternalThreadTracesOnStack();
                ThreadTraceData data = tOnStack.stop(getName());
                count.hasExternalThreadTrace = false;
                ExternalThreadTraceConfig externalConfig = tOnStack.popExternalConfig();
                if (data != null && externalConfig != null) {
                    externalConfig.outputData(data);
                    if (!externalThreadTraceQueue.hasPendingElements()) {
                        this.clearCachedPerfMonTimer();
                    }
                }
            }
            if (count.hasInternalThreadTrace) {
                ThreadTraceMonitor.ThreadTracesOnStack tOnStack = ThreadTraceMonitor.getInternalThreadTracesOnStack();
                ThreadTraceData data = tOnStack.stop(getName());
                count.hasInternalThreadTrace = false;
                if (data != null && internalThreadTraceConfig != null) {
                    AppenderID appenders[] = internalThreadTraceConfig.getAppenders();
                    for (int i = 0; i < appenders.length; i++) {
                        Appender appender = Appender.getAppender(appenders[i]);
                        if (appender != null) {
                            appender.outputData(data);
                        }
                    }
                }
            }
            synchronized (startStopLockToken) {
                long eventStartTime = count.getStartTime();
                activeThreadCount--;
                final boolean active = isActive() && (startTime.longValue() <= eventStartTime);
                final boolean externalElement = hasExternalElement();
                final boolean monitorEvent = (active || externalElement) && !abort;
                final boolean sqlTimeEnabled = monitorEvent && SQLTime.isEnabled();
                if (monitorEvent) {
                    long sqlDuration = 0;
                    long sqlDurationSquared = 0;
                    long duration = systemTime - eventStartTime;
                    if (duration < 0) {
                        if (logger.isInfoEnabled()) {
                            logger.logInfo("System currentime millis returned a negative time increment: " + duration);
                        }
                        duration = 0;
                    }
                    long durationSquared = (duration * duration);
                    if (sqlTimeEnabled) {
                        sqlDuration = SQLTime.getSQLTime() - count.getSQLStartMillis();
                        if (sqlDuration < 0) {
                            sqlDuration = 0;
                        }
                        sqlDurationSquared = (sqlDuration * sqlDuration);
                    }
                    if (active) {
                        if (sqlTimeEnabled) {
                            this.totalSQLDuration += sqlDuration;
                            this.sumOfSQLSquares += sqlDurationSquared;
                            if (sqlDuration >= this.maxSQLDuration) {
                                this.timeMaxSQLDurationSet = systemTime;
                                this.maxSQLDuration = sqlDuration;
                            }
                            if ((sqlDuration <= this.minSQLDuration) || (this.minSQLDuration == NOT_SET)) {
                                this.minSQLDuration = sqlDuration;
                                this.timeMinSQLDurationSet = systemTime;
                            }
                        }
                        totalCompletions++;
                        totalDuration += duration;
                        sumOfSquares += durationSquared;
                        if (duration >= maxDuration) {
                            timeMaxDurationSet = systemTime;
                            maxDuration = duration;
                        }
                        if ((duration <= minDuration) || (minDuration == NOT_SET)) {
                            minDuration = duration;
                            timeMinDurationSet = systemTime;
                        }
                        for (int i = 0; i < dataArray.length; i++) {
                            PushAppenderDataTask data = dataArray[i];
                            if (data != null) {
                                data.perfMonData.stop(duration, durationSquared, systemTime, sqlDuration, sqlDurationSquared);
                            }
                        }
                        if (externalElement) {
                            for (int i = 0; i < externalElementArray.length; i++) {
                                IntervalData data = externalElementArray[i];
                                if (data != null) {
                                    data.stop(duration, durationSquared, systemTime, sqlDuration, sqlDurationSquared);
                                }
                            }
                        }
                    }
                }
            }
        }
    }

    public int getTotalHits() {
        return totalHits;
    }

    public int getActiveThreadCount() {
        return activeThreadCount;
    }

    public String getName() {
        return name;
    }

    private ReferenceCount getThreadLocalReferenceCount() {
        Map<Long, ReferenceCount> map = activeMonitors.get();
        ReferenceCount count = map.get(monitorID);
        if (count == null) {
            count = new ReferenceCount();
            map.put(monitorID, count);
        }
        return count;
    }

    private static String removeTrailingPeriods(String val) {
        while (val.endsWith(".") && val.length() > 1) {
            val = val.substring(0, val.length() - 1);
        }
        return val;
    }

    protected static String[] parseMonitorHirearchy(String monitor) {
        String[] result = new String[] {};
        if (monitor != null && !monitor.equals("")) {
            monitor = removeTrailingPeriods(monitor);
            List<String> x = new ArrayList<String>();
            int offset = 0;
            while (true) {
                offset = monitor.indexOf('.', offset);
                if (offset > 0) {
                    x.add(monitor.substring(0, offset));
                    offset++;
                } else {
                    break;
                }
            }
            x.add(monitor);
            result = x.toArray(result);
        }
        return result;
    }

    public boolean isRootMonitor() {
        return parent == null;
    }

    private static class ReferenceCount {

        private int refCount = 0;

        private long startTime;

        private long sqlStartMillis = 0;

        boolean hasInternalThreadTrace = false;

        boolean hasExternalThreadTrace = false;

        /**
         * @return The updated (incremented value of refCount)
         */
        private int inc(long startTime) {
            if (refCount == 0) {
                this.startTime = startTime;
                this.sqlStartMillis = SQLTime.getSQLTime();
            }
            return ++refCount;
        }

        /**
         * @return The updated (decremented value of refCount)
         */
        private int dec() {
            return --refCount;
        }

        private long getStartTime() {
            return startTime;
        }

        private long getSQLStartMillis() {
            return sqlStartMillis;
        }
    }

    public int getTotalCompletions() {
        return totalCompletions;
    }

    public long getAverageDuration() {
        synchronized (startStopLockToken) {
            return totalCompletions > 0 ? totalDuration / totalCompletions : 0;
        }
    }

    public double getStdDeviation() {
        synchronized (startStopLockToken) {
            return MiscHelper.calcStdDeviation(totalCompletions, totalDuration, sumOfSquares);
        }
    }

    public long getMaxDuration() {
        return maxDuration;
    }

    public long getMinDuration() {
        return Math.max(minDuration, 0);
    }

    long getMinDuration_NO_FIXUP() {
        return minDuration;
    }

    public long getTotalDuration() {
        return totalDuration;
    }

    public int getMaxActiveThreadCount() {
        return maxActiveThreadCount;
    }

    int getNumAppenders() {
        return appenderList.size();
    }

    int getNumPerfMonTasks() {
        int count = 0;
        for (int i = 0; i < dataArray.length; i++) {
            if (dataArray[i] != null) {
                count++;
            }
        }
        return count;
    }

    private int getNumExternalAppenderTasks() {
        int count = 0;
        for (int i = 0; i < externalElementArray.length; i++) {
            if (externalElementArray[i] != null) {
                count++;
            }
        }
        return count;
    }

    public void addAppender(Appender.AppenderID id) throws InvalidConfigException {
        addAppender(Appender.getOrCreateAppender(id));
    }

    public void addAppender(Appender.AppenderID id, String appenderPattern) throws InvalidConfigException {
        addAppender(Appender.getOrCreateAppender(id), true, appenderPattern);
    }

    private void addAppender(Appender appender) {
        addAppender(appender, true);
    }

    private void addAppender(Appender appender, boolean cascadeToChildren) {
        addAppender(appender, cascadeToChildren, APPENDER_PATTERN_PARENT_AND_ALL_DESCENDENTS);
    }

    private boolean shouldChildBeDynamicallyCreated() {
        return !appendersAssociatedWithChildren.isEmpty();
    }

    private void addAppender(Appender appender, boolean cascadeToChildren, String appenderPattern) {
        boolean hasAppender = appenderList.contains(appender);
        if (hasAppender) {
            String currentPattern = appenderPatternMap.get(appender);
            if (!appenderPattern.equals(currentPattern)) {
                removeAppender(appender, false, APPENDER_PATTERN_NA, false);
                hasAppender = false;
            }
        }
        if (!hasAppender) {
            if (logger.isDebugEnabled()) {
                logger.logDebug("Adding appender " + appender + " to monitor " + this + " with appenderPattern \"" + appenderPattern + "\"");
            }
            appenderList.add(appender);
            appenderPatternMap.put(appender, appenderPattern);
            if (appenderPattern != null && appenderPattern.endsWith("*")) {
                appendersAssociatedWithChildren.add(appender);
            }
            int index = -1;
            if (!isRootMonitor() && !APPENDER_PATTERN_ALL_DESCENDENTS.equals(appenderPattern) && !APPENDER_PATTERN_CHILDREN_ONLY.equals(appenderPattern)) {
                synchronized (dataArrayInsertLockToken) {
                    for (int i = 0; i < dataArray.length; i++) {
                        if (dataArray[i] == null) {
                            index = i;
                            break;
                        }
                    }
                    if (index > -1) {
                        makeActive();
                        FailSafeTimerTask task = new PushAppenderDataTask(this, appender, index);
                        if (logger.isDebugEnabled()) {
                            logger.logDebug("Scheduling task: " + task);
                        }
                        priorityTimer.schedule(task, appender.getIntervalMillis());
                    } else {
                        logger.logError("Unable to add appender to monitor: " + this + " - Max appenders exceeded");
                    }
                }
            }
        }
        String childPattern = parentToChildConversion(appenderPattern);
        if (cascadeToChildren && !childPattern.equals(APPENDER_PATTERN_NA)) {
            PerfMon children[] = getChildMonitors();
            for (int i = 0; i < children.length; i++) {
                PerfMon child = children[i];
                child.addAppender(appender, cascadeToChildren, childPattern);
            }
        }
        clearCachedPerfMonTimer();
    }

    public void removeAppender(Appender.AppenderID id) throws InvalidConfigException {
        removeAppender(id, APPENDER_PATTERN_PARENT_AND_ALL_DESCENDENTS);
    }

    public void removeAppender(Appender.AppenderID id, String appenderPattern) throws InvalidConfigException {
        removeAppender(Appender.getOrCreateAppender(id), true, appenderPattern, true);
    }

    private void removeAppender(Appender appender, boolean cascadeToChildren, String appenderPattern, boolean deinitUnusedAppenders) {
        appendersAssociatedWithChildren.remove(appender);
        if (cascadeToChildren && APPENDER_PATTERN_PARENT_ONLY.equals(appenderPattern)) {
            cascadeToChildren = false;
        }
        if (cascadeToChildren) {
            PerfMon children[] = getChildMonitors();
            for (int i = 0; i < children.length; i++) {
                PerfMon child = children[i];
                child.removeAppender(appender, true, parentToChildConversion(appenderPattern), deinitUnusedAppenders);
            }
        }
        final boolean INCLUDE_PARENT = !APPENDER_PATTERN_CHILDREN_ONLY.equals(appenderPattern) && !APPENDER_PATTERN_ALL_DESCENDENTS.equals(appenderPattern);
        if (INCLUDE_PARENT) {
            if (appenderList.contains(appender)) {
                if (logger.isDebugEnabled()) {
                    logger.logDebug("Removing appender " + appender + " from monitor " + this);
                }
                synchronized (dataArrayInsertLockToken) {
                    appenderPatternMap.remove(appender);
                    appenderList.remove(appender);
                    if (deinitUnusedAppenders && !isAppenderInUseByAnyMonitor(appender)) {
                        appender.deInit();
                    }
                    for (int i = 0; i < dataArray.length; i++) {
                        PushAppenderDataTask task = dataArray[i];
                        if (task != null && task.appender.equals(appender)) {
                            dataArray[i] = null;
                            if (!isRootMonitor()) {
                                makeInactiveIfNoAppenders();
                                if (logger.isDebugEnabled()) {
                                    logger.logDebug("Canceling appender task: " + task);
                                }
                                task.cancel();
                            }
                        }
                    }
                }
            }
            clearCachedPerfMonTimer();
        }
    }

    private boolean isInChildIgnoreList(PerfMon monitor, String[] childIgnoreList) {
        boolean result = false;
        if (childIgnoreList != null) {
            for (int i = 0; i < childIgnoreList.length; i++) {
                if (childIgnoreList[i].equalsIgnoreCase(monitor.getName())) {
                    result = true;
                    break;
                }
            }
        }
        return result;
    }

    /**
     * 
     * @param data
     * @return true - if there was an open position to add the data element.
     * 		   false - if there the element could not be added. 
     */
    public boolean addExternalElement(IntervalData data) {
        boolean added = false;
        synchronized (externalElementArrayLockToken) {
            for (int i = 0; i < externalElementArray.length && !added; i++) {
                if (externalElementArray[i] == null) {
                    externalElementArray[i] = data;
                    added = true;
                    activeExternalElements++;
                }
            }
            if (added) {
                if (!isActive()) {
                    makeActive();
                }
                clearCachedPerfMonTimer();
            }
        }
        return added;
    }

    /**
     * 
     * @param data
     * @return true - if the external element was found in the array and removed
     * 			false - if the element could not be found.
     */
    public boolean removeExternalElement(IntervalData data) {
        boolean removed = false;
        synchronized (externalElementArrayLockToken) {
            for (int i = 0; i < externalElementArray.length && !removed; i++) {
                if (externalElementArray[i] == data) {
                    externalElementArray[i] = null;
                    removed = true;
                    activeExternalElements--;
                }
            }
            if (removed) {
                makeInactiveIfNoAppenders();
                clearCachedPerfMonTimer();
            }
        }
        return removed;
    }

    public IntervalData replaceExternalElement(IntervalData current, IntervalData replacement) {
        IntervalData result = null;
        synchronized (externalElementArrayLockToken) {
            for (int i = 0; i < externalElementArray.length && result == null; i++) {
                if (externalElementArray[i] == current) {
                    externalElementArray[i] = replacement;
                    result = replacement;
                }
            }
        }
        return result;
    }

    boolean hasExternalElement() {
        return activeExternalElements > 0;
    }

    private class PushAppenderDataTask extends FailSafeTimerTask {

        final PerfMon owner;

        final Appender appender;

        final int offset;

        final IntervalData perfMonData;

        PushAppenderDataTask(PerfMon owner, Appender appender, int offset) {
            this.owner = owner;
            this.appender = appender;
            this.offset = offset;
            synchronized (startStopLockToken) {
                perfMonData = appender.newIntervalData(owner, MiscHelper.currentTimeWithMilliResolution());
            }
            dataArray[offset] = this;
        }

        public void failSafeRun() {
            try {
                perfMonData.setTimeStop(MiscHelper.currentTimeWithMilliResolution());
                maxThroughputPerMinute = perfMonData.refreshMonitorsMaxThroughputPerMinute(maxThroughputPerMinute);
                appender.appendData(perfMonData);
            } catch (Exception ex) {
                logger.logError("Error running " + this.getClass().getSimpleName() + " task", ex);
            }
            priorityTimer.schedule(new PushAppenderDataTask(owner, this.appender, offset), appender.getIntervalMillis());
        }

        public String toString() {
            return "PushAppenderDataTask(" + "owner=" + owner + " appender=" + appender + ")";
        }
    }

    private boolean isInArray(Appender appender, PerfMonConfiguration.AppenderAndPattern[] appenders) {
        boolean result = false;
        for (int i = 0; i < appenders.length; i++) {
            if (appenders[i].getAppender() == appender) {
                result = true;
                break;
            }
        }
        return result;
    }

    private void resetAppenders(PerfMonConfiguration.AppenderAndPattern[] appenders, String[] childIgnoreList) throws InvalidConfigException {
        PerfMon children[] = getChildMonitors();
        for (int i = 0; i < children.length; i++) {
            PerfMon child = children[i];
            if (!isInChildIgnoreList(child, childIgnoreList)) {
                child.resetAppenders(parentToChildConversion(appenders), childIgnoreList);
            }
        }
        int numAppenders = appenderList.size();
        for (int i = numAppenders; i > 0; i--) {
            Appender appender = appenderList.get(i - 1);
            if (!isInArray(appender, appenders)) {
                removeAppender(appender, false, APPENDER_PATTERN_NA, true);
            }
        }
        for (int i = 0; i < appenders.length; i++) {
            addAppender(appenders[i].getAppender(), false, appenders[i].getAppenderPattern());
        }
    }

    /**
     * Package level!
     * Applications should call PerfMonTimer.start() to start a timer...
     */
    PerfMonTimer getPerfMonTimer() {
        if (cachedPerfMonTimer == null) {
            if (isRootMonitor()) {
                cachedPerfMonTimer = PerfMonTimer.getNullTimer();
            } else if (isActive() || hasExternalElement() || internalThreadTraceConfig != null || externalThreadTraceQueue.hasPendingElements()) {
                cachedPerfMonTimer = new PerfMonTimer(this, parent.getPerfMonTimer());
            } else {
                cachedPerfMonTimer = parent.getPerfMonTimer();
            }
        }
        return cachedPerfMonTimer;
    }

    public static boolean isConfigured() {
        return configured;
    }

    public static void deInit() {
        configure();
        configured = false;
        SnapShotManager.deInit();
    }

    /**
     * TESTONLY Dont Call this outside of TEST... Could have
     * Bad side effects!
     */
    public static void deInitAndCleanMonitors_TESTONLY() {
        deInit();
        synchronized (mapMonitorLockToken) {
            mapMonitors.clear();
        }
    }

    protected void clearCachedPerfMonTimer() {
        cachedPerfMonTimer = null;
        PerfMon children[] = getChildMonitors();
        for (int i = 0; i < children.length; i++) {
            children[i].clearCachedPerfMonTimer();
        }
    }

    public static void configure() {
        try {
            PerfMonConfiguration config = new PerfMonConfiguration();
            config.defineMonitor(PerfMon.ROOT_MONITOR_NAME);
            configure(config);
        } catch (InvalidConfigException ex) {
            logger.logWarn("Default configuration did not work", ex);
        }
    }

    private static int requestBasedTriggerCount = 0;

    public static boolean hasHttpRequestBasedThreadTraceTriggers() {
        return configured && (requestBasedTriggerCount > 0);
    }

    private static int sessionBasedTriggerCount = 0;

    public static boolean hasHttpSessionBasedThreadTraceTriggers() {
        return configured && (sessionBasedTriggerCount > 0);
    }

    private static int cookieBasedTriggerCount = 0;

    public static boolean hasHttpCookieBasedThreadTraceTriggers() {
        return configured && (cookieBasedTriggerCount > 0);
    }

    public static void configure(PerfMonConfiguration config) throws InvalidConfigException {
        if (PerfMon.isConfigured()) {
            Appender.flushAllAppenders();
        }
        configured = true;
        String monitors[] = config.getMonitorArray();
        boolean handledRootMonitor = false;
        for (int i = monitors.length; i > 0; i--) {
            String rawMonitorName = monitors[i - 1];
            PerfMon mon = PerfMon.getMonitor(rawMonitorName);
            PerfMonConfiguration.AppenderAndPattern appenders[] = config.getAppendersForMonitor(rawMonitorName, config);
            mon.resetAppenders(appenders, monitors);
            handledRootMonitor = (i == 1) && mon.isRootMonitor();
        }
        if (!handledRootMonitor) {
            PerfMon.getRootMonitor().resetAppenders(new PerfMonConfiguration.AppenderAndPattern[] {}, monitors);
        }
        SnapShotManager.applyConfig(config);
        Appender.purgeUnusedAppenders(config);
        int numHttpRequestTriggers = 0;
        int numHttpSessionTriggers = 0;
        int numHttpCookieTriggers = 0;
        Map<String, ThreadTraceConfig> threadTraceMap = config.getThreadTraceConfigMap();
        Iterator<Map.Entry<String, ThreadTraceConfig>> threadTraceItr = threadTraceMap.entrySet().iterator();
        while (threadTraceItr.hasNext()) {
            Map.Entry<String, ThreadTraceConfig> current = threadTraceItr.next();
            ThreadTraceConfig traceConfig = current.getValue();
            ThreadTraceConfig.Trigger triggers[] = traceConfig.getTriggers();
            if (triggers != null) {
                for (int i = 0; i < triggers.length; i++) {
                    if (triggers[i] instanceof ThreadTraceConfig.HTTPRequestTrigger) {
                        numHttpRequestTriggers++;
                    } else if (triggers[i] instanceof ThreadTraceConfig.HTTPSessionTrigger) {
                        numHttpSessionTriggers++;
                    } else if (triggers[i] instanceof ThreadTraceConfig.HTTPCookieTrigger) {
                        numHttpCookieTriggers++;
                    }
                }
            }
            PerfMon.getMonitor(current.getKey()).setInternalThreadTraceConfig(traceConfig);
        }
        requestBasedTriggerCount = numHttpRequestTriggers;
        sessionBasedTriggerCount = numHttpSessionTriggers;
        cookieBasedTriggerCount = numHttpCookieTriggers;
        String threadTraceMonitors[] = PerfMon.getMonitorNamesWithThreadTraceConfigAttached();
        for (int i = 0; i < threadTraceMonitors.length; i++) {
            String monitorName = threadTraceMonitors[i];
            if (!threadTraceMap.containsKey(monitorName)) {
                PerfMon.getMonitor(monitorName).setInternalThreadTraceConfig(null);
            }
        }
    }

    /**
     * Package level to allow unit test
     */
    static String parentToChildConversion(String pattern) {
        String result = APPENDER_PATTERN_NA;
        if (APPENDER_PATTERN_ALL_DESCENDENTS.equals(pattern) || APPENDER_PATTERN_PARENT_AND_ALL_DESCENDENTS.equals(pattern)) {
            result = APPENDER_PATTERN_PARENT_AND_ALL_DESCENDENTS;
        } else if (APPENDER_PATTERN_CHILDREN_ONLY.equals(pattern) || APPENDER_PATTERN_PARENT_AND_CHILDREN_ONLY.equals(pattern)) {
            result = APPENDER_PATTERN_PARENT_ONLY;
        }
        return result;
    }

    private static PerfMonConfiguration.AppenderAndPattern[] parentToChildConversion(PerfMonConfiguration.AppenderAndPattern[] appenders) throws InvalidConfigException {
        List<PerfMonConfiguration.AppenderAndPattern> result = new ArrayList<PerfMonConfiguration.AppenderAndPattern>();
        for (int i = 0; i < appenders.length; i++) {
            PerfMonConfiguration.AppenderAndPattern appender = appenders[i];
            String childPattern = parentToChildConversion(appender.getAppenderPattern());
            if (!APPENDER_PATTERN_NA.equals(childPattern)) {
                result.add(new PerfMonConfiguration.AppenderAndPattern(appender.getAppender().getMyAppenderID(), childPattern));
            }
        }
        return result.toArray(new PerfMonConfiguration.AppenderAndPattern[] {});
    }

    public boolean isActive() {
        return startTime != null;
    }

    private void makeInactiveIfNoAppenders() {
        synchronized (startStopLockToken) {
            if ((getNumPerfMonTasks() + getNumExternalAppenderTasks()) < 1) {
                startTime = null;
                totalHits = 0;
                totalCompletions = 0;
                maxDuration = 0;
                minDuration = NOT_SET;
                totalDuration = 0;
                sumOfSquares = 0;
                maxActiveThreadCount = 0;
                maxThroughputPerMinute = null;
            }
        }
    }

    private void makeActive() {
        synchronized (startStopLockToken) {
            if (!isActive()) {
                if (logger.isDebugEnabled()) {
                    logger.logDebug("Activating monitor " + this);
                }
                startTime = new Long(MiscHelper.currentTimeWithMilliResolution());
            }
        }
    }

    public long getStartTime() {
        return startTime != null ? startTime.longValue() : 0;
    }

    public long getTimeMaxActiveThreadCountSet() {
        return timeMaxActiveThreadCountSet;
    }

    public long getTimeMaxDurationSet() {
        return timeMaxDurationSet;
    }

    public long getTimeMinDurationSet() {
        return timeMinDurationSet;
    }

    public long getSumOfSquares() {
        return sumOfSquares;
    }

    public String toString() {
        return "PerfMon(monitorID=" + monitorID + " name=" + getName() + " parent=" + parent + ")";
    }

    public void scheduleExternalThreadTrace(ExternalThreadTraceConfig config) {
        externalThreadTraceQueue.schedule(config);
        clearCachedPerfMonTimer();
    }

    public void unScheduleExternalThreadTrace(ExternalThreadTraceConfig config) {
        externalThreadTraceQueue.unSchedule(config);
        clearCachedPerfMonTimer();
    }

    public void setInternalThreadTraceConfig(ThreadTraceConfig config) throws InvalidConfigException {
        clearCachedPerfMonTimer();
        boolean removeAppender = true;
        if (config != null) {
            AppenderID appenderIDs[] = config.getAppenders();
            boolean hasAppender = false;
            for (int i = 0; i < appenderIDs.length; i++) {
                AppenderID id = appenderIDs[i];
                if (id != null) {
                    Appender.getOrCreateAppender(appenderIDs[i]);
                    hasAppender = true;
                }
            }
            if (hasAppender) {
                removeAppender = false;
                monitorsWithThreadTraceConfigAttached.add(this.getName());
            } else {
                logger.logWarn("ThreadTraceMonitor on " + this.getName() + " will be ignored.  No valid appender attached");
            }
        }
        if (removeAppender) {
            monitorsWithThreadTraceConfigAttached.remove(this.getName());
        }
        this.internalThreadTraceConfig = config;
    }

    static String[] getMonitorNamesWithThreadTraceConfigAttached() {
        return monitorsWithThreadTraceConfigAttached.toArray(new String[] {});
    }

    public ThreadTraceConfig getInternalThreadTraceConfig() {
        return internalThreadTraceConfig;
    }

    public String toHTMLString() {
        String result = "<STRONG>" + "PerfMon(" + monitorID + "-" + name + ")</STRONG><CR>\r\n";
        result += "active=" + isActive() + "<CR>\r\n";
        if (isActive()) {
            result += "&nbsp;&nbsp;startTime=" + MiscHelper.formatDateTimeAsString(startTime) + "<CR>\r\n";
            result += "&nbsp;&nbsp;totalHits=" + totalHits + "<CR>\r\n";
            result += "&nbsp;&nbsp;totalCompletions=" + totalCompletions + "<CR>\r\n";
            result += "&nbsp;&nbsp;activeThreadCount=" + activeThreadCount + "<CR>\r\n";
            result += "&nbsp;&nbsp;maxDuration=" + maxDuration + " " + MiscHelper.formatDateTimeAsString(timeMaxDurationSet, false, true) + "<CR>\r\n";
            result += "&nbsp;&nbsp;minDuration=" + minDuration + " " + MiscHelper.formatDateTimeAsString(timeMinDurationSet, false, true) + "<CR>\r\n";
            result += "&nbsp;&nbsp;maxActiveThreadcount=" + maxActiveThreadCount + " " + MiscHelper.formatDateTimeAsString(timeMaxActiveThreadCountSet, false, true) + "<CR>\r\n";
            result += "&nbsp;&nbsp;maxThroughputPerMinute=" + (maxThroughputPerMinute != null ? maxThroughputPerMinute : "") + "<CR>\r\n";
        }
        return result;
    }

    public static String buildHTMLString(PerfMon monitor) {
        String result = monitor.toHTMLString();
        PerfMon children[] = monitor.getChildMonitors();
        for (int i = 0; i < children.length; i++) {
            result += buildHTMLString(children[i]);
        }
        return result;
    }

    public static String buildHTMLString() {
        return buildHTMLString(PerfMon.getRootMonitor());
    }

    public static ClassLoader getClassLoader() {
        return classLoader;
    }

    public static void setClassLoader(ClassLoader classLoader) {
        PerfMon.classLoader = classLoader;
    }

    public long getMaxSQLDuration() {
        return maxSQLDuration;
    }

    public long getTimeMaxSQLDurationSet() {
        return timeMaxSQLDurationSet;
    }

    public long getMinSQLDuration() {
        return Math.max(minSQLDuration, 0);
    }

    long getMinSQLDuration_NO_FIXUP() {
        return minSQLDuration;
    }

    public long getTimeMinSQLDurationSet() {
        return timeMinSQLDurationSet;
    }

    public long getTotalSQLDuration() {
        return totalSQLDuration;
    }

    public long getSumOfSQLSquares() {
        return sumOfSQLSquares;
    }
}
