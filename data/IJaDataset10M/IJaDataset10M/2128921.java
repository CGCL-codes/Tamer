package test.org.mule.prova.agents;

import javax.jms.JMSException;
import javax.jms.ObjectMessage;
import org.junit.Test;
import org.mule.api.MuleException;
import org.mule.api.MuleMessage;
import org.mule.module.client.MuleClient;
import ws.prova.RMessage;

public class PetriStructuredDiscriminatorTest extends AbstractBasicTest {

    public void setUp() {
        setUp("mule-config-petri-structureddiscriminator.xml");
    }

    @Test
    public void testConnect() throws MuleException, JMSException {
        MuleClient client = new MuleClient();
        for (int i = 0; i < 5; i++) {
            MuleMessage inbound = client.request("vm://global", 1000000);
            assertNotNull(inbound);
            RMessage rMsg = null;
            if (inbound.getPayload() instanceof ObjectMessage) {
                rMsg = (RMessage) ((ObjectMessage) inbound.getPayload()).getObject();
            } else {
                rMsg = (RMessage) inbound.getPayload();
            }
            assertEquals(rMsg.performative(), "job_completed");
        }
    }
}
