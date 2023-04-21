package org.travelfusion.xmlclient.ri.test.misc;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.TimeZone;
import junit.framework.Assert;
import org.junit.Test;
import org.travelfusion.xmlclient.handler.xmltool.XmlToolTemplateCache;
import org.travelfusion.xmlclient.module.XmlInputFactoryModule;
import org.travelfusion.xmlclient.ri.handler.misc.FindAlternativeFlightsHandler;
import org.travelfusion.xmlclient.ri.xobject.misc.XFindAlternativeFlightsItem;
import org.travelfusion.xmlclient.ri.xobject.misc.XFindAlternativeFlightsItem.CodeType;
import org.travelfusion.xmlclient.ri.xobject.misc.XFindAlternativeFlightsRequest;
import org.travelfusion.xmlclient.ri.xobject.misc.XFindAlternativeFlightsResponse;
import org.travelfusion.xmlclient.util.TfXAPIUtil;
import com.mycila.xmltool.XMLDoc;
import com.mycila.xmltool.XMLTag;

public class FindAlternativeFlightsTest {

    private final DateFormat DFT = new SimpleDateFormat(TfXAPIUtil.TRAVELFUSION_TRIPPLANNER_XML_DATE_FORMAT);

    {
        DFT.setTimeZone(TimeZone.getTimeZone("GMT"));
    }

    private static final XmlInputFactoryModule INSTANCE = new XmlInputFactoryModule();

    @Test
    public void findAlternativeFlightsRequest() throws Exception {
        XFindAlternativeFlightsRequest request = new XFindAlternativeFlightsRequest();
        request.setCurrency("GBP");
        request.setDestination("BER");
        request.setDestinationType(CodeType.CITY);
        request.setOrigin("LON");
        request.setOriginType(CodeType.CITY);
        request.setMaxDaysOffset(10);
        request.setOutwardDay(DFT.parse("15/10/2010-00:00"));
        request.setReturnDay(DFT.parse("20/10/2010-00:00"));
        FindAlternativeFlightsHandler handler = new FindAlternativeFlightsHandler();
        handler.setRequest(request);
        handler.setTemplate(new XmlToolTemplateCache().getTemplate(handler.getClass(), request.getClass()));
        String requestString = handler.handleRequest(null);
        XMLTag requestXml = XMLDoc.from(requestString, true);
        assertXmlEquals(requestXml, "/CommandList/FindAlternativeFlights/Currency", "GBP");
        assertXmlEquals(requestXml, "/CommandList/FindAlternativeFlights/Destination", "BER");
        assertXmlEquals(requestXml, "/CommandList/FindAlternativeFlights/DestinationType", "city");
        assertXmlEquals(requestXml, "/CommandList/FindAlternativeFlights/Origin", "LON");
        assertXmlEquals(requestXml, "/CommandList/FindAlternativeFlights/OriginType", "city");
        assertXmlEquals(requestXml, "/CommandList/FindAlternativeFlights/MaxDaysOffset", "10");
        assertXmlDateEquals(requestXml, "/CommandList/FindAlternativeFlights/OutwardDay", "15/10/2010");
        assertXmlDateEquals(requestXml, "/CommandList/FindAlternativeFlights/ReturnDay", "20/10/2010");
    }

    @Test
    public void findAlternativeFlightsResponse() throws Exception {
        FindAlternativeFlightsHandler handler = new FindAlternativeFlightsHandler();
        handler.setResponseReader(INSTANCE.getFactory().createXMLStreamReader(FindAlternativeFlightsTest.class.getResourceAsStream("FindAlternativeFlights.xml")));
        XFindAlternativeFlightsResponse response = handler.handleResponse();
        Assert.assertEquals(81, response.getItemList().size());
        assertFirstItem(response.getItemList().get(0));
        assertTwentiethItem(response.getItemList().get(19));
    }

