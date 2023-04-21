package es.seat131.viewerfree.command;

import static org.junit.Assert.assertEquals;
import org.junit.Before;
import org.junit.Test;
import org.springframework.mock.web.MockHttpServletRequest;
import es.seat131.viewerfree.common.ActionForward;

public class LogoutCommandTest {

    private static MockHttpServletRequest _httpServletRequestMock;

    @Before
    public void initialize() {
        _httpServletRequestMock = new MockHttpServletRequest();
    }

    @Test
    public void execute() throws Exception {
        assertEquals(ActionForward.PICTURE, new PictureNavigatorCommand().execute(_httpServletRequestMock, null));
    }
}
