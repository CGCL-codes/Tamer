package org.squabble.web.account;

import java.util.Map;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.SessionAttributes;
import org.springframework.web.bind.support.SessionStatus;
import org.squabble.SquabbleConstants;
import org.squabble.domain.Account;
import org.squabble.domain.AccountProperty;
import org.squabble.service.AccountService;

@Controller
@RequestMapping("/account/settings")
@SessionAttributes(value = { "account" })
public class AccountSettingsController extends AbstractPropertyController {

    @Autowired
    AccountService accountService;

    @RequestMapping(method = RequestMethod.GET)
    public String handleGet(@ModelAttribute("account") Account account, ModelMap modelMap) {
        account = accountService.getAccount(account.getId());
        Map<String, AccountProperty> props = account.getAccountProperties();
        AccountSettingsCommand asc = new AccountSettingsCommand();
        asc.setEmailAllow((Boolean) getPropertyValue(props, "emailAllow", false));
        asc.setEmailHide((Boolean) getPropertyValue(props, "emailHide", true));
        asc.setGenderHide((Boolean) getPropertyValue(props, "genderHide", false));
        asc.setMsgAllow((Boolean) getPropertyValue(props, "msgAllow", true));
        asc.setTitleDisplay((Boolean) getPropertyValue(props, "titleDisplay", true));
        asc.setThreadsPerPage((Long) getPropertyValue(props, "threadsPerPage", 25L));
        modelMap.addAttribute("threadCount", new Integer[] { 5, 10, 15, 20, 25, 30, 35, 40, 45, 50 });
        modelMap.addAttribute("accountSettingsCommand", asc);
        return "account.settings";
    }

    @RequestMapping(method = RequestMethod.POST)
    public String processSubmit(@ModelAttribute("account") Account account, @ModelAttribute("accountSettingsCommand") AccountSettingsCommand asc, BindingResult result, SessionStatus status, ModelMap model, HttpServletRequest request, HttpServletResponse response) throws Exception {
        account = accountService.updateSettings(account.getId(), asc);
        model.addAttribute("account", account);
        model.addAttribute(SquabbleConstants.POST_PROCESS_MESSAGE, "settings.processed");
        return "redirect:/account/settings";
    }
}
