package vidis.ui.events.jobs.jobs.layouts;

import org.apache.log4j.Logger;
import vidis.ui.events.jobs.ILayoutJob;

/**
 * abstract relayout job
 * @author Dominik
 *
 */
public abstract class ARelayoutJob implements ILayoutJob {

    private static Logger logger = Logger.getLogger(ARelayoutJob.class);

    public void run() {
        try {
            getLayout().relayout(getNodes());
        } catch (Exception e) {
            logger.error(e);
        }
    }

    public boolean mustExecuteUniquely() {
        return false;
    }
}
