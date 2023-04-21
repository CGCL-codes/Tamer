package com.google.code.gtkjfilechooser;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import java.io.File;
import org.junit.Test;
import com.google.code.gtkjfilechooser.ArrayUtil;

/**
 * @author Costantino Cerbo
 *
 */
public class ArrayUtilTest {

    @Test
    public void testAreArrayEqual() {
        File[] array0 = new File[1];
        array0[0] = new File("myfile");
        File[] array1 = new File[1];
        array1[0] = new File("myfile");
        System.out.println(System.getProperty("java.vendor") + " " + System.getProperty("java.version"));
        assertFalse(array0.equals(array1));
        assertTrue(ArrayUtil.areArrayEqual(array0, array1));
    }
}
