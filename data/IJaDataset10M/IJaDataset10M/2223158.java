package org.uimafit.spring.factory;

import static org.uimafit.spring.util.ResourceInitializationUtil.initResource;
import java.util.Map;
import org.apache.uima.resource.Resource;
import org.apache.uima.resource.ResourceInitializationException;
import org.apache.uima.resource.ResourceSpecifier;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;

/**
 * @author Richard Eckart de Castilho
 */
public class CollectionReaderFactory_impl extends org.apache.uima.impl.CollectionReaderFactory_impl implements ApplicationContextAware {

    private ApplicationContext applicationContext;

    public void setApplicationContext(ApplicationContext aApplicationContext) throws BeansException {
        applicationContext = aApplicationContext;
    }

    @Override
    public Resource produceResource(Class<? extends Resource> aResourceClass, ResourceSpecifier aSpecifier, Map<String, Object> aAdditionalParams) throws ResourceInitializationException {
        Resource resource = super.produceResource(aResourceClass, aSpecifier, aAdditionalParams);
        return initResource(resource, applicationContext);
    }
}
