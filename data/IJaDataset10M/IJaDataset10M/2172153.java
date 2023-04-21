package salto.fwk.mvc.action.chart;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

/**
 * @author eloiez
 *
 * To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Generation - Code and Comments
 */
public class ShowImgAction extends Action {

    public ActionForward execute(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
        response.setContentType("image/gif");
        ServletOutputStream out = response.getOutputStream();
        out.write((byte[]) request.getSession().getAttribute("CHART"));
        out.flush();
        if (request.getSession().getAttribute("CHARTNUM") == null) {
            request.getSession().setAttribute("CHARTNUM", Boolean.TRUE);
        } else {
        }
        return null;
    }
}
