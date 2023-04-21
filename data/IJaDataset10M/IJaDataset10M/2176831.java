package org.apache.solr.servlet;

import org.apache.solr.request.SolrQueryRequestBase;
import org.apache.solr.request.ServletSolrParams;
import org.apache.solr.core.SolrCore;
import javax.servlet.http.HttpServletRequest;

/**
 * @version $Id$
 */
class SolrServletRequest extends SolrQueryRequestBase {

    public SolrServletRequest(SolrCore core, HttpServletRequest req) {
        super(core, new ServletSolrParams(req));
    }
}
