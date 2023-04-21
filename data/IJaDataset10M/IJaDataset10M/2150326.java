package au.edu.educationau.opensource.dsm.adapters;

import java.io.Reader;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Enumeration;
import au.edu.educationau.opensource.dsm.DeusExMachina;
import au.edu.educationau.opensource.dsm.adapters.contenthandler.DSMContentHandler;
import au.edu.educationau.opensource.dsm.cache.DiskCacheEntry;
import au.edu.educationau.opensource.dsm.obj.SearchCriteria;
import au.edu.educationau.opensource.dsm.worker.ResultSetUnifier;

/**
 * This adapter is used to search other DSM instances. This provides a very
 * powerful way searching clusters of DSMs
 */
public class DSMSearchAdapterImpl extends SearchAdapter {

    private int startValue = 1;

    private String token = "";

    /** Parent constructor */
    public DSMSearchAdapterImpl() {
        super();
    }

    /**
	 * See the SearchAdapter abstract method doPrime
	 * 
	 * @exception Throwable
	 */
    public void doPrime() throws Throwable {
        SearchCriteria criteria = super.getSearchCriteria();
        addToURLBuffer("q=");
        addToURLBuffer(URLEncoder.encode(criteria.getQuery(), "UTF-8"));
        addToURLBuffer("&cs=");
        addToURLBuffer(criteria.isCaseSensitive());
        addToURLBuffer("&items=");
        addToURLBuffer(super.getProperties().getBatchSize());
        addToURLBuffer("&kc=");
        addToURLBuffer(criteria.getKeywordConstraint());
        addToURLBuffer("&start=");
        addToURLBuffer(startValue);
        addToURLBuffer("&mr=");
        addToURLBuffer(super.getSearchCriteria().getMaxResults());
        if (token.length() > 0) {
            addToURLBuffer("&token=");
            addToURLBuffer(token);
        }
        if (criteria.hasCustomParams()) {
            String param = "";
            String adapterName = super.getProperties().getAdapterCode() + ".";
            for (Enumeration params = criteria.getCustomParams(); params.hasMoreElements(); ) {
                param = (String) params.nextElement();
                if (param.startsWith(adapterName) && param.length() > adapterName.length()) {
                    addToURLBuffer("&");
                    addToURLBuffer(param.substring(adapterName.length()));
                    addToURLBuffer("=");
                    addToURLBuffer(criteria.getCustomParamValue(param));
                }
            }
        }
    }

    /**
	 * See the SearchAdapter abstract method doPerform
	 * 
	 * @exception Throwable
	 */
    public ArrayList doPerform() throws Throwable {
        DSMContentHandler cHandler = new DSMContentHandler();
        cHandler.setSearchAdapterProperties(super.getProperties());
        DiskCacheEntry entry = DeusExMachina._DiskCache().getEntry(super.getAugmentedSearchURL());
        if (null != entry) {
            Reader reader = entry.getReader();
            if (null != reader) {
                cHandler = (DSMContentHandler) ResultSetUnifier.processXML(reader, cHandler);
            }
        }
        ArrayList batch = cHandler.getResults();
        super.setTheoreticalAvailableResults(cHandler.getFound());
        return batch;
    }

    /**
	 * See the SearchAdapter abstract method doNextBatch
	 * 
	 * @exception Throwable
	 */
    public boolean doNextBatch() throws Throwable {
        if (super.getTheoreticalAvailableResults() > super.getProperties().getBatchSize() && super.getProperties().getBatchSize() < super.getSearchCriteria().getMaxResults() && super.getTotalFound() < super.getSearchCriteria().getMaxResults()) {
            startValue = startValue + super.getProperties().getBatchSize();
            return startValue <= 200;
        } else {
            return false;
        }
    }

    /**
	 * See the SearchAdapter abstract method doStopNice
	 * 
	 * @exception Throwable
	 */
    public void doStopNice() throws Throwable {
        token = null;
    }

    /**
	 * See the SearchAdapter abstract method doStopForce
	 * 
	 * @exception Throwable
	 */
    public void doStopForce() throws Throwable {
        token = null;
    }

    /** Class display string */
    public String toString() {
        return "o.m.d.a.DSMSearchAdapterImpl";
    }
}
