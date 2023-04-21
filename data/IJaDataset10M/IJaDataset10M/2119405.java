package it.aco.mandragora.bd.impl.proxy.methodInterceptor.concreteFactory;

import it.aco.mandragora.exception.MethodInterceptorFactoryException;
import it.aco.mandragora.util.configuration.impl.MandragoraConfigurator;
import it.aco.mandragora.bd.impl.proxy.methodInterceptor.MethodInterceptorFactory;
import org.apache.ojb.broker.util.configuration.Configurator;
import org.apache.ojb.broker.util.configuration.Configuration;
import net.sf.cglib.proxy.MethodInterceptor;

public class DefaultMethodInterceptorFactory extends MethodInterceptorFactory {

    private static DefaultMethodInterceptorFactory defaultMethodInterceptorFactory;

    static {
        defaultMethodInterceptorFactory = new DefaultMethodInterceptorFactory();
    }

    private DefaultMethodInterceptorFactory() {
    }

    public static DefaultMethodInterceptorFactory getInstance() {
        return defaultMethodInterceptorFactory;
    }

    /**
   * This method returns a new instance of the class implementation of the interface {@link MethodInterceptor}, specified by the input string <code>methodInterceptorClassName</code>.<br/>
   * The implementation of interface {@link MethodInterceptor} specified by the input string <code>methodInterceptorClassName</code> depends by his mapping in the <code>Mandragora.properties</code> file.<br/>
   * The class is instanced calling one of its constructor.<br/>
   * Apart of the no-arg  constructor the class to instance must have a constructor that take two string parameters,
   * that specify the contextBDFactoryClassName and the contextBDClassName name of the {@link it.aco.mandragora.bd.context.ContextBD} that will be used by the {@link MethodInterceptor#intercept(Object, java.lang.reflect.Method, Object[], net.sf.cglib.proxy.MethodProxy)} method. <br/>
   *
   * This two parameter (contextBDFactoryClassName and the contextBDClassName) to pass to the constructor are built in this way:<br/>
   * * <code>contextBDFactoryClassName = "DefaultMethodInterceptorFactory."+methodInterceptorClassName+".contextBDFactoryClassName"</code>.<br/>
   * * <code>contextBDClassName = "DefaultMethodInterceptorFactory."+methodInterceptorClassName+".contextBDClassName"</code>.<br/>
   * <br/>
   * @param methodInterceptorClassName Specifies the implementation of interface {@link MethodInterceptor} to return.
   * @return Will be returned the value mapped in <code>Mandragora.properties</code> with <code>methodInterceptorClassName</code>
   * @throws MethodInterceptorFactoryException
   */
    public MethodInterceptor getMethodInterceptor(String methodInterceptorClassName) throws MethodInterceptorFactoryException {
        MethodInterceptor methodInterceptor;
        try {
            Configurator configurator = MandragoraConfigurator.getInstance();
            Configuration config = configurator.getConfigurationFor(null);
            Class methodInterceptorClass = config.getClass(methodInterceptorClassName, null);
            String contextBDFactoryClassName = "DefaultMethodInterceptorFactory." + methodInterceptorClassName + ".contextBDFactoryClassName";
            String contextBDClassName = "DefaultMethodInterceptorFactory." + methodInterceptorClassName + ".contextBDClassName";
            methodInterceptor = (MethodInterceptor) methodInterceptorClass.getConstructor(new Class[] { String.class, String.class }).newInstance(new String[] { contextBDFactoryClassName, contextBDClassName });
        } catch (Exception e) {
            throw new MethodInterceptorFactoryException("Error in  DefaultMethodInterceptorFactory.getMethodInterceptor(String methodInterceptorClassName) : " + e.toString(), e);
        }
        return methodInterceptor;
    }

    /**
     * This method acts as <code>getMethodInterceptor("DefaultMethodInterceptorFactory.DefaultMethodInterceptorImpl")</code>; see {@link #getMethodInterceptor(String methodInterceptorClassName)};
     * @return  getMethodInterceptor("DefaultMethodInterceptorFactory.DefaultMethodInterceptorImpl"); see {@link #getMethodInterceptor(String methodInterceptorClassName)}
     * @throws MethodInterceptorFactoryException
     */
    public MethodInterceptor getMethodInterceptor() throws MethodInterceptorFactoryException {
        return getMethodInterceptor("DefaultMethodInterceptorFactory.DefaultMethodInterceptorImpl");
    }
}
