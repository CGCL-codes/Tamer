package fr.cpbrennestt.metier.facade.implementations;

import java.util.List;
import fr.cpbrennestt.acces_donnees.dao.interfaces.DonneeConfDao;
import fr.cpbrennestt.metier.entite.DonneeConf;
import fr.cpbrennestt.metier.facade.interfaces.DonneeConfManager;

public class DonneeConfManagerImpl implements DonneeConfManager {

    private DonneeConfDao donneeConfDao;

    public void setDonneeConfDao(DonneeConfDao donneeConfDao) {
        this.donneeConfDao = donneeConfDao;
    }

    @Override
    public List<DonneeConf> listeDonneesConf() throws Exception {
        return donneeConfDao.getDonneesConf();
    }

    @Override
    public DonneeConf modifierDonneeConf(DonneeConf donneeConf) {
        return donneeConfDao.modifyDonneeConf(donneeConf);
    }
}
