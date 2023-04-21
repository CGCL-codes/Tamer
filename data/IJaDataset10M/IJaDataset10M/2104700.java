package org.proteored.miapeapi.factories.ms;

import org.proteored.miapeapi.interfaces.ms.Maldi;

public class MaldiImpl implements Maldi {

    private final String extraction;

    private final String laser;

    private final String matrix;

    private final String name;

    private final String parameters;

    private final String plateType;

    private final String dissociation;

    private final String laserWaveLength;

    private final String dissociationSummary;

    @SuppressWarnings("unused")
    private MaldiImpl() {
        this(null);
    }

    public MaldiImpl(MaldiBuilder maldiBuilder) {
        this.extraction = maldiBuilder.extraction;
        this.laser = maldiBuilder.laser;
        this.matrix = maldiBuilder.matrix;
        this.name = maldiBuilder.name;
        this.parameters = maldiBuilder.parameters;
        this.plateType = maldiBuilder.plateType;
        this.dissociation = maldiBuilder.dissociation;
        this.dissociationSummary = maldiBuilder.dissociationSummary;
        this.laserWaveLength = maldiBuilder.laserWaveLength;
    }

    @Override
    public String getExtraction() {
        return extraction;
    }

    @Override
    public String getLaser() {
        return laser;
    }

    @Override
    public String getMatrix() {
        return matrix;
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public String getLaserParameters() {
        return parameters;
    }

    @Override
    public String getPlateType() {
        return plateType;
    }

    @Override
    public String getDissociation() {
        return dissociation;
    }

    @Override
    public String getLaserWaveLength() {
        return laserWaveLength;
    }

    @Override
    public String getDissociationSummary() {
        return dissociationSummary;
    }
}
