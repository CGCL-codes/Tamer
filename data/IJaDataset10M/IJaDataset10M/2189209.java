package com.gcalsync.cal.phonecal;

import com.gcalsync.log.*;
import com.gcalsync.cal.IdCorrelation;
import com.gcalsync.store.Store;
import com.gcalsync.log.*;
import javax.microedition.pim.Event;
import javax.microedition.pim.EventList;
import javax.microedition.pim.PIM;
import javax.microedition.pim.PIMException;
import java.util.Enumeration;
import java.util.Hashtable;

/**
 * @author Thomas Oldervoll, thomas@zenior.no
 * @author $Author: batcage $
 * @version $Rev: 32 $
 * @date $Date: 2006-12-26 23:43:46 -0500 (Tue, 26 Dec 2006) $
 */
public class PhoneCalClient {

    public int createdCount = 0;

    public int updatedCount = 0;

    public int removedCount = 0;

    private EventList phoneEventList;

    public PhoneCalClient() {
        if (phoneEventList == null) {
            try {
                PIM pim = PIM.getInstance();
                phoneEventList = (EventList) pim.openPIMList(PIM.EVENT_LIST, PIM.READ_WRITE);
            } catch (Exception e) {
            }
        }
    }

    public void close() {
        try {
            phoneEventList.close();
        } catch (PIMException e) {
            ErrorHandler.showError("Failed to close phone calendar, events may not have been saved", e);
        }
    }

    public Enumeration getPhoneEvents(long startDate, long endDate) throws PIMException {
        return getPhoneEventList().items(EventList.OCCURRING, startDate, endDate, true);
    }

    private EventList getPhoneEventList() throws PIMException {
        if (phoneEventList == null) {
            PIM pim = PIM.getInstance();
            phoneEventList = (EventList) pim.openPIMList(PIM.EVENT_LIST, PIM.READ_WRITE);
        }
        return phoneEventList;
    }

    public String getStringField(Event phoneEvent, int field) {
        if (phoneEventList.isSupportedField(field) && (phoneEvent.countValues(field) > 0)) {
            return phoneEvent.getString(field, 0);
        } else {
            return null;
        }
    }

    public void setStringField(Event phoneEvent, int field, String value) {
        if (phoneEventList.isSupportedField(field)) {
            if (phoneEvent.countValues(field) == 0) {
                phoneEvent.addString(field, Event.ATTR_NONE, value);
            } else {
                phoneEvent.setString(field, 0, Event.ATTR_NONE, value);
            }
        }
    }

    public long getDateField(Event phoneEvent, int field) {
        if (phoneEventList.isSupportedField(field) && (phoneEvent.countValues(field) > 0)) {
            return phoneEvent.getDate(field, 0);
        } else {
            return 0;
        }
    }

    public void setDateField(Event phoneEvent, int field, long value) {
        if (phoneEventList.isSupportedField(field)) {
            if (phoneEvent.countValues(field) == 0) {
                phoneEvent.addDate(field, Event.ATTR_NONE, value);
            } else {
                phoneEvent.setDate(field, 0, Event.ATTR_NONE, value);
            }
        }
    }

    public int getIntField(Event phoneEvent, int field) {
        if (phoneEventList.isSupportedField(field) && (phoneEvent.countValues(field) > 0)) {
            return phoneEvent.getInt(field, 0);
        } else {
            return -1;
        }
    }

    public void setIntField(Event phoneEvent, int field, int value) {
        if (phoneEventList.isSupportedField(field)) {
            if (phoneEvent.countValues(field) == 0) {
                phoneEvent.addInt(field, Event.ATTR_NONE, value);
            } else {
                phoneEvent.setInt(field, 0, Event.ATTR_NONE, value);
            }
        }
    }

    public String getGCalId(Event phoneEvent) {
        int idField = findIdField();
        String phoneId = phoneEvent.getString(idField, 0);
        String gcalId = (String) Store.getIdCorrelator().phoneIdToGcalId.get(phoneId);
        System.out.println("Read " + phoneId + " -> " + gcalId);
        return gcalId;
    }

    public void setGCalId(Event phoneEvent, String gCalId) {
        int idField = findIdField();
        IdCorrelation idCorrelation = new IdCorrelation();
        idCorrelation.phoneCalId = phoneEvent.getString(idField, 0);
        idCorrelation.gCalId = gCalId;
        System.out.println("Storing " + idCorrelation.phoneCalId + " -> " + idCorrelation.gCalId);
        Store.addCorrelation(idCorrelation);
    }

    private int findIdField() {
        if (phoneEventList.isSupportedField(Event.UID)) {
            return Event.UID;
        } else if (phoneEventList.isSupportedField(Event.LOCATION)) {
            return Event.LOCATION;
        } else if (phoneEventList.isSupportedField(Event.NOTE)) {
            return Event.NOTE;
        } else {
            throw new IllegalStateException("Cannot store ID, neither UID, LOCATION nor NOTE is supported");
        }
    }

    public Event createEvent() {
        return phoneEventList.createEvent();
    }

    public boolean insertEvent(Event phoneEvent, String gCalId) throws PIMException {
        boolean success;
        try {
            phoneEvent.commit();
            setGCalId(phoneEvent, gCalId);
            createdCount++;
            success = true;
        } catch (Exception e) {
            success = false;
        }
        return success;
    }

    public boolean updateEvent(Event phoneEvent) throws PIMException {
        boolean success;
        try {
            phoneEvent.commit();
            updatedCount++;
            success = true;
        } catch (Exception e) {
            success = false;
        }
        return success;
    }

    public boolean removeEvent(Event event) throws PIMException {
        boolean success;
        try {
            phoneEventList.removeEvent(event);
            try {
                event.commit();
            } catch (Exception e) {
            }
            removedCount++;
            success = true;
        } catch (Exception e) {
            success = false;
        }
        return success;
    }

    public void removeDownloadedEvents() {
        try {
            EventList phoneEventList = getPhoneEventList();
            Hashtable phoneIdToGcalId = Store.getIdCorrelator().phoneIdToGcalId;
            Enumeration allPhoneEventsEnum = phoneEventList.items();
            int idField = findIdField();
            while (allPhoneEventsEnum.hasMoreElements()) {
                Event phoneEvent = (Event) allPhoneEventsEnum.nextElement();
                String phoneId = phoneEvent.getString(idField, 0);
                if (phoneIdToGcalId.get(phoneId) != null) {
                    removeEvent(phoneEvent);
                }
            }
        } catch (PIMException e) {
            ErrorHandler.showError("Failed to delete downloaded events", e);
        }
    }

    private boolean shouldDownload() {
        return Store.getOptions().download;
    }
}
