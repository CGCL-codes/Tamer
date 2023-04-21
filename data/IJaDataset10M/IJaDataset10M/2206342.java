package org.apache.solr.request;

import java.util.Map;

/**
 * This class is scheduled for deletion.  Please update your code to the moved package.
 *
 * @deprecated Use org.apache.solr.common.params.MapSolrParams
 */
@Deprecated
public class MapSolrParams extends org.apache.solr.common.params.MapSolrParams {

    public MapSolrParams(Map<String, String> map) {
        super(map);
    }
}
