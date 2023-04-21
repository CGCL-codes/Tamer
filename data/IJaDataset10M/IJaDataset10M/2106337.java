package com.planetachewood.service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.compass.core.CompassCallback;
import org.compass.core.CompassHits;
import org.compass.core.CompassSession;
import org.compass.gps.CompassGps;
import org.compass.spring.CompassDaoSupport;
import org.springframework.transaction.annotation.Transactional;
import com.planetachewood.dao.StripDao;
import com.planetachewood.domain.Strip;

/**
 * @author <a href="mailto:mark.a.allen@gmail.com">Mark Allen</a>
 * @since 1.0
 * @since Dec 5, 2006
 * @version $Revision: 24 $ $Date: 2007-03-11 23:16:29 -0400 (Sun, 11 Mar 2007) $
 */
public class DefaultSearchService extends CompassDaoSupport implements SearchService {

    private StripDao stripDao;

    private CompassGps compassGps;

    private static final int PAGE_SIZE = 10;

    /**
	 * @see com.planetachewood.service.SearchService#reindex()
	 */
    public void reindex() {
        compassGps.index();
    }

    /**
	 * @see com.planetachewood.service.SearchService#countSearchResults(java.lang.String)
	 */
    public int countSearchResults(final String query) {
        return (Integer) getCompassTemplate().execute(new CompassCallback() {

            public Object doInCompass(CompassSession compassSession) {
                try {
                    return new Integer(getCompassTemplate().find(query).length());
                } catch (Throwable t) {
                    log.info("Could not execute query count for: " + query);
                    return 0;
                }
            }
        });
    }

    /**
	 * @see com.planetachewood.service.SearchService#searchStrips(java.lang.String, int)
	 */
    @Transactional(readOnly = true)
    @SuppressWarnings("unchecked")
    public StripSearchResults searchStrips(final String query, final int startIndex) {
        return (StripSearchResults) getCompassTemplate().execute(new CompassCallback() {

            public Object doInCompass(CompassSession compassSession) {
                Map<Integer, SearchResultData> resultData = new HashMap<Integer, SearchResultData>();
                List<Integer> stripIds = new ArrayList<Integer>();
                int totalResults = 0;
                try {
                    CompassHits hits = getCompassTemplate().find(query);
                    totalResults = hits.length();
                    int upperBound = startIndex + PAGE_SIZE > totalResults ? totalResults : startIndex + PAGE_SIZE;
                    log.debug("Number of hits: " + totalResults);
                    log.debug("Results from " + startIndex + " to " + upperBound);
                    for (int i = startIndex; i < upperBound; i++) {
                        Strip strip = (Strip) hits.data(i);
                        if (strip != null) {
                            stripIds.add(strip.getId());
                            resultData.put(strip.getId(), new SearchResultData(hits.highlighter(i).fragmentsWithSeparator("transcript"), hits.score(i) * 100));
                        }
                    }
                } catch (Throwable t) {
                    log.info("Could not execute query: " + t);
                    return new StripSearchResults(new ArrayList<Strip>(), resultData, 0);
                }
                List<Strip> unorderedStrips = totalResults > 0 ? stripDao.findByIds(stripIds) : new ArrayList<Strip>();
                Strip[] orderedStrips = new Strip[unorderedStrips.size()];
                for (Strip strip : unorderedStrips) {
                    int count = 0;
                    for (Integer stripId : stripIds) {
                        if (strip.getId().equals(stripId)) {
                            orderedStrips[count] = strip;
                            break;
                        }
                        count++;
                    }
                }
                return new StripSearchResults(Arrays.asList(orderedStrips), resultData, totalResults);
            }
        });
    }

    /**
	 * Sets the stripDao.
	 * 
	 * @param stripDao The stripDao to set.
	 */
    public void setStripDao(StripDao stripDao) {
        this.stripDao = stripDao;
    }

    /**
	 * Sets the compassGps.
	 * 
	 * @param compassGps The compassGps to set.
	 */
    public void setCompassGps(CompassGps compassGps) {
        this.compassGps = compassGps;
    }
}
