package org.apache.solr;

import org.apache.solr.request.*;
import org.apache.solr.util.*;

/**
 * This is an example of how to write a JUnit tests for Solr using the
 * AbstractSolrTestCase
 */
public class SampleTest extends AbstractSolrTestCase {

    /**
   * All subclasses of AbstractSolrTestCase must define this method.
   *
   * <p>
   * Note that different tests can use different schemas by refering
   * to any crazy path they want (as long as it works).
   * </p>
   */
    public String getSchemaFile() {
        return "solr/crazy-path-to-schema.xml";
    }

    /**
   * All subclasses of AbstractSolrTestCase must define this method
   *
   * <p>
   * Note that different tests can use different configs by refering
   * to any crazy path they want (as long as it works).
   * </p>
   */
    public String getSolrConfigFile() {
        return "solr/crazy-path-to-config.xml";
    }

    /**
   * Demonstration of some of the simple ways to use the base class
   */
    public void testSimple() {
        lrf.args.put("version", "2.0");
        assertU("Simple assertion that adding a document works", adoc("id", "4055", "subject", "Hoss the Hoss man Hostetter"));
        assertU(adoc("id", "4056", "subject", "Some Other Guy"));
        assertU(commit());
        assertU(optimize());
        assertQ("couldn't find subject hoss", req("subject:Hoss"), "//result[@numFound=1]", "//int[@name='id'][.='4055']");
    }

    /**
   * Demonstration of some of the more complex ways to use the base class
   */
    public void testAdvanced() throws Exception {
        lrf.args.put("version", "2.0");
        assertU("less common case, a complex addition with options", add(doc("id", "4059", "subject", "Who Me?"), "allowDups", "true"));
        assertU("or just make the raw XML yourself", "<add allowDups=\"true\">" + doc("id", "4059", "subject", "Who Me Again?") + "</add>");
        assertU("<add><doc><field name=\"id\">4055</field>" + "<field name=\"subject\">Hoss the Hoss man Hostetter</field>" + "</doc></add>");
        assertU("<commit/>");
        assertU("<optimize/>");
        SolrQueryRequest req = lrf.makeRequest("subject:Hoss");
        assertQ("couldn't find subject hoss", req, "//result[@numFound=1]", "//int[@name='id'][.='4055']");
        TestHarness.LocalRequestFactory l = h.getRequestFactory("crazy_custom_qt", 100, 200, "version", "2.1");
        assertQ("how did i find Mack Daddy? ", l.makeRequest("Mack Daddy"), "//result[@numFound=0]");
        assertNull("how did i find Mack Daddy? ", h.validateQuery(l.makeRequest("Mack Daddy"), "//result[@numFound=0]"));
    }
}
