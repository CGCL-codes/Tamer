package org.opencms.repository;

import org.opencms.configuration.CmsConfigurationException;
import org.opencms.configuration.I_CmsConfigurationParameterHandler;
import java.util.Map;
import java.util.TreeMap;

/**
 * Abstract implementation of the repository interface {@link I_CmsRepository}.<p>
 * 
 * Handles the functionality of basic configuration. This is actually the configuration
 * of param/values and the filters ({@link CmsRepositoryFilter}) to use of the repository.<p>
 * 
 * @author Peter Bonrad
 * 
 * @version $Revision: 1.5 $ 
 * 
 * @since 6.2.4
 */
public abstract class A_CmsRepository implements I_CmsRepository, I_CmsConfigurationParameterHandler {

    /** The repository configuration. */
    private Map m_configuration;

    /** The filter to use for the repository. */
    private CmsRepositoryFilter m_filter;

    /** The name of the repository. */
    private String m_name;

    /**
     * Default constructor initializing member variables.<p>
     */
    public A_CmsRepository() {
        m_configuration = new TreeMap();
        m_filter = null;
    }

    /**
     * @see org.opencms.configuration.I_CmsConfigurationParameterHandler#addConfigurationParameter(java.lang.String, java.lang.String)
     */
    public void addConfigurationParameter(String paramName, String paramValue) {
        if (m_configuration.containsKey(paramName)) {
            String[] values = (String[]) m_configuration.get(paramName);
            String[] added = new String[values.length + 1];
            System.arraycopy(values, 0, added, 0, values.length);
            added[added.length - 1] = paramValue;
            m_configuration.put(paramName, added);
        } else {
            m_configuration.put(paramName, new String[] { paramValue });
        }
    }

    /**
     * @see org.opencms.configuration.I_CmsConfigurationParameterHandler#getConfiguration()
     */
    public Map getConfiguration() {
        return m_configuration;
    }

    /**
     * Returns the filter.<p>
     *
     * @return the filter
     */
    public CmsRepositoryFilter getFilter() {
        return m_filter;
    }

    /**
     * @see org.opencms.repository.I_CmsRepository#getName()
     */
    public String getName() {
        return m_name;
    }

    /**
     * @see org.opencms.configuration.I_CmsConfigurationParameterHandler#initConfiguration()
     */
    public void initConfiguration() throws CmsConfigurationException {
        if (m_filter != null) {
            m_filter.initConfiguration();
        }
        if (m_configuration == null) {
            throw new CmsConfigurationException(null);
        }
    }

    /**
     * Sets the filter.<p>
     *
     * @param filter the filter to set
     */
    public void setFilter(CmsRepositoryFilter filter) {
        m_filter = filter;
    }

    /**
     * @see org.opencms.repository.I_CmsRepository#setName(String)
     */
    public void setName(String name) {
        m_name = name;
    }
}
