package mcujavasource.translator.general;

import org.w3c.dom.*;
import mcujavasource.mcu.TimerCompare;
import mcujavasource.transformer.instance.ListenerInstance;

/**
 *
 */
public abstract class TimerCompareTranslator {

    public abstract void getValue(McuContext context);

    public abstract void setValue(McuContext context, int value);

    public abstract void setValue(McuContext context, Element value);

    public abstract void setPinMode(McuContext context, TimerCompare.PinMode pinMode);

    public abstract void isCompareMatchFired(McuContext context);

    public abstract void setCompareMatchFired(McuContext context, boolean enabled);

    public abstract void addTimerCompareListener(McuContext context, ListenerInstance listener);

    public abstract String getPin(McuContext context);
}
