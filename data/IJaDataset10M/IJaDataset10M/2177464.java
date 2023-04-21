package net.sf.lavalamp.application;

import static org.mockito.Matchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Calendar;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import net.sf.lavalamp.site.MissingBuildException;
import net.sf.lavalamp.device.DeviceController;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

public class BuildStatusServletTest {

    @Mock
    DeviceController ac;

    @Mock
    HttpServletRequest request;

    @Mock
    HttpServletResponse response;

    @Mock
    PrintWriter out;

    @Before
    public void setUp() throws Exception {
        MockitoAnnotations.initMocks(this);
        when(response.getWriter()).thenReturn(out);
    }

    @Test
    public void testDoGetHttpServletRequestHttpServletResponse() throws ServletException, IOException, MissingBuildException {
        when(request.getParameter(BuildStatusServlet.BUILD_ID)).thenReturn("1");
        when(request.getParameter(BuildStatusServlet.SUCCESSFUL)).thenReturn("true");
        BuildStatusServlet servlet = new BuildStatusServlet(ac);
        servlet.doGet(request, response);
        verify(ac).setStatusForBuild("1", true);
        verify(ac).switchDevices(any(Calendar.class));
    }

    @Test
    public void testDoGetHttpServletRequestHttpServletResponseFail() throws ServletException, IOException, MissingBuildException {
        when(request.getParameter(BuildStatusServlet.BUILD_ID)).thenReturn("1");
        when(request.getParameter(BuildStatusServlet.SUCCESSFUL)).thenReturn("false");
        BuildStatusServlet servlet = new BuildStatusServlet(ac);
        servlet.doGet(request, response);
        verify(ac).setStatusForBuild("1", false);
        verify(ac).switchDevices(any(Calendar.class));
    }
}
