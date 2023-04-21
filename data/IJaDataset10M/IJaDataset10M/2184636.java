package q.zik.basic.effect;

import q.lang.Parameter;
import q.zik.core.machine.definition.EffectDefinition.SimpleEffectDefinition;
import q.zik.core.machine.instance.SimpleEffect;
import q.zik.core.parameter.definition.MachineParameterDefinition;
import q.zik.lang.Gain;
import q.zik.lang.Period;
import q.zik.lang.Sample;

public class QDynamicsDefinition extends SimpleEffectDefinition {

    public static final class QDynamics extends SimpleEffect<QDynamicsDefinition> {

        protected final Parameter<Period> attack;

        protected final Parameter<Gain> gainCorrection;

        protected final Parameter<Period> compressorRelease;

        protected final Parameter<Gain> compressorThreshold;

        protected final Parameter<Double> compressorRatio;

        protected final Parameter<Period> gateRelease;

        protected final Parameter<Gain> gateThreshold;

        protected final Parameter<Double> gateRatio;

        private double instantGain = 0d;

        private long ratio;

        private long sqCurrentVolume;

        public QDynamics(final QDynamicsDefinition _definition) {
            super(_definition);
            attack = getParameter(_definition.attack);
            gainCorrection = getParameter(_definition.gainCorrection);
            compressorRelease = getParameter(_definition.compressorRelease);
            compressorThreshold = getParameter(_definition.compressorThreshold);
            compressorRatio = getParameter(_definition.compressorRatio);
            gateRelease = getParameter(_definition.gateRelease);
            gateThreshold = getParameter(_definition.gateThreshold);
            gateRatio = getParameter(_definition.gateRatio);
        }

        @Override
        public void controllerTick(final int _controllerTime) {
            computeInstantGain();
        }

        @Override
        public Sample simpleProcess(final Sample input, final int _time) {
            final long instantVolume = input.left * input.left + input.right * input.right;
            sqCurrentVolume = (sqCurrentVolume * ratio + instantVolume) / (1 + ratio);
            return input.mult(instantGain);
        }

        public void computeInstantGain() {
            final double gateThresholdValue = gateThreshold.getValue().doubleValue() * Sample.MAX_SAMPLE_VALUE;
            final double compressorThresholdValue = compressorThreshold.getValue().getRatio() * Sample.MAX_SAMPLE_VALUE;
            final double sqLowThreshold = gateThresholdValue * gateThresholdValue;
            final double sqHighThreshold = compressorThresholdValue * compressorThresholdValue;
            if (sqCurrentVolume < sqLowThreshold) {
                updateRatio(gateRelease.getValue().doubleValue());
                instantGain = Math.max(0, gainCorrection.getValue().doubleValue() * (sqLowThreshold + (sqCurrentVolume - sqLowThreshold) * gateRatio.getValue().doubleValue()) / sqCurrentVolume);
            } else if (sqCurrentVolume > sqHighThreshold) {
                updateRatio(compressorRelease.getValue().getPeriodInSample());
                instantGain = gainCorrection.getValue().doubleValue() * (sqLowThreshold + (sqCurrentVolume - sqLowThreshold) * compressorRatio.getValue().doubleValue()) / sqCurrentVolume;
            } else {
                updateRatio(attack.getValue().getPeriodInSample());
                instantGain = gainCorrection.getValue().getRatio();
            }
        }

        private void updateRatio(final double _value) {
            ratio = (int) _value / 8;
        }
    }

    protected final MachineParameterDefinition<Period> attack = addPeriodParameter("Attack", "Attack period in ms. This is the period used to compute the volume change of the signal, which will trigger dynamic effects.", 0f, 1f, 1000, 0.8f);

    protected final MachineParameterDefinition<Gain> gainCorrection = addGainParameter("Gain correction applied to the processed signal.", "", -20d, 0d, 100, -1d);

    protected final MachineParameterDefinition<Period> compressorRelease = addPeriodParameter("Compressor release", "This is the period used to compute the volume change of the signal, which will release the compression.", 0f, 1f, 1000, 1f);

    protected final MachineParameterDefinition<Gain> compressorThreshold = addGainParameter("Compressor threshold", "This ratio determines at which level the compression starts.", -40d, 0d, 100, -3d);

    protected final MachineParameterDefinition<Double> compressorRatio = addRangeParameter("Compressor ratio", "Determines the amount of compression applied to the signal.", ":1", 0d, 1d, 100, 0.5d);

    protected final MachineParameterDefinition<Period> gateRelease = addPeriodParameter("Expander release", "Delay used to compute volume change for expander release.", 0f, 1f, 1000, 0.1f);

    protected final MachineParameterDefinition<Gain> gateThreshold = addGainParameter("Expander Threshold", "Determines the floor value below which the expander starts working.", -40d, 0d, 100, -3d);

    protected final MachineParameterDefinition<Double> gateRatio = addRangeParameter("Expander Ratio", "Determines the expander amount.", ":1", 0d, 1d, 100, 0.5d);

    public QDynamicsDefinition() {
        super("QDynamics", "Dynamic effect : Gate or Compressor. Can be used as distorsion with extreme parameters.");
    }

    @Override
    public QDynamics buildInstance() {
        return new QDynamics(this);
    }
}
