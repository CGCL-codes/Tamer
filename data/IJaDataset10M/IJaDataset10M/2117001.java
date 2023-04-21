package com.c2b2.ipoint.business;

import com.c2b2.ipoint.model.Blog;
import com.c2b2.ipoint.model.BlogEntry;
import com.c2b2.ipoint.model.Code;
import com.c2b2.ipoint.model.Group;
import com.c2b2.ipoint.model.PersistentModelException;
import com.c2b2.ipoint.model.User;
import com.c2b2.ipoint.model.casemanagement.Case;
import com.c2b2.ipoint.model.casemanagement.CaseNotification;
import com.c2b2.ipoint.processing.PortalRequest;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

public class CaseManagementServices {

    public CaseManagementServices() {
    }

    /**
   * Create a new support case by calling to the model
   * @param engineer
   * @param reporter
   * @param caseTitle
   * @param caseBlog
   * @param dateRaised
   * @param description
   * @param status
   * @param pri
   * @param ty
   * @return
   * @throws PersistentModelException
   */
    public Case createCase(User engineer, User reporter, String caseTitle, Blog caseBlog, Date dateRaised, String description, Code status, Code pri, Code ty) throws PersistentModelException {
        Case result = null;
        result = Case.createCase(caseTitle, description, engineer, reporter, dateRaised, status, caseBlog, pri, ty);
        result.getCaseBlog().createEntry(reporter, "Case " + caseTitle + " Created", description);
        CaseNotification.createCaseNotification(reporter, engineer, result);
        return result;
    }

    /**
   * Delete a case and any CaseNotifications which are attached to it.
   * @param c
   * @throws PersistentModelException
   */
    public void deletCase(Case c) throws PersistentModelException {
        List<CaseNotification> notifications = CaseNotification.findNotificationsForCase(c);
        Iterator<CaseNotification> it = notifications.iterator();
        while (it.hasNext()) {
            CaseNotification deleteMe = it.next();
            CaseNotification.delete(deleteMe);
        }
        Case.delete(c);
    }

    /**
   * Assign a case to a user and update any notifications on that case.
   * @param c
   * @param u
   * @throws PersistentModelException
   */
    public void assignCase(Case c, User u) throws PersistentModelException {
        User oldEngineer = c.getEngineer();
        c.setEngineer(u);
        c.getCaseBlog().setOwner(u);
        StringBuffer note = new StringBuffer();
        note.append("Case Reassigned from Engineer ");
        note.append(oldEngineer.getDetails().getFirstName());
        note.append(" ");
        note.append(oldEngineer.getDetails().getLastName());
        note.append(" to ");
        note.append(u.getDetails().getFirstName());
        note.append(" ");
        note.append(u.getDetails().getLastName());
        BlogEntry entry = c.getCaseBlog().createEntry(u, "Case Reassigned", note.toString());
        List<CaseNotification> notifications = CaseNotification.findNotificationsForCase(c);
        Iterator<CaseNotification> it = notifications.iterator();
        while (it.hasNext()) {
            CaseNotification checkMe = it.next();
            if (checkMe.getEngineer() != u) {
                checkMe.setEngineer(u);
            }
        }
    }

    /**
   * Set a case to the closed status and remove any notifications set on the case
   * @param c
   * @throws PersistentModelException
   */
    public void closeCase(Case c) throws PersistentModelException {
        Code closedStatus = Code.find("Case_Status", "Closed");
        c.setStatus(closedStatus);
        List<CaseNotification> notifications = CaseNotification.findNotificationsForCase(c);
        Iterator<CaseNotification> it = notifications.iterator();
        while (it.hasNext()) {
            CaseNotification.delete(it.next());
        }
    }
}
