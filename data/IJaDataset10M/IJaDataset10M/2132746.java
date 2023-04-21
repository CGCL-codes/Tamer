package freemind.modes.actions;

import java.awt.event.ActionEvent;
import javax.swing.JOptionPane;
import javax.xml.bind.JAXBException;
import freemind.controller.actions.ActionPair;
import freemind.controller.actions.ActorXml;
import freemind.controller.actions.FreemindAction;
import freemind.controller.actions.generated.instance.AddLinkXmlAction;
import freemind.controller.actions.generated.instance.XmlAction;
import freemind.modes.MindMapNode;
import freemind.modes.ModeController;
import freemind.modes.NodeAdapter;

public class SetLinkByTextFieldAction extends FreemindAction implements ActorXml {

    private final ModeController controller;

    public SetLinkByTextFieldAction(ModeController controller) {
        super("set_link_by_textfield", (String) null, controller);
        this.controller = controller;
        addActor(this);
    }

    public void actionPerformed(ActionEvent e) {
        String inputValue = JOptionPane.showInputDialog(controller.getText("edit_link_manually"), controller.getSelected().getLink());
        if (inputValue != null) {
            if (inputValue.equals("")) {
                inputValue = null;
            }
            setLink(controller.getSelected(), inputValue);
        }
    }

    public void setLink(MindMapNode node, String link) {
        controller.getActionFactory().startTransaction((String) getValue(NAME));
        controller.getActionFactory().executeAction(getActionPair(node, link));
        controller.getActionFactory().endTransaction((String) getValue(NAME));
    }

    public void act(XmlAction action) {
        if (action instanceof AddLinkXmlAction) {
            AddLinkXmlAction linkAction = (AddLinkXmlAction) action;
            NodeAdapter node = controller.getNodeFromID(linkAction.getNode());
            node.setLink(linkAction.getDestination());
            controller.nodeChanged(node);
        }
    }

    public Class getDoActionClass() {
        return AddLinkXmlAction.class;
    }

    private ActionPair getActionPair(MindMapNode node, String link) {
        return new ActionPair(createAddLinkXmlAction(node, link), createAddLinkXmlAction(node, node.getLink()));
    }

    private AddLinkXmlAction createAddLinkXmlAction(MindMapNode node, String link) {
        try {
            AddLinkXmlAction action = controller.getActionXmlFactory().createAddLinkXmlAction();
            action.setNode(node.getObjectId(controller));
            action.setDestination(link);
            return action;
        } catch (JAXBException e) {
            e.printStackTrace();
            return null;
        }
    }
}
