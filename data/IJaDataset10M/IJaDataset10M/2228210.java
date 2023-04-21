package org.fpdev.apps.admin.analysis;

import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

/**
 *
 * @author demory
 */
public class RecentTripQueries {

    private List<TripQuery> queries_;

    private int maxSize_;

    /** Creates a new instance of RecentTripQueries */
    public RecentTripQueries(int maxSize) {
        queries_ = new LinkedList<TripQuery>();
        maxSize_ = maxSize;
    }

    public void addQuery(String start, String end, int hour, int min, int ampm, int day) {
        queries_.add(0, new TripQuery(start, end, hour, min, ampm, day));
        while (queries_.size() > maxSize_) {
            queries_.remove(queries_.size() - 1);
        }
    }

    public void select(int i) {
    }

    public Iterator<String> getDescriptions() {
        List<String> descriptions = new LinkedList<String>();
        Iterator<TripQuery> queries = queries_.iterator();
        while (queries.hasNext()) {
            descriptions.add(queries.next().getDescription());
        }
        return descriptions.iterator();
    }

    public class TripQuery {

        private String start_, end_;

        private int hour_, min_, ampm_, day_;

        public TripQuery(String start, String end, int hour, int min, int ampm, int day) {
            start_ = start;
            end_ = end;
            hour_ = hour;
            min_ = min;
            ampm_ = ampm;
            day_ = day;
        }

        public String getDescription() {
            return start_ + " to " + end_;
        }
    }
}
