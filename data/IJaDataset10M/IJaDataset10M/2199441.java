package com.bbn.vessel.author.graphEditor.editor;

import java.awt.Point;
import java.awt.Rectangle;
import java.awt.event.ActionEvent;
import java.awt.event.InputEvent;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.JMenu;
import javax.swing.JOptionPane;
import javax.swing.JPopupMenu;
import javax.swing.KeyStroke;
import javax.swing.event.MouseInputAdapter;
import javax.swing.event.MouseInputListener;
import com.bbn.vessel.author.graphEditor.routing.ManhattanRouter;
import com.bbn.vessel.author.graphEditor.views.DisplayGroupView;
import com.bbn.vessel.author.graphEditor.views.NodeView;
import com.bbn.vessel.author.graphEditor.views.View;
import com.bbn.vessel.author.logicEditor.editor.searcher.GraphSearcher;
import com.bbn.vessel.author.logicEditor.editor.searcher.SearchMatcher;
import com.bbn.vessel.author.models.Connection;
import com.bbn.vessel.author.models.DisplayGroup;
import com.bbn.vessel.author.models.Graph;
import com.bbn.vessel.author.models.GraphElement;
import com.bbn.vessel.author.models.GraphNode;
import com.bbn.vessel.author.models.GroupNode;
import com.bbn.vessel.author.models.Terminal;
import com.bbn.vessel.author.models.TerminalSpec;
import com.bbn.vessel.author.models.VesselNode;
import com.bbn.vessel.author.undo.SimpleEdit;
import com.bbn.vessel.author.undo.UndoableEdit;
import com.bbn.vessel.author.util.UIHelper;

/**
 * Add group/ungroup and template support to the basic {@link GraphEditor}.
 */
@SuppressWarnings("serial")
public abstract class GroupingGraphEditor extends GraphEditor {

    private final AbstractAction ungroupAction = new ModifyAction("Ungroup") {

        @Override
        public void actionPerformed(ActionEvent e) {
            ungroup();
        }
    };

    private final AbstractAction groupAction = new ModifyAction("Group") {

        @Override
        public void actionPerformed(ActionEvent e) {
            group();
        }
    };

    private final AbstractAction removeNodesFromGroupAction = new ModifyAction("Remove Nodes From Group") {

        @Override
        public void actionPerformed(ActionEvent e) {
            removeNodesFromGroup();
        }
    };

    private final AbstractAction boxAction = new ModifyAction("Box") {

        @Override
        public void actionPerformed(ActionEvent e) {
            box();
        }
    };

    private final AbstractAction unboxAction = new ModifyAction("Unbox") {

        @Override
        public void actionPerformed(ActionEvent e) {
            unbox();
        }
    };

    private final AbstractAction selectAllInBoxAction = new ModifyAction("Select All in Box") {

        @Override
        public void actionPerformed(ActionEvent e) {
            selectAllInBox();
        }
    };

    private final AbstractAction createGroupTemplateAction = new ModifyAction("Create Template From Node") {

        @Override
        public void actionPerformed(ActionEvent e) {
            createGroupTemplate();
        }
    };

    private final AbstractAction showHideGroupArgumentsAction = new ModifyAction("Show/Hide Arguments") {

        @Override
        public void actionPerformed(ActionEvent e) {
            showHideGroupArguments();
        }
    };

    private final AbstractAction showExposeTerminalInGroupNodeAction = new ModifyAction("Show Terminal In Group Node") {

        @Override
        public void actionPerformed(ActionEvent e) {
            showExposeTerminalInGroupNode();
        }
    };

    private final AbstractAction renameGroupNodeTerminalAction = new ModifyAction("Rename Group Node Terminal") {

        @Override
        public void actionPerformed(ActionEvent e) {
            renameGroupNodeTerminal();
        }
    };

    private final AbstractAction viewGroupGraphAction = new AbstractAction("View Group Graph") {

        @Override
        public void actionPerformed(ActionEvent e) {
            viewGroupGraph();
        }
    };

    private final AbstractAction unlockInstructionAction = new AbstractAction("Unlock Instruction Group") {

        @Override
        public void actionPerformed(ActionEvent e) {
            unlockInstructionGroup();
        }
    };

