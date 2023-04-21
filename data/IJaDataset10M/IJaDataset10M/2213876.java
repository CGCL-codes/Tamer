package com.idna.riskengine.dao;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.fail;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import com.idna.common.utils.ClasspathXmlFileImporter;
import com.idna.riskengine.CorporateRequestXMLExtractor;
import com.idna.riskengine.domain.RiskEngRequestData;

@Ignore
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = { "classpath:test-config.xml", "classpath:database-config.xml", "classpath:dataHarvester-config.xml", "classpath:riskEngineIntegration-config.xml" })
public class RequestDataDaoTest {

    private Log logger = LogFactory.getLog(this.getClass());

    @Autowired
    private RequestDataDao<RiskEngRequestData> dao;

    @Autowired
    private CorporateRequestXMLExtractor requestXmlExtractor;

    private RiskEngRequestData data;

    @Before
    public void setUp() throws Exception {
        String xml = ClasspathXmlFileImporter.importXMLFromClasspath("FullPacket1.xml");
    }

    @Test
    public void testGetMatchingRequestData() {
        fail("Not yet implemented");
    }

    @Test
    public void testGetRequestData() {
        fail("Not yet implemented");
    }
}
