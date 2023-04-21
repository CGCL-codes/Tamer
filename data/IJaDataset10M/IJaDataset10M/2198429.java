package com.nhncorp.usf.web.taglibs.pager;

import static org.easymock.EasyMock.createMock;
import java.util.Enumeration;
import javax.servlet.jsp.JspException;
import javax.servlet.jsp.PageContext;
import javax.servlet.jsp.tagext.Tag;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import com.nhncorp.lucy.common.data.DataHandler;
import com.nhncorp.lucy.common.data.DataHandlerFactory;
import com.nhncorp.usf.core.result.template.directive.PagerInfo;

/**
 * @author Web Platform Development Team
 */
public class PagerTagTest {

    static DataHandler dataHandler = null;

    @BeforeClass
    public static void setUpBeforeClass() throws Exception {
    }

    @AfterClass
    public static void tearDownAfterClass() throws Exception {
    }

    @Before
    public void setUp() throws Exception {
    }

    @After
    public void tearDown() throws Exception {
        PagerTagForTest pagerTag = new PagerTagForTest();
        pagerTag.appendPageParameter("Test", 1, 1, false);
    }

    @Test
    public void appendPageParameterTest() {
        PagerTagForTest pagerTag = new PagerTagForTest();
        pagerTag.appendPageParameter("Test", 1, 1, false);
        pagerTag.appendPageParameter(null, 1, 1, false);
        pagerTag.appendPageParameter("a&page=", 1, 1, false);
        pagerTag.appendPageParameter("a&page=", 1, 1, true);
    }

    @Test
    public void getRequestURLTest() {
        PageContext pageContext = new MockPageContext(null, null, null);
        PagerTagForTest pagerTag = new PagerTagForTest();
        pageContext.setAttribute("requestURI", "test?");
        pagerTag.getRequestURL(pageContext);
    }

    @Test
    public void getQueryStringTest() {
        PageContext pageContext = new MockPageContext(null, null, null);
        pageContext.setAttribute("queryString", "test");
        PagerTagForTest pagerTag = new PagerTagForTest();
        pagerTag.setPageContext(pageContext);
        pagerTag.getQueryString(null);
    }

    @Test
    public void getStartTagTest() {
        PageContext pageContext = new MockPageContext(null, null, null);
        pageContext.setAttribute("requestURI", "test");
        PagerTagForTest pagerTag = new PagerTagForTest();
        pagerTag.setPageContext(pageContext);
        NavigatorTag navigatorTag = new NavigatorTag();
        navigatorTag.setAttributes("test");
        pagerTag.getStartTag(navigatorTag, "test", 1, "test");
        String[] s = { "aaa", "bbb" };
        pagerTag.appendings = s;
        pagerTag.getStartTag(navigatorTag, "test", 1, "test");
    }

    @SuppressWarnings("static-access")
    @Test
    public void resolveTest() {
        DataHandlerFactory dataHandlerFactory = new DataHandlerFactory();
        dataHandler = createMock(DataHandler.class);
        dataHandlerFactory.init("test", dataHandler);
        PagerTagForTest pagerTag = new PagerTagForTest();
        pagerTag.resolve("$test", "test");
        pagerTag.resolve("test", "test");
    }

    @SuppressWarnings("serial")
    public class PagerTagForTest extends PagerTag {

        @Override
        protected String appendPageParameter(String queryString, int page, int totalRows, boolean appendTotalRows) {
            return super.appendPageParameter(queryString, page, totalRows, appendTotalRows);
        }

        @Override
        public int doStartTag() throws JspException {
            return super.doStartTag();
        }

        @Override
        protected void failed(Exception ex) {
        }

        @Override
        protected void generate(NavigatorTag parent, PagerInfo pagerInfo) throws Exception {
        }

        @Override
        protected String getQueryString(PagerInfo pagerInfo) {
            return super.getQueryString(pagerInfo);
        }

        @Override
        protected String getRequestURL(PageContext context) {
            return super.getRequestURL(context);
        }

        @Override
        public void setAppendings(String appendings) {
            super.setAppendings(appendings);
        }

        @Override
        public void setPageName(String pageName) {
            super.setPageName(pageName);
        }

        @Override
        public void setTotalRowsName(String totalRowsName) {
            super.setTotalRowsName(totalRowsName);
        }

        @Override
        public int doAfterBody() throws JspException {
            return super.doAfterBody();
        }

        @Override
        public int doEndTag() throws JspException {
            return super.doEndTag();
        }

        @Override
        public String getId() {
            return super.getId();
        }

        @Override
        public Tag getParent() {
            return super.getParent();
        }

        @Override
        public Object getValue(String k) {
            return super.getValue(k);
        }

        @SuppressWarnings("unchecked")
        @Override
        public Enumeration getValues() {
            return super.getValues();
        }

        @Override
        public void release() {
            super.release();
        }

        @Override
        public void removeValue(String k) {
            super.removeValue(k);
        }

        @Override
        public void setId(String id) {
            super.setId(id);
        }

        @Override
        public void setPageContext(PageContext pageContext) {
            super.setPageContext(pageContext);
        }

        @Override
        public void setParent(Tag t) {
            super.setParent(t);
        }

        @Override
        public void setValue(String k, Object o) {
            super.setValue(k, o);
        }

        @Override
        protected Object clone() throws CloneNotSupportedException {
            return super.clone();
        }

        @Override
        public boolean equals(Object arg0) {
            return super.equals(arg0);
        }

        @Override
        protected void finalize() throws Throwable {
            super.finalize();
        }

        @Override
        public int hashCode() {
            return super.hashCode();
        }

        @Override
        public String toString() {
            return super.toString();
        }
    }
}
