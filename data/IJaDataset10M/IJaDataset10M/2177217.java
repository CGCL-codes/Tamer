package it.pronetics.madstore.crawler.spring;

import it.pronetics.madstore.common.configuration.spring.AbstractMadStoreConfigurationFactoryBean;
import it.pronetics.madstore.common.configuration.spring.MadStoreConfigurationBean;
import it.pronetics.madstore.crawler.CrawlerConfiguration;
import it.pronetics.madstore.crawler.Pipeline;
import it.pronetics.madstore.crawler.impl.CrawlerConfigurationImpl;
import java.util.LinkedList;
import java.util.List;

/**
 * Spring-based <code>FactoryBean</code> for creating a list of {@link it.pronetics.madstore.crawler.CrawlerConfiguration} objects
 * out of the {@link it.pronetics.madstore.common.configuration.spring.MadStoreConfigurationBean}.
 *
 * @author Salvatore Incandela
 */
public class CrawlerConfigurationsFactoryBean extends AbstractMadStoreConfigurationFactoryBean {

    public Object getObject() throws Exception {
        MadStoreConfigurationBean madstoreConfiguration = getMadStoreConfiguration();
        List<CrawlerConfiguration> crawlerConfigurationsResult = new LinkedList<CrawlerConfiguration>();
        List<MadStoreConfigurationBean.CrawlerConfiguration> crawlerConfigurations = madstoreConfiguration.getCrawlerConfigurations();
        for (MadStoreConfigurationBean.CrawlerConfiguration tmpConfig : crawlerConfigurations) {
            CrawlerConfiguration crawlerConfiguration = new CrawlerConfigurationImpl();
            crawlerConfiguration.setServer(tmpConfig.getHostName());
            crawlerConfiguration.setStartLink(tmpConfig.getStartLink());
            crawlerConfiguration.setMaxConcurrentDownloads(tmpConfig.getMaxConcurrentDownloads());
            crawlerConfiguration.setMaxVisitedLinks(tmpConfig.getMaxVisitedLinks());
            crawlerConfiguration.setPipeline(getPipeline(tmpConfig));
            crawlerConfigurationsResult.add(crawlerConfiguration);
        }
        return crawlerConfigurationsResult;
    }

    public Class getObjectType() {
        return List.class;
    }

    public boolean isSingleton() {
        return true;
    }

    private Pipeline getPipeline(MadStoreConfigurationBean.CrawlerConfiguration tmpConfig) {
        String pipelineName = tmpConfig.getPipelineName();
        if (pipelineName != null) {
            return getUniqueBean(pipelineName);
        } else {
            return getUniqueBean(Pipeline.class);
        }
    }
}
