package br.ufrn.cerescaico.sepe.interceptors;

import br.ufrn.cerescaico.sepe.actions.LogonAction;
import br.ufrn.cerescaico.sepe.actions.SepeAction;
import br.ufrn.cerescaico.sepe.beans.Usuario;
import com.opensymphony.xwork2.ActionInvocation;
import com.opensymphony.xwork2.interceptor.MethodFilterInterceptor;

/**
 * Interceptor para login e bloqueio de actions não permitidas.
 * @author Taciano Morais Silva
 * @version 12/02/2009, 11h37m
 * @since 28/10/2008, 14h32m
 */
public class StrutsInterceptor extends MethodFilterInterceptor {

    /**
     * serialVersionUID.
     */
    private static final long serialVersionUID = 2995090738909466564L;

    /**
     * Intercepta a chamada a um action e verifica se há algum usuário logado.
     * @param invocation Um action invocation.
     * @return O string de forward para a página correspondente.
     * @throws java.lang.Exception Caso ocorra alguma exception.
     */
    @Override
    protected String doIntercept(ActionInvocation invocation) throws Exception {
        SepeAction action = (SepeAction) invocation.getAction();
        if (action instanceof LogonAction) {
            return invocation.invoke();
        }
        Usuario user = action.getSepe().getUsuarioSessao();
        if (action.getPrecisaLogar() && (user == null || !user.getPerfil().equals(action.getPerfil()))) {
            action.addActionError(action.getText("erro.usuario.nao.logado"));
            return "login-fail";
        } else {
            action.getSepe().closeSessaoHibernate();
            return invocation.invoke();
        }
    }
}
