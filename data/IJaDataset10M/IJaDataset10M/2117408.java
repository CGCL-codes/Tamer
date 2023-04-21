package ces.platform.system.ui.resource.action;

import org.apache.struts.action.*;
import java.util.Vector;
import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.http.*;
import java.util.ArrayList;
import ces.platform.system.dbaccess.Resource;

/**
 * <p>Title: </p>
 * <p>Description: </p>
 * <p>Copyright: Copyright (c) 2003</p>
 * <p>Company: </p>
 * @author unascribed
 * @version 1.0
 */
public class AddResTreeAction extends Action {

    public ActionForward execute(ActionMapping actionMapping, ActionForm actionForm, HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
        String strForward = "error";
        strForward = "success";
        return (actionMapping.findForward(strForward));
    }
}
