package verinec.adaptation;

import verinec.adaptation.Translator;
import verinec.adaptation.repository.TranslationRepFactory;
import verinec.util.VerinecNamespaces;
import org.jdom.Document;
import org.jdom.Element;
import org.jdom.input.SAXBuilder;
import org.jdom.output.XMLOutputter;
import org.jdom.output.Format;
import junit.framework.TestCase;
import java.io.StringReader;
import java.util.List;

/** Test case for the Translator. 
  * 
  * @author david.buchmann at unifr.ch
  * @version $Revision: 824 $
  */
public class WinTranslatorTest extends TestCase {

    private Element globals;

    private Element pc01;

    private Element pc02;

    private Element pc03;

    private String snodes = "<?xml version='1.0' ?>\n" + "<nodes " + VerinecNamespaces.schemaLocationAttrString + " xmlns='" + VerinecNamespaces.SCHEMA_NODE + "' " + "xmlns:gui='" + VerinecNamespaces.SCHEMA_GUI + "'  xmlns:tr='" + VerinecNamespaces.SCHEMA_TRANSLATION + "'>\n" + "<tr:typedef def-type-id='typ01' def-target-id='tar01'>" + "<tr:type name='testtype' id='typ01'>" + "<tr:service name='ethernet' translation='windows-xp' />" + "</tr:type>" + "<tr:target name='target 01' id='tar01'>" + "<tr:wmi host='.'/>" + "</tr:target>" + "</tr:typedef>" + "<node hostname='pc01'>" + "<tr:nodetype>" + "<tr:service name='ethernet' translation='windows-xp'><tr:target name='test-pc01'><tr:wmi host='pc01' /></tr:target></tr:service>" + "</tr:nodetype>" + "<hardware>" + "<ethernet name='ethernet 0' hwaddress='11:11:11:11:11:11'><ethernet-binding id='ebna3zt6'>" + "<nw id='i60' address='192.168.0.55' subnet='255.255.255.0' gateway='192.168.1.1' type='ip' />" + "<nw id='i61' address='192.168.1.56' subnet='255.255.0.0' gateway='192.168.2.3' type='ip' />" + "<nw id='i62' address='192.168.2.57' type='ip'/>" + "</ethernet-binding></ethernet>" + "<ethernet name='ethernet 1'><ethernet-binding id='ebna3zt61'>" + "<nw id='i63' address='14.14.14.14' subnet='15.15.15.15'>" + "<nw-dnsserver ip='134.135.168.13'/><nw-dnsserver ip='156.156.14.15'/><nw-dnsserver ip='135.25.26.13'/></nw>" + "<nw id='i64' address='15.13.15.15' subnet='16.16.16.16'></nw>" + "</ethernet-binding></ethernet>" + "</hardware>" + "<services /></node>\n" + "<node hostname='pc02'><tr:nodetype>" + "<tr:service name='ethernet' translation='windows-xp'><tr:target name='test-pc02'><tr:wmi host='pc02' user='me' password='hello' /></tr:target></tr:service>" + "</tr:nodetype>" + "<hardware>" + "<ethernet name='ethernet 0'>" + "<ethernet-binding id='ebna3zt7'>" + "<nw id='i70' address='192.168.0.55' subnet='255.255.255.0' type='ip' />" + "<nw id='i71' type='ip'><dyn type='bootp' /></nw>" + "</ethernet-binding></ethernet>" + "<ethernet name='ethernet 1'><ethernet-binding id='ebna3zt71'>" + "<nw id='i72' type='ip'><dyn type='dhcp'/></nw>" + "</ethernet-binding></ethernet>" + "<ethernet name='ethernet 2'><ethernet-binding id='ebnakjsd'>" + "<nw id='i73' type='ip'><dyn type='dhcp'/></nw>" + "<nw id='i74' type='ip' address='135.26.25.23' subnet='255.255.255.0' gateway='136.26.235.25'/>" + "</ethernet-binding></ethernet>" + "</hardware>" + "<services /></node>\n" + "<node hostname='pc03'><tr:nodetype>" + "<tr:service name='ethernet' translation='windows-xp'><tr:target name='test-pc03'><tr:wmi host='pc03' /></tr:target></tr:service>" + "</tr:nodetype>" + "<prefix-lists>" + "<prefix-list name='trusted' id='pref1'><description>IP numbers we trust</description><match-prefix address='192.168.0.0' length='16' />" + "</prefix-list><prefix-list name='trusted' id='pref2'><description>A very evil hacker net</description>" + "<match-prefix address='10.10.30.0' length='24' /></prefix-list></prefix-lists>" + "<hardware><ethernet name='ethernet 0'>" + "<ethernet-binding id='xyz' name='uni'>" + "<nw id='i15' address='192.168.0.1'  subnet='192.168.0.0' gateway='192.168.0.199' hwaddress='aa:aa:aa:aa:aa:aa' type='ip'/></ethernet-binding></ethernet>" + "<ethernet name='ethernet 1'><ethernet-binding id='ebnetbf'>" + "<nw id='i201' type='ip' onboot='yes'><dyn type='dhcp' /><nw-dnsserver ip='155.155.13.13'/><nw-dnsserver ip='13.13.13.15'/><nw-dnsserver ip='77.77.77.77'/></nw>" + "<nw id='i201_1' type='ip' onboot='no' address='13.13.15.18' subnet='15.13.15.15' /></ethernet-binding></ethernet>" + "<ethernet name='ethernet 2'><ethernet-binding id='ebnetb2'>" + "<nw id='i202' address='18.18.0.2' type='ip' peerdns='yes'><nw-dnsserver ip='155.155.13.13'/></nw></ethernet-binding></ethernet>" + "<ethernet name='ethernet 3'><ethernet-binding id='ebnetb3'>" + "<nw id='i203' address='18.18.0.3' type='ip'  onboot='yes'/></ethernet-binding></ethernet>" + "</hardware>" + "<services />" + "</node>" + "</nodes>";

