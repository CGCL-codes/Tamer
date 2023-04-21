package net.sf.jabs.ui.component;

import java.util.Iterator;
import java.util.Map;
import net.sf.jabs.data.Exporter;
import net.sf.jabs.data.system.SystemConfigDAO;
import net.sf.jabs.infr.LdapConfigurator;
import net.sf.jabs.infr.services.ProjectSchedulingService;
import net.sf.jabs.infr.services.SchedulerService;
import net.sf.jabs.util.Constants;
import net.sf.jabs.util.StringUtil;
import org.apache.commons.configuration.ConfigurationException;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.tapestry.IRequestCycle;
import org.apache.tapestry.annotations.InjectObject;
import org.apache.tapestry.valid.IValidationDelegate;
import org.ofbiz.core.entity.GenericValue;
import org.quartz.CronExpression;

public abstract class SiteConfig extends AbstractComponent {

    private static Log _log = LogFactory.getLog(SiteConfig.class);

    public static final String PROPERTY_TYPE_SYSTEM = "system";

    @InjectObject("service:jabs.services.SystemConfigDAO")
    public abstract SystemConfigDAO getConfigDAO();

    @InjectObject("service:jabs.services.Exporter")
    public abstract Exporter getExporter();

    @InjectObject("service:jabs.services.ProjectSchedulingService")
    public abstract ProjectSchedulingService getProjectSchedulingService();

    public abstract void setMessage(String value);

    public abstract void setPropertyCache(Map<String, GenericValue> cache);

    public abstract void setEditForm(Boolean value);

    public abstract Map<String, GenericValue> getPropertyCache();

    public abstract IValidationDelegate getDelegate();

    public void doSave(IRequestCycle cycle) {
        IValidationDelegate delegate = getDelegate();
        if (delegate.getHasErrors()) {
            return;
        } else {
            _log.info("Cache Size " + getPropertyCache().size());
            saveSystemProperties();
            getPropertyCache().clear();
            setEditForm(false);
        }
    }

    /**
     * Render all fields in edit mode.
     * @param cycle
     */
    public void doEditForm(IRequestCycle cycle) {
        setEditForm(true);
    }

    /**
     * Cancel editing
     * @param cycle
     */
    public void doCancelForm(IRequestCycle cycle) {
        setEditForm(false);
    }

    private void saveSystemProperties() {
        Iterator<String> i = getPropertyCache().keySet().iterator();
        while (i.hasNext()) {
            saveSystemProperty(i.next());
        }
    }

    public GenericValue getSystemProperty(String systemProperty) {
        if (getPropertyCache().get(systemProperty) == null) {
            GenericValue g = getConfigDAO().getSystemConfig(systemProperty);
            if (g == null) {
                getPropertyCache().put(systemProperty, SystemConfigDAO.getNewProperty(systemProperty));
            } else {
                getPropertyCache().put(systemProperty, g);
            }
        }
        return getPropertyCache().get(systemProperty);
    }

    private void saveSystemProperty(String key) {
        saveSystemProperty(key, null);
    }

    private void saveSystemProperty(String key, String value) {
        GenericValue sp = getPropertyCache().get(key);
        if (sp == null) {
            sp = SystemConfigDAO.getNewProperty(key);
        }
        if (value != null) {
            sp.set("keyValue", value);
        }
        sp.set("type", PROPERTY_TYPE_SYSTEM);
        getConfigDAO().save(sp);
    }

    public abstract GenericValue getCreateLogin();

    public abstract void setCreateLogin(GenericValue value);

    public void saveCreateLogin(String newValue) throws Exception {
        _log.debug("saveCreateLogin(" + newValue + ")");
        if (newValue != null) {
            boolean b = Boolean.parseBoolean(newValue);
            getCreateLogin().set("keyValue", String.valueOf(b));
            getCreateLogin().set("type", PROPERTY_TYPE_SYSTEM);
            getConfigDAO().save(getCreateLogin());
            _log.info("Create login to value saved.");
        }
    }

    public abstract GenericValue getPurgeSchedule();

