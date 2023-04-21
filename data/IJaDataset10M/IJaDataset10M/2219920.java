package com.hs.framework.service;

import com.hs.framework.Mode;
import com.hs.framework.common.util.BeanUtil;
import com.hs.framework.common.util.ConfigUtil;
import com.hs.framework.common.util.Configs;

/**
 * serviceFactory �Ĵ�����
 * 
 * @author Ansun
 * 
 */
public class ServiceFactoryProxy {

    private static ServiceFactory serviceFactory = null;

    /**
	 * 通过ServiceFactory获得Service的实例，并返回�??
	 * 
	 * @param mode
	 * @return
	 */
    public static synchronized Service getService(Mode mode) {
        Service service = null;
        if (serviceFactory == null) {
            getServiceFactory();
        }
        service = (Service) serviceFactory.getBean(mode);
        return service;
    }

    public static synchronized Service getService(String name) {
        Service service = null;
        if (serviceFactory == null) {
            getServiceFactory();
        }
        service = (Service) serviceFactory.getBean(name);
        return service;
    }

    /**
	 * ���serviceFactoryʵ��
	 * 
	 */
    private static void getServiceFactory() {
        Configs mainConfig = null;
        mainConfig = ConfigUtil.getConfig("club");
        if (mainConfig == null) {
            ConfigUtil.addConfig("club", "/club.properties");
            mainConfig = ConfigUtil.getConfig("club");
        }
        serviceFactory = (ServiceFactory) BeanUtil.getBean(mainConfig.getProperty("serviceFactory"));
    }
}
