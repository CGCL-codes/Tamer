package org.netbeams.dsp.message;

import java.io.File;
import java.io.FileWriter;
import junit.framework.TestCase;
import org.netbeams.dsp.data.property.DSProperties;
import org.netbeams.dsp.data.property.DSProperty;
import org.netbeams.dsp.demo.mouseactions.ButtonName;
import org.netbeams.dsp.demo.mouseactions.EventName;
import org.netbeams.dsp.demo.mouseactions.MouseAction;
import org.netbeams.dsp.demo.mouseactions.MouseActionsContainer;
import org.netbeams.dsp.demo.stock.StockTick;
import org.netbeams.dsp.demo.stock.StockTicks;
import org.netbeams.dsp.util.DSPXMLUnmarshaller;
import org.netbeams.dsp.util.NetworkUtil;
import org.netbeams.dsp.ysi.SondeDataContainer;
import org.netbeams.dsp.ysi.SondeDataType;

public class JaxbMessagesMarshallUnmarshallTest extends TestCase {

    public String a;

    private DSPMessagesFactory fac;

    private File xmlObjectPersisted = new File("messagesExample-dsp.xml");

    public JaxbMessagesMarshallUnmarshallTest() {
        this.fac = DSPMessagesFactory.INSTANCE;
        System.out.println(xmlObjectPersisted.getAbsolutePath());
    }

    /**
     * Verifies if a message container with different messages from different DSP components can be
     * marshalled.
     */
    public void testMarshallMessagesContainerWithDifferentMessages() {
        ComponentIdentifier prod = this.fac.makeDSPComponentIdentifier("1", "LOCAL", "org.netbeams.dsp.platform.management.component.ComponentManager");
        ComponentIdentifier cons = this.fac.makeDSPComponentIdentifier(null, "LOCAL", "org.netbeams.dsp.demo.stocks.consumer");
        Header header = this.fac.makeDSPMessageHeader(null, prod, cons);
        StockTicks ticks = new StockTicks();
        StockTick tk1 = new StockTick();
        tk1.setName("Google, Inc.");
        tk1.setSymbol("GOOG");
        tk1.setValue((float) 23.4);
        ticks.getStockTick().add(tk1);
        MeasureMessage stocks = this.fac.makeDSPMeasureMessage(header, ticks);
        ComponentIdentifier prod3 = this.fac.makeDSPComponentIdentifier("1234", "LOCAL", "org.netbeams.dsp.platform.management.component.ComponentManager");
        ComponentIdentifier cons3 = this.fac.makeDSPComponentIdentifier(null, "LOCAL", "org.netbeams.dsp.wiretransport.client");
        Header header3 = this.fac.makeDSPMessageHeader(null, prod3, cons3);
        DSProperties properties = new DSProperties();
        DSProperty serverIp = new DSProperty();
        serverIp.setName("WIRE_TRANSPORT_SERVER_IP");
        serverIp.setValue("127.0.0.1");
        properties.getProperty().add(serverIp);
        DSProperty serverPort = new DSProperty();
        serverPort.setName("WIRE_TRANSPORT_SERVER_PORT");
        serverPort.setValue("8080");
        properties.getProperty().add(serverPort);
        DSProperty serverUri = new DSProperty();
        serverUri.setName("HTTP_SERVER_BASE_URI");
        serverUri.setValue("/transportDspMessages");
        properties.getProperty().add(serverUri);
        DSProperty serverVar = new DSProperty();
        serverVar.setName("HTTP_SERVER_REQUEST_VARIABLE");
        serverVar.setValue("dspMessagesContainer");
        properties.getProperty().add(serverVar);
        UpdateMessage updates = null;
        updates = this.fac.makeDSPUpdateMessage(header3, properties);
        ComponentIdentifier cons2 = this.fac.makeDSPComponentIdentifier(null, "192.168.0.101", "org.netbeams.dsp.mouseactions");
        Header header2 = this.fac.makeDSPMessageHeader(null, prod, cons2);
        MouseActionsContainer macontainer = new MouseActionsContainer();
        MouseAction ma = new MouseAction();
        ma.setButton(ButtonName.CENTER);
        ma.setEvent(EventName.CLICKED);
        ma.setX(3);
        ma.setY(5);
        MouseAction ma1 = new MouseAction();
        ma1.setButton(ButtonName.NONE);
        ma1.setEvent(EventName.MOVED);
        ma1.setX(4);
        ma1.setY(7);
        macontainer.getMouseAction().add(ma);
        macontainer.getMouseAction().add(ma1);
        MeasureMessage mouseActionsMessage = null;
        mouseActionsMessage = this.fac.makeDSPMeasureMessage(header2, macontainer);
        String destIpAddress = NetworkUtil.getCurrentEnvironmentNetworkIp();
        SondeDataContainer sondeContainer = new SondeDataContainer();
        SondeDataType sData = new SondeDataType();
        sData.setDateTime("2009/12/27", "14:23:44");
        sData.setCond(Float.parseFloat("35.4"));
        sData.setBattery(Float.parseFloat("377"));
        sData.setPH(Float.parseFloat("845"));
        sData.setPress(Float.parseFloat("443"));
        sondeContainer.getSondeData().add(sData);
        MeasureMessage sondeMeasurements = this.fac.makeDSPMeasureMessage(header3, sondeContainer);
        MessagesContainer messages = this.fac.makeDSPMessagesContainer(destIpAddress);
        messages.getMessage().add(sondeMeasurements);
        messages.getMessage().add(updates);
        messages.getMessage().add(mouseActionsMessage);
        try {
            FileWriter fileWriter = new FileWriter(this.xmlObjectPersisted);
            fileWriter.append(messages.toXml());
            fileWriter.flush();
            fileWriter.close();
            assertTrue("Marshalled object doesn't exist at " + this.xmlObjectPersisted.getAbsolutePath(), this.xmlObjectPersisted.exists());
        } catch (Exception e) {
            e.printStackTrace();
            fail(e.getMessage());
        }
    }

