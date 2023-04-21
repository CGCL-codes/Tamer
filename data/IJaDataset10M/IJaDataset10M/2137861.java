package org.ministone.security.web;

import org.springframework.web.servlet.mvc.SimpleFormController;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.view.RedirectView;
import org.springframework.validation.BindException;
import org.ministone.arena.vo.Arena;
import org.ministone.arena.web.helper.ArenaSession;
import org.ministone.arena.service.ArenaService;
import org.ministone.ServiceLocator;
import org.ministone.mlets.user.vo.Account;
import org.ministone.portal.web.PortalSession;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * @author : Sun Wenju
 * @since 0.1
 *        date 2007-6-6 20:56:47
 */
public class LoginController extends SimpleFormController {

    @Override
    protected Object formBackingObject(HttpServletRequest request) throws Exception {
        return new LoginForm();
    }

    @Override
    protected ModelAndView onSubmit(HttpServletRequest request, HttpServletResponse httpServletResponse, Object object, BindException bindException) throws Exception {
        LoginForm loginForm = (LoginForm) object;
        ArenaService arenaService = ServiceLocator.instance().getArenaService();
        Account account = arenaService.login(loginForm.getUserName(), loginForm.getPassword());
        Arena arena = arenaService.getArenaByUserName(loginForm.getUserName());
        ArenaSession.setOwnerArena(request, arena);
        PortalSession.setOwnerPortal(request, arena.getPortal());
        return new ModelAndView(new RedirectView(super.getSuccessView()));
    }
}
