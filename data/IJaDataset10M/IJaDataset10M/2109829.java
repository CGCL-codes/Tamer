package org.goet.defaultkit;

import org.goet.gui.*;
import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.tree.*;
import org.goet.datamodel.*;
import org.goet.datamodel.impl.*;
import org.goet.datamodel.reflect.*;
import org.bdgp.swing.event.*;
import java.util.*;

public class DefaultGestureRecognizer extends GestureRecognizer {

    protected static final int MOVE_TYPE = 0;

    protected static final int COPY_TYPE = 1;

    protected static final int DELETE_TYPE = 1;

    protected int gestureType;

    protected class PropertyChoiceListener implements ActionListener {

        Property property;

        int type;

        Node target;

        TreePath[] paths;

        public PropertyChoiceListener(int type, Node target, Property property, TreePath[] paths) {
            this.type = type;
            this.target = target;
            this.paths = paths;
            this.property = property;
        }

        public void actionPerformed(ActionEvent e) {
            completeGesture(type, new NodeProperty(target, property), paths);
        }
    }

    public void press(KeyEvent e, Component origin, TreePath[] sourcePaths) {
        if (e.getKeyCode() == KeyEvent.VK_DELETE) {
            NodePropertyValue[] sources = new NodePropertyValue[sourcePaths.length];
            for (int i = 0; i < sourcePaths.length; i++) {
                NodePropertyValue source = (NodePropertyValue) sourcePaths[i].getLastPathComponent();
                if (source == null) return;
                sources[i] = source;
            }
            boolean destroy = sources.length == 1 && sources[0].getNodeProperty() == null && sources[0].getNodeValue() != null && sources[0].getNodeValue().getReferencedByProperties().size() == 0;
            if (destroy) {
                DataCollection dc = controller.getDataCollection();
                Node node = sources[0].getNodeValue();
                if (node instanceof Property) {
                    for (int i = 0; i < dc.getNodeCount(); i++) {
                        Set values = dc.getNodeAt(i).getPropertyValues((Property) node);
                        if (values != null && values.size() > 0) {
                            System.err.println("property " + node + " has values " + values + " for " + dc.getNodeAt(i));
                            return;
                        }
                    }
                }
            }
            setGesture(new DeleteHistoryItem(sources, destroy));
            completeGesture();
        }
    }

    public boolean classModeDrop(DragEvent e, Node targetNode, NodePropertyValue[] sources) {
        if (ClassClass.getClassClass().isInstance(targetNode)) {
            MacroHistoryItem item = new MacroHistoryItem();
            for (int i = 0; i < sources.length; i++) {
                System.err.println("sources[" + i + "] = " + sources[i]);
                if (sources[i].getValue() instanceof Node) {
                    Node source = sources[i].getNodeValue();
                    Node sourceParent = (sources[i].getNodeProperty() == null ? null : sources[i].getNode());
                    try {
                        DataCollection dc = controller.getDataCollection();
                        if (e.isControlDown() && ClassClass.getClassClass().isInstance(targetNode)) {
                            NodeClass nodeClass = (NodeClass) targetNode;
                            Set valueSet = nodeClass.getAllowedValues();
                            if (valueSet != null && !valueSet.contains(source)) {
                                org.goet.datamodel.List l = (org.goet.datamodel.List) nodeClass.getFirstValue(ClassClass.ALLOWS_VALUE);
                                Property p = new ListIndexPropertyImpl(dc, l.getSize());
                                item.addItem(new CopyHistoryItem(source, new NodeProperty(l, p)));
                                System.err.println("added " + source + " to " + l + " at " + p);
                            }
                        } else if (PropertyClass.getPropertyClass().isInstance(source) && !NodeUtil.isInUse((Property) source, dc)) {
                            item.addItem(OperationUtil.copyProperty((Property) source, dc, (NodeClass) targetNode));
                        } else if (ClassClass.getClassClass().isInstance(source)) {
                            NodeProperty np = new NodeProperty(source, ClassClass.HAS_SUPER_CLASS);
                            item.addItem(new AddHistoryItem(new NodePropertyValue(np, targetNode)));
                        }
                    } catch (OperationUtil.OperationException ex) {
                        System.err.println("Error: " + ex.getMessage());
                    }
                }
            }
            controller.applyHistoryItem(item);
            return true;
        } else System.err.println("in classModeDrop(), couldn't drop here");
        return false;
    }