    private final MouseInputListener mouseDoubleClickListener = new MouseInputAdapter() {

        @Override
        public void mouseClicked(MouseEvent e) {
            if (e.getClickCount() != 2) {
                return;
            }
            viewGroupGraph();
        }
    };

    private static final int UNGROUP_MARGIN = 20;

    /**
     * Construct the EditorComponent
     *
     * @param graph
     *            the graph to be edited
     * @param name
     *            the name
     * @param graphEditorFrame
     *            the graphEditorFrame to which theis editor belongs
     */
    public GroupingGraphEditor(Graph graph, String name, GraphEditorFrame graphEditorFrame) {
        super(graph, name, graphEditorFrame);
        getGraphViews().addMouseInputListener(mouseDoubleClickListener);
    }

    protected abstract GraphEditor newInstance(Graph graph, String name);

    /**
     * @param editMenu
     *            menu to be populated. it will already have some stuff in it
     */
    @Override
    public void populateEditMenu(JMenu editMenu) {
        super.populateEditMenu(editMenu);
        editMenu.addSeparator();
        editMenu.add(groupAction);
        groupAction.putValue(Action.ACCELERATOR_KEY, KeyStroke.getKeyStroke(KeyEvent.VK_G, InputEvent.CTRL_DOWN_MASK));
        editMenu.add(ungroupAction);
        ungroupAction.putValue(Action.ACCELERATOR_KEY, KeyStroke.getKeyStroke(KeyEvent.VK_G, InputEvent.CTRL_DOWN_MASK + InputEvent.SHIFT_DOWN_MASK));
        editMenu.add(removeNodesFromGroupAction);
        editMenu.add(unlockInstructionAction);
        removeNodesFromGroupAction.setEnabled(graph.isSubGraph());
        editMenu.add(boxAction);
        boxAction.putValue(Action.ACCELERATOR_KEY, KeyStroke.getKeyStroke(KeyEvent.VK_B, InputEvent.CTRL_DOWN_MASK));
        editMenu.add(unboxAction);
        unboxAction.putValue(Action.ACCELERATOR_KEY, KeyStroke.getKeyStroke(KeyEvent.VK_B, InputEvent.CTRL_DOWN_MASK + InputEvent.SHIFT_DOWN_MASK));
    }

    @Override
    protected boolean isDragSelectable(View view) {
        return !(view instanceof DisplayGroupView);
    }

    @Override
    protected void deleteGraphElement(GraphElement graphElement) {
        if (graphElement instanceof GroupNode) {
            GroupNode groupNode = (GroupNode) graphElement;
            graphEditorFrame.closeEditorFor(groupNode.getGroupGraph());
        }
        if (graphElement instanceof DisplayGroup) {
            DisplayGroup box = (DisplayGroup) graphElement;
            List<GraphNode> tmp = new ArrayList<GraphNode>(box.getGraphNodes());
            for (GraphNode node : tmp) {
                box.removeGraphNode(node);
                addSelectedView(node.getView());
            }
        }
        if (graphElement instanceof GraphNode) {
            GraphNode node = (GraphNode) graphElement;
            DisplayGroup box = node.getDisplayGroup();
            if (box != null) {
                box.removeGraphNode(node);
                if (box.getGraphNodes().isEmpty()) {
                    getGraph().removeGraphElement(box);
                }
            }
        }
    }

    @Override
    protected void updateSelectedViewsActions() {
        super.updateSelectedViewsActions();
        boolean hasNode = false;
        boolean canUngroup = false;
        boolean cannotUngroup = false;
        boolean hasBox = false;
        boolean readOnly = getGraphEditorFrame().isReadOnly(getGraph());
        for (View view : selectedViews) {
            if (view instanceof NodeView) {
                hasNode = true;
                GraphElement node = ((NodeView) view).getGraphElement();
                if (node instanceof GroupNode) {
                    GroupNode groupNode = (GroupNode) node;
                    if (groupNode.isReadOnly()) {
                        cannotUngroup = true;
                    } else {
                        canUngroup = true;
                    }
                }
            }
            hasBox |= (view instanceof DisplayGroupView);
        }
        groupAction.setEnabled(!readOnly && hasNode);
        ungroupAction.setEnabled(!readOnly && canUngroup && !cannotUngroup);
        boxAction.setEnabled(!readOnly && hasNode);
        unboxAction.setEnabled(!readOnly && hasBox);
        removeNodesFromGroupAction.setEnabled(!readOnly && !cannotUngroup);
        if (cannotUngroup) {
            deleteAction.setEnabled(false);
            cutAction.setEnabled(false);
            copyAction.setEnabled(false);
            unlockInstructionAction.setEnabled(!readOnly);
        } else {
            unlockInstructionAction.setEnabled(false);
        }
    }

