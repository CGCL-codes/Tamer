package org.opennms.test;

import org.springframework.beans.factory.InitializingBean;
import org.springframework.util.Assert;

/**
 * Support class to help with configuration that needs to happen in
 * integration tests before Spring attempts to do context initialization of
 * applicationContext-dao.xml.
 * In particular, this sets up system properties that are needed by Spring.
 * System properties are not set until afterPropertiesSet() is called.
 * 
 * @author <a href="mailto:dj@opennms.org">DJ Gregor</a>
 */
public class DaoTestConfigBean implements InitializingBean {

    private String m_relativeHomeDirectory = null;

    private final String m_absoluteHomeDirectory = null;

    private String m_rrdBinary = "/bin/true";

    private String m_relativeRrdBaseDirectory = "target/test/opennms-home/share/rrd";

    private final String m_relativeImporterDirectory = "target/test/opennms-home/etc/imports";

    private final String m_relativeForeignSourceDirectory = "target/test/opennms-home/etc/foreign-sources";

    public DaoTestConfigBean() {
    }

    public void afterPropertiesSet() {
        Assert.state(m_relativeHomeDirectory == null || m_absoluteHomeDirectory == null, "Only one of the properties relativeHomeDirectory and absoluteHomeDirectory can be set.");
        if (m_absoluteHomeDirectory != null) {
            ConfigurationTestUtils.setAbsoluteHomeDirectory(m_absoluteHomeDirectory);
        } else if (m_relativeHomeDirectory != null) {
            ConfigurationTestUtils.setRelativeHomeDirectory(m_relativeHomeDirectory);
        } else {
            ConfigurationTestUtils.setAbsoluteHomeDirectory(ConfigurationTestUtils.getDaemonEtcDirectory().getParentFile().getAbsolutePath());
        }
        ConfigurationTestUtils.setRrdBinary(m_rrdBinary);
        ConfigurationTestUtils.setRelativeRrdBaseDirectory(m_relativeRrdBaseDirectory);
        ConfigurationTestUtils.setRelativeImporterDirectory(m_relativeImporterDirectory);
        ConfigurationTestUtils.setRelativeForeignSourceDirectory(m_relativeForeignSourceDirectory);
    }

    public String getRelativeHomeDirectory() {
        return m_relativeHomeDirectory;
    }

    public void setRelativeHomeDirectory(String relativeHomeDirectory) {
        m_relativeHomeDirectory = relativeHomeDirectory;
    }

    public String getRelativeRrdBaseDirectory() {
        return m_relativeRrdBaseDirectory;
    }

    public void setRelativeRrdBaseDirectory(String rrdBaseDirectory) {
        m_relativeRrdBaseDirectory = rrdBaseDirectory;
    }

    public String getRrdBinary() {
        return m_rrdBinary;
    }

    public void setRrdBinary(String rrdBinary) {
        m_rrdBinary = rrdBinary;
    }
}

;
