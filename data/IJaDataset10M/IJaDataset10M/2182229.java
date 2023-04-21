package org.openoss.opennms.spring.qosd;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.lang.reflect.UndeclaredThrowableException;
import java.rmi.RMISecurityManager;
import java.util.ArrayList;
import java.util.Date;
import java.util.Hashtable;
import java.util.List;
import java.util.Properties;
import javax.oss.fm.monitor.AlarmKey;
import javax.oss.fm.monitor.AlarmValue;
import org.apache.log4j.Logger;
import org.exolab.castor.xml.MarshalException;
import org.exolab.castor.xml.ValidationException;
import org.opennms.core.fiber.PausableFiber;
import org.opennms.core.utils.ThreadCategory;
import org.opennms.netmgt.dao.AlarmDao;
import org.opennms.netmgt.dao.AssetRecordDao;
import org.opennms.netmgt.dao.NodeDao;
import org.opennms.netmgt.eventd.EventIpcManager;
import org.opennms.netmgt.model.OnmsAlarm;
import org.opennms.netmgt.model.events.EventListener;
import org.opennms.netmgt.xml.event.Event;
import org.openoss.opennms.spring.dao.OnmsAlarmOssjMapper;
import org.openoss.opennms.spring.dao.OssDaoOpenNMSImpl;
import org.openoss.ossj.jvt.fm.monitor.OOSSAlarmValue;
import org.springframework.context.support.ClassPathXmlApplicationContext;

/** 
 * This class is a fiber scheduled by OpenNMS. It's purpose is to
 * collect OpenNMS alarms and transmit them to an OpenOSS QoS bean.
 *
 * The start() method loads the configuration for the QosD daemon and registers for events
 * Configuration is held in 4 files. 
 * <p>      QoSD-configuration.xml
 * <p>      qosd.properties
 * <p>      opennms.conf
 * <p>      rmi.policy
 *  
 * The Daemon starts in the following sequence;
 * <p>1. When the deamon starts it initialises the <CODE>AlarmListJ2eeConnectionManagerThread</CODE> and registers with the 
 * AlarmMonitor bean in the application server. 
 * 
 * <p>2. It then calls the AlarmListJ2eeConnectionManagerThread.Reset_List in order to cause the interface to send an 
 * AlarmListRebuiltEvent. 
 * <p>The JNDI naming factory, JMS queues and ejb's conected to by the daemon are specified in the 
 * qosd.properties file. The location of qosd.properties file is set by the JRE system variable
 * -DpropertiesFile which should be set when OpenNMS is started up. This is set in /etc/opennms.conf file
 * 
 * Contents of opennms.conf:
 * <CODE>
 * <p>ADDITIONAL_MANAGER_OPTIONS='-Djava.security.policy=/opt/OpenNMS/etc/rmi.policy \
 * -DpropertiesFile=/opt/OpenNMS/etc/qosd.properties \
 * -Drx_propertiesFile=/opt/OpenNMS/etc/qosdrx.properties \
 * -Djava.naming.provider.url=jnp://jbossjmsserver1:1099 \
 * -Djava.naming.factory.initial=org.jnp.interfaces.NamingContextFactory \
 * -Djava.naming.factory.url.pkgs=org.jboss.naming '
 * </CODE>
 * 
 * rmi.policy sets the security settings to allow the JVM to connect externally
 * 
 * Contents of rmi.policy:
 * <CODE>grant{permission java.security.AllPermission;};</CODE>
 * 
 * <p>3. The daemon then sends out the full current alarm list to the AlarmMonitor bean and  registers 
 * with OpenNMS for events
 * 
 * <p>The events used to run the QosD bean are determined by the file /etc/QoSD-configuration.xml
 * By default only the 'uei.opennms.org/vacuumd/alarmListChanged' uei is included in this file. This event
 * is generated when the <code>notifyOSSJnewAlarm</code> automation running in the vacuumd deamon 
 * determines that the alarm list has changed. In normal operation there is a short delay between an alarm 
 * entering the alarm list and the notifyOSSJnewAlarm automation picking it up. This can be significantly 
 * shortend for high priority alarms if their raise uei's are also included in the QoSD-configuration.xml file.
 * However for most alarms this is not worth the effort. 
 * <p>
 */
public class QoSDimpl2 implements PausableFiber, EventListener, QoSD {

