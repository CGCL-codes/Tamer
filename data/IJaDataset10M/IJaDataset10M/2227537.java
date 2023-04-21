package es.seat131.viewerfree.command.mail;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;
import org.springframework.mock.web.MockHttpServletRequest;
import es.seat131.viewerfree.common.ActionForward;
import es.seat131.viewerfree.common.ParamKey;
import es.seat131.viewerfree.dto.Message;
import es.seat131.viewerfree.service.IEmailService;

public class ForwardMessageCommandTest extends ReplyAllMessageCommandTest {

    private static final String ID = "1";

    private ForwardMessageCommand _forwardMessageCommand;

    private MockHttpServletRequest _request;

    private IEmailService _emailService;

    private static final String SUBJECT = "SUBJECT";

    private static final String EMAIL = "EMAIL";

    private static final String NAME = "NAME";

    private static final String TEXT = "Texto";

    @Before
    public void setUp() throws Exception {
        _emailService = Mockito.mock(IEmailService.class);
        _request = new MockHttpServletRequest();
        _forwardMessageCommand = new ForwardMessageCommand();
    }

    @Test
    public void testExecuteGmail() throws Exception {
        fixtureAttribute();
        Message message = createMessage();
        Mockito.when(_emailService.getMessage(Integer.valueOf(ID))).thenReturn(message);
        assertEquals(ActionForward.GMAIL_CREATE_MESSAGE, _forwardMessageCommand.executeGmail(_request, null, _emailService));
        assertEquals(message, _request.getAttribute(ParamKey.MESSAGE.toString()));
        assertNull(message.getCc());
        assertEquals("", message.getFrom());
    }

    private void fixtureAttribute() {
        _request.setAttribute(ParamKey.ID_MESSAGE.toString(), ID);
    }

    private Message createMessage() {
        Message message = new Message();
        message.setText(TEXT);
        message.setFrom(NAME + "&lt;" + EMAIL + "&gt;");
        message.setSubject(SUBJECT);
        message.setSentDate(new Date());
        List<String> tos = new ArrayList<String>();
        tos.add(EMAIL);
        message.setTo(tos);
        message.setCc(tos);
        return message;
    }
}
