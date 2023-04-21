package com.idna.riskengine;

import java.util.UUID;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.jmock.Expectations;
import org.jmock.Mockery;
import org.jmock.integration.junit4.JMock;
import org.jmock.integration.junit4.JUnit4Mockery;
import org.springframework.integration.core.Message;
import org.springframework.integration.message.MessageBuilder;
import com.idna.common.utils.ClasspathXmlFileImporter;
import com.idna.riskengine.domain.RequestIdentification;
import com.idna.riskengine.domain.RequestSource;
import com.idna.riskengine.domain.RiskEngRequestData;
import com.idna.riskengine.domain.RiskEngineRequestIdentification;
import com.idna.riskengine.service.RequestDataService;

@Ignore
@RunWith(JMock.class)
public class RiskEngineRequestPersisterServiceTest {

    Mockery context = new JUnit4Mockery();

    RiskEngineRequestPersisterServiceImpl riskEngineRequestPersisterService;

    RequestPopulator requestPopulator = context.mock(RequestPopulator.class);

    RequestDataService requestDataService = context.mock(RequestDataService.class);

    String corporateRequestXML;

    Message<String> mes;

    RiskEngineRequestIdentification requestMetaData;

    static final String REQUEST_ID = UUID.randomUUID().toString();

    static final String LOGIN_ID = UUID.randomUUID().toString();

    static final RequestSource REQUEST_SOURCE = RequestSource.Search;

    @Before
    public void setUp() throws Exception {
        riskEngineRequestPersisterService = new RiskEngineRequestPersisterServiceImpl();
        riskEngineRequestPersisterService.setRequestPopulator(requestPopulator);
        riskEngineRequestPersisterService.setRequestDataService(requestDataService);
        corporateRequestXML = ClasspathXmlFileImporter.importXMLFromClasspath("FullPacket1.xml");
        mes = MessageBuilder.withPayload(corporateRequestXML).setHeader(RequestIdentification.KEY_IDENTIFIER_REQUEST_ID, REQUEST_ID).setHeader(RequestIdentification.KEY_IDENTIFIER_LOGIN_ID, LOGIN_ID).setHeader(RequestSource.keyField(), RequestSource.Search.toString()).build();
        requestMetaData = new RiskEngineRequestIdentification(REQUEST_ID, LOGIN_ID, REQUEST_SOURCE);
    }

    @Test
    public void aRequestIsParsedToRetrieveAppropriateParamsWhichAreThenPersisted() {
        context.checking(new Expectations() {

            {
                oneOf(requestPopulator).populateRequest(corporateRequestXML, requestMetaData);
                will(returnValue(new RiskEngRequestData(REQUEST_SOURCE)));
            }
        });
        context.checking(new Expectations() {

            {
                oneOf(requestDataService);
            }
        });
        riskEngineRequestPersisterService.persistRequest(mes);
    }
}
