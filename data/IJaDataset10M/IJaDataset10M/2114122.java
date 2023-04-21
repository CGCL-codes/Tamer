package au.org.tpac.portal.repository;

import java.util.Calendar;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Set;
import au.org.tpac.portal.domain.Dataset;
import au.org.tpac.portal.domain.DlpUser;
import au.org.tpac.portal.domain.PartyIndividual;
import au.org.tpac.portal.domain.PartyOrganisation;
import au.org.tpac.portal.domain.PathWatch;
import au.org.tpac.portal.domain.PathWatchEvent;

/**
 * The Interface DlpUserDao.
 */
public interface PathWatchEventDao {

    /**
	 * Record an event occured
	 * @param event
	 */
    public void saveEvent(PathWatchEvent event);

    /**
	 * List all events for a dataset during a particular period
	 * @param dataset
	 * @param periodStart
	 * @param periodEnd
	 * @return
	 */
    public List<PathWatchEvent> listPathEventsTriggered(int dataset, Calendar periodStart, Calendar periodEnd);

    /**
	 * List all events during a particular period
	 * @param periodStart
	 * @param periodEnd
	 * @return
	 */
    public List<PathWatchEvent> listPathEventsTriggered(Calendar periodStart, Calendar periodEnd);
}
