package org.dcm4chee.xero.search;

import java.util.HashMap;
import java.util.Map;
import org.dcm4chee.xero.metadata.filter.Filter;
import org.dcm4chee.xero.metadata.filter.MemoryCacheFilter;
import org.dcm4chee.xero.search.study.ResultsBean;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Contains utilies for calling the various search filters
 * @author bwallace
 */
public class SearchFilterUtils {

    private static final Logger log = LoggerFactory.getLogger(SearchFilterUtils.class);

    /** 
    * Calls the filter item to get the results for search for the given UID.  May use internal knowledge
    * about cached series level results to optimize this by not making actual calls to read the data.
    * If gspsUID is present, then this will cause the GSPS information to be embedded for the given image.
    * @param filterItem
    * @param params
    * @param uid
    * @param gspsUid
    * @return ResultsBean containing at least the given image bean, and GSPS information.
    */
    public static ResultsBean filterImage(Filter<ResultFromDicom> imageSource, Map<String, Object> params, String uid, String presentationUID) {
        Map<String, Object> newParams = new HashMap<String, Object>();
        StringBuffer queryStr = new StringBuffer("&object=").append(uid).append("&presentationUID=").append(presentationUID);
        newParams.put("objectUID", uid);
        newParams.put("presentationUID", presentationUID);
        String frame = (String) params.get("frameNumber");
        if (frame != null) {
            newParams.put("frameNumber", frame);
            queryStr.append("&frameNumber=" + frame);
        }
        newParams.put(MemoryCacheFilter.KEY_NAME, queryStr.toString());
        log.info("BurnIn Query str for filter image is " + queryStr);
        return (ResultsBean) imageSource.filter(null, newParams);
    }
}
