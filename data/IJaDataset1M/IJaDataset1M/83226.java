package jbluesman.t;

import java.io.*;
import javax.servlet.*;
import javax.servlet.http.*;
import java.net.*;

public class Mail extends HttpServlet {

    String MAIL_SERVER_URL = "myjavaserver.com";

    int MAIL_PORT = 25;

    Socket socket;

    DataInputStream in;

    PrintStream out;

    String str;

    public void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String conf = request.getParameter("action");
        PrintWriter out;
        response.setContentType("text/html");
        out = response.getWriter();
        if (conf != null) {
            if (conf.equals("config")) {
                out.println("<config><t>You can send EMAIL with this. USER(from) and SENDER(subject) are required</t><nu>1</nu><np>0</np><nn>2</nn><mr>1</mr><mc>800</mc><mm>100</mm><in>0</in></config>>");
                return;
            } else {
                out.println("<num>3</num><res>Servlet Errors</res>");
            }
        } else {
            conf = "";
        }
        SendMail(request.getParameter("user"), request.getParameter("nick"), request.getParameter("rcpt"), request.getParameter("text"));
        out.println("<num>0</num><txt>Email Sent!</txt>");
        out.close();
    }

    public void SendMail(String from, String name, String to, String message) throws ProtocolException, IOException {
        if (name == null) {
            name = "";
        }
        Socket socket = new Socket(MAIL_SERVER_URL, MAIL_PORT);
        in = new DataInputStream(socket.getInputStream());
        out = new PrintStream(socket.getOutputStream());
        if (!checkStatus("220")) throw new ProtocolException(str);
        out.println("HELO " + MAIL_SERVER_URL);
        out.flush();
        if (!checkStatus("250")) throw new ProtocolException(str);
        out.println("MAIL FROM: " + from);
        out.flush();
        if (!checkStatus("250")) throw new ProtocolException(str);
        out.println("RCPT TO: " + to);
        out.flush();
        if (!checkStatus("250")) throw new ProtocolException(str);
        out.println("DATA");
        out.flush();
        if (!checkStatus("354")) throw new ProtocolException(str);
        out.println("From: " + from);
        out.println("To: " + to);
        out.println("Subject: " + name + "\n");
        out.flush();
        out.println("");
        out.println(message);
        out.println(".");
        out.flush();
        if (!checkStatus("250")) throw new ProtocolException(str);
        out.println("QUIT");
        out.flush();
        in.close();
        socket.close();
    }

    private boolean checkStatus(String RFC) throws ProtocolException, IOException {
        str = in.readLine();
        System.out.println(str);
        if (!str.startsWith(RFC)) throw new ProtocolException(str);
        while (str.indexOf('-') == 3) {
            str = in.readLine();
            if (!str.startsWith("RFC")) throw new ProtocolException(str);
        }
        return true;
    }
}
