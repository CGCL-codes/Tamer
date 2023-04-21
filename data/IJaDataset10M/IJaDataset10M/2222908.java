package org.openhealthexchange.openpixpdq.ihe.impl_v2;

import java.io.File;
import java.net.URL;
import java.util.Collection;
import junit.framework.TestCase;
import org.openhealthexchange.openpixpdq.ihe.IPixManagerAdapter;
import org.openhealthexchange.openpixpdq.ihe.audit.IheAuditTrail;
import org.openhealthexchange.openpixpdq.ihe.configuration.ConfigurationLoader;
import org.openhealthexchange.openpixpdq.ihe.log.IMessageStoreLogger;
import org.openhealthexchange.openpixpdq.ihe.log.MessageStore;
import ca.uhn.hl7v2.HL7Exception;
import ca.uhn.hl7v2.model.Message;
import ca.uhn.hl7v2.model.v231.datatype.ELD;
import ca.uhn.hl7v2.model.v231.message.ACK;
import ca.uhn.hl7v2.model.v231.segment.ERR;
import ca.uhn.hl7v2.parser.PipeParser;
import com.misyshealthcare.connect.net.IConnectionDescription;
import com.misyshealthcare.connect.util.OID;

/**
 * This class is used to testing the PixFeedHandler
 * @author Rasakannu Palaniyandi
 * @version dec 02, 2008
 * 
 
 */
public class TestPixFeed extends TestCase {

    private IConnectionDescription connection = null;

    private IPixManagerAdapter pixAdapter = null;

    private static ConfigurationLoader loader = null;

    private PixManager actor = null;

    public void setUp() {
        try {
            loader = ConfigurationLoader.getInstance();
            URL url = TestPixQuery.class.getResource("/tests/actors/IheActors.xml");
            File file = new File(url.toURI());
            loader.loadConfiguration(file, false, null, new OidMock(), null, null, null, new TestLogContext());
            Collection actors = loader.getActorDescriptions();
            ConfigurationLoader.ActorDescription pixqactor = loader.getDescriptionById("pixman");
            connection = pixqactor.getConnection();
            ConfigurationLoader.ActorDescription auditactor = loader.getDescriptionById("localaudit");
            IheAuditTrail auditTrail = new IheAuditTrail("localaudit", auditactor.getLogConnection());
            actor = new PixManager(connection, auditTrail, null, null);
            MockPixAdapter pixAdapter = new MockPixAdapter();
            actor.registerPixManagerAdapter(pixAdapter);
            IMessageStoreLogger storeLogger = new MockMessageStoreLogger();
            actor.setStoreLogger(storeLogger);
        } catch (Exception e) {
            e.printStackTrace();
            fail("Cannot load the actor property");
        }
    }

    /**
	 * Test PixFeedHandler: Invalid PixFeedHandler event.
	 */
    public void testInvalidPixFeedPatientAdmit() {
        String msh = "MSH|^~\\&|EHR_MISYS|MISYS|PAT_IDENTITY_X_REF_MGR_IBM1|IBM|20060809155816-0400||ADT^A03|PIF_0|P|2.3.1||||||||\r" + "EVN|A07|20060809155816||||20060809155814\r" + "PID|||12345678^^^HIMSS2005&1.3.6.1.4.1.21367.2005.1.1&ISO^PI||DePinto^Joe^V^Jr^Dr.|Wang|19580325|M|||||(716)385-6235^PRN^PH^^^716^3856235\r" + "PV1||O";
        PipeParser pipeParser = new PipeParser();
        try {
            Message msgIn = pipeParser.parse(msh);
            Message resposeMsg = new PixFeedHandler(actor).processMessage(msgIn);
            fail("processQuery() should've thrown exception");
        } catch (Exception e) {
        }
    }

