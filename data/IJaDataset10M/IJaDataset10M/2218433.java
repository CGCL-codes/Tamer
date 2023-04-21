package visualbiology.sbmlEditor.actions;

import org.eclipse.ui.actions.ActionFactory;
import org.eclipse.ui.actions.RetargetAction;
import org.eclipse.ui.plugin.AbstractUIPlugin;

public class FindRetarget extends RetargetAction {

    public FindRetarget() {
        super(ActionFactory.FIND.getId(), "Find SBML term");
        setToolTipText("Find SBML term");
        setImageDescriptor(AbstractUIPlugin.imageDescriptorFromPlugin("SBSI_VisualBiology", "icons/search_src.gif"));
    }
}
