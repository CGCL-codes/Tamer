package mf.torneo.action;

import org.hibernate.HibernateException;

/**
 * Remove the object Categorie
 */
public class RemoveCategorieAction extends mf.torneo.logic.CategorieLogic {

    private static final long serialVersionUID = -608304634611149750L;

    public String execute() throws HibernateException {
        if (hasErrors()) return INPUT; else {
            delete();
            if (hasActionErrors()) return ERROR;
        }
        return SUCCESS;
    }
}
