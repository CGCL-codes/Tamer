package org.opencms.i18n;

import java.util.Locale;

/**
 * Convenience base class to access the localized messages of an OpenCms package.<p> 
 * 
 * @author Alexander Kandzior 
 * 
 * @version $Revision: 1.57 $
 * 
 * @since 6.0.0
 */
public abstract class A_CmsMessageBundle implements I_CmsMessageBundle {

    /**
     * Returns an array of all messages bundles used by the OpenCms core.<p>
     * 
     * @return an array of all messages bundles used by the OpenCms core
     */
    public static I_CmsMessageBundle[] getOpenCmsMessageBundles() {
        return new I_CmsMessageBundle[] { org.opencms.cache.Messages.get(), org.opencms.configuration.Messages.get(), org.opencms.db.Messages.get(), org.opencms.db.generic.Messages.get(), org.opencms.file.Messages.get(), org.opencms.file.collectors.Messages.get(), org.opencms.file.types.Messages.get(), org.opencms.flex.Messages.get(), org.opencms.i18n.Messages.get(), org.opencms.importexport.Messages.get(), org.opencms.jsp.Messages.get(), org.opencms.jsp.decorator.Messages.get(), org.opencms.jsp.util.Messages.get(), org.opencms.loader.Messages.get(), org.opencms.lock.Messages.get(), org.opencms.mail.Messages.get(), org.opencms.main.Messages.get(), org.opencms.module.Messages.get(), org.opencms.monitor.Messages.get(), org.opencms.notification.Messages.get(), org.opencms.publish.Messages.get(), org.opencms.relations.Messages.get(), org.opencms.report.Messages.get(), org.opencms.scheduler.Messages.get(), org.opencms.scheduler.jobs.Messages.get(), org.opencms.search.Messages.get(), org.opencms.search.documents.Messages.get(), org.opencms.security.Messages.get(), org.opencms.site.Messages.get(), org.opencms.staticexport.Messages.get(), org.opencms.synchronize.Messages.get(), org.opencms.util.Messages.get(), org.opencms.widgets.Messages.get(), org.opencms.workplace.Messages.get(), org.opencms.workplace.commons.Messages.get(), org.opencms.workplace.comparison.Messages.get(), org.opencms.workplace.editors.Messages.get(), org.opencms.workplace.explorer.Messages.get(), org.opencms.workplace.explorer.menu.Messages.get(), org.opencms.workplace.galleries.Messages.get(), org.opencms.workplace.help.Messages.get(), org.opencms.workplace.list.Messages.get(), org.opencms.workplace.search.Messages.get(), org.opencms.workplace.threads.Messages.get(), org.opencms.workplace.tools.Messages.get(), org.opencms.xml.Messages.get(), org.opencms.xml.content.Messages.get(), org.opencms.xml.page.Messages.get(), org.opencms.xml.types.Messages.get() };
    }

    /**
     * @see org.opencms.i18n.I_CmsMessageBundle#container(java.lang.String)
     */
    public CmsMessageContainer container(String key) {
        return container(key, null);
    }

    /**
     * @see org.opencms.i18n.I_CmsMessageBundle#container(java.lang.String, java.lang.Object)
     */
    public CmsMessageContainer container(String key, Object arg0) {
        return container(key, new Object[] { arg0 });
    }

    /**
     * @see org.opencms.i18n.I_CmsMessageBundle#container(java.lang.String, java.lang.Object, java.lang.Object)
     */
    public CmsMessageContainer container(String key, Object arg0, Object arg1) {
        return container(key, new Object[] { arg0, arg1 });
    }

    /**
     * @see org.opencms.i18n.I_CmsMessageBundle#container(java.lang.String, java.lang.Object, java.lang.Object, java.lang.Object)
     */
    public CmsMessageContainer container(String key, Object arg0, Object arg1, Object arg2) {
        return container(key, new Object[] { arg0, arg1, arg2 });
    }

    /**
     * @see org.opencms.i18n.I_CmsMessageBundle#container(java.lang.String, java.lang.Object[])
     */
    public CmsMessageContainer container(String message, Object[] args) {
        return new CmsMessageContainer(this, message, args);
    }

    /**
     * @see org.opencms.i18n.I_CmsMessageBundle#getBundle()
     */
    public CmsMessages getBundle() {
        Locale locale = CmsLocaleManager.getDefaultLocale();
        if (locale == null) {
            locale = Locale.getDefault();
        }
        return getBundle(locale);
    }

    /**
     * @see org.opencms.i18n.I_CmsMessageBundle#getBundle(java.util.Locale)
     */
    public CmsMessages getBundle(Locale locale) {
        return new CmsMessages(getBundleName(), locale);
    }

    /**
     * @see java.lang.Object#toString()
     */
    public String toString() {
        StringBuffer result = new StringBuffer();
        result.append('[');
        result.append(this.getClass().getName());
        result.append(", bundle: ");
        result.append(getBundle());
        result.append(']');
        return result.toString();
    }
}
