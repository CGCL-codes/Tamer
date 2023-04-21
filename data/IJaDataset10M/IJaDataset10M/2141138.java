package com.nusino.dql.query.resolver;

import com.nusino.dql.query.JPQLCause;
import com.nusino.dql.query.JPQLElement;
import java.util.List;

/**
 * Copy Right DynamicQL&copy; Nusino Technologies Inc.
 * If you are authorized to use this code. then you can modify code. However, the copy right marker does not allow to be removed. 
 * @author daping huang, dhuang05@gmail.com
 */
public class WhereCauseResolver extends CauseResolver {

    public JPQLCause resolve(String query, String queryLowercase) {
        JPQLCause eqlCause = new JPQLCause();
        eqlCause.setName(JPQLCause.CAUSE_where);
        query = query.trim();
        queryLowercase = queryLowercase.trim();
        query = query.substring(JPQLCause.CAUSE_where.length()).trim();
        queryLowercase = queryLowercase.substring(JPQLCause.CAUSE_where.length()).trim();
        List<JPQLElement> elementList = resolveExpression(query, queryLowercase);
        for (JPQLElement element : elementList) {
            eqlCause.addElement(element);
        }
        return eqlCause;
    }
}
