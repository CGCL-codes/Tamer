package accessories.plugins;

import java.util.Iterator;
import java.util.List;
import accessories.plugins.dialogs.ChooseFormatPopupDialog;
import freemind.controller.actions.generated.instance.Pattern;
import freemind.modes.MindMapNode;
import freemind.modes.StylePatternFactory;
import freemind.modes.mindmapmode.hooks.MindMapNodeHookAdapter;

/**
 * @author adapted to the plugin mechanism by ganzer
 * 
 */
public class ApplyFormatPlugin extends MindMapNodeHookAdapter {

    /**
	 */
    public ApplyFormatPlugin() {
        super();
    }

    public void invoke(MindMapNode rootNode) {
        MindMapNode focussed = getController().getSelected();
        List selected = getController().getSelecteds();
        Pattern nodePattern = StylePatternFactory.createPatternFromSelected(focussed, selected);
        ChooseFormatPopupDialog formatDialog = new ChooseFormatPopupDialog(getController().getFrame().getJFrame(), getMindMapController(), "accessories/plugins/ApplyFormatPlugin.dialog.title", nodePattern);
        formatDialog.setModal(true);
        formatDialog.setVisible(true);
        if (formatDialog.getResult() == ChooseFormatPopupDialog.OK) {
            Pattern pattern = formatDialog.getPattern();
            for (Iterator iter = selected.iterator(); iter.hasNext(); ) {
                MindMapNode node = (MindMapNode) iter.next();
                getMindMapController().applyPattern(node, pattern);
            }
        }
    }
}
