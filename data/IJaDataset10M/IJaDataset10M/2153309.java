package au.songdi.javapc.test;

import java.util.regex.Pattern;
import org.junit.Test;
import junit.framework.Assert;

public class RegexTest {

    @Test
    public void testDefine() {
        String comment = "//";
        String pattern = "^\\s*(" + comment + ")*\\s*/\\*\\*\\s+#define\\s+[A-Za-z_]+\\w*\\s+\".+\"\\s+\\*\\*/\\s*$";
        String sample1 = " // /** #define SCREEN_WIDTH \"176\" ddd\" **/ ";
        String sample2 = "/**  	 #define   a_SCREEN_WIDTH    \"String dd\"  **/";
        String sample3 = "/**   #define 9_SCREEN_WIDTH    176   **/";
        String sample4 = "//**   #define SCREEN_WIDTH    176   **/";
        String sample5 = "/**   #define SCREEN_WIDTH    176   **//";
        boolean result1 = Pattern.matches(pattern, sample1);
        Assert.assertEquals(true, result1);
        boolean result2 = Pattern.matches(pattern, sample2);
        Assert.assertEquals(true, result2);
        boolean result3 = Pattern.matches(pattern, sample3);
        Assert.assertEquals(false, result3);
        boolean result4 = Pattern.matches(pattern, sample4);
        Assert.assertEquals(false, result4);
        boolean result5 = Pattern.matches(pattern, sample5);
        Assert.assertEquals(false, result5);
    }

    @Test
    public void testDefineGlobal() {
        String pattern = "^\\s*/\\*\\*\\s+#define\\s+global\\s+[A-Z_]+\\w+\\s+\\w+\\s+\\*\\*/\\s*$";
        String sample1 = "/** #define global SCREEN_WIDTH 176 **/";
        String sample2 = "/**   #define global  _SCREEN_WIDTH    String   **/";
        String sample3 = "/**   #define global 9_SCREEN_WIDTH    176   **/";
        String sample4 = "//**   #define global SCREEN_WIDTH    176   **/";
        String sample5 = "/**   #define global SCREEN_WIDTH    176   **//";
        boolean result1 = Pattern.matches(pattern, sample1);
        Assert.assertEquals(true, result1);
        boolean result2 = Pattern.matches(pattern, sample2);
        Assert.assertEquals(true, result2);
        boolean result3 = Pattern.matches(pattern, sample3);
        Assert.assertEquals(false, result3);
        boolean result4 = Pattern.matches(pattern, sample4);
        Assert.assertEquals(false, result4);
        boolean result5 = Pattern.matches(pattern, sample5);
        Assert.assertEquals(false, result5);
    }

    @Test
    public void testIfDefine() {
        String pattern = "\\s*^/\\*\\*\\s+#ifdef\\s+[A-Za-z_]+\\w*(\\s*((==)|(!=)|(<=)|(>=)|(<)|(>))+\\s*\".+\")?\\s+\\*\\*/\\s*$";
        String sample1 = "/** #ifdef SCREEN_WIDTH ==\"=== s==s\" **/";
        String sample2 = "/**   #ifdef  a_SCREEN_WIDTH_99ssddd **/";
        String sample3 = "/**   #ifdef _SCREEN_WIDTH **/";
        String sample4 = "//**   #ifdef SCREEN_WIDTH **/";
        String sample5 = "/**   #ifdef SCREEN_WIDTH==\"176\" **/";
        String sample6 = "/**   #ifdefe SCREEN_WIDTH **/";
        String sample7 = "/**   #ifdef SCREEN_WIDTH **//";
        boolean result1 = Pattern.matches(pattern, sample1);
        Assert.assertEquals(true, result1);
        boolean result2 = Pattern.matches(pattern, sample2);
        Assert.assertEquals(true, result2);
        boolean result3 = Pattern.matches(pattern, sample3);
        Assert.assertEquals(true, result3);
        boolean result4 = Pattern.matches(pattern, sample4);
        Assert.assertEquals(false, result4);
        boolean result5 = Pattern.matches(pattern, sample5);
        Assert.assertEquals(true, result5);
        boolean result6 = Pattern.matches(pattern, sample6);
        Assert.assertEquals(false, result6);
        boolean result7 = Pattern.matches(pattern, sample7);
        Assert.assertEquals(false, result7);
    }

    @Test
    public void testIfNotDefine() {
        String pattern = "^\\s*/\\*\\*\\s+#ifndef\\s+[A-Z_]+\\w+\\s+\\*\\*/\\s*$";
        String sample1 = "/** #ifndef SCREEN_WIDTH **/";
        String sample2 = "/**   #ifndef   _SCREEN_WIDTH_99 **/";
        String sample3 = "/**   #ifndef 9_SCREEN_WIDTH **/";
        String sample4 = "//**   #ifndef SCREEN_WIDTH **/";
        String sample5 = "/**   #ifndef SCREEN_WIDTH 176 **/";
        String sample6 = "/**   #ifndefe SCREEN_WIDTH **/";
        String sample7 = "/**   #ifndef SCREEN_WIDTH **//";
        boolean result1 = Pattern.matches(pattern, sample1);
        Assert.assertEquals(true, result1);
        boolean result2 = Pattern.matches(pattern, sample2);
        Assert.assertEquals(true, result2);
        boolean result3 = Pattern.matches(pattern, sample3);
        Assert.assertEquals(false, result3);
        boolean result4 = Pattern.matches(pattern, sample4);
        Assert.assertEquals(false, result4);
        boolean result5 = Pattern.matches(pattern, sample5);
        Assert.assertEquals(false, result5);
        boolean result6 = Pattern.matches(pattern, sample6);
        Assert.assertEquals(false, result6);
        boolean result7 = Pattern.matches(pattern, sample7);
        Assert.assertEquals(false, result7);
    }

