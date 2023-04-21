package slojj.dotsbox.core;

public interface Task extends Runnable {

    String getId();

    String getStatus();

    int getReturnCode();

    long getCost();

    Object getSource();

    Object getTarget();

    boolean isTimerTask();

    long getDelaySeconds();

    long getPeriodSeconds();
}
