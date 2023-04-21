package org.yum.service.impl;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.BeanFactoryAware;
import org.yum.dao.impl.BaseDAO;
import org.yum.util.YumException;

/**
 * base service
 * 
 * @author zhhan
 */
public class BaseService implements BeanFactoryAware {

    /**
	 * service log instance
	 */
    protected final Log logger = LogFactory.getLog(getClass());

    private BeanFactory beanFactory;

    /**
	 * @see org.springframework.beans.factory.BeanFactoryAware#setBeanFactory(org.springframework.beans.factory.BeanFactory)
	 */
    public final void setBeanFactory(BeanFactory beanFactory) throws BeansException {
        this.beanFactory = beanFactory;
    }

    /**
	 * get dao
	 * 
	 * @param daoName
	 * @return
	 * @throws YumException
	 */
    protected BaseDAO getDAO(String daoName) throws YumException {
        Object obj = beanFactory.getBean(daoName);
        if (obj == null) {
            throw new YumException(daoName + " bean isn't found");
        }
        if (!(obj instanceof BaseDAO)) {
            throw new YumException(daoName + " bean isn't dao");
        }
        return (BaseDAO) obj;
    }
}
