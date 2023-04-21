package br.com.bafonline.controller.negocio;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import br.com.bafonline.util.struts.action.BaseAction;

/**
 * Action (View) que prepara a tela de uma venda.
 * @author bafonline
 *
 */
public class VendaAction extends BaseAction {

    @Override
    public ActionForward processExecute(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
        return mapping.findForward(SUCCESS);
    }
}
