package com.hp.hpl.jena.query.darq.engine.compiler;

import java.util.List;
import com.hp.hpl.jena.query.engine1.plan.PlanGroup;
import com.hp.hpl.jena.query.util.Context;

public class PlanGroupDarq extends PlanGroup {

    protected PlanGroupDarq(Context context, List subElts, boolean reorderable) {
        super(context, subElts, reorderable);
    }

    public static PlanGroup make(Context context, List acc, boolean reorderable) {
        return new PlanGroupDarq(context, acc, reorderable);
    }
}
