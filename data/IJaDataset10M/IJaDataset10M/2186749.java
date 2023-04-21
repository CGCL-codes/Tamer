package net.ar.guia.own.validators;

import java.util.*;
import net.ar.guia.own.interfaces.*;

public interface Validator extends VisualComponent {

    public abstract boolean doValidation();

    public abstract void addTrigger(VisualComponent aComponent);

    public abstract boolean isRemoteValidation();

    public abstract void setRemoteValidation(boolean b);

    public abstract VisualComponent getComponentToValidate();

    public abstract void setComponentToValidate(VisualComponent component);

    public abstract String getGroupMessage();

    public abstract void setGroupMessage(String aString);

    public abstract String getOwnMessage();

    public abstract void setOwnMessage(String aString);

    public abstract boolean isGrouped();

    public abstract void setGrouped(boolean b);

    public abstract List getValidationTriggers();

    public abstract void setValidationTriggers(List aValidationTriggers);

    public abstract String getText();
}
