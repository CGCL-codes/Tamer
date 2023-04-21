package org.gamenet.util;

public interface TaskObserver {

    public int getRange();

    public void taskProgress(String identifier, float percentageDone);
}
