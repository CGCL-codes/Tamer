package org.slasoi.gslam.slaregistry.impl.db.utils;

import javassist.ClassClassPath;
import javassist.ClassPool;
import javassist.util.proxy.ProxyFactory;
import javassist.util.proxy.ProxyFactory.ClassLoaderProvider;
import org.apache.log4j.Logger;

/**
 * Helper class for JavaAssist. Workaround for fixing the classloader under OSGi
 * 
 * @author Miguel Rojas (UDO)
 * 
 */
public class JavaAssistHelper {

    private static final Logger LOGGER = Logger.getLogger(JavaAssistHelper.class);

    private static ClassLoaderProvider current = ProxyFactory.classLoaderProvider;

    public static ClassLoaderProvider linkJavaAssistClassLoader() {
        LOGGER.info("--------------- ***  Linking JavaAssistHelper to SLARegistry *** -------------- [1]");
        LOGGER.info("--------------- ***  JavaAssistHelper :  current classLoaderProvider = " + current);
        ClassPool cp = ClassPool.getDefault();
        ClassClassPath classClassPath = new ClassClassPath(JavaAssistHelper.class);
        LOGGER.info("--------------- ***  JavaAssistHelper :  classpath = " + classClassPath);
        cp.insertClassPath(classClassPath);
        ProxyFactory.classLoaderProvider = new ProxyFactory.ClassLoaderProvider() {

            public ClassLoader get(ProxyFactory pf) {
                LOGGER.info("--------------- ***  Linking JavaAssistHelper to SLARegistry *** -------------- [2]");
                return Thread.currentThread().getContextClassLoader();
            }
        };
        return ProxyFactory.classLoaderProvider;
    }
}
