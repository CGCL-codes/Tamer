package net.sf.epontus.store;

import com.opensymphony.xwork2.ActionContext;
import com.opensymphony.xwork2.ActionSupport;
import com.opensymphony.xwork2.util.ValueStack;
import net.sf.epontus.persistence.Customer;
import org.apache.log4j.Logger;
import org.hibernate.Session;

/**
 * Created by IntelliJ IDEA.
 * User: thilo
 * Date: 02.05.2008
 * Time: 10:29:42
 */
public class Paging extends ActionSupport {

    private static Logger logger = Logger.getLogger(Paging.class);

    private int p = 1;

    public int getP() {
        return p;
    }

    public void setP(int p) {
        this.p = p;
    }

    public ValueStack getStack() {
        return ActionContext.getContext().getValueStack();
    }

    public Customer getAcc() {
        return (Customer) getStack().findValue("account");
    }

    public Session getSession() {
        return (Session) getStack().findValue("hibernateSession");
    }

    public boolean isAdmin() {
        Boolean isAdmin = (Boolean) getStack().findValue("isAdmin");
        return isAdmin == null ? false : isAdmin;
    }

    public boolean isLoggedIn() {
        return getAcc().getId() > 0;
    }
}
