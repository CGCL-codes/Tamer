package net.sourceforge.cruisecontrol.dashboard.web;

import net.sourceforge.cruisecontrol.dashboard.BuildSummary;
import net.sourceforge.cruisecontrol.dashboard.CurrentStatus;
import net.sourceforge.cruisecontrol.dashboard.PreviousResult;
import net.sourceforge.cruisecontrol.dashboard.service.BuildLoopQueryService;
import net.sourceforge.cruisecontrol.dashboard.service.BuildSummaryUIService;
import net.sourceforge.cruisecontrol.dashboard.service.DashboardConfigService;
import net.sourceforge.cruisecontrol.dashboard.service.DashboardXmlConfigService;
import net.sourceforge.cruisecontrol.dashboard.service.EnvironmentService;
import net.sourceforge.cruisecontrol.dashboard.service.HistoricalBuildSummariesService;
import net.sourceforge.cruisecontrol.dashboard.service.LatestBuildSummariesService;
import net.sourceforge.cruisecontrol.dashboard.testhelpers.DataUtils;
import org.apache.commons.lang.StringUtils;
import org.jmock.Mock;
import org.jmock.cglib.MockObjectTestCase;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class GetProjectBuildStatusXmlControllerTest extends MockObjectTestCase {

    private GetProjectBuildStatusXmlController controller;

    private MockHttpServletRequest request;

    private MockHttpServletResponse response;

    private Mock mockLatestBuildSummariesService;

    private BuildSummary oneBuild;

    private Mock buildSummaryUIService;

    protected void setUp() throws Exception {
        oneBuild = new BuildSummary("project1", PreviousResult.PASSED, DataUtils.PASSING_BUILD_LBUILD_0_XML);
        mockLatestBuildSummariesService = mock(LatestBuildSummariesService.class, new Class[] { HistoricalBuildSummariesService.class, BuildLoopQueryService.class }, new Object[] { null, null });
        LatestBuildSummariesService buildSummariesService = (LatestBuildSummariesService) mockLatestBuildSummariesService.proxy();
        final EnvironmentService environmentService = new EnvironmentService(new DashboardConfigService[] {});
        BuildLoopQueryService buildLoopQueryService = new BuildLoopQueryService(environmentService, null) {

            public Map<String, String> getAllProjectsStatus() {
                Map<String, String> map = new HashMap<String, String>();
                map.put("project1", "now building since");
                return map;
            }
        };
        buildSummaryUIService = mock(BuildSummaryUIService.class, new Class[] { HistoricalBuildSummariesService.class, DashboardXmlConfigService.class }, new Object[] { null, null });
        controller = new GetProjectBuildStatusXmlController(buildSummariesService, buildLoopQueryService, (BuildSummaryUIService) buildSummaryUIService.proxy());
        request = new MockHttpServletRequest();
        response = new MockHttpServletResponse();
        request.setMethod("GET");
    }

    private List returnedValue() {
        List list = new ArrayList();
        BuildSummary build2 = new BuildSummary("project2", PreviousResult.UNKNOWN, DataUtils.FAILING_BUILD_XML);
        build2.updateStatus(CurrentStatus.WAITING.getCruiseStatus());
        list.add(oneBuild);
        list.add(build2);
        return list;
    }

    public void testShouldReturnAllFieldsInCCTrayFormat() throws Exception {
        buildSummaryUIService.expects(once()).method("toXml").will(returnValue(""));
        mockLatestBuildSummariesService.expects(once()).method("getLatestOfProjects").withNoArguments().will(returnValue(returnedValue()));
        request.setRequestURI("/dashboard/cctray.xml");
        controller.handleRequest(request, response);
        String xml = response.getContentAsString();
        assertEquals("text/xml", response.getContentType());
        assertFalse(xml, StringUtils.contains(xml, "name=\"project2\""));
        assertFalse(xml, StringUtils.contains(xml, "lastBuildLabel=\"build2\""));
    }

    public void testShouldReturnAllFieldsInRssFormat() throws Exception {
        buildSummaryUIService.expects(once()).method("toXml").will(returnValue(""));
        mockLatestBuildSummariesService.expects(once()).method("getLatestOfProjects").withNoArguments().will(returnValue(returnedValue()));
        request.setRequestURI("/dashboard/rss.xml");
        controller.handleRequest(request, response);
        String xml = response.getContentAsString();
        assertEquals("text/xml", response.getContentType());
        assertTrue(StringUtils.contains(xml, "<?xml version=\"1.0\" encoding=\"UTF-8\"?>"));
        assertTrue(StringUtils.contains(xml, "<rss version=\"2.0\">"));
        assertTrue(StringUtils.contains(xml, "<channel>"));
        assertTrue(StringUtils.contains(xml, "<title>CruiseControl Results</title>"));
        assertTrue(StringUtils.contains(xml, "<link>http://localhost:80/dashboard/</link>"));
        assertTrue(StringUtils.contains(xml, "<language>en-us</language>"));
        assertTrue(StringUtils.contains(xml, "</channel>"));
        assertTrue(StringUtils.contains(xml, "<channel>"));
        assertTrue(StringUtils.contains(xml, "</rss>"));
    }

    public void testShouldReturnSpecificRssFormat() throws Exception {
        mockLatestBuildSummariesService.expects(once()).method("getLatestProject").with(eq("project1")).will(returnValue(oneBuild));
        buildSummaryUIService.expects(once()).method("toXml").will(returnValue(""));
        request.setRequestURI("/dashboard/rss.xml");
        request.addParameter("projectName", "project1");
        controller.handleRequest(request, response);
        String xml = response.getContentAsString();
        assertTrue(StringUtils.contains(xml, "<?xml version=\"1.0\" encoding=\"UTF-8\"?>"));
        assertTrue(StringUtils.contains(xml, "<rss version=\"2.0\">"));
        assertTrue(StringUtils.contains(xml, "<channel>"));
        assertTrue(StringUtils.contains(xml, "<title>CruiseControl Results</title>"));
        assertTrue(StringUtils.contains(xml, "<link>http://localhost:80/dashboard/</link>"));
        assertTrue(StringUtils.contains(xml, "<language>en-us</language>"));
        assertTrue(StringUtils.contains(xml, "</channel>"));
        assertTrue(StringUtils.contains(xml, "<channel>"));
        assertTrue(StringUtils.contains(xml, "</rss>"));
    }

    public void testPostMethodIsNotAllowed() throws Exception {
        request.setMethod("POST");
        try {
            controller.handleRequest(request, response);
            fail("exception exptected");
        } catch (HttpRequestMethodNotSupportedException e) {
        }
    }
}
