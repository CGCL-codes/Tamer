package de.shandschuh.jaolt.gui.core;

public interface Statusable {

    public void nextStatus() throws Exception;

    public void previousStatus();

    public boolean hasNextStatus();

    public boolean hasPreviousStatus();
}
