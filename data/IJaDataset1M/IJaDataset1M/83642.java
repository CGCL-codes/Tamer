package com.nyandu.weboffice.common.action;

import com.nyandu.weboffice.common.business.BusinessSession;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

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
 * User: alvaro
 * Date: 30/12/2004
 * Time: 04:22:37 PM
 */
public class RSAction extends PrivateAction {

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
        return map.findForward("ok");
    }
}
