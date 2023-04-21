package com.scholardesk.abstracts.edit;

import javax.servlet.http.HttpServletRequest;
import com.scholardesk.abstracts.AbstractsTask;

public class DefaultTask extends AbstractsTask {

    private boolean m_verification_passed = true;

    @Override
    public void process() {
        String _password = http_request.getParameter("_password");
        if (_password == null) return;
        if (account == null) throw new IllegalStateException("Session has expired! Please sign-in again.");
        if (account.verifyPassword(_password)) http_session.setAttribute("verification_confirmed", true); else m_verification_passed = false;
    }

    @Override
    public String getView() {
        if (http_session.getAttribute("verification_confirmed") == null) return "confirm_password.vm";
        return "edit.vm";
    }

    @Override
    public HttpServletRequest setRequest() {
        if (!m_verification_passed) http_request.setAttribute("verify_error_message", config.getString("edit.password_verification_failed")); else http_request.setAttribute("load_js", toJavascriptArray("response", getWebParams(account)));
        return http_request;
    }
}
