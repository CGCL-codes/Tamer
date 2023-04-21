package it.imolinfo.jbi4ejb.runtime.ejbproxy;

import it.imolinfo.jbi4ejb.Logger;
import it.imolinfo.jbi4ejb.LoggerFactory;
import it.imolinfo.jbi4ejb.descriptor.ProviderServiceDescriptor;
import it.imolinfo.jbi4ejb.exception.EJBDeployException;
import it.imolinfo.jbi4ejb.exception.EJBWSDLGenerationException;
import it.imolinfo.jbi4ejb.webservice.generator.Util;
import java.io.File;
import java.net.MalformedURLException;
import java.util.List;
import java.util.Properties;
import org.omg.CORBA.ORB;

/**
 * A factory for creating StatelessEJBProxy objects.
 * 
 * @author <a href="mailto:mpiraccini@imolinfo.it">Marco Piraccini</a>
 */
public final class StatelessEJBProxyFactory {

    /** The Constant LOG. */
    private static final Logger LOG = LoggerFactory.getLogger(StatelessEJBProxyFactory.class);

    /**
     * Instantiates a new stateless EJB proxy factory.
     */
    private StatelessEJBProxyFactory() {
    }

    /**
     * Creates the <code>StatelessEJBProxy</code> from the service
     * description.
     * 
     * @param wsdl
     *            the wsdl <source>File</source>
     * @param serviceDescriptor
     *            the service descriotor
     * @param tempDir
     *            the temp dir where to create the EJB proxy files
     * @param jarFilesName
     *            the jars list
     * 
     * @return the <code>StatelessEJBProxy</code>
     * 
     * @throws EJBDeployException if some problem occurs in the ejb proxy creation
     */
    @SuppressWarnings("unchecked")
    public static StatelessEJBProxy createEJBProxy(File wsdl, ProviderServiceDescriptor serviceDescriptor, File tempDir, List<String> jarFilesName) throws EJBDeployException {
        String portTypeName = serviceDescriptor.getPortTypeName().getLocalPart();
        EJBClasses ejbClasses = EJBProxyUtils.createEJBClasses(wsdl, serviceDescriptor.getSerialVersionUID(), tempDir, jarFilesName, null, portTypeName);
        ClassLoader ejbInvokeClassLoader;
        try {
            ejbInvokeClassLoader = Util.getURLClassLoader(ejbClasses.getEjbClassesPath());
        } catch (MalformedURLException e) {
            LOG.error(e.getMessage());
            throw new EJBDeployException(e);
        }
        ORB orb = ORB.init(new String[] {}, serviceDescriptor.getOrbProperties());
        Object remoteBean = EJBProxyUtils.createStatelessEJBFromCorbaName(serviceDescriptor.getName(), ejbClasses.getRemoteInterfaceClassName(), ejbInvokeClassLoader, orb);
        StatelessEJBProxy ejbProxy = new StatelessEJBProxy(ejbClasses.getRemoteInterfaceClassName(), remoteBean, ejbInvokeClassLoader, orb);
        try {
            Class myRemoteInterface = ejbInvokeClassLoader.loadClass(ejbClasses.getRemoteInterfaceClassName());
            ejbProxy.setRemoteInterfaceClass(myRemoteInterface);
        } catch (ClassNotFoundException e) {
            String msg = "Error in getting the remote interface class from the EJB client class loader:" + e.getMessage();
            LOG.error(msg);
            throw new EJBDeployException(msg);
        }
        return ejbProxy;
    }

    /**
     * Gets the EJB from corbaname.
     * 
     * @param wsdlPath
     *          The path to the WSDL file
     * @param remoteInterfaceClassName
     *          The remote interface class name
     * @param corbaName
     *          The corba name
     * @param classesId
     *          The classes UIDs
     * @param jarFilesName
     *          The jar list to use to compile the classes
     * @param orbParams
     *          The orb parameters
     * @return the EJB from corbaname
     * 
     * @throws EJBWSDLGenerationException
     *          if some proxy ejb generation  occurs
     */
    @SuppressWarnings("unchecked")
    public static StatelessEJBProxy getEJBFromCorbaname(String wsdlPath, String remoteInterfaceClassName, String corbaName, Properties classesId, List<String> jarFilesName, Properties orbParams) throws EJBWSDLGenerationException {
        return getEJBFromCorbaname(wsdlPath, remoteInterfaceClassName, corbaName, classesId, jarFilesName, orbParams, false);
    }

    /**
     * Gets the EJB from corbaname using RMI classloader.
     * 
     * @param wsdlPath
     *      The path to the WSDL file
     * @param remoteInterfaceClassName
     *      The remote interface class name
     * @param corbaName
     *       The corba name
     * @param classesId
     *        The classes UIDs
     * @param jarFilesName
     *      The jar list to use to compile the classes
     * @param orbParams
     *       The orb parameters
     * 
     * @return the EJB from corbaname using RMI classloader
     * 
     * @throws EJBWSDLGenerationException
     *          if some proxy ejb generation  occurs
     */
    @SuppressWarnings("unchecked")
    public static StatelessEJBProxy getEJBFromCorbanameUsingRMIClassloader(String wsdlPath, String remoteInterfaceClassName, String corbaName, Properties classesId, List<String> jarFilesName, Properties orbParams) throws EJBWSDLGenerationException {
        return getEJBFromCorbaname(wsdlPath, remoteInterfaceClassName, corbaName, classesId, jarFilesName, orbParams, true);
    }

