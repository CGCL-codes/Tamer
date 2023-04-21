package org.campware.cream.modules.screens;

import org.apache.torque.util.Criteria;
import org.apache.velocity.context.Context;
import org.campware.cream.om.Country;
import org.campware.cream.om.CountryPeer;

/**
 * To read comments for this class, please see
 * the ancestor class
 */
public class CountryForm extends CreamLookupForm {

    protected void initScreen() {
        setModuleType(LOOKUP);
        setModuleName("COUNTRY");
        setIdName(CountryPeer.COUNTRY_ID);
        setFormIdName("countryid");
    }

    protected boolean getEntry(Criteria criteria, Context context) {
        try {
            Country entry = (Country) CountryPeer.doSelect(criteria).get(0);
            context.put("entry", entry);
            return true;
        } catch (Exception e) {
            return false;
        }
    }
}
