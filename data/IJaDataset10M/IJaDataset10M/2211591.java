package admin;

import java.io.IOException;
import java.io.PrintWriter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import db.DBConnection;
import db.Domains;

public class Domain {

    private DBConnection dbConnection;

    private HttpServletRequest request;

    private HttpServletResponse response;

    public Domain(HttpServletRequest request, HttpServletResponse response, DBConnection dbConnection) {
        this.request = request;
        this.response = response;
        this.dbConnection = dbConnection;
    }

    public void createDomain() {
        String domain = request.getParameter("domain");
        String node = request.getParameter("node");
        String maxMails = request.getParameter("maxMails");
        String imap = request.getParameter("imap");
        String pop3 = request.getParameter("pop3");
        String webmail = request.getParameter("webmail");
        String spam = request.getParameter("spam");
        Domains domains = new Domains();
        domains.setDomain(domain);
        domains.setNode(node);
        domains.setMaxMails(Integer.parseInt(maxMails));
        domains.setImap(Integer.parseInt(imap));
        domains.setPop3(Integer.parseInt(pop3));
        domains.setWebmail(Integer.parseInt(webmail));
        domains.setSpam(Integer.parseInt(spam));
        if (domains.insertDomain(dbConnection) == 0) {
            try {
                PrintWriter out = response.getWriter();
                out.println("<?xml version=\"1.0\" encoding=\"UTF-8\"?>");
                out.println("<AjaxResponse data=\"createDomain\">");
                out.println("<Response>true</Response>");
                out.println("</AjaxResponse>");
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