    @Test
    public void testElse() {
        String pattern = "^\\s*/\\*\\*\\s+#else\\s+\\*\\*/\\s*$";
        String sample1 = "/** #else **/";
        String sample2 = "/**   #else IDTH_99 **/";
        String sample3 = "//**   #else **/";
        String sample4 = "/**   #else 99 **/";
        String sample5 = "/**   #else SCREEN_WIDTH 176 **/";
        String sample6 = "/**   #ifdefe SCREEN_WIDTH **//";
        boolean result1 = Pattern.matches(pattern, sample1);
        Assert.assertEquals(true, result1);
        boolean result2 = Pattern.matches(pattern, sample2);
        Assert.assertEquals(false, result2);
        boolean result3 = Pattern.matches(pattern, sample3);
        Assert.assertEquals(false, result3);
        boolean result4 = Pattern.matches(pattern, sample4);
        Assert.assertEquals(false, result4);
        boolean result5 = Pattern.matches(pattern, sample5);
        Assert.assertEquals(false, result5);
        boolean result6 = Pattern.matches(pattern, sample6);
        Assert.assertEquals(false, result6);
    }

    @Test
    public void testEndIf() {
        String pattern = "^\\s*/\\*\\*\\s+#endif\\s+\\*\\*/\\s*$";
        String sample1 = "/** #endif **/";
        String sample2 = "/**   #endif IDTH_99 **/";
        String sample3 = "//**   #endif **/";
        String sample4 = "/**   #endif 99 **/";
        String sample5 = "/**   #endif SCREEN_WIDTH 176 **/";
        String sample6 = "/**   #endif SCREEN_WIDTH **//";
        boolean result1 = Pattern.matches(pattern, sample1);
        Assert.assertEquals(true, result1);
        boolean result2 = Pattern.matches(pattern, sample2);
        Assert.assertEquals(false, result2);
        boolean result3 = Pattern.matches(pattern, sample3);
        Assert.assertEquals(false, result3);
        boolean result4 = Pattern.matches(pattern, sample4);
        Assert.assertEquals(false, result4);
        boolean result5 = Pattern.matches(pattern, sample5);
        Assert.assertEquals(false, result5);
        boolean result6 = Pattern.matches(pattern, sample6);
        Assert.assertEquals(false, result6);
    }

    @Test
    public void testOutput() {
        String pattern = "^.*/\\*\\*\\s+<%\\s+[A-Za-z_]+\\w*\\s+%>\\s+\\*\\*/.*$";
        String sample1 = "bbbbb/** <% SCREEN_WIDTH %> **/bbbb";
        String sample2 = " /**   /**   <%   _SCREEN_WIDTH_9   %>   **/";
        String sample3 = "//**   <% SCREEN_WIDTH %> **/";
        String sample4 = "/**  <% SCREEN_WIDTH %> 99 **/";
        String sample5 = "if block: NUM is  /** <% NUM %> **/";
        String sample6 = "/**   <% SCREEN_WIDTH %> SCREEN_WIDTH **//";
        boolean result1 = Pattern.matches(pattern, sample1);
        Assert.assertEquals(true, result1);
        boolean result2 = Pattern.matches(pattern, sample2);
        Assert.assertEquals(true, result2);
        boolean result3 = Pattern.matches(pattern, sample3);
        Assert.assertEquals(true, result3);
        boolean result4 = Pattern.matches(pattern, sample4);
        Assert.assertEquals(false, result4);
        boolean result5 = Pattern.matches(pattern, sample5);
        Assert.assertEquals(true, result5);
        boolean result6 = Pattern.matches(pattern, sample6);
        Assert.assertEquals(false, result6);
    }

    @Test
    public void testInclude() {
        String pattern = "^\\s*/\\*\\*\\s+#include\\s+.+\\s+\\*\\*/\\s*$";
        String sample1 = "/** #include sample.txt **/";
        String sample2 = "/**   #include  /tmp/sample.txt **/";
        String sample3 = "//**  #include **/";
        String sample4 = "/**  #include 99 **/";
        String sample5 = "/** d  #include **/";
        String sample6 = "/**   #include SCREEN_WIDTH **//";
        boolean result1 = Pattern.matches(pattern, sample1);
        Assert.assertEquals(true, result1);
        boolean result2 = Pattern.matches(pattern, sample2);
        Assert.assertEquals(true, result2);
        boolean result3 = Pattern.matches(pattern, sample3);
        Assert.assertEquals(false, result3);
        boolean result4 = Pattern.matches(pattern, sample4);
        Assert.assertEquals(true, result4);
        boolean result5 = Pattern.matches(pattern, sample5);
        Assert.assertEquals(false, result5);
        boolean result6 = Pattern.matches(pattern, sample6);
        Assert.assertEquals(false, result6);
    }

