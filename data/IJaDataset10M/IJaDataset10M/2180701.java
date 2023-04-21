package net.sourceforge.traffiscope.engine;

import net.sourceforge.traffiscope.engine.entity.*;

public class QuantityTO extends AbstractNamedTO {

    private static final long serialVersionUID = 2620967277391464570L;

    public static final AggregatorType DEFAULT_AGGREGATOR = AggregatorType.SUM;

    public static final double DEFAULT_FILL_VALUE = Double.NaN;

    public static final double DEFAULT_MISSING_VALUE = Double.NaN;

    public static final double DEFAULT_SCALE_FACTOR = 1.0d;

    public static final double DEFAULT_VALID_MAX = Double.POSITIVE_INFINITY;

    public static final double DEFAULT_VALID_MIN = Double.NEGATIVE_INFINITY;

    private double _addOffset;

    private AggregatorType _aggregator;

    private double _fillValue;

    private double _missingValue;

    private double _scaleFactor;

    private String _unit;

    private double _validMax;

    private double _validMin;

    public QuantityTO() {
        _aggregator = DEFAULT_AGGREGATOR;
        _fillValue = DEFAULT_FILL_VALUE;
        _missingValue = DEFAULT_MISSING_VALUE;
        _scaleFactor = DEFAULT_SCALE_FACTOR;
        _validMax = DEFAULT_VALID_MAX;
        _validMin = DEFAULT_VALID_MIN;
    }

    public double getAddOffset() {
        return _addOffset;
    }

    public void setAddOffset(double value) {
        _addOffset = value;
    }

    public AggregatorType getAggregator() {
        return _aggregator;
    }

    public void setAggregator(AggregatorType value) {
        _aggregator = value;
    }

    public double getFillValue() {
        return _fillValue;
    }

    public void setFillValue(double value) {
        _fillValue = value;
    }

    public double getMissingValue() {
        return _missingValue;
    }

    public void setMissingValue(double value) {
        _missingValue = value;
    }

    public double getScaleFactor() {
        return _scaleFactor;
    }

    public void setScaleFactor(double value) {
        _scaleFactor = value;
    }

    public String getUnit() {
        return _unit;
    }

    public void setUnit(String value) {
        _unit = value;
    }

    public double getValidMax() {
        return _validMax;
    }

    public void setValidMax(double value) {
        _validMax = value;
    }

    public double getValidMin() {
        return _validMin;
    }

    public void setValidMin(double value) {
        _validMin = value;
    }
}
