package google;

import gate.ProcessingResource;
import gate.Resource;
import gate.creole.*;
import gate.gui.MainFrame;
import gate.corpora.*;
import gate.util.*;
import gate.*;
import java.util.*;
import com.google.soap.search.*;

public class GooglePR extends AbstractLanguageAnalyser implements ProcessingResource {

    private String query = null;

    private int limit = -1;

    private String key = null;

    private Corpus google = null;

    private Boolean corpusAppendMode;

    private static final boolean DEBUG = false;

    private ArrayList pagesToExclude;

    /** Constructor of the class*/
    public GooglePR() {
    }

    /** Initialise this resource, and return it. */
    public Resource init() throws ResourceInstantiationException {
        return super.init();
    }

    /**
	 * Reinitialises the processing resource. After calling this method the
	 * resource should be in the state it is after calling init.
	 * If the resource depends on external resources (such as rules files) then
	 * the resource will re-read those resources. If the data used to create
	 * the resource has changed since the resource has been created then the
	 * resource will change too after calling reInit().
	 */
    public void reInit() throws ResourceInstantiationException {
        init();
    }

    /**
	 * This method runs the coreferencer. It assumes that all the needed parameters
	 * are set. If they are not, an exception will be fired.
	 */
    public void execute() throws ExecutionException {
        if (google == null) {
            throw new ExecutionException("Corpus to store results in is not provided");
        }
        if (query == null) {
            throw new ExecutionException("Query is not initialized");
        }
        if (limit == -1) {
            throw new ExecutionException("Limit is not initialized");
        }
        if (key == null) {
            throw new ExecutionException("Key is not initialized");
        }
        if (!corpusAppendMode.booleanValue()) {
            while (google.size() > 0) {
                Resource resource = (Resource) google.get(0);
                google.remove(0);
                Factory.deleteResource(resource);
            }
        }
        GoogleSearch search = new GoogleSearch();
        search.setKey(key);
        search.setQueryString(query);
        int index = 0;
        try {
            while (index < limit) {
                search.setStartResult(index);
                if (limit - index < 10) {
                    search.setMaxResults(limit - index);
                }
                GoogleSearchResult results = search.doSearch();
                GoogleSearchResultElement[] rs = new GoogleSearchResultElement[limit];
                rs = results.getResultElements();
                if (rs != null) {
                    for (int i = 0; i < rs.length; i++) {
                        GoogleSearchResultElement rElement = rs[i];
                        if (DEBUG) Err.println(index + i + ") " + rElement.getURL());
                        String urlString = rElement.getURL();
                        if (pagesToExclude != null && pagesToExclude.contains(urlString)) {
                            continue;
                        }
                        String docName = rElement.getURL() + "_" + Gate.genSym();
                        FeatureMap params = Factory.newFeatureMap();
                        params.put(Document.DOCUMENT_URL_PARAMETER_NAME, rElement.getURL());
                        try {
                            Document doc = (Document) Factory.createResource(DocumentImpl.class.getName(), params, null, docName);
                            google.add(doc);
                        } catch (ResourceInstantiationException e) {
                            String nl = Strings.getNl();
                            Err.prln("WARNING: could not intantiate document :" + e.getMessage());
                        }
                    }
                }
                index += 10;
            }
        } catch (Exception gsf) {
            Err.println("Google Search Fault: " + gsf.getMessage());
        }
    }

    public void setQuery(String query) {
        this.query = query;
    }

    public String getQuery() {
        return this.query;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getKey() {
        return this.key;
    }

    public void setLimit(Integer limit) {
        this.limit = limit.intValue();
    }

    public Integer getLimit() {
        return new Integer(this.limit);
    }

    public Corpus getCorpus() {
        return google;
    }

    public void setCorpus(Corpus corpus) {
        this.google = corpus;
    }

    public void setCorpusAppendMode(Boolean appendMode) {
        this.corpusAppendMode = appendMode;
    }

    public Boolean getCorpusAppendMode() {
        return this.corpusAppendMode;
    }

    public void setPagesToExclude(List pagesToExclude) {
        this.pagesToExclude = new ArrayList();
        if (pagesToExclude == null) return;
        Iterator iterator = pagesToExclude.iterator();
        while (iterator.hasNext()) {
            String page = (String) iterator.next();
            page = page.toLowerCase();
            this.pagesToExclude.add(page);
        }
    }

    public List getPagesToExclude() {
        return this.pagesToExclude;
    }
}
