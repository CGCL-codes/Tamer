package ie.blackoutscout.ui.group.common;

import ie.blackoutscout.ui.stage.AppPrimaryStage;
import javafx.scene.Group;

/**
 *
 * @author John
 */
public abstract class AbstractBlackoutScoutGroup extends Group {

    private AppPrimaryStage parentStage;

    public AbstractBlackoutScoutGroup(AppPrimaryStage parentStage) {
        this.parentStage = parentStage;
    }

    public AppPrimaryStage getParentStage() {
        return parentStage;
    }

    public void setParentStage(AppPrimaryStage parentStage) {
        this.parentStage = parentStage;
    }
}
