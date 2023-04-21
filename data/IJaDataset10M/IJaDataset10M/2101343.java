package org.bs.sm;

/**
* @author Boaz Nahum
* @version WI VX.6, V12.0X, ADC V0.95
*/
final class SMTriggerDataPacking {

    private final SystemTrigger trigger;

    private final SMStateVertex target;

    private final Object data;

    SMTriggerDataPacking(SystemTrigger trigger, SMStateVertex target, Object data) {
        this.trigger = trigger;
        this.target = target;
        this.data = data;
    }

    public SystemTrigger getTrigger() {
        return trigger;
    }

    public SMStateVertex getTarget() {
        return target;
    }

    public Object getData() {
        return data;
    }
}