    private void createGroupTemplate() {
        GroupNode groupNode = findGroupNodeFromSelection();
        if (groupNode == null) {
            return;
        }
        ((GroupingGraphEditorFrame) graphEditorFrame).createGroupTemplate(groupNode);
    }

    private void showHideGroupArguments() {
        for (Iterator<View> i = selectedViews.iterator(); i.hasNext(); ) {
            View view = i.next();
            GraphElement graphElement = view.getGraphElement();
            showShowHideGroupArgumentsDialog(graphElement);
        }
        getLayeredPane().repaint();
    }

    private void showExposeTerminalInGroupNode() {
        for (Iterator<View> i = selectedViews.iterator(); i.hasNext(); ) {
            View view = i.next();
            GraphElement graphElement = view.getGraphElement();
            GroupNode groupNode = getGraph().getContainingGroupNode();
            Terminal terminal = (Terminal) graphElement;
            String defaultName = groupNode.getUniqueExternalNameFor(terminal);
            String selectedName = JOptionPane.showInputDialog(getComponent(), "Specify the external terminal name", defaultName);
            if (selectedName == null) return;
            groupNode.addExternalTerminalForInsideTerminal(terminal, selectedName);
        }
        getLayeredPane().repaint();
    }

    private void renameGroupNodeTerminal() {
        for (Iterator<View> i = selectedViews.iterator(); i.hasNext(); ) {
            View view = i.next();
            GraphElement graphElement = view.getGraphElement();
            GraphNode node = (GraphNode) graphElement;
            Terminal outsideTerminal = node.getTerminal(0);
            TerminalSpec outsideTerminalSpec = outsideTerminal.getTerminalSpec();
            GroupNode groupNode = node.getGraph().getContainingGroupNode();
            Terminal externalTerminal = groupNode.getExternalForOutside(outsideTerminal);
            TerminalSpec externalTerminalSpec = externalTerminal.getTerminalSpec();
            String name = outsideTerminalSpec.getName();
            name = JOptionPane.showInputDialog(getComponent(), "Rename Group Node Terminal", name);
            if (name == null) continue;
            outsideTerminalSpec.setName(name);
            externalTerminalSpec.setName(name);
            graphEditorFrame.setModified(true);
        }
    }

    private void viewGroupGraph() {
        GroupNode groupNode = findGroupNodeFromSelection();
        if (groupNode == null) {
            return;
        }
        viewGroupGraph(groupNode);
    }

    /**
     * show the editor for the group graph of a given group node
     *
     * @param groupNode
     *            the group node
     */
    public void viewGroupGraph(GroupNode groupNode) {
        graphEditorFrame.viewGroupGraph(groupNode);
    }

    private void closeGroupGraph(GroupNode groupNode) {
        GraphEditor graphEditor = graphEditorFrame.getEditor(groupNode.getGroupGraph());
        if (graphEditor == null) {
            return;
        }
        UndoableEdit edit = new SimpleEdit("Close Group Graph", graphEditor.RUN_close(), graphEditorFrame.RUN_addEditor(graphEditor));
        edit.run();
        getGraph().getUndoManager().recordEdit(edit);
    }

    private void removeNodesFromGroup() {
        if (selectedViews.isEmpty()) {
            return;
        }
        getGraph().getUndoManager().startCompositeEdit("Remove Nodes From Group");
        GroupNode containingGroupNode = graph.getContainingGroupNode();
        List<View> viewsToSelect = new ArrayList<View>();
        for (View view : selectedViews) {
            GraphElement graphNode = view.getGraphElement();
            if (graphNode instanceof GraphNode) {
                GraphNode addedNode = containingGroupNode.removeGraphNode((GraphNode) graphNode);
                viewsToSelect.add(addedNode.getView());
            }
        }
        GraphEditor parentGraphEditor = graphEditorFrame.getEditor(containingGroupNode.getGraph());
        if (parentGraphEditor != null) {
            parentGraphEditor.clearSelectedViews();
            for (View v : viewsToSelect) {
                parentGraphEditor.addSelectedView(v);
            }
            graphEditorFrame.bringEditorToFront(parentGraphEditor);
        }
        getGraph().getUndoManager().finishCompositeEdit();
    }

