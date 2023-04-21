package de.tum.in.botl.diff;

import de.tum.in.botl.model.BotlObject;
import de.tum.in.botl.model.ModelException;
import de.tum.in.botl.model.ModelFragment;
import de.tum.in.botl.util.Config;

/**
 * @author marschal
 *
 */
public class AddObject extends ObjectOperation {

    public AddObject(BotlObject bo) {
        super(bo);
    }

    /**
   * @return Returns the bo.
   */
    public BotlObject getBotlObject() {
        return bo;
    }

    public ModelFragment perform(ModelFragment mf) throws ModelException {
        Config.getInstance().getModelHelper().mergeCopyOfObjectIntoFragment(bo, mf);
        return mf;
    }

    public String toString() {
        return "ADD     " + bo.getId() + " (" + bo.getType().getName() + ")";
    }
}
