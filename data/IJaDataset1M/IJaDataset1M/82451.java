package com.ecomponentes.formularios.tratamento.action;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import com.ecomponentes.formularios.tratamento.bo.TratamentoBO;
import com.ecomponentes.formularios.tratamento.dao.TratamentoDAO;
import com.ecomponentes.formularios.tratamento.form.TratamentoListForm;

public class TratamentoListAction extends Action {

    /**
	 * Method loads book from DB
	 * @param mapping
	 * @param form
	 * @param request
	 * @param response
	 * @return ActionForward
	 */
    public ActionForward execute(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) {
        TratamentoListForm tratamentoListForm = new TratamentoListForm();
        tratamentoListForm = (TratamentoListForm) form;
        TratamentoBO tratamentoBO = new TratamentoBO();
        if (tratamentoListForm.getCampoBusca() != null && !tratamentoListForm.getCampoBusca().equals("")) {
            tratamentoListForm.setTratamentoTOs(tratamentoBO.getTratamentos(tratamentoListForm.getCampoBusca(), tratamentoListForm.getValorBusca()));
        } else {
            tratamentoListForm.setTratamentoTOs(tratamentoBO.selecionarTodos());
        }
        TratamentoDAO dao = new TratamentoDAO();
        return mapping.findForward("showList");
    }
}
