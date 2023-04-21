package net.sf.clairv.index;

import java.util.Collection;
import net.sf.clairv.index.processor.ResourceProcessor;
import net.sf.clairv.index.resource.Resource;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.BeanFactoryUtils;
import org.springframework.beans.factory.NoSuchBeanDefinitionException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.context.support.FileSystemXmlApplicationContext;

/**
 * @author qiuyin
 *
 */
public class IndexerContext {

    private static final Log log = LogFactory.getLog(IndexerContext.class);

    protected ApplicationContext context;

    public IndexerContext(String configFile) {
        if (configFile.startsWith("classpath:")) {
            context = new ClassPathXmlApplicationContext(configFile.substring(10));
        } else {
            context = new FileSystemXmlApplicationContext(configFile);
        }
    }

    public Collection getResources() {
        return BeanFactoryUtils.beansOfTypeIncludingAncestors(context, Resource.class).values();
    }

    public ResourceProcessor getResourceProcessor() {
        try {
            return (ResourceProcessor) BeanFactoryUtils.beanOfTypeIncludingAncestors(context, ResourceProcessor.class);
        } catch (NoSuchBeanDefinitionException e) {
            log.error("No single bean of type " + ResourceProcessor.class.getName() + " is found");
            throw e;
        }
    }
}
