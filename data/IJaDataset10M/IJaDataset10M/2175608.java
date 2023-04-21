package com.sitechasia.webx.core.utils.populator;

import java.util.HashMap;
import java.util.Map;
import servletunit.HttpServletRequestSimulator;
import com.sitechasia.webx.core.utils.populator.PriTypeConverter;
import com.sitechasia.webx.core.utils.populator.ServletRequestBeanPopulator;
import com.sitechasia.webx.core.utils.populator.WebXTypeConverter;
import com.sitechasia.webx.test.BaseActionTestCase;

/**
 * <p>Title: ServletRequestBeanPopulatorTest</p>
 * <p>Description:ServletRequestBeanPopulator单元测试类 </p>
 *
 * @author mashaojing
 * @version 1.0
 */
public class ServletRequestBeanPopulatorTest extends BaseActionTestCase {

    public ServletRequestBeanPopulatorTest(String name) {
        super(name);
    }

    /**
	 * 执行初始化操作
	 */
    protected void setUp() throws Exception {
        super.setUp();
    }

    /**
	 * 执行清除操作
	 */
    public void tearDown() throws Exception {
    }

    public void testRequest2Bean() {
        HttpServletRequestSimulator requestsimulator = this.getMockRequest();
        requestsimulator.addParameter("id", new String[] { "1", "2" });
        requestsimulator.addParameter("name", new String[] { "junit in action", "c++" });
        requestsimulator.addParameter("publishdate", new String[] { "2004-06-25" });
        ServletRequestBeanPopulator populator = new ServletRequestBeanPopulator();
        WebXTypeConverter converter = new WebXTypeConverter();
        PriTypeConverter priConverter = new PriTypeConverter();
        converter.getConverters().add(priConverter);
        populator.setConverter(converter);
        A beanA = new A();
        Map map = new HashMap();
        populator.populate(requestsimulator, map, null, null);
        assertEquals("junit in action", ((String[]) map.get("name"))[0]);
        assertEquals("1", ((String[]) map.get("id"))[0]);
        assertEquals("2004-06-25", ((String[]) map.get("publishdate"))[0]);
        Map<String, String> mapping = new HashMap<String, String>();
        mapping.put("bookid", "id");
        mapping.put("bookname", "name");
        mapping.put("bookpublishdate", "publishdate");
        populator.populate(requestsimulator, beanA, mapping, null);
        assertEquals(2, beanA.getBookid()[1]);
        assertEquals("junit in action", beanA.getBookname()[0]);
        assertEquals("2004-06-25", beanA.getBookpublishdate()[0]);
    }

    public class A {

        private int[] bookid;

        private String[] bookname;

        private String[] bookpublishdate;

        public int[] getBookid() {
            return bookid;
        }

        public void setBookid(int[] bookid) {
            this.bookid = bookid;
        }

        public String[] getBookname() {
            return bookname;
        }

        public void setBookname(String[] bookname) {
            this.bookname = bookname;
        }

        public void setBookpublishdate(String[] bookpublishdate) {
            this.bookpublishdate = bookpublishdate;
        }

        public String[] getBookpublishdate() {
            return bookpublishdate;
        }
    }
}
