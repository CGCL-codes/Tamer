package com.technoetic.xplanner.history;

import java.util.Date;
import net.sf.hibernate.Session;
import com.technoetic.xplanner.domain.DomainObject;

public class Historian {

    private Session session;

    private int currentUserId;

    public Historian(Session session, int currentUserId) {
        this.session = session;
        this.currentUserId = currentUserId;
    }

    public void saveEvent(DomainObject object, String action, String description, Date when) {
        HistorySupport.saveEvent(session, object, action, description, currentUserId, when);
    }
}
