package org.gvsig.remoteClient.sld;

import org.gvsig.remoteClient.sld.symbolizers.ISLDSymbolizer;

public abstract class AbstractSLDSymbolizer implements ISLDSymbolizer {

    protected String geometry;

    protected double minScaleDenominator;

    protected double maxScaleDenominator;

    public void setMaxScaleDenominator(double maxScaleDenominator) {
        this.maxScaleDenominator = maxScaleDenominator;
    }

    public void setMinScaleDenominator(double minScaleDenominator) {
        this.minScaleDenominator = minScaleDenominator;
    }

    public String getGeometry() {
        return geometry;
    }

    public void setGeometry(String geometry) {
        this.geometry = geometry;
    }

    public double getMinScaleDenominator() {
        return minScaleDenominator;
    }

    public double getMaxScaleDenominator() {
        return maxScaleDenominator;
    }
}
