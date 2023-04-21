package net.techwatch.guava.base;

import java.util.Iterator;
import org.junit.Assert;
import org.junit.Test;
import com.google.common.base.Splitter;

public class SplitterTest {

    private static String[] JDK_RESULT = ",a,,b,".split(",");

    @Test
    public void testJdkSplitA() {
        Assert.assertArrayEquals(new String[] { "", "a", "", "b", "" }, JDK_RESULT);
    }

    @Test
    public void testJdkSplitB() {
        Assert.assertArrayEquals(new String[] { null, "a", null, "b", null }, JDK_RESULT);
    }

    @Test
    public void testJdkSplitC() {
        Assert.assertArrayEquals(new String[] { "a", null, "b" }, JDK_RESULT);
    }

    @Test
    public void testJdkSplitD() {
        Assert.assertArrayEquals(new String[] { "a", "b" }, JDK_RESULT);
    }

    @Test
    public void testJdkSplitE() {
        Assert.assertArrayEquals(new String[] { "", "a", "", "b" }, JDK_RESULT);
    }

    @Test
    public void testSimpleSplitter() {
        Iterable<String> splitIt = Splitter.on(',').split(" foo, ,bar, quux,");
        String[] split = { " foo", " ", "bar", " quux", "" };
        Iterator<String> it = splitIt.iterator();
        for (int i = 0; i < split.length; i++) {
            Assert.assertEquals(split[i], it.next());
        }
    }

    @Test
    public void testOmitEmptySplitter() {
        Iterable<String> splitIt = Splitter.on(',').trimResults().omitEmptyStrings().split(" foo, ,bar, quux,");
        String[] split = { "foo", "bar", "quux" };
        Iterator<String> it = splitIt.iterator();
        for (int i = 0; i < split.length; i++) {
            Assert.assertEquals(split[i], it.next());
        }
    }
}
