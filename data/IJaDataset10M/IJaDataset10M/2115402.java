package org.openconcerto.modules.finance.payment.ebics.request;

import java.io.File;
import java.io.IOException;
import java.io.StringWriter;
import java.math.BigInteger;
import java.security.GeneralSecurityException;
import java.security.KeyStoreException;
import java.security.cert.CertificateEncodingException;
import java.security.interfaces.RSAPublicKey;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Source;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import javax.xml.transform.stream.StreamSource;
import javax.xml.validation.SchemaFactory;
import org.apache.poi.util.HexDump;
import org.bouncycastle.util.encoders.Base64;
import org.openconcerto.modules.finance.payment.ebics.EbicsConfiguration;
import org.w3c.dom.DOMImplementation;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.xml.sax.ErrorHandler;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import org.xml.sax.SAXParseException;
import org.xml.sax.XMLReader;

public class SignaturePubKeyOrderData {

    private final EbicsConfiguration config;

    private Date date;

    private BigInteger modulus;

    private BigInteger exponent;

    private String certificate;

    private static SimpleDateFormat sdfIso8601 = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSZ", Locale.US);

    /**
     * Public key used for electronic signature
     * 
     * @param encodedX509Certificate can be null (except for French bank)
     * */
    public SignaturePubKeyOrderData(EbicsConfiguration config, Date date, BigInteger modulus, BigInteger publicExponent, String encodedX509Certificate) {
        this.config = config;
        this.date = date;
        this.modulus = modulus;
        this.exponent = publicExponent;
        this.certificate = encodedX509Certificate;
        System.out.println("SignaturePubKeyOrderData: modulus");
        System.out.println(HexDump.toHex(this.modulus.toByteArray()));
    }

    /**
     * Public key used for electronic signature
     * 
     * @param encodedX509Certificate can be null (except for French bank)
     * @throws KeyStoreException
     * @throws CertificateEncodingException
     * */
    public SignaturePubKeyOrderData(EbicsConfiguration config) throws GeneralSecurityException, IOException {
        this.config = config;
        final RSAPublicKey signatureKey = (RSAPublicKey) config.getSignatureCertificate().getPublicKey();
        this.date = config.getKeyGenerationDate();
        this.modulus = signatureKey.getModulus();
        this.exponent = signatureKey.getPublicExponent();
        this.certificate = new String(Base64.encode(config.getSignatureCertificate().getEncoded()));
    }

