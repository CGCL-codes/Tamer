package com.hs.framework.service;

import com.hs.framework.BaseFactory;
import com.hs.framework.BaseModeFactory;
import com.hs.framework.Mode;
import com.hs.framework.common.util.BeanUtil;
import com.hs.framework.common.util.ConfigUtil;
import com.hs.framework.common.util.Configs;

public class ServiceFactory implements BaseFactory {

    private static final String SCORE = "score";

    public Object getBean(Mode mode) {
        return getModeFactory(mode).getBean(mode);
    }

    public Object getBean(String name) {
        String beanName = getServiceConfig().getProperty(name);
        return BeanUtil.getBean(beanName);
    }

    /**
	 * 返回�?个Service的实例�??
	 * 
	 * @param mode
	 * @return
	 */
    private Service getService(Mode mode) {
        return (Service) getBean(mode);
    }

    /**
	 * 通过mode获得ModeFactory的实例�??
	 * 
	 * @param mode
	 * @return
	 */
    private BaseModeFactory getModeFactory(Mode mode) {
        BaseModeFactory baseModeFactory = null;
        String modeID = mode.getModeID();
        if (modeID.equals(SCORE)) {
        }
        return baseModeFactory;
    }

    /**
	 * ���service�����ļ�ʵ��
	 * 
	 * @return
	 */
    private Configs getServiceConfig() {
        Configs result = null;
        String DAOConfigPath = null;
        result = ConfigUtil.getConfig("serviceConfig");
        if (result == null) {
            DAOConfigPath = ConfigUtil.getProperty("club", "serviceConfigPath");
            ConfigUtil.addConfig("serviceConfig", DAOConfigPath);
            result = ConfigUtil.getConfig("serviceConfig");
        }
        return result;
    }
}
