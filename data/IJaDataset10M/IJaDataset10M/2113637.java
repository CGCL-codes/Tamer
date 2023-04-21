package com.inature.oce.web.struts.action.user;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.apache.log4j.Logger;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import com.inature.oce.core.api.OCEException;
import com.inature.oce.core.api.UserManager;
import com.inature.oce.core.service.OCEService;
import com.inature.oce.core.service.ServiceLocator;
import com.inature.oce.web.common.GlobalMessages;
import com.inature.oce.web.model.bean.UserContextBean;
import com.inature.oce.web.struts.action.AbstractAction;
import com.inature.oce.web.struts.common.GlobalForwards;

/**
 * Copyright 2007 i-nature
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or
 * implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 * 
 * @author Yavor Mitkov Gologanov
 *
 */
public class ChangePasswordAction extends AbstractAction {

    private static final Logger LOGGER = Logger.getLogger(ChangePasswordAction.class);

    public static final String ACTION_NAME = "changePassword";

    /**
	 * 
	 */
    public ActionForward executeAction(ActionMapping mapping, ActionForm form, UserContextBean userContext, HttpServletRequest request, HttpServletResponse response) throws OCEException {
        LOGGER.info("Execute " + ACTION_NAME);
        ChangePasswordForm passwordForm = (ChangePasswordForm) form;
        String oldPassword = passwordForm.getOldPassword();
        String newPassword = passwordForm.getNewPassword();
        OCEService service = ServiceLocator.getOCEService();
        UserManager userManager = service.getUserManager();
        String userId = userContext.getUser().getId();
        if (!userManager.isValidPassword(userId, oldPassword)) {
            request.setAttribute(GlobalMessages.GLOBAL_MESSAGE_KEY, "oce.messages.error.passwordNotMatching");
            return mapping.getInputForward();
        }
        userManager.changePassword(userId, newPassword);
        request.setAttribute(GlobalMessages.GLOBAL_MESSAGE_KEY, "oce.messages.user.passwordChanged");
        return mapping.findForward(GlobalForwards.SUCCESS);
    }
}
