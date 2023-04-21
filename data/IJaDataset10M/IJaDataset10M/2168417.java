package com.redhat.gs.mrlogistics.reporting.filters;

import com.redhat.gs.mrlogistics.data.ActivityType;
import org.hibernate.criterion.Criterion;
import static org.hibernate.criterion.Restrictions.*;

public class ActivityFilter {

    public static Criterion Type(ActivityType type) {
        return eq("type", type.getType());
    }
}
