package uk.ac.lkl.migen.mockup.polydials.model.event;

import uk.ac.lkl.migen.mockup.polydials.model.ModuloCounter;
import uk.ac.lkl.migen.mockup.polydials.model.CounterModel;

public class CounterEvent extends StepwiseAdjustableEvent<ModuloCounter> {

    public CounterEvent(CounterModel model, ModuloCounter counter) {
        super(model, counter);
    }

    public ModuloCounter getCounter() {
        return getStepwiseAdjustable();
    }
}
