package org.apache.axis2.schema.enumeration;

import junit.framework.TestCase;
import org.apache.axiom.om.OMAbstractFactory;
import org.apache.axiom.om.OMElement;
import org.apache.axiom.om.util.StAXUtils;
import org.tempuri.CountPlacesInRect;
import org.tempuri.PlaceType;
import javax.xml.stream.XMLStreamReader;
import java.io.ByteArrayInputStream;

public class EnumerationTest extends TestCase {

    public void testEnumeration() {
        CountPlacesInRect countPlacesInRect = new CountPlacesInRect();
        countPlacesInRect.setLowerright("lowerright");
        countPlacesInRect.setUpperleft("upperleft");
        countPlacesInRect.setPtype(PlaceType.River);
        try {
            OMElement omElement = countPlacesInRect.getOMElement(CountPlacesInRect.MY_QNAME, OMAbstractFactory.getOMFactory());
            String omElementString = omElement.toStringWithConsume();
            System.out.println("OM String ==> " + omElementString);
            XMLStreamReader xmlReader = StAXUtils.createXMLStreamReader(new ByteArrayInputStream(omElementString.getBytes()));
            CountPlacesInRect newObject = CountPlacesInRect.Factory.parse(xmlReader);
            assertEquals(countPlacesInRect.getPtype(), PlaceType.River);
        } catch (Exception e) {
            assertFalse(true);
        }
    }
}
