package org.mobicents.slee.container.management.jmx;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicLong;
import javax.management.MBeanNotificationInfo;
import javax.management.NotCompliantMBeanException;
import javax.management.ObjectName;
import javax.slee.ComponentID;
import javax.slee.SbbID;
import javax.slee.UnrecognizedComponentException;
import javax.slee.facilities.AlarmFacility;
import javax.slee.facilities.AlarmLevel;
import javax.slee.facilities.Level;
import javax.slee.management.Alarm;
import javax.slee.management.AlarmNotification;
import javax.slee.management.ManagementException;
import javax.slee.management.NotificationSource;
import javax.slee.management.UnrecognizedNotificationSourceException;
import org.apache.log4j.Logger;
import org.mobicents.slee.container.SleeContainer;
import org.mobicents.slee.container.facilities.NotificationSourceWrapper;
import org.mobicents.slee.container.management.AlarmManagement;
import org.mobicents.slee.container.transaction.SleeTransactionManager;
import org.mobicents.slee.container.transaction.TransactionalAction;
import org.mobicents.slee.container.util.JndiRegistrationManager;
import org.mobicents.slee.runtime.facilities.DefaultAlarmFacilityImpl;

/**
 * Implementation of the Alarm MBean: The implementation of the JMX interface to
 * the SLEE alarm facility
 * 
 * @author baranowb
 * @author Tim
 * 
 */
@SuppressWarnings("deprecation")
public class AlarmMBeanImpl extends MobicentsServiceMBeanSupport implements AlarmManagement, AlarmMBeanImplMBean {

    public static String JNDI_NAME = "alarm";

    private static Logger log = Logger.getLogger(AlarmMBeanImpl.class);

    private Map<AlarmPlaceHolder, NotificationSource> placeHolderToNotificationSource = new HashMap<AlarmPlaceHolder, NotificationSource>();

    private Map<String, AlarmPlaceHolder> alarmIdToAlarm = new HashMap<String, AlarmPlaceHolder>();

    private final SleeTransactionManager sleeTransactionManager;

    private final TraceMBeanImpl traceMBean;

    /**
	 * @param sleeTransactionManager
	 * @param traceMBean
	 */
    public AlarmMBeanImpl(SleeTransactionManager sleeTransactionManager, TraceMBeanImpl traceMBean) throws NotCompliantMBeanException {
        super(AlarmMBeanImplMBean.class);
        this.sleeTransactionManager = sleeTransactionManager;
        this.traceMBean = traceMBean;
    }

    public AlarmFacility newAlarmFacility(NotificationSource notificationSource) {
        return new DefaultAlarmFacilityImpl(notificationSource, this);
    }

    public void setSleeContainer(SleeContainer sleeContainer) {
        this.sleeContainer = sleeContainer;
    }

    public boolean clearAlarm(String alarmID) throws NullPointerException, ManagementException {
        if (alarmID == null) {
            throw new NullPointerException("AlarmID must not be null");
        }
        AlarmPlaceHolder aph = alarmIdToAlarm.remove(alarmID);
        placeHolderToNotificationSource.remove(aph);
        if (aph == null) {
            return false;
        } else {
            try {
                generateNotification(aph, true);
            } catch (Exception e) {
                throw new ManagementException("Failed to clear alarm due to: " + e);
            }
            return true;
        }
    }

    public int clearAlarms(NotificationSource notificationSource) throws NullPointerException, UnrecognizedNotificationSourceException, ManagementException {
        if (notificationSource == null) {
            throw new NullPointerException("NotificationSource must not be null");
        }
        mandateSource(notificationSource);
        int count = 0;
        try {
            Map<AlarmPlaceHolder, NotificationSource> copy = new HashMap<AlarmPlaceHolder, NotificationSource>();
            copy.putAll(this.placeHolderToNotificationSource);
            for (Map.Entry<AlarmPlaceHolder, NotificationSource> e : copy.entrySet()) {
                if (e.getValue().equals(notificationSource)) {
                    if (clearAlarm(e.getKey().getAlarm().getAlarmID())) {
                        count++;
                    }
                }
            }
        } catch (Exception e) {
            throw new ManagementException("Failed to get alarm id list due to: ", e);
        }
        return count;
    }

