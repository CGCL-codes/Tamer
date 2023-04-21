package es.seat131.javi.command;

import static org.junit.Assert.assertEquals;
import javax.servlet.http.HttpServletRequestMock;
import javax.servlet.http.HttpSessionMock;
import org.junit.Before;
import org.junit.Test;
import es.seat131.javi.common.ActionForward;

public class LogoutCommandTest {

    private static HttpSessionMock _httpSessionMock;

    private static HttpServletRequestMock _httpServletRequestMock;

    @Before
    public void initialize() {
        _httpServletRequestMock = new HttpServletRequestMock();
        _httpSessionMock = (HttpSessionMock) _httpServletRequestMock.getSession();
    }

    @Test
    public void execute() throws Exception {
        assertEquals(ActionForward.FOTO, new FotoNavigatorCommand().execute(_httpServletRequestMock, null));
    }
}
