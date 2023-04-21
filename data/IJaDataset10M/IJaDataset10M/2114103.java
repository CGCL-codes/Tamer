package fr.cpbrennestt.presentation.action.gestion.club.bureau;

import javax.servlet.http.HttpServletRequest;
import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.apache.struts2.ServletActionContext;
import fr.cpbrennestt.metier.entite.Joueur;
import fr.cpbrennestt.metier.entite.MembreBureau;
import fr.cpbrennestt.presentation.action.BaseAction;
import fr.cpbrennestt.presentation.utils.Config;
import fr.cpbrennestt.presentation.utils.Utils;

@SuppressWarnings("serial")
public class SuppressionMembreBureauAction extends BaseAction {

    private static Logger logger = Logger.getLogger(SuppressionMembreBureauAction.class);

    public String supprimerMembreBureau() {
        try {
            logger.debug("DEBUT - supprimerMembreBureau");
            chargement();
            if (!checkUserPresent()) {
                addActionError("Cette partie du site est priv�e. Veuillez vous connecter pour y acc�der.");
                logger.debug("FIN - supprimerMembreBureau : " + ERROR);
                return ERROR;
            }
            HttpServletRequest request = ServletActionContext.getRequest();
            String numLicence = request.getParameter("numLicence");
            if (StringUtils.isEmpty(numLicence)) {
                addActionError("Le num�ro de licence doit �tre diff�rent de 0.");
                return ERROR;
            }
            logger.debug("Recherche du joueur - numLicence :" + numLicence);
            Joueur joueur = joueurManager.rechercherJoueur(numLicence, Config.getAnneeCourante());
            if (joueur == null) {
                addActionError("Le membre du bureau � supprimer n'existe pas.");
                logger.debug("Le membre du bureau � supprimer n'existe pas.");
                return ERROR;
            }
            MembreBureau membreBureau = bureauManager.rechercherMembreBureau(joueur);
            String nomPhoto = membreBureau.getNomPhoto();
            if (StringUtils.isNotEmpty(nomPhoto)) {
                logger.debug("Suppression de la photo.");
                Utils.supprimerFichier(nomPhoto, PATH_IMAGES_BUREAU);
            }
            if (logger.isDebugEnabled()) {
                logger.debug("Suppression du membre : " + joueur.getPrenom() + " " + joueur.getNom());
            }
            joueurManager.retirerJoueurBureau(joueur);
            addActionMessage("Le membre du bureau a bien �t� supprim�.");
            logger.debug("FIN - supprimerMembreBureau : " + SUCCESS);
            return SUCCESS;
        } catch (Exception e) {
            addActionError("Erreur lors de la suppression du membre du bureau.");
            logger.error("FIN - supprimerMembreBureau ", e);
            return ERROR;
        }
    }
}
