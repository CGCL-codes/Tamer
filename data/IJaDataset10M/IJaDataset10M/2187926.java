package repast.simphony.data.gui.formula;

import org.pietschy.wizard.models.SimplePath;
import repast.simphony.data.gui.MappingWizardOption;
import repast.simphony.util.wizard.AbstractWizardOption;

/**
 * @author Nick Collier
 */
public class FormulaScriptOption extends AbstractWizardOption implements MappingWizardOption {

    private boolean aggregate = false;

    public FormulaScriptOption() {
        super("Formula Script Mapping", "A mapping based on the execution of a formula script");
    }

    public SimplePath getWizardPath() {
        return new SimplePath(new FormulaScriptStep());
    }

    public void setIsAggregate(boolean isAggregate) {
        this.aggregate = isAggregate;
    }
}
