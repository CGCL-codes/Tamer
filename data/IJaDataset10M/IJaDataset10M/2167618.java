package mf.torneo.action;

/**
 * Save the object Societa
 */
public class UpdateSocietaAction extends mf.torneo.logic.SocietaLogic {

    private static final long serialVersionUID = -8661532620604615863L;

    public String execute() {
        if (hasErrors()) return INPUT; else {
            saveForm();
            if (hasActionErrors()) return ERROR;
        }
        return SUCCESS;
    }
}
