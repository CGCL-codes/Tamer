package com.vladium.emma.ant;

import java.util.Properties;
import org.apache.tools.ant.types.EnumeratedAttribute;
import com.vladium.emma.AppLoggers;
import com.vladium.emma.EMMAProperties;
import com.vladium.logging.ILogLevels;
import com.vladium.util.IProperties;

/**
 * @author Vlad Roubtsov, (C) 2004
 */
public final class VerbosityCfg {

    public static final class VerbosityAttribute extends EnumeratedAttribute {

        public String[] getValues() {
            return VALUES;
        }

        private static final String[] VALUES = new String[] { ILogLevels.SEVERE_STRING, ILogLevels.SILENT_STRING, ILogLevels.WARNING_STRING, ILogLevels.QUIET_STRING, ILogLevels.INFO_STRING, ILogLevels.VERBOSE_STRING, ILogLevels.TRACE1_STRING, ILogLevels.TRACE2_STRING, ILogLevels.TRACE3_STRING };
    }

    public void setVerbosity(final VerbosityAttribute verbosity) {
        m_verbosity = verbosity.getValue();
    }

    public void setVerbosityfilter(final String filter) {
        m_verbosityFilter = filter;
    }

    public IProperties getSettings() {
        IProperties settings = m_settings;
        if (settings == null) {
            settings = EMMAProperties.wrap(new Properties());
            if ((m_verbosity != null) && (m_verbosity.trim().length() > 0)) settings.setProperty(AppLoggers.PROPERTY_VERBOSITY_LEVEL, m_verbosity.trim());
            if ((m_verbosityFilter != null) && (m_verbosityFilter.trim().length() > 0)) settings.setProperty(AppLoggers.PROPERTY_VERBOSITY_FILTER, m_verbosityFilter.trim());
            m_settings = settings;
            return settings;
        }
        return settings;
    }

    private String m_verbosity;

    private String m_verbosityFilter;

    private transient IProperties m_settings;
}
