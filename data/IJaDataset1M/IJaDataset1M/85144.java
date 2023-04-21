package de.java.com.office.outlook;

import com.jacob.com.Dispatch;
import com.jacob.com.Variant;
import de.java.com.office.AbstractOfficeObject;
import de.java.com.office.outlook.constants.OutlookMethods;
import de.java.com.office.outlook.constants.OutlookProperties;

public class AddressEntries extends AbstractOfficeObject {

    public AddressEntries(Dispatch dispatch) {
        super(dispatch);
    }

    public String getName() {
        Variant textString = getProperty(OutlookProperties.NAME);
        return textString.toString().trim();
    }

    public AddressEntry getItem(int index) {
        Dispatch addressEntry = callMethod(OutlookMethods.ITEM, new Integer(index)).toDispatch();
        return new AddressEntry(addressEntry);
    }

    public long getCount() {
        Variant tablesCount = getProperty(OutlookProperties.COUNT);
        return tablesCount.getInt();
    }
}
