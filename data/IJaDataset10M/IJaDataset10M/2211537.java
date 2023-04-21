package org.bee.testcase;

import java.util.ArrayList;
import java.util.List;
import junit.framework.TestCase;
import org.bee.tl.core.BeeRuntimeException;
import org.bee.tl.core.BeeTemplate;

public class RuntimeErrorDisplayTest extends TestCase {

    public void testConditionEval2() throws Exception {
        BeeTemplate t = new BeeTemplate("\n\n#:if(user.age){var c=1;}");
        User user = new User();
        user.age = 12;
        t.set("user", user);
        TestErrorHandler actual = new TestErrorHandler();
        t.setErrorHandler(actual);
        t.getTextAsString();
        TestErrorHandler expected = new TestErrorHandler();
        expected.line = 3;
        expected.tokenName = "age";
        expected.errorCode = BeeRuntimeException.BOOLEAN_EXPECTED_ERROR;
        this.assertEquals(expected, actual);
    }

    public void testConditionEval() throws Exception {
        BeeTemplate t = new BeeTemplate("\n\n#:if(a){var c=1;}");
        t.set("a", "hello");
        TestErrorHandler actual = new TestErrorHandler();
        t.setErrorHandler(actual);
        t.getTextAsString();
        TestErrorHandler expected = new TestErrorHandler();
        expected.line = 3;
        expected.tokenName = "a";
        expected.errorCode = BeeRuntimeException.BOOLEAN_EXPECTED_ERROR;
        this.assertEquals(expected, actual);
    }

    public void testDivZero() throws Exception {
        BeeTemplate t = new BeeTemplate("#:var a=1/c;b=2;");
        t.set("c", 0);
        TestErrorHandler actual = new TestErrorHandler();
        t.setErrorHandler(actual);
        t.getTextAsString();
        TestErrorHandler expected = new TestErrorHandler();
        expected.line = 1;
        expected.errorCode = BeeRuntimeException.DIV_ZERO_ERROR;
        expected.tokenName = "c";
        this.assertEquals(expected, actual);
    }

    public void testRefNotDefined() throws Exception {
        BeeTemplate t = new BeeTemplate("$u$");
        TestErrorHandler actual = new TestErrorHandler();
        t.setErrorHandler(actual);
        t.getTextAsString();
        TestErrorHandler expected = new TestErrorHandler();
        expected.line = 1;
        expected.errorCode = BeeRuntimeException.VAR_NOT_DEFINED;
        expected.tokenName = "u";
        this.assertEquals(expected, actual);
    }

    public void testNull() throws Exception {
        BeeTemplate t = new BeeTemplate("\n$u.h$");
        t.set("u", null);
        TestErrorHandler actual = new TestErrorHandler();
        t.setErrorHandler(actual);
        t.getTextAsString();
        TestErrorHandler expected = new TestErrorHandler();
        expected.line = 2;
        expected.errorCode = BeeRuntimeException.NULL;
        expected.tokenName = "u";
        this.assertEquals(expected, actual);
    }

    public void testArrayOutIndex() throws Exception {
        BeeTemplate t = new BeeTemplate("\n$list[a]$");
        List list = new ArrayList();
        t.set("list", list);
        t.set("a", 1);
        TestErrorHandler actual = new TestErrorHandler();
        t.setErrorHandler(actual);
        t.getTextAsString();
        TestErrorHandler expected = new TestErrorHandler();
        expected.line = 2;
        expected.errorCode = BeeRuntimeException.ARRAY_INDEX_ERROR;
        expected.tokenName = "a";
        this.assertEquals(expected, actual);
    }

    public void testNoSuchAttribute() throws Exception {
        BeeTemplate t = new BeeTemplate("\n$u.h$");
        t.set("u", "hello");
        TestErrorHandler actual = new TestErrorHandler();
        t.setErrorHandler(actual);
        t.getTextAsString();
        TestErrorHandler expected = new TestErrorHandler();
        expected.line = 2;
        expected.errorCode = BeeRuntimeException.ATTRIBUTE_INVALID;
        expected.tokenName = "h";
        this.assertEquals(expected, actual);
    }

    public void testCastExcepion() throws Exception {
        BeeTemplate t = new BeeTemplate("$list[0]$");
        t.set("list", "hello");
        TestErrorHandler actual = new TestErrorHandler();
        t.setErrorHandler(actual);
        t.getTextAsString();
        TestErrorHandler expected = new TestErrorHandler();
        expected.line = 1;
        expected.errorCode = BeeRuntimeException.CAST_LIST_OR_MAP_ERROR;
        expected.tokenName = "list";
        this.assertEquals(expected, actual);
    }
}