    /**
	 * Test PixFeedHandler: ReceivingApplication is not recognized.
	 */
    public void testPatientAdmitReceivingApplication() {
        String msh = "MSH|^~\\&|EHR_MISYS|MISYS|PAT_IDENTITY_X_REF_MGR_IBM1|IBM|20060809155816-0400||ADT^A01|PIF_0|P|2.3.1||||||||\r" + "EVN|A07|20060809155816||||20060809155814\r" + "PID|||12345678^^^HIMSS2005&1.3.6.1.4.1.21367.2005.1.1&ISO^PI||DePinto^Joe^V^Jr^Dr.|Wang|19580325|M|||||(716)385-6235^PRN^PH^^^716^3856235\r" + "PV1||O";
        PipeParser pipeParser = new PipeParser();
        try {
            Message msgIn = pipeParser.parse(msh);
            Message resposeMsg = new PixFeedHandler(actor).processMessage(msgIn);
            ACK rMessage = getResponseMsg(resposeMsg);
            assertEquals("AE", rMessage.getMSA().getAcknowledgementCode().getValue());
            String returnValues = processPixQueryResponse(rMessage);
            String[] errorLog = returnValues.split(",");
            assertEquals("MSH", errorLog[0]);
            assertEquals("1", errorLog[1]);
            assertEquals("5", errorLog[2]);
            assertEquals("null", errorLog[3]);
            assertEquals("Unknown Receiving Application", errorLog[4]);
        } catch (Exception e) {
        }
    }

    /**
	 * Test PixFeedHandler: ReceivingFacility is not recognized.
	 */
    public void testPatientAdmitReceivingFacility() {
        String msh = "MSH|^~\\&|PAT_IDENTITY_X_REF_MGR_IBM1|IBM1|EHR_MISYS|IBM|20060809155816-0400||ADT^A01|PIF_0|P|2.3.1||||||||\r" + "EVN|A07|20060809155816||||20060809155814\r" + "PID|||12345678^^^HIMSS2005&1.3.6.1.4.1.21367.2005.1.1&ISO^PI||DePinto^Joe^V^Jr^Dr.|Wang|19580325|M|||||(716)385-6235^PRN^PH^^^716^3856235\r" + "PV1||O";
        PipeParser pipeParser = new PipeParser();
        try {
            Message msgIn = pipeParser.parse(msh);
            Message resposeMsg = new PixFeedHandler(actor).processMessage(msgIn);
            ACK rMessage = getResponseMsg(resposeMsg);
            assertEquals("AE", rMessage.getMSA().getAcknowledgementCode().getValue());
            String returnValues = processPixQueryResponse(rMessage);
            String[] errorLog = returnValues.split(",");
            assertEquals("MSH", errorLog[0]);
            assertEquals("1", errorLog[1]);
            assertEquals("6", errorLog[2]);
            assertEquals("null", errorLog[3]);
            assertEquals("Unknown Receiving Facility", errorLog[4]);
        } catch (Exception e) {
        }
    }

    /**
	 * Test PixFeedHandler: the request domain is not valid.
	 */
    public void testPatientAdmitRequestDomain() {
        String msh = "MSH|^~\\&|PAT_IDENTITY_X_REF_MGR_IBM1|IBM1|EHR_MISYS|MISYS|20060809155816-0400||ADT^A01|PIF_0|P|2.3.1||||||||\r" + "EVN|A07|20060809155816||||20060809155814\r" + "PID|||12345678^^^HIMSS2005&1.3.6.1.4.1.21367.2005.1.1&ISO^PI||DePinto^Joe^V^Jr^Dr.|Wang|19580325|M|||||(716)385-6235^PRN^PH^^^716^3856235\r" + "PV1||O";
        PipeParser pipeParser = new PipeParser();
        try {
            Message msgIn = pipeParser.parse(msh);
            Message resposeMsg = new PixFeedHandler(actor).processMessage(msgIn);
            ACK rMessage = getResponseMsg(resposeMsg);
            assertEquals("AE", rMessage.getMSA().getAcknowledgementCode().getValue());
            String returnValues = processPixQueryResponse(rMessage);
            String[] errorLog = returnValues.split(",");
            assertEquals("PID", errorLog[0]);
            assertEquals("1", errorLog[1]);
            assertEquals("3", errorLog[2]);
            assertEquals("204", errorLog[3]);
            assertEquals("Unknown Key Identifier", errorLog[4]);
        } catch (Exception e) {
        }
    }

