package ch.ethz.xquts.MinimalConformance.UpdateOperations.UpdateRoutines;

import org.junit.Test;
import ch.ethz.mxquery.testsuite.XQueryTestBase;
import ch.ethz.mxquery.testsuite.XQueryTestCase;

public class propagateNamespaceclass extends XQueryTestBase {

    @Test
    public void test_propagateNamespaces01() throws Exception {
        clearVariableCache();
        String query;
        XQueryTestCase testcase;
        query = "declare copy-namespaces preserve, inherit;\ndeclare boundary-space preserve;\n\ncopy $data := <v xmlns:a=\"a-one\" xmlns:b=\"b-one\"/>\nmodify \n  insert node \n      <w>\n        <x xmlns:a=\"a-two\">\n          <y xmlns:b=\"b-two\"><z/></y>\n        </x>\n      </w>\n  into $data\nreturn \n   let $w := $data/w\n   let $x := $w/x\n   let $y := $x/y\n   let $z := $y/z\n   return\n<result>\n  <w>{namespace-uri-for-prefix(\"a\", $w), namespace-uri-for-prefix(\"b\",$w)}</w>\n  <x>{namespace-uri-for-prefix(\"a\", $x), namespace-uri-for-prefix(\"b\",$x)}</x>\n  <y>{namespace-uri-for-prefix(\"a\", $y), namespace-uri-for-prefix(\"b\",$y)}</y>\n  <z>{namespace-uri-for-prefix(\"a\", $z), namespace-uri-for-prefix(\"b\",$z)}</z>\n</result>\n\n";
        testcase = new XQueryTestCase(driver, query);
        testcase.execute();
        do {
            boolean match = false;
            String[] expectedoutput = new String[] { "<result>\n  <w>a-one b-one</w>\n  <x>a-two b-one</x>\n  <y>a-two b-two</y>\n  <z>a-two b-two</z>\n</result>\n" };
            String errorcode = testcase.getErrorCode();
            if (errorcode != null) {
                if (match) break;
                throw new AssertionError(errorcode + " was thrown\nwhile " + expectedoutput[0] + "was expected");
            }
            testcase.assertXMLEqual("XML", expectedoutput);
        } while (false);
    }

    ;

    @Test
    public void test_propagateNamespaces02() throws Exception {
        clearVariableCache();
        String query;
        XQueryTestCase testcase;
        query = "declare copy-namespaces preserve, no-inherit;\ndeclare boundary-space preserve;\n\ncopy $data := <v xmlns:a=\"a-one\" xmlns:b=\"b-one\"/>\nmodify \n  insert node \n      <w>\n        <x xmlns:a=\"a-two\">\n          <y xmlns:b=\"b-two\"><z/></y>\n        </x>\n      </w>\n  into $data\nreturn \n   let $w := $data/w\n   let $x := $w/x\n   let $y := $x/y\n   let $z := $y/z\n   return\n<result>\n  <w>{namespace-uri-for-prefix(\"a\", $w), namespace-uri-for-prefix(\"b\",$w)}</w>\n  <x>{namespace-uri-for-prefix(\"a\", $x), namespace-uri-for-prefix(\"b\",$x)}</x>\n  <y>{namespace-uri-for-prefix(\"a\", $y), namespace-uri-for-prefix(\"b\",$y)}</y>\n  <z>{namespace-uri-for-prefix(\"a\", $z), namespace-uri-for-prefix(\"b\",$z)}</z>\n</result>\n\n";
        testcase = new XQueryTestCase(driver, query);
        testcase.execute();
        do {
            boolean match = false;
            String[] expectedoutput = new String[] { "<result>\n  <w/>\n  <x>a-two</x>\n  <y>a-two b-two</y>\n  <z>a-two b-two</z>\n</result>\n" };
            String errorcode = testcase.getErrorCode();
            if (errorcode != null) {
                if (match) break;
                throw new AssertionError(errorcode + " was thrown\nwhile " + expectedoutput[0] + "was expected");
            }
            testcase.assertXMLEqual("XML", expectedoutput);
        } while (false);
    }

    ;

    @Test
    public void test_propagateNamespaces03() throws Exception {
        clearVariableCache();
        String query;
        XQueryTestCase testcase;
        query = "declare copy-namespaces no-preserve, inherit;\ndeclare boundary-space preserve;\n\ncopy $data := <v xmlns:a=\"a-one\" xmlns:b=\"b-one\"/>\nmodify \n  insert node \n      <w>\n        <x xmlns:a=\"a-two\">\n          <y xmlns:b=\"b-two\"><z/></y>\n        </x>\n      </w>\n  into $data\nreturn \n   let $w := $data/w\n   let $x := $w/x\n   let $y := $x/y\n   let $z := $y/z\n   return\n<result>\n  <w>{namespace-uri-for-prefix(\"a\", $w), namespace-uri-for-prefix(\"b\",$w)}</w>\n  <x>{namespace-uri-for-prefix(\"a\", $x), namespace-uri-for-prefix(\"b\",$x)}</x>\n  <y>{namespace-uri-for-prefix(\"a\", $y), namespace-uri-for-prefix(\"b\",$y)}</y>\n  <z>{namespace-uri-for-prefix(\"a\", $z), namespace-uri-for-prefix(\"b\",$z)}</z>\n</result>\n\n";
        testcase = new XQueryTestCase(driver, query);
        testcase.execute();
        do {
            boolean match = false;
            String[] expectedoutput = new String[] { "<result>\n  <w/>\n  <x>a-two</x>\n  <y>a-two b-two</y>\n  <z>a-two b-two</z>\n</result>\n" };
            String errorcode = testcase.getErrorCode();
            if (errorcode != null) {
                if (match) break;
                throw new AssertionError(errorcode + " was thrown\nwhile " + expectedoutput[0] + "was expected");
            }
            testcase.assertXMLEqual("XML", expectedoutput);
        } while (false);
    }

