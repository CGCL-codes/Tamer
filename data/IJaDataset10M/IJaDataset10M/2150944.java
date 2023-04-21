package jtweet.hack;

import java.util.ArrayList;
import java.util.List;
import twitter4j.Status;

public class StatusHelper {

    public static List<JStatus> parseStatus(List<Status> status) {
        List<JStatus> jStatus = new ArrayList<JStatus>(status.size());
        for (Status s : status) {
            JStatus js = new JStatus(s);
            jStatus.add(js);
        }
        return jStatus;
    }
}
