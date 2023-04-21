package applicationWorkbench.actions;

import org.eclipse.ui.actions.RetargetAction;
import org.eclipse.ui.plugin.AbstractUIPlugin;
import cards.CardConstants;
import fitintegration.PluginInformation;

public class ArrangeRetargetAction extends RetargetAction {

    public ArrangeRetargetAction(String actionID, String text) {
        super(actionID, text);
        setImageDescriptor(AbstractUIPlugin.imageDescriptorFromPlugin(PluginInformation.getPLUGIN_ID(), CardConstants.ArrangeIterationsByEndDateIcon));
    }

    public ArrangeRetargetAction(String actionID, String text, int style) {
        super(actionID, text, style);
    }
}
