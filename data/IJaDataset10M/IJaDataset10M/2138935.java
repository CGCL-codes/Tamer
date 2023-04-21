package com.greentea.relaxation.jnmf.gui.components.project.network;

import com.greentea.relaxation.jnmf.gui.components.project.network.learning.LearningState;

/**
 * Created by IntelliJ IDEA. User: GreenTea Date: 28.08.2009 Time: 23:19:14 To change this template
 * use File | Settings | File Templates.
 */
public class NetworkInfo {

    private String name;

    private ParametersState parametersState;

    private String dataTransformations;

    private LearningState learningState;

    private Double errorOnTestData;

    private Double notGuessedClassesPersentOnTestData;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public ParametersState getParametersState() {
        return parametersState;
    }

    public void setParametersState(ParametersState parametersState) {
        this.parametersState = parametersState;
    }

    public LearningState getLearningState() {
        return learningState;
    }

    public void setLearningState(LearningState learningState) {
        this.learningState = learningState;
    }

    public Double getErrorOnTestData() {
        return errorOnTestData;
    }

    public void setErrorOnTestData(Double errorOnTestData) {
        this.errorOnTestData = errorOnTestData;
    }

    public Double getNotGuessedClassesPersentOnTestData() {
        return notGuessedClassesPersentOnTestData;
    }

    public void setNotGuessedClassesPersentOnTestData(Double notGuessedClassesPersentOnTestData) {
        this.notGuessedClassesPersentOnTestData = notGuessedClassesPersentOnTestData;
    }

    public String getDataTransformations() {
        return dataTransformations;
    }

    public void setDataTransformations(String dataTransformations) {
        this.dataTransformations = dataTransformations;
    }
}