    /**
	 * Test PixFeedHandler: successful patient creation.
	 */
    public void testPatientAdmitSuccess() {
        String msh = "MSH|^~\\&|CLINREG|WESTCLIN|EHR_MISYS|MISYS|20060809155816-0400||ADT^A01|PIF_0|P|2.3.1||||||||\r" + "EVN|A01|20060809155816||||20060809155814\r" + "PID|||112236^^^WESTCLINIC&1.2234.634325.5734&ISO^PI||DePinto^Joe^V^Jr^Dr.|Wang|19580325|M|raja|^race^^^^|||(716)385-6235^PRN^PH^^^716^3856235||tamil|^U^^^^|^hindu^^^^|077746567576|666777888|9877876|priya|^ethnic^^^^|tamilnadu||1|^indian^^^^||||090120071011|1|\r" + "PV1||O";
        PipeParser pipeParser = new PipeParser();
        try {
            Message msgIn = pipeParser.parse(msh);
            Message resposeMsg = new PixFeedHandler(actor).processMessage(msgIn);
            ACK rMessage = getResponseMsg(resposeMsg);
            assertEquals("AA", rMessage.getMSA().getAcknowledgementCode().getValue());
        } catch (Exception e) {
        }
    }

    /**
	 * Test PixFeedHandler: the request Id is not valid for patient update.
	 */
    public void testUpdatePatientRequestId() {
        String msh = "MSH|^~\\&|PAT_IDENTITY_X_REF_MGR_IBM1|IBM1|EHR_MISYS|MISYS|20060809155816-0400||ADT^A08^ADT_A08|PIF_0|P|2.3.1||||||||\r" + "EVN|A08|20060809155816||||20060809155814\r" + "PID|||123456789^^^WESTCLINIC&1.2234.634325.5734&ISO^PI||DePinto^Joe^V^Jr^Dr.|Wang|19580325|M|||||(716)385-6235^PRN^PH^^^716^3856235\r" + "PV1||O";
        PipeParser pipeParser = new PipeParser();
        try {
            Message msgIn = pipeParser.parse(msh);
            Message resposeMsg = new PixFeedHandler(actor).processMessage(msgIn);
            ACK rMessage = getResponseMsg(resposeMsg);
            assertEquals("AE", rMessage.getMSA().getAcknowledgementCode().getValue());
            String returnValues = processPixQueryResponse(rMessage);
            String[] errorLog = returnValues.split(",");
            assertEquals("PID", errorLog[0]);
            assertEquals("1", errorLog[1]);
            assertEquals("3", errorLog[2]);
            assertEquals("204", errorLog[3]);
            assertEquals("Unknown Key Identifier", errorLog[4]);
        } catch (Exception e) {
        }
    }

    /**
	 * Test PixFeedHandler: successful patient update.
	 */
    public void testUpdatePatientSuccess() {
        String msh = "MSH|^~\\&|PAT_IDENTITY_X_REF_MGR_IBM1|IBM1|EHR_MISYS|MISYS|20060809155816-0400||ADT^A08^ADT_A08|PIF_0|P|2.3.1||||||||\r" + "EVN|A08|20060809155816||||20060809155814\r" + "PID|||12345678^^^WESTCLINIC&1.2234.634325.5734&ISO^PI||DePinto^Joe^V^Jr^Dr.|Wang|19580325|M|raja|race|||(716)385-6235^PRN^PH^^^716^3856235||tamil|U|hindu|077746567576|666777888|9877876|priya|ethnic|tamilnadu||1|indian||||090120071011|1|\r" + "PV1||O";
        PipeParser pipeParser = new PipeParser();
        try {
            Message msgIn = pipeParser.parse(msh);
            Message resposeMsg = new PixFeedHandler(actor).processMessage(msgIn);
            ACK rMessage = getResponseMsg(resposeMsg);
            assertEquals("AA", rMessage.getMSA().getAcknowledgementCode().getValue());
        } catch (Exception e) {
            e.printStackTrace();
            fail(e.getMessage());
        }
    }

