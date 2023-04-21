package playground.johannes.coopsim.mental.planmod;

import java.util.Map;
import playground.johannes.sna.util.Composite;

/**
 * @author illenberger
 *
 */
public class Choice2ModAdaptorComposite extends Composite<Choice2ModAdaptor> implements Choice2ModAdaptor {

    @Override
    public PlanModifier convert(Map<String, Object> choices) {
        PlanModifierComposite composite = new PlanModifierComposite();
        for (Choice2ModAdaptor adaptor : components) composite.addComponent(adaptor.convert(choices));
        return composite;
    }
}
