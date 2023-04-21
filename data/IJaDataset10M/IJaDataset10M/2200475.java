package org.isurf.spmbl.cep;

import java.util.Random;
import org.fosstrak.ale.xsd.ale.epcglobal.ECReports;
import org.isurf.spmbl.jms.JMSSender;
import org.isurf.spmiddleware.UnitUtils;
import org.isurf.spmiddleware.model.LLRPEvent;
import org.isurf.spmiddleware.utils.XMLUtils;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;
import org.llrp.ltk.exceptions.InvalidLLRPMessageException;
import org.llrp.ltk.generated.messages.KEEPALIVE;

public class JMSCEPReceiverIT {

    @BeforeClass
    public static void setUpBeforeClass() throws Exception {
    }

    @AfterClass
    public static void tearDownAfterClass() throws Exception {
    }

    @Test
    public void testCEPUsingXML() throws InterruptedException, InvalidLLRPMessageException {
        JMSSender processor = JMSSender.getJMSSender();
        for (int i = 0; i < 1; i++) {
            final ECReports ale = UnitUtils.createECReports("urn:epc:id:sgtin:2057000.123430.0030");
            ale.getECSpec().getLogicalReaders().getLogicalReader().remove("AlienReader");
            ale.getECSpec().getLogicalReaders().getLogicalReader().add("PRP01");
            Random random = new Random();
            ale.setTotalMilliseconds(random.nextInt());
            ale.setTerminationCondition("TRIGGER");
            String xml = XMLUtils.toXML(ale, "urn:epcglobal:ale:xsd:1");
            processor.send("observation.queue", xml);
            KEEPALIVE keepAlive = new KEEPALIVE();
            LLRPEvent event = new LLRPEvent();
            event.setLlrpMessage(keepAlive);
            event.setReader("R1");
            processor.send("observation.queue", event);
            Thread.sleep(1);
            System.out.println(i);
        }
    }

    @Test
    public void testCEPUsingBinary() throws InterruptedException, InvalidLLRPMessageException {
        JMSSender processor = JMSSender.getJMSSender();
        for (int i = 0; i < 1; i++) {
            final ECReports ale = UnitUtils.createECReports("urn:epc:id:sgtin:3157000.123430.0030");
            ale.getECSpec().getLogicalReaders().getLogicalReader().remove("AlienReader");
            ale.getECSpec().getLogicalReaders().getLogicalReader().add("PRP01");
            Random random = new Random();
            ale.setTotalMilliseconds(random.nextInt());
            ale.setTerminationCondition("TRIGGER");
            processor.send("observation.queue", ale);
            KEEPALIVE keepAlive = new KEEPALIVE();
            LLRPEvent event = new LLRPEvent();
            event.setLlrpMessage(keepAlive);
            event.setReader("R1");
            processor.send("observation.queue", event);
        }
    }

    @Test
    public void testEDBLUsingXML() throws InterruptedException, InvalidLLRPMessageException {
        JMSSender processor = JMSSender.getJMSSender();
        for (int i = 0; i < 1; i++) {
            final ECReports ale = UnitUtils.createECReports("urn:epc:id:sgtin:2057000.123430.003" + i);
            ale.getECSpec().getLogicalReaders().getLogicalReader().remove("AlienReader");
            ale.getECSpec().getLogicalReaders().getLogicalReader().add("PRP01");
            ale.setSpecName("Printing");
            Random random = new Random();
            ale.setTotalMilliseconds(random.nextInt());
            ale.setTerminationCondition("TRIGGER");
            String xml = XMLUtils.toXML(ale, "urn:epcglobal:ale:xsd:1");
            processor.publish("ale.topic", xml);
            Thread.sleep(1);
            System.out.println(i);
        }
    }

    @Test
    public void testEDBLUsingBinary() throws InterruptedException, InvalidLLRPMessageException {
        JMSSender processor = JMSSender.getJMSSender();
        for (int i = 0; i < 1; i++) {
            final ECReports ale = UnitUtils.createECReports("urn:epc:id:sgtin:8057000.123430.003" + i);
            ale.getECSpec().getLogicalReaders().getLogicalReader().remove("AlienReader");
            ale.getECSpec().getLogicalReaders().getLogicalReader().add("PRP01");
            Random random = new Random();
            ale.setTotalMilliseconds(random.nextInt());
            ale.setTerminationCondition("TRIGGER");
            processor.publish("ale.topic", ale);
        }
    }
}
