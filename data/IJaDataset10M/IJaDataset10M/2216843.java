package uk.ac.ebi.intact.core.persister.stats;

import uk.ac.ebi.intact.model.AnnotatedObject;
import uk.ac.ebi.intact.model.CvObject;
import uk.ac.ebi.intact.core.persister.stats.impl.CvObjectStatsUnit;
import uk.ac.ebi.intact.core.persister.stats.impl.AnnotatedObjectStatsUnit;

/**
 * Factory to create StatsUnits
 *
 * @author Bruno Aranda (baranda@ebi.ac.uk)
 * @version $Id$
 */
public class StatsUnitFactory {

    private StatsUnitFactory() {
    }

    public static StatsUnit createStatsUnit(AnnotatedObject<?, ?> ao) {
        if (ao instanceof CvObject) {
            return new CvObjectStatsUnit((CvObject) ao);
        } else {
            return new AnnotatedObjectStatsUnit(ao);
        }
    }
}