    public int clearAlarms(NotificationSource notificationSource, String alarmType) throws NullPointerException, UnrecognizedNotificationSourceException, ManagementException {
        if (notificationSource == null) {
            throw new NullPointerException("NotificationSource must not be null");
        }
        if (alarmType == null) {
            throw new NullPointerException("AlarmType must not be null");
        }
        mandateSource(notificationSource);
        int count = 0;
        try {
            Map<AlarmPlaceHolder, NotificationSource> copy = new HashMap<AlarmPlaceHolder, NotificationSource>();
            copy.putAll(this.placeHolderToNotificationSource);
            for (Map.Entry<AlarmPlaceHolder, NotificationSource> e : copy.entrySet()) {
                if (e.getValue().equals(notificationSource) && e.getKey().getAlarmType().compareTo(alarmType) == 0) {
                    if (clearAlarm(e.getKey().getAlarm().getAlarmID())) {
                        count++;
                    }
                }
            }
        } catch (Exception e) {
            throw new ManagementException("Failed to get alarm id list due to: ", e);
        }
        return count;
    }

    public String[] getAlarms() throws ManagementException {
        try {
            Set<String> ids = alarmIdToAlarm.keySet();
            return ids.toArray(new String[ids.size()]);
        } catch (Exception e) {
            throw new ManagementException("Failed to get list of active alarms due to.", e);
        }
    }

    public String[] getAlarms(NotificationSource notificationSource) throws NullPointerException, UnrecognizedNotificationSourceException, ManagementException {
        if (notificationSource == null) {
            throw new NullPointerException("NotificationSource must not be null");
        }
        mandateSource(notificationSource);
        try {
            List<String> ids = new ArrayList<String>();
            Map<AlarmPlaceHolder, NotificationSource> copy = new HashMap<AlarmPlaceHolder, NotificationSource>();
            copy.putAll(this.placeHolderToNotificationSource);
            for (Map.Entry<AlarmPlaceHolder, NotificationSource> e : copy.entrySet()) {
                if (e.getValue().equals(notificationSource)) {
                    ids.add(e.getKey().getAlarm().getAlarmID());
                }
            }
            return ids.toArray(new String[ids.size()]);
        } catch (Exception e) {
            throw new ManagementException("Failed to get alarm id list due to: ", e);
        }
    }

    public Alarm getDescriptor(String alarmID) throws NullPointerException, ManagementException {
        if (alarmID == null) {
            throw new NullPointerException("AlarmID must not be null");
        }
        AlarmPlaceHolder aph = this.alarmIdToAlarm.get(alarmID);
        if (aph == null) return null;
        return aph.getAlarm();
    }

    public Alarm[] getDescriptors(String[] alarmIDs) throws NullPointerException, ManagementException {
        if (alarmIDs == null) {
            throw new NullPointerException("AlarmID[] must not be null");
        }
        List<Alarm> alarms = new ArrayList<Alarm>();
        try {
            for (String id : alarmIDs) {
                Alarm a = getDescriptor(id);
                if (a != null) alarms.add(a);
            }
            return alarms.toArray(new Alarm[alarms.size()]);
        } catch (Exception e) {
            throw new ManagementException("Failed to get desciptors.", e);
        }
    }

    public boolean isActive(String alarmID) throws NullPointerException, ManagementException {
        return this.alarmIdToAlarm.containsKey(alarmID);
    }

    public boolean isSourceOwnerOfAlarm(NotificationSourceWrapper notificationSource, String alarmID) {
        AlarmPlaceHolder aph = this.alarmIdToAlarm.get(alarmID);
        if (aph == null) return false;
        return aph.getNotificationSource().getNotificationSource().equals(notificationSource.getNotificationSource());
    }

    public boolean isAlarmAlive(String alarmID) {
        return this.alarmIdToAlarm.containsKey(alarmID);
    }

    public boolean isAlarmAlive(NotificationSourceWrapper notificationSource, String alarmType, String instanceID) {
        AlarmPlaceHolder aph = new AlarmPlaceHolder(notificationSource, alarmType, instanceID);
        return this.alarmIdToAlarm.containsValue(aph);
    }

