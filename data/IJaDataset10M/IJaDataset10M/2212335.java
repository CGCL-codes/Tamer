package com.nusino.dql.query.resolver;

import com.nusino.dql.query.JPQLCause;
import com.nusino.dql.query.JPQLElement;

/**
 * Copy Right DynamicQL&copy; Nusino Technologies Inc.
 * If you are authorized to use this code. then you can modify code. However, the copy right marker does not allow to be removed. 
 * @author daping huang, dhuang05@gmail.com
 */
public class SetCauseResolver extends CauseResolver {

    public static final String COMMAS = ",";

    public JPQLCause resolve(String query, String queryLowercase) {
        JPQLCause eqlCause = new JPQLCause();
        eqlCause.setName(JPQLCause.CAUSE_set);
        JPQLElement element = null;
        int count = 0;
        int index = queryLowercase.indexOf(JPQLCause.CAUSE_set) + JPQLCause.CAUSE_set.length();
        query = query.substring(index);
        queryLowercase = queryLowercase.substring(index);
        String[] valuePairs = query.split(COMMAS);
        for (String valuePair : valuePairs) {
            element = new JPQLElement();
            if (count > 0) {
                element.setLogicSymbol(COMMAS);
            }
            analyzeElementValue(element, valuePair);
            count++;
            eqlCause.addElement(element);
        }
        return eqlCause;
    }
}
