package org.sqlanyware.struts;

import java.io.IOException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

/**
 * @author foucault-s
 *
 * To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Generation - Code and Comments
 */
public class DriverManagerAction extends ActionBase {

    public ActionForward executeEx(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) {
        String driverToDelete = request.getParameter("deleteDriver");
        String driverToDownload = request.getParameter("downloadDriver");
        if (driverToDelete != null && !driverToDelete.equals("")) {
            if (!getDriverManager().deleteDriver(driverToDelete)) {
                getLogger().error("Unable to delete driver " + driverToDelete);
            }
            return mapping.findForward("DriverManager");
        } else if (driverToDownload != null && !driverToDownload.equals("")) {
            try {
                byte[] buf = getDriverManager().getDriverCode(driverToDownload);
                response.setContentType("application/octet-stream");
                response.setContentLength(buf.length);
                response.setHeader("Content-Disposition", "attachement; filename=\"" + driverToDownload + "\"");
                response.getOutputStream().write(buf, 0, buf.length);
                response.getOutputStream().flush();
                response.getOutputStream().close();
            } catch (IOException e) {
            }
            return mapping.findForward("DriverManager");
        } else {
            return mapping.findForward("DriverManager");
        }
    }
}
