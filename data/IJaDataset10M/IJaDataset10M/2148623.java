package net.jforum.view.forum.common;

import java.util.Date;
import net.jforum.JForumExecutionContext;
import net.jforum.context.RequestContext;
import net.jforum.entities.Poll;
import net.jforum.entities.PollOption;

/**
 * @author David Almilli
 * @version $Id: PollCommon.java,v 1.10 2007/09/18 16:47:45 rafaelsteil Exp $
 */
public class PollCommon {

    private PollCommon() {
    }

    public static Poll fillPollFromRequest() {
        RequestContext request = JForumExecutionContext.getRequest();
        String label = request.getParameter("poll_label");
        if (label == null || label.length() == 0) {
            return null;
        }
        Poll poll = new Poll();
        poll.setStartTime(new Date());
        poll.setLabel(label);
        int count = request.getIntParameter("poll_option_count");
        for (int i = 0; i <= count; i++) {
            String option = request.getParameter("poll_option_" + i);
            if (option == null) {
                continue;
            }
            option = option.trim();
            if (option.length() > 0) {
                PollOption po = new PollOption();
                po.setId(i);
                po.setText(option);
                poll.addOption(po);
            }
        }
        String pollLength = request.getParameter("poll_length");
        if (pollLength != null && pollLength.length() > 0) {
            poll.setLength(Integer.parseInt(pollLength));
        }
        return poll;
    }
}
