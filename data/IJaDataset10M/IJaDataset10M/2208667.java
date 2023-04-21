package com.liferay.portlet.messageboards.social;

import com.liferay.portal.kernel.util.StringPool;
import com.liferay.portal.theme.ThemeDisplay;
import com.liferay.portlet.messageboards.model.MBMessage;
import com.liferay.portlet.messageboards.service.MBMessageLocalServiceUtil;
import com.liferay.portlet.social.model.BaseSocialActivityInterpreter;
import com.liferay.portlet.social.model.SocialActivity;
import com.liferay.portlet.social.model.SocialActivityFeedEntry;

/**
 * <a href="MBActivityInterpreter.java.html"><b><i>View Source</i></b></a>
 *
 * @author Brian Wing Shun Chan
 *
 */
public class MBActivityInterpreter extends BaseSocialActivityInterpreter {

    public String[] getClassNames() {
        return _CLASS_NAMES;
    }

    protected SocialActivityFeedEntry doInterpret(SocialActivity activity, ThemeDisplay themeDisplay) throws Exception {
        String creatorUserName = getUserName(activity.getUserId(), themeDisplay);
        String receiverUserName = getUserName(activity.getReceiverUserId(), themeDisplay);
        int activityType = activity.getType();
        MBMessage message = MBMessageLocalServiceUtil.getMessage(activity.getClassPK());
        String link = themeDisplay.getURLPortal() + themeDisplay.getPathMain() + "/message_boards/find_message?messageId=" + activity.getClassPK();
        String title = StringPool.BLANK;
        if (activityType == MBActivityKeys.ADD_MESSAGE) {
            title = themeDisplay.translate("activity-message-boards-add-message", creatorUserName);
        } else if (activityType == MBActivityKeys.REPLY_MESSAGE) {
            title = themeDisplay.translate("activity-message-boards-reply-message", new Object[] { creatorUserName, receiverUserName });
        }
        StringBuilder sb = new StringBuilder();
        sb.append("<a href=\"");
        sb.append(link);
        sb.append("\">");
        sb.append(cleanContent(message.getSubject()));
        sb.append("</a><br />");
        sb.append(cleanContent(message.getBody()));
        String body = sb.toString();
        return new SocialActivityFeedEntry(link, title, body);
    }

    private static final String[] _CLASS_NAMES = new String[] { MBMessage.class.getName() };
}