    /**
	 *  Method to get the QosD's logger from OpenNMS
	 */
    public static Logger getLog() {
        ThreadCategory.setPrefix(LOG4J_CATEGORY);
        return (Logger) ThreadCategory.getInstance(QoSDimpl2.class);
    }

    private static OssDaoOpenNMSImpl ossDao;

    /**
	 * provides an interface to OpenNMS which provides a unified api 
	 * @param ossDao the ossDao to set
	 */
    public void setossDao(OssDaoOpenNMSImpl _ossDao) {
        ossDao = _ossDao;
    }

    private static OnmsAlarmOssjMapper onmsAlarmOssjMapper;

    /**
	 * Used by Spring Application context to pass in OnmsAlarmOssjMapper
	 * The OnmsAlarmOssjMapper class maps OpenNMS alarms to OSS/J alarms and events
	 * @param onmsAlarmOssjMapper the onmsAlarmOssjMapper to set
	 */
    public void setOnmsAlarmOssjMapper(OnmsAlarmOssjMapper _onmsAlarmOssjMapper) {
        onmsAlarmOssjMapper = _onmsAlarmOssjMapper;
    }

    /**
	 * Used to obtain opennms asset information for inclusion in alarms
	 * @see org.opennms.netmgt.dao.AssetRecordDao
	 */
    @SuppressWarnings("unused")
    private static AssetRecordDao assetRecordDao;

    /**
	 * Used by Spring Application context to pass in AssetRecordDao
	 * @param ar 
	 */
    public void setassetRecordDao(AssetRecordDao ar) {
        assetRecordDao = ar;
    }

    /**
	 * Used to obtain opennms node information for inclusion in alarms
	 * @see org.opennms.netmgt.dao.NodeDao 
	 */
    @SuppressWarnings("unused")
    private static NodeDao nodeDao;

    /**
	 * Used by Spring Application context to pass in NodeDaof
	 * @param nodedao 
	 */
    public void setnodeDao(NodeDao nodedao) {
        nodeDao = nodedao;
    }

    /**
	 * Used to register for opennms events
	 * @see org.opennms.netmgt.eventd.EventIpcManager
	 */
    private static EventIpcManager eventIpcManager;

    /**
	 * Used by Spring Application context to pass in EventIpcManager
	 * @param eventIpcManager
	 */
    public void seteventIpcManager(EventIpcManager evtIpcManager) {
        eventIpcManager = evtIpcManager;
    }

    /**
	 * Used to search and update opennms alarm list
	 * @see org.opennms.netmgt.dao.AlarmDao
	 */
    @SuppressWarnings("unused")
    private static AlarmDao alarmDao;

    /**
	 * Used by Spring Application context to pass in alarmDao
	 * @param alarmDao
	 */
    public void setalarmDao(AlarmDao almDao) {
        alarmDao = almDao;
    }

    /**
	 * AlarmListConnectionManager connects to the alarm list and allows the QosD to send alarm updates
	 * This is used by spring to provide a proxy for the J2EE AlarmMonitor bean or for the local spring
	 * AlarmMonitor if J2EE is not being used
	 */
    private static AlarmListConnectionManager alarmListConnectionManager;

    /**
	 * Used by Spring Application context to pass in AlarmListConnectionManager
	 * @param alcm
	 */
    public void setalarmListConnectionManager(AlarmListConnectionManager alcm) {
        alarmListConnectionManager = alcm;
    }

    /**
	 * used to hold a local reference to the application context from which this bean was started
	 */
    private ClassPathXmlApplicationContext m_context = null;

    /**
	 * Used by jmx mbean QoSD to pass in Spring Application context
	 * @param m_context - application conext for this bean to use
	 */
    public void setapplicationcontext(ClassPathXmlApplicationContext m_context) {
        this.m_context = m_context;
    }

    private int status = START_PENDING;

    private QoSDConfiguration config = null;

    public static PropertiesLoader props;

    private static Properties env;

    private static Hashtable<String, String> triggerUeiList;

    public static final String NAME = "OpenOSS.QoSD";

    private static final String LOG4J_CATEGORY = "OpenOSS.QoSD";

    private static String m_stats = null;

    public static boolean useUeiList = false;

    OpenNMSEventHandlerThread openNMSEventHandlerThread;

    /** Method to set up the fiber
	 *  Note - not used in Spring activation */
    public void init() {
        Logger log = getLog();
        log.info("Initialising QoSD");
    }

