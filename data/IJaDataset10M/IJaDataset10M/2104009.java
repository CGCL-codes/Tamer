package gwtip.sotu.server;

import com.google.gwt.user.server.rpc.RemoteServiceServlet;
import gwtip.sotu.client.remote.AccessException;
import gwtip.sotu.client.remote.ConversationDescriptor;
import gwtip.sotu.client.remote.ConversationService;
import gwtip.sotu.client.remote.SystemException;
import java.io.IOException;
import javax.servlet.ServletConfig;
import javax.servlet.ServletException;

public class ConversationServiceServlet extends RemoteServiceServlet implements ConversationService {

    static final ConversationServiceLocal service = new ConversationServiceLocal();

    public ConversationServiceServlet() {
        super();
    }

    public boolean sendChatMessage(ConversationDescriptor conversation, String message) {
        User u = (User) this.getThreadLocalRequest().getSession().getAttribute(StreamServlet.USER);
        return ConversationServiceServlet.service.sendChatMessage(u, conversation, message);
    }

    public ConversationDescriptor startConversation(ConversationDescriptor conversation) throws AccessException, SystemException {
        User u = (User) this.getThreadLocalRequest().getSession().getAttribute(StreamServlet.USER);
        try {
            return ConversationServiceServlet.service.createConversation(u, conversation);
        } catch (AccessException ae) {
            return null;
        }
    }

    public ConversationDescriptor[] listConversations() {
        return ConversationServiceServlet.service.listConversations();
    }

    public boolean joinConversation(ConversationDescriptor conversation) throws AccessException {
        User u = (User) this.getThreadLocalRequest().getSession().getAttribute(StreamServlet.USER);
        ConversationServiceServlet.service.joinConversation(u, conversation);
        return true;
    }

    public boolean endConversation(ConversationDescriptor conversation) throws AccessException {
        User u = (User) this.getThreadLocalRequest().getSession().getAttribute(StreamServlet.USER);
        ConversationServiceServlet.service.endConversation(u, conversation);
        return true;
    }

    public boolean leaveConversation(ConversationDescriptor conversation) throws SystemException {
        User u = (User) this.getThreadLocalRequest().getSession().getAttribute(StreamServlet.USER);
        ConversationServiceServlet.service.leaveConversation(u, conversation);
        return true;
    }

    public ConversationDescriptor playback(long conversationId, long startPosition) throws AccessException, SystemException {
        User u = (User) this.getThreadLocalRequest().getSession().getAttribute(StreamServlet.USER);
        return ConversationServiceServlet.service.playback(u, conversationId, startPosition);
    }

    public void init(ServletConfig config) throws ServletException {
        super.init(config);
        try {
            ConversationServiceServlet.service.init(config.getServletContext().getRealPath("/"));
        } catch (IOException ioe) {
            throw new ServletException("Unable to init directories", ioe);
        }
    }
}
