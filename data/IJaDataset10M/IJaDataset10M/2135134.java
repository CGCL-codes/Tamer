package org.fto.jthink.j2ee.web.util;

import java.io.FileNotFoundException;
import junit.framework.TestCase;

/**
 * @author Administrator
 *
 * TODO 要更改此生成的类型注释的模板，请转至
 * 窗口 － 首选项 － Java － 代码样式 － 代码模板
 */
public class HTMLHelperTestCase extends TestCase {

    public static void main(String[] args) {
        junit.textui.TestRunner.run(HTMLHelperTestCase.class);
    }

    /**
	 * 测试 parseToHashMap
	 */
    public static void testtoHTMLString() {
        System.out.println("\n[正在测试方法: HTMLHelper.toHTMLString()...]");
        System.out.println("Text out:" + HTMLHelper.toHTMLString(""));
        System.out.println("Text out:" + HTMLHelper.toHTMLString(null));
        System.out.println("Text out:" + HTMLHelper.toHTMLString("", HTMLHelper.ES_DEF$INPUT));
        System.out.println("Text out:" + HTMLHelper.toHTMLString(null, HTMLHelper.ES_DEF$INPUT));
        System.out.println("Text out:" + HTMLHelper.toHTMLString("", new String[][] { { "", "" } }));
        System.out.println("Text out:" + HTMLHelper.toHTMLString(null, new String[][] { { "", "" } }));
    }

    /**
   * 测试 encode
   */
    public static void testencode() {
        System.out.println("\n[正在测试方法: HTMLHelper.encode()...]");
        System.out.println("Text out:" + HTMLHelper.encode("1234abcd!@#$%^&*()_+{}|[]\":;'?><,./测试\n汉字"));
    }
}
