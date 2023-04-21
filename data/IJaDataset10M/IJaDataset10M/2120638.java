package org.openremote.modeler.domain.component;

import org.openremote.modeler.domain.BusinessEntity;
import flexjson.JSON;

/**
 * This domain class represents a action to navigate to.
 * It can navigate to screen, group or some logical targets.
 */
public class Navigate extends BusinessEntity {

    private static final long serialVersionUID = 4180916727141357903L;

    private long toScreen = -1L;

    private long toGroup = -1L;

    /** Navigate to logical type, which includes to setting, next screen, previous screen, 
    *  back, login and logout. 
    */
    private ToLogicalType toLogical = null;

    public long getToScreen() {
        return toScreen;
    }

    public long getToGroup() {
        return toGroup;
    }

    public void setToScreen(long toScreen) {
        this.toScreen = toScreen;
    }

    public void setToGroup(long toGroup) {
        this.toGroup = toGroup;
    }

    public ToLogicalType getToLogical() {
        return toLogical;
    }

    public void setToLogical(ToLogicalType toLogical) {
        this.toLogical = toLogical;
    }

    public void clearToLogical() {
        this.toLogical = null;
    }

    @JSON(include = false)
    public boolean isSet() {
        if (toGroup != -1) {
            return true;
        } else if (toLogical != null) {
            return true;
        }
        return false;
    }

    public static enum ToLogicalType {

        setting, back, login, logout, nextScreen, previousScreen
    }

    public boolean isToLogic() {
        if (toLogical != null) {
            return true;
        }
        return false;
    }

    public boolean toGroup() {
        if (toGroup != -1) {
            return true;
        }
        return false;
    }

    /**
    * Set navigate to nothing.
    */
    public void clear() {
        this.toLogical = null;
        this.toGroup = -1;
        this.toScreen = -1;
    }
}
