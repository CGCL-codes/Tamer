package fr.loria.ecoo.wooki.web.servlets;

import fr.loria.ecoo.wooki.web.WookiSite;
import java.io.File;
import java.io.FileInputStream;
import java.io.OutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * DOCUMENT ME!
 *
 * @author $author$
 * @version $Revision$
 */
public class GetState extends BaseServlet {

    private static final long serialVersionUID = 5009984916472092893L;

    /**
     * DOCUMENT ME!
     *
     * @param request DOCUMENT ME!
     * @param response DOCUMENT ME!
     *
     * @throws Exception DOCUMENT ME!
     */
    @SuppressWarnings("unused")
    public void process(HttpServletRequest request, HttpServletResponse response) throws Exception {
        try {
            File state = (File) WookiSite.getInstance().getWookiEngine().getWootEngine().getState();
            if (state != null) {
                response.setHeader("Content-type", "application/zip");
                response.setHeader("Content-Disposition", "attachment; filename=" + state.getName());
                OutputStream out = response.getOutputStream();
                FileInputStream fis = new FileInputStream(state);
                byte[] buffer = new byte[2048];
                int bytesIn = 0;
                while ((bytesIn = fis.read(buffer)) != -1) {
                    out.write(buffer, 0, bytesIn);
                }
                out.flush();
            } else {
                response.setHeader("Content-type", "null");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
