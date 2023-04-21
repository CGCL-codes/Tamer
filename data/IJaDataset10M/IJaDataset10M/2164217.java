package com.nyandu.weboffice.common.action;

import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ActionForm;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import com.nyandu.weboffice.common.business.BusinessSession;
import com.nyandu.weboffice.common.util.Consts;

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
 * User: marcelo
 * Date: 10-may-2005
 * Time: 22:15:56
 * To change this template use File | Settings | File Templates.
 */
public class ChangeUserFilePermission extends PrivateAction {

    private static final int FILE_PARAM = 0;

    private static final int HOST_FILE_ID_PARAM = 1;

    /**
 * 
 * @param map
 * @param form
 * @param req
 * @param res
 * @param bSession
 * @return
 * @throws Exception
 */
    public ActionForward execute(ActionMapping map, ActionForm form, HttpServletRequest req, HttpServletResponse res, BusinessSession bSession) throws Exception {
        String name = "unexpected_error";
        int code = Consts.UNEXPECTED_ERROR;
        String function = "";
        return map.findForward(name);
    }
}
