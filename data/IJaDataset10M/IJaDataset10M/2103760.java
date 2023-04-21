package br.edu.uncisal.action;

import java.util.Map;
import org.apache.struts2.interceptor.SessionAware;
import com.opensymphony.xwork2.ActionSupport;

public abstract class AbstractAction extends ActionSupport implements SessionAware {

    private static final long serialVersionUID = 3388063575099126817L;

    protected Map<String, Object> session;

    @SuppressWarnings("unchecked")
    public final void setSession(Map session) {
        this.session = session;
    }

    public boolean hasMessages() {
        return hasActionMessages() || hasActionErrors() || hasFieldErrors();
    }
}
