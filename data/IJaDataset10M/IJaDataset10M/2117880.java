package gu;

import gu.server.PMF;
import java.io.IOException;
import java.io.PrintWriter;
import javax.jdo.PersistenceManager;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class postbridge7 extends HttpServlet {

    private static final long serialVersionUID = 1L;

    public static String[] s4 = null;

    public void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        doPost(req, resp);
    }

    public void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        PrintWriter out = resp.getWriter();
        String s1 = req.getParameter("a1");
        String s2 = req.getParameter("a2");
        String s3 = req.getParameter("a3");
        String s4 = req.getParameter("a4");
        String s5 = req.getParameter("a5");
        PersistenceManager pm = PMF.get().getPersistenceManager();
        A1test tt = new A1test(s1, s2, s3, s4, s5);
        pm.makePersistent(tt);
        out.println("ok");
    }
}
