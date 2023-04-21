package com.ecomponentes.formularios.tipo.form;

import javax.servlet.http.HttpServletRequest;
import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionMapping;
import com.ecomponentes.formularios.tipo.to.TipoTO;

public class TipoListForm extends ActionForm {

    private TipoTO[] tipoTO = new TipoTO[0];

    private String campoBusca = new String();

    private String valorBusca = new String();

    public ActionErrors validate(ActionMapping arg0, HttpServletRequest arg1) {
        ActionErrors actionErrors = new ActionErrors();
        return actionErrors;
    }

    /**
	 * @return Returns the to.
	*/
    public TipoTO[] getTipoTOs() {
        return tipoTO;
    }

    /**
	 * @param to The to to set.
	 */
    public void setTipoTOs(TipoTO[] toValues) {
        this.tipoTO = toValues;
    }

    public String getValorBusca() {
        return valorBusca;
    }

    public void setValorBusca(String valorBusca) {
        this.valorBusca = valorBusca;
    }

    public String getCampoBusca() {
        return campoBusca;
    }

    public void setCampoBusca(String campoBusca) {
        this.campoBusca = campoBusca;
    }

    /**
	 * Method reset
	 * @param mapping
	 * @param request
	 */
    public void reset(ActionMapping mapping, HttpServletRequest request) {
        tipoTO = new TipoTO[0];
    }
}
