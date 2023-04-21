package com.sitescape.team.ical.util;

import javax.portlet.PortletRequest;
import com.sitescape.team.context.request.RequestContext;
import com.sitescape.team.context.request.RequestContextHolder;
import com.sitescape.team.module.mail.MailModule;
import com.sitescape.team.web.util.WebUrlUtil;

/**
 * iCalendar URLs util class. 
 * 
 * @author Pawel Nowicki
 *
 */
public class UrlUtil {

    /**
	 * Creates iCalendar URL for given entry or forum.
	 * 
	 * @param req
	 * @param binderId
	 * @param entryId
	 * @return
	 */
    public static String getICalURL(PortletRequest req, String binderId, String entryId) {
        if (binderId == null) {
            throw new IllegalArgumentException("'binderId' is required.");
        }
        RequestContext rc = RequestContextHolder.getRequestContext();
        StringBuffer url = new StringBuffer();
        url.append(WebUrlUtil.getIcalRootURL(req)).append("basic").append(MailModule.ICAL_FILE_EXTENSION).append("?bi=").append(binderId);
        if (entryId != null) {
            url.append("&entry=").append(entryId);
        }
        url.append("&ui=").append(rc.getUserId()).append("&pd=").append(rc.getUser().getPrivateDigest(binderId));
        return url.toString();
    }
}
