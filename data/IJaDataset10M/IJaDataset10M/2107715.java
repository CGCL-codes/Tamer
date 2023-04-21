package org.vikamine.swing.subgroup.debugger;

import org.vikamine.app.DMManager;
import org.vikamine.app.Resources;
import org.vikamine.kernel.subgroup.SG;
import org.vikamine.kernel.subgroup.SGDescription;
import org.vikamine.kernel.subgroup.debug.Variable;
import org.vikamine.kernel.subgroup.target.SGTarget;
import org.vikamine.swing.subgroup.AllSubgroupPluginController;

/**
 * @author Tobias Vogele
 */
public class SubgroupEditor implements VariableEditor {

    public void edit(Variable v, VariablesView vview) {
        vview.getTextArea().setText(Resources.I18N.getString("vikamine.debugger.editor.subgroups"));
        vview.getTextArea().setEditable(false);
        SG sg;
        Object value = v.getValue();
        if (value instanceof SG) {
            sg = (SG) value;
        } else if (value instanceof SGDescription) {
            SGTarget target = AllSubgroupPluginController.getInstance().getSubgroupTreeController().getModel().getSubgroup().getTarget();
            sg = new SG(DMManager.getInstance().getOntology().getPopulation(), target);
            sg.setSGDescription((SGDescription) value);
        } else {
            throw new IllegalArgumentException("bad value: " + value);
        }
        AllSubgroupPluginController.getInstance().getSubgroupTreeController().getModel().setSubgroup(sg);
    }

    public void stopEdit() {
    }
}
