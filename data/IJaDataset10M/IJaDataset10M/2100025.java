package com.sos.controller;

import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.http.HttpSession;
import com.sos.vo.Usuario;

public class Utils {

    protected static boolean validateSession(HttpSession session, String sessionId) throws ServletException, IOException {
        return session.getAttribute(sessionId) != null;
    }

    protected static boolean validateRoles(HttpSession session, String sessionId, int[] roles) {
        Usuario usuario = (Usuario) session.getAttribute(sessionId);
        for (int role : roles) {
            if (role == usuario.getNivel()) {
                return true;
            }
        }
        return false;
    }
}
