package org.insia.teamexperts.action;

import java.util.List;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.apache.log4j.Logger;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.actions.DispatchAction;
import org.apache.struts.validator.DynaValidatorForm;
import org.insia.teamexperts.dao.factory.DAOFactory;
import org.insia.teamexperts.dao.interfaces.product.IBrandDAO;
import org.insia.teamexperts.model.product.Brand;

/**
 * @author ibitan
 *
 */
public class BrandManagementAction extends DispatchAction {

    @SuppressWarnings("unused")
    private static Logger _logger = Logger.getLogger(BrandManagementAction.class);

    @Override
    protected ActionForward unspecified(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
        IBrandDAO dao = DAOFactory.getBrandDAO();
        List<Brand> list = (List<Brand>) dao.getList();
        request.setAttribute("brandList", list);
        return mapping.findForward("pageList");
    }

    protected ActionForward search(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
        DynaValidatorForm frm = (DynaValidatorForm) form;
        IBrandDAO dao = DAOFactory.getBrandDAO();
        List<Brand> list = (List<Brand>) dao.getBrandList(frm.getString("keyword"));
        request.setAttribute("brandList", list);
        return mapping.findForward("pageList");
    }
}
