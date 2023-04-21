package fr.gouv.defense.terre.esat.formathlon.metier.uc00303;

import fr.gouv.defense.terre.esat.formathlon.entity.Formation;
import fr.gouv.defense.terre.esat.formathlon.entity.SessionFormation;
import fr.gouv.defense.terre.esat.formathlon.metier.exception.MetierException;
import fr.gouv.defense.terre.esat.formathlon.persistence.sessionbean.FormationPersistence;
import fr.gouv.defense.terre.esat.formathlon.persistence.sessionbean.SessionFormationPersistence;
import java.util.List;
import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.ejb.LocalBean;

/**
 *
 * @author maxime.guinchard
 */
@Stateless
@LocalBean
public class UC00303Metier {

    @EJB
    private SessionFormationPersistence sessionFormationPersistence;

    @EJB
    private FormationPersistence formationPersistence;

    /**
     * Methode qui retourne l'ensemble des sessions de formation
     * 
     * @return List<SessionFormation> listeSession
     */
    public List<SessionFormation> listerSessions() {
        return sessionFormationPersistence.readAll();
    }

    /**
     * Methode qui permet de supprimer une session d'une formation
     * 
     * @param maFormation Session formation 
     */
    public void supprimeSession(SessionFormation maSession) {
        if (maSession.getLstInscriptions().size() > 0) {
            throw new MetierException("uc_303_erreurSessionInscrits");
        }
        Formation formation = formationPersistence.read(maSession.getFormation().getId());
        formation.getLstSessionsFormation().remove(maSession);
        formationPersistence.update(formation);
        sessionFormationPersistence.delete(maSession);
    }
}
