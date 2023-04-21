package com.liferay.portlet.addressbook.service.persistence;

import com.liferay.util.dao.hibernate.Transformer;

/**
 * <a href="ABContactHBMUtil.java.html"><b><i>View Source</i></b></a>
 *
 * @author  Brian Wing Shun Chan
 *
 */
public class ABContactHBMUtil implements Transformer {

    public static com.liferay.portlet.addressbook.model.ABContact model(ABContactHBM abContactHBM) {
        return model(abContactHBM, true);
    }

    public static com.liferay.portlet.addressbook.model.ABContact model(ABContactHBM abContactHBM, boolean checkPool) {
        com.liferay.portlet.addressbook.model.ABContact abContact = null;
        if (checkPool) {
            abContact = ABContactPool.get(abContactHBM.getPrimaryKey());
        }
        if (abContact == null) {
            abContact = new com.liferay.portlet.addressbook.model.ABContact(abContactHBM.getContactId(), abContactHBM.getUserId(), abContactHBM.getFirstName(), abContactHBM.getMiddleName(), abContactHBM.getLastName(), abContactHBM.getNickName(), abContactHBM.getEmailAddress(), abContactHBM.getHomeStreet(), abContactHBM.getHomeCity(), abContactHBM.getHomeState(), abContactHBM.getHomeZip(), abContactHBM.getHomeCountry(), abContactHBM.getHomePhone(), abContactHBM.getHomeFax(), abContactHBM.getHomeCell(), abContactHBM.getHomePager(), abContactHBM.getHomeTollFree(), abContactHBM.getHomeEmailAddress(), abContactHBM.getBusinessCompany(), abContactHBM.getBusinessStreet(), abContactHBM.getBusinessCity(), abContactHBM.getBusinessState(), abContactHBM.getBusinessZip(), abContactHBM.getBusinessCountry(), abContactHBM.getBusinessPhone(), abContactHBM.getBusinessFax(), abContactHBM.getBusinessCell(), abContactHBM.getBusinessPager(), abContactHBM.getBusinessTollFree(), abContactHBM.getBusinessEmailAddress(), abContactHBM.getEmployeeNumber(), abContactHBM.getJobTitle(), abContactHBM.getJobClass(), abContactHBM.getHoursOfOperation(), abContactHBM.getBirthday(), abContactHBM.getTimeZoneId(), abContactHBM.getInstantMessenger(), abContactHBM.getWebsite(), abContactHBM.getComments());
            ABContactPool.put(abContact.getPrimaryKey(), abContact);
        }
        return abContact;
    }

    public static ABContactHBMUtil getInstance() {
        return _instance;
    }

    public Comparable transform(Object obj) {
        return model((ABContactHBM) obj);
    }

    private static ABContactHBMUtil _instance = new ABContactHBMUtil();
}