    ;

    @Test
    public void test_propagateNamespaces04() throws Exception {
        clearVariableCache();
        String query;
        XQueryTestCase testcase;
        query = "declare copy-namespaces no-preserve, no-inherit;\ndeclare boundary-space preserve;\n\ncopy $data := <v xmlns:a=\"a-one\" xmlns:b=\"b-one\"/>\nmodify \n  insert node \n      <w>\n        <x xmlns:a=\"a-two\">\n          <y xmlns:b=\"b-two\"><z/></y>\n        </x>\n      </w>\n  into $data\nreturn \n   let $w := $data/w\n   let $x := $w/x\n   let $y := $x/y\n   let $z := $y/z\n   return\n<result>\n  <w>{namespace-uri-for-prefix(\"a\", $w), namespace-uri-for-prefix(\"b\",$w)}</w>\n  <x>{namespace-uri-for-prefix(\"a\", $x), namespace-uri-for-prefix(\"b\",$x)}</x>\n  <y>{namespace-uri-for-prefix(\"a\", $y), namespace-uri-for-prefix(\"b\",$y)}</y>\n  <z>{namespace-uri-for-prefix(\"a\", $z), namespace-uri-for-prefix(\"b\",$z)}</z>\n</result>\n\n";
        testcase = new XQueryTestCase(driver, query);
        testcase.execute();
        do {
            boolean match = false;
            String[] expectedoutput = new String[] { "<result>\n  <w/>\n  <x>a-two</x>\n  <y>a-two b-two</y>\n  <z>a-two b-two</z>\n</result>\n" };
            String errorcode = testcase.getErrorCode();
            if (errorcode != null) {
                if (match) break;
                throw new AssertionError(errorcode + " was thrown\nwhile " + expectedoutput[0] + "was expected");
            }
            testcase.assertXMLEqual("XML", expectedoutput);
        } while (false);
    }

    ;

    @Test
    public void test_propagateNamespaces05() throws Exception {
        clearVariableCache();
        String query;
        XQueryTestCase testcase;
        query = "declare copy-namespaces preserve, inherit;\ndeclare boundary-space preserve;\n\ndeclare default element namespace \"bar\";\n\ncopy $x := <x xmlns=\"foo\" xmlns:a=\"a-ns\"/>\nmodify \n  insert node <y/> into $x\nreturn\n<result>\n  <x>{namespace-uri-for-prefix(\"\", $x), namespace-uri-for-prefix(\"a\", $x)}</x>\n  <y>{namespace-uri-for-prefix(\"\", $x/y), namespace-uri-for-prefix(\"a\", $x/y)}</y>\n</result>";
        testcase = new XQueryTestCase(driver, query);
        testcase.execute();
        do {
            boolean match = false;
            String[] expectedoutput = new String[] { "<result xmlns=\"bar\">\n  <x>foo a-ns</x>\n  <y>bar a-ns</y>\n</result>\n" };
            String errorcode = testcase.getErrorCode();
            if (errorcode != null) {
                if (match) break;
                throw new AssertionError(errorcode + " was thrown\nwhile " + expectedoutput[0] + "was expected");
            }
            testcase.assertXMLEqual("XML", expectedoutput);
        } while (false);
    }

    ;

    @Test
    public void test_propagateNamespaces06() throws Exception {
        clearVariableCache();
        String query;
        XQueryTestCase testcase;
        query = "declare copy-namespaces preserve, no-inherit;\ndeclare boundary-space preserve;\n\ndeclare default element namespace \"bar\";\n\ncopy $x := <x xmlns=\"foo\" xmlns:a=\"a-ns\"/>\nmodify \n  insert node <y/> into $x\nreturn\n<result>\n  <x>{namespace-uri-for-prefix(\"\", $x), namespace-uri-for-prefix(\"a\", $x)}</x>\n  <y>{namespace-uri-for-prefix(\"\", $x/y), namespace-uri-for-prefix(\"a\", $x/y)}</y>\n</result>";
        testcase = new XQueryTestCase(driver, query);
        testcase.execute();
        do {
            boolean match = false;
            String[] expectedoutput = new String[] { "<result xmlns=\"bar\">\n  <x>foo a-ns</x>\n  <y>bar</y>\n</result>\n" };
            String errorcode = testcase.getErrorCode();
            if (errorcode != null) {
                if (match) break;
                throw new AssertionError(errorcode + " was thrown\nwhile " + expectedoutput[0] + "was expected");
            }
            testcase.assertXMLEqual("XML", expectedoutput);
        } while (false);
    }

    ;
}
