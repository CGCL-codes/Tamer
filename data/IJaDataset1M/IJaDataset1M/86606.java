package gov.ca.bdo.modeling.dsm2.map.server;

import java.io.IOException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@SuppressWarnings("serial")
public class DSM2DownloadServlet extends HttpServlet {

    /**
	 * The request should contain two parameters : studyName and the inpuName
	 * (gis_inp or hydro_echo_inp)
	 */
    public void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        String studyName = req.getParameter("studyName");
        String inputName = req.getParameter("inputName");
        String studyKey = req.getParameter("studyKey");
        resp.setContentType("text/plain");
        String filename = inputName.replace("_inp", ".inp");
        resp.setHeader("Content-Disposition", "attachment; filename=" + filename);
        if ((studyKey == null) || studyKey.equals("")) {
            resp.getWriter().write(new DSM2InputServiceImpl().showInput(studyName, inputName));
        } else {
            resp.getWriter().write(new DSM2InputServiceImpl().showInputForKey(studyKey, inputName));
        }
    }
}