    public String getAlarmId(NotificationSourceWrapper notificationSource, String alarmType, String instanceID) {
        AlarmPlaceHolder localAPH = new AlarmPlaceHolder(notificationSource, alarmType, instanceID);
        Alarm a = null;
        for (Map.Entry<String, AlarmPlaceHolder> e : this.alarmIdToAlarm.entrySet()) {
            if (e.getValue().equals(localAPH)) {
                a = e.getValue().getAlarm();
                break;
            }
        }
        if (a != null) return a.getAlarmID(); else return null;
    }

    /**
	 * THis methods raises alarm. It MUST not receive AlarmLevel.CLEAR, it has
	 * to be filtered.
	 * 
	 * @param notificationSource
	 * @param alarmType
	 * @param instanceID
	 * @param level
	 * @param message
	 * @param cause
	 * @return - AlarmId
	 */
    public String raiseAlarm(NotificationSourceWrapper notificationSource, String alarmType, String instanceID, AlarmLevel level, String message, Throwable cause) {
        synchronized (notificationSource) {
            if (isAlarmAlive(notificationSource, alarmType, instanceID)) {
                Alarm a = null;
                try {
                    AlarmPlaceHolder localAPH = new AlarmPlaceHolder(notificationSource, alarmType, instanceID);
                    for (Map.Entry<String, AlarmPlaceHolder> e : this.alarmIdToAlarm.entrySet()) {
                        if (e.getValue().equals(localAPH)) {
                            a = e.getValue().getAlarm();
                            break;
                        }
                    }
                } catch (Exception e) {
                }
                if (a != null) {
                    return a.getAlarmID();
                } else {
                    return this.raiseAlarm(notificationSource, alarmType, instanceID, level, message, cause);
                }
            } else {
                Alarm a = new Alarm(UUID.randomUUID().toString(), notificationSource.getNotificationSource(), alarmType, instanceID, level, message, cause, System.currentTimeMillis());
                AlarmPlaceHolder aph = new AlarmPlaceHolder(notificationSource, alarmType, instanceID, a);
                this.alarmIdToAlarm.put(a.getAlarmID(), aph);
                this.placeHolderToNotificationSource.put(aph, aph.getNotificationSource().getNotificationSource());
                generateNotification(aph, false);
                return a.getAlarmID();
            }
        }
    }

    private void generateNotification(AlarmPlaceHolder aph, boolean isCleared) {
        Alarm alarm = aph.getAlarm();
        AlarmLevel generalLevel = isCleared ? AlarmLevel.CLEAR : alarm.getAlarmLevel();
        AlarmNotification notification = new AlarmNotification(aph.getNotificationSource().getNotificationSource().getAlarmNotificationType(), this, alarm.getAlarmID(), aph.getNotificationSource().getNotificationSource(), alarm.getAlarmType(), alarm.getInstanceID(), generalLevel, alarm.getMessage(), alarm.getCause(), aph.getNotificationSource().getNextSequence(), System.currentTimeMillis());
        super.sendNotification(notification);
    }

    /**
	 * This method is requried - in case component is removed on call to method with its noti source we must throw unknown notification source exception - even thought alarms MAY be present?
	 * @throws UnrecognizedNotificationSourceException 
	 */
    private void mandateSource(NotificationSource src) throws UnrecognizedNotificationSourceException {
        if (!traceMBean.isNotificationSourceDefined(src)) {
            throw new UnrecognizedNotificationSourceException("Notification source is not present: " + src);
        }
    }

    class AlarmPlaceHolder {

        private NotificationSourceWrapper notificationSource;

        private String alarmType;

        private String instanceID;

        private Alarm alarm;

        public AlarmPlaceHolder(NotificationSourceWrapper notificationSource, String alarmType, String instanceID) {
            super();
            this.notificationSource = notificationSource;
            this.alarmType = alarmType;
            this.instanceID = instanceID;
        }

        public AlarmPlaceHolder(NotificationSourceWrapper notificationSource, String alarmType, String instanceID, Alarm a) {
            this.notificationSource = notificationSource;
            this.alarmType = alarmType;
            this.instanceID = instanceID;
            this.alarm = a;
        }

        public NotificationSourceWrapper getNotificationSource() {
            return notificationSource;
        }

        public String getAlarmType() {
            return alarmType;
        }

        public String getInstanceID() {
            return instanceID;
        }

        public Alarm getAlarm() {
            return alarm;
        }

