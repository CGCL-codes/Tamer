package jlokg.categories;

import javax.swing.JOptionPane;
import jlokg.engine.JLoKGEngine;

/**
 *
 * @author bruno
 */
public class CatChauffageViewControler extends GenericCategoryViewController {

    /** Creates a new instance of CatChauffageViewControler */
    public CatChauffageViewControler() {
        m_label = "Chauffage";
        m_labelList = "Liste des chauffages";
    }

    protected void saveGenericCategory(IGenericCategory cat, JDCategoryUI catUI) {
        CatChauffage localCat = (CatChauffage) cat;
        boolean isNew = (localCat.getCode().equals(""));
        CatChauffageFacade facade = new CatChauffageFacade();
        if (isNew) {
            cat.setCode(JLoKGEngine.getJLoKGEngine().getKeyFor(CatChauffage.class.getName()));
            JLoKGEngine.getJLoKGEngine().getDictionary(CatChauffage.class.getName()).put(localCat.getCode(), localCat);
            facade.saveCatChauffage(localCat);
        } else {
            facade.saveOrUpdateCatChauffage(localCat);
        }
        JOptionPane.showMessageDialog(catUI, "Chauffage sauvegard�");
    }

    protected IGenericCategory getNewCategory() {
        return new CatChauffage();
    }
}
