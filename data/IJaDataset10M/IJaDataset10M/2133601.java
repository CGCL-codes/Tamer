package com.nyandu.weboffice.site.action;

import com.nyandu.weboffice.common.action.PrivateAction;
import com.nyandu.weboffice.common.action.PublicForm;
import com.nyandu.weboffice.common.business.BusinessSession;
import com.nyandu.weboffice.common.util.Consts;
import com.nyandu.weboffice.site.business.UserPreferences;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Set;
import java.util.HashSet;

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
 * Date: 19/12/2004
 * Time: 02:41:05 PM
 */
public class ChangeUserPreferencesAction extends PrivateAction {

    private static final int LANGUAGE_PARAM = 0;

    private static final int PREFERENCES_PARAM = 1;

    public ActionForward execute(ActionMapping map, ActionForm form, HttpServletRequest req, HttpServletResponse res, BusinessSession bSession) throws Exception {
        String name = "unexpected_error";
        String msg = "";
        String function = "";
        int code = Consts.UNEXPECTED_ERROR;
        String account = "";
        if (form instanceof PublicForm) {
            PublicForm pForm = (PublicForm) form;
            UserPreferences up = bSession.getUser().getUserPreferences();
            function = pForm.getF();
            String lang = (String) pForm.getParam(LANGUAGE_PARAM);
            if ((lang != null) && (lang.trim().length() > 0)) {
                up.setPreferenceValue(Consts.USER_PREFERENCES_LANGUAGE, lang);
                bSession.getUserDAO().updateUserPreferences(bSession.getUser().getId(), up);
            }
            String prefValues = (String) pForm.getParam(PREFERENCES_PARAM);
            if ((prefValues != null) && (prefValues.trim().length() > 0)) {
                String[] pv = prefValues.split(";");
                HashSet prefKeys = new HashSet();
                int key = 0;
                String value = null;
                int init;
                if (pv[0].equals("")) {
                    init = 1;
                } else {
                    init = 0;
                }
                for (int i = init; i < pv.length; i = i + 2) {
                    try {
                        key = Integer.parseInt(pv[i]);
                    } catch (NumberFormatException e) {
                        name = "unexpected_error";
                        return map.findForward(name);
                    }
                    prefKeys.add(new Integer(key));
                    value = pv[i + 1];
                    up.setPreferenceValue(key, value);
                }
                bSession.getUserDAO().updateUserPreferenceValues(prefKeys, bSession.getUser().getId(), up);
            }
            bSession.getUserDAO().commit();
            code = Consts.USER_PREFERENCES_CHANGED;
            name = "preferences_changed";
        }
        req.setAttribute(Consts.RS_RESPONSE_FUNCTION, function);
        req.setAttribute(Consts.RS_RESPONSE_CODE, new Integer(code));
        return map.findForward(name);
    }
}
