package com.liferay.portlet.calendar.social;

import com.liferay.portal.kernel.util.StringPool;
import com.liferay.portal.theme.ThemeDisplay;
import com.liferay.portlet.calendar.model.CalEvent;
import com.liferay.portlet.calendar.service.CalEventLocalServiceUtil;
import com.liferay.portlet.social.model.BaseSocialActivityInterpreter;
import com.liferay.portlet.social.model.SocialActivity;
import com.liferay.portlet.social.model.SocialActivityFeedEntry;

/**
 * <a href="CalendarActivityInterpreter.java.html"><b><i>View Source</i></b></a>
 *
 * @author Brian Wing Shun Chan
 *
 */
public class CalendarActivityInterpreter extends BaseSocialActivityInterpreter {

    public String[] getClassNames() {
        return _CLASS_NAMES;
    }

    protected SocialActivityFeedEntry doInterpret(SocialActivity activity, ThemeDisplay themeDisplay) throws Exception {
        String creatorUserName = getUserName(activity.getUserId(), themeDisplay);
        int activityType = activity.getType();
        CalEvent event = CalEventLocalServiceUtil.getEvent(activity.getClassPK());
        String link = themeDisplay.getURLPortal() + themeDisplay.getPathMain() + "/calendar/find_event?eventId=" + activity.getClassPK();
        String title = StringPool.BLANK;
        if (activityType == CalendarActivityKeys.ADD_EVENT) {
            title = themeDisplay.translate("activity-calendar-add-event", creatorUserName);
        } else if (activityType == CalendarActivityKeys.UPDATE_EVENT) {
            title = themeDisplay.translate("activity-calendar-update-event", creatorUserName);
        }
        StringBuilder sb = new StringBuilder();
        sb.append("<a href=\"");
        sb.append(link);
        sb.append("\">");
        sb.append(cleanContent(event.getTitle()));
        sb.append("</a><br />");
        sb.append(cleanContent(event.getDescription()));
        String body = sb.toString();
        return new SocialActivityFeedEntry(link, title, body);
    }

    private static final String[] _CLASS_NAMES = new String[] { CalEvent.class.getName() };
}
