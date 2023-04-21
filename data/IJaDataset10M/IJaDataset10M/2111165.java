package ca.ubc.jquery.api.tyruba;

import java.util.ArrayList;
import java.util.List;
import ca.ubc.jquery.api.JQuery;
import ca.ubc.jquery.api.JQueryAPI;
import ca.ubc.jquery.api.JQueryException;
import ca.ubc.jquery.api.JQueryResult;
import ca.ubc.jquery.api.JQueryResultSet;
import ca.ubc.jquery.api.JQueryUpdateTarget;

public class TyRuBaUpdateTarget extends JQueryUpdateTarget {

    protected TyRuBaUpdateTarget(String name) {
        super(name);
    }

    public Filter[] getFilters() throws JQueryException {
        List names = new ArrayList();
        List queries = new ArrayList();
        JQuery q = JQueryAPI.createQuery("updateTargetFilter(?X,?Y)");
        JQueryResultSet rs = q.execute();
        try {
            while (rs.hasNext()) {
                JQueryResult r = rs.next();
                names.add((String) r.get("?X"));
                queries.add((String) r.get("?Y"));
            }
            Filter[] result = new Filter[names.size()];
            for (int i = 0; i < names.size(); i++) {
                result[i] = createFilter((String) names.get(i), (String) queries.get(i));
            }
            return result;
        } finally {
            if (rs != null) {
                rs.close();
            }
        }
    }
}
