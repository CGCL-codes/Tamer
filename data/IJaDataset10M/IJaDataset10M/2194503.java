package org.apache.solr.search;

import org.apache.solr.common.params.SolrParams;
import org.apache.solr.common.util.NamedList;
import org.apache.solr.request.SolrQueryRequest;

/**
 * Parse Solr's variant of Lucene QueryParser syntax, including the
 * deprecated sort specification after the query.
 * <br>Example: <code>{!lucenePlusSort}myfield:foo +bar -baz;price asc</code>
 */
public class OldLuceneQParserPlugin extends QParserPlugin {

    public static String NAME = "lucenePlusSort";

    public void init(NamedList args) {
    }

    public QParser createParser(String qstr, SolrParams localParams, SolrParams params, SolrQueryRequest req) {
        return new OldLuceneQParser(qstr, localParams, params, req);
    }
}
