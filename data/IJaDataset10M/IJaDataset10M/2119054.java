package mx.com.nyak.base.struts2.action;

import java.util.List;
import javax.servlet.http.HttpServletRequest;
import mx.com.nyak.base.dto.User;
import mx.com.nyak.empresa.service.impl.UsuarioService;
import org.apache.log4j.Logger;
import org.apache.struts2.ServletActionContext;
import com.opensymphony.xwork2.ActionInvocation;
import com.opensymphony.xwork2.interceptor.AbstractInterceptor;

public class UserInterceptor extends AbstractInterceptor {

    /**
	 * 
	 */
    private static final long serialVersionUID = 1L;

    private User user;

    private UsuarioService usuarioService;

    private static Logger logger = Logger.getLogger(UserInterceptor.class);

    @SuppressWarnings("unchecked")
    @Override
    public String intercept(ActionInvocation invocation) throws Exception {
        HttpServletRequest request = ServletActionContext.getRequest();
        logger.debug("Obtiene El usuario: " + request.getUserPrincipal().getName());
        List<User> users = null;
        users = (List<User>) getUsuarioService().findByProperty("user", request.getUserPrincipal().getName());
        logger.debug("lista de usuarios: " + users);
        if (users != null) {
            user = (User) users.get(0);
            logger.debug("setting the user attribute: " + user);
            request.getSession().setAttribute("user", user);
        }
        return invocation.invoke();
    }

    public void setUser(User user) {
        this.user = user;
    }

    public User getUser() {
        return user;
    }

    public void setUsuarioService(UsuarioService usuarioService) {
        this.usuarioService = usuarioService;
    }

    public UsuarioService getUsuarioService() {
        return usuarioService;
    }
}
