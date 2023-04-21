package at.rc.tacos.web.controller;

import java.util.HashMap;
import java.util.Map;
import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Error Controller
 * @author Payer Martin
 * @version 1.0
 */
public class ErrorController extends Controller {

    public Map<String, Object> handleRequest(HttpServletRequest request, HttpServletResponse response, ServletContext context) throws Exception {
        Map<String, Object> params = new HashMap<String, Object>();
        return params;
    }
}
