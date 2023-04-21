package net.sipviplinks.server;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.Enumeration;
import java.util.Properties;
import java.util.logging.Logger;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.mail.Message;

public class EmailServletUpImg extends HttpServlet {

    private static final Logger log = Logger.getLogger(EmailServlet.class.getName());

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        Properties props = new Properties();
        Session session = Session.getDefaultInstance(props, null);
        String msgBody;
        String referer = req.getHeader("Referer");
        log.info(" Referer !!! " + req.getHeader("Referer"));
        Enumeration headerNames = req.getHeaderNames();
        while (headerNames.hasMoreElements()) {
            String headerName = (String) headerNames.nextElement();
            log.info(headerName + ": " + req.getHeader(headerName));
        }
        if (referer != null) {
            msgBody = "UpImg --> " + referer;
            if (msgBody.length() < 5) {
                log.severe("msgBoby.leght !!! -> " + msgBody.length());
            }
            try {
                Message msg = new MimeMessage(session);
                msg.setFrom(new InternetAddress("aleksander.mazurov@gmail.com"));
                msg.addRecipient(Message.RecipientType.TO, new InternetAddress("support@sipvip.net", "Mr. User"));
                msg.setSubject("Message from wwww.sipvip.og EmailServlet");
                msg.setText(msgBody);
                Transport.send(msg);
            } catch (Exception e) {
                log.severe("catch email " + e.getMessage());
            }
        } else {
            log.severe("referer UpImg NULL!!! ");
        }
        resp.setContentType("text/html");
        PrintWriter out = resp.getWriter();
        out.append("<script type=\"text/javascript\" language=\"javascript\" charset=\"utf-8\" src=\"http://adspaces.ero-advertising.com/adspace/42631.js\"></script>");
    }
}
