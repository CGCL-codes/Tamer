package com.breachwalls.mogen.utils;

import static org.junit.Assert.*;
import org.junit.Test;

/**
 * {@link JavaNameUtils}のテストケース.
 * @author todoken
 * @version $Revision: 31 $
 * @see com.breachwalls.mogen.utils.JavaNameUtils
 */
public class JavaNameUtilsTest {

    /**
     * Test method for {@link com.breachwalls.mogen.utils.JavaNameUtils#toRdbName(java.lang.String)}.
     * @see com.breachwalls.mogen.utils.JavaNameUtils#toRdbName(java.lang.String)
     */
    @Test
    public void testToRdbName() {
        assertEquals(null, JavaNameUtils.toRdbName(null));
        assertEquals("", JavaNameUtils.toRdbName(""));
        assertEquals("JAVA_NAME", JavaNameUtils.toRdbName("javaName"));
        assertEquals("JAVA_NAME", JavaNameUtils.toRdbName("JavaName"));
        assertEquals("JAVA_NAME", JavaNameUtils.toRdbName("JAVA_NAME"));
        assertEquals("J_A_V_A", JavaNameUtils.toRdbName("J_A_V_A"));
    }

    /**
     * Test method for {@link com.breachwalls.mogen.utils.JavaNameUtils#toJavaName(java.lang.String)}.
     * @see com.breachwalls.mogen.utils.JavaNameUtils#toJavaName(java.lang.String);
     */
    @Test
    public void testToJavaName() {
        assertEquals(null, JavaNameUtils.toRdbName(null));
        assertEquals("", JavaNameUtils.toJavaName(""));
        assertEquals("javaName", JavaNameUtils.toJavaName("javaName"));
        assertEquals("JavaName", JavaNameUtils.toJavaName("JavaName"));
        assertEquals("javaName", JavaNameUtils.toJavaName("JAVA_NAME"));
        assertEquals("jAVA", JavaNameUtils.toJavaName("J_A_V_A"));
    }

    /**
     * Test method for {@link com.breachwalls.mogen.utils.JavaNameUtils#toJavaFieldName(java.lang.String)}.
     * @see com.breachwalls.mogen.utils.JavaNameUtils#toJavaFieldName(java.lang.String)
     */
    @Test
    public void testToJavaFieldName() {
        assertEquals(null, JavaNameUtils.toJavaFieldName(null));
        assertEquals("", JavaNameUtils.toJavaFieldName(""));
        assertEquals("javaName", JavaNameUtils.toJavaFieldName("javaName"));
        assertEquals("javaName", JavaNameUtils.toJavaFieldName("JavaName"));
        assertEquals("javaName", JavaNameUtils.toJavaFieldName("JAVA_NAME"));
        assertEquals("jAVA", JavaNameUtils.toJavaFieldName("J_A_V_A"));
    }

    /**
     * Test method for {@link com.breachwalls.mogen.utils.JavaNameUtils#toJavaMethodName(java.lang.String)}.
     * @see com.breachwalls.mogen.utils.JavaNameUtils#toJavaMethodName(java.lang.String)
     */
    @Test
    public void testToJavaMethodName() {
        assertEquals(null, JavaNameUtils.toJavaMethodName(null));
        assertEquals("", JavaNameUtils.toJavaMethodName(""));
        assertEquals("JavaName", JavaNameUtils.toJavaMethodName("javaName"));
        assertEquals("JavaName", JavaNameUtils.toJavaMethodName("JavaName"));
        assertEquals("JavaName", JavaNameUtils.toJavaMethodName("JAVA_NAME"));
        assertEquals("JAVA", JavaNameUtils.toJavaMethodName("J_A_V_A"));
    }

    /**
     * Test method for {@link com.breachwalls.mogen.utils.JavaNameUtils#toCollectionFieldName(java.lang.String)}.
     * @see com.breachwalls.mogen.utils.JavaNameUtils#toCollectionFieldName(java.lang.String)
     */
    @Test
    public void testToCollectionFieldName() {
        assertEquals(null, JavaNameUtils.toCollectionFieldName(null));
        assertEquals("", JavaNameUtils.toCollectionFieldName(""));
        assertEquals("javaNames", JavaNameUtils.toCollectionFieldName("javaName"));
        assertEquals("javaNames", JavaNameUtils.toCollectionFieldName("JavaName"));
        assertEquals("javaNames", JavaNameUtils.toCollectionFieldName("JAVA_NAME"));
        assertEquals("jAVAs", JavaNameUtils.toCollectionFieldName("J_A_V_A"));
        assertEquals("cities", JavaNameUtils.toCollectionFieldName("city"));
        assertEquals("buses", JavaNameUtils.toCollectionFieldName("bus"));
        assertEquals("classes", JavaNameUtils.toCollectionFieldName("class"));
        assertEquals("cashes", JavaNameUtils.toCollectionFieldName("cash"));
        assertEquals("tomatoes", JavaNameUtils.toCollectionFieldName("tomato"));
        assertEquals("boxes", JavaNameUtils.toCollectionFieldName("box"));
        assertEquals("leaves", JavaNameUtils.toCollectionFieldName("leaf"));
        assertEquals("lives", JavaNameUtils.toCollectionFieldName("life"));
        assertEquals("books", JavaNameUtils.toCollectionFieldName("book"));
    }
}
