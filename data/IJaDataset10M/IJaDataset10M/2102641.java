package net.sourceforge.strueaf;

import com.opensymphony.xwork2.ActionSupport;

public class HelpProvider extends ActionSupport {

    private static final long serialVersionUID = 2311300973698781767L;

    private String m_key;

    public String execute() {
        return "success";
    }

    public void setKey(String p_key) {
        m_key = p_key;
    }

    public String getKey() {
        return m_key;
    }

    public String getHelptext() {
        String retval;
        retval = getText(m_key);
        if (retval.equals(m_key)) {
            retval = null;
        }
        return retval;
    }
}