    private void unlockInstructionGroup() {
        GroupNode groupNode = findGroupNodeFromSelection();
        if (groupNode == null) {
            return;
        }
        if (!groupNode.isReadOnly()) {
            return;
        }
        SearchMatcher<VesselNode> matcher = new SearchMatcher<VesselNode>() {

            @Override
            public boolean matches(VesselNode elt) {
                return elt instanceof GroupNode;
            }
        };
        Collection<VesselNode> nodes = GraphSearcher.findNodes(matcher, groupNode.getGroupGraph(), true, false);
        for (VesselNode vesselNode : nodes) {
            ((GroupNode) vesselNode).setUserEdited();
            getGraphEditorFrame().clearReadOnlyCache(groupNode.getGroupGraph());
        }
        groupNode.setUserEdited();
        getGraphEditorFrame().clearReadOnlyCache(groupNode.getGroupGraph());
    }

    private void ungroup() {
        GroupNode groupNode = findGroupNodeFromSelection();
        if (groupNode == null) {
            return;
        }
        getGraph().getUndoManager().startCompositeEdit("Ungroup");
        clearSelectedViews();
        ungroup(groupNode);
        getGraph().getUndoManager().finishCompositeEdit();
    }

    /**
     * flatten a group node into its parent graph
     *
     * @param groupNode
     *            the group node to flatten
     */
    public void ungroup(final GroupNode groupNode) {
        closeGroupGraph(groupNode);
        Graph groupGraph = groupNode.getGroupGraph();
        HashSet<GraphElement> graphElementsToAdd = new HashSet<GraphElement>();
        HashSet<GraphNode> terminalNodes = new HashSet<GraphNode>();
        for (GraphNode node : groupGraph.getNodes()) {
            if (!(node.getNodeSpec().getType().equals(GroupNode.GROUP_TERMINAL_NODE_TYPE))) {
                graphElementsToAdd.add(node);
            } else {
                terminalNodes.add(node);
            }
        }
        for (Connection connection : groupGraph.getConnections()) {
            if ((!(connection.getTo().getGraphNode().getNodeSpec().getType().equals(GroupNode.GROUP_TERMINAL_NODE_TYPE))) && (!(connection.getFrom().getGraphNode().getNodeSpec().getType().equals(GroupNode.GROUP_TERMINAL_NODE_TYPE)))) {
                graphElementsToAdd.add(connection);
            }
        }
        Rectangle groupDestinationBoundingBox = new Rectangle(groupNode.getView().getBounds().getLocation(), UIHelper.getBoundsOfViewsForGraphElements(graphElementsToAdd, 0).getSize());
        moveNodesOutOfWayForUngroup(groupDestinationBoundingBox, groupNode);
        Map<GraphNode, GraphNode> duplicateMap = graph.addDuplicates(graphElementsToAdd, groupDestinationBoundingBox.getLocation());
        for (GraphNode terminalNode : terminalNodes) {
            for (int i = 0; i < terminalNode.getTerminalCount(); i++) {
                HashSet<Terminal> insideTerminalsForNode = new HashSet<Terminal>();
                Terminal outsideTerminal = terminalNode.getTerminal(i);
                for (Connection insideConnection : outsideTerminal.getConnections()) {
                    final Terminal oldInsideTerminal = insideConnection.getOtherTerminal(outsideTerminal);
                    GraphNode insideNode = duplicateMap.get(oldInsideTerminal.getGraphNode());
                    insideTerminalsForNode.add(insideNode.getTerminal(oldInsideTerminal.getTerminalType(), oldInsideTerminal.getName()));
                }
                Terminal externalTerminal = groupNode.getExternalForOutside(outsideTerminal);
                for (Connection externalConnection : externalTerminal.getConnections()) {
                    Terminal parentGraphTerminal = externalConnection.getOtherTerminal(externalTerminal);
                    for (Terminal insideTerminal : insideTerminalsForNode) {
                        Connection connection = new Connection(insideTerminal, parentGraphTerminal, Connection.Type.SPLINE, graph);
                        graph.addGraphElement(connection);
                    }
                }
            }
        }
        for (GraphNode node : duplicateMap.values()) {
            addSelectedView(node.getView());
        }
        getGraph().removeGraphElement(groupNode);
    }

