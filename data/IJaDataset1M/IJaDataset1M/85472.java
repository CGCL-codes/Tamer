package com.nyandu.weboffice.site.action;

import com.nyandu.weboffice.common.action.PrivateAction;
import com.nyandu.weboffice.common.action.PublicForm;
import com.nyandu.weboffice.common.business.BusinessSession;
import com.nyandu.weboffice.common.util.Consts;
import com.nyandu.weboffice.site.database.IGroupDAO;
import com.nyandu.weboffice.site.business.AuthorizablePermissionsValues;
import com.nyandu.weboffice.site.business.Permission;
import com.nyandu.weboffice.site.business.Group;
import com.nyandu.weboffice.file.database.IFileDAO;
import com.nyandu.weboffice.file.business.File;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ActionForm;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.ArrayList;

/**
 * 
 *  The contents of this file are subject to the Nandu Public License
 * Version 1.1 ("License"); you may not use this file except in
 * compliance with the License. You may obtain a copy of the License at
 * http://www.nyandu.com
 * 
 * Software distributed under the License is distributed on an "AS IS"
 * basis, WITHOUT WARRANTY OF ANY KIND, either express or implied. See
 * the License for the specific language governing rights and limitations
 * under the License.
 * 
 * The Initial Developer of the Original Code is User.
 * Portions created by User are Copyleft (C) www.nyandu.com. 
 * All Rights Reserved.
 * 
 * Contributor(s): ______________________________________.
 * User: ern
 * Date: Jun 9, 2005
 * Time: 10:39:03 AM
 */
public class CreateGroupAction extends PrivateAction {

    public ActionForward execute(ActionMapping map, ActionForm form, HttpServletRequest req, HttpServletResponse res, BusinessSession bSession) throws Exception {
        int code = Consts.UNEXPECTED_ERROR;
        String name = "unexpected_error";
        String function = "";
        if (form instanceof PublicForm) {
            PublicForm pForm = (PublicForm) form;
            function = pForm.getF();
            name = "ok";
            Group group = new Group((String) pForm.getParam(0), "");
            IGroupDAO groupDAO = bSession.getGroupDAO();
            int groupId = groupDAO.insertGroup(group, bSession.getUser().getId());
            req.setAttribute(Consts.RS_RESPONSE_FUNCTION, function);
            req.setAttribute(Consts.RS_RESPONSE_CODE, new Integer(Consts.ADDRESS_BOOK_CONTACT_ADDED));
            ArrayList params = null;
            params = new ArrayList();
            params.add(new Integer(groupId));
            params.add(group.getName());
            req.setAttribute(Consts.RS_RESPONSE_PARAMS, params);
            groupDAO.commit();
            return map.findForward(name);
        }
        return map.findForward(name);
    }
}
