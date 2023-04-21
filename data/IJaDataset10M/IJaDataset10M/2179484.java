package com.hp.hpl.jena.rdf.arp.test;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;
import com.hp.hpl.jena.rdf.model.RDFErrorHandler;

/**
 * @author <a href="mailto:Jeremy.Carroll@hp.com">Jeremy Carroll</a>
 *
*/
public class TestScope extends TestCase {

    public static Test suite() {
        TestSuite s = new TestSuite(TestScope.class);
        s.setName("ARP Scoping");
        return s;
    }

    public TestScope(String nm) {
        super(nm);
    }

    public void test06() throws Exception {
        check("testing/arp/scope/test06.rdf");
    }

    static RDFErrorHandler suppress = new RDFErrorHandler() {

        public void warning(Exception e) {
        }

        public void error(Exception e) {
        }

        public void fatalError(Exception e) {
        }
    };

    private void check(final String fn) throws IOException {
        NTripleTestSuite.loadRDFx(new InFactoryX() {

            public InputStream open() throws IOException {
                return new FileInputStream(fn);
            }
        }, suppress, "http://example.org/a", false, 0);
    }
}