    private void group() {
        if (selectedViews.isEmpty()) {
            return;
        }
        getGraph().getUndoManager().startCompositeEdit("Group Nodes");
        final GroupNode groupNode = new GroupNode(getGraph());
        getGraph().addGraphElement(groupNode);
        int totalX = 0;
        int totalY = 0;
        int count = 0;
        for (View view : selectedViews) {
            Point ref = view.getReferencePoint();
            totalX += ref.x;
            totalY += ref.y;
            count++;
        }
        Point cm = new Point(ManhattanRouter.toNearestGrid(totalX / count), ManhattanRouter.toNearestGrid(totalY / count));
        getGraphViews().setReferencePoint(groupNode.getView(), cm);
        Rectangle boundingBox = UIHelper.getBoundsOfViews(selectedViews);
        for (View view : selectedViews) {
            GraphElement graphNode = view.getGraphElement();
            if (graphNode instanceof GraphNode) {
                groupNode.addGraphNode((GraphNode) graphNode, boundingBox);
            }
        }
        clearSelectedViews();
        addSelectedView(groupNode.getView());
        getGraph().getUndoManager().finishCompositeEdit();
    }

    private void box() {
        if (selectedViews.isEmpty()) {
            return;
        }
        getGraph().getUndoManager().startCompositeEdit("Create Box");
        DisplayGroup displayGroup = null;
        for (View view : selectedViews) {
            GraphElement graphElement = view.getGraphElement();
            if (graphElement instanceof DisplayGroup) {
                displayGroup = (DisplayGroup) graphElement;
            }
        }
        if (displayGroup == null) {
            displayGroup = getGraph().addDisplayGroup(null);
            getGraphViews().setReferencePoint(displayGroup.getView(), new Point(0, 0));
        }
        Set<GraphNode> nodesToAdd = new HashSet<GraphNode>();
        for (View view : selectedViews) {
            GraphElement graphElement = view.getGraphElement();
            if (graphElement == displayGroup) {
                continue;
            }
            if (graphElement instanceof DisplayGroup) {
                DisplayGroup selectedDisplayGroup = (DisplayGroup) graphElement;
                nodesToAdd.addAll(selectedDisplayGroup.getGraphNodes());
                getGraph().removeGraphElement(selectedDisplayGroup);
            } else if (graphElement instanceof GraphNode) {
                GraphNode graphNode = (GraphNode) graphElement;
                DisplayGroup oldDisplayGroup = graphNode.getDisplayGroup();
                if (oldDisplayGroup != null) {
                    oldDisplayGroup.removeGraphNode(graphNode);
                }
                nodesToAdd.add(graphNode);
            }
        }
        for (GraphNode graphNode : nodesToAdd) {
            displayGroup.addGraphNode(graphNode);
        }
        clearSelectedViews();
        addSelectedView(displayGroup.getView());
        getGraph().getUndoManager().finishCompositeEdit();
    }

    private void unbox() {
        if (selectedViews.isEmpty()) {
            return;
        }
        List<View> tmp = new ArrayList<View>(selectedViews);
        selectedViews.clear();
        for (View view : tmp) {
            if (view instanceof DisplayGroupView) {
                selectedViews.add(view);
            }
        }
        if (!selectedViews.isEmpty()) {
            delete();
        }
        for (View view : tmp) {
            if (!(view instanceof DisplayGroupView)) {
                selectedViews.add(view);
            }
        }
    }

    private void selectAllInBox() {
        if (selectedViews.isEmpty()) {
            return;
        }
        List<DisplayGroup> boxes = new ArrayList<DisplayGroup>(selectedViews.size());
        for (View view : selectedViews) {
            if (view instanceof DisplayGroupView) {
                boxes.add(((DisplayGroupView) view).getGraphElement());
            }
            view.setSelected(false);
        }
        selectedViews.clear();
        for (DisplayGroup box : boxes) {
            for (GraphNode node : box.getGraphNodes()) {
                addSelectedView(node.getView());
            }
        }
        updateSelectedViewsActions();
        getGraphViews().repaint();
    }

