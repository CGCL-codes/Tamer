package org.impalaframework.spring.service.exporter;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.BeansException;

/**
 * See {@link NamedServiceAutoExportPostProcessor}
 * 
 * @author Phil Zoio
 * @deprecated Use {@link NamedServiceAutoExportPostProcessor} instead
 */
@Deprecated
public class ModuleContributionPostProcessor extends NamedServiceAutoExportPostProcessor {

    Log logger = LogFactory.getLog(ModuleContributionPostProcessor.class);

    @Override
    public Object maybeExportBean(Object bean, String beanName) throws BeansException {
        System.out.println("*************** WARNING ***************");
        String message = "You are using " + ModuleContributionPostProcessor.class.getName() + ". This class is deprecated and will be removed in the next release. Use " + NamedServiceAutoExportPostProcessor.class.getName() + " or the 'auto-export' element from the Impala 'service' namespace";
        System.out.println(message);
        logger.warn(message);
        System.out.println("*************** WARNING ***************");
        return super.maybeExportBean(bean, beanName);
    }
}
