package mf.torneo.action;

import org.hibernate.HibernateException;

/**
 * getter object Play
 */
public class ViewPlayAction extends mf.torneo.logic.PlayLogic {

    private static final long serialVersionUID = 5706505021982744569L;

    public String execute() throws HibernateException {
        if (hasErrors()) return INPUT; else if (getIdplay() == null) {
            return ERROR;
        } else {
            getDataDB();
            if (hasActionErrors()) return ERROR;
        }
        return SUCCESS;
    }
}
