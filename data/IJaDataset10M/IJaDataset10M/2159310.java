package ch.trackedbean.client.views;

import static ch.trackedbean.client.resources.Messages.*;
import ch.trackedbean.binding.action.*;
import ch.trackedbean.binding.components.*;
import ch.trackedbean.server.data.dom.*;
import ch.trackedbean.server.data.to.*;

/**
 * Table of {@link PersonShort} elements.
 * 
 * @author hautle
 */
@SuppressWarnings("serial")
public class PersonList extends TablePanel<PersonShort> {

    /**
     * Default constructor.
     */
    @SuppressWarnings("unchecked")
    public PersonList() {
        super(true, false);
        addColumn(NAME, PersonShort.ATTR_NAME);
        addColumn(FIRST_NAME, PersonShort.ATTR_FIRST_NAME);
        addColumn(AGE, PersonShort.ATTR_AGE);
        addColumn(ADDRESS, PersonShort.ATTR_ADDRESS, "${" + AddressTO.ATTR_STREET + "} ${" + AddressTO.ATTR_STREET_NUMBER + "}, ${" + AddressTO.ATTR_PLACE + "." + PlaceDom.ATTR_CITY + "}");
        setDefaultAction(ActionManager.getAction(PersonDetail.DETAIL_ACTION_ID));
    }
}
