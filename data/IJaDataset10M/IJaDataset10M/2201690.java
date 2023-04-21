package com.jungbo.servlet.centric.control;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import com.jungbo.servlet.centric.dto.CustUserDto;
import com.jungbo.servlet.centric.help.ActionForward;
import com.jungbo.servlet.centric.help.ActionMapping;
import com.jungbo.servlet.centric.help.CustError;
import com.jungbo.servlet.centric.model.CustUserService;

public class CustUserDetailAction implements Action {

    @Override
    public ActionForward execute(HttpServletRequest request, HttpServletResponse response, ActionMapping maps) {
        CustUserService manager = CustUserService.getInstance();
        String id = request.getParameter("id");
        CustUserDto cust = manager.getCustUser(id);
        ActionForward forword = new ActionForward();
        if (cust != null) {
            request.setAttribute("cust", cust);
            forword.setUrl(maps.getKey("detail"));
            forword.setRedirect(false);
        } else {
            CustError errors = new CustError();
            errors.setErrorMessage("�? �󼼺��⿡ �����߽��ϴ�.");
            errors.setErrorType("��û�Ķ����, DB��� ");
            request.setAttribute("errors", errors);
            forword.setUrl(maps.getKey("errors"));
            forword.setRedirect(false);
        }
        return forword;
    }
}
