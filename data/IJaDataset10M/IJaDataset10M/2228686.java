package org.encuestame.mvc.controller.settings;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.encuestame.core.util.ConvertDomainBean;
import org.encuestame.mvc.controller.AbstractBaseOperations;
import org.encuestame.persistence.domain.security.UserAccount;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

/**
 * Settings Controller.
 * @author Picado, Juan juanATencuestame.org
 * @since Jan 29, 2011 11:37:56 AM
 * @version $Id:$
 */
@Controller
public class SettingsController extends AbstractBaseOperations {

    private Log log = LogFactory.getLog(this.getClass());

    /**
     * Account Settings.
     * @param model
     * @return
     */
    @RequestMapping(value = "/settings/configuration", method = RequestMethod.GET)
    public String settingsAccountController(ModelMap model, final UserAccount userAccount) {
        log.debug("settingsAccountController account: " + userAccount);
        log.debug("settingsAccountController account: " + userAccount.toString());
        model.put("username", getUserPrincipalUsername());
        model.put("userAccount", ConvertDomainBean.convertBasicSecondaryUserToUserBean(userAccount));
        return "settings/account";
    }

    /**
    * Social Settings Accounts.
    * @param model
    * @return
    */
    @RequestMapping(value = "/settings/social", method = RequestMethod.GET)
    public String socialSettingsController(ModelMap model) {
        log.debug("social");
        return "settings/social";
    }
}
