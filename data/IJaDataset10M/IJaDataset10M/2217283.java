package mil.army.logsa.weeklystatus;

import java.text.DateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Map.Entry;
import com.atlassian.confluence.labels.Label;
import com.atlassian.confluence.pages.BlogPost;
import com.atlassian.confluence.user.UserAccessor;
import com.atlassian.core.util.DataUtils;
import com.atlassian.core.util.DateUtils;
import com.atlassian.user.User;
import com.ctc.wstx.util.DataUtil;

public class ReportContentBuilder {

    private final List<BlogPost> posts;

    private final UserAccessor userAccessor;

    public ReportContentBuilder(final List<BlogPost> posts, final UserAccessor userAccessor) {
        this.posts = Collections.unmodifiableList(posts);
        this.userAccessor = userAccessor;
    }

    public String getContent() {
        List<BlogPost> safetyMinutus = new ArrayList<BlogPost>();
        String content = "h2. Weekly Statuses\n\n" + "\n";
        for (Entry<String, List<BlogPost>> entry : groupByUser().entrySet()) {
            User user = userAccessor.getUser(entry.getKey());
            content += "h4. [" + user.getFullName() + "|~" + user.getName() + "]\n";
            for (BlogPost post : entry.getValue()) {
                if (post.getLabels().contains(new Label("safety"))) {
                    safetyMinutus.add(post);
                    continue;
                }
                content += "* [" + post.getTitle() + "|" + post.getSpaceKey() + ":" + post.getLinkPart() + "]\n";
            }
            content += "\n";
        }
        content += "h2. Safety Minuetes\n\n";
        for (BlogPost post : safetyMinutus) {
            User user = userAccessor.getUser(post.getCreatorName());
            content += "h4. [" + user.getFullName() + "|~" + user.getName() + "]\n";
            content += "* [" + post.getTitle() + "|" + post.getSpaceKey() + ":" + post.getLinkPart() + "]\n";
            content += post.getContent() + "\n\n";
        }
        return content;
    }

    protected static String getCurrentWeekRange() {
        Calendar calSat = Calendar.getInstance();
        calSat.setTime(getCurrentSunday());
        String satString = calSat.getDisplayName(Calendar.MONTH, Calendar.LONG, Locale.ENGLISH);
        satString += " " + calSat.get(Calendar.DAY_OF_MONTH);
        satString += ", " + calSat.get(Calendar.YEAR);
        Calendar calSun = Calendar.getInstance();
        calSun.setTime(getCurrentSaturday());
        String sunString = calSun.getDisplayName(Calendar.MONTH, Calendar.LONG, Locale.ENGLISH);
        sunString += " " + calSun.get(Calendar.DAY_OF_MONTH);
        sunString += ", " + calSun.get(Calendar.YEAR);
        return satString + " - " + sunString;
    }

    protected static Date getCurrentSunday() {
        Date sunday = null;
        Calendar rightNow = Calendar.getInstance();
        int day = rightNow.get(Calendar.DAY_OF_WEEK);
        int distance = 0;
        if (day == Calendar.SUNDAY) sunday = rightNow.getTime(); else {
            distance = day - Calendar.SUNDAY;
            if (distance == -1) distance = 6;
            sunday = (Date) (rightNow.getTime());
            sunday.setTime(sunday.getTime() - 1000 * 60 * 60 * 24 * (distance));
        }
        return sunday;
    }

    protected static Date getCurrentSaturday() {
        Date saturday = null;
        Calendar rightNow = Calendar.getInstance();
        int day = rightNow.get(Calendar.DAY_OF_WEEK);
        int distance = 0;
        if (day == Calendar.SATURDAY) saturday = rightNow.getTime(); else {
            distance = day - Calendar.SATURDAY;
            if (distance == -1) distance = 6;
            saturday = (Date) (rightNow.getTime());
            saturday.setTime(saturday.getTime() - 1000 * 60 * 60 * 24 * (distance));
        }
        return saturday;
    }

    private Map<String, List<BlogPost>> groupByUser() {
        Map<String, List<BlogPost>> map = new HashMap<String, List<BlogPost>>();
        for (BlogPost post : posts) {
            List<BlogPost> sorted = map.get(post.getCreatorName());
            if (sorted == null) {
                sorted = new ArrayList<BlogPost>();
                map.put(post.getCreatorName(), sorted);
            }
            sorted.add(post);
        }
        return map;
    }
}
