package freemind.modes.actions;

import java.awt.event.ActionEvent;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.Vector;
import java.util.logging.Logger;
import javax.swing.Action;
import javax.swing.ImageIcon;
import javax.swing.JMenuItem;
import javax.xml.bind.JAXBException;
import freemind.controller.MenuItemEnabledListener;
import freemind.controller.actions.ActionPair;
import freemind.controller.actions.ActorXml;
import freemind.controller.actions.FreemindAction;
import freemind.controller.actions.generated.instance.HookNodeAction;
import freemind.controller.actions.generated.instance.NodeListMember;
import freemind.controller.actions.generated.instance.NodeListMemberType;
import freemind.controller.actions.generated.instance.XmlAction;
import freemind.extensions.HookFactory;
import freemind.extensions.HookInstanciationMethod;
import freemind.extensions.NodeHook;
import freemind.extensions.PermanentNodeHook;
import freemind.modes.MindMapNode;
import freemind.modes.ModeController;

public class NodeHookAction extends FreemindAction implements ActorXml, MenuItemEnabledListener {

    String _hookName;

    ModeController controller;

    public ModeController getController() {
        return controller;
    }

    private static Logger logger;

    public NodeHookAction(String hookName, ModeController controller) {
        super(hookName, (ImageIcon) null, null);
        this._hookName = hookName;
        this.controller = controller;
        if (logger == null) logger = controller.getFrame().getLogger(this.getClass().getName());
        controller.getActionFactory().registerActor(this, getDoActionClass());
    }

    public void actionPerformed(ActionEvent arg0) {
        controller.getFrame().setWaitingCursor(true);
        invoke(controller.getSelected(), controller.getSelecteds());
        controller.getFrame().setWaitingCursor(false);
    }

    public void addHook(MindMapNode focussed, List selecteds, String hookName) {
        HookNodeAction doAction = createHookNodeAction(focussed, selecteds, hookName);
        XmlAction undoAction = null;
        try {
            undoAction = controller.getActionXmlFactory().createCompoundAction();
        } catch (JAXBException e) {
            e.printStackTrace();
        }
        if (getInstanciationMethod(hookName).isPermanent()) {
            undoAction = createHookNodeAction(focussed, selecteds, hookName);
        }
        if (getInstanciationMethod(hookName).isUndoable()) {
            getController().getActionFactory().startTransaction((String) getValue(NAME));
            getController().getActionFactory().executeAction(new ActionPair(doAction, undoAction));
            getController().getActionFactory().endTransaction((String) getValue(NAME));
        } else {
            invoke(focussed, selecteds, hookName);
        }
    }

    public void invoke(MindMapNode focussed, List selecteds) {
        addHook(focussed, selecteds, _hookName);
    }

    private void invoke(MindMapNode focussed, List selecteds, String hookName) {
        logger.finest("invoke(selecteds) called.");
        HookInstanciationMethod instMethod = getInstanciationMethod(hookName);
        Collection destinationNodes = instMethod.getDestinationNodes(controller, focussed, selecteds);
        MindMapNode adaptedFocussedNode = instMethod.getCenterNode(controller, focussed, selecteds);
        if (instMethod.isAlreadyPresent(controller, hookName, adaptedFocussedNode, destinationNodes)) {
            for (Iterator i = destinationNodes.iterator(); i.hasNext(); ) {
                MindMapNode currentDestinationNode = (MindMapNode) i.next();
                for (Iterator j = currentDestinationNode.getActivatedHooks().iterator(); j.hasNext(); ) {
                    PermanentNodeHook hook = (PermanentNodeHook) j.next();
                    if (hook.getName().equals(hookName)) {
                        currentDestinationNode.removeHook(hook);
                        break;
                    }
                }
            }
        } else {
            for (Iterator it = destinationNodes.iterator(); it.hasNext(); ) {
                MindMapNode currentDestinationNode = (MindMapNode) it.next();
                NodeHook hook = controller.createNodeHook(hookName, currentDestinationNode, controller.getMap());
                logger.finest("created hook " + hookName);
                currentDestinationNode.invokeHook(hook);
                if (hook instanceof PermanentNodeHook) {
                    PermanentNodeHook permHook = (PermanentNodeHook) hook;
                    logger.finest("This is a permanent hook " + hookName);
                    if (currentDestinationNode == adaptedFocussedNode) {
                        permHook.onReceiveFocusHook();
                    }
                    controller.nodeChanged(currentDestinationNode);
                }
            }
            finishInvocation(focussed, selecteds, adaptedFocussedNode, destinationNodes);
        }
    }

