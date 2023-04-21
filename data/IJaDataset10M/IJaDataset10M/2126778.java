package br.guj.chat.servlet;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.Date;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import br.guj.chat.GujChat;
import br.guj.chat.dao.DAOLocator;
import br.guj.chat.model.server.ChatServer;

/**
 * Instance/chat servlet
 * @author Guilherme de Azevedo Silveira
 * @version $Revision: 1.24 $, $Date: 2003/10/29 18:33:45 $
 */
public class ChatInstance extends ChatServlet {

    /** number of server hits */
    private static long serverHits = 0;

    /**
	 * Serves the chat request
	 */
    protected void doGet(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException {
        if (req.getParameter("content-type") != null) {
            res.setContentType(req.getParameter("content-type"));
        } else {
            res.setContentType("text/html");
        }
        try {
            if (req.getPathInfo() == null || req.getPathInfo().length() < 2) {
                DAOLocator.getDAO().getVelocityController().displayManagementPage("list.instances.vm", null, req, res);
            } else {
                String name = req.getPathInfo().substring(1);
                ChatServer server = DAOLocator.getDAO().getServerDAO().get(name);
                newServerHit();
                DAOLocator.getDAO().getChatController().handleRequest(req, res, server);
            }
        } catch (Exception e) {
            try {
                DAOLocator.getDAO().getLogger().error("exception found", e);
                DAOLocator.getDAO().getVelocityController().displayError(req, res, e);
            } catch (Exception ex) {
                DAOLocator.getDAO().getLogger().error("fatal error", ex);
                DAOLocator.getDAO().getVelocityController().error(res, ex);
            }
        }
    }

    /**
	 * Serves the request to the get method
	 */
    protected void doPost(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException {
        doGet(req, res);
    }

    /**
	 * Logs the servlet destruction by sending data to our anonymous gujchat usage page
	 */
    public void destroy() {
        Date stopTime = new Date();
        SimpleDateFormat format = new java.text.SimpleDateFormat("dd.MM.yyyy_hh:mm:ss");
        try {
            if (getInitParameter("recordStats").equals("false")) {
                return;
            }
            String url = getInitParameter("statsPath") + "?startTime=" + format.format(GujChat.getStartupTime()) + "&stopTime=" + format.format(stopTime) + "&serverHits=" + getServerHits();
            URL baseUrl = new URL("http", getInitParameter("statsURL"), Integer.parseInt(getInitParameter("statsPort")), url);
            DAOLocator.getDAO().getLogger().info("Logged server stats: " + baseUrl.getContent());
        } catch (MalformedURLException e) {
            DAOLocator.getDAO().getLogger().error("Stats: Malformed URL", e);
        } catch (IOException e) {
            DAOLocator.getDAO().getLogger().error("Stats: IOException", e);
        }
    }

    /**
	 * Increments the server hit counter
	 */
    protected static void newServerHit() {
        serverHits++;
    }

    /**
	 * Returns the number of server hits per server
	 */
    protected static long getServerHits() {
        return serverHits;
    }
}
