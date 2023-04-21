package adc.app.ui.wicket.session;

import adc.app.spec.IBo;
import adc.app.spec.IDao;
import adc.app.spec.IUser;
import adc.app.spec.IUserContext;

/**
 * {@link IUserContext} implementation scoped by Spring to "request".
 * Uses {@link SessionUserContext} to get the id of the current user.
 * @author Alex
 *
 */
public class RequestUserContext implements IUserContext {

    private static final long serialVersionUID = 1L;

    public IUser user;

    private SessionUserContext sessionUserContext;

    private IDao<IBo> dao;

    /**
	 * Getter for the current user.
	 */
    public IUser getUser() {
        return user != null ? user : sessionUserContext.getUserId() != null ? dao.byId(IUser.class, getSessionUserContext().getUserId()) : null;
    }

    /**
	 * Setter for the curent user.
	 */
    public void setUser(IUser user) {
        this.user = user;
        this.sessionUserContext.setUserId(user != null && user.getId() != null ? user.getId() : null);
    }

    public IDao<IBo> getDao() {
        return dao;
    }

    public void setDao(IDao<IBo> dao) {
        this.dao = dao;
    }

    /**
	 * Getter for the {@link SessionUserContext} storing the id of the curent user.
	 */
    public SessionUserContext getSessionUserContext() {
        return sessionUserContext;
    }

    /**
	 * Setter for the {@link SessionUserContext} storing the id of the curent user.
	 */
    public void setSessionUserContext(SessionUserContext sessionUserContext) {
        this.sessionUserContext = sessionUserContext;
    }
}
