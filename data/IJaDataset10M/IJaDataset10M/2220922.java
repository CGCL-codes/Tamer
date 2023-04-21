package de.java.com.office.outlook;

import com.jacob.com.Dispatch;
import com.jacob.com.Variant;
import de.java.com.office.AbstractOfficeObject;
import de.java.com.office.outlook.constants.OutlookMethods;
import de.java.com.office.outlook.constants.OutlookProperties;
import de.java.com.office.word.constants.WordProperties;

public class Items extends AbstractOfficeObject {

    public Items(Dispatch dispatch) {
        super(dispatch);
    }

    public AppointmentItem getItem(int index) {
        Dispatch appointmentItem = callMethod(OutlookMethods.ITEM, new Integer(index)).toDispatch();
        return new AppointmentItem(appointmentItem);
    }

    public long getCount() {
        Variant itemCount = getProperty(OutlookProperties.COUNT);
        return itemCount.getInt();
    }

    public void setIncludeRecurrences(boolean value) {
        setProperty(OutlookProperties.INCLUSE_RECURRENCES, value);
    }

    public boolean getIncludeRecurrences() {
        return getProperty(OutlookProperties.INCLUSE_RECURRENCES).getBoolean();
    }

    public void sort(String value) {
        callMethod(OutlookMethods.SORT, value);
    }

    public AppointmentItem getNext() {
        Dispatch appointmentItem = callMethod(OutlookMethods.GET_NEXT).toDispatch();
        return new AppointmentItem(appointmentItem);
    }
}
