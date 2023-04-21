package org.javalid.test.springweb.controller;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.javalid.test.springweb.model.Account;
import org.springframework.orm.ObjectRetrievalFailureException;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.Controller;

/**
 * For sake of simplicity we 'grab' this bean in the detail controller (of course this
 * is very bad practise etc., but that's not the aim of this test :p).
 * The bean provides an overview of accounts.
 * @author  M.Reuvers
 * @version 1.0
 * @since   1.0
 */
public class AccountOverviewController implements Controller {

    protected static final String NAME = "accountOverviewController";

    private List<Account> accounts;

    private long indexCount = 1L;

    public AccountOverviewController() {
        accounts = new ArrayList<Account>();
        Account act = new Account();
        act.setFirstName("Someone");
        act.setLastName("Special");
        act.setId(indexCount++);
        act.setLoginName("someone");
        act.setPassword("secret");
        accounts.add(act);
    }

    public ModelAndView handleRequest(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse) {
        return new ModelAndView("account_overview", "accounts", accounts);
    }

    public long getNextIndexCount() {
        return indexCount++;
    }

    /**
   * Open it up so we can find an account from the accountDetailController
   * (bad practise, don't do in real environment!)
   * @param id Id
   * @return Account or ObjectRetrievalFailureException if not found
   */
    protected Account findAccount(Long id) {
        Iterator<Account> itr = accounts.iterator();
        Account act = null;
        while (itr.hasNext()) {
            act = itr.next();
            if (act.getId().equals(id)) {
                break;
            }
        }
        if (act == null) {
            throw new ObjectRetrievalFailureException(Account.class, id);
        }
        return act;
    }

    protected void addAccount(Account act) {
        this.accounts.add(act);
    }
}
