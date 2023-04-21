package com.alianzamedica.view;

import javax.servlet.http.HttpServletRequest;
import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ActionMessage;
import com.alianzamedica.businessobject.Lot;
import com.alianzamedica.tools.ValidateTool;

/**
 * @author Carlos
 * 
 */
public class LotViewForm extends ActionForm {

    /**
	 * 
	 */
    private static final long serialVersionUID = -8654089280265485382L;

    private Integer id;

    private String code;

    /**
	 * regresa el id del lote.
	 * 
	 * @return id del lote.
	 */
    public Integer getId() {
        return id;
    }

    /**
	 * setea el id del lote
	 * 
	 * @param id
	 *            del lote
	 */
    public void setId(Integer id) {
        this.id = id;
    }

    /**
	 * obtiene el codigo del lote.
	 * 
	 * @return codigo del lote.
	 */
    public String getCode() {
        return code;
    }

    /**
	 * setea el codigo del lote.
	 * 
	 * @param code
	 *            codigo del lote.
	 */
    public void setCode(String code) {
        this.code = code;
    }

    /**
	 * regresa el lote.
	 * 
	 * @return lote.
	 */
    public Lot getLot() {
        Lot l = new Lot();
        l.setId(id);
        l.setCode(code);
        return l;
    }

    @Override
    public ActionErrors validate(ActionMapping mapping, HttpServletRequest request) {
        ActionErrors errors = new ActionErrors();
        ValidateTool validateTool = new ValidateTool();
        if (validateTool.isStringEmpty(code)) {
            errors.add("code", new ActionMessage("Codigo es requerido"));
        }
        if (validateTool.isStringEmpty(String.valueOf(id))) {
            errors.add("id", new ActionMessage("ID es requerido"));
        }
        if (errors.size() > 0) {
            request.setAttribute("error", errors);
            Lot lot = this.getLot();
            request.setAttribute("lot", lot);
        }
        return errors;
    }
}
