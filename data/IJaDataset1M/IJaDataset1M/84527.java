package accessories.plugins;

import java.util.HashMap;
import java.util.Iterator;
import java.util.logging.Logger;
import freemind.controller.actions.generated.instance.CompoundAction;
import freemind.controller.actions.generated.instance.FormatNodeAction;
import freemind.controller.actions.generated.instance.NewNodeAction;
import freemind.controller.actions.generated.instance.NodeAction;
import freemind.controller.actions.generated.instance.XmlAction;
import freemind.extensions.HookRegistration;
import freemind.modes.MindMap;
import freemind.modes.ModeController;
import freemind.modes.mindmapmode.MindMapController;
import freemind.modes.mindmapmode.actions.xml.ActionFilter;
import freemind.modes.mindmapmode.actions.xml.ActionHandler;
import freemind.modes.mindmapmode.actions.xml.ActionPair;

/** This plugin formats new nodes using the formats given to former nodes.
 * @author foltin
 */
public class FormatNewNodes implements ActionHandler, ActionFilter, HookRegistration {

    private MindMapController controller;

    private MindMap mMap;

    private Logger logger;

    private HashMap formatActions;

    public FormatNewNodes(ModeController controller, MindMap map) {
        this.controller = (MindMapController) controller;
        mMap = map;
        logger = controller.getFrame().getLogger(this.getClass().getName());
        this.formatActions = new HashMap();
    }

    public void register() {
        controller.getActionFactory().registerHandler(this);
        controller.getActionFactory().registerFilter(this);
    }

    public void deRegister() {
        controller.getActionFactory().deregisterHandler(this);
        controller.getActionFactory().deregisterFilter(this);
    }

    public void executeAction(XmlAction action) {
        detectFormatChanges(action);
    }

    /**
	 */
    private void detectFormatChanges(XmlAction doAction) {
        if (doAction instanceof CompoundAction) {
            CompoundAction compAction = (CompoundAction) doAction;
            for (Iterator i = compAction.getListChoiceList().iterator(); i.hasNext(); ) {
                XmlAction childAction = (XmlAction) i.next();
                detectFormatChanges(childAction);
            }
        } else if (doAction instanceof FormatNodeAction) {
            formatActions.put(doAction.getClass().getName(), doAction);
        }
    }

    public void startTransaction(String name) {
    }

    public void endTransaction(String name) {
    }

    public ActionPair filterAction(ActionPair pair) {
        if (pair.getDoAction() instanceof NewNodeAction) {
            NewNodeAction newNodeAction = (NewNodeAction) pair.getDoAction();
            CompoundAction compound = new CompoundAction();
            compound.addChoice(newNodeAction);
            for (Iterator i = formatActions.values().iterator(); i.hasNext(); ) {
                NodeAction formatAction = (NodeAction) i.next();
                FormatNodeAction copiedFormatAction = (FormatNodeAction) controller.unMarshall(controller.marshall(formatAction));
                copiedFormatAction.setNode(newNodeAction.getNewId());
                compound.addChoice(copiedFormatAction);
            }
            ActionPair newPair = new ActionPair(compound, pair.getUndoAction());
            return newPair;
        }
        return pair;
    }
}
