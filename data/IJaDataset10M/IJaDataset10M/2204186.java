package com.butnet.myframe.actions;

import java.io.IOException;
import java.lang.reflect.Method;
import java.util.LinkedHashMap;
import java.util.Map;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import net.sf.json.JSON;
import net.sf.json.JSONObject;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

public class BaseDispatchAction extends BaseAction {

    private Map<String, Method> methods = new LinkedHashMap<String, Method>();

    @Override
    public ActionForward execute(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
        String uri = request.getRequestURI().toLowerCase();
        boolean json = uri.endsWith(".json");
        String p = mapping.getParameter();
        if (p != null && p.trim().length() != 0) {
            String methodName = request.getParameter(p.trim());
            if (!(methodName == null || methodName.trim().length() == 0)) {
                Method method = null;
                if (methods.containsKey(methodName)) method = methods.get(methodName); else {
                    try {
                        method = this.getClass().getMethod(methodName, ActionMapping.class, ActionForm.class, HttpServletRequest.class, HttpServletResponse.class);
                        method.setAccessible(false);
                        methods.put(methodName, method);
                    } catch (Exception e) {
                    }
                }
                if (method != null) {
                    Object obj = method.invoke(this, mapping, form, request, response);
                    if (obj instanceof ActionForward) return (ActionForward) obj;
                    processOver(obj, request, response, methodName);
                    return null;
                }
                if (json) {
                    JSONObject jsonObj = new JSONObject();
                    setErrorMsg(jsonObj, "ERROR: " + this.getClass().getName() + "." + methodName + " mthod is not found");
                    printJSON(response, jsonObj);
                } else {
                    response.getWriter().print("ERROR: " + this.getClass().getName() + "." + methodName + " mthod is not found");
                }
            }
        } else {
            if (json) {
                JSONObject jsonObj = new JSONObject();
                setErrorMsg(jsonObj, "ERROR: " + this.getClass().getName() + " is configed as not a DispatchAction");
                printJSON(response, jsonObj);
            } else {
                response.getWriter().print("ERROR: " + this.getClass().getName() + " is configed as not a DispatchAction");
            }
        }
        return null;
    }

    private void processOver(Object reValue, HttpServletRequest request, HttpServletResponse response, String methodName) throws IOException, ServletException {
        String uri = request.getRequestURI();
        String format = uri.toLowerCase();
        boolean json = format.endsWith(".json");
        boolean jt = format.endsWith(".jt");
        if (json) {
            if (reValue instanceof JSON) printJSON(response, (JSON) reValue); else {
                JSONObject jsonObject = new JSONObject();
                setSuccessData(jsonObject, reValue);
                printJSON(response, jsonObject);
            }
            return;
        } else if (jt) {
            request.setAttribute(methodName, reValue);
            String forwardJspUrl = getServlet().getServletContext().getInitParameter("forwardJspUrl");
            if (forwardJspUrl != null) {
                if (!forwardJspUrl.endsWith("/")) forwardJspUrl += '/';
                int index;
                if ((index = uri.lastIndexOf('/')) != -1) {
                    forwardJspUrl += uri.substring(index + 1);
                } else forwardJspUrl += uri;
                if ((index = forwardJspUrl.indexOf('.')) != -1) {
                    forwardJspUrl = forwardJspUrl.substring(0, index);
                }
                forwardJspUrl += "/" + methodName;
                forwardJspUrl += ".jsp";
                request.getRequestDispatcher(forwardJspUrl).forward(request, response);
                return;
            }
        }
        request.setAttribute(methodName, reValue);
        response.getWriter().print("INFO: " + this.getClass().getName() + "." + methodName + " return [" + reValue + "] is not a ActionForward object");
        return;
    }
}
