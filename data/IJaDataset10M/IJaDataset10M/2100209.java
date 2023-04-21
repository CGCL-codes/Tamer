package mf.torneo.action;

import org.hibernate.HibernateException;

/**
 * parameter for search the object Team
 */
public class SearchTeamAction extends mf.torneo.logic.TeamLogic {

    private static final long serialVersionUID = 1035039265442986486L;

    public String execute() throws HibernateException {
        if (hasErrors()) return INPUT;
        return SUCCESS;
    }
}