    @Test
    public void testPackage() {
        String pattern = "^\\s*package\\s+.+;\\s*$";
        String sample1 = "package com.songdi.bb; ";
        String sample2 = "   package com.songdi.bb;";
        String sample3 = "package com.songdi.bb;   ";
        String sample4 = " package com.songdi.bb ;  ";
        String sample5 = "ss package com.songdi.bb;";
        String sample6 = " package com:songdi.bb; ";
        boolean result1 = Pattern.matches(pattern, sample1);
        Assert.assertEquals(true, result1);
        boolean result2 = Pattern.matches(pattern, sample2);
        Assert.assertEquals(true, result2);
        boolean result3 = Pattern.matches(pattern, sample3);
        Assert.assertEquals(true, result3);
        boolean result4 = Pattern.matches(pattern, sample4);
        Assert.assertEquals(true, result4);
        boolean result5 = Pattern.matches(pattern, sample5);
        Assert.assertEquals(false, result5);
        boolean result6 = Pattern.matches(pattern, sample6);
        Assert.assertEquals(true, result6);
    }

    @Test
    public void testDefineBoolean() {
        String pattern = "^\\s*/\\*\\*\\s+#define\\s+[A-Za-z_]+\\w*\\s+((true)|(True)|(TRUE)){1}\\s+\\*\\*/\\s*$";
        String sample1 = "/** #define Bool  True **/";
        String sample2 = "/** #define Bool  true **/";
        String sample3 = "/** #define Bool  TRUE **/ ";
        String sample4 = "/** #define Bool ddd **/";
        String sample5 = "/** #define Bool 123 **/";
        String sample6 = "/** #define Bool Tdd dd **/";
        boolean result1 = Pattern.matches(pattern, sample1);
        Assert.assertEquals(true, result1);
        boolean result2 = Pattern.matches(pattern, sample2);
        Assert.assertEquals(true, result2);
        boolean result3 = Pattern.matches(pattern, sample3);
        Assert.assertEquals(true, result3);
        boolean result4 = Pattern.matches(pattern, sample4);
        Assert.assertEquals(false, result4);
        boolean result5 = Pattern.matches(pattern, sample5);
        Assert.assertEquals(false, result5);
        boolean result6 = Pattern.matches(pattern, sample6);
        Assert.assertEquals(false, result6);
    }

    @Test
    public void testDefineNumber() {
        String pattern = "^\\s*/\\*\\*\\s+#define\\s+[A-Za-z_]+\\w*\\s+[+-]?\\d+(\\.\\d+)?\\s+\\*\\*/\\s*$";
        String sample1 = "/** #define NUM  -100.0 **/";
        String sample2 = "/** #define NUM  +10.103 **/";
        String sample3 = "/** #define NUM  0.55 **/ ";
        String sample4 = "/** #define NUM 5. **/";
        String sample5 = "/** #define NUM 5.5.5 **/";
        String sample6 = "/** #define NUM .66 **/";
        boolean result1 = Pattern.matches(pattern, sample1);
        Assert.assertEquals(true, result1);
        boolean result2 = Pattern.matches(pattern, sample2);
        Assert.assertEquals(true, result2);
        boolean result3 = Pattern.matches(pattern, sample3);
        Assert.assertEquals(true, result3);
        boolean result4 = Pattern.matches(pattern, sample4);
        Assert.assertEquals(false, result4);
        boolean result5 = Pattern.matches(pattern, sample5);
        Assert.assertEquals(false, result5);
        boolean result6 = Pattern.matches(pattern, sample6);
        Assert.assertEquals(false, result6);
        String s = "-100.01";
        float f = Float.parseFloat(s);
        System.out.println(f);
    }

    @Test
    public void testFloat() {
        String pattern = "[+-]?\\d+(\\.\\d+)?";
        String sample1 = "-100.0";
        String sample2 = "+10.103";
        String sample3 = "0.55";
        String sample4 = "5.";
        String sample5 = "0";
        String sample6 = ".66";
        boolean result1 = Pattern.matches(pattern, sample1);
        Assert.assertEquals(true, result1);
        boolean result2 = Pattern.matches(pattern, sample2);
        Assert.assertEquals(true, result2);
        boolean result3 = Pattern.matches(pattern, sample3);
        Assert.assertEquals(true, result3);
        boolean result4 = Pattern.matches(pattern, sample4);
        Assert.assertEquals(false, result4);
        boolean result5 = Pattern.matches(pattern, sample5);
        Assert.assertEquals(true, result5);
        boolean result6 = Pattern.matches(pattern, sample6);
        Assert.assertEquals(false, result6);
        String s = "-100.01";
        float f = Float.parseFloat(s);
        System.out.println(f);
    }
}
