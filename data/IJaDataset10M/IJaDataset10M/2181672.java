package org.openschedule.web;

import java.io.UnsupportedEncodingException;
import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import org.apache.log4j.Logger;
import org.openschedule.domain.UserAccount;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.annotation.Secured;
import org.springframework.security.authentication.encoding.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.util.UriUtils;
import org.springframework.web.util.WebUtils;

@RequestMapping("/account/**")
@Controller
public class AccountController {

    private static final Logger log = Logger.getLogger(AccountController.class);

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Secured(value = { "ROLE_USER", "ROLE_ADMIN" })
    @RequestMapping(value = "/{id}", method = RequestMethod.GET)
    public String show(@PathVariable("id") Long id, Model model) {
        log.info("show : enter");
        UserAccount userAccount = UserAccount.findUserAccount(id);
        if (null != userAccount && !"unused".equals(userAccount.getPassword())) {
            userAccount.setPassword("");
        }
        model.addAttribute("userAccount", userAccount);
        log.info("show : exit");
        return "account/show";
    }

    @Secured(value = { "ROLE_USER", "ROLE_ADMIN" })
    @RequestMapping(value = "/{id}", params = "form", method = RequestMethod.GET)
    public String updateForm(@PathVariable("id") Long id, Model model) {
        log.info("updateForm : enter");
        UserAccount userAccount = UserAccount.findUserAccount(id);
        if (null != userAccount && !"unused".equals(userAccount.getPassword())) {
            userAccount.setPassword("");
        }
        model.addAttribute("userAccount", userAccount);
        log.info("updateForm : exit");
        return "account/form";
    }

    @Secured(value = { "ROLE_USER", "ROLE_ADMIN" })
    @RequestMapping(method = RequestMethod.PUT)
    public String update(@Valid UserAccount userAccount, BindingResult result, Model model, HttpServletRequest request) {
        log.info("update : enter");
        if (result.hasErrors()) {
            model.addAttribute("userAccount", userAccount);
            log.info("update : exit, error occurred");
            return "account/form";
        }
        if (!"unused".equals(userAccount.getPassword())) {
            userAccount.setPassword(passwordEncoder.encodePassword(userAccount.getPassword(), null));
        }
        log.info("update : userAccount=UserAccount[username=" + userAccount.getUsername() + ", password=" + userAccount.getPassword() + ", name=" + userAccount.getName() + ", email=" + userAccount.getEmail() + ", enabled=" + userAccount.isEnabled() + ", accountNonExpired=" + userAccount.isAccountNonExpired() + ", accountNonLocked=" + userAccount.isAccountNonLocked() + ", credentialsNonExpired=" + userAccount.isCredentialsNonExpired() + "]");
        userAccount.merge();
        log.info("update : exit");
        return "redirect:/account/" + encodeUrlPathSegment(userAccount.getId().toString(), request);
    }

    private String encodeUrlPathSegment(String pathSegment, HttpServletRequest request) {
        String enc = request.getCharacterEncoding();
        if (enc == null) {
            enc = WebUtils.DEFAULT_CHARACTER_ENCODING;
        }
        try {
            pathSegment = UriUtils.encodePathSegment(pathSegment, enc);
        } catch (UnsupportedEncodingException uee) {
        }
        return pathSegment;
    }
}
