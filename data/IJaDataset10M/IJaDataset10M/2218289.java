package aplicacion;

import javax.servlet.http.HttpServletRequest;
import org.apache.struts.action.ActionMessage;
import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionMapping;

public class UsersListForm extends ActionForm {

    private String idAEliminar;

    /**
	 * Resets data fields to initial values on loginform
	 * @param mapping
	 * @param request
	 */
    @Override
    public void reset(ActionMapping mapping, HttpServletRequest request) {
        idAEliminar = "";
    }

    /**
	 * Performs validation of data on loginform
	 * @param mapping
	 * @param request
	 * @return ActionErrors
	 */
    @Override
    public ActionErrors validate(ActionMapping mapping, HttpServletRequest request) {
        ActionErrors errors = new ActionErrors();
        if (idAEliminar == null) errors.add("idAEliminar", new ActionMessage("error.idAEliminar.required"));
        return errors;
    }

    /**
	 * @return String
	 */
    public String getIdAEliminar() {
        return idAEliminar;
    }

    /**
	 * @param string
	 */
    public void setIdAEliminar(String string) {
        idAEliminar = string;
    }
}
