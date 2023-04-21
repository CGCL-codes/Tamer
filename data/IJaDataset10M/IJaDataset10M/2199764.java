package org.xmatthew.spy2servers.component.spy.jmx;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import javax.management.MBeanServerConnection;
import javax.management.ObjectInstance;
import javax.management.ObjectName;
import org.apache.commons.lang.StringUtils;
import org.xmatthew.spy2servers.core.Message;
import org.xmatthew.spy2servers.util.CollectionUtils;

/**
 * @author Matthew Xie
 * 
 */
public class TomcatJmxSpyComponent extends SunJVMJmxSpyComponent {

    public static final String DATASOURCE = "DataSource";

    public static final String MANAGER = "Manage";

    public static final String BASEMODELBEAN_CLASS = "org.apache.tomcat.util.modeler.BaseModelMBean";

    public static final String BASEMODELBAEN_CLASS_CHANGED = "org.apache.commons.modeler.BaseModelMBean";

    public static final String NUMACTIVE_NAME = "numActive";

    public static final String MAXACTIVE_NAME = "maxActive";

    public static final String NUMIDLE_NAME = "numIdle";

    public static final String MAXIDLE_NAME = "maxIdle";

    public static final String URL_NAME = "url";

    public static final String WEB_MODULE_MBNAME = "Catalina:type=Manager,path=%s,host=localhost";

    public static final String WEB_MODULE_STATUS = "WebModuleStatus";

    public static final String DATASOURCE_STATUS = "DataSourceStatus";

    private WebModuleSpy webModuleSpy;

    private DataSourcesSpy dataSourcesSpy;

    /**
	 * 
	 */
    public TomcatJmxSpyComponent() {
    }

    @Override
    protected void inspectMBean(ObjectInstance objectInstance, MBeanServerConnection mbsc) throws Exception {
        super.inspectMBean(objectInstance, mbsc);
        String mBeanClass = objectInstance.getClassName();
        ObjectName objectName = objectInstance.getObjectName();
        if (BASEMODELBEAN_CLASS.equals(mBeanClass) || BASEMODELBAEN_CLASS_CHANGED.equals(mBeanClass)) {
            String type = objectName.getKeyProperty(JmxSpySupportComponent.TYPE);
            String name = objectName.getKeyProperty(JmxSpySupportComponent.NAME);
            name = StringUtils.remove(name, "\"");
            if (DATASOURCE.equals(type)) {
                dataSourceMBeanSpy(name, getAttributesAsMap(objectName.toString(), mbsc, getDataSourceKeys()));
            } else if (MANAGER.equals(type)) {
            }
        }
    }

    private Set<String> dataSourceKeys;

    private Set<String> getDataSourceKeys() {
        if (dataSourceKeys == null) {
            dataSourceKeys = new HashSet<String>(5);
            dataSourceKeys.add(NUMACTIVE_NAME);
            dataSourceKeys.add(MAXACTIVE_NAME);
            dataSourceKeys.add(NUMIDLE_NAME);
            dataSourceKeys.add(MAXIDLE_NAME);
            dataSourceKeys.add(URL_NAME);
        }
        return dataSourceKeys;
    }

    private void dataSourceMBeanSpy(String dataSourceName, Map<String, Object> beansMap) {
        if (dataSourcesSpy == null) {
            return;
        }
        if (CollectionUtils.isBlankMap(beansMap)) {
            return;
        }
        long numActive = (Integer) beansMap.get(NUMACTIVE_NAME);
        long maxActive = (Integer) beansMap.get(MAXACTIVE_NAME);
        int spyedResult = dataSourcesSpy.spyDataSource(dataSourceName, numActive, maxActive);
        if (MemorySpy.MEMORY_LOWER_TO_GOOD == spyedResult || MemorySpy.MEMORY_WILL_OUT_OF_MAX == spyedResult) {
            Map<String, Object> dataSourceValues = new HashMap<String, Object>(6);
            dataSourceValues.put(NUMACTIVE_NAME, numActive);
            dataSourceValues.put(MAXACTIVE_NAME, maxActive);
            dataSourceValues.put(NUMIDLE_NAME, beansMap.get(NUMIDLE_NAME));
            dataSourceValues.put(MAXIDLE_NAME, beansMap.get(MAXIDLE_NAME));
            dataSourceValues.put(URL_NAME, beansMap.get(URL_NAME));
            dataSourceValues.put("dataSourceName", dataSourceName);
            onSpy(createMessage(dataSourceName, String.valueOf(spyedResult), Message.LV_ERROR, DATASOURCE_STATUS, dataSourceValues));
        }
    }

    /**
	 * @param dataSourcesSpy
	 *            the dataSourcesSpy to set
	 */
    public void setDataSourcesSpy(DataSourcesSpy dataSourcesSpy) {
        this.dataSourcesSpy = dataSourcesSpy;
    }

    public static final String DATASOURCE_PREFIX = "DataSource";

    @Override
    protected Set<String> getObjectNamesPrefix() {
        Set<String> newObjectNamesPrefix;
        Set<String> objectNamesPrefix = super.getObjectNamesPrefix();
        if (objectNamesPrefix == null) {
            newObjectNamesPrefix = new HashSet<String>(1);
        } else {
            newObjectNamesPrefix = new HashSet<String>(objectNamesPrefix.size() + 1);
            newObjectNamesPrefix.addAll(objectNamesPrefix);
        }
        newObjectNamesPrefix.add(DATASOURCE_PREFIX);
        return newObjectNamesPrefix;
    }

    public static final String WEB_MODULE_ON = "WebModuleOn";

    public static final String WEB_MODULE_OFF = "WebModuleOff";

    @Override
    public void mscOnInterval(MBeanServerConnection mbsc) throws Exception {
        super.mscOnInterval(mbsc);
        if (webModuleSpy == null) {
            return;
        }
        Set<String> webModules = webModuleSpy.getWebModules();
        if (CollectionUtils.isBlankCollection(webModules)) {
            return;
        }
        String mbeanName;
        ObjectName objectName;
        String webStatus;
        boolean registered;
        for (String webModule : webModules) {
            mbeanName = String.format(WEB_MODULE_MBNAME, webModule);
            objectName = new ObjectName(mbeanName);
            registered = mbsc.isRegistered(objectName);
            if (webModuleSpy.spyWebModule(webModule, registered)) {
                if (registered) {
                    webStatus = WEB_MODULE_ON;
                } else {
                    webStatus = WEB_MODULE_OFF;
                }
                onSpy(createMessage(webModule, webStatus, Message.LV_ERROR, WEB_MODULE_STATUS, new HashMap<String, Object>()));
            }
        }
    }

    /**
	 * @param webModuleSpy the webModuleSpy to set
	 */
    public void setWebModuleSpy(WebModuleSpy webModuleSpy) {
        this.webModuleSpy = webModuleSpy;
    }

    @Override
    protected void reStartJmxConnection() {
        if (webModuleSpy != null) webModuleSpy.init();
        if (dataSourcesSpy != null) dataSourcesSpy.init();
        super.reStartJmxConnection();
    }
}
