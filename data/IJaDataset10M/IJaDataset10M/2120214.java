package fr.cpbrennestt.metier.facade.implementations;

import java.util.List;
import fr.cpbrennestt.acces_donnees.dao.interfaces.ClassementDao;
import fr.cpbrennestt.metier.entite.Classement;
import fr.cpbrennestt.metier.facade.interfaces.ClassementManager;

public class ClassementManagerImpl implements ClassementManager {

    private ClassementDao classementDao;

    public void setClassementDao(ClassementDao classementDao) {
        this.classementDao = classementDao;
    }

    @Override
    public void enregistrerClassement(Classement classement) throws Exception {
        classementDao.saveOrUpdateClassement(classement);
    }

    @Override
    public Classement rechercherClassement(String numLicence, int annee, int mois) throws Exception {
        return classementDao.getClassement(numLicence, annee, mois);
    }

    @Override
    public List<Classement> rechercherClassements(int numLicence) throws Exception {
        return null;
    }

    @Override
    public void supprimerClassement(Classement classement) throws Exception {
        classementDao.saveOrUpdateClassement(classement);
    }

    @Override
    public Classement rechercherMeilleurClassement(int action, int annee, int mois) throws Exception {
        Classement classementRes = null;
        switch(action) {
            case ACTION_MOIS:
                classementRes = classementDao.getBestClassementMonth(annee, mois);
                break;
            case ACTION_PHASE:
                classementRes = classementDao.getBestClassementPhase(annee, mois);
                break;
            case ACTION_ANNEE:
                classementRes = classementDao.getBestClassementYear(annee, mois);
                break;
            default:
                break;
        }
        return classementRes;
    }
}
