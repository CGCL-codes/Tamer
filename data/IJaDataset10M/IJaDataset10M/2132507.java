package nl.headspring.photoz.imagecollection.fs;

import nl.headspring.photoz.common.eventbus.BusEvent;
import nl.headspring.photoz.common.eventbus.Subscriber;
import nl.headspring.photoz.imagecollection.Annotation;
import nl.headspring.photoz.imagecollection.ImageCollection;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

/**
 * Class TimeIndexer.
 *
 * @author Eelco Sommer
 * @since Sep 21, 2010
 */
public class TimeIndexer implements Runnable, Subscriber {

    private static final Log LOG = LogFactory.getLog(TimeIndexer.class);

    private final ImageCollection imageCollection;

    private final Map<String, List<String>> timeToUniqueIdIndex;

    private boolean keepWorking;

    public TimeIndexer(ImageCollection imageCollection, Map<String, List<String>> timeToUniqueIdIndex) {
        this.imageCollection = imageCollection;
        this.timeToUniqueIdIndex = timeToUniqueIdIndex;
    }

    public void run() {
        LOG.info("TimeIndexer started");
        this.keepWorking = true;
        for (Iterator<Annotation> iterator = imageCollection.iterator(); keepWorking && iterator.hasNext(); ) {
            Annotation annotation = iterator.next();
            LOG.debug(annotation);
        }
        LOG.info("TimeIndexer finished");
    }

    public void handleBusEvent(BusEvent e) {
    }
}
