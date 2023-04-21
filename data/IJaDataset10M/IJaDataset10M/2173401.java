package kohary.datamodel.actions;

import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import java.util.List;
import kohary.datamodel.DatamodelCreator;
import kohary.datamodel.util.GraphicsTools;
import kohary.datamodel.variables.EditVariableFrame;
import kohary.datamodel.variables.Dvariable;
import kohary.datamodel.variables.VariableCreatorFrame;

/**
 *
 * @author Godric
 */
public class VariableEditingAction extends Action {

    public VariableEditingAction() {
        String name = "Variables";
        putValue(NAME, name);
        putValue(SHORT_DESCRIPTION, name);
        putValue(SMALL_ICON, GraphicsTools.getIcon("add.gif"));
        putValue(SHORT_DESCRIPTION, "");
        putValue(MNEMONIC_KEY, KeyEvent.VK_V);
    }

    public void actionPerformed(ActionEvent e) {
        List<Dvariable> variables = DatamodelCreator.getInstance().getDocument().getVariables();
        if (variables.isEmpty()) new VariableCreatorFrame(); else new VariableCreatorFrame(variables);
    }
}