    public abstract void setPurgeSchedule(GenericValue value);

    public void savePurgeSchedule(String newValue) throws Exception {
        if (_log.isDebugEnabled()) _log.debug("savePurgeSchedule(" + newValue + ")");
        if (newValue != null) {
            if (!CronExpression.isValidExpression(newValue)) {
                _log.warn("Purge schedule [" + newValue + "] is not valid.");
                setMessage("The purge schedule value has a syntax error.");
                if (getPurgeSchedule().originalDbValuesAvailable()) {
                    getPurgeSchedule().set("keyValue", getPurgeSchedule().getOriginalDbValue("keyValue"));
                } else {
                    getPurgeSchedule().set("keyValue", Constants.SYSTEM_CONFIG_NO_VALUE);
                }
            } else {
                getPurgeSchedule().set("keyValue", newValue);
                getPurgeSchedule().set("type", PROPERTY_TYPE_SYSTEM);
                getConfigDAO().save(getPurgeSchedule());
                _log.info("Purge schedule saved.");
            }
        }
    }

    public void setDefaultSiteURL(IRequestCycle cycle) {
        saveSystemProperty(Constants.SYSTEM_CONFIG_SITE_URL, cycle.getAbsoluteURL(""));
    }

    public void doExport(IRequestCycle cycle) {
        getProjectSchedulingService().startExport();
        cycle.activate("ActiveJobs");
    }

    public void doImport(IRequestCycle cycle) {
        try {
            GenericValue sp = getConfigDAO().getSystemConfig(Constants.SYSTEM_CONFIG_IMPORT_PATH);
            if (sp != null) {
                getExporter().siteXmlImport(sp.getString("keyValue"));
            } else {
                _log.error("Unable to run import. The system property is not set.");
            }
        } catch (Exception e) {
            _log.error("Error importing", e);
        }
    }

    public void doReportPurge(IRequestCycle cycle) {
        getProjectSchedulingService().startReportPurge();
        cycle.activate("ActiveJobs");
    }

    public void doSchedulePurge(IRequestCycle cycle) {
        getProjectSchedulingService().startReportPurge(SchedulerService.SCHEDULE_CRON);
    }

    public void doScheduleExport(IRequestCycle cycle) {
        getProjectSchedulingService().startExport(SchedulerService.SCHEDULE_CRON);
    }

    public void doLdapEnable(IRequestCycle cycle) {
        LdapConfigurator ldap = new LdapConfigurator();
        try {
            ldap.enable();
            saveSystemProperty(Constants.SYSTEM_CONFIG_LDAP_ENABLED, "true");
        } catch (ConfigurationException e) {
            _log.error("Unable to enable LDAP", e);
        }
    }

    public void doLdapDisable(IRequestCycle cycle) {
        LdapConfigurator ldap = new LdapConfigurator();
        try {
            ldap.disable();
            saveSystemProperty(Constants.SYSTEM_CONFIG_LDAP_ENABLED, "false");
        } catch (ConfigurationException e) {
            _log.error("Unable to disable LDAP", e);
        }
    }

    /**
     * Runs the java garbage collector.
     * @param cycle
     */
    public void doGC(IRequestCycle cycle) {
        System.gc();
    }

    /**
     * Reschedules all stored jobs.
     * @param cycle
     */
    public void doRescheduleJobs(IRequestCycle cycle) {
        getProjectSchedulingService().rescheduleAll();
    }

    public String getFreeMemory() {
        StringBuffer sb = new StringBuffer();
        sb.append("Free: ");
        sb.append(StringUtil.formatSize(Runtime.getRuntime().freeMemory(), true));
        sb.append(" Used: ");
        sb.append(StringUtil.formatSize(Runtime.getRuntime().totalMemory() - Runtime.getRuntime().freeMemory(), true));
        sb.append(" Total: ");
        sb.append(StringUtil.formatSize(Runtime.getRuntime().totalMemory(), true));
        sb.append(" Max: ");
        sb.append(StringUtil.formatSize(Runtime.getRuntime().maxMemory(), true));
        return sb.toString();
    }
}
