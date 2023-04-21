package org.dcm4chee.xero.search.study;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;
import org.dcm4che2.data.Tag;
import org.dcm4chee.xero.dicom.SOPClassUIDs;
import org.dcm4chee.xero.metadata.MetaData;
import org.dcm4chee.xero.metadata.filter.Filter;
import org.dcm4chee.xero.search.SearchCriteria;

/** A C-Find searcher for series level data.
 * Uses the private SOP classes to get all the available series level data, if these are supported.
 *
 * @author bwallace
 */
public class SeriesSearch extends StudySearch {

    static final String SERIES_SEARCH_LEVEL = "SERIES";

    public static final Integer[] SERIES_RETURN_KEYS = { Tag.Modality, Tag.SeriesNumber, Tag.SeriesInstanceUID, Tag.NumberOfSeriesRelatedInstances, Tag.Manufacturer, Tag.InstitutionName, Tag.PerformingPhysicianName, Tag.StationName, Tag.SeriesDate, Tag.SeriesTime, Tag.OperatorName, Tag.InstitutionalDepartmentName, Tag.ManufacturerModelName, Tag.RequestingPhysician, Tag.RequestedProcedureID, Tag.ScheduledProcedureStepID, Tag.PerformedProcedureStepStartDate, Tag.PerformedProcedureStepEndDate };

    protected static Set<Integer> returnKeys = new HashSet<Integer>(Arrays.asList(SERIES_RETURN_KEYS));

    static {
        returnKeys.addAll(StudySearch.returnKeys);
    }

    @Override
    protected String[] getCuids() {
        return SOPClassUIDs.CFindSeriesLevel;
    }

    @Override
    protected String getQueryLevel() {
        return SERIES_SEARCH_LEVEL;
    }

    @Override
    protected Set<Integer> getReturnKeys() {
        return SeriesSearch.returnKeys;
    }

    /**
	 * Set the filter that determines the search criteria to use for this query.
	 * 
	 * @param searchCondition
	 */
    @Override
    @MetaData(out = "${class:org.dcm4chee.xero.search.study.ImageSearchConditionParser}")
    public void setSearchParser(Filter<SearchCriteria> searchParser) {
        super.setSearchParser(searchParser);
    }
}
