package com.velocityme.www.interfaces.nodeactions;

import com.velocityme.interfaces.*;
import com.velocityme.www.utility.NodeUtil;
import java.util.*;
import javax.servlet.http.*;
import org.apache.struts.action.*;

/**
 *
 * @author  Robert
 */
public class DirectoryActions extends NodeActions {

    /** Creates a new instance of DirectoryActions */
    public DirectoryActions() {
    }

    public ActionForward showNode(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
        HttpSession session = request.getSession();
        KeySession key = (KeySession) session.getAttribute("sessionKey");
        RemoteClientSession cs = RemoteClientSessionUtil.getHome().create();
        session.setAttribute("directoryDTO", cs.getDirectoryDTO(key));
        return mapping.findForward("ACTION_DISPLAY");
    }
}
