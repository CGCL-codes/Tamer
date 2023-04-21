package dmedv.aurora;

import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.util.List;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.apache.log4j.Logger;

public class FileListServlet2 extends HttpServlet {

    private Logger log = Logger.getLogger("dmedv.aurora.FileListServlet2");

    public void doGet(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException {
        PrintWriter pw = null;
        try {
            res.setContentType("text/plain");
            OutputStream out = res.getOutputStream();
            pw = new PrintWriter(out);
            int year = Integer.parseInt(req.getParameter("year"));
            int month = Integer.parseInt(req.getParameter("month"));
            int day = Integer.parseInt(req.getParameter("day"));
            int hour = Integer.parseInt(req.getParameter("hour"));
            int minute = Integer.parseInt(req.getParameter("minute"));
            String type = req.getParameter("type");
            List<String> files = FileList2.getFiles(year, month, day, hour, minute, type);
            for (int i = 0; i < files.size(); i++) {
                pw.println(files.get(i));
            }
            pw.close();
        } catch (Exception e) {
            pw.print("");
            pw.close();
            log.error("Aurora file list error: " + e.getMessage());
        }
    }
}
