package ppine.visual.calculators;

import cytoscape.visual.EdgeAppearanceCalculator;
import cytoscape.visual.VisualPropertyType;
import cytoscape.visual.calculators.Calculator;
import java.util.List;

public class PPINEEdgeAppearanceCalculator extends EdgeAppearanceCalculator {

    Calculator defaultCalc = null;

    Calculator calcmy = null;

    public PPINEEdgeAppearanceCalculator(Calculator defaultCalc) {
        this.defaultCalc = defaultCalc;
        calcmy = new PPINEBasicEdgeCalculator("PPINEEdgeCalculator", defaultCalc.getMappings().get(0), defaultCalc.getVisualPropertyType());
    }

    @Override
    public Calculator getCalculator(VisualPropertyType type) {
        if (calcmy.getVisualPropertyType().equals(type)) {
            return calcmy;
        } else {
            return super.getCalculator(type);
        }
    }

    @Override
    public List<Calculator> getCalculators() {
        List<Calculator> calcsl = super.getCalculators();
        calcsl.add(calcmy);
        return calcsl;
    }
}
