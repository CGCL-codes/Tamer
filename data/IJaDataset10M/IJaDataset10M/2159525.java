package com.google.code.javastorage;

import static org.junit.Assert.assertEquals;
import org.junit.Test;
import com.google.code.javastorage.cli.ArgsUtil;

/**
 * 
 * @author thomas.scheuchzer@gmail.com
 * 
 */
public class ArgsUtilTest {

    @Test
    public void testFlatten() {
        String[] args = { "a", "b", "c", "\"hallo du\"", "d" };
        String result = ArgsUtil.flatten(args);
        assertEquals("a b c \"hallo du\" d", result);
    }

    @Test
    public void testFlattenPos() {
        String[] args = { "a", "b", "c", "\"hallo du\"", "d" };
        String result = ArgsUtil.flatten(args, 1);
        assertEquals("b c \"hallo du\" d", result);
    }
}