    public Document getXMLDocument() {
        try {
            final DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
            final DocumentBuilder builder = factory.newDocumentBuilder();
            final DOMImplementation impl = builder.getDOMImplementation();
            final Document doc = impl.createDocument(null, null, null);
            final Element eSignaturePubKeyOrderData = doc.createElementNS("http://www.ebics.org/S001", "SignaturePubKeyOrderData");
            eSignaturePubKeyOrderData.setAttribute("xmlns:xsi", "http://www.w3.org/2001/XMLSchema-instance");
            eSignaturePubKeyOrderData.setAttribute("xmlns:ds", "http://www.w3.org/2000/09/xmldsig#");
            eSignaturePubKeyOrderData.setAttribute("xsi:schemaLocation", "http://www.ebics.org/S001 http://www.ebics.org/S001/ebics_signature.xsd");
            doc.appendChild(eSignaturePubKeyOrderData);
            final Element eSignaturePubKeyInfo = doc.createElement("SignaturePubKeyInfo");
            eSignaturePubKeyOrderData.appendChild(eSignaturePubKeyInfo);
            if (this.certificate != null) {
                final Element eX509Data = doc.createElement("ds:X509Data");
                final Element eX509IssuerSerial = doc.createElement("ds:X509IssuerSerial");
                eX509Data.appendChild(eX509IssuerSerial);
                final Element eX509IssuerName = doc.createElement("ds:X509IssuerName");
                eX509IssuerName.setTextContent("CN=OpenConcerto");
                eX509IssuerSerial.appendChild(eX509IssuerName);
                final Element eX509SerialNumber = doc.createElement("ds:X509SerialNumber");
                eX509SerialNumber.setTextContent("01");
                eX509IssuerSerial.appendChild(eX509SerialNumber);
                eSignaturePubKeyInfo.appendChild(eX509Data);
                final Element eX509Certificate = doc.createElement("ds:X509Certificate");
                eX509Certificate.setTextContent(this.certificate);
                eX509Data.appendChild(eX509Certificate);
            }
            final Element ePubKeyValue = doc.createElement("PubKeyValue");
            eSignaturePubKeyInfo.appendChild(ePubKeyValue);
            final Element eRSAKeyValue = doc.createElement("ds:RSAKeyValue");
            final Element eModulus = doc.createElement("ds:Modulus");
            eModulus.setTextContent(new String(Base64.encode(this.modulus.toByteArray())));
            eRSAKeyValue.appendChild(eModulus);
            final Element eExponent = doc.createElement("ds:Exponent");
            eExponent.setTextContent(new String(Base64.encode(this.exponent.toByteArray())));
            eRSAKeyValue.appendChild(eExponent);
            ePubKeyValue.appendChild(eRSAKeyValue);
            final Element eTypeStamp = doc.createElement("TimeStamp");
            eTypeStamp.setTextContent(getFormatedDate());
            ePubKeyValue.appendChild(eTypeStamp);
            final Element eSignatureVersion = doc.createElement("SignatureVersion");
            eSignatureVersion.setTextContent("A005");
            eSignaturePubKeyInfo.appendChild(eSignatureVersion);
            final Element ePartnerID = doc.createElement("PartnerID");
            ePartnerID.setTextContent(this.config.getPartner().getPartnerId());
            eSignaturePubKeyOrderData.appendChild(ePartnerID);
            final Element eUserID = doc.createElement("UserID");
            eUserID.setTextContent(this.config.getUser().getUserId());
            eSignaturePubKeyOrderData.appendChild(eUserID);
            return doc;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public String getFormatedDate() {
        return getFormatedDate(this.date);
    }

    public static String getFormatedDate(Date date) {
        String string = sdfIso8601.format(date);
        if (string.endsWith("00")) {
            string = string.substring(0, string.length() - 2) + ":00";
        }
        return string;
    }

    public static Date getDate(String string) throws ParseException {
        if (string.endsWith(":00")) {
            string = string.substring(0, string.length() - 3) + "00";
        }
        return sdfIso8601.parse(string);
    }

    public String getXML() {
        try {
            DOMSource domSource = new DOMSource(getXMLDocument());
            TransformerFactory tf = TransformerFactory.newInstance();
            Transformer transformer = tf.newTransformer();
            transformer.setOutputProperty(OutputKeys.OMIT_XML_DECLARATION, "yes");
            transformer.setOutputProperty(OutputKeys.METHOD, "xml");
            transformer.setOutputProperty(OutputKeys.ENCODING, "UTF-8");
            transformer.setOutputProperty("{http://xml.apache.org/xslt}indent-amount", "2");
            transformer.setOutputProperty(OutputKeys.INDENT, "yes");
            StringWriter sw = new StringWriter();
            StreamResult sr = new StreamResult(sw);
            transformer.transform(domSource, sr);
            String xml = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n" + sw.toString();
            return xml;
        } catch (Exception e) {
            e.printStackTrace();
            return e.getMessage();
        }
    }

    public void validate() throws Exception {
        System.setProperty("jaxp.debug", "1");
        File schemaLocation = new File("schema/ebics_signature.xsd");
        File xmlLocation = new File("test/ebics/INI_order_data.xml");
        try {
            SAXParserFactory factory = SAXParserFactory.newInstance();
            factory.setValidating(false);
            factory.setNamespaceAware(true);
            SchemaFactory schemaFactory = SchemaFactory.newInstance("http://www.w3.org/2001/XMLSchema");
            SAXParser parser = null;
            try {
                factory.setSchema(schemaFactory.newSchema(new Source[] { new StreamSource(schemaLocation.getAbsolutePath()) }));
                parser = factory.newSAXParser();
            } catch (SAXException se) {
                System.err.println("SCHEMA : " + se.getMessage());
                se.printStackTrace();
                return;
            }
            XMLReader reader = parser.getXMLReader();
            reader.setErrorHandler(new ErrorHandler() {

                public void warning(SAXParseException e) throws SAXException {
                    System.out.println("WARNING: " + e.getMessage());
                }

                public void error(SAXParseException e) throws SAXException {
                    System.out.println("ERROR : " + e.getMessage());
                    throw e;
                }

                public void fatalError(SAXParseException e) throws SAXException {
                    System.out.println("FATAL : " + e.getMessage());
                    throw e;
                }
            });
            reader.parse(new InputSource(xmlLocation.getAbsolutePath()));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
