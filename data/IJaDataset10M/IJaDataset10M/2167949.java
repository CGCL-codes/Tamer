package fbench.graph.popup;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import javax.swing.JMenuItem;
import javax.swing.JPopupMenu;
import javax.swing.border.BevelBorder;
import fbench.FBench;
import fbench.IconFactory;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

/**
 * Context menu for GraphView.</br>
 * List of elements that support Context menu is in <b>enum SupportedElement<b> 
 * @author JP
 * @version 20070530/JP - Provides ContextMenu for Parameter as well
 * @version 20070222/JP
 */
@SuppressWarnings("serial")
public class ContextMenu extends JPopupMenu implements ActionListener {

    private MouseEvent mouseEvt;

    private Element element;

    private boolean isInternalVarAllowed = false;

    public enum SupportedElement {

        /**  DRType represents DeviceType and ResourceType  */
        FBType, DRType, Event, VarDeclaration, FBNetwork, FB, Connection, ECC, HCECC, ECState, ECTransition, Algorithm, System, Device, Parameter
    }

    private static SupportedElement elementType;

    /**
	 * ContextMenu for GraphView.  It receives GraphModel element and
	 * creates an appropriate ContextMenu.  
	 * @param elem GraphModel element of the current GraphView
	 * @param evt MouseEvent from right-click
	 */
    public ContextMenu(Element elem, MouseEvent evt) {
        super();
        mouseEvt = evt;
        element = elem;
        setBorder(new BevelBorder(BevelBorder.RAISED));
        NodeList basicFBNL = element.getElementsByTagName("BasicFB");
        if (basicFBNL.getLength() == 0) isInternalVarAllowed = false; else isInternalVarAllowed = true;
    }

