package com.redhat.gs.mrlogistics.scheduler.filters;

import com.redhat.gs.mrlogistics.data.DatePair;
import org.hibernate.criterion.Criterion;
import static org.hibernate.criterion.Restrictions.*;

public class TimeFilter {

    public static Criterion During(DatePair start, DatePair end) {
        return and(ge("Start", start.getDate()), le("End", end.getDate()));
    }

    public static Criterion Excluding(DatePair start, DatePair end) {
        return and(le("Start", start.getDate()), ge("End", end.getDate()));
    }

    public static Criterion DuringDuration(DatePair date) {
        return eq("date", date.getDate());
    }
}
