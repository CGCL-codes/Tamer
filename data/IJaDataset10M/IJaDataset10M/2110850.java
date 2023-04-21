package org.xfap.http;

import java.io.IOException;
import java.util.Vector;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.ksoap.SoapEnvelope;
import org.kxml.io.XmlWriter;
import org.kxml.kdom.Node;
import org.kxml.parser.XmlParser;
import org.xfap.*;
import org.xfap.ams.*;
import org.xfap.http.PlatformServlet;

public class SoapHandler extends DefaultAgent {

    PlatformServlet platformServlet;

    SoapHandler(Platform platform, PlatformServlet platformServlet) {
        registerAms(platform, "soaphandler");
        this.platformServlet = platformServlet;
    }

    /** init method required by the Agent interface. Never called 
	since this agent is Servlets have their own init method. Throws
	an exception. */
    public void init(Platform platform, String[] args) {
        throw new RuntimeException("SoapHandler must be started from WebServer or servletrunner!");
    }

    /** Builds the message that is sent to an agent.  The agent is
	determined from the "directory" part following the servlet
	name. The message content is an element "http-request" containing
	the following child elements: 

        <ul>
          <li>method: POST, GET, PUT or HEAD</li>
          <li>url: Full url including query part</li>
          <li>agentPath: Servlet path plus agent name</li>
          <li>pathInfo: Remaining path if any</li>
          <li>query: Query part if any</li>
        </ul>*/
    Message buildMessage(HttpServletRequest req) throws IOException {
        Message message = new Message(Message.REQUEST, agentIdentifier);
        String pathInfo = req.getPathInfo();
        if (pathInfo == null || pathInfo.equals("/")) pathInfo = platformServlet.defaultName; else if (pathInfo.startsWith("/")) pathInfo = pathInfo.substring(1);
        String receiver;
        int i = pathInfo.indexOf('/');
        if (i == -1) {
            receiver = pathInfo;
            pathInfo = "/";
        } else {
            receiver = pathInfo.substring(0, i);
            pathInfo = pathInfo.substring(i);
        }
        message.addReceiver(new AgentIdentifier(platform.getAbsoluteName(receiver), new Vector()));
        XmlParser xp = new XmlParser(req.getReader());
        SoapEnvelope envelope = new SoapEnvelope();
        envelope.parseHead(xp);
        Node node = new Node();
        node.parse(xp);
        message.setContent(node);
        envelope.parseTail(xp);
        message.setConversationId(req.getSession().getId());
        return message;
    }

    public void service(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        reply(resp, platform.query(buildMessage(req)));
    }

    /** generates a reply from the incoming reply agent message */
    void reply(HttpServletResponse res, Message reply) throws IOException {
        res.setContentType("text/xml");
        XmlWriter xw = new XmlWriter(res.getWriter());
        SoapEnvelope envelope = new SoapEnvelope();
        envelope.writeHead(xw);
        reply.getContent().write(xw);
        envelope.writeTail(xw);
        xw.close();
    }
}
