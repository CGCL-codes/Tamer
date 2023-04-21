package game.evolution.treeEvolution.run;

import game.evolution.treeEvolution.evolutionControl.EvolutionControl;
import org.apache.log4j.Logger;
import java.util.concurrent.Semaphore;

public class ExperimentThread extends Thread {

    EvolutionControl evol;

    private Semaphore activeThreads;

    public ExperimentThread(String filename, int timeSeconds, Semaphore activeThreads) {
        evol = new EvolutionControl(filename);
        evol.setRunTime(timeSeconds);
        this.activeThreads = activeThreads;
    }

    public void run() {
        Logger log = Logger.getLogger(this.getClass());
        log.info("starting experiment thread[" + Thread.currentThread().getId() + "]");
        evol.autoRun();
        log.info("ending experiment thread[" + Thread.currentThread().getId() + "]");
        activeThreads.release();
    }
}
