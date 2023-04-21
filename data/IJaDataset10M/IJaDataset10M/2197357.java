package jacky.lanlan.song.reflection;

import static org.junit.Assert.*;
import java.lang.reflect.*;
import org.junit.After;
import org.junit.Test;
import jacky.lanlan.song.TestDomain;
import jacky.lanlan.song.reflection.ReflectionUtils.MethodFilter;

public class MethodFilterTest {

    private Method getter = ReflectionUtils.findMethod(TestDomain.class, "getAddr");

    private Method setter = ReflectionUtils.findMethod(TestDomain.class, "setAddr", new Class[] { String.class });

    @Test
    public void testINCLUDE() throws Exception {
        assertTrue(setter + "] 应该是 public+void ", MethodFilter.INCLUDE.combine(MethodFilter.PUBLIC, MethodFilter.VOID).matches(setter));
    }

    @Test
    public void testEXCLUDE() throws Exception {
        assertFalse(getter + "] 应该没有参数 ", MethodFilter.EXCLUDE.combine(MethodFilter.NO_ARG).matches(getter));
    }

    @Test
    public void testOR() throws Exception {
        assertTrue("应该有Getter", MethodFilter.OR.combine(MethodFilter.GETTER, MethodFilter.SETTER).matches(getter));
        assertTrue("应该有Setter", MethodFilter.OR.combine(MethodFilter.GETTER, MethodFilter.SETTER).matches(setter));
    }

    @Test
    public void testNOT() throws Exception {
        assertFalse("应该没有Getter", MethodFilter.NOT.doWith(MethodFilter.GETTER).matches(getter));
        assertFalse("应该没有Setter", MethodFilter.NOT.doWith(MethodFilter.SETTER).matches(setter));
    }

    @After
    public void destory() {
    }
}
