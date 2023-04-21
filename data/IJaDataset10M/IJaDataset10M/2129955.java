package com.birdpiss.web.controller;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import javax.annotation.PostConstruct;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.BeanCreationException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.support.SessionStatus;
import org.springframework.web.servlet.ModelAndView;
import com.birdpiss.commons.domain.AccountType;
import com.birdpiss.commons.domain.User;
import com.birdpiss.commons.manager.interfaces.UserManager;
import com.birdpiss.web.domain.WebTransaction;
import com.birdpiss.web.manager.interfaces.WebTransactionManager;
import com.birdpiss.web.validators.BirdpissValidator;

/**
 * The class MainController.
 */
@Controller
public class MainController {

    private static final Logger log = Logger.getLogger(MainController.class);

    private static final String COMMAND_OBJECT = "webTransaction";

    private static final String INDEX_VIEW = "index";

    private static final String ERROR_VIEW = "error";

    private static final String INDEX_HTML = "/index.html";

    private static final String ERROR_HTML = "/error.html";

    private UserManager userManager;

    private WebTransactionManager webTransactionManager;

    private BirdpissValidator birdpissValidator;

    private Map<String, String> userMap;

    private Map<String, String> accountTypes;

    /**
	 * Verify DI was setup correctly and perform any post DI initialization.
	 */
    @PostConstruct
    public void initialize() {
        if (this.userManager == null) throw new BeanCreationException("Must set User Manager");
        if (this.webTransactionManager == null) throw new BeanCreationException("Must set Web Transaction Manager");
        if (this.birdpissValidator == null) throw new BeanCreationException("Must set Validator");
    }

    /**
	 * Gets the account types.
	 * This data will populate our radio buttons.
	 *
	 * @return the account types
	 */
    @ModelAttribute("accountTypeMap")
    public Map<String, String> getAccountTypes() {
        if (this.accountTypes == null) {
            this.accountTypes = new LinkedHashMap<String, String>(2);
            this.accountTypes.put(String.valueOf(AccountType.CHECKING.value()), "Checking");
            this.accountTypes.put(String.valueOf(AccountType.SAVINGS.value()), "Savings");
        }
        return this.accountTypes;
    }

    /**
	 * Gets the users.
	 * This data will populate our select list.
	 *
	 * @return the users
	 */
    @ModelAttribute("userMap")
    public Map<String, String> getUsers() {
        if (this.userMap == null) {
            List<User> users = this.userManager.getAllUsers();
            this.userMap = new LinkedHashMap<String, String>(users.size() + 1);
            this.userMap.put("", "");
            for (User user : users) {
                this.userMap.put(String.valueOf(user.getId()), user.getName());
            }
        }
        return this.userMap;
    }

    /**
	 * Setup transfer form.  This is our GET method.
	 *
	 * @param model the model
	 *
	 * @return the string
	 */
    @RequestMapping(value = INDEX_HTML, method = RequestMethod.GET)
    public String setupTransferForm(Model model) {
        model.addAttribute(COMMAND_OBJECT, new WebTransaction());
        return INDEX_VIEW;
    }

    /**
	 * Process request.  This is our POST method.
	 *
	 * @param request the request
	 * @param webTransaction the web transaction
	 * @param bindingResult the binding result
	 * @param status the status
	 *
	 * @return the string
	 */
    @RequestMapping(value = INDEX_HTML, method = RequestMethod.POST)
    public ModelAndView processRequest(@ModelAttribute(COMMAND_OBJECT) WebTransaction webTransaction, BindingResult bindingResult, Model model, SessionStatus status) {
        this.birdpissValidator.validate(webTransaction, bindingResult);
        if (bindingResult.hasErrors()) {
            return new ModelAndView(INDEX_VIEW, model.asMap());
        }
        try {
            webTransaction = this.webTransactionManager.processWebTransaction(webTransaction);
            User creditUser = this.userManager.getUser(Integer.valueOf(webTransaction.getCreditUserId()));
            model.addAttribute("creditUser", creditUser);
            model.addAttribute("transaction", webTransaction);
        } catch (Exception ex) {
            status.setComplete();
            model.addAttribute("errors", ex.getMessage());
            return new ModelAndView(ERROR_VIEW, model.asMap());
        }
        status.setComplete();
        return new ModelAndView(INDEX_VIEW, model.asMap());
    }

    /**
	 * Error handler.
	 */
    @RequestMapping(ERROR_HTML)
    public void errorHandler() {
    }

    /**
	 * Sets the user manager.
	 *
	 * @param userManager the new user manager
	 */
    @Autowired
    public void setUserManager(UserManager userManager) {
        this.userManager = userManager;
    }

    /**
	 * Sets the web transaction manager.
	 *
	 * @param webTransactionManager the new web transaction manager
	 */
    @Autowired
    public void setWebTransactionManager(WebTransactionManager webTransactionManager) {
        this.webTransactionManager = webTransactionManager;
    }

    /**
	 * Sets the birdpiss validator.
	 *
	 * @param birdpissValidator the new birdpiss validator
	 */
    @Autowired
    public void setBirdpissValidator(BirdpissValidator birdpissValidator) {
        this.birdpissValidator = birdpissValidator;
    }
}
