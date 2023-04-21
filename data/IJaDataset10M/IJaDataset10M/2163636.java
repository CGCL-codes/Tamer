package org.argeproje.resim.ui.component.basic;

import org.argeproje.resim.proc.ThresholdPR;
import org.argeproje.resim.property.DoublePRP;
import org.argeproje.resim.ui.model.ComponentItem;

public class ThresholdCI extends ComponentItem {

    private static final long serialVersionUID = 1;

    private static final String THRESHOLD_PROP = "ThresholdCI._threshold";

    private double _threshold = 128.0;

    private static final String LOW_VALUE_PROP = "ThresholdCI._lowValue";

    private double _lowValue = 0.0;

    private static final String HIGH_VALUE_PROP = "ThresholdCI._highValue";

    private double _highValue = 255.0;

    public ThresholdCI() {
        super();
        _name = "Threshold";
        _description = "Sets pixel values less than threshold " + "to lowValue and others to highValue";
        initProc();
    }

    public void setThreshold(double value) {
        _threshold = value;
    }

    public double getThreshold() {
        return _threshold;
    }

    public void setLowValue(double value) {
        _lowValue = value;
    }

    public double getLowValue() {
        return _lowValue;
    }

    public void setHighValue(double value) {
        _highValue = value;
    }

    public double getHighValue() {
        return _highValue;
    }

    protected void initDescriptorList() {
        super.initDescriptorList();
        registerProperty(new DoublePRP(this, THRESHOLD_PROP, "Threshold", "getThreshold", "setThreshold"));
        registerProperty(new DoublePRP(this, LOW_VALUE_PROP, "Low Value", "getLowValue", "setLowValue"));
        registerProperty(new DoublePRP(this, HIGH_VALUE_PROP, "High Value", "getHighValue", "setHighValue"));
    }

    @Override
    public Class<ThresholdCI> getComponentClass() {
        return ThresholdCI.class;
    }

    @Override
    public void initProc() {
        if (_processor == null) {
            _processor = new ThresholdPR();
        }
        _threshold = ((ThresholdPR) _processor).getParamThreshold();
        _lowValue = ((ThresholdPR) _processor).getParamLowValue();
        _highValue = ((ThresholdPR) _processor).getParamHighValue();
    }

    public void updateProc() {
        if (_processor == null) {
            _processor = new ThresholdPR();
        }
        ((ThresholdPR) _processor).setParameters(_threshold, _lowValue, _highValue);
    }
}
