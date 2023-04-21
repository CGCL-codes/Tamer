package com.idna.riskengine;

import static org.junit.Assert.*;
import java.io.Serializable;
import java.util.Map;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import com.idna.common.utils.ClasspathXmlFileImporter;
import com.idna.common.utils.CorporateRequestXPath;
import com.idna.riskengine.domain.RequestSource;
import com.idna.riskengine.domain.RiskEngRequestData;
import com.idna.riskengine.domain.XmlToDbMapper;

/**
 * 
 * Integration tests for the extraction sub-component. 
 * 
 * There are two different xsd's which requests can conform to so we test two different request packets.
 * 
 * Also note that this test does not consider the Request Identification Data, which is dealt with by a separate decorating sub-component.
 * 
 * @author matthew.cosgrove
 *
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = "classpath:risk-dataHarvester-config.xml")
public class CorporateRequestXMLExtractorTests {

    @Autowired
    private CorporateRequestXMLExtractor requestExtractor;

    int expectedSize = 22;

    private String searchXml;

    private String riskXml;

    private RiskEngRequestData requestData;

    private static Log logger = LogFactory.getLog(CorporateRequestXMLExtractorTests.class);

    @Before
    public void setUp() throws Exception {
        searchXml = ClasspathXmlFileImporter.importXMLFromClasspath("FullPacket1.xml");
        riskXml = ClasspathXmlFileImporter.importXMLFromClasspath("FullRiskPacket1.xml");
    }

    @Test
    public void testFullPacket1() throws Exception {
        requestData = requestExtractor.extractCorporateRequestParameters(searchXml, RequestSource.Search);
        logger.info("requestParameters returned: " + requestData.size());
        assertTrue("Returned map is null ", requestData != null);
        assertTrue("Request Param Search ID should be null as it is dealt with by a separate decorating sub-component", requestData.get(XmlToDbMapper.KEY_FIELD_SEARCH_ID) == null);
        assertTrue("Request Param YourReference is null ", requestData.get("YourReference") != null);
        assertTrue("Request Param Forename is null ", requestData.get("//Search/Person/Name/Forename") != null);
    }

    @Test
    public void testNumberOfExtractedElements() throws Exception {
        requestData = requestExtractor.extractCorporateRequestParameters(searchXml, RequestSource.Search);
        logger.info("requestParameters returned: " + requestData.size());
        assertTrue("Returned map size was " + requestData.size() + " not expected size of " + expectedSize, requestData.size() == expectedSize);
    }

    @Test
    public void testFullPacket1PopulatesDomainRiskEngineRequestDataMap() throws Exception {
        RiskEngRequestData requestData = requestExtractor.extractCorporateRequestParameters(searchXml, RequestSource.Search);
        System.err.println("requestData: " + requestData);
        assertEquals("Forename incorrect", "Elizabeth", requestData.get("//Search/Person/Name/Forename"));
        assertEquals("Surname incorrect", "Storey", requestData.get("//Search/Person/Name/Surname"));
        assertEquals("Gender incorrect", "F", requestData.get("//Search/Person/Gender"));
        assertEquals("Telephone incorrect", "02085619203", requestData.get("//Search/Telephones/Telephone/Number"));
        assertEquals("Premise incorrect", "3", requestData.get("//Search/Addresses/Address/Premise"));
        assertEquals("Postcode incorrect", "BA13 3BN", requestData.get("//Search/Addresses/Address/Postcode"));
        assertEquals("Address Country code incorrect", "GBR", requestData.get("//Search/Addresses/Address/CountryCode"));
        assertEquals("Nationality incorrect", "British", requestData.get("Nationality"));
        assertEquals("DateOfBirth incorrect", "1961-10-04", requestData.get("//Search/Person/DateOfBirth"));
        assertTrue("TransactionDate is not in format expected", ((String) requestData.get("TransactionDate")).contains("2009-05-12"));
        assertEquals("BINNumber incorrect", "123443", requestData.get("BINNumber"));
        assertEquals("CardLast4Digits incorrect", "4321", requestData.get("CardLast4Digits"));
        assertEquals("Delivery Postcode incorrect", "BA13 3BN", requestData.get("//Search/Orders/Order/Delivery/Address/Postcode"));
        assertEquals("Delviery Premise incorrect", "3", requestData.get("//Search/Orders/Order/Delivery/Address/Premise"));
        assertEquals("Delivery Country code incorrect", "FRA", requestData.get("//Search/Orders/Order/Delivery/Address/CountryCode"));
        assertEquals("IPAddress incorrect", "128.154.23.19", requestData.get("IPAddress"));
        assertEquals("Email incorrect", "elizabeth.storey@hotmail.com", requestData.get("Email"));
        assertEquals("Transaction amount incorrect", "3.00", requestData.get("//Search/Orders/Order/Payment/Cards/Card/Transaction/TransactionAmount/Amount"));
        assertEquals("Currency incorrect", "GBP", requestData.get("Currency"));
    }

    @Test
    public void testFullRiskPacket1PopulatesDomainRiskEngineRequestDataMap() throws Exception {
        RiskEngRequestData requestData = requestExtractor.extractCorporateRequestParameters(riskXml, RequestSource.Fraud);
        System.err.println("requestData: " + requestData);
        assertEquals("Forename incorrect", "Elizabeth", requestData.get("//RiskEngine/RiskLogs/RiskLog/Person/Name/Forename"));
        assertEquals("Surname incorrect", "Storey", requestData.get("//RiskEngine/RiskLogs/RiskLog/Person/Name/Surname"));
        assertEquals("Gender incorrect", "F", requestData.get("//RiskEngine/RiskLogs/RiskLog/Person/Gender"));
        assertEquals("Telephone incorrect", "02085619203", requestData.get("//RiskEngine/RiskLogs/RiskLog/Telephones/Telephone/Number"));
        assertEquals("Premise incorrect", "3", requestData.get("//RiskEngine/RiskLogs/RiskLog/Addresses/Address/Premise"));
        assertEquals("Postcode incorrect", "BA13 3BN", requestData.get("//RiskEngine/RiskLogs/RiskLog/Addresses/Address/Postcode"));
        assertEquals("Address Country code incorrect", "GBR", requestData.get("//RiskEngine/RiskLogs/RiskLog/Addresses/Address/CountryCode"));
        assertEquals("Nationality incorrect", "British", requestData.get("Nationality"));
        assertEquals("DateOfBirth incorrect", "1961-10-04", requestData.get("//RiskEngine/RiskLogs/RiskLog/Person/DateOfBirth"));
        assertTrue("TransactionDate is not in format expected", ((String) requestData.get("TransactionDate")).contains("2009-05-12"));
        assertEquals("BINNumber incorrect", "123443", requestData.get("BINNumber"));
        assertEquals("CardLast4Digits incorrect", "4321", requestData.get("CardLast4Digits"));
        assertEquals("Delivery Postcode incorrect", "BA13 3BN", requestData.get("//RiskEngine/RiskLogs/RiskLog/Orders/Order/Delivery/Address/Postcode"));
        assertEquals("Delviery Premise incorrect", "3", requestData.get("//RiskEngine/RiskLogs/RiskLog/Orders/Order/Delivery/Address/Premise"));
        assertEquals("Delivery Country code incorrect", "FRA", requestData.get("//RiskEngine/RiskLogs/RiskLog/Orders/Order/Delivery/Address/CountryCode"));
        assertEquals("IPAddress incorrect", "128.154.23.19", requestData.get("IPAddress"));
        assertEquals("Email incorrect", "elizabeth.storey@hotmail.com", requestData.get("Email"));
        assertEquals("Transaction amount incorrect", "3.00", requestData.get("//RiskEngine/RiskLogs/RiskLog/Orders/Order/Payment/Cards/Card/Transaction/TransactionAmount/Amount"));
        assertEquals("Currency incorrect", "GBP", requestData.get("Currency"));
    }

    @Test
    public void testDoesNotReturnUnneededElements() throws Exception {
        requestData = requestExtractor.extractCorporateRequestParameters(searchXml, RequestSource.Search);
        assertNotSame("Credit Card Number not persistable", "1234432112344321", requestData.get(CorporateRequestXPath.ORDERS_ORDER_PAYMENT_CARDS_CARD_NUMBER));
    }

    @After
    public void tearDown() throws Exception {
        requestData = null;
    }
}
