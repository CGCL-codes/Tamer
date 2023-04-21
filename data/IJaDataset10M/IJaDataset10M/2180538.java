package org.jwebsocket.jetty;

import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.eclipse.jetty.websocket.WebSocket;
import org.eclipse.jetty.websocket.WebSocketServlet;

/**
 *
 * @author aschulze
 */
public class jWebSocket extends WebSocketServlet {

    @Override
    protected void doGet(HttpServletRequest aRequest, HttpServletResponse aResponse) throws ServletException, IOException {
        System.out.print("@doGet");
        getServletContext().getNamedDispatcher("default").forward(aRequest, aResponse);
    }

    /**
	 * Handles the HTTP <code>POST</code> method.
	 * @param aRequest servlet request
	 * @param aResponse servlet response
	 * @throws ServletException if a servlet-specific error occurs
	 * @throws IOException if an I/O error occurs
	 */
    @Override
    protected void doPost(HttpServletRequest aRequest, HttpServletResponse aResponse) throws ServletException, IOException {
        System.out.println("@doPost");
    }

    @Override
    protected WebSocket doWebSocketConnect(HttpServletRequest aRequest, String aProtocol) {
        System.out.println("@doWebSocketConnect");
        return new JettyWrapper(aRequest, aProtocol);
    }

    /**
	 * Returns a short description of the servlet.
	 * @return a String containing servlet description
	 */
    @Override
    public String getServletInfo() {
        return "Short description";
    }
}
