package org.proteored.miapeapi.factories.gi;

import java.util.Set;
import org.proteored.miapeapi.interfaces.Algorithm;
import org.proteored.miapeapi.interfaces.gi.DataAnalysis;
import org.proteored.miapeapi.interfaces.gi.ImageAnalysisSoftware;

public class DataAnalysisImpl implements DataAnalysis {

    private final String name;

    private final String type;

    private final String intent;

    private final String parameters;

    private final String inputData;

    private final Set<Algorithm> dataAnalysisTransformations;

    private final Set<ImageAnalysisSoftware> dataAnalysisSoftwares;

    public DataAnalysisImpl(DataAnalysisBuilder dataAnalysisBuilder) {
        this.name = dataAnalysisBuilder.name;
        this.type = dataAnalysisBuilder.type;
        this.intent = dataAnalysisBuilder.intent;
        this.parameters = dataAnalysisBuilder.parameters;
        this.inputData = dataAnalysisBuilder.inputData;
        this.dataAnalysisTransformations = dataAnalysisBuilder.dataAnalysisTransformations;
        this.dataAnalysisSoftwares = dataAnalysisBuilder.dataAnalysisSoftwares;
    }

    @Override
    public Set<ImageAnalysisSoftware> getDataAnalysisSoftwares() {
        return dataAnalysisSoftwares;
    }

    @Override
    public Set<Algorithm> getDataAnalysisTransformations() {
        return dataAnalysisTransformations;
    }

    @Override
    public String getInputData() {
        return inputData;
    }

    @Override
    public String getIntent() {
        return intent;
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public String getParameters() {
        return parameters;
    }

    @Override
    public String getType() {
        return type;
    }
}