    /**
	 * The start() method loads the configuration for the QosD daemon and registers for events
	 */
    public void start() {
        status = STARTING;
        String jnp_host;
        Logger log = getLog();
        log.info("Qosd.start(): Preparing to load configuration");
        try {
            if (log.isDebugEnabled()) log.debug("Qosd.start():setting application context for alarmListConnectionManager: m.context.toString:" + m_context.toString());
            alarmListConnectionManager.setapplicationcontext(m_context);
        } catch (Exception ex) {
            throw new IllegalArgumentException("Qosd.start(): Error setting spring application context: " + ex);
        }
        try {
            config = QoSDConfigFactory.getConfig();
            log.info("QoSD QoSD-configuration.xml - Configuration Loaded Successfully");
            triggerUeiList = new Hashtable<String, String>();
            String[] temp = config.getEventlist().getUei();
            for (int i = 0; i < temp.length; i++) triggerUeiList.put(temp[i], "1");
        } catch (MarshalException mrshl_ex) {
            log.error("Qosd.start(): Marshal Exception thrown whilst getting QoSD configuration\n" + "\t\t\t\tEnsure tags have correct names", mrshl_ex);
            throw new UndeclaredThrowableException(mrshl_ex);
        } catch (ValidationException vldtn_ex) {
            log.error("Qosd.start(): Validation Exception thrown whilst getting QoSD configuration\n" + "\t\t\t\tMake sure all the tags are formatted correctly within QoSD-configuration.xml", vldtn_ex);
            throw new UndeclaredThrowableException(vldtn_ex);
        } catch (IOException io_ex) {
            String configFile = System.getProperty("opennms.home");
            if (configFile.endsWith(java.io.File.separator)) configFile.substring(0, configFile.length() - 1);
            configFile += "/etc/QoSD-configuration.xml";
            log.error("Qosd.start(): Failed to load configuration file: " + configFile + "\n\t\t\t\tMake sure that it exists", io_ex);
            throw new UndeclaredThrowableException(io_ex);
        }
        if (useUeiList) log.info("Qosd.start(): useUeiList = true = using QoSD QoSD-configuration.xml UEI list selects which alarms are sent");
        try {
            props = PropertiesLoader.getInstance();
        } catch (FileNotFoundException fnf_ex) {
            String propertiesFilename = System.getProperty("propertiesFile");
            log.error("Qosd.start(): Could not find properties file: " + propertiesFilename, fnf_ex);
            throw new UndeclaredThrowableException(fnf_ex);
        } catch (IOException io_ex) {
            String propertiesFilename = System.getProperty("propertiesFile");
            log.error("Qosd.start(): Could not read from properties file: " + propertiesFilename + "\n\t\t\t\tPlease check the file permissions", io_ex);
            throw new UndeclaredThrowableException(io_ex);
        }
        log.info("Qosd.start(): QosD Properties File Loaded");
        if (System.getSecurityManager() == null) System.setSecurityManager(new RMISecurityManager());
        if (props.getProperty("org.openoss.opennms.spring.qosd.naming.provider") != null) {
            jnp_host = (String) props.getProperty("org.openoss.opennms.spring.qosd.naming.provider");
            log.info("Using JNP: " + jnp_host);
        } else {
            log.warn("Qosd.start(): Naming provider property not set, Using default: jnp://jbossjmsserver1:1099");
            jnp_host = "jnp://jbossjmsserver1:1099";
        }
        env = new Properties();
        env.setProperty("java.naming.provider.url", jnp_host);
        env.setProperty("java.naming.factory.initial", props.getProperty("org.openoss.opennms.spring.qosd.naming.contextfactory"));
        env.setProperty("java.naming.factory.url.pkgs", props.getProperty("org.openoss.opennms.spring.qosd.naming.pkg"));
        try {
            alarmListConnectionManager.init(props, env);
            alarmListConnectionManager.start();
            log.info("Qosd.start(): Waiting Connection Manager Thread to get JMS connection");
            while (alarmListConnectionManager.getStatus() != AlarmListConnectionManager.CONNECTED) ;
            log.info("Qosd.start(): Connection Manager Thread JMS connection successfully registered");
            log.info("Qosd.start(): openNMS just restarted - sending alarm list rebuilt event");
            alarmListConnectionManager.reset_list("openNMS just restarted - alarm list rebuilt. Time:" + new Date());
        } catch (Exception iae) {
            log.error("Qosd.start(): Exception caught starting alarmListConnectionManager", iae);
            throw new UndeclaredThrowableException(iae);
        }
        try {
            if (log.isDebugEnabled()) log.debug("Qosd.start(): Using ossDao instance:" + (ossDao == null ? "IS NULL" : ossDao.toString()));
            log.info("Qosd.start(): Initialising the Node and alarm Caches");
            ossDao.init();
            log.info("Qosd.start(): Set up ossDao call back interface to QoSD for forwarding changes to alarm list");
            ossDao.setQoSD(this);
        } catch (Exception ex) {
            log.error("Qosd.start(): Exception caught setting callback interface from ossDao", ex);
            throw new UndeclaredThrowableException(ex);
        }
        log.info("Qosd.start(): initialising OpenNMSEventHandlerThread");
        try {
            openNMSEventHandlerThread = new OpenNMSEventHandlerThread();
            openNMSEventHandlerThread.setossDao(ossDao);
            openNMSEventHandlerThread.init();
            openNMSEventHandlerThread.start();
        } catch (Exception ex) {
            log.error("Qosd.start(): Exception caught initialising OpenNMSEventHandlerThread", ex);
            throw new UndeclaredThrowableException(ex);
        }
        log.info("Qosd.start(): openNMS just restarted - sending all alarms in rebuilt alarm list");
        try {
            openNMSEventHandlerThread.sendAlarmList();
        } catch (Exception e) {
            log.error("Qosd.start(): problem sending initial alarm list Error:", e);
        }
        log.info("Qosd.start(): Starting OpenNMS event listener");
        try {
            registerListener();
        } catch (Exception e) {
            log.error("Qosd.start(): problem registering event listener Error:", e);
        }
        status = RUNNING;
        log.info("QoSD Started");
    }

