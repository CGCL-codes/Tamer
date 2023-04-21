package delphorm.dao.questionnaire;

import java.util.List;
import org.springframework.orm.hibernate3.support.HibernateDaoSupport;
import delphorm.entite.personne.Utilisateur;
import delphorm.entite.questionnaire.InstanceQuestionnaire;
import delphorm.entite.questionnaire.Questionnaire;
import delphorm.entite.questionnaire.ValeurFichier;

/**
 * Implémentation avec Hibernate de la couche persistance pour les objets
 * relatifs à un questionnaire.
 * 
 * @author Jérôme Prudent
 * @version 0.5
 */
public class ImplHibernateQuestionnaire extends HibernateDaoSupport implements IQuestionnaire {

    /**
	 * Permet de persister un questionnaire. Le questionnaire sera ajouté ou
	 * renseigné selon la valeur de l'attribut id du questionnaire.
	 * 
	 * @param questionnaire
	 *            Le questionnaire à persster.
	 * @return Le questionnaire persisté. Si c'est un nouveau questionnaire,
	 *         l'attribut id est renseigné.
	 */
    public Questionnaire enregistrerQuestionnaire(Questionnaire questionnaire) {
        getHibernateTemplate().saveOrUpdate(questionnaire);
        return questionnaire;
    }

    public List<Questionnaire> getAllTriesParTitre() {
        String requete = "FROM delphorm.entite.questionnaire.Questionnaire q ORDER BY q.titre";
        List l = getHibernateTemplate().find(requete);
        return l;
    }

    public List<Questionnaire> getUtilisateursTriesParTitre(Utilisateur utilisateur) {
        String requete = "FROM delphorm.entite.questionnaire.Questionnaire q WHERE q.auteur=" + utilisateur.getId() + "ORDER BY q.titre";
        List l = getHibernateTemplate().find(requete);
        return l;
    }

    public Questionnaire getQuestionnaireParId(Long id) {
        String requete = "FROM delphorm.entite.questionnaire.Questionnaire q WHERE q.id=" + id;
        List<Questionnaire> l = getHibernateTemplate().find(requete);
        if (l.size() == 1) return l.get(0); else return null;
    }

    public void supprimerQuestionnaire(Long id) {
        Questionnaire q = getQuestionnaireParId(id);
        getHibernateTemplate().delete(q);
    }

    public List<Questionnaire> getQuestionnaireEnPublication() {
        String requete = "FROM delphorm.entite.questionnaire.Questionnaire q WHERE q.etat='P'";
        List<Questionnaire> l = getHibernateTemplate().find(requete);
        return l;
    }

    public InstanceQuestionnaire enregistrerInstanceQuestionnaire(InstanceQuestionnaire instanceQuestionnaire) {
        getHibernateTemplate().saveOrUpdate(instanceQuestionnaire);
        return instanceQuestionnaire;
    }

    public List<InstanceQuestionnaire> getInstancesQuestionnaireParIdQuestionnaire(Long idQuestionnaire) {
        String requete = "FROM delphorm.entite.questionnaire.InstanceQuestionnaire iq WHERE iq.questionnaire.id=" + idQuestionnaire;
        List<InstanceQuestionnaire> l = getHibernateTemplate().find(requete);
        return l;
    }

    public InstanceQuestionnaire getInstancesQuestionnaireParId(Long numeroInstanceQuestionnaire) {
        String requete = "FROM delphorm.entite.questionnaire.InstanceQuestionnaire iq WHERE iq.id=" + numeroInstanceQuestionnaire;
        List<InstanceQuestionnaire> l = getHibernateTemplate().find(requete);
        if (l.size() == 1) return l.get(0); else return null;
    }

    public ValeurFichier getValeurFichier(Integer idFichier) {
        String requete = "FROM delphorm.entite.questionnaire.ValeurFichier fic WHERE fic.id=" + idFichier;
        List<ValeurFichier> l = getHibernateTemplate().find(requete);
        if (l.size() == 1) return l.get(0); else return null;
    }

    public List<InstanceQuestionnaire> getInstancesQuestionnaireEnCoursParUtilisateur(Utilisateur utilisateur) {
        String requete = "FROM delphorm.entite.questionnaire.InstanceQuestionnaire iq WHERE iq.auteur.id = " + utilisateur.getId() + " AND iq.etat='C'";
        List<InstanceQuestionnaire> l = getHibernateTemplate().find(requete);
        return l;
    }
}
