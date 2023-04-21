package com.gcsf.books.search.filters;

import com.gcsf.books.search.SearchActivator;
import com.gcsf.books.search.filters.nondynamic.StaticFilter;
import com.gcsf.books.search.preferences.ISearchPreferencesConstants;

public class StaticFiltersController {

    private boolean isStaticFilteringActive = true;

    private static StaticFiltersController ourInstance = null;

    private StaticFilter[] staticFilters = null;

    private StaticFilter activeFilter;

    private StaticFiltersController() {
        initFilters();
    }

    public static StaticFiltersController getInstance() {
        if (null == ourInstance) {
            ourInstance = new StaticFiltersController();
        }
        return ourInstance;
    }

    private void initFilters() {
        staticFilters = new StaticFilter[10];
        StaticFilter filterAll = new StaticFilter("All", IStaticFiltersConstants.ALL_IMAGE_PATH, IStaticFiltersConstants.OVR_ALL_IMAGE_PATH, false, null);
        staticFilters[0] = filterAll;
        StaticFilter filterSep = new StaticFilter("", "", "", true, null);
        staticFilters[1] = filterSep;
        StaticFilter filterInCollection = new StaticFilter("In Collection", IStaticFiltersConstants.INCOLLECTION_IMAGE_PATH, IStaticFiltersConstants.OVR_INCOLLECTION_IMAGE_PATH, false, null);
        staticFilters[2] = filterInCollection;
        StaticFilter filterNotInCollection = new StaticFilter("Not In Collection", IStaticFiltersConstants.NOTINCOLLECTION_IMAGE_PATH, IStaticFiltersConstants.OVR_NOTINCOLLECTION_IMAGE_PATH, false, null);
        staticFilters[3] = filterNotInCollection;
        StaticFilter filterWanted = new StaticFilter("Wanted", IStaticFiltersConstants.WANTED_IMAGE_PATH, IStaticFiltersConstants.OVR_WANTED_IMAGE_PATH, false, null);
        staticFilters[4] = filterWanted;
        StaticFilter filterForSale = new StaticFilter("For Sale", IStaticFiltersConstants.FORSALE_IMAGE_PATH, IStaticFiltersConstants.OVR_FORSALE_IMAGE_PATH, false, null);
        staticFilters[5] = filterForSale;
        StaticFilter filterOnOrder = new StaticFilter("On Order", IStaticFiltersConstants.ONORDER_IMAGE_PATH, IStaticFiltersConstants.OVR_ONORDER_IMAGE_PATH, false, null);
        staticFilters[6] = filterOnOrder;
        staticFilters[7] = filterSep;
        StaticFilter filterInCollectionOrForSale = new StaticFilter("In collection or for sale", IStaticFiltersConstants.INCOLLECTIONORFORSALE_IMAGE_PATH, IStaticFiltersConstants.OVR_INCOLLECTIONORFORSALE_IMAGE_PATH, false, null);
        staticFilters[8] = filterInCollectionOrForSale;
        StaticFilter filterNotInCollectionWantedOrForSale = new StaticFilter("Not in collection, wanted or on order", IStaticFiltersConstants.NOTINCOLLECTIONWANTEDORONORDER_IMAGE_PATH, IStaticFiltersConstants.OVR_NOTINCOLLECTIONWANTEDORONORDER_IMAGE_PATH, false, null);
        staticFilters[9] = filterNotInCollectionWantedOrForSale;
        setActiveFilter(filterAll);
    }

    public boolean isActive() {
        return isStaticFilteringActive;
    }

    public StaticFilter[] getFilters() {
        return staticFilters;
    }

    public StaticFilter getActiveFilter() {
        return activeFilter;
    }

    public void setActiveFilter(StaticFilter aFilter) {
        this.activeFilter = aFilter;
        SearchActivator.getDefault().getPreferenceStore().setValue(ISearchPreferencesConstants.P_STATIC_FILTER, aFilter.getFilterLabel());
    }
}
