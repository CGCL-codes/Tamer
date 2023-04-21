package skylight1.servlet;

import java.io.BufferedReader;
import java.io.IOException;
import java.util.Date;
import java.util.Properties;
import javax.mail.Authenticator;
import javax.mail.Message;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@SuppressWarnings("serial")
public class LogHandlerBasicServlet extends HttpServlet {

    public void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        doPost(request, response);
    }

    public void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        StringBuilder message = new StringBuilder();
        try {
            BufferedReader reader = request.getReader();
            int len = request.getContentLength();
            if (len > 0) {
                char[] buf = new char[len];
                while (reader.read(buf, 0, buf.length) != -1) {
                    message.append(buf);
                }
            }
        } catch (Exception e) {
            getServletContext().log("problem filtering page to UTF-8?");
            System.out.println("ERROR: reading....." + e);
            e.printStackTrace();
        }
        String st = message.toString().trim();
        if (st.equals("")) return;
        response.setContentType("text/html");
        String source = request.getHeader("User-Agent");
        if (request.getParameter("emailtest") != null) {
            source = request.getParameter("emailtest");
        }
        final String host = getServletConfig().getInitParameter("host");
        final String to = getServletConfig().getInitParameter(source);
        final String from = getServletConfig().getInitParameter("from");
        if (host != null) {
            System.out.println(String.format("mail: host=%s source=%s to=%s from=%s", host, source, to, from));
            try {
                if (to != null) {
                    sendMail(host, to, from, "stack trace", message.toString());
                }
                System.out.println(String.format("mail sent to %s", to));
            } catch (Exception ex) {
                ex.printStackTrace();
                getServletContext().log("ERROR sending mail" + ex);
            }
        }
    }

    private void sendMail(String host, String to, String from, String subject, String content) throws Exception {
        Properties props = new Properties();
        props.put("mail.smtp.host", host);
        props.put("mail.smtp.auth", "true");
        Authenticator auth = new SMTPAuthenticator(getServletConfig().getInitParameter("user"), getServletConfig().getInitParameter("pwd"));
        Session session = Session.getInstance(props, auth);
        Message msg = new MimeMessage(session);
        msg.setFrom(new InternetAddress(from));
        InternetAddress[] address = { new InternetAddress(to) };
        msg.setRecipients(Message.RecipientType.TO, address);
        msg.setSubject(subject);
        msg.setSentDate(new Date());
        msg.setText(content);
        Transport.send(msg);
    }

    private static class SMTPAuthenticator extends Authenticator {

        private String user;

        private String pwd;

        public SMTPAuthenticator(String username, String pass) {
            user = username;
            pwd = pass;
        }

        public PasswordAuthentication getPasswordAuthentication() {
            return new PasswordAuthentication(user, pwd);
        }
    }
}
