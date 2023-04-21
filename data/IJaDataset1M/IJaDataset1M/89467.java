package org.rg.scanner.loaders;

import java.io.BufferedReader;
import java.io.FileReader;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashSet;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.rg.common.datatypes.Event;
import org.rg.workflow.ContextManager;
import org.rg.workflow.executor.ServiceException;
import org.rg.workflow.planner.IPlanner;
import org.rg.workflow.service.WorkflowService;
import org.rg.workflow.support.IntegratedPropertiesService;

/**
 * Load an html page, LoadPageDataEvent
 */
public class SeedLoadingService extends WorkflowService {

    /** logger */
    private static final Log log = LogFactory.getLog(SeedLoadingService.class);

    /** the planner object used to issue new events. */
    private IPlanner planner = null;

    /** the integrated props server. */
    private IntegratedPropertiesService props = null;

    /**
     * set the integrate properties service.
     * @param props the properties service.
     */
    public void setPropertiesService(IntegratedPropertiesService props) {
        this.props = props;
    }

    /**
     * set the planner reference done via the bean properties.
     * @param planner the planner interface.
     */
    public void setPlanner(IPlanner planner) {
        this.planner = planner;
    }

    /**
     * when we start the bean for now, we also generate the event to load the seeds.
     */
    public boolean startBean() {
        boolean rv = super.startBean();
        if (!rv) return rv;
        if (planner != null) {
            log.info("Sending event to start bean loading.");
            planner.handleArrivalFromEventSource(new LoadSeedsEvent(), "RgAutomatic");
        }
        return true;
    }

    /**
     * Handle event to load a web page.
     * @param evt the event containing the page to load.
     */
    @Override
    public void handleEvent(Event evt) throws ServiceException {
        try {
            if (evt instanceof LoadSeedsEvent) {
                String seedFile = props.getProperties().getProperty(ContextManager.SPRING_APP_DEF_ROOT_PATH) + "/seeds.txt";
                BufferedReader br = new BufferedReader(new FileReader(seedFile));
                String line;
                ArrayList<URL> seeds = new ArrayList<URL>();
                HashSet<String> unique = new HashSet<String>();
                while ((line = br.readLine()) != null) {
                    URL url = null;
                    try {
                        url = UrlNormalizer.normalize(line);
                    } catch (Throwable mue) {
                        log.warn(line + " is not a valid url.");
                        continue;
                    }
                    if (!unique.contains(url.toString())) {
                        seeds.add(url);
                        unique.add(url.toString());
                    }
                }
                log.info("Of " + seeds.size() + " seeds from seed file " + seedFile + ", " + unique.size() + " web sites were identified.");
                ((LoadSeedsEvent) evt).setUrls(seeds);
            } else {
                log.error("This service does not deal with events of type " + evt.getClass().getName());
            }
        } catch (Throwable t) {
            log.error("Can't load the seeds.", t);
            ;
        }
    }
}
