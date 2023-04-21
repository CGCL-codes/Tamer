package at.ac.tuwien.j3dvn.view;

import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.EnumSet;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import javax.media.j3d.Bounds;
import javax.media.j3d.BranchGroup;
import javax.media.j3d.Canvas3D;
import javax.media.j3d.Node;
import javax.media.j3d.Group;
import at.ac.tuwien.j3dvn.control.DataConnector;
import at.ac.tuwien.j3dvn.control.RelationConnector;
import at.ac.tuwien.j3dvn.control.VisualConnector;
import com.sun.j3d.utils.pickfast.behaviors.PickMouseBehavior;
import javax.media.j3d.PickInfo;

/**
 * 
 */
public class PickBehavior extends PickMouseBehavior {

    public static enum ClickType {

        ALT, BUTTON1, BUTTON2, BUTTON3, CONTROL, DOUBLECLICK, SHIFT
    }

    ;

    private static class MouseListener extends MouseAdapter {

        private PickBehavior adaptee;

        public MouseListener(PickBehavior adaptee) {
            this.adaptee = adaptee;
        }

        /**
		 * Is called on every mouse click.
		 * 
		 * @param e
		 *            MouseEvent
		 */
        public void mouseClicked(MouseEvent e) {
            adaptee.checkMouseClickEvent(e);
        }
    }

    private static final String NAME_PROPERTY = "name";

    private static final String TITLE_PROPERTY = "title";

    private Canvas3D canvas;

    private String currentTitle;

    private VisualConnector currentVisual;

    private Set<ClickType> edgePick = EnumSet.noneOf(ClickType.class);

    private Map<PickListener, Set<ClickType>> listeners = new HashMap<PickListener, Set<ClickType>>();

    private MouseListener mouseListener = new MouseListener(this);

    private Set<ClickType> nodePick = EnumSet.noneOf(ClickType.class);

    public PickBehavior(Canvas3D canvas, BranchGroup root, Bounds bounds) {
        super(canvas, root, bounds);
        this.canvas = canvas;
        this.canvas.addMouseListener(mouseListener);
        pickCanvas.setMode(PickInfo.PICK_BOUNDS);
        pickCanvas.setTolerance(0);
    }

    /**
	 * Adds a pick listener to the list of pick listeners.  
	 * @param listener
	 * @param conditions
	 */
    public void addPickListener(PickListener listener, ClickType... conditions) {
        Set<ClickType> conditionSet = new HashSet<ClickType>();
        for (ClickType type : conditions) {
            conditionSet.add(type);
        }
        listeners.put(listener, conditionSet);
    }

    /**
	 * Defines how an edge can be selected.
	 * 
	 * @param conditions
	 * @see #defineNodePick(at.ac.tuwien.j3dvn.view.PickBehavior.ClickType[])
	 */
    public void defineEdgePick(ClickType... conditions) {
        definePick(edgePick, conditions);
    }

    /**
	 * Defines how a node can be selected. For example (ClickType.BUTTON1,
	 * ClickType.DOUBLECLICK, ClickType.CONTROL) defines that a node can be
	 * selected by left double clicking on the node while holding down control
	 * key.
	 * 
	 * @param conditions
	 * @see #defineEdgePick(at.ac.tuwien.j3dvn.view.PickBehavior.ClickType[])
	 */
    public void defineNodePick(ClickType... conditions) {
        definePick(nodePick, conditions);
    }

    public void removePickListener(PickListener listener) {
        listeners.remove(listener);
    }

    @Override
    public void updateScene(int xpos, int ypos) {
        updateScene(xpos, ypos, null);
    }

    private void checkMouseClickEvent(MouseEvent e) {
        int xpos = e.getPoint().x;
        int ypos = e.getPoint().y;
        if (e.getButton() != MouseEvent.NOBUTTON) {
            updateScene(xpos, ypos, e);
        }
    }

    private void definePick(Set<ClickType> set, ClickType... conditions) {
        for (ClickType condition : conditions) {
            set.add(condition);
        }
    }

    /**
	 * @param event
	 * @param click
	 */
    private void fireEvent(DataConnector data, Set<ClickType> click, MouseEvent event) {
        for (PickListener listener : listeners.keySet()) {
            Set<ClickType> clicks = listeners.get(listener);
            if (isClick(click, clicks)) listener.picked(data, clicks, event);
        }
    }