    /**
	 * Stop method of fiber, called by OpenNMS when fiber execution is to
	 * finish. Its purpose is to clean everything up, e.g. close any JNDI or 
	 * database connections, before the fiber's execution is ended. 
	 */
    public void stop() {
        Logger log = getLog();
        log.info("Stopping QosD");
        status = STOP_PENDING;
        try {
            unregisterListener();
        } catch (Exception ex) {
            log.error("stop() Error unregistering the OpenNMS event listener. Error:", ex);
        }
        try {
            openNMSEventHandlerThread.kill();
        } catch (Exception ex) {
            log.error("stop() Error killing openNMSEventHandlerThread. Error:", ex);
        }
        try {
            alarmListConnectionManager.kill();
        } catch (Exception ex) {
            log.error("stop() Error killing alarmListConnectionManager. Error:", ex);
        }
        status = STOPPED;
        log.info("QosD Stopped");
    }

    /**
	 * Resume method of fiber, called by OpenNMS to start the fiber up from 
	 * a paused state.
	 */
    public void resume() {
        Logger log = getLog();
        log.info("Resuming QosD");
        status = RESUME_PENDING;
        registerListener();
        status = RUNNING;
        log.info("QosD Resumed");
    }

    /**
	 * Pause method of fiber, called by OpenNMS to put the fiber in a 
	 * suspended state until it can be later resumed.
	 */
    public void pause() {
        Logger log = getLog();
        log.info("Pausing QosD");
        status = PAUSE_PENDING;
        unregisterListener();
        status = PAUSED;
        log.info("QosD Paused");
    }

    /**
	 *  Returns the Log category name
	 */
    public String getName() {
        return LOG4J_CATEGORY;
    }

    /**
	 *  lets OpenNMS know what state the daemon is in
	 *  @param status
	 */
    public int getStatus() {
        return status;
    }

    /**
	 * Registers an OpenNMS event listener with this class.
	 * When an event occurs, OpenNMS will call the onEvent()
	 * method of this object.
	 */
    public void registerListener() {
        Logger log = getLog();
        List<String> ueiList = new ArrayList<String>();
        String[] temp = config.getEventlist().getUei();
        for (int i = 0; i < temp.length; i++) ueiList.add(temp[i]);
        log.info("QosD Registering for " + temp.length + " types of event");
        eventIpcManager.addEventListener(this, ueiList);
    }

