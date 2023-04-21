package com.buschmais.maexo.mbeans.osgi.core.impl;

import javax.management.DynamicMBean;
import javax.management.MBeanInfo;
import javax.management.MBeanNotificationInfo;
import javax.management.ObjectName;
import javax.management.openmbean.OpenMBeanAttributeInfoSupport;
import javax.management.openmbean.OpenMBeanConstructorInfoSupport;
import javax.management.openmbean.OpenMBeanInfoSupport;
import javax.management.openmbean.OpenMBeanOperationInfoSupport;
import org.osgi.framework.Bundle;
import org.osgi.framework.BundleContext;
import org.osgi.service.startlevel.StartLevel;
import com.buschmais.maexo.framework.commons.mbean.dynamic.DynamicMBeanSupport;
import com.buschmais.maexo.mbeans.osgi.core.BundleMBeanConstants;
import com.buschmais.maexo.mbeans.osgi.core.StartLevelMBean;
import com.buschmais.maexo.mbeans.osgi.core.StartLevelMBeanConstants;

/**
 * Represents the OSGi start level service.
 */
public final class StartLevelMBeanImpl extends DynamicMBeanSupport implements StartLevelMBean, DynamicMBean {

    /** The bundle context. */
    private final BundleContext bundleContext;

    /** The start level service to manage. */
    private final StartLevel startLevel;

    /**
	 * Constructor.
	 * 
	 * @param bundleContext
	 *            The bundle context.
	 * @param startLevel
	 *            The start level service.
	 */
    public StartLevelMBeanImpl(BundleContext bundleContext, StartLevel startLevel) {
        this.bundleContext = bundleContext;
        this.startLevel = startLevel;
    }

    /**
	 * {@inheritDoc}
	 */
    public MBeanInfo getMBeanInfo() {
        String className = StartLevelMBeanImpl.class.getName();
        OpenMBeanAttributeInfoSupport[] mbeanAttributeInfos = new OpenMBeanAttributeInfoSupport[] { StartLevelMBeanConstants.STARTLEVEL, StartLevelMBeanConstants.INITIALBUNDLE_STARTLEVEL };
        OpenMBeanOperationInfoSupport[] mbeanOperationInfos = new OpenMBeanOperationInfoSupport[] { StartLevelMBeanConstants.GETBUNDLESTARTLEVEL_BY_OBJECTNAME, StartLevelMBeanConstants.SETBUNDLESTARTLEVEL_BY_OBJECTNAME, StartLevelMBeanConstants.GETBUNDLESTARTLEVEL_BY_ID, StartLevelMBeanConstants.SETBUNDLESTARTLEVEL_BY_ID, StartLevelMBeanConstants.ISBUNDLEPERSISTENTLYSTARTED_BY_OBJECTNAME, StartLevelMBeanConstants.ISBUNDLEPERSISTENTLYSTARTED_BY_ID };
        OpenMBeanConstructorInfoSupport[] mbeanConstructorInfos = new OpenMBeanConstructorInfoSupport[] {};
        MBeanNotificationInfo[] mbeanNotificationInfos = new MBeanNotificationInfo[] {};
        OpenMBeanInfoSupport mbeanInfo = new OpenMBeanInfoSupport(className, BundleMBeanConstants.MBEAN_DESCRIPTION, mbeanAttributeInfos, mbeanConstructorInfos, mbeanOperationInfos, mbeanNotificationInfos);
        return mbeanInfo;
    }

    /**
	 * {@inheritDoc}
	 */
    public Integer getBundleStartLevel(ObjectName objectName) {
        Long id = (Long) getAttribute(objectName, BundleMBeanConstants.ID.getName());
        return this.getBundleStartLevel(id);
    }

    /**
	 * {@inheritDoc}
	 */
    public Integer getBundleStartLevel(Long id) {
        Bundle bundle = this.bundleContext.getBundle(id.longValue());
        if (null == bundle) {
            throw new IllegalArgumentException(String.format("cannot get bundle for id %s", id));
        }
        return Integer.valueOf(this.startLevel.getBundleStartLevel(bundle));
    }

    /**
	 * {@inheritDoc}
	 */
    public Integer getInitialBundleStartLevel() {
        return Integer.valueOf(this.startLevel.getInitialBundleStartLevel());
    }

    /**
	 * {@inheritDoc}
	 */
    public Integer getStartLevel() {
        return Integer.valueOf(this.startLevel.getStartLevel());
    }

    /**
	 * {@inheritDoc}
	 */
    public Boolean isBundlePersistentlyStarted(ObjectName objectName) {
        Long id = (Long) getAttribute(objectName, BundleMBeanConstants.ID.getName());
        return this.isBundlePersistentlyStarted(id);
    }

    /**
	 * {@inheritDoc}
	 */
    public Boolean isBundlePersistentlyStarted(Long id) {
        Bundle bundle = this.bundleContext.getBundle(id.longValue());
        if (null == bundle) {
            throw new IllegalArgumentException(String.format("cannot get bundle for id %s", id));
        }
        return Boolean.valueOf(this.startLevel.isBundlePersistentlyStarted(bundle));
    }

    /**
	 * {@inheritDoc}
	 */
    public void setBundleStartLevel(ObjectName objectName, Integer startLevel) {
        Long id = (Long) getAttribute(objectName, BundleMBeanConstants.ID.getName());
        this.setBundleStartLevel(id, startLevel);
    }

    /**
	 * {@inheritDoc}
	 */
    public void setBundleStartLevel(Long id, Integer startLevel) {
        Bundle bundle = this.bundleContext.getBundle(id.longValue());
        if (null == bundle) {
            throw new IllegalArgumentException(String.format("cannot get bundle for id %s", id));
        }
        this.startLevel.setBundleStartLevel(bundle, startLevel.intValue());
    }

    /**
	 * {@inheritDoc}
	 */
    public void setInitialBundleStartLevel(Integer startLevel) {
        this.startLevel.setInitialBundleStartLevel(startLevel.intValue());
    }

    /**
	 * {@inheritDoc}
	 */
    public void setStartLevel(Integer startLevel) {
        this.startLevel.setStartLevel(startLevel.intValue());
    }
}
