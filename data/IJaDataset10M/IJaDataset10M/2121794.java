package org.restlet.ext.solr.internal;

import java.util.ArrayList;
import org.apache.solr.common.util.ContentStream;
import org.apache.solr.core.SolrCore;
import org.apache.solr.request.SolrQueryRequestBase;
import org.restlet.Request;

/**
 * Solr query request wrapping a Restlet request.
 * 
 * @author Remi Dewitte <remi@gide.net>
 */
public class SolrRestletQueryRequest extends SolrQueryRequestBase {

    /**
     * Constructor.
     * 
     * @param request
     *            The Restlet request to wrap.
     * @param core
     *            The Solr core.
     */
    public SolrRestletQueryRequest(Request request, SolrCore core) {
        super(core, new SolrRestletParams(request));
        getContext().put("path", request.getResourceRef().getPath());
        ArrayList<ContentStream> _streams = new ArrayList<ContentStream>(1);
        _streams.add(new SolrRepresentationContentStream(request.getEntity()));
        setContentStreams(_streams);
    }
}
