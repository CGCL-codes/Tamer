package org.vikamine.swing.util.featureEditor;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import org.vikamine.app.DMManager;
import org.vikamine.kernel.data.DataRecord;
import org.vikamine.kernel.data.DerivedNominalValue;
import org.vikamine.kernel.data.Population;
import org.vikamine.kernel.formula.EvaluationData;
import org.vikamine.kernel.formula.FormulaNumberElement;
import org.vikamine.kernel.subgroup.SG;
import org.vikamine.kernel.subgroup.SGDescription;
import org.vikamine.kernel.subgroup.selectors.DefaultSGSelector;
import org.vikamine.kernel.subgroup.target.SGTarget;
import org.vikamine.swing.subgroup.AllSubgroupPluginController;

/**
 * @author Tobias Vogele, atzmueller
 */
public class CustomAggregatedNominalValue extends DerivedNominalValue {

    private static final long serialVersionUID = 7183684189183279038L;

    private FormulaNumberElement formula;

    public CustomAggregatedNominalValue(String description, String name) {
        super(description, name);
    }

    @Override
    public String getDescription() {
        return "" + calculateValue();
    }

    public double calculateValue() {
        EvaluationData numericData = new EvaluationData();
        SG sg = createSG();
        numericData.setStatistics(sg.getStatistics());
        List instances = new ArrayList();
        for (Iterator<DataRecord> iter = sg.subgroupInstanceIterator(); iter.hasNext(); ) {
            instances.add(iter.next());
        }
        numericData.setInstances(instances);
        return formula.eval(numericData).doubleValue();
    }

    private SG createSG() {
        Population population = DMManager.getInstance().getOntology().getPopulation();
        SG currentSG = AllSubgroupPluginController.getInstance().getSubgroupTreeController().getModel().getSubgroup();
        SGTarget target = currentSG.getTarget();
        SG sg = new SG(population, target);
        SGDescription description = (SGDescription) currentSG.getSGDescription().clone();
        description.add(new DefaultSGSelector(getAttribute(), this));
        sg.setSGDescription(description);
        sg.createStatistics(AllSubgroupPluginController.getInstance().getSubgroupDiscoveryController().getSConstraints());
        return sg;
    }

    public FormulaNumberElement getFormula() {
        return formula;
    }

    public void setFormula(FormulaNumberElement formula) {
        this.formula = formula;
    }
}
