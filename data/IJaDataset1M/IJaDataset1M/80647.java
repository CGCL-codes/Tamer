package org.dcm4chex.archive.mbean;

import java.io.File;
import javax.management.ObjectName;
import javax.xml.transform.Templates;
import org.dcm4chex.archive.exceptions.ConfigurationException;
import org.jboss.system.ServiceMBeanSupport;

/**
 * @author Gunter Zeilinger <gunterze@gmail.com>
 * @version $Revision: 14795 $ $Date: 2011-01-31 10:15:13 -0500 (Mon, 31 Jan 2011) $
 * @since May 4, 2007
 */
public final class TemplatesDelegate {

    private final ServiceMBeanSupport service;

    private ObjectName templatesServiceName;

    private String configDir;

    public TemplatesDelegate(final ServiceMBeanSupport service) {
        this.service = service;
    }

    public final ObjectName getTemplatesServiceName() {
        return templatesServiceName;
    }

    public final void setTemplatesServiceName(ObjectName templatesServiceName) {
        this.templatesServiceName = templatesServiceName;
    }

    public final String getConfigDir() {
        return configDir;
    }

    public final void setConfigDir(String path) {
        this.configDir = path;
    }

    public Templates getTemplates(File f) {
        try {
            return (Templates) service.getServer().invoke(templatesServiceName, "getTemplates", new Object[] { f }, new String[] { File.class.getName() });
        } catch (Exception e) {
            throw new ConfigurationException(e);
        }
    }

    public Templates getTemplatesForAET(String aet, String fname) {
        try {
            return (Templates) service.getServer().invoke(templatesServiceName, "getTemplatesForAET", new Object[] { configDir, aet, fname }, new String[] { String.class.getName(), String.class.getName(), String.class.getName() });
        } catch (Exception e) {
            throw new ConfigurationException(e);
        }
    }

    public Templates findTemplates(String[] subdirs, String prefix, String[] fnames, String postfix) {
        try {
            return (Templates) service.getServer().invoke(templatesServiceName, "findTemplates", new Object[] { configDir, subdirs, prefix, fnames, postfix }, new String[] { String.class.getName(), String[].class.getName(), String.class.getName(), String[].class.getName(), String.class.getName() });
        } catch (Exception e) {
            throw new ConfigurationException(e);
        }
    }
}
