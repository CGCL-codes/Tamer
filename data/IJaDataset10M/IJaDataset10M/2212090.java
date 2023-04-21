package org.subrecord.console.jms;

public interface Job {

    public void performJob(CoreListener cs, Object to);
}