    /**
	 * @param focussed The real focussed node
	 * @param selecteds The list of selected nodes
	 * @param adaptedFocussedNode The calculated focussed node (if the hook specifies, that 
	 * the hook should apply to root, then this is the root node).
	 * @param destinationNodes The calculated list of selected nodes (see last)
	 */
    private void finishInvocation(MindMapNode focussed, List selecteds, MindMapNode adaptedFocussedNode, Collection destinationNodes) {
        if (focussed.getViewer() != null) {
            getController().getView().selectAsTheOnlyOneSelected(focussed.getViewer());
            getController().getView().scrollNodeToVisible(focussed.getViewer());
            for (Iterator i = selecteds.iterator(); i.hasNext(); ) {
                MindMapNode node = (MindMapNode) i.next();
                if (node.getViewer() != null) {
                    getController().getView().makeTheSelected(node.getViewer());
                }
            }
        }
    }

    /**
	 * @return
	 */
    private HookInstanciationMethod getInstanciationMethod(String hookName) {
        HookFactory factory = getHookFactory();
        HookInstanciationMethod instMethod = factory.getInstanciationMethod(hookName);
        return instMethod;
    }

    /**
	 * @return
	 */
    private HookFactory getHookFactory() {
        HookFactory factory = controller.getFrame().getHookFactory();
        return factory;
    }

    public boolean isEnabled(JMenuItem item, Action action) {
        HookFactory factory = getHookFactory();
        Object baseClass = factory.getPluginBaseClass(_hookName);
        if (baseClass != null) {
            if (baseClass instanceof MenuItemEnabledListener) {
                MenuItemEnabledListener listener = (MenuItemEnabledListener) baseClass;
                return listener.isEnabled(item, this);
            }
        }
        MindMapNode focussed = controller.getSelected();
        List selecteds = controller.getSelecteds();
        HookInstanciationMethod instMethod = getInstanciationMethod(_hookName);
        Collection destinationNodes = instMethod.getDestinationNodes(controller, focussed, selecteds);
        MindMapNode adaptedFocussedNode = instMethod.getCenterNode(controller, focussed, selecteds);
        boolean isActionSelected = instMethod.isAlreadyPresent(controller, _hookName, adaptedFocussedNode, destinationNodes);
        setSelected(item, isActionSelected);
        return true;
    }

    public HookNodeAction createHookNodeAction(MindMapNode focussed, List selecteds, String hookName) {
        try {
            HookNodeAction hookNodeAction = getController().getActionXmlFactory().createHookNodeAction();
            hookNodeAction.setNode(focussed.getObjectId(getController()));
            hookNodeAction.setHookName(hookName);
            for (Iterator i = selecteds.iterator(); i.hasNext(); ) {
                MindMapNode node = (MindMapNode) i.next();
                NodeListMember nodeListMember = getController().getActionXmlFactory().createNodeListMember();
                nodeListMember.setNode(node.getObjectId(getController()));
                hookNodeAction.getNodeListMember().add(nodeListMember);
            }
            return hookNodeAction;
        } catch (JAXBException e) {
            e.printStackTrace();
            return null;
        }
    }

    public void act(XmlAction action) {
        if (action instanceof HookNodeAction) {
            HookNodeAction hookNodeAction = (HookNodeAction) action;
            MindMapNode selected = getController().getNodeFromID(hookNodeAction.getNode());
            Vector selecteds = new Vector();
            for (Iterator i = hookNodeAction.getNodeListMember().iterator(); i.hasNext(); ) {
                NodeListMemberType node = (NodeListMemberType) i.next();
                selecteds.add(getController().getNodeFromID(node.getNode()));
            }
            invoke(selected, selecteds, hookNodeAction.getHookName());
        }
    }

    public Class getDoActionClass() {
        return HookNodeAction.class;
    }

    /**
	 * @return
	 */
    public String getHookName() {
        return _hookName;
    }
}
