package q.zik.basic.effect;

import q.lang.BasicParameter;
import q.lang.Constant;
import q.zik.component.Delay;
import q.zik.component.Dummy;
import q.zik.component.EffectUnit;
import q.zik.component.Sine;
import q.zik.core.machine.definition.EffectDefinition.SimpleEffectDefinition;
import q.zik.core.machine.instance.Effect;
import q.zik.core.machine.instance.SimpleEffect;
import q.zik.core.parameter.definition.MachineParameterDefinition;
import q.zik.core.parameter.instance.MachineParameter;
import q.zik.lang.Gain;
import q.zik.lang.Period;
import q.zik.lang.Sample;

public class QPhaserDefinition extends SimpleEffectDefinition {

    protected final MachineParameterDefinition<Period> delay = addPeriodParameter("Min Delay", "Minimum delay length in ms", 0, 100, 500, 50);

    protected final MachineParameterDefinition<Period> amplitude = addPeriodParameter("Delay Amplitude", "Delay variation amplitude in ms", 0, 100, 500, 50);

    protected final MachineParameterDefinition<Period> freq = addPeriodParameter("Delay Period", "Delay variation period in ms", 100, 10100, 500, 1000);

    protected final MachineParameterDefinition<Gain> feedBackGain = addGainParameter("FeedBack Gain", "Gain applied to the feedback loop.", -60d, 0d, 100, -3d);

    public static class QPhaser extends SimpleEffect<QPhaserDefinition> {

        private final BasicParameter<Period> _delta = new BasicParameter<Period>(Period.valueOfForSampleRate(0));

        private MachineParameter<Period> amp;

        private Delay innerDelay;

        private final Sine innerSine;

        public QPhaser(final QPhaserDefinition _definition) {
            super(_definition);
            final EffectUnit feedBackFilter = Dummy.INSTANCE;
            innerDelay = new Delay(feedBackFilter, getParameter(_definition.feedBackGain), getParameter(_definition.delay), _delta);
            innerSine = new Sine(getParameter(_definition.freq), new Constant<Gain>(Gain.gainFromRatio(1)));
            amp = getParameter(_definition.amplitude);
        }

        @Override
        public Sample simpleProcess(Sample _input, int _time) {
            final float ampValue = amp.getValue().floatValue();
            _delta.setValue(Period.valueOfForSampleRate((ampValue + innerSine.generate(_time).floatValue() * ampValue) / 2f));
            return innerDelay.process(_input, _time);
        }
    }

    public QPhaserDefinition() {
        super("QPhaser", "Short variable delay with feedback. Can be used as a phaser, flanger or chorus effect.");
    }

    @Override
    protected Effect<?> buildInstance() {
        return new QPhaser(this);
    }
}