    public void testUnmashallMessagesContainerWithDifferentMessages() {
        try {
            MessagesContainer container = DSPXMLUnmarshaller.INSTANCE.unmarshallStream(this.xmlObjectPersisted);
            assertNotNull("The messages container was not unmarshalled", container);
            assertNotNull("The messages container must not be null", container.getMessage());
            assertEquals("The messages container must have 3 messages", 3, container.getMessage().size());
            for (AbstractMessage dspMsg : container.getMessage()) {
                MessageContent obj = dspMsg.getBody().getAny();
                if (dspMsg.getContentType().equals("org.netbeams.dsp.ysi")) {
                    SondeDataContainer sondeData = (SondeDataContainer) obj;
                    assertNotNull("The payload was not unmashalled", sondeData);
                    assertNotNull("The list of stock ticks is null", sondeData.getSondeData());
                    assertEquals("The list of sonde data must have one element", 1, sondeData.getSondeData().size());
                    for (SondeDataType sttk : sondeData.getSondeData()) {
                        assertEquals("The Battery level is incorrect", "full-Power", sttk.getBattery());
                        assertEquals("The Conditions is incorrect", "blue skies", sttk.getCond());
                        assertEquals("The Pressure is incorrect", "too-much", sttk.getPress());
                    }
                } else if (dspMsg.getContentType().equals("org.netbeams.dsp.data.property")) {
                    DSProperties propts = (DSProperties) obj;
                    assertNotNull("The payload was not unmashalled", propts);
                    assertNotNull("The list of properties is null", propts.getProperty());
                    assertEquals("The list properties must have one element", 4, propts.getProperty().size());
                    for (DSProperty dsProp : propts.getProperty()) {
                        assertNotNull("The DSP property is null", dsProp);
                        assertNotNull("The name of the property is null", dsProp.getName());
                        assertNotNull("The value of the property is null", dsProp.getValue());
                    }
                } else if (dspMsg.getContentType().equals("org.netbeams.dsp.demo.stock")) {
                    StockTicks ticks = (StockTicks) obj;
                    assertNotNull("The payload was not unmashalled", ticks);
                    assertNotNull("The list of stock ticks is null", ticks.getStockTick());
                    assertEquals("The list of stock ticks must have one element", 1, ticks.getStockTick().size());
                    for (StockTick sttk : ticks.getStockTick()) {
                        assertEquals("The name of the stock is different", "Google, Inc.", sttk.getName());
                    }
                } else if (dspMsg.getContentType().equals("org.netbeams.dsp.demo.mouseactions")) {
                    MouseActionsContainer mouseacts = (MouseActionsContainer) obj;
                    assertNotNull("The payload was not unmashalled", mouseacts);
                    assertNotNull("The list of stock ticks is null", mouseacts.getMouseAction());
                    assertEquals("The list of stock ticks must have one element", 2, mouseacts.getMouseAction().size());
                    for (MouseAction sttk : mouseacts.getMouseAction()) {
                        assertNotNull("The mouse action is null", sttk);
                    }
                }
            }
            if (this.xmlObjectPersisted.exists()) {
                this.xmlObjectPersisted.delete();
            }
        } catch (Exception e) {
            e.printStackTrace();
            fail(e.getMessage());
        }
    }

    public static void changeMe(JaxbMessagesMarshallUnmarshallTest change) {
        change = new JaxbMessagesMarshallUnmarshallTest();
        JaxbMessagesMarshallUnmarshallTest b = new JaxbMessagesMarshallUnmarshallTest();
        change = b;
    }

    public static void changeMe(String a) {
        a = "Changed";
    }

    public static void main(String[] args) {
        JaxbMessagesMarshallUnmarshallTest obj = new JaxbMessagesMarshallUnmarshallTest();
        obj.a = "Ashwin";
        String a = "Marcello";
        changeMe(obj);
        String ccc = "Ashwin";
        changeMe(ccc);
        System.out.println(obj.a);
    }
}