    /**
     * Instantiates a new dynamic EJB proxy.
     * 
     * @param wsdlPath
     *            The complete WSDL path
     * @param remoteInterfaceClassName
     *            The remote interface name
     * @param classesId
     *            The <code>Hashtable</code> containing the clasess UIDS
     * @param jarFilesName
     *            The jar list to use to compile the generated classes
     * @param jndiName
     *            The jndi name
     * @param jndiParams
     *            The jndi properties
     * @param orbProperties
     *            The ORB properties
     * 
     * @return the EJB proxy
     * 
     * @throws EJBWSDLGenerationException
     *             If some problem occurs
     */
    @SuppressWarnings("unchecked")
    public static StatelessEJBProxy getEJBFromJNDIName(String wsdlPath, String remoteInterfaceClassName, String jndiName, Properties jndiParams, Properties orbProperties, Properties classesId, List<String> jarFilesName) throws EJBWSDLGenerationException {
        ClassLoader ejbInvokeClassLoader = null;
        Object remoteBean = null;
        try {
            String classesDir = EJBProxyUtils.createEJBClasses(wsdlPath, remoteInterfaceClassName, null, classesId, jarFilesName);
            LOG.debug("The classes are in the directory:" + classesDir);
            ejbInvokeClassLoader = Util.getURLClassLoader(classesDir);
            ClassLoader previousClassLoader = Thread.currentThread().getContextClassLoader();
            Thread.currentThread().setContextClassLoader(ejbInvokeClassLoader);
            org.omg.CORBA.portable.ObjectImpl remoteHome = EJBProxyUtils.createStatelessHomeFromJNDI(jndiName, jndiParams, remoteInterfaceClassName, ejbInvokeClassLoader);
            ORB orb = remoteHome._orb();
            LOG.info("ORB found: " + orb);
            remoteBean = EJBProxyUtils.getEJBFromCorbaHomeObject(remoteHome, orb, remoteInterfaceClassName, ejbInvokeClassLoader);
            Thread.currentThread().setContextClassLoader(previousClassLoader);
            StatelessEJBProxy ejbProxy = new StatelessEJBProxy(remoteInterfaceClassName, remoteBean, ejbInvokeClassLoader, orb);
            return ejbProxy;
        } catch (Exception ex) {
            LOG.error(ex.getMessage());
            throw new EJBWSDLGenerationException(ex);
        }
    }

    /**
 * Instantiates a new dynamic EJB client.
 * 
 * @param wsdlPath
 *            The complete WSDL path
 * @param remoteInterfaceClassName
 *            The remote interface name
 * @param corbaName
 *            The corba name to use
 * @param classesId
 *            The <code>Hashtable</code> containing the clasess UIDS
 * @param jarFilesName
 *            The jar list to use to compile the generated classes
 * @param orbParams
 *            The ORB params
 * @param dynamicClassLoading
 *            If true, try to dinamically load stubs and classes using the
 *            RMIClassLoader
 * 
 * @return
 *      The EJB proxy
 * 
 * @throws EJBWSDLGenerationException
 *             If some problem occurs
 */
    @SuppressWarnings("unchecked")
    public static StatelessEJBProxy getEJBFromCorbaname(String wsdlPath, String remoteInterfaceClassName, String corbaName, Properties classesId, List<String> jarFilesName, Properties orbParams, boolean dynamicClassLoading) throws EJBWSDLGenerationException {
        ClassLoader ejbInvokeClassLoader = null;
        Object remoteBean = null;
        try {
            ORB orb = null;
            if (orbParams != null) {
                orb = ORB.init(new String[] {}, orbParams);
            } else {
                orb = ORB.init(new String[] {}, new Properties());
            }
            if (!dynamicClassLoading) {
                String classesDir = EJBProxyUtils.createEJBClasses(wsdlPath, remoteInterfaceClassName, null, classesId, jarFilesName);
                LOG.debug("The ejb client classes are in the directory:" + classesDir);
                ejbInvokeClassLoader = Util.getURLClassLoader(classesDir);
                remoteBean = EJBProxyUtils.createStatelessEJBFromCorbaName(corbaName, remoteInterfaceClassName, ejbInvokeClassLoader, orb);
            } else {
                LOG.debug("Dynamic invocation, classes loaded using RMI");
                Class myRemoteInterfaceClass = EJBProxyUtils.getInterfaceClass(remoteInterfaceClassName, wsdlPath, jarFilesName);
                LOG.debug("Loaded remote interface: " + myRemoteInterfaceClass);
                remoteBean = EJBProxyUtils.createStatelessEJBUsingRMIClassLoader(corbaName, remoteInterfaceClassName, myRemoteInterfaceClass, orb);
            }
            StatelessEJBProxy ejbProxy = new StatelessEJBProxy(remoteInterfaceClassName, remoteBean, ejbInvokeClassLoader, orb);
            return ejbProxy;
        } catch (Exception ex) {
            LOG.error(ex.getMessage());
            throw new EJBWSDLGenerationException(ex);
        }
    }
}
