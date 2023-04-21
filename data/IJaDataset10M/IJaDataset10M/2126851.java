package org.peony.standard.db;

import org.peony.tools.PeonyLogger;

/**
 *  
 *
 *  @author     陈慧然
 *  @version    Id: SAJTAConnectionProvider.java, v 0.000 2008-7-8 下午04:08:39 陈慧然 Exp
 */
public abstract class SAJTAConnectionProviderFactory {

    /**
	 * 获得JTA连接生产器工厂实例
	 * @param className
	 * @return
	 */
    public static SAJTAConnectionProviderFactory getConnectionProviderFactory(String className) {
        PeonyLogger logger = PeonyLogger.getInstance();
        logger.info("正在初始化 JTA连接生成器工厂：" + className);
        try {
            Class clazz = Class.forName(className);
            SAJTAConnectionProviderFactory factory = (SAJTAConnectionProviderFactory) clazz.newInstance();
            logger.info("完成初始化 JTA连接生成器工厂：" + className);
            return factory;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    /**
	 * 获得JTA连接生产器实例
	 * @return
	 */
    public abstract SIJTAConnectionProvider getConnectionProvider();
}
