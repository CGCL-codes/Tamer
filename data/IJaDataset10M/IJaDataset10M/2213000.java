package fr.cpbrennestt.presentation.action.frontal.newsletter;

import java.util.List;
import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import fr.cpbrennestt.metier.entite.Email;
import fr.cpbrennestt.presentation.action.BaseAction;

@SuppressWarnings("serial")
public class DesabonnementAction extends BaseAction {

    private static Logger logger = Logger.getLogger(DesabonnementAction.class);

    private String email;

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String validerDesabonnement() {
        try {
            logger.debug("DEBUT - validerDesabonnement");
            chargement();
            if (StringUtils.isEmpty(email)) {
                addActionError("Veuillez indiquer une adresse e-mail.");
                return INPUT;
            }
            List<Email> emails = emailManager.rechercherEmailsAvecAbonnement(email);
            if (emails.size() != 0) {
                for (Email e : emails) {
                    e.setNewsletter(false);
                    emailManager.enregistrerEmail(e);
                }
            } else {
                addActionError("Il n'y a aucun abonnement correspondant � cet e-mail.");
                return INPUT;
            }
            addActionMessage("Vous �tes maintenant d�sabonn� � la newsletter.");
            logger.debug("FIN - validerDesabonnement");
            return SUCCESS;
        } catch (Exception e) {
            logger.error("Erreur lors du d�sabonnement.");
            addActionError("Erreur lors du d�sabonnement.");
            return INPUT;
        }
    }
}
