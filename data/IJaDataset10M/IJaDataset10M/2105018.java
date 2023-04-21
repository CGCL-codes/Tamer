package ch.ethz.xquts.Optional.Revalidation;

import org.junit.Test;
import ch.ethz.mxquery.testsuite.XQueryTestBase;
import ch.ethz.mxquery.testsuite.XQueryTestCase;

public class RevalidationDeclarationStrictandSkipclass extends XQueryTestBase {

    @Test
    public void test_revalidate_valtrans_ins_003() throws Exception {
        clearVariableCache();
        String query;
        XQueryTestCase testcase;
        query = "(: Name: valtrans-ins-003 :)\n(: Description: inserting a disallowed element is bad news; detected by revalidation defined\n   in prolog of library module :)\n\ndeclare construction strip;\ndeclare revalidation skip;\nimport schema default element namespace \"http://ns.example.com/books\";\nimport module namespace m1 = \"http://www.w3.org/xqupd/tests/ns/valtrans-ins-003\" at \"valtrans-ins-003-mod1.xq\";   \n\n(: insert-start :)\ndeclare variable $books as document-node(schema-element(BOOKLIST)) external;\n(: insert-end :)\n\nm1:update($books)\n\n\n      ";
        testcase = new XQueryTestCase(driver, query);
        testcase.addSchemaMapping("http://ns.example.com/books", "" + executionpath + "/test/tests/TestSources/books.xsd");
        testcase.addVariable("books", "" + executionpath + "/test/tests/TestSources/books.xml", true);
        testcase.execute();
        do {
            boolean match = false;
            String errorcode = testcase.getErrorCode();
            String[] errorcodes = { "XQDY0027" };
            if (errorcode == null) throw new AssertionError("nothing was thrown\nwhile the error" + errorcodes[0] + "was expected");
            for (String testcode : errorcodes) {
                if (errorcode.equals(testcode)) match = true;
            }
            if (match) break;
            throw new AssertionError(errorcode + " was thrown\nwhile the error" + errorcodes[0] + "was expected");
        } while (false);
    }

    ;
}
