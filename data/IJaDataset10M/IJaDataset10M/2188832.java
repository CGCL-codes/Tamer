package ch.ethz.xquts.Optional.fnput_other_nodekinds;

import org.junit.Test;
import ch.ethz.mxquery.testsuite.XQueryTestBase;
import ch.ethz.mxquery.testsuite.XQueryTestCase;

public class put_attribute_failclass extends XQueryTestBase {

    @Test
    public void test_fn_put_005_fail() throws Exception {
        clearVariableCache();
        String query;
        XQueryTestCase testcase;
        query = "(: Name: fn-put-005 :)\n(: Description: Store an attribute. :)\n\n(: insert-start :)\ndeclare variable $input-URI external;\n(: insert-end :)\n\nfn:put(attribute name { \"Barack\" }, $input-URI)\n";
        testcase = new XQueryTestCase(driver, query);
        testcase.addInputURI("input-URI", "'/TestSources/putOutput.xml'");
        testcase.execute();
        do {
            boolean match = false;
        } while (false);
    }

    ;
}
