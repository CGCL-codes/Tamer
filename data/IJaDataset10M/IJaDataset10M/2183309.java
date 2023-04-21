package org.powerfolder.workflow.query;

import java.util.ArrayList;

public class AndQC implements QueryContainer {

    private QueryContainer query1 = null;

    private QueryContainer query2 = null;

    private AndQC() {
    }

    public static final QueryContainer createQuery(QueryContainer inQuery1, QueryContainer inQuery2) {
        AndQC outValue = new AndQC();
        outValue.query1 = inQuery1;
        outValue.query2 = inQuery2;
        return outValue;
    }

    public QueryContainer getLeftQuery() {
        return this.query1;
    }

    public QueryContainer getRightQuery() {
        return this.query2;
    }
}
