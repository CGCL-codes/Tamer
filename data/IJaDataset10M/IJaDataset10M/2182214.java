package jmemento.web.controller.user.signin;

import jmemento.api.domain.user.IUser;
import jmemento.api.service.pool.IPoolService;
import jmemento.api.service.storage.IStorageService;
import jmemento.api.service.user.IUserService;
import jmemento.api.service.user.IUserSignOnService;
import jmemento.web.controller.user.dto.UserDto;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.support.SessionStatus;

/**
 * @author rusty
 */
@Controller
@RequestMapping("/signin")
public final class SignInController {

    private final transient Logger log = LoggerFactory.getLogger(getClass());

    @Autowired
    private IUserSignOnService userSignOnService;

    @Autowired
    private IUserService userService;

    @Autowired
    private IStorageService storageService;

    @Autowired
    private IPoolService poolService;

    @Autowired
    private SignInValidator signInValidator;

    private static final String signinView = "signin";

    public void setup(final IUserSignOnService _userSignOnService, final IUserService _userService, final IStorageService _storageService, final IPoolService _poolService, final SignInValidator _signInValidator) {
        if (_userSignOnService != null) userSignOnService = _userSignOnService;
        if (_userService != null) userService = _userService;
        if (_storageService != null) storageService = _storageService;
        if (_poolService != null) poolService = _poolService;
        if (_signInValidator != null) signInValidator = _signInValidator;
    }

    @RequestMapping(method = RequestMethod.GET)
    public void getStub() {
        log.debug("getStub");
    }

    @ModelAttribute("user")
    public UserDto setUpForm() {
        log.debug("setUpForm");
        return (new UserDto());
    }

    @RequestMapping(method = RequestMethod.POST)
    public String onSubmit(@ModelAttribute(value = "user") final UserDto userParam, final BindingResult result, final SessionStatus status) {
        if (!signInValidator.validate(userParam, result)) return (signinView);
        log.debug("userParam: {}", userParam);
        final String userName = userParam.getName();
        final IUser user = userService.getUserByName(userName);
        userSignOnService.initUserSession(user);
        log.debug("user: {}", user);
        storageService.initUserStorage(user.getDisplayId());
        poolService.initUserPools(user.getDisplayId());
        status.setComplete();
        return (String.format("redirect:userhome.zug?userId=%s", user.getDisplayId()));
    }
}
