package com.idna.dm.util;

import java.util.UUID;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.BeansException;
import org.springframework.beans.MutablePropertyValues;
import org.springframework.beans.PropertyValue;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.beans.factory.config.BeanFactoryPostProcessor;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
import com.idna.dm.logging.activity.domain.DecisionMatrixLog;
import com.idna.dm.logging.activity.domain.LogMessage;
import com.idna.dm.logging.activity.logger.ActivityLogger;
import com.idna.dm.logging.activity.logger.LogReadChannel;
import com.idna.dm.logging.activity.logger.LogWriteChannel;
import com.idna.dm.logging.activity.logger.impl.AsyncActivityLogger;
import com.idna.dm.util.reflection.ObjectFieldParser;

/**
 * This class is used to replace a spring managed bean with a custom bean at 
 * runtime without having to rewrite your application  context. VERY handy for testing.
 * 
 * @author gawain.hammond
 *
 */
public class BeanReplacementUtility implements BeanFactoryPostProcessor {

    private String newClassName;

    private String beanNameToReplace;

    ConfigurableListableBeanFactory configurableListableBeanFactory;

    /**
	 * Method used by spring to intercept and work with beans before they are instantiated. 
	 */
    @Override
    public void postProcessBeanFactory(ConfigurableListableBeanFactory arg0) throws BeansException {
        configurableListableBeanFactory = arg0;
        BeanDefinition bd = configurableListableBeanFactory.getBeanDefinition(beanNameToReplace);
        bd.setBeanClassName(newClassName);
    }

    public void setNewClassName(String newClassName) {
        this.newClassName = newClassName;
    }

    public void setBeanNameToReplace(String beanNameToReplace) {
        this.beanNameToReplace = beanNameToReplace;
    }
}
