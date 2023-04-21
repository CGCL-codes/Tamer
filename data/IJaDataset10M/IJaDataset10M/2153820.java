package br.gov.frameworkdemoiselle.internal.implementation;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;
import javax.enterprise.context.SessionScoped;
import javax.enterprise.event.Observes;
import javax.faces.context.FacesContext;
import javax.inject.Inject;
import javax.servlet.http.HttpSession;
import br.gov.frameworkdemoiselle.annotation.PageNotFoundException;
import br.gov.frameworkdemoiselle.event.AfterLoginSuccessful;
import br.gov.frameworkdemoiselle.event.AfterLogoutSuccessful;
import br.gov.frameworkdemoiselle.internal.configuration.JsfSecurityConfig;
import br.gov.frameworkdemoiselle.util.Redirector;
import com.sun.faces.config.ConfigurationException;

@SessionScoped
public class SecurityObserver implements Serializable {

    private static final long serialVersionUID = 1L;

    @Inject
    private FacesContext facesContext;

    @Inject
    private JsfSecurityConfig config;

    @Inject
    private HttpSession session;

    private Map<String, Object> savedParams = new HashMap<String, Object>();

    private String savedViewId;

    public SecurityObserver() {
        clear();
    }

    private void saveCurrentState() {
        clear();
        if (!config.getLoginPage().equals(facesContext.getViewRoot().getViewId())) {
            savedParams.putAll(facesContext.getExternalContext().getRequestParameterMap());
            savedViewId = facesContext.getViewRoot().getViewId();
        }
    }

    public void redirectToLoginPage() {
        saveCurrentState();
        try {
            Redirector.redirect(config.getLoginPage());
        } catch (PageNotFoundException cause) {
            throw new ConfigurationException("A tela de login \"" + cause.getViewId() + "\" não foi encontrada. Caso o seu projeto possua outra, defina no arquivo de configuração a chave \"" + "frameworkdemoiselle.security.login.page" + "\"", cause);
        }
    }

    public void onLoginSuccessful(@Observes final AfterLoginSuccessful event) {
        boolean isWelcomePage = false;
        if (savedViewId == null) {
            savedViewId = config.getWelcomePage();
            isWelcomePage = true;
        }
        try {
            Redirector.redirect(savedViewId, savedParams);
        } catch (PageNotFoundException cause) {
            if (isWelcomePage) {
                throw new ConfigurationException("A tela de boas-vindas \"" + cause.getViewId() + "\" não foi encontrada. Caso o seu projeto possua outra, defina no arquivo de configuração a chave \"" + "frameworkdemoiselle.security.welcome.page" + "\"", cause);
            } else {
                throw cause;
            }
        } finally {
            clear();
        }
    }

    public void onLogoutSuccessful(@Observes final AfterLogoutSuccessful event) {
        try {
            Redirector.redirect(config.getWelcomePage());
        } catch (PageNotFoundException cause) {
            throw new ConfigurationException("A tela de boas-vindas \"" + cause.getViewId() + "\" não foi encontrada. Caso o seu projeto possua outra, defina no arquivo de configuração a chave \"" + "frameworkdemoiselle.security.welcome.page" + "\"", cause);
        } finally {
            session.invalidate();
        }
    }

    private void clear() {
        savedViewId = null;
        savedParams.clear();
    }
}