        @Override
        public int hashCode() {
            final int prime = 31;
            int result = 1;
            result = prime * result + getOuterType().hashCode();
            result = prime * result + ((alarmType == null) ? 0 : alarmType.hashCode());
            result = prime * result + ((instanceID == null) ? 0 : instanceID.hashCode());
            result = prime * result + ((notificationSource == null) ? 0 : notificationSource.hashCode());
            return result;
        }

        @Override
        public boolean equals(Object obj) {
            if (this == obj) return true;
            if (obj == null) return false;
            if (getClass() != obj.getClass()) return false;
            AlarmPlaceHolder other = (AlarmPlaceHolder) obj;
            if (!getOuterType().equals(other.getOuterType())) return false;
            if (alarmType == null) {
                if (other.alarmType != null) return false;
            } else if (!alarmType.equals(other.alarmType)) return false;
            if (instanceID == null) {
                if (other.instanceID != null) return false;
            } else if (!instanceID.equals(other.instanceID)) return false;
            if (notificationSource == null) {
                if (other.notificationSource != null) return false;
            } else if (!notificationSource.equals(other.notificationSource)) return false;
            return true;
        }

        private AlarmMBeanImpl getOuterType() {
            return AlarmMBeanImpl.this;
        }
    }

    /**
	 * Represents a component registered with the alarm facility. Basically just
	 * stores notification sequence number
	 * 
	 * @author Tim
	 */
    static class RegisteredComp {

        public AtomicLong seqNo = new AtomicLong(0);

        public long getSeqNo() {
            return seqNo.getAndIncrement();
        }
    }

    private Map<ComponentID, RegisteredComp> registeredComps = new ConcurrentHashMap<ComponentID, RegisteredComp>();

    public boolean isRegisteredAlarmComponent(ComponentID alarmSource) {
        return this.registeredComps.containsKey(alarmSource);
    }

    public void createAlarm(ComponentID alarmSource, Level alarmLevel, String alarmType, String message, Throwable cause, long timestamp) throws UnrecognizedComponentException {
        if (log.isDebugEnabled()) {
            log.debug("alarmSource:" + alarmSource + " alarmLevel:" + alarmLevel + " alarmType:" + alarmType + " message:" + message + " cause:" + cause + " timeStamp:" + timestamp);
        }
        if (alarmSource == null || alarmLevel == null || alarmType == null || message == null) throw new NullPointerException("Null parameter");
        if (alarmLevel.isOff()) throw new IllegalArgumentException("Invalid alarm level");
        RegisteredComp comp = registeredComps.get(alarmSource);
        if (comp == null) throw new UnrecognizedComponentException("Component not registered");
        AlarmNotification notification = new AlarmNotification(this, alarmType, alarmSource, alarmLevel, message, cause, comp.getSeqNo(), timestamp);
        super.sendNotification(notification);
    }

    public void registerComponent(final SbbID sbbID) {
        if (log.isDebugEnabled()) {
            log.debug("Registering component with alarm facility: " + sbbID);
        }
        registeredComps.put(sbbID, new RegisteredComp());
        TransactionalAction action = new TransactionalAction() {

            public void execute() {
                registeredComps.remove(sbbID);
            }
        };
        sleeTransactionManager.getTransactionContext().getAfterRollbackActions().add(action);
    }

    public void unRegisterComponent(final SbbID sbbID) {
        final RegisteredComp registeredComp = this.registeredComps.remove(sbbID);
        if (registeredComp != null) {
            TransactionalAction action = new TransactionalAction() {

                public void execute() {
                    registeredComps.put(sbbID, registeredComp);
                }
            };
            sleeTransactionManager.getTransactionContext().getAfterRollbackActions().add(action);
        }
    }

    public ObjectName getAlarmMBeanObjectName() {
        return getObjectName();
    }

    public MBeanNotificationInfo[] getNotificationInfo() {
        return null;
    }

    @Override
    public void sleeInitialization() {
        JndiRegistrationManager.registerWithJndi("slee/facilities", AlarmMBeanImpl.JNDI_NAME, this);
    }

    @Override
    public void sleeStarting() {
    }

    @Override
    public void sleeRunning() {
    }

    @Override
    public void sleeStopping() {
    }

    @Override
    public void sleeStopped() {
    }

    @Override
    public void sleeShutdown() {
    }
}