    /** set up the test
      * @throws Exception does not catch exceptions. 
      */
    protected void setUp() throws Exception {
        SAXBuilder xmlbuilder = new SAXBuilder();
        globals = xmlbuilder.build(new StringReader(snodes)).getRootElement();
        List pcs = globals.getChildren("node", VerinecNamespaces.DEFNS_NODE);
        pc01 = (Element) pcs.get(0);
        pc02 = (Element) pcs.get(1);
        pc03 = (Element) pcs.get(2);
    }

    /**
      * Test if a correct document can be translated.
      * @throws Throwable does not catch exceptions.
      */
    public void testValidTranslate() throws Throwable {
        Translator t = new Translator(TranslationRepFactory.createRepository());
        Format format = Format.getPrettyFormat();
        XMLOutputter out = new XMLOutputter(format);
        System.out.println("*** Configuration output ***");
        System.out.println("\n\n* A node with more than one ethernet bindings *\n");
        Document d = t.translateNode(pc01);
        out.output(d, System.out);
        System.out.println("\n\n* A node with dhcp and static ethernet-binding *\n");
        d = t.translateNode(pc02);
        out.output(d, System.out);
        System.out.println("\n\n* A node with many ethernet cards and bindings *\n");
        d = t.translateNode(pc03);
        out.output(d, System.out);
    }

    /**
      * Test a complete translatoe procedure. 
      * @throws Throwable does not catch exceptions.
      */
    public void testCompleteTranslate() throws Throwable {
        Format format = Format.getPrettyFormat();
        XMLOutputter out = new XMLOutputter(format);
        SAXBuilder xmlbuilder = new SAXBuilder();
        Element all = xmlbuilder.build(new StringReader(snodes)).getRootElement();
        Translator t = new Translator(TranslationRepFactory.createRepository());
        System.out.println("\n\n*** A complete document translated ***\n");
        Document d = t.translate(all);
        out.output(d, System.out);
    }

    /** 
      * Test if a valid restriction works. 
      * @throws Throwable does not catch exceptions.
      */
    public void testValidRestrict() throws Throwable {
        Translator t = new Translator(TranslationRepFactory.createRepository());
        Format format = Format.getPrettyFormat();
        XMLOutputter out = new XMLOutputter(format);
        System.out.println("*** Restriction output ***");
        System.out.println("\n\n* A node with some ethernet-bindings *\n");
        Document d = t.restrictNode(pc01);
        out.output(d, System.out);
        System.out.println("\n\n* A node with dynamic and static connections *\n");
        d = t.restrictNode(pc02);
        out.output(d, System.out);
        System.out.println("\n\n* A node with many ethernet cards and bindings *\n");
        d = t.restrictNode(pc03);
        out.output(d, System.out);
    }

    /**
      * Test if a complete restriction works. 
      * @throws Throwable does not catch exceptions.
      */
    public void testCompleteRestrict() throws Throwable {
        Translator t = new Translator(TranslationRepFactory.createRepository());
        Format format = Format.getPrettyFormat();
        XMLOutputter out = new XMLOutputter(format);
        System.out.println("\n\n*** Restriction output of a complete document ***\n");
        Document d = t.restrict(globals);
        out.output(d, System.out);
    }
}
