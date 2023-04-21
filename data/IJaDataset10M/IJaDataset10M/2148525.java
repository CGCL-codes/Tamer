package com.unitt.commons.persist;

import java.util.Calendar;

public class PersistHelper {

    private static PersistHelper instance;

    private ActiveUserHelper activeUserHelper;

    public static PersistHelper instance() {
        if (instance == null) {
            synchronized (PersistHelper.class) {
                if (instance == null) {
                    instance = new PersistHelper();
                }
            }
        }
        return instance;
    }

    protected ActiveUserHelper getActiveUserHelper() {
        return activeUserHelper;
    }

    public void setActiveUserHelper(ActiveUserHelper aActiveUserHelper) {
        activeUserHelper = aActiveUserHelper;
    }

    public void onPersistOrUpdate(PersistedObject aObject) {
        if (aObject.getCreatedOn() == null) {
            aObject.setCreatedById(getActiveUserId());
            aObject.setCreatedOn(Calendar.getInstance());
        }
        aObject.setLastModifiedById(getActiveUserId());
        aObject.setLastModifiedOn(Calendar.getInstance());
    }

    protected long getActiveUserId() {
        if (getActiveUserHelper() != null) {
            return getActiveUserHelper().getActiveUserId();
        }
        return -1;
    }
}
