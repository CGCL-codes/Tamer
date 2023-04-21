package org.easyrec.controller;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.springframework.web.servlet.ModelAndView;
import org.easyrec.model.web.Operator;
import org.easyrec.model.web.RemoteTenant;
import org.easyrec.utils.Security;
import org.easyrec.utils.servlet.ServletUtils;
import org.springframework.web.servlet.mvc.multiaction.MultiActionController;

/**
 * This Controller shows the API documentation.
 *
 * <p><b>Company:&nbsp;</b>
 * SAT, Research Studios Austria</p>
 *
 * <p><b>Copyright:&nbsp;</b>
 * (c) 2007</p>
 *
 * <p><b>last modified:</b><br/>
 * $Author: phlavac $<br/>
 * $Date: 2010-02-26 16:54:38 +0100 (Fr, 26 Feb 2010) $<br/>
 * $Revision: 15673 $</p>
 *
 * @author dmann
 * @version 1.0
 * @since 1.0
 */
public class ApiController extends MultiActionController {

    private ModelAndView security(HttpServletRequest request) {
        ModelAndView mav = new ModelAndView("page");
        String apiKey = Operator.DEFAULT_API_KEY;
        String tenantId = RemoteTenant.DEFAULT_TENANT_ID;
        Operator signedInOperator = Security.signedInOperator(request);
        if (signedInOperator != null) {
            apiKey = signedInOperator.getApiKey();
        }
        mav.addObject("apiKey", apiKey);
        mav.addObject("tenant", tenantId);
        mav.addObject("selectedMenu", "api");
        mav.addObject("signedIn", Security.isSignedIn(request));
        return mav;
    }

    public ModelAndView restapi(HttpServletRequest request, HttpServletResponse httpServletResponse) {
        ModelAndView mav = security(request);
        mav.addObject("title", "easyrec :: rest api");
        mav.addObject("page", "apidoc/overview");
        return mav;
    }

    public ModelAndView doc(HttpServletRequest request, HttpServletResponse httpServletResponse) {
        ModelAndView mav = security(request);
        String method = ServletUtils.getSafeParameter(request, "method", "");
        mav.addObject("title", "easyrec :: api :: " + method);
        mav.addObject("page", "apidoc/doc");
        mav.addObject("doc", method);
        return mav;
    }

    /**
     * prints the documentation in raw html no css and page context
     * @param request
     * @param httpServletResponse
     * @return
     */
    public ModelAndView rawdoc(HttpServletRequest request, HttpServletResponse httpServletResponse) {
        ModelAndView mav = security(request);
        String method = ServletUtils.getSafeParameter(request, "method", "");
        mav.addObject("title", "easyrec :: api :: " + method);
        mav.setViewName("apidoc/" + method);
        return mav;
    }
}
