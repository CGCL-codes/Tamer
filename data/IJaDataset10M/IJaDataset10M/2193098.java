package org.campware.cream.modules.screens;

import java.util.List;
import org.apache.turbine.util.RunData;
import org.apache.torque.util.Criteria;
import org.campware.cream.om.ContactPeer;
import org.campware.cream.om.ContactCategoryPeer;
import org.campware.cream.om.CountryPeer;

/**
 * To read comments for this class, please see
 * the ancestor class
 */
public class ContactList extends CreamList {

    protected void initScreen() {
        setModuleType(ENTITY);
        setModuleName("CONTACT");
        setIdName(ContactPeer.CONTACT_ID);
        setDefOrderColumn(ContactPeer.CONTACT_DISPLAY);
    }

    protected String getSortColumn(int sortNo) {
        if (sortNo == 1) {
            return ContactPeer.CONTACT_CODE;
        } else if (sortNo == 2) {
            return ContactPeer.CONTACT_DISPLAY;
        } else if (sortNo == 3) {
            return ContactCategoryPeer.CONTACT_CAT_NAME;
        } else if (sortNo == 4) {
            return ContactPeer.CITY;
        } else if (sortNo == 5) {
            return CountryPeer.COUNTRY_CODE;
        }
        return "";
    }

    protected void setFilter(int filterNo, Criteria listCriteria, RunData data) {
        try {
            if (filterNo == 1001) {
                listCriteria.add(ContactPeer.STATUS, new Integer(30), Criteria.EQUAL);
            } else if (filterNo == 1002) {
                listCriteria.add(ContactPeer.STATUS, new Integer(50), Criteria.EQUAL);
            } else if (filterNo == 1003) {
                listCriteria.add(ContactPeer.CREATED_BY, (Object) data.getUser().getName(), Criteria.EQUAL);
            }
        } catch (Exception e) {
        }
    }

    protected void setFind(String findStr, Criteria listCriteria) {
        try {
            listCriteria.add(ContactPeer.CONTACT_DISPLAY, (Object) findStr, Criteria.LIKE);
        } catch (Exception e) {
        }
    }

    protected List getEntries(Criteria criteria) {
        try {
            criteria.addJoin(ContactCategoryPeer.CONTACT_CAT_ID, ContactPeer.CONTACT_CAT_ID);
            criteria.addJoin(CountryPeer.COUNTRY_ID, ContactPeer.COUNTRY_ID);
            return ContactPeer.doSelect(criteria);
        } catch (Exception e) {
            return null;
        }
    }
}