    public void drop(DragEvent e, Component origin, TreePath targetPath, TreePath[] sourcePaths) {
        Node targetNode = ((NodePropertyValue) targetPath.getLastPathComponent()).getNodeValue();
        NodePropertyValue[] sources = new NodePropertyValue[sourcePaths.length];
        for (int i = 0; i < sourcePaths.length; i++) {
            sources[i] = (NodePropertyValue) sourcePaths[i].getLastPathComponent();
        }
        boolean classMode = (((DefaultEditorKit) controller.getEditorKit()).getClassMode() != DefaultEditorKit.CLASS_MODE_NONE);
        if (classMode) {
            if (classModeDrop(e, targetNode, sources)) return;
        }
        Vector allowedProps = new Vector();
        Iterator it = targetNode.getProperties().iterator();
        while (it.hasNext()) {
            Property prop = (Property) it.next();
            boolean addAllowed = true;
            NodeProperty np = new NodeProperty(targetNode, prop);
            for (int j = 0; j < sources.length; j++) {
                Value val = sources[j].getValue();
                addAllowed = isAddAllowed(np, val);
                if (!addAllowed) {
                    break;
                }
            }
            if (addAllowed) {
                allowedProps.addElement(prop);
            }
        }
        if (allowedProps.size() == 0) {
            System.err.println("nowhere to put this");
            return;
        }
        if (allowedProps.size() == 1) {
            completeGesture(gestureType, new NodeProperty(targetNode, (Property) allowedProps.elementAt(0)), sourcePaths);
        } else {
            String opTitle;
            if (gestureType == MOVE_TYPE) opTitle = "Move"; else if (gestureType == COPY_TYPE) opTitle = "Copy"; else opTitle = "Unknown";
            JPopupMenu menu = new JPopupMenu();
            menu.add(opTitle + " to which property?");
            menu.addSeparator();
            for (int i = 0; i < allowedProps.size(); i++) {
                Property p = (Property) allowedProps.elementAt(i);
                JMenuItem item = new JMenuItem(p.getURI().getID());
                ActionListener listener = new PropertyChoiceListener(gestureType, targetNode, p, sourcePaths);
                item.addActionListener(listener);
                menu.add(item);
            }
            menu.show(origin, e.getX(), e.getY());
        }
    }

    protected void completeGesture(int type, NodeProperty target, TreePath[] sourcePaths) {
        if (type == MOVE_TYPE) {
            NodePropertyValue[] objects = new NodePropertyValue[sourcePaths.length];
            for (int i = 0; i < sourcePaths.length; i++) {
                objects[i] = (NodePropertyValue) sourcePaths[i].getLastPathComponent();
            }
            setGesture(new MoveHistoryItem(objects, target));
        } else if (type == COPY_TYPE) {
            Value[] objects = new Value[sourcePaths.length];
            for (int i = 0; i < sourcePaths.length; i++) {
                objects[i] = ((NodePropertyValue) sourcePaths[i].getLastPathComponent()).getValue();
            }
            setGesture(new CopyHistoryItem(objects, target));
        }
        completeGesture();
    }

    public void completeGesture() {
        super.completeGesture();
    }

    public static boolean isAddAllowed(NodeProperty np, Value value) {
        Set types = NodeUtil.getRestrictedRange(np.getNode().getSignature().getClasses(), np.getProperty());
        if (np.getProperty().isDatatypeProperty() && !(value instanceof TypedValue)) {
            return false;
        }
        System.err.println("looking at " + np.getNode() + "," + np.getProperty());
        if (types.size() == 0) {
            System.err.println("np " + np + " has no types!");
        }
        Iterator it = types.iterator();
        while (it.hasNext()) {
            Type type = (Type) it.next();
            System.err.println("   looking at type " + type);
            if (!NodeUtil.isObjectInstanceOf(value, type)) {
                System.err.println("   failed because " + value + " doesn't match type");
                return false;
            } else {
                System.err.println("*!!* " + value + " IS an instance of " + type);
            }
            if (type instanceof NodeClass) {
                NodeClass nodeClass = (NodeClass) type;
                Set allowed = nodeClass.getAllowedValues();
                if (allowed != null && allowed.contains(value)) return true;
                if (allowed != null && allowed.size() > 0) return false;
            }
        }
        return true;
    }

    public boolean isAllowed(DragEvent e, Component origin, TreePath targetPath, TreePath[] sourcePaths) {
        Object destObj = targetPath.getLastPathComponent();
        if (!(destObj instanceof NodePropertyValue) || ((NodePropertyValue) destObj).getValue() instanceof TypedValue) {
            return false;
        }
        if (e.isShiftDown()) {
            gestureType = MOVE_TYPE;
        } else {
            gestureType = COPY_TYPE;
        }
        for (int i = 0; i < sourcePaths.length; i++) {
            if (!(sourcePaths[i].getLastPathComponent() instanceof NodePropertyValue)) return false;
            NodePropertyValue npv = (NodePropertyValue) sourcePaths[i].getLastPathComponent();
            if (gestureType == MOVE_TYPE && npv.getNodeProperty() == null) return false;
        }
        return true;
    }
}
