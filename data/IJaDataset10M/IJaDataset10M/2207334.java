package org.modelibra.persistency.xml.test1;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import org.junit.BeforeClass;
import org.junit.Test;
import org.modelibra.config.DomainConfig;
import org.modelibra.config.DomainsConfig;
import org.modelibra.config.ModelConfig;
import org.modelibra.config.ModelibraProperties;

public class XmlConfigTest {

    private static XmlConfig xmlConfig;

    @BeforeClass
    public static void beforeTests() throws Exception {
        xmlConfig = new XmlConfig();
    }

    @Test
    public void getModelibraProperties() throws Exception {
        ModelibraProperties modelibraProperties = xmlConfig.getModelibraProperties();
        assertNotNull(modelibraProperties);
    }

    @Test
    public void getDomainsConfig() throws Exception {
        DomainsConfig domainsConfig = xmlConfig.getDomainsConfig();
        assertNotNull(domainsConfig);
    }

    @Test
    public void getDomainConfig() throws Exception {
        DomainConfig domainConfig = xmlConfig.getDomainConfig("Xml", "Specific");
        assertNotNull(domainConfig);
    }

    @Test
    public void getPackagePrefix() throws Exception {
        DomainConfig domainConfig = xmlConfig.getDomainConfig("Xml", "Specific");
        String packafePrefix = domainConfig.getPackagePrefix();
        assertEquals("org.modelibra.persistency", packafePrefix);
    }

    @Test
    public void getModelConfig() throws Exception {
        DomainConfig domainConfig = xmlConfig.getDomainConfig("Xml", "Specific");
        ModelConfig modelConfig = domainConfig.getModelConfig("Notes");
        assertNotNull(modelConfig);
    }

    @Test
    public void getDataRelativePath() throws Exception {
        DomainConfig domainConfig = xmlConfig.getDomainConfig("Xml", "Specific");
        ModelConfig modelConfig = domainConfig.getModelConfig("Notes");
        String dataRelativePath = modelConfig.getPersistenceRelativePath();
        String pathFromModelConfig = "test/org/modelibra/persistency/xml/test1";
        assertEquals(pathFromModelConfig, dataRelativePath);
    }
}
