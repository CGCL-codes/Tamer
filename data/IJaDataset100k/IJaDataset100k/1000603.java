package wood.model.manager;

import org.apache.log4j.Logger;
import wood.log.WoodLevel;

/**
 * This class implements the functionality that prompts updates on all Updateable 
 * objects at regular intervals
 * 
 * @author Nikolas Wolfe
 */
public class MasterTime extends Updater {

    private final Model model;

    public MasterTime(Model model, double updatesPerSecond) {
        super(updatesPerSecond);
        this.model = model;
    }

    @Override
    public void doTimedTask() {
        model.update();
    }
}
