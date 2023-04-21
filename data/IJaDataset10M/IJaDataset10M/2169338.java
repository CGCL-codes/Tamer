package photospace.search;

import java.io.*;
import java.util.*;
import org.apache.commons.logging.*;
import org.apache.lucene.document.*;
import org.apache.lucene.index.*;
import org.apache.lucene.queryParser.*;
import org.apache.lucene.search.*;
import photospace.meta.*;

public class Searcher {

    private static final Log log = LogFactory.getLog(Searcher.class);

    public static final int ASCENDING = 1;

    public static final int DESCENDING = -1;

    public static final Sort CREATED_ASCENDING = new Sort(new SortField(DocumentFactory.SORT_FIELD, SortField.STRING));

    public static final Sort CREATED_DESCENDING = new Sort(new SortField(DocumentFactory.SORT_FIELD, SortField.STRING, true));

    public static final Query PHOTO_CONSTRAINT = new TermQuery(new Term(DocumentFactory.TYPE_FIELD, DocumentFactory.PHOTO_TYPE));

    public static final Query LOCATED_CONSTRAINT = new TermQuery(new Term(DocumentFactory.LOCATED_FIELD, "true"));

    private SearchIndex index;

    private DocumentFactory factory = new DocumentFactory();

    public Meta browse(String path, int sort, int start, int end) throws IOException {
        return browse(path, null, sort, start, end);
    }

    public Meta browse(String path, Query constraint, int sort, int start, int end) throws IOException {
        Document doc = findDocument(path);
        if (doc == null) return null;
        Meta meta = factory.createMeta(doc);
        assignParents(meta);
        if (!(meta instanceof CollectionMeta)) return meta;
        CollectionMeta collection = (CollectionMeta) meta;
        Sort order = (sort == ASCENDING) ? CREATED_ASCENDING : CREATED_DESCENDING;
        Query query = new TermQuery(new Term(DocumentFactory.PARENT_FIELD, collection.getPath()));
        if (constraint != null) {
            query = addConstraint(query, constraint);
        }
        SearchResult children = search(query, null, order, start, end);
        collection.addFiles(children.getFiles());
        collection.setStart(children.getStart());
        collection.setEnd(children.getEnd());
        collection.setTotal(children.getTotal());
        return collection;
    }

    private BooleanQuery addConstraint(Query query, Query constraint) {
        BooleanQuery constrained = new BooleanQuery();
        constrained.add(query, true, false);
        constrained.add(constraint, true, false);
        return constrained;
    }

    private void assignParents(Meta meta) throws IOException {
        if (meta.getParentPath() == null) return;
        CollectionMeta parent = (CollectionMeta) browse(meta.getParentPath(), DESCENDING, -1, -1);
        meta.setParent(parent);
    }

    /**
   * Retrieves the set of field names from the index
   */
    public Collection getFieldNames() throws IOException {
        IndexReader reader = index.getReader();
        List names = new ArrayList(reader.getFieldNames(true));
        Collections.sort(names);
        return names;
    }

    /**
   * Retrieves a collection of Matches for a specific Meta path
   */
    public Collection getDocumentMatches(String path) throws IOException {
        Collection matches = new ArrayList();
        Document doc = findDocument(path);
        if (doc == null) return matches;
        for (Enumeration fields = doc.fields(); fields.hasMoreElements(); ) {
            Field field = (Field) fields.nextElement();
            matches.addAll(getFieldMatches(field.name(), field.stringValue()));
        }
        return matches;
    }

    /**
   * Retrieves a collection of Matches for a specific Meta path and field
   */
    public Collection getDocumentMatches(String path, String field) throws IOException {
        Collection matches = new ArrayList();
        Document doc = findDocument(path);
        if (doc == null) return matches;
        String[] values = doc.getValues(field);
        if (values == null || values.length == 0 || "".equals(values[0])) return matches;
        for (int i = 0; i < values.length; i++) {
            matches.addAll(getFieldMatches(field, values[i]));
        }
        return matches;
    }

    private Document findDocument(String path) throws IOException {
        IndexReader reader = index.getReader();
        TermDocs docs = reader.termDocs(new Term("path", path));
        if (!docs.next()) {
            log.warn("No document found for path " + path);
            return null;
        }
        return reader.document(docs.doc());
    }

    public Collection getFieldMatches(String field) throws IOException {
        return getFieldMatches(field, "");
    }

