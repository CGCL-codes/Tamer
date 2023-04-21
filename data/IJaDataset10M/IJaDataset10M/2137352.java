package org.promotego.controllers;

import java.util.List;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.promotego.beans.OfferedGame;
import org.promotego.beans.User;
import org.promotego.beans.UserHolder;
import org.promotego.dao.interfaces.UserDao;
import org.springframework.beans.factory.annotation.Required;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.AbstractController;

public class ShowOfferedGamesController extends AbstractController {

    private UserHolder m_userHolder;

    private UserDao m_userDao;

    @Override
    protected ModelAndView handleRequestInternal(HttpServletRequest request, HttpServletResponse response) throws Exception {
        assert m_userHolder.getUser() != null : "User object may not be null";
        User theUser = m_userHolder.getUser();
        m_userDao.reattach(theUser);
        List<OfferedGame> offeredGames = theUser.getOfferedGames();
        return new ModelAndView("showOfferedGames", "offeredGames", offeredGames);
    }

    @Required
    public void setUserHolder(UserHolder theUserHolder) {
        m_userHolder = theUserHolder;
    }

    @Required
    public void setUserDao(UserDao userDao) {
        m_userDao = userDao;
    }
}
