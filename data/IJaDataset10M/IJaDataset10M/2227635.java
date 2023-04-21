package org.dbe.composer.wfengine.bpel.server.service.report;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import org.apache.log4j.Logger;
import org.dbe.composer.wfengine.SDLangException;
import org.dbe.composer.wfengine.WSDLangException;
import org.dbe.composer.wfengine.bpel.impl.list.ListResult;
import org.dbe.composer.wfengine.bpel.impl.list.ListingDetail;
import org.dbe.composer.wfengine.bpel.impl.list.ListingFilter;
import org.dbe.composer.wfengine.bpel.server.service.IServiceCache;
import org.dbe.composer.wfengine.bpel.server.service.ServiceKey;
import org.dbe.composer.wfengine.sdl.def.BPELExtendedSDLDef;
import org.dbe.composer.wfengine.wsdl.def.BPELExtendedWSDLDef;

/**
 * Produce a <code>ListResult</code> for the sdl or wsdl catalog display.
 */
public class InMemoryServiceCatalogListing {

    /** for deployment logging purposes */
    private static final Logger logger = Logger.getLogger(InMemoryServiceCatalogListing.class.getName());

    /** Sort by sdl or wsdl namespace. */
    private static ServiceSorter SORTER = new ServiceSorter();

    /**
     * Create the <code>ListResult</code>.
     * @param aFilter The filter (row start and num of rows) params.
     * @param aLocationHintsToWsdlKeys Used to pull wsdl from the cache.
     * @param aCache
     * @throws WSDLangException
     */
    public static ListResult extractWsdlListing(ListingFilter aFilter, Map aLocationHintsToWsdlKeys, IServiceCache aCache) throws WSDLangException {
        logger.debug("extractWsdlListing()");
        List results = new ArrayList();
        for (Iterator iter = aLocationHintsToWsdlKeys.entrySet().iterator(); iter.hasNext(); ) {
            Map.Entry entry = (Map.Entry) iter.next();
            String locationHint = (String) entry.getKey();
            ServiceKey wsdlKey = (ServiceKey) entry.getValue();
            BPELExtendedWSDLDef def = aCache.getWsdl(wsdlKey);
            results.add(new ListingDetail(locationHint, def.getTargetNamespace()));
        }
        sort(results);
        int totalResults = results.size();
        results = results.subList(aFilter.getListStart(), totalResults);
        if (aFilter.getMaxReturn() > 0 && aFilter.getMaxReturn() < results.size()) {
            results = results.subList(0, aFilter.getMaxReturn());
        }
        return new ListResult(totalResults, results, true);
    }

    /**
     * Create the <code>ListResult</code>.
     * @param aFilter The filter (row start and num of rows) params.
     * @param aLocationHintsToWsdlKeys Used to pull wsdl from the cache.
     * @param aCache
     * @throws SDLangException
     */
    public static ListResult extractSdlListing(ListingFilter aFilter, Map aLocationHintsToSdlKeys, IServiceCache aCache) throws SDLangException {
        logger.debug("extractSdlListing()");
        List results = new ArrayList();
        for (Iterator iter = aLocationHintsToSdlKeys.entrySet().iterator(); iter.hasNext(); ) {
            Map.Entry entry = (Map.Entry) iter.next();
            String locationHint = (String) entry.getKey();
            ServiceKey sdlKey = (ServiceKey) entry.getValue();
            BPELExtendedSDLDef def = aCache.getSdl(sdlKey);
            logger.info("adding sdlKey " + sdlKey);
            results.add(new ListingDetail(locationHint, def.getTargetNamespace()));
        }
        sort(results);
        int totalResults = results.size();
        results = results.subList(aFilter.getListStart(), totalResults);
        if (aFilter.getMaxReturn() > 0 && aFilter.getMaxReturn() < results.size()) {
            results = results.subList(0, aFilter.getMaxReturn());
        }
        return new ListResult(totalResults, results, true);
    }

    /**
     * Sort the wsdl by namespace.
     * @param aWsdlListingDetails
     */
    protected static void sort(List aListingDetails) {
        Collections.sort(aListingDetails, SORTER);
    }

    /**
     * Default sort order is namespace.
     */
    protected static class ServiceSorter implements Comparator {

        public int compare(Object aO1, Object aO2) {
            return ((ListingDetail) aO1).getNamespace().compareTo(((ListingDetail) aO2).getNamespace());
        }
    }
}
