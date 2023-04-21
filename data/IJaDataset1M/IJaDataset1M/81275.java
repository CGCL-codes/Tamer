package org.freeworld.jmultiplug.binding.base.settergetter;

import org.freeworld.jmultiplug.binding.base.BindingActivation;
import org.freeworld.jmultiplug.binding.base.BindingStatus;
import org.freeworld.jmultiplug.logging.Logger;
import org.freeworld.jmultiplug.util.devel.DebugMessages;

public abstract class AbstractGetterBinding implements GetterBinding {

    public Object fetch() throws ObjectNotFoundException, ObjectDeallocatedException {
        Logger.error(DebugMessages.incomplete());
        throw new ObjectNotFoundException();
    }

    public BindingActivation getBindingActivation() {
        Logger.error(DebugMessages.incomplete());
        return null;
    }

    public boolean isActive() {
        Logger.error(DebugMessages.incomplete());
        return false;
    }

    public boolean isBackendDataUpdateSupported() {
        Logger.error(DebugMessages.incomplete());
        return false;
    }

    public boolean isRevertSupported() {
        Logger.error(DebugMessages.incomplete());
        return false;
    }

    public void revert(Object toObject) {
        Logger.error(DebugMessages.incomplete());
    }

    public void setBackendDataUpdateProxy(SetterBinding binding) {
        Logger.error(DebugMessages.incomplete());
    }

    public boolean setBindingActivation(BindingActivation activationType) {
        Logger.error(DebugMessages.incomplete());
        return false;
    }

    public void setEnableBackendDataUpdate(boolean enableBackedUpdates) {
        Logger.error(DebugMessages.incomplete());
    }

    public BindingStatus getStatus() {
        Logger.error(DebugMessages.incomplete());
        return null;
    }

    public void setExpression(Object expression) {
        Logger.error(DebugMessages.incomplete());
    }

    public void setObject(Object object) {
        Logger.error(DebugMessages.incomplete());
    }

    public void activate() {
        Logger.error(DebugMessages.incomplete());
    }

    public void deActivate() {
        Logger.error(DebugMessages.incomplete());
    }
}
