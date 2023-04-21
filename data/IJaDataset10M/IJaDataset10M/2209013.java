package org.apache.solr.request;

import javax.servlet.ServletRequest;
import org.apache.solr.common.params.MultiMapSolrParams;

/**
 * @version $Id$
 */
public class ServletSolrParams extends MultiMapSolrParams {

    public ServletSolrParams(ServletRequest req) {
        super(req.getParameterMap());
    }

    public String get(String name) {
        String[] arr = map.get(name);
        if (arr == null) return null;
        String s = arr[0];
        if (s.length() == 0) return null;
        return s;
    }
}
