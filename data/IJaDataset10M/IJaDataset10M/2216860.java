package org.apache.solr.handler.component;

import org.apache.lucene.index.Term;
import org.apache.lucene.index.TermEnum;
import org.apache.lucene.util.StringHelper;
import org.apache.solr.common.SolrException;
import org.apache.solr.common.params.CommonParams;
import org.apache.solr.common.params.SolrParams;
import org.apache.solr.common.params.TermsParams;
import org.apache.solr.common.util.NamedList;
import org.apache.solr.schema.FieldType;
import org.apache.solr.schema.StrField;
import org.apache.solr.request.SimpleFacets.CountPair;
import org.apache.solr.util.BoundedTreeSet;
import java.io.IOException;

/**
 * Return TermEnum information, useful for things like auto suggest.
 *
 * @see org.apache.solr.common.params.TermsParams
 *      See Lucene's TermEnum class
 */
public class TermsComponent extends SearchComponent {

    public static final int UNLIMITED_MAX_COUNT = -1;

    public void process(ResponseBuilder rb) throws IOException {
        SolrParams params = rb.req.getParams();
        if (params.getBool(TermsParams.TERMS, false)) {
            String lowerStr = params.get(TermsParams.TERMS_LOWER, null);
            String[] fields = params.getParams(TermsParams.TERMS_FIELD);
            if (fields != null && fields.length > 0) {
                NamedList terms = new NamedList();
                rb.rsp.add("terms", terms);
                int limit = params.getInt(TermsParams.TERMS_LIMIT, 10);
                if (limit < 0) {
                    limit = Integer.MAX_VALUE;
                }
                String upperStr = params.get(TermsParams.TERMS_UPPER);
                boolean upperIncl = params.getBool(TermsParams.TERMS_UPPER_INCLUSIVE, false);
                boolean lowerIncl = params.getBool(TermsParams.TERMS_LOWER_INCLUSIVE, true);
                boolean sort = !TermsParams.TERMS_SORT_INDEX.equals(params.get(TermsParams.TERMS_SORT, TermsParams.TERMS_SORT_COUNT));
                int freqmin = params.getInt(TermsParams.TERMS_MINCOUNT, 1);
                int freqmax = params.getInt(TermsParams.TERMS_MAXCOUNT, UNLIMITED_MAX_COUNT);
                if (freqmax < 0) {
                    freqmax = Integer.MAX_VALUE;
                }
                String prefix = params.get(TermsParams.TERMS_PREFIX_STR);
                boolean raw = params.getBool(TermsParams.TERMS_RAW, false);
                for (int j = 0; j < fields.length; j++) {
                    String field = StringHelper.intern(fields[j]);
                    FieldType ft = raw ? null : rb.req.getSchema().getFieldTypeNoEx(field);
                    if (ft == null) ft = new StrField();
                    String lower = lowerStr == null ? prefix : (raw ? lowerStr : ft.toInternal(lowerStr));
                    if (lower == null) lower = "";
                    String upper = upperStr == null ? null : (raw ? upperStr : ft.toInternal(upperStr));
                    Term lowerTerm = new Term(field, lower);
                    Term upperTerm = upper == null ? null : new Term(field, upper);
                    TermEnum termEnum = rb.req.getSearcher().getReader().terms(lowerTerm);
                    int i = 0;
                    BoundedTreeSet<CountPair<String, Integer>> queue = (sort ? new BoundedTreeSet<CountPair<String, Integer>>(limit) : null);
                    NamedList fieldTerms = new NamedList();
                    terms.add(field, fieldTerms);
                    Term lowerTestTerm = termEnum.term();
                    if (lowerTestTerm != null && lowerIncl == false && lowerTestTerm.field() == field && lowerTestTerm.text().equals(lower)) {
                        termEnum.next();
                    }
                    while (i < limit || sort) {
                        Term theTerm = termEnum.term();
                        if (theTerm == null || field != theTerm.field()) break;
                        String indexedText = theTerm.text();
                        if (prefix != null && !indexedText.startsWith(prefix)) break;
                        if (upperTerm != null) {
                            int upperCmp = theTerm.compareTo(upperTerm);
                            if (upperCmp > 0 || (upperCmp == 0 && !upperIncl)) break;
                        }
                        int docFreq = termEnum.docFreq();
                        if (docFreq >= freqmin && docFreq <= freqmax) {
                            String label = raw ? indexedText : ft.indexedToReadable(indexedText);
                            if (sort) {
                                queue.add(new CountPair<String, Integer>(label, docFreq));
                            } else {
                                fieldTerms.add(label, docFreq);
                                i++;
                            }
                        }
                        termEnum.next();
                    }
                    termEnum.close();
                    if (sort) {
                        for (CountPair<String, Integer> item : queue) {
                            if (i < limit) {
                                fieldTerms.add(item.key, item.val);
                                i++;
                            } else {
                                break;
                            }
                        }
                    }
                }
            } else {
                throw new SolrException(SolrException.ErrorCode.BAD_REQUEST, "No terms.fl parameter specified");
            }
        }
    }

    public void prepare(ResponseBuilder rb) throws IOException {
    }

    public String getVersion() {
        return "$Revision: 807289 $";
    }

    public String getSourceId() {
        return "$Id: TermsComponent.java 807289 2009-08-24 15:56:32Z yonik $";
    }

    public String getSource() {
        return "$URL: https://svn.apache.org/repos/asf/lucene/solr/branches/branch-1.4/src/java/org/apache/solr/handler/component/TermsComponent.java $";
    }

    public String getDescription() {
        return "A Component for working with Term Enumerators";
    }
}
