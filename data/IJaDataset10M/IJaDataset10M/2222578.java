package jmcf.impl;

import jmcf.impl.AbstractCyclicActivityComponent;

public abstract class AbstractFixedDelayActivityComponent extends AbstractCyclicActivityComponent {

    public static String PREFERRED_DELAY = "PREFERRED_DELAY";

    int preferredDelay = 0;

    public AbstractFixedDelayActivityComponent() {
        super();
    }

    @Override
    public void init() {
        super.init();
        properties.setProperty(PREFERRED_DELAY, "1");
    }

    @Override
    public void onStateChange(int newState) {
        if (newState == STATE_RUNNING) {
            nCycle = 0;
            preferredDelay = Integer.parseInt(properties.getProperty(PREFERRED_DELAY));
            getExecutor().scheduleWithFixedDelay(stepTask, preferredDelay);
        } else if (newState == STATE_SUSPENDED) {
            getExecutor().remove(stepTask);
        }
        fireStateDescription(newState);
    }
}
