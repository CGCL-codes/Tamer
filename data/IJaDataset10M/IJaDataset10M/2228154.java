package org.proteored.miapeapi.factories.ms;

import org.proteored.miapeapi.factories.SoftwareImpl;
import org.proteored.miapeapi.interfaces.ms.Acquisition;

class AcquisitionImpl extends SoftwareImpl implements Acquisition {

    private final String parameterFile;

    private final String transitionListFile;

    private final String targetList;

    public AcquisitionImpl(AcquisitionBuilder acquisitionBuilder) {
        super(acquisitionBuilder);
        this.parameterFile = acquisitionBuilder.parameterFile;
        this.targetList = acquisitionBuilder.targetList;
        this.transitionListFile = acquisitionBuilder.transitionListFile;
    }

    @Override
    public String getParameterFile() {
        return parameterFile;
    }

    @Override
    public String getTransitionListFile() {
        return transitionListFile;
    }

    @Override
    public String getTargetList() {
        return targetList;
    }
}
