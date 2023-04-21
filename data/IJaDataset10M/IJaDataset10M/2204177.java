package study.mondrian;

import java.io.IOException;
import java.io.PrintWriter;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.servlet.jsp.JspException;
import org.xml.sax.SAXException;
import com.jsf.sun.Logger;
import com.jsf.sun.OpTitleI;
import com.zzsoft.app.zzmonitor.admmonidata.socket.FrameMonitorsSev;

public class MoniTestServlet extends HttpServlet {

    public void init() throws ServletException {
        new FrameMonitorsSev().init();
        LogConfigMonitorCenter.initLogger();
    }
}