    private Collection getFieldMatches(String field, String value) throws IOException {
        IndexReader reader = index.getReader();
        Collection values = new ArrayList();
        TermEnum terms = reader.terms(new Term(field, value));
        while (field.equals(terms.term().field()) && ("".equals(value) || value.equals(terms.term().text()))) {
            if (!"".equals(terms.term().text())) values.add(new Match(terms.term(), terms.docFreq()));
            if (!terms.next()) break;
        }
        return values;
    }

    public SearchResult search(String query, int sort, int start, int end) throws ParseException, IOException {
        return search(query, null, sort, start, end);
    }

    public SearchResult search(String query, Query constraint, int sort, int start, int end) throws ParseException, IOException {
        ParseResult search = parseQuery(query);
        if (search.filter != null && empty(search.query)) {
            search.query = new TermQuery(new Term(DocumentFactory.ALL_FIELD, DocumentFactory.ALL_VALUE));
        }
        if (constraint != null) {
            search.query = addConstraint(search.query, constraint);
        }
        Sort order = (sort == ASCENDING) ? CREATED_ASCENDING : CREATED_DESCENDING;
        SearchResult result = search(search.query, search.filter, order, start, end);
        result.setQuery(query);
        result.setName(query);
        result.setTitle("[" + query + "]");
        result.setDescription("Search results for: " + query);
        return result;
    }

    private ParseResult parseQuery(String queryString) throws ParseException {
        MetaAnalyzer analyzer = new MetaAnalyzer();
        Query query = QueryParser.parse(queryString, DocumentFactory.TEXT_FIELD, analyzer);
        ParseResult result = new ParseResult();
        result.query = query;
        if (analyzer.hasPoint()) {
            result.filter = new LocationFilter(analyzer.getLatitude(), analyzer.getLongitude(), analyzer.getRadius());
            if (empty(result.query)) result.query = LOCATED_CONSTRAINT; else result.query = addConstraint(result.query, LOCATED_CONSTRAINT);
        }
        return result;
    }

    protected SearchResult search(Query query) throws IOException {
        return search(query, null, Searcher.CREATED_DESCENDING);
    }

    private boolean empty(Query query) {
        return query instanceof BooleanQuery && ((BooleanQuery) query).getClauses().length == 0;
    }

    protected SearchResult search(Query query, Filter filter, Sort sort) throws IOException {
        return search(query, filter, sort, 0, -1);
    }

    protected SearchResult search(Query query, Filter filter, Sort sort, int start, int end) throws IOException {
        if (start < -1) throw new IllegalArgumentException("Start value of " + start + " must be -1 or greater");
        if (end < -1) throw new IllegalArgumentException("End value of " + end + " must be be -1 or greater");
        log.debug("query=" + query + ", filter=" + filter + ", sort=" + sort);
        SearchResult result = new SearchResult();
        result.setQuery(query.toString());
        result.setName(query.toString());
        result.setTitle("[" + query.toString() + "]");
        result.setDescription("Search results for: " + query.toString());
        result.setCreated(new Date());
        IndexReader reader = index.getReader();
        IndexSearcher searcher = new IndexSearcher(reader);
        Hits hits = searcher.search(query, filter, sort);
        result.setStart(start);
        result.setEnd(end < start && start < 0 ? start : (end >= hits.length() || end < start ? hits.length() - 1 : end));
        result.setTotal(hits.length());
        if (end <= start && start < 0) return result;
        float threshold = 0.0f;
        for (int i = result.getStart() < 0 ? 0 : result.getStart(); i <= result.getEnd(); i++) {
            if (hits.score(i) < threshold) break;
            Meta meta = factory.createMeta(hits.doc(i));
            try {
                if (meta instanceof CollectionMeta) {
                    BooleanQuery b = new BooleanQuery();
                    b.add(new BooleanClause(new TermQuery(new Term(DocumentFactory.PARENT_FIELD, meta.getPath())), true, false));
                    b.add(new BooleanClause(PHOTO_CONSTRAINT, true, false));
                    SearchResult children = search(b, null, CREATED_ASCENDING, 0, 3);
                    CollectionMeta collection = (CollectionMeta) meta;
                    collection.setTotal(children.getTotal());
                    collection.setStart(children.getStart());
                    collection.setEnd(children.getEnd());
                    collection.addFiles(children.getFiles());
                }
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
            result.addFile(meta);
        }
        result.setDuration(new Date().getTime() - result.getCreated().getTime());
        return result;
    }

    public void setIndex(SearchIndex index) {
        this.index = index;
    }

    private static class ParseResult {

        Query query;

        Filter filter;
    }
}