    private void assertTwentiethItem(XFindAlternativeFlightsItem item) throws Exception {
        Assert.assertEquals("LON", item.getOrigin());
        Assert.assertEquals("LTN", item.getOriginAirport());
        Assert.assertEquals("PAR", item.getDestination());
        Assert.assertEquals("CDG", item.getDestinationAirport());
        Assert.assertEquals((double) 103.47, item.getPrice());
        Assert.assertEquals(DFT.parse("04/11/2010-20:04"), item.getTimestamp());
        Assert.assertEquals("U2", item.getOutwardLeg().getOperatorCode());
        Assert.assertEquals("easyJet", item.getOutwardLeg().getOperatorName());
        Assert.assertEquals("ezy", item.getOutwardLeg().getSupplier());
        Assert.assertEquals("U22431", item.getOutwardLeg().getFlightNo());
        Assert.assertEquals(DFT.parse("09/11/2010-07:55"), item.getOutwardLeg().getDepartDate());
        Assert.assertEquals(DFT.parse("09/11/2010-10:10"), item.getOutwardLeg().getArriveDate());
        Assert.assertEquals("U2", item.getReturnLeg().getOperatorCode());
        Assert.assertEquals("easyJet", item.getReturnLeg().getOperatorName());
        Assert.assertEquals("ezy", item.getReturnLeg().getSupplier());
        Assert.assertEquals("U22432", item.getReturnLeg().getFlightNo());
        Assert.assertEquals(DFT.parse("08/12/2010-10:40"), item.getReturnLeg().getDepartDate());
        Assert.assertEquals(DFT.parse("08/12/2010-10:50"), item.getReturnLeg().getArriveDate());
    }

    private void assertFirstItem(XFindAlternativeFlightsItem item) throws Exception {
        Assert.assertEquals("LON", item.getOrigin());
        Assert.assertEquals("LTN", item.getOriginAirport());
        Assert.assertEquals("PAR", item.getDestination());
        Assert.assertEquals("CDG", item.getDestinationAirport());
        Assert.assertEquals((double) 126.12, item.getPrice());
        Assert.assertEquals(DFT.parse("04/11/2010-20:45"), item.getTimestamp());
        Assert.assertEquals("U2", item.getOutwardLeg().getOperatorCode());
        Assert.assertEquals("easyJet", item.getOutwardLeg().getOperatorName());
        Assert.assertEquals("ezy", item.getOutwardLeg().getSupplier());
        Assert.assertEquals("U22433", item.getOutwardLeg().getFlightNo());
        Assert.assertEquals(DFT.parse("13/11/2010-06:50"), item.getOutwardLeg().getDepartDate());
        Assert.assertEquals(DFT.parse("13/11/2010-09:05"), item.getOutwardLeg().getArriveDate());
        Assert.assertEquals("U2", item.getReturnLeg().getOperatorCode());
        Assert.assertEquals("easyJet", item.getReturnLeg().getOperatorName());
        Assert.assertEquals("ezy", item.getReturnLeg().getSupplier());
        Assert.assertEquals("U22432", item.getReturnLeg().getFlightNo());
        Assert.assertEquals(DFT.parse("08/12/2010-10:40"), item.getReturnLeg().getDepartDate());
        Assert.assertEquals(DFT.parse("08/12/2010-10:50"), item.getReturnLeg().getArriveDate());
    }

    /**
   * Asserts if <code>toAssert</code> equals the content in <code>xml</code> at the location <code>tag</code>.
   * 
   * @param xml
   * @param tag
   * @param toAssert
   */
    private void assertXmlEquals(XMLTag xml, String tag, String toAssert) {
        xml.gotoTag(tag);
        Assert.assertEquals(toAssert, xml.getText());
    }

    /**
   * Asserts if <code>toAssert</code> equals the content in <code>xml</code> at the location <code>tag</code>, where
   * <code>toAssert</code> is a Date.
   * 
   * @param xml
   * @param tag
   * @param toAssert
   */
    private void assertXmlDateEquals(XMLTag xml, String tag, String toAssert) {
        xml.gotoTag(tag);
        String text = xml.getText();
        int indexOfTime = text.indexOf("-");
        Assert.assertEquals(toAssert, text.substring(0, indexOfTime));
    }
}
