package net.lukemurphey.nsia.tasks;

import net.lukemurphey.nsia.Application;
import net.lukemurphey.nsia.DefinitionUpdateWorker;
import java.util.TimerTask;

public class DefinitionsUpdateTask extends TimerTask {

    public DefinitionsUpdateTask(Application app) {
    }

    public void run() {
        DefinitionUpdateWorker worker = new DefinitionUpdateWorker();
        worker.run();
    }
}
