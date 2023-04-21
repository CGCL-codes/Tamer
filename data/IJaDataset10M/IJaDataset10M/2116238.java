package net.sourceforge.cruisecontrol.dashboard.service;

import net.sourceforge.cruisecontrol.dashboard.BuildDetail;
import net.sourceforge.cruisecontrol.dashboard.testhelpers.DataUtils;
import org.apache.commons.lang.StringUtils;
import org.jmock.Mock;
import org.jmock.cglib.MockObjectTestCase;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class WidgetPluginServiceTest extends MockObjectTestCase {

    private WidgetPluginService service;

    private BuildDetail buildDetail;

    private Mock dashboardConfigMock;

    protected void setUp() throws Exception {
        dashboardConfigMock = mock(DashboardXmlConfigService.class, new Class[] { DashboardConfigFileFactory.class }, new Object[] { null });
        service = new WidgetPluginService((DashboardXmlConfigService) dashboardConfigMock.proxy());
        Map props = new HashMap();
        props.put("projectname", "project1");
        buildDetail = new BuildDetail(DataUtils.getFailedBuildLbuildAsFile(), props);
    }

    protected void tearDown() throws Exception {
        buildDetail.getPluginOutputs().clear();
    }

    public void testShouldBeAbleToReturnEmptyListWhenCfgIsNotDefined() throws Exception {
        dashboardConfigMock.expects(once()).method("getSubTabClassNames").will(returnValue(new ArrayList()));
        service.mergePluginOutput(buildDetail, new HashMap());
        assertEquals(0, buildDetail.getPluginOutputs().size());
    }

    public void testShouldIgnoreNonExistentClassAndContinueInitializingTheResult() throws Exception {
        dashboardConfigMock.expects(once()).method("getSubTabClassNames").will(returnValue(assembleSubTabs()));
        service.mergePluginOutput(buildDetail, new HashMap());
        assertEquals(1, buildDetail.getPluginOutputs().size());
    }

    private List assembleSubTabs() {
        return Arrays.asList(new String[] { "not.exist.class", "net.sourceforge.cruisecontrol.dashboard.widgets.MergedCheckStyleWidget" });
    }

    public void testShouldBeAbleToReturnInitializedServiceWhenCfgIsDefined() throws Exception {
        dashboardConfigMock.expects(once()).method("getSubTabClassNames").will(returnValue(assembleSubTabs()));
        service.mergePluginOutput(buildDetail, new HashMap());
        assertTrue(buildDetail.getPluginOutputs().containsKey("Merged Check Style"));
        String content = (String) buildDetail.getPluginOutputs().get("Merged Check Style");
        assertTrue(StringUtils.contains(content, "Line has trailing spaces."));
    }

    public void testShouldNOTThrowExceptionWhenLineStartedWithHash() throws Exception {
        try {
            service.assemblePlugin(buildDetail, new HashMap(), "#this is a comment");
        } catch (Exception e) {
            fail();
        }
    }
}
