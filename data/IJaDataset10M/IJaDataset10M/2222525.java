package net.mjrz.fm.actions;

import java.util.*;
import org.hibernate.Session;
import net.mjrz.fm.entity.*;
import net.mjrz.fm.entity.beans.*;
import net.mjrz.fm.search.Filter;
import net.mjrz.fm.services.SessionManager;
import net.mjrz.fm.utils.AlertsCache;

public class AddAlertAction {

    public AddAlertAction() {
    }

    public ActionResponse executeAction(ActionRequest request) throws Exception {
        ActionResponse resp = new ActionResponse();
        try {
            AlertsEntityManager em = new AlertsEntityManager();
            Alert entry = (Alert) request.getProperty("ENTRY");
            Account a = new FManEntityManager().getAccount(SessionManager.getSessionUserId(), entry.getAccountId());
            em.addAlert(entry);
            resp.addResult("ENTRY", entry);
            AlertsCache.getInstance().addToCache(entry);
            AlertsEntityManager.checkAlert(a, entry);
        } catch (Exception e) {
            resp.setErrorCode(ActionResponse.GENERAL_ERROR);
            resp.setErrorMessage("Unable to add alert, reason = " + e.getMessage());
        }
        return resp;
    }
}
