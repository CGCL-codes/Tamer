package com.ordocalendarws.model;

import com.ordocalendarws.model.objects.OrdoDayObject;
import javax.jdo.PersistenceManager;
import javax.jdo.Query;

public class OrdoDayModel {

    public static OrdoDayObject getDay(String date) {
        PersistenceManager pm = PMF.get().getPersistenceManager();
        OrdoDayObject od = null;
        try {
            od = pm.getObjectById(OrdoDayObject.class, date);
        } catch (javax.jdo.JDOObjectNotFoundException ex) {
            od = null;
        }
        return od;
    }

    public static void deleteAllDay() {
        PersistenceManager pm = PMF.get().getPersistenceManager();
        Query query = pm.newQuery(OrdoDayObject.class);
        try {
            if (query != null) query.deletePersistentAll();
        } finally {
            if (query != null) query.closeAll();
        }
    }
}
