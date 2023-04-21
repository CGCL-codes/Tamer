package com.javaeedev.web.runtime;

import java.io.PrintWriter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.Controller;
import com.javaeedev.util.HttpUtil;
import com.javaeedev.web.Identity;

/**
 * Show user's signon state. This will return a global javascript variable 
 * "g_signon", "g_username", "g_nickname", "g_boards". No transaction needed.
 * 
 * @author Xuefeng
 * 
 * @spring.bean name="/runtime/state.jspx"
 */
public class StateController implements Controller {

    private static final String NOT_SIGNON = "var g_signon=false;\nvar g_username='';\nvar g_nickname='';\n";

    public ModelAndView handleRequest(HttpServletRequest request, HttpServletResponse response) throws Exception {
        response.setContentType("application/x-javascript;charset=UTF-8");
        response.setHeader("Cache-Control", "no-cache");
        Identity identity = HttpUtil.getIdentityFromSession(request.getSession());
        PrintWriter writer = response.getWriter();
        if (identity == null) {
            writer.write(NOT_SIGNON);
        } else {
            writer.write("var g_signon=true;\nvar g_username='");
            writer.write(identity.username);
            writer.write("';\nvar g_nickname='");
            writer.write(identity.nickname);
            writer.write("';\nvar g_boards='");
            writer.write(identity.getBoards());
            writer.write("';\n");
        }
        writer.flush();
        return null;
    }
}
