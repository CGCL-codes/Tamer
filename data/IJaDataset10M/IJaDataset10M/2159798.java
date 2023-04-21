package org.personalsmartspace.cm.reasoning.activity.features;

import java.util.ArrayList;
import org.personalsmartspace.cm.reasoning.activity.interfaces.IFeature;
import org.personalsmartspace.cm.reasoning.activity.interfaces.ISignal;
import org.personalsmartspace.cm.reasoning.activity.signals.SignalVerticalAccelerationBodyFrame;

public class FeatureMaximumValueVertAccBF128 extends Feature_SimpleFeature {

    final int UPDATE_FREQUENCY = 25;

    final double WINDOW_SIZE = 128;

    SignalVerticalAccelerationBodyFrame vertAcc;

    Feature_Output[] value;

    public FeatureMaximumValueVertAccBF128(ArrayList<ISignal> a, ArrayList<IFeature> f) {
        super();
        for (ISignal any : a) {
            if (any instanceof SignalVerticalAccelerationBodyFrame) {
                this.vertAcc = (SignalVerticalAccelerationBodyFrame) any;
            }
        }
        this.value = new Feature_Output[1];
        this.value[0] = new Feature_Output();
    }

    public Feature_Output[] computeFeature() {
        double maximum = (Double) this.vertAcc.getLastSample().getValue();
        for (int i = 0; i < this.WINDOW_SIZE; i++) {
            if (((Double) this.vertAcc.getSample(this.vertAcc.getLength() - i).getValue()) > maximum) {
                maximum = (Double) this.vertAcc.getSample(this.vertAcc.getLength() - i).getValue();
            }
        }
        value[0].updateOutput(maximum);
        return value;
    }

    public Feature_Output[] getFeature() {
        return value;
    }

    public int getUpdateFrequency() {
        return this.UPDATE_FREQUENCY;
    }
}
