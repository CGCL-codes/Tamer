package in980.rgms.presentation;

import in980.rgms.domain.Usuario;
import com.opensymphony.xwork2.ActionInvocation;
import com.opensymphony.xwork2.interceptor.Interceptor;

public class AutenticacaoInterceptor implements Interceptor {

    private static final long serialVersionUID = 1L;

    @Override
    public void destroy() {
    }

    @Override
    public void init() {
    }

    @Override
    public String intercept(ActionInvocation invocation) throws Exception {
        Usuario usuarioLogado = (Usuario) invocation.getInvocationContext().getSession().get("usuarioAutenticado");
        if (usuarioLogado == null) {
            return "usuarioNaoAutenticado";
        } else {
            return invocation.invoke();
        }
    }
}
