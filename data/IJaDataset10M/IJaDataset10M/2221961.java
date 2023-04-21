package component_interfaces.semanticmm4u.realization.compositor.provided;

import component_interfaces.semanticmm4u.realization.compositor.realization.IInteractionOperator;
import component_interfaces.semanticmm4u.realization.compositor.realization.IProjectorList;
import de.offis.semanticmm4u.compositors.variables.operators.basics.interaction.fields_and_areas.HyperlinkEntryList;

public interface IHypertext extends IOneChildVariable, IInteractionOperator {

    public static final int VANISH = 0;

    public static final int PREVAIL = 1;

    public abstract HyperlinkEntryList getHyperlinks();

    public abstract int getMode();

    public abstract void setVariables(IVariableList myVariables);

    public abstract void setProjectors(IProjectorList myProjectors);
}
