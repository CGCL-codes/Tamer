package com.ecomponentes.formularios.fonte.action;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import com.ecomponentes.formularios.fonte.bo.FonteBO;
import com.ecomponentes.formularios.fonte.dao.FonteDAO;
import com.ecomponentes.formularios.fonte.form.FonteListForm;

public class FonteListAction extends Action {

    /**
	 * Method loads book from DB
	 * @param mapping
	 * @param form
	 * @param request
	 * @param response
	 * @return ActionForward
	 */
    public ActionForward execute(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) {
        FonteListForm fonteListForm = new FonteListForm();
        fonteListForm = (FonteListForm) form;
        FonteBO fonteBO = new FonteBO();
        if (fonteListForm.getCampoBusca() != null && !fonteListForm.getCampoBusca().equals("")) {
            fonteListForm.setFonteTOs(fonteBO.getFontes(fonteListForm.getCampoBusca(), fonteListForm.getValorBusca()));
        } else {
            fonteListForm.setFonteTOs(fonteBO.selecionarTodos());
        }
        FonteDAO dao = new FonteDAO();
        return mapping.findForward("showList");
    }
}
