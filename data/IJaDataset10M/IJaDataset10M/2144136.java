package org.javacrawler.core.http.util;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import org.javacrawler.core.http.HttpDocument;
import org.javacrawler.core.http.Status;

/**
 *
 * @author luis
 */
public class CrawlerStatistics {

    private static int deep = 0;

    private static List<String> visited;

    public static HashMap<Status, Integer> getStatistics(HttpDocument doc) {
        HashMap<Status, Integer> statistics = new HashMap<Status, Integer>();
        statistics.put(Status.OK, 0);
        statistics.put(Status.BROKEN, 0);
        statistics.put(Status.SPAM, 0);
        statistics.put(Status.UNKNOWN, 0);
        visited = new LinkedList<String>();
        visited.add(doc.getUrl());
        getStatistics(doc.getLinks(), statistics);
        return statistics;
    }

    private static void getStatistics(HashMap<String, HttpDocument> links, HashMap<Status, Integer> statistics) {
        for (HttpDocument doc : links.values()) {
            if (visited.contains(doc.getUrl())) continue; else {
                visited.add(doc.getUrl());
            }
            switch(doc.getStatus()) {
                case OK:
                    Integer ok = statistics.get(Status.OK);
                    statistics.put(Status.OK, ++ok);
                    break;
                case BROKEN:
                    Integer broken = statistics.get(Status.BROKEN);
                    statistics.put(Status.BROKEN, ++broken);
                    break;
                case UNKNOWN:
                    Integer unknown = statistics.get(Status.UNKNOWN);
                    statistics.put(Status.UNKNOWN, ++unknown);
                    break;
                case SPAM:
                    Integer spam = statistics.get(Status.SPAM);
                    statistics.put(Status.SPAM, ++spam);
                    break;
            }
            if (doc.getStatus() != Status.NO_ANALIZE && !doc.getLinks().isEmpty()) {
                getStatistics(doc.getLinks(), statistics);
            }
        }
    }
}