    /**
	 * Using the modelName parameter, add appropriate components onto the empty context menu.
	 * @param modelName String variable containing the name of current GraphModel
	 */
    public void oldcreate(String elemType) {
        System.out.println("ContextMenu." + elemType);
        setElementType(elemType);
        System.out.println("ContextMenu." + elementType.toString());
        if (elementType == null) {
            System.err.println("No context menu for " + element.getNodeName());
            return;
        }
        switch(elementType) {
            case FBType:
                JMenuItem addInputEvent = new JMenuItem("Add Input Event", IconFactory.getIcon("evin.gif"));
                addInputEvent.addActionListener(this);
                add(addInputEvent);
                JMenuItem addOutputEvent = new JMenuItem("Add Output Event", IconFactory.getIcon("evout.gif"));
                addOutputEvent.addActionListener(this);
                add(addOutputEvent);
                addSeparator();
            case DRType:
                JMenuItem addInputVar = new JMenuItem("Add Input Variable", IconFactory.getIcon("varin.gif"));
                addInputVar.addActionListener(this);
                add(addInputVar);
                JMenuItem addOutputVar = new JMenuItem("Add Output Variable", IconFactory.getIcon("varout.gif"));
                addOutputVar.addActionListener(this);
                add(addOutputVar);
                JMenuItem addInternalVar = new JMenuItem("Add Internal Variable", IconFactory.getIcon("var.gif"));
                addInternalVar.addActionListener(this);
                add(addInternalVar);
                if (elementType == SupportedElement.FBType) {
                    addInternalVar.setEnabled(isInternalVarAllowed);
                    break;
                } else {
                    addOutputVar.setEnabled(false);
                    addInternalVar.setEnabled(false);
                }
                break;
            case System:
            case Device:
                return;
            case Parameter:
                JMenuItem editParameter = new JMenuItem("Edit", IconFactory.getIcon("Edit16.gif"));
                editParameter.setActionCommand("Edit Parameter");
                editParameter.addActionListener(this);
                add(editParameter);
                JMenuItem deleteParameter = new JMenuItem("Delete", IconFactory.getIcon("Delete16.gif"));
                deleteParameter.setActionCommand("Delete Parameter");
                deleteParameter.addActionListener(this);
                add(deleteParameter);
                break;
            case Event:
                JMenuItem editEvent = new JMenuItem("Edit", IconFactory.getIcon("Edit16.gif"));
                editEvent.setActionCommand("Edit Event");
                editEvent.addActionListener(this);
                add(editEvent);
                JMenuItem deleteEvent = new JMenuItem("Delete", IconFactory.getIcon("Delete16.gif"));
                deleteEvent.setActionCommand("Delete Event");
                deleteEvent.addActionListener(this);
                add(deleteEvent);
                break;
            case VarDeclaration:
                JMenuItem editVar = new JMenuItem("Edit", IconFactory.getIcon("Edit16.gif"));
                editVar.setActionCommand("Edit Variable");
                editVar.addActionListener(this);
                add(editVar);
                JMenuItem deleteVar = new JMenuItem("Delete", IconFactory.getIcon("Delete16.gif"));
                deleteVar.setActionCommand("Delete Variable");
                deleteVar.addActionListener(this);
                add(deleteVar);
                break;
            case FBNetwork:
                JMenuItem addFB = new JMenuItem("Add Function Block", IconFactory.getIcon("fbicon.gif"));
                addFB.addActionListener(this);
                add(addFB);
                JMenuItem addConnection = new JMenuItem("Add Connection", IconFactory.getIcon("3segs.gif"));
                addConnection.addActionListener(this);
                add(addConnection);
                break;
            case FB:
                JMenuItem editFB = new JMenuItem("Edit", IconFactory.getIcon("Edit16.gif"));
                editFB.setActionCommand("Edit Function Block");
                editFB.addActionListener(this);
                add(editFB);
                JMenuItem deleteFB = new JMenuItem("Delete", IconFactory.getIcon("Delete16.gif"));
                deleteFB.setActionCommand("Delete Function Block");
                deleteFB.addActionListener(this);
                add(deleteFB);
                break;
            case Connection:
                JMenuItem editConnection = new JMenuItem("Edit", IconFactory.getIcon("Edit16.gif"));
                editConnection.setActionCommand("Edit Connection");
                editConnection.addActionListener(this);
                add(editConnection);
                JMenuItem deleteConnection = new JMenuItem("Delete", IconFactory.getIcon("Delete16.gif"));
                deleteConnection.setActionCommand("Delete Connection");
                deleteConnection.addActionListener(this);
                add(deleteConnection);
                break;
            case ECC:
                JMenuItem addState = new JMenuItem("Add State", IconFactory.getIcon("ecstate.gif"));
                add(addState);
                addState.addActionListener(this);
                JMenuItem addTransition = new JMenuItem("Add Transition", IconFactory.getIcon("arrow15r.gif"));
                add(addTransition);
                addTransition.addActionListener(this);
                break;
            case ECState:
                JMenuItem editState = new JMenuItem("Edit State", IconFactory.getIcon("Edit16.gif"));
                editState.setActionCommand("Edit State");
                add(editState);
                editState.addActionListener(this);
                JMenuItem deleteState = new JMenuItem("Delete", IconFactory.getIcon("Delete16.gif"));
                deleteState.setActionCommand("Delete State");
                add(deleteState);
                deleteState.addActionListener(this);
                break;
            case ECTransition:
                JMenuItem editTransition = new JMenuItem("Edit", IconFactory.getIcon("Edit16.gif"));
                editTransition.setActionCommand("Edit Transition");
                add(editTransition);
                editTransition.addActionListener(this);
                JMenuItem deleteTransition = new JMenuItem("Delete", IconFactory.getIcon("Delete16.gif"));
                deleteTransition.setActionCommand("Delete Transition");
                add(deleteTransition);
                deleteTransition.addActionListener(this);
                break;
            case Algorithm:
                JMenuItem addAlgorithm = new JMenuItem("Add Algorithm", IconFactory.getIcon("file_obj.gif"));
                addAlgorithm.setActionCommand("Add Algorithm");
                add(addAlgorithm);
                addAlgorithm.addActionListener(this);
                break;
        }
        show(mouseEvt.getComponent(), mouseEvt.getX() - 3, mouseEvt.getY() - 3);
    }