    /**
	 * Translates a MouseEvent into a click definition.
	 * 
	 * @param event
	 *            The mouse event.
	 * @return The click definition as seen in event. If event is null, a simple
	 *         left mouse button click is assumed.
	 */
    private Set<ClickType> getClickType(MouseEvent event) {
        Set<ClickType> result = EnumSet.noneOf(ClickType.class);
        if (event == null) result.add(ClickType.BUTTON1); else {
            if (event.getButton() == MouseEvent.BUTTON1) result.add(ClickType.BUTTON1);
            if (event.getButton() == MouseEvent.BUTTON2) result.add(ClickType.BUTTON2);
            if (event.getButton() == MouseEvent.BUTTON3) result.add(ClickType.BUTTON3);
            if (event.getClickCount() == 2) result.add(ClickType.DOUBLECLICK);
            if (event.isShiftDown()) result.add(ClickType.SHIFT);
            if (event.isAltDown()) result.add(ClickType.ALT);
            if (event.isControlDown()) result.add(ClickType.CONTROL);
        }
        return result;
    }

    private boolean isClick(Set<ClickType> clickSet, Set<ClickType> definitionSet) {
        for (ClickType type : definitionSet) {
            if (!clickSet.contains(type)) return false;
        }
        return true;
    }

    /**
	 * @param edge
	 */
    private void pickEdge(VisualRelationConnector edge) {
        currentVisual = edge;
        RelationConnector relation = (RelationConnector) currentVisual.getModelData();
        currentTitle = (String) currentVisual.getProperty(TITLE_PROPERTY);
        if (currentTitle == null) currentTitle = "";
        String edgeName = null;
        try {
            edgeName = (String) relation.getProperty(NAME_PROPERTY);
        } catch (IllegalArgumentException e) {
        }
        if (edgeName == null) edgeName = "";
        String ent1Name;
        String ent2Name;
        StringBuilder stats = new StringBuilder();
        try {
            ent1Name = (String) relation.getEntity1().getProperty(NAME_PROPERTY);
            ent2Name = (String) relation.getEntity2().getProperty(NAME_PROPERTY);
            if ((ent1Name != null) && (ent2Name != null) && (edgeName == "")) {
                stats.append(ent1Name);
                stats.append("-");
                stats.append(ent2Name);
            } else stats.append(edgeName);
        } catch (IllegalArgumentException e) {
            stats.append(edgeName);
        }
        for (String name : relation.propertyNames()) {
            if (!NAME_PROPERTY.equals(name)) {
                stats.append(", ");
                stats.append(name);
                stats.append(":");
                stats.append(relation.getProperty(name).toString());
            }
        }
        try {
            currentVisual.setProperty(TITLE_PROPERTY, stats.toString());
        } catch (IllegalArgumentException e) {
        }
    }

    /**
	 * @param node
	 */
    private void pickNode(VisualEntityConnector node) {
        currentVisual = node;
        DataConnector entity = currentVisual.getModelData();
        currentTitle = (String) currentVisual.getProperty(TITLE_PROPERTY);
        if (currentTitle == null) currentTitle = "";
        String nodeName = (String) entity.getProperty(NAME_PROPERTY);
        if (nodeName == null) nodeName = "";
        StringBuilder stats = new StringBuilder(nodeName);
        for (String name : entity.propertyNames()) {
            if (!NAME_PROPERTY.equals(name)) {
                stats.append(", ");
                stats.append(name);
                stats.append(":");
                stats.append(entity.getProperty(name).toString());
            }
        }
        try {
            currentVisual.setProperty(TITLE_PROPERTY, stats.toString());
        } catch (IllegalArgumentException e) {
        }
    }

    private void updateScene(int xpos, int ypos, MouseEvent event) {
        Set<ClickType> click = getClickType(event);
        if (isClick(click, nodePick) || isClick(click, edgePick)) {
            if (currentVisual != null) {
                try {
                    currentVisual.setProperty(TITLE_PROPERTY, currentTitle);
                } catch (IllegalArgumentException e) {
                }
            }
            pickCanvas.setShapeLocation(xpos, ypos);
            pickCanvas.setTolerance(0);
            PickInfo[] pickInfos = pickCanvas.pickAllSorted();
            if (pickInfos == null) return;
            Node node;
            Node parentNode = null;
            Object userData;
            for (PickInfo pickInfo : pickInfos) {
                node = (Node) pickInfo.getNode();
                parentNode = node;
                while (parentNode != null) {
                    parentNode = parentNode.getParent();
                    if (parentNode instanceof Group) {
                        userData = parentNode.getUserData();
                        if (userData instanceof VisualConnector) {
                            if (isClick(click, nodePick) && (userData instanceof VisualEntityConnector)) pickNode((VisualEntityConnector) userData); else if (isClick(click, edgePick) && (userData instanceof VisualRelationConnector)) pickEdge((VisualRelationConnector) userData);
                            DataConnector data = ((VisualConnector) userData).getModelData();
                            if (data != null) fireEvent(data, click, event);
                            return;
                        }
                    }
                }
            }
        }
    }
}
