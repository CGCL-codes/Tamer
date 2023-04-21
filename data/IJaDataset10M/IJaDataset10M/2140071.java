package it.aco.mandragora.bd.impl.proxy.methodInterceptor.impl;

import net.sf.cglib.proxy.MethodInterceptor;
import com.thoughtworks.paranamer.CachingParanamer;
import it.aco.mandragora.bd.BD;
import it.aco.mandragora.common.ServiceLocator;
import it.aco.mandragora.common.methodInterceptor.CGLibMethodInterceptor;
import it.aco.mandragora.exception.MethodInterceptorException;
import it.aco.mandragora.exception.ServiceLocatorException;

public class CGLibBDMethodInterceptor extends CGLibMethodInterceptor implements MethodInterceptor {

    private static org.apache.log4j.Category log = org.apache.log4j.Logger.getLogger(CGLibBDMethodInterceptor.class.getName());

    private static String ContextBDFactoryClassName = "CGLibBDMethodInterceptor.ContextBDFactoryClassName";

    private static String ContextBDClassName = "CGLibBDMethodInterceptor.ContextBDClassName";

    /**
     * This constructor creates a new instance of this class, where the {@link #paranamer} is instantiated with the {@link com.thoughtworks.paranamer.CachingParanamer}, and the {@link #contextLayer} is looked up
     * by calling the {@link it.aco.mandragora.common.ServiceLocator} method  {@link it.aco.mandragora.common.ServiceLocator#getContextBD(String bdFactoryClassName, String bdClassName)} where :<br/>
     * <code>bdFactoryClassName={@link #ContextBDFactoryClassName}</code> and <br/>
     * <code>bdClassName={@link #ContextBDClassName}</code> and <br/>
     *
     * @throws it.aco.mandragora.exception.MethodInterceptorException
     */
    public CGLibBDMethodInterceptor() throws MethodInterceptorException {
        try {
            paranamer = new CachingParanamer();
            contextLayer = ServiceLocator.getInstance().getContextBD(ContextBDFactoryClassName, ContextBDClassName);
            layerClassName = "it.aco.mandragora.bd.BD";
        } catch (ServiceLocatorException e) {
            log.error("ServiceLocatorException caught in  CGLibBDMethodInterceptor.CGLibBDMethodInterceptor(): " + e.toString());
            throw new MethodInterceptorException("Error in  in CGLibBDMethodInterceptor.CGLibBDMethodInterceptor()" + e.toString(), e);
        } catch (Exception e) {
            log.error("Exception caught in  CGLibBDMethodInterceptor.CGLibBDMethodInterceptor(): " + e.toString());
            throw new MethodInterceptorException("Error in  in CGLibBDMethodInterceptor.CGLibBDMethodInterceptor()" + e.toString(), e);
        }
    }

    /**
     * This constructor creates a new instance of the MethodInterceptorDefaultImpl class, where the {@link #paranamer} is instantiated with the {@link com.thoughtworks.paranamer.CachingParanamer}, and the {@link #contextLayer} is looked up
     * by calling the {@link it.aco.mandragora.common.ServiceLocator} method  {@link it.aco.mandragora.common.ServiceLocator#getContextBD(String contextBDFactoryClassName, String contextBDClassName)} where
     * <code>contextBDFactoryClassName</code> is the input parameter <code>contextBDFactoryClassName</code> if  not null, and is {@link #ContextBDFactoryClassName} if null, and
     * analogously  <code>contextBDClassName</code> is the input parameter <code>contextBDClassName</code> if  not null, and is {@link #ContextBDClassName} if null. <br/>
     *
     * @param contextBDFactoryClassName used to look up the concrete subclass of {@link it.aco.mandragora.bd.context.ContextBDFactory} that is in turn used to look up the {@link it.aco.mandragora.bd.context.ContextBD} implementation that the attribute {@link #contextLayer} holds.
     * @param contextBDClassName used to identify the {@link it.aco.mandragora.bd.context.ContextBD} implementation to look up and load in the the attribute {@link #contextLayer}.
     * @throws it.aco.mandragora.exception.MethodInterceptorException
     */
    public CGLibBDMethodInterceptor(String contextBDFactoryClassName, String contextBDClassName) throws MethodInterceptorException {
        log.info("Entering CGLibBDMethodInterceptor.CGLibBDMethodInterceptor(String contextBDFactoryClassName,String contextBDClassName)");
        try {
            log.info("CGLibBDMethodInterceptor(String contextBDFactoryClassName,String contextBDClassName): contextBDFactoryClassName = " + contextBDFactoryClassName);
            log.info("CGLibBDMethodInterceptor(String contextBDFactoryClassName,String contextBDClassName): contextBDClassName = " + contextBDClassName);
            paranamer = new CachingParanamer();
            String localContextBDFactoryClassName = contextBDFactoryClassName;
            String localContextBDClassName = contextBDClassName;
            if (contextBDFactoryClassName == null || contextBDFactoryClassName.equals("")) localContextBDFactoryClassName = ContextBDFactoryClassName;
            if (contextBDClassName == null || contextBDClassName.equals("")) localContextBDClassName = ContextBDClassName;
            contextLayer = ServiceLocator.getInstance().getContextBD(localContextBDFactoryClassName, localContextBDClassName);
            layerClassName = "it.aco.mandragora.bd.BD";
        } catch (ServiceLocatorException e) {
            log.error("ServiceLocatorException caught in  CGLibBDMethodInterceptor.CGLibBDMethodInterceptor(String contextBDFactoryClassName,String contextBDClassName): " + e.toString());
            throw new MethodInterceptorException("Error in  in CGLibBDMethodInterceptor.CGLibBDMethodInterceptor(String contextBDFactoryClassName,String contextBDClassName)" + e.toString(), e);
        } catch (Exception e) {
            log.error("Exception caught in  CGLibBDMethodInterceptor.CGLibBDMethodInterceptor(String contextBDFactoryClassName,String contextBDClassName): " + e.toString());
            throw new MethodInterceptorException("Error in  in CGLibBDMethodInterceptor.CGLibBDMethodInterceptor(String contextBDFactoryClassName,String contextBDClassName)" + e.toString(), e);
        }
        log.info("Done with CGLibBDMethodInterceptor.CGLibBDMethodInterceptor(String contextBDFactoryClassName,String contextBDClassName)");
    }
}
