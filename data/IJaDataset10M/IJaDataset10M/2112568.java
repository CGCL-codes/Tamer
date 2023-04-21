package fab.formatic.backend.util;

import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

public final class FabBeansLocator {

    private static final String CONFIG = "classpath:applicationContext-jpa.xml";

    private static ApplicationContext context;

    public static final String CUSTOMER_DAO = "fabCustomerDao";

    public static final String SERVICE_DAO = "fabServiceDao";

    public static final String INCIDENT_DAO = "fabIncidentDao";

    public static final String LOGIN_DAO = "fabLoginDao";

    public static final String MODULE_DAO = "fabModuleDao";

    public static final String MODULE_STATUS_DAO = "fabModuleStatusDao";

    public static final String ORDER_DAO = "fabOrderDao";

    public static final String PACKAGE_DAO = "fabPackageDao";

    public static final String ORDER_MODULE_DAO = "fabOrderModuleDao";

    public static final String SERVICE_STATUS_DAO = "fabServiceStatusDao";

    public static final String SERVICE_USED_DAO = "fabServiceUsedDao";

    public static final String STATUS_ORDER_DAO = "fabStatusOrderDao";

    public static final String TARIFF_DAO = "fabTariffDao";

    public static final String TYPE_CHARGING_DAO = "fabTypeChargingDao";

    public static ApplicationContext getDaoContext() {
        context = new ClassPathXmlApplicationContext(CONFIG);
        return context;
    }

    public static Object getDaoClass(String springBeanId) {
        if (context == null) context = new ClassPathXmlApplicationContext(CONFIG);
        return context.getBean(springBeanId);
    }
}
