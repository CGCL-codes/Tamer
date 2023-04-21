package org.kabeja.svg.action;

public interface CanvasUpdateManager {

    public void invokeLater(Runnable r) throws InterruptedException;

    public void invokeAndWait(Runnable r) throws InterruptedException;
}
