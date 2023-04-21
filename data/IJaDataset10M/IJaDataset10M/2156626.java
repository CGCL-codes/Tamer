package siac.com.controller;

import java.io.IOException;
import java.security.NoSuchAlgorithmException;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import siac.com.dao.AuthUtilizadorDao;
import siac.com.entity.AuthUtilizador;
import siac.com.util.EncriptacaoUtil;
import siac.com.util.HttpJSFUtil;
import siac.com.util.JsfUtil;
import siac.com.util.PaginaUtil;

@ManagedBean
@SessionScoped
public class AuthUtilizadorLoginController {

    private AuthUtilizador userLogado;

    private String paginaDestino;

    private String tituloPagina;

    private boolean loginOk = false;

    public AuthUtilizadorLoginController() {
        this.userLogado = new AuthUtilizador();
        tituloPagina = "Login";
    }

    public String goToLogin() {
        HttpSession sessao = HttpJSFUtil.getSession(false);
        HttpServletResponse rp = HttpJSFUtil.getResponse();
        HttpServletRequest rq = HttpJSFUtil.getRequest();
        userLogado = null;
        sessao.invalidate();
        loginOk = false;
        try {
            rp.sendRedirect(rq.getContextPath() + PaginaUtil.LOGIN);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return "goToLogin";
    }

    public String doLogin() {
        HttpSession sessao = HttpJSFUtil.getSession(false);
        HttpServletResponse rp = HttpJSFUtil.getResponse();
        HttpServletRequest rq = HttpJSFUtil.getRequest();
        AuthUtilizadorDao dao = new AuthUtilizadorDao();
        AuthUtilizador info = dao.findByLogin(userLogado.getUsername());
        try {
            if (info != null) {
                this.userLogado = info;
                sessao.setAttribute("user", this.userLogado);
                loginOk = true;
                rp.sendRedirect(rq.getContextPath() + PaginaUtil.HOME);
                return null;
            } else {
                sessao.setAttribute("user", null);
                sessao.removeAttribute("user");
                loginOk = false;
                JsfUtil.addErrorMessage("Username ou Password invalida.");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return (loginOk ? "loginOk" : "loginBAD");
    }

    public boolean validaLogin() {
        if ((userLogado.getUsername() == null || userLogado.getUsername().trim().toUpperCase().equals("")) && (userLogado.getPassword() == null || userLogado.getPassword().trim().toUpperCase().equals(""))) return false;
        return true;
    }

    public String doLogout() {
        HttpSession sessao = HttpJSFUtil.getSession(false);
        HttpServletResponse rp = HttpJSFUtil.getResponse();
        HttpServletRequest rq = HttpJSFUtil.getRequest();
        try {
            userLogado = null;
            sessao.removeAttribute("user");
            sessao.invalidate();
            loginOk = false;
            rp.sendRedirect(rq.getContextPath() + PaginaUtil.HOME);
            return "logoutOk";
        } catch (Exception e) {
            e.printStackTrace();
            return "logoutBAD";
        }
    }

    /**
	 * @return the userLogado
	 */
    public AuthUtilizador getUserLogado() {
        return userLogado;
    }

    /**
	 * @param userLogado the userLogado to set
	 */
    public void setUserLogado(AuthUtilizador userLogado) {
        this.userLogado = userLogado;
    }

    /**
	 * @return the paginaDestino
	 */
    public String getPaginaDestino() {
        return paginaDestino;
    }

    /**
	 * @param paginaDestino the paginaDestino to set
	 */
    public void setPaginaDestino(String paginaDestino) {
        this.paginaDestino = paginaDestino;
    }

    /**
	 * @return the tituloPagina
	 */
    public String getTituloPagina() {
        return tituloPagina;
    }

    /**
	 * @param tituloPagina the tituloPagina to set
	 */
    public void setTituloPagina(String tituloPagina) {
        this.tituloPagina = tituloPagina;
    }

    /**
	 * @return the loginOk
	 */
    public boolean isLoginOk() {
        return loginOk;
    }

    /**
	 * @param loginOk the loginOk to set
	 */
    public void setLoginOk(boolean loginOk) {
        this.loginOk = loginOk;
    }
}
