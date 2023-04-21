package fr.ign.cogit.appli.geopensim.scheduler;

import java.util.EventListener;

/**
 * @author Julien Perret
 *
 */
public interface SchedulerEventListener extends EventListener {

    void changed(SchedulerEvent event);
}
