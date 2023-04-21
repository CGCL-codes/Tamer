package net.fourinfo.gateway.model;

import java.io.ByteArrayOutputStream;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

/**
 * Represent a phone number unblock request. This request should be
 * constructed, then the getXml() method should be called to get HTTP body that
 * should be POSTed to the Gateway.
 * 
 * @author Garth Patil <a href="mailto:g@4info.net">g@4info.net</a>
 */
public class UnblockRequest extends GenericRequest {

    protected Address address;

    public UnblockRequest(String clientId, String clientKey) {
        super(clientId, clientKey);
        address = new Address();
    }

    /**
     * @return the carrier
     */
    public Carrier getCarrier() {
        return this.address.getCarrier();
    }

    /**
     * @param carrier
     *            the carrier to set
     */
    public void setCarrier(Carrier carrier) {
        this.address.setCarrier(carrier);
    }

    /**
     * @return the mSISDN
     */
    public String getMSISDN() {
        return this.address.getPhoneNumber();
    }

    /**
     * MSISDN (phone number): This is the standard internationalized format for
     * phone numbers. The 4INFO Messaging Gateway only accepts numbers in this
     * format for US handsets. The format is: +<country code><national number>
     * For example, the US number (650) 814-5269 would be represented as
     * +16508145269
     * 
     * Use the <code>setPhoneNumber()</code> method to submit a localized
     * phone number string, which will set the MSISDN automatically.
     * 
     * @param msisdn
     *            the MSISDN to set
     */
    public void setMSISDN(String msisdn) {
        this.address.setPhoneNumber(msisdn);
    }

    /**
     * Set the country code, plus phone number. This will strip out non-digit
     * numbers.
     * 
     * @param phoneNumber
     */
    public void setPhoneNumber(String countryCode, String phoneNumber) {
        StringBuffer msisdn = new StringBuffer("+");
        msisdn.append(countryCode);
        final char[] numbers = phoneNumber.toCharArray();
        for (int x = 0; x < numbers.length; x++) {
            final char c = numbers[x];
            if ((c >= '0') && (c <= '9')) msisdn.append(c);
        }
        setMSISDN(msisdn.toString());
    }

    /**
     * Set the phone number. This method accepts a pretty-printed phone number
     * string like "(202) 555-1212", and converts it to the appropriate MSISDN
     * string.
     * 
     * NOTE: currently it only works for US phone number, as it adds "+1" to the
     * number string.
     * 
     * 
     * @param phoneNumber
     */
    public void setPhoneNumber(String phoneNumber) {
        setPhoneNumber("1", phoneNumber);
    }

    /**
     * Render request xml:
     * @return the xml as a string
     */
    public byte[] getXmlByteArray() {
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        try {
            DocumentBuilder builder = factory.newDocumentBuilder();
            Document document = builder.newDocument();
            Element type = (Element) document.createElement("type");
            type.appendChild(document.createTextNode("5"));
            Element msisdn = (Element) document.createElement("id");
            msisdn.appendChild(document.createTextNode(this.address.getPhoneNumber()));
            Element pName = (Element) document.createElement("name");
            pName.appendChild(document.createTextNode("CARRIER"));
            Element pValue = (Element) document.createElement("value");
            pValue.appendChild(document.createTextNode(this.address.getCarrier().getId().toString()));
            Element property = (Element) document.createElement("property");
            property.appendChild(pName);
            property.appendChild(pValue);
            Element recipient = (Element) document.createElement("recipient");
            recipient.appendChild(type);
            recipient.appendChild(msisdn);
            recipient.appendChild(property);
            Element unblock = (Element) document.createElement("unblock");
            unblock.appendChild(recipient);
            Element root = (Element) document.createElement("request");
            root.setAttribute("clientId", clientId);
            root.setAttribute("clientKey", clientKey);
            root.setAttribute("type", "UNBLOCK");
            root.appendChild(unblock);
            document.appendChild(root);
            document.setXmlVersion("1.0");
            document.normalizeDocument();
            TransformerFactory transformerFactory = TransformerFactory.newInstance();
            Transformer transformer = transformerFactory.newTransformer();
            transformer.setOutputProperty(OutputKeys.INDENT, "no");
            transformer.setOutputProperty(OutputKeys.ENCODING, "UTF-8");
            ByteArrayOutputStream buff = new ByteArrayOutputStream();
            transformer.transform(new DOMSource(document), new StreamResult(buff));
            return buff.toByteArray();
        } catch (ParserConfigurationException pce) {
            pce.printStackTrace();
        } catch (TransformerConfigurationException tce) {
            tce.printStackTrace();
        } catch (TransformerException te) {
            te.printStackTrace();
        }
        return null;
    }
}
