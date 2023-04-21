package net.ad.adsp.session;

import java.io.Serializable;
import java.util.Date;
import javax.ejb.EnterpriseBean;
import javax.persistence.EntityManager;
import net.ad.adsp.entity.User;
import net.ad.adsp.entity.UserProperties;
import net.ad.adsp.entity.UtentiConnessi;
import org.jboss.seam.ScopeType;
import org.jboss.seam.annotations.In;
import org.jboss.seam.annotations.Logger;
import org.jboss.seam.annotations.Name;
import org.jboss.seam.annotations.Observer;
import org.jboss.seam.annotations.Out;
import org.jboss.seam.annotations.Scope;
import org.jboss.seam.faces.FacesMessages;
import org.jboss.seam.international.LocaleSelector;
import org.jboss.seam.international.StatusMessage.Severity;
import org.jboss.seam.log.Log;
import org.jboss.seam.security.Identity;

@Name("sessionBean")
@Scope(ScopeType.SESSION)
public class SessionBean implements Serializable {

    /**
	 * 
	 */
    private static final long serialVersionUID = 1L;

    @Logger
    private Log log;

    @In(value = "entityManager", create = true)
    private EntityManager em;

    UtentiConnessi utenteConnesso;

    UserProperties userProperties;

    String skin;

    @Observer(Identity.EVENT_LOGGED_OUT)
    public void disconnectUtenteConnesso() {
        try {
            log.debug("Disconnessione Utente Connesso... '#0'", utenteConnesso.getUsername());
            utenteConnesso.setOnLine('0');
            utenteConnesso.setDataFineConnessione(new Date(System.currentTimeMillis()));
            em.merge(utenteConnesso);
            log.debug("Disconnessione Utente Connesso '#0' completata!", utenteConnesso.getUsername());
        } catch (Exception e) {
            log.error("Errore durante la disconnessione dell'Utente Connesso '#0', causa: #1", utenteConnesso.getUsername(), e.toString());
        }
    }

    @Observer("org.jboss.seam.postDestroyContext.SESSION")
    public void disconnectUtenteConnessoByTimeout() {
        if (Identity.instance().isLoggedIn()) {
            disconnectUtenteConnesso();
        }
    }

    public void disconnectUtenteConnessoByBrowserClosing() {
        if (Identity.instance().isLoggedIn()) {
            org.jboss.seam.web.Session.instance().invalidate();
        }
    }

    public String languageForImg() {
        return LocaleSelector.instance().getLocale().toString();
    }

    public void memorizeUserProperties(User user) {
        try {
            if (user.getUserProperties() == null) {
                userProperties = new UserProperties(user, 5);
            } else {
                userProperties = user.getUserProperties();
            }
        } catch (Exception e) {
            log.error("ERRORE in SessionBean.memorizeRowsPage(...), lanciata eccezione '#0'", e.toString());
            userProperties = new UserProperties(user, 5);
        }
    }

    public void saveUserProperties() {
        try {
            userProperties.setSkinName(skin);
            em.merge(userProperties);
            FacesMessages.instance().addFromResourceBundle(Severity.INFO, "success_saveUserProperties", Identity.instance().getCredentials().getUsername(), userProperties.getUser().getUsername());
            log.debug("Utente '#0': salvataggio proprieta' dell'User '#1' completato con successo!", Identity.instance().getCredentials().getUsername(), userProperties.getUser().getUsername());
        } catch (Exception e) {
            em.refresh(userProperties);
            FacesMessages.instance().addFromResourceBundle(Severity.ERROR, "error_saveUserProperties", Identity.instance().getCredentials().getUsername(), userProperties.getUser().getUsername());
            log.error("Errore durante il salvataggio delle proprita' dell'utente '#0', causa: #1", Identity.instance().getCredentials().getUsername(), e.toString());
        }
    }

    /******************************************************************************/
    public UtentiConnessi getUtenteConnesso() {
        return utenteConnesso;
    }

    public void setUtenteConnesso(UtentiConnessi utenteConnesso) {
        this.utenteConnesso = utenteConnesso;
    }

    public UserProperties getUserProperties() {
        return userProperties;
    }

    public void setUserProperties(UserProperties userProperties) {
        this.userProperties = userProperties;
    }

    public String getSkin() {
        if (userProperties != null) {
            skin = userProperties.getSkinName();
        }
        return skin;
    }

    public void setSkin(String skin) {
        this.skin = skin;
    }

    public String getCss() {
        if (userProperties != null && userProperties.getSkinName().equalsIgnoreCase("DEFAULT")) {
            return "stili.css";
        } else {
            return "stiliDefault.css";
        }
    }

    public String getTema() {
        if (userProperties != null && userProperties.getSkinName().equalsIgnoreCase("DEFAULT")) {
            return "xtheme-gray.css";
        } else {
            return "ext-all.css";
        }
    }
}
