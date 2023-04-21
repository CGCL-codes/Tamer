package issrg.test.multi;

import issrg.utils.handler.EncodeXML;
import issrg.utils.handler.Handler;
import issrg.utils.handler.HandlerServiceException;
import issrg.saml.util.XMLReader;
import issrg.pba.Subject;
import org.w3c.dom.*;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.util.*;
import java.io.*;
import org.apache.log4j.*;

/**
 * this is to test the multiple token parser mechanism.
 *
 * @author Linying Su
 */
public class MultiTokenParser {

    static Logger logger = Logger.getRootLogger();

    private static PrintStream out = System.out;

    /** Creates a new instance of MultiTokenParser */
    public MultiTokenParser() {
    }

    public static void main(String[] args) {
        System.setProperty("line.separator", "\r\n");
        Document doc = null;
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        factory.setValidating(false);
        try {
            doc = factory.newDocumentBuilder().newDocument();
        } catch (ParserConfigurationException pe) {
            out.println("error:" + pe);
        }
        try {
            out = new PrintStream(new FileOutputStream(args[2]));
        } catch (IOException ioe) {
            out.println("This shouldn't have happened! " + ioe.getMessage());
        }
        try {
            issrg.utils.handler.Config name = new issrg.utils.handler.Config();
            Handler service = new Handler();
            service.initialise(args[0]);
            Element msg = doc.createElement("Request");
            msg.setAttribute("xmlns", "urn:oasis:names:tc:xacml:1.0:context");
            msg.setAttribute("xmlns:xsi", "http://www.w3.org/2001/XMLSchema-instance");
            Element sub = doc.createElement("Subject");
            Element att = doc.createElement("Attribute");
            att.setAttribute("AttributeId", "urn:oasis:names:tc:xacml:1.0:subject:subject-id");
            att.setAttribute("DataType", "http://www.w3.org/2001/XMLSchema#string");
            Element attVal = doc.createElement("AttributeValue");
            Text val = doc.createTextNode("/c=gb/o=Permis/cn=User");
            attVal.appendChild(val);
            att.appendChild(attVal);
            Element att1 = doc.createElement("Attribute");
            att1.setAttribute("AttributeId", "permis:permisRole");
            att1.setAttribute("DataType", "http://www.w3.org/2001/XMLSchema#string");
            Element attVal1 = doc.createElement("AttributeValue");
            Text val1 = doc.createTextNode("RA");
            attVal1.appendChild(val1);
            att1.appendChild(attVal1);
            sub.appendChild(att);
            sub.appendChild(att1);
            msg.appendChild(sub);
            out.println(new EncodeXML().encode(msg, 0));
            Element saml = doc.createElementNS("urn:oasis:names:tc:SAML:2.0:protocol", "samlp:Response");
            saml.setAttributeNS("http://www.w3.org/2000/xmlns/", "xmlns:samlp", "urn:oasis:names:tc:SAML:2.0:protocol");
            saml.setAttributeNS(null, "ID", "_7a78f78c-6989-4fc0-893c-53f7fbc0fed9");
            saml.setAttributeNS(null, "InResponseTo", "VOMS_SAML_Query_-1867389794");
            saml.setAttributeNS(null, "IssueInstant", "2008-02-26T21:14:09.428Z");
            saml.setAttributeNS(null, "Version", "2.0");
            Element iss = doc.createElementNS("urn:oasis:names:tc:SAML:2.0:assertion", "saml:Issuer");
            iss.setAttributeNS("http://www.w3.org/2000/xmlns/", "xmlns:saml", "urn:oasis:names:tc:SAML:2.0:assertion");
            Text val2 = doc.createTextNode("CN=omii002.cnaf.infn.it,L=CNAF,OU=Host,O=INFN,C=IT");
            iss.appendChild(val2);
            Element sta = doc.createElement("Status");
            Element cod = doc.createElement("StatusCode");
            cod.setAttribute("Value", "urn:oasis:names:tc:SAML:2.0:status:Success");
            sta.appendChild(cod);
            Element ass = doc.createElementNS("urn:oasis:names:tc:SAML:2.0:assertion", "saml:Assertion");
            ass.setAttributeNS("http://www.w3.org/2000/xmlns/", "xmlns:saml", "urn:oasis:names:tc:SAML:2.0:assertion");
            ass.setAttributeNS(null, "ID", "_167f2fa0-bd18-4c21-8221-86baee8b33f1");
            ass.setAttributeNS(null, "IssueInstant", "2008-02-26T21:14:09.427Z");
            ass.setAttributeNS(null, "Version", "2.0");
            Element iss1 = doc.createElementNS("urn:oasis:names:tc:SAML:2.0:assertion", "saml:Issuer");
            Text val3 = doc.createTextNode("CN=A Permis Test User,O=PERMIS,C=GB");
            iss1.appendChild(val3);
            Element subj = doc.createElementNS("urn:oasis:names:tc:SAML:2.0:assertion", "saml:Subject");
            Element nameid = doc.createElementNS("urn:oasis:names:tc:SAML:2.0:assertion", "saml:NameID");
            nameid.setAttributeNS(null, "Format", "urn:oasis:names:tc:SAML:1.1:nameid-format:X509SubjectName");
            Text val4 = doc.createTextNode("CN=User,O=Permis,C=gb");
            nameid.appendChild(val4);
            subj.appendChild(nameid);
            Element con = doc.createElementNS("urn:oasis:names:tc:SAML:2.0:assertion", "saml:Conditions");
            con.setAttributeNS(null, "NotBefore", "2008-02-26T21:14:09.428Z");
            con.setAttributeNS(null, "NotOnOrAfter", "2018-02-27T09:14:09.428Z");
            Element attSta = doc.createElementNS("urn:oasis:names:tc:SAML:2.0:assertion", "saml:AttributeStatement");
            Element attr = doc.createElementNS("urn:oasis:names:tc:SAML:2.0:assertion", "saml:Attribute");
            attr.setAttributeNS(null, "Name", "permisRole");
            attr.setAttributeNS(null, "NameFormat", "urn:oasis:names:tc:SAML:2.0:attrname-format:unspecified");
            Element attrVal = doc.createElementNS("urn:oasis:names:tc:SAML:2.0:assertion", "saml:AttributeValue");
            attrVal.setAttributeNS("http://www.w3.org/2000/xmlns/", "xmlns:xsi", "http://www.w3.org/2001/XMLSchema-instance");
            attrVal.setAttributeNS("", "xsi:type", "xs:string");
            Text val5 = doc.createTextNode("/omiieurope/NeSC");
            attrVal.appendChild(val5);
            Element attrVal1 = doc.createElementNS("urn:oasis:names:tc:SAML:2.0:assertion", "saml:AttributeValue");
            attrVal1.setAttributeNS("http://www.w3.org/2000/xmlns/", "xmlns:xsi", "http://www.w3.org/2001/XMLSchema-instance");
            attrVal1.setAttributeNS("", "xsi:type", "xs:string");
            Text val6 = doc.createTextNode("/omiieurope");
            attrVal1.appendChild(val6);
            attr.appendChild(attrVal);
            attr.appendChild(attrVal1);
            attSta.appendChild(attr);
            ass.appendChild(iss1);
            ass.appendChild(subj);
            ass.appendChild(con);
            ass.appendChild(attSta);
            saml.appendChild(iss);
            saml.appendChild(sta);
            saml.appendChild(ass);
            String trust = new EncodeXML().encode(saml, 0);
            out.println(trust);
            String fName = name.getURL(args[1]);
            File f = new File(fName);
            byte[] b = new byte[(int) f.length()];
            new FileInputStream(f).read(b);
            Object[] creds = new Object[1];
            creds[0] = b;
            String[] parsers = { "issrg.pba.rbac.x509.RoleBasedACParser" };
            Subject subject = service.createSubject(msg, "cn=A Permis Test User,o=permis,c=gb", creds, parsers);
            Element xacml = service.getSubjectXACML(subject);
            String xml = new EncodeXML().encode(xacml, 0);
            out.println(xml);
        } catch (Exception e) {
            out.println("error : " + e);
        }
    }
}