    /**
	 * Stops OpenNMS calling the onEvent method of this object when
	 * an event occurs.
	 */
    public void unregisterListener() {
        Logger log = getLog();
        log.info("QosD Unregistering for events");
        eventIpcManager.removeEventListener(this);
    }

    /**
	 * The OpenNMS event listener runs this routine when a 
	 * new event is detected. This can be run on any event but only needs to run on 
	 * uei.opennms.org/vacuumd/alarmListChanged
	 */
    public void onEvent(Event event) {
        Logger log = getLog();
        if (log.isDebugEnabled()) log.debug("Qosd.onEvent: OpenNMS Event Detected by QosD. uei '" + event.getUei() + "' Dbid(): " + event.getDbid() + "  event.getTime(): " + event.getTime());
        String s = event.getUei();
        if (s == null) return;
        if ("uei.opennms.org/nodes/nodeAdded".equals(s) || "uei.opennms.org/nodes/nodeLabelChanged".equals(s) || "uei.opennms.org/nodes/nodeDeleted".equals(s) || "uei.opennms.org/nodes/assetInfoChanged".equals(s)) {
            try {
                if (log.isDebugEnabled()) log.debug("QosD.onEvent Event causing update to node list");
                openNMSEventHandlerThread.updateNodeCache();
                return;
            } catch (Exception ex) {
                log.error("Qosd.onEvent. Problem calling openNMSEventHandlerThread.updateNodeCache(). Error:" + ex);
                return;
            }
        }
        if (event.getUei().equals("uei.opennms.org/vacuumd/alarmListChanged")) {
            if (log.isDebugEnabled()) log.debug("QosD.onEvent received 'uei.opennms.org/vacuumd/alarmListChanged' event; Updating alarm list");
        } else {
            try {
                if (event.getLogmsg().getDest().equals("donotpersist")) {
                    if (log.isDebugEnabled()) log.debug("QosD.onEvent Ignoring event marked as 'doNotPersist'. Event Uei:" + event.getUei());
                    return;
                }
                if (event.getAlarmData().getAlarmType() == 2) {
                    if (log.isDebugEnabled()) log.debug("Qosd.onEvent: uei '" + event.getUei() + "' Dbid(): " + event.getDbid() + " alarm type = 2 (clearing alarm) so ignoring.");
                    return;
                }
            } catch (NullPointerException e) {
                log.error("Qosd.onEvent: uei '" + event.getUei() + "' Dbid(): " + event.getDbid() + "' problem dealing with event. Check QoSD-configuration.xml.");
                return;
            }
        }
        try {
            if (log.isDebugEnabled()) log.debug("QosD.onEvent calling openNMSEventHandlerThread.sendAlarmList() to update list.");
            openNMSEventHandlerThread.sendAlarmList();
        } catch (Exception ex) {
            log.error("Qosd.onEvent. Problem calling openNMSEventHandlerThread.sendAlarmList(). Error:" + ex);
        }
    }

