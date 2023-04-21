package com.luciddreamingapp.beta.util.state;

public interface SmartTimerState {

    public void epochChanged(int newEpoch);

    public void updateActivity(double sleepScore);

    public void updateUserAsleep(boolean userAsleep);

    public void userEvent();
}