    public void create(String elemType) {
        String source = mouseEvt.getComponent().getClass().getCanonicalName().toString();
        if ((source == "fbench.tree.DOMTree") && FBench.getTreeMenus().containsKey(elemType)) {
            for (String menuText : FBench.getTreeMenu(elemType)) {
                JMenuItem jItem = new JMenuItem(menuText);
                jItem.setActionCommand(menuText);
                jItem.addActionListener(this);
                add(jItem);
            }
            show(mouseEvt.getComponent(), mouseEvt.getX() - 3, mouseEvt.getY() - 3);
        } else oldcreate(elemType);
    }

    public void actionPerformed(ActionEvent evt) {
        if (FBench.getTreeMenuActions().containsKey(evt.getActionCommand())) {
            java.lang.System.out.println(evt.getActionCommand());
            String classname = FBench.getTreeMenuAction(evt.getActionCommand());
            java.lang.System.out.println(classname);
            try {
                Class dmclass = Class.forName(classname);
                java.lang.System.out.println(dmclass.getSuperclass().toString());
                if (dmclass.getSuperclass().toString().contains("DialogModel")) {
                    java.lang.System.out.println(dmclass.getConstructors()[0].toString());
                    Object obj = dmclass.getConstructors()[0].newInstance(new Object[] { element, mouseEvt });
                    if (obj instanceof DialogModel) {
                        DialogModel dm = (DialogModel) obj;
                        dm.create(evt.getActionCommand());
                    }
                }
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        } else oldactionPerformed(evt);
    }

    private SupportedElement setElementType(String elemType) {
        if (elemType.equals("FBType") || elemType.equals("AdapterType")) elementType = SupportedElement.FBType; else if (elemType.equals("DeviceType") || elemType.equals("ResourceType")) elementType = SupportedElement.DRType; else if (elemType.equals("Event")) elementType = SupportedElement.Event; else if (elemType.equals("VarDeclaration")) elementType = SupportedElement.VarDeclaration; else if (elemType.equals("FBNetwork") || elemType.equals("Resource")) elementType = SupportedElement.FBNetwork; else if (elemType.equals("FB")) elementType = SupportedElement.FB; else if (elemType.equals("Connection")) elementType = SupportedElement.Connection; else if (elemType.equals("ECC")) elementType = SupportedElement.ECC; else if (elemType.equals("HCECC")) elementType = SupportedElement.HCECC; else if (elemType.equals("ECState")) elementType = SupportedElement.ECState; else if (elemType.equals("ECTransition")) elementType = SupportedElement.ECTransition; else if (elemType.equals("System")) elementType = SupportedElement.System; else if (elemType.equals("Device")) elementType = SupportedElement.Device; else if (elemType.equals("Parameter")) elementType = SupportedElement.Parameter; else if (elemType.equals("Algorithm")) elementType = SupportedElement.Algorithm; else elementType = null;
        return elementType;
    }

    public void oldactionPerformed(ActionEvent evt) {
        System.out.println("[CM] = " + evt.getActionCommand());
        DialogModel dialog = null;
        boolean didSwitchPerform = false;
        switch(elementType) {
            case FBType:
            case DRType:
            case Event:
            case VarDeclaration:
                didSwitchPerform = true;
                dialog = new FBTypeDialog(element, mouseEvt);
                break;
            case FBNetwork:
            case FB:
            case Connection:
                didSwitchPerform = true;
                dialog = new FBNetworkDialog(element, mouseEvt);
                break;
            case System:
            case Device:
                return;
            case Parameter:
                didSwitchPerform = true;
                dialog = new ParameterDialog(element, mouseEvt);
            case ECC:
            case ECState:
            case ECTransition:
                didSwitchPerform = true;
                dialog = new ECCDialog(element, mouseEvt);
                break;
            case Algorithm:
                didSwitchPerform = true;
                dialog = new AlgorithmDialog(element, mouseEvt);
                break;
        }
        if (didSwitchPerform) dialog.create(evt.getActionCommand()); else return;
    }
}