    /**
	 * A method to request an alarm list from the OpenNMS database using the ossDao, 
	 * convert them to OSS/J alarms using the onmsAlarmOssjMapper and send the OSS/J alarms
	 * using the alarm list connection manager (alcm) to  update the the AlarmMonitor bean.
	 * This is called from ossDao every time there is an update to the database.
	 */
    public void sendAlarms() {
        Logger log = getLog();
        Hashtable<AlarmKey, AlarmValue> ossjAlarmUpdateList = new Hashtable<AlarmKey, AlarmValue>();
        OnmsAlarm[] onmsAlarmUpdateList = null;
        AlarmValue ossjAlarm;
        try {
            if (log.isDebugEnabled()) log.debug("sendAlarms() using ossDao to get current alarm list");
            onmsAlarmUpdateList = ossDao.getAlarmCache();
        } catch (Exception ex) {
            log.error("sendAlarms() Cannot retrieve alarms from ossDao.getAlarmCache()", ex);
            throw new UndeclaredThrowableException(ex, "sendAlarms() Cannot retrieve alarms from ossDao.getAlarmCache()");
        }
        if (log.isDebugEnabled()) log.debug("sendAlarms() Alarms fetched. Processing each alarm in list.");
        try {
            for (int i = 0; i < onmsAlarmUpdateList.length; i++) {
                if (log.isDebugEnabled()) log.debug("sendAlarms() processing an OpenNMS alarm:");
                if (useUeiList) {
                    if (log.isDebugEnabled()) log.debug("sendAlarms() useUeiList= true: using UeiList to determine alarms to send");
                    if (null == triggerUeiList.get(onmsAlarmUpdateList[i].getUei())) {
                        if (log.isDebugEnabled()) log.debug("sendAlarms() alarm UEI not in QosD-configuration.xml. Not sending. alarmID:" + onmsAlarmUpdateList[i].getId() + " alarmUEI:" + onmsAlarmUpdateList[i].getUei());
                        continue;
                    }
                    if (log.isDebugEnabled()) log.debug("sendAlarms() alarm UEI is in QosD-configuration.xml. Trying to send alarmID:" + onmsAlarmUpdateList[i].getId() + " alarmUEI:" + onmsAlarmUpdateList[i].getUei());
                }
                if (onmsAlarmUpdateList[i].getAlarmType() != 1) {
                    if (log.isDebugEnabled()) log.debug("sendAlarms() Alarm AlarmType !=1 ( not raise alarm ) Not sending alarmID:" + onmsAlarmUpdateList[i].getId() + " :alarmBuf[i].getQosAlarmState()=: " + onmsAlarmUpdateList[i].getQosAlarmState());
                    continue;
                } else {
                    if (log.isDebugEnabled()) log.debug("sendAlarms() Alarm AlarmType==1 ( raise alarm ) Sending alarmID:" + onmsAlarmUpdateList[i].getId() + " :alarmBuf[i].getQosAlarmState()=: " + onmsAlarmUpdateList[i].getQosAlarmState());
                    try {
                        if (log.isDebugEnabled()) log.debug("sendAlarms(): generating the OSS/J alarm specification:");
                        ossjAlarm = alarmListConnectionManager.makeAlarmValueFromSpec();
                        if (log.isDebugEnabled()) log.debug("sendAlarms(): OSS/J alarm specification:" + OOSSAlarmValue.converttoString(ossjAlarm));
                        if (log.isDebugEnabled()) log.debug("sendAlarms(): onmsAlarmOssjMapper.populateOssjAlarmFromOpenNMSAlarm:");
                        ossjAlarm = onmsAlarmOssjMapper.populateOssjAlarmFromOpenNMSAlarm(ossjAlarm, onmsAlarmUpdateList[i]);
                        if (log.isDebugEnabled()) log.debug("buildList(): alarm specifcation:" + OOSSAlarmValue.converttoString(ossjAlarm));
                        if (true) try {
                            if (log.isDebugEnabled()) log.debug("sendAlarms() including ACKNOWLEDGED and CLEARED alarms in alarm in list");
                            ossjAlarmUpdateList.put(ossjAlarm.getAlarmKey(), ossjAlarm);
                        } catch (Exception e) {
                            log.error("sendAlarms() error putting alarm in alarmList", e);
                        }
                    } catch (Exception ex) {
                        log.error("sendAlarms() error trying to populate alarm - alarm disguarded - check alarm definitons", ex);
                    }
                }
            }
        } catch (Exception ex) {
            log.error("Qosd.sendAlarms(): Problem when building alarm list:", ex);
            throw new UndeclaredThrowableException(ex, "Qosd.sendAlarms(): Problem when building alarm list");
        }
        try {
            if (log.isDebugEnabled()) {
                log.debug("QosD sendAlarms() - Alarm list built:");
                log.debug("QosD sendAlarms() - ******* Alarm List to be sent : primary keys");
                for (AlarmKey key : ossjAlarmUpdateList.keySet()) {
                    AlarmValue a = ossjAlarmUpdateList.get(key);
                    log.debug("QosD sendAlarms() key : " + key.getPrimaryKey() + "  AlarmValue.getAlarmChangedTime: " + a.getAlarmChangedTime());
                }
                log.debug("QosD sendAlarms() - ******* END OF LIST");
                log.debug("QosD sendAlarms() Sending alarm list to bean");
            }
            alarmListConnectionManager.send(ossjAlarmUpdateList);
        } catch (Exception ex) {
            log.error("Qosd.sendAlarms(): Problem when sending alarm list:", ex);
            throw new UndeclaredThrowableException(ex, "Qosd.sendAlarms(): Problem when sending alarm list");
        }
    }

    /**
	 * not used but needed for initialization 
	 * @return stats
	 */
    public String getStats() {
        return (m_stats == null ? "No Stats Available" : m_stats.toString());
    }
}
