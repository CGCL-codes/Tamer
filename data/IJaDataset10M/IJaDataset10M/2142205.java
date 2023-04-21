package com.googlecode.jazure.sdk.job;

import java.util.EventObject;

public class JobStoppedEvent extends EventObject {

    private static final long serialVersionUID = -3331820625057152555L;

    public JobStoppedEvent(Object source) {
        super(source);
    }

    @Override
    public Job<?> getSource() {
        return (Job<?>) source;
    }
}
