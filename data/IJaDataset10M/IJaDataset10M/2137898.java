package es.rediris.searchy.dc;

import java.util.*;
import es.rediris.searchy.dc.*;
import junit.framework.*;
import org.w3c.dom.*;

public class DCAll extends TestCase {

    DC dc = new DC();

    public DCAll(String name) {
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
        resource2.add(DCVocabulary.SUBJECT, "subject 2");
        resource2.add(DCVocabulary.DESCRIPTION, "description test 2");
        resource2.add(DCVocabulary.CREATOR, "creator test 2");
        resource2.add(DCVocabulary.CREATOR, "creator test bis 2");
        resource2.add(DCVocabulary.PUBLISHER, "publisher test 2");
        this.dc.addElement(resource);
        this.dc.addElement(resource2);
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
        DCVocabulary.printTree(doc, "");
        System.out.println();
    }

    public static Test suite() {
        TestSuite suite = new TestSuite();
        suite.addTest(new DCElementTest("DCElementTest"));
        suite.addTest(new DCResourceTest("DCResourceTest"));
        suite.addTest(new DCTest("DC"));
        return suite;
    }

    /** 
	 *  Esta es el punto de entrada al programa.
	 *  El lector de lo que sea identifica esto para meterlo
	 *  como entrada al programa y tal y pascual.
	 *  @param args Argumentos de entrada al programa. No se usa.
	 **/
    public static void main(String[] args) {
        junit.textui.TestRunner.run(suite());
    }
}
