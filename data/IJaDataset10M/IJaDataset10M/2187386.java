package com.adserversoft.flexfuse.server.reporter;

import com.adserversoft.flexfuse.server.api.ApplicationConstants;
import com.adserversoft.flexfuse.server.api.reporter.EventRecord;
import java.util.HashSet;
import java.util.Set;

/**
 * Author: Vitaly Sazanovich
 * Email: Vitaly.Sazanovich@gmail.com
 */
public class KeyMaker {

    public Set<String> makeKeysForInsert(EventRecord er) {
        Set<String> keys = new HashSet();
        keys.add(er.sysId + ":" + ApplicationConstants.REPORTER_ENTITY_SYSTEM + ":" + er.sysId + ":" + ApplicationConstants.REPORTER_TIME_WHOLE);
        keys.add(er.sysId + ":" + ApplicationConstants.REPORTER_ENTITY_SYSTEM + ":" + er.sysId + ":" + ApplicationConstants.REPORTER_TIME_YEAR + ":" + er.getYear());
        keys.add(er.sysId + ":" + ApplicationConstants.REPORTER_ENTITY_SYSTEM + ":" + er.sysId + ":" + ApplicationConstants.REPORTER_TIME_QUARTER + ":" + er.getQuarter());
        keys.add(er.sysId + ":" + ApplicationConstants.REPORTER_ENTITY_SYSTEM + ":" + er.sysId + ":" + ApplicationConstants.REPORTER_TIME_MONTH + ":" + er.getMonth());
        keys.add(er.sysId + ":" + ApplicationConstants.REPORTER_ENTITY_SYSTEM + ":" + er.sysId + ":" + ApplicationConstants.REPORTER_TIME_WEEK + ":" + er.getWeek());
        keys.add(er.sysId + ":" + ApplicationConstants.REPORTER_ENTITY_SYSTEM + ":" + er.sysId + ":" + ApplicationConstants.REPORTER_TIME_DAY + ":" + er.getDay());
        keys.add(er.sysId + ":" + ApplicationConstants.REPORTER_ENTITY_SYSTEM + ":" + er.sysId + ":" + ApplicationConstants.REPORTER_TIME_HOUR + ":" + er.getHour());
        keys.add(er.sysId + ":" + ApplicationConstants.REPORTER_ENTITY_USER + ":" + er.advUserId + ":" + ApplicationConstants.REPORTER_TIME_WHOLE);
        keys.add(er.sysId + ":" + ApplicationConstants.REPORTER_ENTITY_USER + ":" + er.advUserId + ":" + ApplicationConstants.REPORTER_TIME_YEAR + ":" + er.getYear());
        keys.add(er.sysId + ":" + ApplicationConstants.REPORTER_ENTITY_USER + ":" + er.advUserId + ":" + ApplicationConstants.REPORTER_TIME_QUARTER + ":" + er.getQuarter());
        keys.add(er.sysId + ":" + ApplicationConstants.REPORTER_ENTITY_USER + ":" + er.advUserId + ":" + ApplicationConstants.REPORTER_TIME_MONTH + ":" + er.getMonth());
        keys.add(er.sysId + ":" + ApplicationConstants.REPORTER_ENTITY_USER + ":" + er.advUserId + ":" + ApplicationConstants.REPORTER_TIME_WEEK + ":" + er.getWeek());
        keys.add(er.sysId + ":" + ApplicationConstants.REPORTER_ENTITY_USER + ":" + er.advUserId + ":" + ApplicationConstants.REPORTER_TIME_DAY + ":" + er.getDay());
        keys.add(er.sysId + ":" + ApplicationConstants.REPORTER_ENTITY_USER + ":" + er.advUserId + ":" + ApplicationConstants.REPORTER_TIME_HOUR + ":" + er.getHour());
        keys.add(er.sysId + ":" + ApplicationConstants.REPORTER_ENTITY_USER + ":" + er.pubUserId + ":" + ApplicationConstants.REPORTER_TIME_WHOLE);
        keys.add(er.sysId + ":" + ApplicationConstants.REPORTER_ENTITY_USER + ":" + er.pubUserId + ":" + ApplicationConstants.REPORTER_TIME_YEAR + ":" + er.getYear());
        keys.add(er.sysId + ":" + ApplicationConstants.REPORTER_ENTITY_USER + ":" + er.pubUserId + ":" + ApplicationConstants.REPORTER_TIME_QUARTER + ":" + er.getQuarter());
        keys.add(er.sysId + ":" + ApplicationConstants.REPORTER_ENTITY_USER + ":" + er.pubUserId + ":" + ApplicationConstants.REPORTER_TIME_MONTH + ":" + er.getMonth());
        keys.add(er.sysId + ":" + ApplicationConstants.REPORTER_ENTITY_USER + ":" + er.pubUserId + ":" + ApplicationConstants.REPORTER_TIME_WEEK + ":" + er.getWeek());
        keys.add(er.sysId + ":" + ApplicationConstants.REPORTER_ENTITY_USER + ":" + er.pubUserId + ":" + ApplicationConstants.REPORTER_TIME_DAY + ":" + er.getDay());
        keys.add(er.sysId + ":" + ApplicationConstants.REPORTER_ENTITY_USER + ":" + er.pubUserId + ":" + ApplicationConstants.REPORTER_TIME_HOUR + ":" + er.getHour());
        keys.add(er.sysId + ":" + ApplicationConstants.REPORTER_ENTITY_CAMPAIGN + ":" + er.campaignId + ":" + ApplicationConstants.REPORTER_TIME_WHOLE);
        keys.add(er.sysId + ":" + ApplicationConstants.REPORTER_ENTITY_CAMPAIGN + ":" + er.campaignId + ":" + ApplicationConstants.REPORTER_TIME_YEAR + ":" + er.getYear());
        keys.add(er.sysId + ":" + ApplicationConstants.REPORTER_ENTITY_CAMPAIGN + ":" + er.campaignId + ":" + ApplicationConstants.REPORTER_TIME_QUARTER + ":" + er.getQuarter());
        keys.add(er.sysId + ":" + ApplicationConstants.REPORTER_ENTITY_CAMPAIGN + ":" + er.campaignId + ":" + ApplicationConstants.REPORTER_TIME_MONTH + ":" + er.getMonth());
        keys.add(er.sysId + ":" + ApplicationConstants.REPORTER_ENTITY_CAMPAIGN + ":" + er.campaignId + ":" + ApplicationConstants.REPORTER_TIME_WEEK + ":" + er.getWeek());
        keys.add(er.sysId + ":" + ApplicationConstants.REPORTER_ENTITY_CAMPAIGN + ":" + er.campaignId + ":" + ApplicationConstants.REPORTER_TIME_DAY + ":" + er.getDay());
        keys.add(er.sysId + ":" + ApplicationConstants.REPORTER_ENTITY_CAMPAIGN + ":" + er.campaignId + ":" + ApplicationConstants.REPORTER_TIME_HOUR + ":" + er.getHour());
        keys.add(er.sysId + ":" + ApplicationConstants.REPORTER_ENTITY_SITE + ":" + er.siteId + ":" + ApplicationConstants.REPORTER_TIME_WHOLE);
        keys.add(er.sysId + ":" + ApplicationConstants.REPORTER_ENTITY_SITE + ":" + er.siteId + ":" + ApplicationConstants.REPORTER_TIME_YEAR + ":" + er.getYear());
        keys.add(er.sysId + ":" + ApplicationConstants.REPORTER_ENTITY_SITE + ":" + er.siteId + ":" + ApplicationConstants.REPORTER_TIME_QUARTER + ":" + er.getQuarter());
        keys.add(er.sysId + ":" + ApplicationConstants.REPORTER_ENTITY_SITE + ":" + er.siteId + ":" + ApplicationConstants.REPORTER_TIME_MONTH + ":" + er.getMonth());
        keys.add(er.sysId + ":" + ApplicationConstants.REPORTER_ENTITY_SITE + ":" + er.siteId + ":" + ApplicationConstants.REPORTER_TIME_WEEK + ":" + er.getWeek());
        keys.add(er.sysId + ":" + ApplicationConstants.REPORTER_ENTITY_SITE + ":" + er.siteId + ":" + ApplicationConstants.REPORTER_TIME_DAY + ":" + er.getDay());
        keys.add(er.sysId + ":" + ApplicationConstants.REPORTER_ENTITY_SITE + ":" + er.siteId + ":" + ApplicationConstants.REPORTER_TIME_HOUR + ":" + er.getHour());
        keys.add(er.sysId + ":" + ApplicationConstants.REPORTER_ENTITY_BANNER + ":" + er.bannerId + ":" + ApplicationConstants.REPORTER_TIME_WHOLE);
        keys.add(er.sysId + ":" + ApplicationConstants.REPORTER_ENTITY_BANNER + ":" + er.bannerId + ":" + ApplicationConstants.REPORTER_TIME_YEAR + ":" + er.getYear());
        keys.add(er.sysId + ":" + ApplicationConstants.REPORTER_ENTITY_BANNER + ":" + er.bannerId + ":" + ApplicationConstants.REPORTER_TIME_QUARTER + ":" + er.getQuarter());
        keys.add(er.sysId + ":" + ApplicationConstants.REPORTER_ENTITY_BANNER + ":" + er.bannerId + ":" + ApplicationConstants.REPORTER_TIME_MONTH + ":" + er.getMonth());
        keys.add(er.sysId + ":" + ApplicationConstants.REPORTER_ENTITY_BANNER + ":" + er.bannerId + ":" + ApplicationConstants.REPORTER_TIME_WEEK + ":" + er.getWeek());
        keys.add(er.sysId + ":" + ApplicationConstants.REPORTER_ENTITY_BANNER + ":" + er.bannerId + ":" + ApplicationConstants.REPORTER_TIME_DAY + ":" + er.getDay());
        keys.add(er.sysId + ":" + ApplicationConstants.REPORTER_ENTITY_BANNER + ":" + er.bannerId + ":" + ApplicationConstants.REPORTER_TIME_HOUR + ":" + er.getHour());
        keys.add(er.sysId + ":" + ApplicationConstants.REPORTER_ENTITY_ADPLACE + ":" + er.adPlaceId + ":" + ApplicationConstants.REPORTER_TIME_WHOLE);
        keys.add(er.sysId + ":" + ApplicationConstants.REPORTER_ENTITY_ADPLACE + ":" + er.adPlaceId + ":" + ApplicationConstants.REPORTER_TIME_YEAR + ":" + er.getYear());
        keys.add(er.sysId + ":" + ApplicationConstants.REPORTER_ENTITY_ADPLACE + ":" + er.adPlaceId + ":" + ApplicationConstants.REPORTER_TIME_QUARTER + ":" + er.getQuarter());
        keys.add(er.sysId + ":" + ApplicationConstants.REPORTER_ENTITY_ADPLACE + ":" + er.adPlaceId + ":" + ApplicationConstants.REPORTER_TIME_MONTH + ":" + er.getMonth());
        keys.add(er.sysId + ":" + ApplicationConstants.REPORTER_ENTITY_ADPLACE + ":" + er.adPlaceId + ":" + ApplicationConstants.REPORTER_TIME_WEEK + ":" + er.getWeek());
        keys.add(er.sysId + ":" + ApplicationConstants.REPORTER_ENTITY_ADPLACE + ":" + er.adPlaceId + ":" + ApplicationConstants.REPORTER_TIME_DAY + ":" + er.getDay());
        keys.add(er.sysId + ":" + ApplicationConstants.REPORTER_ENTITY_ADPLACE + ":" + er.adPlaceId + ":" + ApplicationConstants.REPORTER_TIME_HOUR + ":" + er.getHour());
        keys.add(er.sysId + ":" + ApplicationConstants.REPORTER_ENTITY_ADPLACE + ":" + er.adPlaceId + ":" + ApplicationConstants.REPORTER_ENTITY_BANNER + ":" + er.bannerId + ":" + ApplicationConstants.REPORTER_TIME_WHOLE);
        keys.add(er.sysId + ":" + ApplicationConstants.REPORTER_ENTITY_ADPLACE + ":" + er.adPlaceId + ":" + ApplicationConstants.REPORTER_ENTITY_BANNER + ":" + er.bannerId + ":" + ApplicationConstants.REPORTER_TIME_YEAR + ":" + er.getYear());
        keys.add(er.sysId + ":" + ApplicationConstants.REPORTER_ENTITY_ADPLACE + ":" + er.adPlaceId + ":" + ApplicationConstants.REPORTER_ENTITY_BANNER + ":" + er.bannerId + ":" + ApplicationConstants.REPORTER_TIME_QUARTER + ":" + er.getQuarter());
        keys.add(er.sysId + ":" + ApplicationConstants.REPORTER_ENTITY_ADPLACE + ":" + er.adPlaceId + ":" + ApplicationConstants.REPORTER_ENTITY_BANNER + ":" + er.bannerId + ":" + ApplicationConstants.REPORTER_TIME_MONTH + ":" + er.getMonth());
        keys.add(er.sysId + ":" + ApplicationConstants.REPORTER_ENTITY_ADPLACE + ":" + er.adPlaceId + ":" + ApplicationConstants.REPORTER_ENTITY_BANNER + ":" + er.bannerId + ":" + ApplicationConstants.REPORTER_TIME_WEEK + ":" + er.getWeek());
        keys.add(er.sysId + ":" + ApplicationConstants.REPORTER_ENTITY_ADPLACE + ":" + er.adPlaceId + ":" + ApplicationConstants.REPORTER_ENTITY_BANNER + ":" + er.bannerId + ":" + ApplicationConstants.REPORTER_TIME_DAY + ":" + er.getDay());
        keys.add(er.sysId + ":" + ApplicationConstants.REPORTER_ENTITY_ADPLACE + ":" + er.adPlaceId + ":" + ApplicationConstants.REPORTER_ENTITY_BANNER + ":" + er.bannerId + ":" + ApplicationConstants.REPORTER_TIME_HOUR + ":" + er.getHour());
        return keys;
    }
}
