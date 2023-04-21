package net.sf.katta.util;

import org.junit.Test;
import static org.junit.Assert.fail;
import static org.fest.assertions.Assertions.assertThat;

public class ClassUtilTest {

    @Test
    public void testPrivateField() throws Exception {
        TestClass1 testClass1 = new TestClass1();
        TestClass2 testClass2 = new TestClass2();
        assertThat(ClassUtil.getPrivateFieldValue(testClass1, "field1")).isEqualTo("1");
        assertThat(ClassUtil.getPrivateFieldValue(testClass2, "field2")).isEqualTo("2");
        assertThat(ClassUtil.getPrivateFieldValue(testClass2, "field1")).isEqualTo("1");
        try {
            ClassUtil.getPrivateFieldValue(testClass2, "fieldXY");
            fail("should throw exception");
        } catch (Exception e) {
        }
    }

    private static class TestClass1 {

        @SuppressWarnings("unused")
        private String field1 = "1";
    }

    private static class TestClass2 extends TestClass1 {

        @SuppressWarnings("unused")
        private String field2 = "2";
    }
}
