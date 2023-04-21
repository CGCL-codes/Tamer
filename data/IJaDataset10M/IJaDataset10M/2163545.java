package ldapbeans.config;

import java.lang.reflect.Proxy;

public interface LdapbeansConfigurationMBean {

    /**
     * Return the path where the generated class will be stored or
     * <code>null</code> if generated class will not be stored on file system
     * 
     * @return The path where the generated class will be stored or
     *         <code>null</code> if generated class will not be stored on file
     *         system
     */
    String getGeneratedClassPath();

    /**
     * Set the path where generated class will be written
     * 
     * @param p_GeneratedClassPath
     *            The path where generated class will be written
     */
    void setGeneratedClassPath(String p_GeneratedClassPath);

    /**
     * Return the name of the cache class implementation
     * 
     * @return The name of the cache class implementation
     */
    String getCacheImplementationClassName();

    /**
     * Return <code>true</code> if ldapbeans have to use dynamic proxy to
     * implements beans, <code>false</code> if beans classes have to be
     * generated
     * 
     * @return <code>true</code> if ldapbeans have to use dynamic proxy to
     *         implements beans, <code>false</code> if beans classes have to be
     *         generated
     */
    boolean useProxyBean();

    /**
     * The the flag that indicate if beans will be created by using
     * {@link Proxy}
     * 
     * @param p_UseProxyBean
     *            <code>true</code> if Proxy will be used, <code>false</code>
     *            otherwise
     */
    void setUseProxyBean(boolean p_UseProxyBean);
}
