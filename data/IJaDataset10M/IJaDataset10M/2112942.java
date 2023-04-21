package org.opennms.netmgt.dao;

import org.opennms.netmgt.dao.db.AbstractTransactionalTemporaryDatabaseSpringContextTests;
import org.opennms.netmgt.dao.hibernate.LocationMonitorDaoHibernate;
import org.opennms.netmgt.model.OnmsNode;
import org.opennms.test.DaoTestConfigBean;

public class AbstractTransactionalDaoTestCase extends AbstractTransactionalTemporaryDatabaseSpringContextTests {

    private DistPollerDao m_distPollerDao;

    private NodeDao m_nodeDao;

    private IpInterfaceDao m_ipInterfaceDao;

    private SnmpInterfaceDao m_snmpInterfaceDao;

    private MonitoredServiceDao m_monitoredServiceDao;

    private ServiceTypeDao m_serviceTypeDao;

    private AssetRecordDao m_assetRecordDao;

    private CategoryDao m_categoryDao;

    private OutageDao m_outageDao;

    private EventDao m_eventDao;

    private AlarmDao m_alarmDao;

    private NotificationDao m_notificationDao;

    private UserNotificationDao m_userNotificationDao;

    private AvailabilityReportLocatorDao m_availabilityReportLocatorDao;

    private LocationMonitorDaoHibernate m_locationMonitorDao;

    private OnmsMapDao m_onmsMapDao;

    private OnmsMapElementDao m_onmsMapElementDao;

    private DataLinkInterfaceDao m_dataLinkInterfaceDao;

    private AcknowledgmentDao m_acknowledgmentDao;

    private LinkStateDao m_linkStateDao;

    private DatabasePopulator m_populator;

    private boolean m_populate = true;

    @Override
    protected void setUpConfiguration() {
        DaoTestConfigBean daoTestConfig = new DaoTestConfigBean();
        daoTestConfig.afterPropertiesSet();
    }

    @Override
    protected String[] getConfigLocations() {
        return new String[] { "classpath:/META-INF/opennms/applicationContext-dao.xml", "classpath*:/META-INF/opennms/component-dao.xml", "classpath:/META-INF/opennms/applicationContext-databasePopulator.xml", "classpath:/META-INF/opennms/applicationContext-setupIpLike-enabled.xml" };
    }

    @Override
    protected void onSetUpInTransactionIfEnabled() {
        populateDatabase();
    }

    private void populateDatabase() {
        if (m_populate) {
            m_populator.populateDatabase();
        }
    }

    public AlarmDao getAlarmDao() {
        return m_alarmDao;
    }

    public void setAlarmDao(AlarmDao alarmDao) {
        m_alarmDao = alarmDao;
    }

    public AssetRecordDao getAssetRecordDao() {
        return m_assetRecordDao;
    }

    public void setAssetRecordDao(AssetRecordDao assetRecordDao) {
        m_assetRecordDao = assetRecordDao;
    }

    public AvailabilityReportLocatorDao getAvailabilityReportLocatorDao() {
        return m_availabilityReportLocatorDao;
    }

    public void setAvailabilityReportLocatorDao(AvailabilityReportLocatorDao availabilityReportLocatorDao) {
        m_availabilityReportLocatorDao = availabilityReportLocatorDao;
    }

    public CategoryDao getCategoryDao() {
        return m_categoryDao;
    }

    public void setCategoryDao(CategoryDao categoryDao) {
        m_categoryDao = categoryDao;
    }

    public DistPollerDao getDistPollerDao() {
        return m_distPollerDao;
    }

    public void setDistPollerDao(DistPollerDao distPollerDao) {
        m_distPollerDao = distPollerDao;
    }

    public EventDao getEventDao() {
        return m_eventDao;
    }

    public void setEventDao(EventDao eventDao) {
        m_eventDao = eventDao;
    }

    public IpInterfaceDao getIpInterfaceDao() {
        return m_ipInterfaceDao;
    }

    public void setIpInterfaceDao(IpInterfaceDao ipInterfaceDao) {
        m_ipInterfaceDao = ipInterfaceDao;
    }

    public MonitoredServiceDao getMonitoredServiceDao() {
        return m_monitoredServiceDao;
    }

    public void setMonitoredServiceDao(MonitoredServiceDao monitoredServiceDao) {
        m_monitoredServiceDao = monitoredServiceDao;
    }

    public NodeDao getNodeDao() {
        return m_nodeDao;
    }

    public void setNodeDao(NodeDao nodeDao) {
        m_nodeDao = nodeDao;
    }

    public NotificationDao getNotificationDao() {
        return m_notificationDao;
    }

    public void setNotificationDao(NotificationDao notificationDao) {
        m_notificationDao = notificationDao;
    }

    public OutageDao getOutageDao() {
        return m_outageDao;
    }

    public void setOutageDao(OutageDao outageDao) {
        m_outageDao = outageDao;
    }

    public ServiceTypeDao getServiceTypeDao() {
        return m_serviceTypeDao;
    }

    public void setServiceTypeDao(ServiceTypeDao serviceTypeDao) {
        m_serviceTypeDao = serviceTypeDao;
    }

    public SnmpInterfaceDao getSnmpInterfaceDao() {
        return m_snmpInterfaceDao;
    }

    public void setSnmpInterfaceDao(SnmpInterfaceDao snmpInterfaceDao) {
        m_snmpInterfaceDao = snmpInterfaceDao;
    }

    public UserNotificationDao getUserNotificationDao() {
        return m_userNotificationDao;
    }

    public void setUserNotificationDao(UserNotificationDao userNotificationDao) {
        m_userNotificationDao = userNotificationDao;
    }

    public OnmsNode getNode1() {
        return m_populator.getNode1();
    }

    public LocationMonitorDaoHibernate getLocationMonitorDao() {
        return m_locationMonitorDao;
    }

    public void setLocationMonitorDao(LocationMonitorDaoHibernate locationMonitorDao) {
        m_locationMonitorDao = locationMonitorDao;
    }

    public OnmsMapDao getOnmsMapDao() {
        return m_onmsMapDao;
    }

    public void setOnmsMapDao(OnmsMapDao onmsMapDao) {
        this.m_onmsMapDao = onmsMapDao;
    }

    public OnmsMapElementDao getOnmsMapElementDao() {
        return m_onmsMapElementDao;
    }

    public void setOnmsElementMapDao(OnmsMapElementDao onmsMapElementDao) {
        this.m_onmsMapElementDao = onmsMapElementDao;
    }

    public DataLinkInterfaceDao getDataLinkInterfaceDao() {
        return m_dataLinkInterfaceDao;
    }

    public void setDataLinkInterfaceDao(DataLinkInterfaceDao dataLinkInterfaceDao) {
        this.m_dataLinkInterfaceDao = dataLinkInterfaceDao;
    }

    public AcknowledgmentDao getAcknowledgmentDao() {
        return m_acknowledgmentDao;
    }

    public void setAcknowledgmentDao(AcknowledgmentDao acknowledgmentDao) {
        m_acknowledgmentDao = acknowledgmentDao;
    }

    public boolean isPopulate() {
        return m_populate;
    }

    public void setPopulate(boolean populate) {
        m_populate = populate;
    }

    public DatabasePopulator getPopulator() {
        return m_populator;
    }

    public void setPopulator(DatabasePopulator populator) {
        m_populator = populator;
    }

    public void setLinkStateDao(LinkStateDao linkStateDao) {
        m_linkStateDao = linkStateDao;
    }

    public LinkStateDao getLinkStateDao() {
        return m_linkStateDao;
    }
}
