package com.greentea.relaxation.algorithms;

public interface ILearningController {

    /**
    * ExecutionStatus is the advice to algorithm how it should to execute. Every concrete algorithm
    * can follow or not to follow to this advice.
    */
    public static enum LearningStatus {

        InProgress, Paused, Stoped
    }

    void startLearning();

    void pauseLearning();

    void resumeLearning();

    void stopLearning();

    LearningStatus getLearningStatus();
}
