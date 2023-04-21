package mf.torneo.action;

import org.hibernate.HibernateException;

/**
 * Remove the object Team
 */
public class RemoveTeamAction extends mf.torneo.logic.TeamLogic {

    private static final long serialVersionUID = 4860581147459749819L;

    public String execute() throws HibernateException {
        if (hasErrors()) return INPUT; else {
            delete();
            if (hasActionErrors()) return ERROR;
        }
        return SUCCESS;
    }
}
