package net.sf.buildbox.util;

import net.sf.buildbox.util.BbxMiscUtils;
import org.junit.Assert;
import org.junit.Test;
import java.util.Arrays;
import java.util.LinkedHashSet;

public class BbxMiscUtilsTest {

    @Test
    public void testFindLongestCommonPathPrefix() throws Exception {
        final String rv = flcpp("/alpha/beta/gama", "/alpha/be/ta/gama");
        System.out.println("rv = " + rv);
        Assert.assertEquals("/alpha", rv);
    }

    @Test
    public void testFindLongestCommonPathPrefix_BadIndex() throws Exception {
        final String rv = flcpp("/branches", "/branches/alpha/be/ta/gama");
        System.out.println("rv = " + rv);
        Assert.assertEquals("", rv);
    }

    private String flcpp(String... paths) {
        return BbxMiscUtils.findLongestCommonPathPrefix(new LinkedHashSet<String>(Arrays.asList(paths)), "/");
    }
}
