package lichen.pages.base;

import lichen.entities.user.User;
import lichen.internal.services.UserServiceImpl;
import org.apache.tapestry5.ioc.annotations.Inject;
import org.apache.tapestry5.services.Cookies;
import org.springframework.orm.hibernate3.HibernateTemplate;

/**
 * 抽象控制类页面。
 * @author <a href=mailto:xiafei114@gmail.com>xiafei114</a>
 * @author Jun Tsai
 * @version $Revision: 225 $
 * @since 0.0.3
 */
public class AbstractControlPage extends BasePage {

    @Inject
    private Cookies cookies;

    @Inject
    private HibernateTemplate template;

    /**
	 * @return Returns the cookies.
	 */
    public Cookies getCookies() {
        return cookies;
    }

    /**
	 * @param cookies The cookies to set.
	 */
    public void setCookies(Cookies cookies) {
        this.cookies = cookies;
    }

    /**
	 * @return Returns the template.
	 */
    public HibernateTemplate getTemplate() {
        return template;
    }

    /**
	 * @param template The template to set.
	 */
    public void setTemplate(HibernateTemplate template) {
        this.template = template;
    }

    /**
	 * 是否登录.
	 * @return 是否登录.
	 */
    public boolean isLogin() {
        String userId = cookies.readCookieValue(UserServiceImpl.LICHEN_USER_ID_COOKIE_NAME);
        if (userId != null) {
            User user = (User) template.get(User.class, userId);
            if (user == null) {
                cookies.removeCookieValue(UserServiceImpl.LICHEN_USER_ID_COOKIE_NAME);
            }
            return user != null;
        }
        return false;
    }

    public User getUser() {
        if (isLogin()) {
            String userId = cookies.readCookieValue(UserServiceImpl.LICHEN_USER_ID_COOKIE_NAME);
            User user = (User) template.get(User.class, userId);
            return user;
        } else {
            throw new RuntimeException("invalid request");
        }
    }
}
