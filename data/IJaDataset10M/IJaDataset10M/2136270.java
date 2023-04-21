package interceptors;

import java.util.Map;
import session.SessionConstants;
import interfacesAware.UsuarioAware;
import com.opensymphony.xwork2.ActionContext;
import com.opensymphony.xwork2.ActionInvocation;
import com.opensymphony.xwork2.ActionSupport;
import com.opensymphony.xwork2.interceptor.Interceptor;
import entidades.usuario.Usuario;

/**
 * 
 * Injeta as variaveis aware do sistema quando a action implementa a interface correspondente
 *
 */
public class AwareSetterInterceptor implements Interceptor {

    private static final long serialVersionUID = 1L;

    @Override
    public void destroy() {
    }

    @Override
    public void init() {
    }

    @SuppressWarnings("unchecked")
    @Override
    public String intercept(ActionInvocation invocation) throws Exception {
        Map<String, Object> parameters = ActionContext.getContext().getSession();
        Usuario usuario = (Usuario) parameters.get(SessionConstants.VARIAVEL_USUARIO);
        ActionSupport action = (ActionSupport) invocation.getAction();
        if (action instanceof UsuarioAware) {
            ((UsuarioAware) action).setUsuario(usuario);
        }
        return invocation.invoke();
    }
}
