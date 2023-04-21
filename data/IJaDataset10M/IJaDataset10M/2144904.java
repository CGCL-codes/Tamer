package sdloader.javaee.webxml;

import java.util.List;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;
import junit.framework.TestCase;

public class WebXmlParseHandlerTest extends TestCase {

    public void testWebXmlParseHandler() throws Exception {
        try {
            SAXParser parser = SAXParserFactory.newInstance().newSAXParser();
            WebXmlParseHandler handler = new WebXmlParseHandler();
            handler.register("-//Sun Microsystems, Inc.//DTD Web Application 2.3//EN", WebXmlParseHandler.class.getResource("/sdloader/resource/web-app_2_3.dtd"));
            parser.parse(WebXmlParseHandlerTest.class.getResourceAsStream("web.xml"), handler);
            WebAppTag webAppTag = (WebAppTag) handler.getRootObject();
            assertContextParamTag(webAppTag);
            assertFilterTag(webAppTag);
            assertFilterMappingTag(webAppTag);
            assertServletTag(webAppTag);
            assertServletMappingTag(webAppTag);
            assertWelcomeFileListTag(webAppTag);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void assertContextParamTag(WebAppTag webAppTag) {
        List<ContextParamTag> contextParamTagList = webAppTag.getContextParam();
        assertEquals(2, contextParamTagList.size());
        ContextParamTag contextParamTag1 = (ContextParamTag) contextParamTagList.get(0);
        assertEquals("CONTEXT-PARAM-NAME1", contextParamTag1.getParamName());
        assertEquals("CONTEXT-PARAM-VALUE1", contextParamTag1.getParamValue());
        assertEquals("CONTEXT-PARAM-DESC11CONTEXT-PARAM-DESC12", contextParamTag1.getDescription());
        ContextParamTag contextParamTag2 = (ContextParamTag) contextParamTagList.get(1);
        assertEquals("CONTEXT-PARAM-NAME2", contextParamTag2.getParamName());
        assertEquals("CONTEXT-PARAM-VALUE2", contextParamTag2.getParamValue());
        assertEquals("CONTEXT-PARAM-DESC21CONTEXT-PARAM-DESC22", contextParamTag2.getDescription());
    }

    private void assertFilterTag(WebAppTag webAppTag) {
        List<FilterTag> filterTagList = webAppTag.getFilter();
        assertEquals(2, filterTagList.size());
        FilterTag filter1 = (FilterTag) filterTagList.get(0);
        assertEquals("FILTER-NAME1", filter1.getFilterName());
        assertEquals("FILTER-CLASS1", filter1.getFilterClass());
        InitParamTag initParamTag1 = (InitParamTag) filter1.getInitParamList().get(0);
        assertEquals("FILTER-PARAM-NAME1", initParamTag1.getParamName());
        assertEquals("FILTER-PARAM-VALUE1", initParamTag1.getParamValue());
        InitParamTag initParamTag11 = (InitParamTag) filter1.getInitParamList().get(1);
        assertEquals("FILTER-PARAM-NAME11", initParamTag11.getParamName());
        assertEquals("FILTER-PARAM-VALUE11", initParamTag11.getParamValue());
        assertEquals("FILTER-PARAM-VALUE1", filter1.getInitParam("FILTER-PARAM-NAME1"));
        assertEquals("FILTER-PARAM-VALUE11", filter1.getInitParam("FILTER-PARAM-NAME11"));
        FilterTag filter2 = (FilterTag) filterTagList.get(1);
        assertEquals("FILTER-NAME2", filter2.getFilterName());
        assertEquals("FILTER-CLASS2", filter2.getFilterClass());
        InitParamTag initParamTag2 = (InitParamTag) filter2.getInitParamList().get(0);
        assertEquals("FILTER-PARAM-NAME2", initParamTag2.getParamName());
        assertEquals("FILTER-PARAM-VALUE2", initParamTag2.getParamValue());
    }

    private void assertFilterMappingTag(WebAppTag webAppTag) {
        List<FilterMappingTag> filterMappingTagList = webAppTag.getFilterMapping();
        assertEquals(1, filterMappingTagList.size());
        assertEquals("FILTER-NAME", filterMappingTagList.get(0).getFilterName());
        assertEquals("URL-PATTERN", filterMappingTagList.get(0).getUrlPattern());
    }

    private void assertServletTag(WebAppTag webAppTag) {
        List<ServletTag> servletTagList = webAppTag.getServlet();
        assertEquals(1, servletTagList.size());
        ServletTag servlet1 = (ServletTag) servletTagList.get(0);
        assertEquals("SERVLET-NAME", servlet1.getServletName());
        assertEquals("SERVLET-CLASS", servlet1.getServletClass());
        assertEquals(0, servlet1.getLoadOnStartup().intValue());
        InitParamTag initParamTag1 = (InitParamTag) servlet1.getInitParamList().get(0);
        assertEquals("PARAM-NAME", initParamTag1.getParamName());
        assertEquals("PARAM-VALUE", initParamTag1.getParamValue());
    }

    private void assertServletMappingTag(WebAppTag webAppTag) {
        List<ServletMappingTag> servletMappingTagList = webAppTag.getServletMapping();
        assertEquals(1, servletMappingTagList.size());
        assertEquals("SERVLET-NAME", servletMappingTagList.get(0).getServletName());
        assertEquals("URL-PATTERN", servletMappingTagList.get(0).getUrlPattern());
    }

    private void assertWelcomeFileListTag(WebAppTag webAppTag) {
        WelcomeFileListTag welcomeFileListTag = webAppTag.getWelcomeFileList();
        List<String> welcomeList = welcomeFileListTag.getWelcomeFileList();
        assertEquals(2, welcomeList.size());
        assertEquals("WELCOME-FILE1", (String) welcomeList.get(0));
        assertEquals("WELCOME-FILE2", (String) welcomeList.get(1));
    }
}