    /**
	 * Test PixFeedHandler : invalid Merge patient id.
	 */
    public void testInvalidMergePatient() {
        String msh = "MSH|^~\\&|PAT_IDENTITY_X_REF_MGR_IBM1|IBM1|EHR_MISYS|MISYS|20060919004624-0400||ADT^A40^ADT_A39|PIF_3|P|2.3.1||||||||\r" + "EVN|A40|20060919004624||||20060919004340\r" + "PID|||12345678^^^WESTCLINIC&1.2234.634325.5734&ISO^PI||DePinto^Joe^V^Jr^Dr.|Wang|19580325|M|raja|race|||(716)385-6235^PRN^PH^^^716^3856235||tamil|U|hindu|077746567576|666777888|9877876|priya|ethnic|tamilnadu||1|indian||||090120071011|1|\r" + "MRG|445233^^^MIEH^PI||||||Tusona^Luis^N^Sr^Dr.\r";
        PipeParser pipeParser = new PipeParser();
        try {
            Message msgIn = pipeParser.parse(msh);
            Message resposeMsg = new PixFeedHandler(actor).processMessage(msgIn);
            ACK rMessage = getResponseMsg(resposeMsg);
            assertEquals("AE", rMessage.getMSA().getAcknowledgementCode().getValue());
            assertEquals("MRG", rMessage.getERR().getErrorCodeAndLocation(0).getSegmentID().getValue());
            assertEquals("1", rMessage.getERR().getErrorCodeAndLocation(0).getFieldPosition().getValue());
            assertEquals("1", rMessage.getERR().getErrorCodeAndLocation(0).getSequence().getValue());
        } catch (Exception e) {
            e.printStackTrace();
            fail(e.getMessage());
        }
    }

    /**
	 * Test PixFeedHandler : successful patient Merge
	 */
    public void testPixFeedMergeSuccess() {
        String msh = "MSH|^~\\&|PAT_IDENTITY_X_REF_MGR_IBM1|IBM1|EHR_MISYS|MISYS|20060919004624-0400||ADT^A40^ADT_A39|PIF_3|P|2.3.1||||||||\r" + "EVN|A40|20060919004624||||20060919004340\r" + "PID|||12345678^^^WESTCLINIC&1.2234.634325.5734&ISO^PI||DePinto^Joe^V^Jr^Dr.|Wang|19580325|M|raja|race|||(716)385-6235^PRN^PH^^^716^3856235||tamil|U|hindu|077746567576|666777888|9877876|priya|ethnic|tamilnadu||1|indian||||090120071011|1|\r" + "MRG|536253^^^MIEH^PI||||||Tusona^Luis^N^Sr^Dr.\r";
        PipeParser pipeParser = new PipeParser();
        try {
            Message msgIn = pipeParser.parse(msh);
            Message resposeMsg = new PixFeedHandler(actor).processMessage(msgIn);
            ACK rMessage = getResponseMsg(resposeMsg);
            assertEquals("AA", rMessage.getMSA().getAcknowledgementCode().getValue());
        } catch (Exception e) {
            e.printStackTrace();
            fail(e.getMessage());
        }
    }

    private ACK getResponseMsg(Message response) {
        ACK message = null;
        if (response instanceof ACK) {
            message = (ACK) response;
        } else {
            String error = "Unexpected response from PIX server \"" + connection.getDescription() + "\"";
        }
        return message;
    }

    private String processPixQueryResponse(ACK message) throws HL7Exception {
        String status = message.getMSA().getAcknowledgementCode().getValue();
        if ((status == null) || (!status.equalsIgnoreCase("AA") && !status.equalsIgnoreCase("CA"))) {
            return getErrorMsg(message.getERR());
        }
        return status;
    }

    private String getErrorMsg(ERR msgIn) throws HL7Exception {
        ELD erl = msgIn.getErrorCodeAndLocation(0);
        String errorMsg = erl.getSegmentID().getValue() + "," + erl.getSequence().getValue() + "," + erl.getFieldPosition().getValue() + "," + erl.getCodeIdentifyingError().getIdentifier().getValue() + "," + erl.getCodeIdentifyingError().getText().getValue();
        return errorMsg;
    }

    public class OidMock implements OID.OidSource {

        public synchronized String generateId() {
            return Long.toString(System.currentTimeMillis());
        }
    }
}
