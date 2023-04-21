package net.techwatch.guava.base;

import org.junit.Assert;
import org.junit.Test;
import com.google.common.base.CharMatcher;

public class CharMatcherTest {

    @Test
    public void testCharMatcher() {
        String input = "tralala 5 tralala - tralala 6 tralala";
        String id = CharMatcher.DIGIT.or(CharMatcher.is('-')).retainFrom(input);
        Assert.assertEquals("5-6", id);
    }
}
