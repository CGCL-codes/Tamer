package com.alianzamedica.view;

import javax.servlet.http.HttpServletRequest;
import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ActionMessage;
import com.alianzamedica.tools.ValidateTool;

/**
 * @author Carlos
 * 
 */
public class PrescriptionForm extends ActionForm {

    /**
	 * 
	 */
    private static final long serialVersionUID = 1635483911274177515L;

    private String pacienteId;

    private String tag;

    private String guardar;

    /**
	 * id del paciente.
	 * 
	 * @return id del paciente.
	 */
    public String getPacienteId() {
        return pacienteId;
    }

    /**
	 * id del paciente.
	 * 
	 * @param pacienteId
	 *            id del paciente.
	 */
    public void setPacienteId(String pacienteId) {
        this.pacienteId = pacienteId;
    }

    /**
	 * tag.
	 * 
	 * @return tag.
	 */
    public String getTag() {
        return tag;
    }

    /**
	 * tag.
	 * 
	 * @param tag
	 *            tag.
	 */
    public void setTag(String tag) {
        this.tag = tag;
    }

    /**
	 * boton guardar.
	 * 
	 * @return boton guardar.
	 */
    public String getGuardar() {
        return guardar;
    }

    /**
	 * boton guardar.
	 * 
	 * @param guardar
	 *            boton guardar.
	 */
    public void setGuardar(String guardar) {
        this.guardar = guardar;
    }

    @Override
    public ActionErrors validate(ActionMapping mapping, HttpServletRequest request) {
        ActionErrors errors = new ActionErrors();
        ValidateTool validateTool = new ValidateTool();
        if (validateTool.isStringEmpty(pacienteId)) {
            errors.add("pacienteId", new ActionMessage("el paciente es un campo requerido"));
        }
        if (validateTool.isStringEmpty(tag)) {
            errors.add("tag", new ActionMessage("tag es un campo requerido"));
        }
        if (errors.size() > 0) {
            request.setAttribute("errors", errors);
        }
        return errors;
    }
}
