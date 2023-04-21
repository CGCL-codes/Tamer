package com.zara.store.web.struts;

import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import com.zara.store.client.clientmodel.Basket;
import com.zara.store.client.clientmodel.RetailBasket;

public class BasketNewAction extends Action {

    public ActionForward execute(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
        Basket basket = new RetailBasket();
        HttpSession session = request.getSession();
        session.setAttribute(Basket.class.getName(), basket);
        return (mapping.findForward("success"));
    }
}
