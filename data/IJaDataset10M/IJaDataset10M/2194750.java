package com.rallydev.integration.build.ant;

import org.apache.tools.ant.BuildEvent;
import org.apache.tools.ant.Project;
import org.easymock.MockControl;
import com.rallydev.integration.build.plugin.Build;
import com.rallydev.integration.build.rest.AbstractRestTest;
import com.rallydev.integration.build.rest.RallyRestService;

public class RallyAntBuildListenerTest extends AbstractRestTest {

    private static final String BUILD_SUCCESS = "build-success.xml";

    private static final String BUILD_FAILURE = "build-failure.xml";

    private static final String TEST_URL = "http://localhost:7001/slm/webservice/1.16";

    private RallyAntBuildListener listener;

    private RallyRestService serviceMock;

    private MockControl control;

    protected void setUp() throws Exception {
        control = MockControl.createControl(RallyRestService.class);
        serviceMock = (RallyRestService) control.getMock();
        control.expectAndReturn(serviceMock.getUrl(), TEST_URL);
        listener = new RallyAntBuildListener() {

            protected Build createBuild(String username, String password, String host) {
                Build build = new Build(serviceMock);
                return build;
            }
        };
    }

    public void testBuildFinishedSuccess() throws Exception {
        control.expectAndReturn(serviceMock.create(readFile(BUILD_SUCCESS)), "Response Success");
        control.replay();
        BuildEvent buildEvent = getBuildEvent();
        listener.buildFinished(buildEvent);
        control.verify();
    }

    public void testBuildFinishedFailure() throws Exception {
        control.expectAndReturn(serviceMock.create(readFile(BUILD_FAILURE)), "Response Success");
        control.replay();
        BuildEvent buildEvent = getBuildEvent();
        buildEvent.setException(new Exception("This is a test exception"));
        listener.buildFinished(buildEvent);
        control.verify();
    }

    public void testBuildFinishedIOFailure() throws Exception {
        control.expectAndThrow(serviceMock.create(readFile(BUILD_FAILURE)), new RuntimeException("I failed big time!"));
        control.replay();
        BuildEvent buildEvent = getBuildEvent();
        buildEvent.setException(new Exception("This is a test exception"));
        try {
            listener.buildFinished(buildEvent);
            fail();
        } catch (RuntimeException e) {
            assertEquals("I failed big time!", e.getMessage());
        }
        control.verify();
    }

    private BuildEvent getBuildEvent() {
        BuildEvent buildEvent = new BuildEvent(new Project());
        buildEvent.getProject().setNewProperty("rally.username", "testUserName");
        buildEvent.getProject().setNewProperty("rally.password", "testPassword");
        buildEvent.getProject().setNewProperty("rally.host", "localhost:8080");
        buildEvent.getProject().setNewProperty("rally.buildDefinitionId", "5169");
        buildEvent.getProject().setNewProperty("ant.project.name", "testProject");
        return buildEvent;
    }
}
