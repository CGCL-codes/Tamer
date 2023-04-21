package ru.cos.sim.visualizer.traffic.core;

public interface Controller {

    void actionPerformed(String action);

    ConditionManager.Action getActionType();
}
