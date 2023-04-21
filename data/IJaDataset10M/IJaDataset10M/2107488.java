package org.jiopi.ibean.kernel.util;

import java.util.Enumeration;
import java.util.Properties;
import org.jiopi.framework.core.JiopiConfigConstants;
import org.jiopi.ibean.share.ShareUtil.ClassUtil;

/**
 * 
 * 配置文件相关的工具类
 * 
 * @since 2010.4.19
 *
 */
public class ConfigUtil {

    /**
	 * 合并Properties
	 * @param properties Properties列表
	 * @param highBegin  高优先级在先
	 * @return
	 */
    public static Properties mergeProperties(Properties[] properties, boolean highBegin) {
        Properties pro = new Properties();
        if (properties != null && properties.length > 0) {
            int begin = 0;
            int add = 1;
            if (highBegin) {
                begin = properties.length - 1;
                add = -1;
            }
            for (int i = 0; i < properties.length; i++) {
                Properties propertie = properties[begin];
                Enumeration<Object> keys = propertie.keys();
                while (keys.hasMoreElements()) {
                    String key = (String) keys.nextElement();
                    String value = propertie.getProperty(key);
                    pro.put(key, value);
                }
                begin += add;
            }
        }
        return pro;
    }

    /**
	 * 
	 * @return
	 */
    public static ClassLoader getIBeanContextClassLoader() {
        ClassLoader jiopiClassLoader = ClassUtil.getClassLoaderByClass(JiopiConfigConstants.class);
        ClassLoader contextClassLoader = Thread.currentThread().getContextClassLoader();
        ClassLoader iBeanContextClassLoader = null;
        if (jiopiClassLoader == null && contextClassLoader == null) {
            iBeanContextClassLoader = ClassLoader.getSystemClassLoader();
        } else if (jiopiClassLoader == null || contextClassLoader == null) {
            if (jiopiClassLoader == null) iBeanContextClassLoader = contextClassLoader;
            if (contextClassLoader == null) iBeanContextClassLoader = jiopiClassLoader;
        } else if (jiopiClassLoader == contextClassLoader && contextClassLoader != null) {
            iBeanContextClassLoader = contextClassLoader;
        } else {
            int compare = ClassUtil.compareClassLoader(jiopiClassLoader, contextClassLoader);
            if (compare == 0) {
                iBeanContextClassLoader = jiopiClassLoader;
            } else {
                iBeanContextClassLoader = contextClassLoader;
            }
        }
        return iBeanContextClassLoader;
    }
}
