package org.usca.workshift.database.util;

import org.hibernate.CacheMode;
import org.hibernate.Criteria;
import org.hibernate.FlushMode;
import org.hibernate.criterion.Restrictions;
import org.hibernate.transform.DistinctRootEntityResultTransformer;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import java.util.Arrays;
import java.util.List;

/**
 * User: dannyant
 * Date: Oct 25, 2007
 * Time: 6:56:30 PM
 */
public class Searcher {

    private static final Log log = LogFactory.getLog(Searcher.class);

    public static void startIndex() {
    }

    public static void main(String... args) {
        startIndex();
    }
}
