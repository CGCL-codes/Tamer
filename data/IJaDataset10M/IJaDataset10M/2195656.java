package com.vlee.servlet.onestop;

import com.vlee.servlet.main.*;
import javax.servlet.*;
import javax.servlet.http.*;
import com.vlee.ejb.user.*;
import com.vlee.util.*;

public class DoIndex extends ActionDo implements Action {

    public ActionRouter perform(HttpServlet servlet, HttpServletRequest req, HttpServletResponse res) throws java.io.IOException, javax.servlet.ServletException {
        return new ActionRouter("onestop-index-page");
    }
}
