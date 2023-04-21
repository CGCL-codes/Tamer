package nl.gridshore.newsfeed.web.receivers;

import com.google.appengine.api.xmpp.JID;
import com.google.appengine.api.xmpp.Message;
import com.google.appengine.api.xmpp.XMPPService;
import com.google.appengine.api.xmpp.XMPPServiceFactory;
import nl.gridshore.newsfeed.integration.xmpp.XmppMessagingService;
import nl.gridshore.newsfeed.service.ReceivedMessageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * Controller class for handling incoming xmpp (google talk) posts
 *
 * @author Jettro Coenradie
 */
@Controller
public class XmppMessageReceiverController {

    private XmppMessagingService xmppMessagingService;

    private ReceivedMessageService receivedMessageService;

    @Autowired
    public XmppMessageReceiverController(XmppMessagingService xmppMessagingService, ReceivedMessageService receivedMessageService) {
        this.xmppMessagingService = xmppMessagingService;
        this.receivedMessageService = receivedMessageService;
    }

    @RequestMapping(value = "/xmpp/message/chat/", method = RequestMethod.POST)
    public void create(HttpServletRequest request, HttpServletResponse response) throws IOException {
        XMPPService xmpp = XMPPServiceFactory.getXMPPService();
        Message message = xmpp.parseMessage(request);
        JID fromJid = message.getFromJid();
        String body = message.getBody();
        receivedMessageService.createReceivedMessage(fromJid.getId(), body);
        xmppMessagingService.sendMessage(fromJid, "Thank you for your response");
        response.setStatus(HttpServletResponse.SC_OK);
    }
}
