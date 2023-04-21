package es.rvp.java.simpletag.gui.components.tasks;

import org.apache.log4j.Logger;

/**
 * Ejeucta en un hilo la tarea FindCoversTask
 *
 * @author Rodrigo Villamil Perez
 */
public class FindCoversTaskRunnable implements Runnable {

    protected static final Logger LOGGER = Logger.getLogger(FindCoversTaskRunnable.class);

    FindCoversTask findCoversTask = null;

    public FindCoversTaskRunnable(final FindCoversTask findCoversTask) {
        this.findCoversTask = findCoversTask;
    }

    @Override
    public void run() {
        Thread.currentThread().setName("Search covers Thread (" + Thread.currentThread().getId() + ")");
        this.findCoversTask.execute();
        if (Thread.interrupted()) {
            LOGGER.warn("The thread has been interrupted !!!!");
            return;
        }
    }
}
