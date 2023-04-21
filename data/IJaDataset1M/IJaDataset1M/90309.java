package atg.metier.logiquemetier.sessionfacade;

import atg.service.constante.AtgConstantes;
import atg.service.log.AtgLogManager;
import atg.service.servicelocator.ATGServiceLocator;
import atg.service.servicelocator.exception.ATGServiceLocatorException;
import atg.util.singleton.ATGServiceSingleton;

/**
 * <p>Titre : Classe Facade de la partie M�tier de l'application</p>
 * <p>Description : Cette classe sera h�rit�e par les cklasses de l'application
 * dont leur voccation est de servir de fa�ade c'est � dire de pr�senter
 * le coeur m�tier de l'application par des entr�es simplifi�es.</p>
 * <p>Copyright : FERRARI Olivier</p>
 * @author FERRARI Olivier
 * @version 1.0
 * Ce logiciel est r�gi par la licence CeCILL soumise au droit fran�ais et
 * respectant les principes de diffusion des logiciels libres. Vous pouvez
 * utiliser, modifier et/ou redistribuer ce programme sous les conditions
 * de la licence CeCILL telle que diffus�e par le CEA, le CNRS et l'INRIA 
 *  sur le site http://www.cecill.info.
 * 
 * Le fait que vous puissiez acc�der � cet en-t�te signifie que vous avez 
 * pris connaissance de la licence CeCILL, et que vous en avez accept� les
 * termes.
 */
public class ATGSessionFacadeBasic extends atg.util.service.ATGBasicClass implements ATGISessionFacade {

    /**
   * Gestion des logs
   */
    protected static java.util.logging.Logger logger_ = null;

    /**
  * Gestion de la localisation des composants
  */
    static ATGServiceLocator serviceLocator = null;

    /**
 * Retourne le log associ�
 * @return java.util.logging.Logger Log associ�
 */
    protected java.util.logging.Logger getLogger() {
        if (logger_ == null) logger_ = AtgLogManager.getLog(AtgConstantes.ATG_LOG_CATEGORY_LOGIQUEMETIER_FACADE_BASIC);
        return logger_;
    }

    /**
   * Initialisation de la facade, notemment pour utiliser le service locator.
   * Cette m�thode doit �tre lanc�e par la classe qui cr�e l'instance de cette favcade
   * pour l'initialiser.
   */
    public void init() throws ATGServiceLocatorException {
        try {
            serviceLocator = ATGServiceLocator.getInstance();
            logConfig("Initialisation du service locator dans la facade m�tier WF");
        } catch (ATGServiceLocatorException ex) {
            logSevere("Erreur initialisation de la facade m�tier WF");
            throw new ATGServiceLocatorException();
        }
    }

    /**
   * Retourne le service locator associ�
   */
    public ATGServiceLocator getServiceLocator() throws ATGServiceLocatorException {
        if (serviceLocator == null) {
            logSevere("Erreur Access au service locator par facade WF - Lancer l'initilaisation de la fa�ade");
            throw new ATGServiceLocatorException("Service Locator indisponible");
        }
        return serviceLocator;
    }

    /**
   * Retourne une instance de la fa�ade
   */
    public static Object getInstance(Class classParam) {
        ATGServiceSingleton serv = ATGServiceSingleton.getInstance();
        return serv.getInstance(classParam);
    }
}
