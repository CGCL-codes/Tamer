package es.rediris.searchy.dc;

import java.util.*;
import es.rediris.searchy.dc.*;
import junit.framework.*;
import org.w3c.dom.*;

/**
 * @author David F. Barrero
 * @version $Id: DCTest.java,v 1.2 2005/03/03 09:33:12 dfbarrero Exp $
 */
public class DCTest extends TestCase {

    DC dc = new DC();

    public DCTest(String name) {
        super(name);
    }

    protected void setUp() {
        DCResource resource = new DCResource();
        resource.add(DCVocabulary.TITLE, "test title 1");
        resource.add(DCVocabulary.SUBJECT, "subject 1");
        resource.add(DCVocabulary.DESCRIPTION, "description test 1");
        resource.add(DCVocabulary.CREATOR, "creator test 1");
        resource.add(DCVocabulary.CREATOR, "creator test 1 bis");
        resource.add(DCVocabulary.PUBLISHER, "publisher test 1");
        resource.add(DCVocabulary.CONTRIBUTOR, "contributor test 1");
        resource.add(DCVocabulary.DATE, "date test 1");
        resource.add(DCVocabulary.TYPE, "type test 1");
        resource.add(DCVocabulary.FORMAT, "format test 1");
        resource.add(DCVocabulary.IDENTIFIER, "identifier test 1");
        resource.add(DCVocabulary.SOURCE, "source test 1");
        resource.add(DCVocabulary.LANGUAGE, "languaje test 1");
        resource.add(DCVocabulary.RELATION, "relation test 1");
        resource.add(DCVocabulary.COVERAGE, "coverage test 1");
        resource.add(DCVocabulary.RIGHTS, "rights test 1");
        DCResource resource2 = new DCResource();
        resource2.add(DCVocabulary.TITLE, "test title 2");
        resource2.add(DCVocabulary.IDENTIFIER, "test identifier 2");
        resource2.add(DCVocabulary.SUBJECT, "subject 2");
        resource2.add(DCVocabulary.DESCRIPTION, "description test 2");
        resource2.add(DCVocabulary.CREATOR, "creator test 2");
        resource2.add(DCVocabulary.CREATOR, "creator test bis 2");
        resource2.add(DCVocabulary.PUBLISHER, "publisher test 2");
        DCResource resource3 = new DCResource();
        resource3.add(DCVocabulary.TITLE, "test title 3");
        resource3.add(DCVocabulary.SUBJECT, "subject 3");
        resource3.add(DCVocabulary.DESCRIPTION, "description test 3");
        resource3.add(DCVocabulary.CREATOR, "creator test 3");
        resource3.add(DCVocabulary.CREATOR, "creator test bis 3");
        resource3.add(DCVocabulary.PUBLISHER, "publisher test 3");
        this.dc.addElement(resource);
        this.dc.addElement(resource2);
        this.dc.addElement(resource3);
    }

    public void testConstructor() {
        System.out.println("******* testConstructor *****");
        this.dc.print();
    }

    public void testRDF() {
        System.out.println("****** testRDF *******");
        System.out.print(this.dc.toRDF());
    }

    public void testToDOM() {
        System.out.println("****** testToDOM *******");
        Document doc = this.dc.toDOM();
        es.rediris.searchy.util.DOMUtil.printDOM(doc);
        System.out.println();
    }

    public void testDOMConstructor() {
        System.out.println("****** testDOMConstructor *******");
    }

    public void testAdd() {
        System.out.println("****** testAdd *******");
        DCResource resource = new DCResource();
        resource.add(DCVocabulary.TITLE, "Added Title 1");
        resource.add(DCVocabulary.SUBJECT, "Added Subject 1");
        resource.add(DCVocabulary.DESCRIPTION, "Added Description 1");
        resource.add(DCVocabulary.CREATOR, "Added Creator 1");
        resource.add(DCVocabulary.PUBLISHER, "Added Publisher 1");
        DC foo = new DC();
        foo.addElement(resource);
        this.dc.add(foo);
        this.dc.print();
    }

    public void testJenaXMLConstructor() {
        String xml = "<?xml version=\"1.0\"?> <rdf:RDF xmlns:rdf=\"http://www.w3.org/1999/02/22-rdf-syntax-ns#\" xmlns:dc=\"http://purl.org/dc/elements/1.1/\" xmlns:exterms=\"http://www.example.org/terms/\"> <rdf:Description rdf:about=\"http://www.example.org/index.html\"> <exterms:creation-date>August 16, 1999</exterms:creation-date> <dc:language>en</dc:language> <dc:creator rdf:resource=\"http://www.example.org/staffid/85740\"/> </rdf:Description><rdf:Description rdf:about=\"http://example.com/person#Homer\"><dc:creator>Homer J. Simpson</dc:creator><dc:language>es_ES</dc:language></rdf:Description></rdf:RDF>";
        DC foo = new DC(xml, DC.XML);
        StringBuffer buffer = new StringBuffer("");
        for (Enumeration eDC = foo.elements(); eDC.hasMoreElements(); ) {
            DCResource resource = (DCResource) eDC.nextElement();
            System.out.println();
            for (Enumeration eResource = resource.elementsDCElement(); eResource.hasMoreElements(); ) {
                DCElement element = (DCElement) eResource.nextElement();
                System.out.print(element.getName() + " :\t" + element.getValue() + " " + element.getType() + "\n");
            }
        }
    }

    public void testToXML() {
        String xml = "<?xml version=\"1.0\"?> <rdf:RDF xmlns:rdf=\"http://www.w3.org/1999/02/22-rdf-syntax-ns#\" xmlns:dc=\"http://purl.org/dc/elements/1.1/\" xmlns:exterms=\"http://www.example.org/terms/\"> <rdf:Description rdf:about=\"http://www.example.org/index.html\"> <exterms:creation-date>August 16, 1999</exterms:creation-date> <dc:language>en</dc:language> <dc:creator rdf:resource=\"http://www.example.org/staffid/85740\"/> </rdf:Description><rdf:Description rdf:about=\"http://example.com/person#Homer\"><dc:creator>Homer J. Simpson</dc:creator><dc:language>es_ES</dc:language></rdf:Description></rdf:RDF>";
        DC foo = new DC(xml, DC.XML);
        System.out.println(foo.toXML());
    }

    public static Test suite() {
        TestSuite suite = new TestSuite();
        suite.addTest(new DCTest("testRDF"));
        suite.addTest(new DCTest("testToDOM"));
        suite.addTest(new DCTest("testAdd"));
        suite.addTest(new DCTest("testJenaXMLConstructor"));
        suite.addTest(new DCTest("testToXML"));
        return suite;
    }

    /** 
	 *  Esta es el punto de entrada al programa.
	 *  El lector de lo que sea identifica esto para meterlo
	 *  como entrada al programa y tal y pascual.
	 *
	 *  @param args Argumentos de entrada al programa. No se usa.
	 **/
    public static void main(String[] args) {
        junit.textui.TestRunner.run(suite());
    }
}
