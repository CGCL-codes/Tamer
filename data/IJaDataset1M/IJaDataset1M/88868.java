package com.controlexpenses.controller;

import java.util.List;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import com.controlexpenses.business.FunctionaryService;
import com.controlexpenses.controller.framework.AbstractController;

public class FuncListContr extends AbstractController {

    /**
	 * Servi�o de funcion�rios
	 */
    private FunctionaryService funcServ;

    public void setFuncServ(FunctionaryService funcServ) {
        this.funcServ = funcServ;
    }

    @Override
    public String exec(HttpServletRequest req, HttpServletResponse res) {
        List funcs = funcServ.findFunctionary();
        req.setAttribute("funcs", funcs);
        return getSuccess();
    }
}
