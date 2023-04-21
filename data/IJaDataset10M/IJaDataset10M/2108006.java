package org.comptahome.metier.impl;

import javax.persistence.EntityExistsException;
import javax.persistence.EntityNotFoundException;
import javax.persistence.TransactionRequiredException;
import org.comptahome.dao.IAffectationDao;
import org.comptahome.metier.IGererAffectation;
import org.comptahome.model.Affectation;
import org.comptahome.model.Categorie;
import org.comptahome.model.Operation;
import org.comptahome.util.ExceptionApplicative;

public class GererAffectation implements IGererAffectation {

    private IAffectationDao affectationDao;

    /**
	 * TODO que faire lors d'une supression ? pour les autres pourcentage s? recalcul ?
	 */
    public Affectation creer(Operation operation, Categorie categorie, int pourcentage) throws ExceptionApplicative {
        Affectation affectation = new Affectation();
        try {
            affectation.setCategorie(categorie);
            affectation.setOperation(operation);
            affectation.setPourcentage(pourcentage);
        } catch (EntityExistsException e) {
            throw new ExceptionApplicative("GererAffectation - L'affectation existe d�j�", e);
        } catch (TransactionRequiredException e1) {
            throw new ExceptionApplicative("GererAffectation - Erreur de Transaction base de donn�es", e1);
        }
        return affectation;
    }

    public Affectation lire(Long id) throws ExceptionApplicative {
        Affectation retour = null;
        try {
            retour = affectationDao.getReference(id);
        } catch (EntityNotFoundException e) {
            throw new ExceptionApplicative("GererAffectation - Reference non trouvee", e);
        }
        return retour;
    }

    public Affectation modifier(Affectation affectation) {
        affectationDao.update(affectation);
        return affectation;
    }

    public void supprimer(Affectation affectation) {
        affectationDao.delete(affectation);
    }

    public IAffectationDao getAffectationDao() {
        return affectationDao;
    }

    public void setAffectationDao(IAffectationDao affectationDao) {
        this.affectationDao = affectationDao;
    }
}
