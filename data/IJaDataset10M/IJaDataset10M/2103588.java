package br.com.wepa.webapps.orca.logica.negocio.facade.to;

import java.util.List;
import br.com.wepa.webapps.orca.controle.actions.login.UsuarioSessao;
import br.com.wepa.webapps.security.keys.AuthenticationKey;

/**
 * UsuarioTO 
 */
public class LoginTO extends UsuarioTO {

    /**
	 * 
	 */
    private static final long serialVersionUID = 1L;

    private List menuItens;

    private AuthenticationKey authenticationKey;

    private String contextPath;

    private UsuarioSessao usuarioSessao;

    public AuthenticationKey getAuthenticationrKey() {
        return authenticationKey;
    }

    public void setAuthenticationrKey(AuthenticationKey authenticationKey) {
        this.authenticationKey = authenticationKey;
    }

    public UsuarioSessao getUsuarioSessao() {
        return usuarioSessao;
    }

    public void setUsuarioSessao(UsuarioSessao usuarioSessao) {
        this.usuarioSessao = usuarioSessao;
    }

    public List getMenuItens() {
        return menuItens;
    }

    public void setMenuItens(List menuItens) {
        this.menuItens = menuItens;
    }

    public String getContextPath() {
        return contextPath;
    }

    public void setContextPath(String contextPath) {
        this.contextPath = contextPath;
    }
}