    /**
     * @param boundingBox
     *            the area that the newly ungrouped nodes wil take up
     *
     * @param groupNodeToIgnore
     *            the group node that is being ungrouped (and which shouldn't be
     *            taken into consideration since it will shortly be removed
     */
    private void moveNodesOutOfWayForUngroup(Rectangle boundingBox, final GroupNode groupNodeToIgnore) {
        HashSet<GraphNode> nodesToMove = new HashSet<GraphNode>();
        int minY = Integer.MAX_VALUE;
        for (GraphNode node : graph.getNodes()) {
            final Point location = node.getView().getBounds().getLocation();
            if ((node != groupNodeToIgnore) && (boundingBox.getX() <= location.getX()) && (boundingBox.getY() <= location.getY())) {
                nodesToMove.add(node);
                int nodeY = (int) location.getY();
                if (nodeY < minY) {
                    minY = nodeY;
                }
            }
        }
        final int existingNodeOffset = (int) (boundingBox.getHeight() - (minY - boundingBox.getY()) + UNGROUP_MARGIN);
        for (GraphNode node : nodesToMove) {
            final Point referencePoint = node.getView().getReferencePoint();
            Point newReferencePoint = new Point((int) referencePoint.getX(), (int) referencePoint.getY() + existingNodeOffset);
            graph.getGraphViews().setReferencePoint(node.getView(), newReferencePoint);
        }
    }

    private GroupNode findGroupNodeFromSelection() {
        GroupNode ret = null;
        for (View view : selectedViews) {
            GraphElement graphElement = view.getGraphElement();
            if (graphElement instanceof GroupNode) {
                if (ret != null) {
                    logger.error("More than one group selected");
                    break;
                }
                ret = (GroupNode) graphElement;
            }
        }
        return ret;
    }

    /**
     * close this editor
     */
    @Override
    public void close() {
        super.close();
        Iterator<GraphNode> it = getGraph().iterateNodes();
        while (it.hasNext()) {
            GraphNode graphNode = it.next();
            if (graphNode instanceof GroupNode) {
                GroupNode groupNode = (GroupNode) graphNode;
                graphEditorFrame.closeEditorFor(groupNode.getGroupGraph());
            }
        }
    }

    /**
     * @param graphElement
     */
    private void showShowHideGroupArgumentsDialog(GraphElement graphElement) {
        if (graphElement instanceof GroupNode) {
            GroupNode groupNode = (GroupNode) graphElement;
            ShowHideGroupNodeArguments showHideArguments = new ShowHideGroupNodeArguments(groupNode);
            showHideArguments.showDialog();
            return;
        }
    }

    /**
     * Show a context menu for the given view.
     *
     * @param graphElement
     * @param popup
     */
    @Override
    protected void populateContextMenu(GraphElement graphElement, JPopupMenu popup) {
        if (graphElement instanceof GroupNode) {
            popup.add(viewGroupGraphAction);
            popup.add(createGroupTemplateAction);
            popup.add(showHideGroupArgumentsAction);
            popup.add(unlockInstructionAction);
            popup.add(ungroupAction);
        } else if (graphElement instanceof DisplayGroup) {
            popup.add(selectAllInBoxAction);
        } else if (graphElement instanceof GraphNode) {
            GraphNode node = (GraphNode) graphElement;
            if (node.getNodeSpec().getType().equals(GroupNode.GROUP_TERMINAL_NODE_TYPE)) {
                popup.add(renameGroupNodeTerminalAction);
            }
        } else if ((graphElement instanceof Terminal) && (getGraph().isSubGraph()) && (!((Terminal) graphElement).getGraphNode().getNodeSpec().getType().equals(GroupNode.GROUP_TERMINAL_NODE_TYPE))) {
            popup.add(showExposeTerminalInGroupNodeAction);
        }
        if (graph.isSubGraph()) {
            removeNodesFromGroupAction.setEnabled(!graphEditorFrame.isReadOnly(graph));
            popup.add(removeNodesFromGroupAction);
        }
    }
}
