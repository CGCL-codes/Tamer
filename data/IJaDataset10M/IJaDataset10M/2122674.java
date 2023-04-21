package royere.cwi.view;

import royere.cwi.appl.FileCommands;
import royere.cwi.appl.MenuEntry;
import royere.cwi.appl.RFrame;
import royere.cwi.appl.Royere;
import royere.cwi.db.GraphDbMediator;
import royere.cwi.db.OidFactory;
import royere.cwi.framework.*;
import royere.cwi.framework.edit.AddDeleteMessage;
import royere.cwi.framework.edit.AddDeleteMessageHandler;
import royere.cwi.framework.edit.ColourMessage;
import royere.cwi.framework.edit.ColourMessageHandler;
import royere.cwi.framework.edit.EditBundleMessage;
import royere.cwi.framework.edit.EditBundleMessageHandler;
import royere.cwi.framework.edit.EditMessage;
import royere.cwi.framework.edit.EditMessageHandler;
import royere.cwi.framework.edit.EndOfBundleMessage;
import royere.cwi.framework.edit.EndOfBundleMessageHandler;
import royere.cwi.framework.edit.HistoryActionMessage;
import royere.cwi.framework.edit.HistoryActionMessageHandler;
import royere.cwi.framework.edit.MoveMessage;
import royere.cwi.framework.edit.MoveMessageHandler;
import royere.cwi.framework.edit.NoApplicableElementsException;
import royere.cwi.framework.edit.NoChangeException;
import royere.cwi.framework.edit.OpenCloseMessage;
import royere.cwi.framework.edit.OpenCloseMessageHandler;
import royere.cwi.framework.edit.ResizeMessage;
import royere.cwi.framework.edit.ResizeMessageHandler;
import royere.cwi.framework.edit.SelectionMessage;
import royere.cwi.framework.edit.SelectionMessageHandler;
import royere.cwi.framework.edit.StartOfBundleMessage;
import royere.cwi.framework.edit.StartOfBundleMessageHandler;
import royere.cwi.framework.edit.ViewFilterMessage;
import royere.cwi.framework.edit.ViewFilterMessageHandler;
import royere.cwi.framework.edit.ZoomPanMessage;
import royere.cwi.framework.edit.ZoomPanMessageHandler;
import royere.cwi.layout.CompoundLayout;
import royere.cwi.layout.Coordinates;
import royere.cwi.layout.Layout;
import royere.cwi.layout.LayoutManager;
import royere.cwi.layout.LayoutMessage;
import royere.cwi.layout.LayoutMessageHandler;
import royere.cwi.structure.*;
import royere.cwi.util.*;
import royere.cwi.view.interact.ViewPanelInteractor;
import royere.cwi.view.draw.ElementDrawing;
import org.apache.log4j.Logger;
import org.apache.log4j.Priority;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.event.*;
import javax.swing.*;

/**
* Abstract view module. Used by the application level as the only entry
* point to viewing. For further details, see the separate document on
* <a href="../../../RoyereView.html">viewing in Royere</a>.
*
* @author Ivan Herman
* @author yugen
*/
public abstract class View extends TriggerModule implements Keys, ViewMessageHandler, ControlViewMessageHandler, PropertyChangeListener, AddDeleteMessageHandler, ColourMessageHandler, EditMessageHandler, EditBundleMessageHandler, OpenCloseMessageHandler, ResizeMessageHandler, SelectionMessageHandler, ViewFilterMessageHandler, ZoomPanMessageHandler, MoveMessageHandler, HistoryActionMessageHandler {

    /** Debug object.  Logs data to various channels. */
    private static Logger logger = Logger.getLogger("royere.cwi.view.View");

    /** Object Identifier (for OJB persistence) */
    public Integer oid;

    private static Color[][] colours = { new Color[] { Color.blue, Color.red }, new Color[] { Color.blue, Color.blue }, new Color[] { Color.lightGray, Color.red } };

    /**
    * The real default values for the properties should be set in the implementation
    * classes. To be on the safe side, something is set here, though...
    */
    protected void setPropertyDefaults() {
        properties.setProperty(LINEWIDTH, new Float(1.5f));
        properties.setProperty(DEFAULT_NODE_COLOUR, Color.red);
        properties.setProperty(DEFAULT_EDGE_COLOUR, Color.blue);
        properties.setProperty(VIEW_FILTER, null);
        properties.setProperty(CONTEXT_FILTER, null);
    }

    /** The corresponding view panel */
    protected ViewPanel thePanel = null;

    /** The containing JFrame */
    protected JFrame theFrame = null;

    /** Whether to show tool tips in this view */
    public boolean tooltipsEnabled = false;

    /** This view's current tool tip */
    public JToolTip tooltip = null;

    /** Whether all nodes should be aligned to a grid */
    public boolean snapToGrid = false;

    /**
     * A mapping from graphs to file names.
     */
    private HashMap graphToFileNameMap = new HashMap();

    /** 
     * The name of the file where this view obtained the current graph 
     */
    private String theFileName = null;

    /** 
     * History of user activity (edit actions) in this view,
     * one history per graph 
     */
    private HashMap histories = new HashMap();

    /** 
     * History of user activity (edit actions) in this view,
     * against the current graph 
     */
    private History theHistory = null;

    /** Currently selected elements in this view */
    private Selection theSelection = null;

    /** Current selection's Object Identifier (for OJB persistence) */
    public Integer theSelectionOid;

    /** 
     * Currently selected elements in each graph that
     * this view knows about
     */
    private HashMap selections = new HashMap();

    /** 
    * Check data integrity of parameters.  Should only be set
    * to false in certain test case scenarios.
    */
    public boolean validationOn = true;

    /** 
     * Mapping graphs in this view.
     * // TODO: Key off subgraph pair
     */
    public Graph[] mappingGraphs = new Graph[0];

    /**
     * Index into mapping graph array.  Ranges from -1 to length.
     */
    private int mappingGraphIndex = -1;

    /**
    * Return the panel where the drawing takes place
    */
    public JPanel getPanel() {
        return thePanel;
    }

    /**
    * Return the containing JFrame
    */
    public JFrame getFrame() {
        return theFrame;
    }

    /**
    * Return the current graph file name
    */
    public String getFileName() {
        return theFileName;
    }

    /**
    * Return the current view history 
    */
    public History getHistory() {
        return theHistory;
    }

    /**
    * Return the currently selected elements in this view 
    *
    * @returns the currently selected elements in this view 
    */
    public Selection getSelection() {
        return theSelection;
    }

    /**
    * Return the currently selected elements in this view,
    * for the given graph
    *
    * @returns the currently selected elements in this view 
    */
    public Selection getSelection(Graph graph) {
        return (Selection) this.selections.get(graph);
    }

    /**
    * Define the currently selected elements in this view 
    */
    public void setSelection(Graph graph, Selection selection) {
        this.selections.put(graph, selection);
        this.theSelection = selection;
    }

    /** convenience accessor for Oid property */
    public Integer getOid() {
        return this.oid;
    }

    /** convenience accessor for Oid property */
    public void setOid(Integer oid) {
        this.oid = oid;
    }

    /** convenience accessor for graphOid property */
    public Integer getGraphOid() {
        return getGraph().getOid();
    }

    /** convenience accessor for graphOid property */
    public void setGraphOid(Integer graphOid) {
    }

    public Graph getGraph() {
        return ((ViewPanel) getPanel()).getGraph();
    }

    /** convenience accessor for SelectionOid property */
    public Integer getSelectionOid() {
        if (getSelection() == null) {
            if (this.theSelectionOid != null) {
                logger.log(Priority.WARN, "getSelectionOid(): '" + this + "' has no selection, but does have a selection oid");
                return this.theSelectionOid;
            } else {
                logger.log(Priority.ERROR, "getSelectionOid(): '" + this + "' has no selection and no selection oid!");
                return null;
            }
        }
        this.theSelectionOid = getSelection().getOid();
        logger.log(Priority.DEBUG, "getSelectionOid(): '" + this + "' selection oid = " + this.theSelectionOid);
        return this.theSelectionOid;
    }

    /** convenience accessor for SelectionOid property */
    public void setSelectionOid(Integer selectionOid) {
        logger.log(Priority.DEBUG, "setSelectionOid(): '" + this + "' selection oid = " + selectionOid);
        this.theSelectionOid = selectionOid;
        this.theSelection = null;
    }

    /**
    * Advance to the next mapping in this view 
    */
    public void nextMap() {
        if (mappingGraphIndex >= 0 && mappingGraphIndex <= mappingGraphs.length - 1) {
            Graph mapping = mappingGraphs[mappingGraphIndex];
            thePanel.theLayout.setProperty(Keys.OPENED, mapping, Boolean.FALSE);
        }
        if (mappingGraphIndex <= mappingGraphs.length - 1) {
            mappingGraphIndex++;
        }
        if (mappingGraphIndex <= mappingGraphs.length - 1) {
            Graph mapping = mappingGraphs[mappingGraphIndex];
            thePanel.theLayout.setProperty(Keys.OPENED, mapping, Boolean.TRUE);
        }
        redoLayout(thePanel.theGraph, thePanel.theLayout.getName());
    }

    /**
    * Retreat to the previous mapping in this view 
    */
    public void previousMap() {
        if (mappingGraphIndex >= 0 && mappingGraphIndex <= mappingGraphs.length - 1) {
            Graph mapping = mappingGraphs[mappingGraphIndex];
            thePanel.theLayout.setProperty(Keys.OPENED, mapping, Boolean.FALSE);
        }
        if (mappingGraphIndex >= 0) {
            mappingGraphIndex--;
        }
        if (mappingGraphIndex >= 0) {
            Graph mapping = mappingGraphs[mappingGraphIndex];
            thePanel.theLayout.setProperty(Keys.OPENED, mapping, Boolean.TRUE);
        }
        redoLayout(thePanel.theGraph, thePanel.theLayout.getName());
    }

    /**
    * Advance to the next mapping in this view 
    */
    public void setMappings(Graph subgraph1, Graph subgraph2, Graph[] mappingGraphs) {
        logger.log(Priority.DEBUG, "setMappings(): " + mappingGraphs.length + " mappings between '" + subgraph1 + "' from '" + subgraph2 + "'");
        this.mappingGraphs = mappingGraphs;
        this.mappingGraphIndex = -1;
        nextMap();
    }

    /**
    * Return the status area. This is a status line which has to be managed somehow
    * (if possible) in the enclosing frame. It returns the corresponding status area
    * in the ViewPanel.
    * @see ViewPanel#setStatusArea
    */
    public JTextField setStatusArea() {
        return thePanel.setStatusArea();
    }

    /**
    * Return the Command Area. This is a line where the user can supply
    * text commands for graph editing or view actions.
    */
    public JTextField setCommandArea() {
        return thePanel.setCommandArea();
    }

    /**
    * Return the interaction area. This is a a tabbed pane which collects the
    * various interaction subpanels. It just returns the corresponding area in the
    * View Panel
    * 
    * @see ViewPanel#setInteractorArea
    */
    public JComponent setInteractorArea() {
        return thePanel.setInteractorArea();
    }

    /**
    * The only thing this constructor does is to set the default properties.
    */
    public View() {
        super();
        this.setOid(OidFactory.newOid());
        theHistory = new History(this);
        theSelection = new Selection();
        setPropertyDefaults();
    }

    /**
    * Main entry points to create a new view.
    * Note that the width and height information is necessary used, this depends on the
    * drawing engine. In the case of a clean Java implementation, these values should be simply
    * ignored, because the frame sizes are set by the application layer and all internal panel
    * sizes follow automatically. On the other hand, in some other cases (e.g., Magician interface)
    * the drawing area is a heavyweight canvas whose size has to be given explicitly, and therefore
    * the width and height values are used.
    * <p>
    * Semantically, this routine should belong to the constructor, and should be called by the
    * application right after having constructed by the view. The reason this is not a constructor 
    * is that instances of a specific view might be constructed dynamically (ie, through a Class
    * instance) which allows for constructors withoug arguments only.
    *
    * @param width width of the view
    * @param height height of the view
    */
    public void setUpDrawing(int width, int height) {
        thePanel.setUpDrawing(width, height);
    }

    /**
    * Initialize the underlying frame.
    * <p>
    * Strictly speaking, this method breaks the rules, because it will be executed in the
    * callee's thread. It should be invoked before <em>any</em> message is sent through
    * the pipelines!
    */
    public void initialize(JFrame frame) {
        theFrame = frame;
        thePanel.initialize();
        ((JComponent) theFrame.getGlassPane()).setLayout(null);
        theFrame.getGlassPane().setVisible(tooltipsEnabled);
        tooltip = ((JComponent) theFrame.getGlassPane()).createToolTip();
        tooltip.setBackground(Color.white);
        tooltip.setForeground(Color.black);
        ((JPanel) theFrame.getGlassPane()).add(tooltip);
    }

    /**
    * A PickEvent must be translated to an EditMessage to
    * support undo.  Also, the translation allows us to
    * forward the data to the other modules.
    *
    * @see EditMessage#getInverse
    */
    public void raiseEvent(PickEvent pe) {
        super.raiseEvent(pe);
        EditMessage m = null;
        PickData data = (PickData) pe.getData();
        if ((data.modifiers & InputEvent.ALT_MASK) != 0) {
            m = new OpenCloseMessage(pe, OpenCloseMessage.ACTION_OPEN);
        } else {
            m = new SelectionMessage(pe);
        }
        handleEditMessage(m);
    }

    /**
    * Handle a view message (i.e., a new graph). The message is repeated through
    * the standard module mechanisms, and the buildView call of the panel is
    * invoked
    * @see royere.cwi.framework.TargetControl#sendMessage
    * @see ViewPanel#buildView
    * @param message the new message instance
    */
    public void handleViewMessage(ViewMessage message) {
        Graph graph = message.getGraph();
        Layout layout = message.getLayout();
        String fileName = message.getFileName();
        if (graph == null) {
            MenuEntry entry = MenuEntry.getMenuEntry("Open...");
            if (entry != null) {
                logger.log(Priority.DEBUG, "handleViewMessage(): Selecting menu item");
                entry.choose((RFrame) thePanel.getFrame(), null);
            }
            return;
        }
        java.sql.Timestamp now = new java.sql.Timestamp(System.currentTimeMillis());
        logger.log(Priority.DEBUG, "handleViewMessage(): Graph '" + graph + "' last viewed at: " + now);
        graph.setLastViewedTime(now);
        logger.log(Priority.DEBUG, "handleViewMessage(): " + " from '" + message.getSource() + "', fileName = '" + fileName + "', " + " graph = '" + graph + "'" + " with " + graph.getNumberOfNodes() + " nodes and" + " with " + graph.getNumberOfEdges() + " edges ");
        if (fileName != null) {
            theFileName = fileName;
            graphToFileNameMap.put(graph, fileName);
        }
        if (this.selections.get(graph) == null) {
            GraphDbMediator.readViewState(this, graph);
        }
        theHistory = (History) this.histories.get(graph);
        if (theHistory == null) {
            logger.log(Priority.DEBUG, "handleViewMessage()" + " Creating new history for graph '" + graph + "'");
            theHistory = new History(this);
        }
        this.histories.put(graph, theHistory);
        theSelection = (Selection) this.selections.get(graph);
        if (theSelection == null) {
            logger.log(Priority.DEBUG, "handleViewMessage()" + " Creating new selection for graph '" + graph + "'");
            theSelection = new Selection();
        }
        this.selections.put(graph, theSelection);
        theSelection.setSearchScope(graph);
        thePanel.buildView(graph, layout);
        System.gc();
        logger.log(Priority.DEBUG, "handleViewMessage(): Total memory: " + Runtime.getRuntime().totalMemory());
        logger.log(Priority.DEBUG, "handleViewMessage(): Free memory: " + Runtime.getRuntime().freeMemory());
        logger.log(Priority.DEBUG, "handleViewMessage(): Used memory: " + (Runtime.getRuntime().totalMemory() - Runtime.getRuntime().freeMemory()));
        notifyViewReady();
        sendMessage(new ViewMessage(this, graph, layout));
    }

    /**
    * Add a view message handler.
    * @see royere.cwi.framework.TargetControl
    */
    public void addViewMessageHandler(ViewMessageHandler handler) {
        addMessageHandler(handler, "royere.cwi.view.ViewMessage");
    }

    /**
    * Remove a view message handler.
    * @see royere.cwi.framework.TargetControl
    */
    public void removeViewMessageHandler(ViewMessageHandler handler) {
        removeMessageHandler(handler, "royere.cwi.view.ViewMessage");
    }

    /**
    * Add a pick event listener
    * @see royere.cwi.framework.TargetControl
    */
    public void addPickEventListener(PickEventListener h) {
        addEventListener(h, "royere.cwi.util.PickEvent");
    }

    /**
    * Remove a pick event listener
    * @see royere.cwi.framework.TargetControl
    */
    public void removePickEventListener(PickEventListener h) {
        removeEventListener(h, "royere.cwi.util.PickEvent");
    }

    /**
    * Simulates a PickEvent, but reads the pick data from a 
    * SelectionMessage, sent from another Module.
    *
    * @see SelectionMessage 
    * @exception NoApplicableElementsException if the message will not 
    * change the status of any elements
    */
    public synchronized void handleSelectionMessage(SelectionMessage m) throws NoApplicableElementsException {
        logger.log(Priority.DEBUG, "handleSelectionMessage() from '" + m.getSource() + "'");
        logger.log(Priority.DEBUG, "handleSelectionMessage(): " + m);
        sendMessage(m);
        PickData data = m.getPickData();
        if (data == null) return;
        m.setPreviousSelection((Graph) getSelection().getGraph().clone());
        ArrayList unchangedElements = new ArrayList();
        if (thePanel != null && thePanel instanceof ViewPanel) {
            boolean repaint = false;
            ((ViewPanel) thePanel).unhighlightSelection(repaint);
            switch(m.getAction()) {
                case SelectionMessage.ACTION_SELECT:
                    unchangedElements = getSelection().select(data, m.getAppend());
                    break;
                case SelectionMessage.ACTION_TOGGLE:
                    unchangedElements = getSelection().toggle(data);
                    break;
                case SelectionMessage.ACTION_UNSELECT:
                    unchangedElements = getSelection().unselect(data);
                    break;
            }
            repaint = true;
            ((ViewPanel) thePanel).highlightSelection(repaint);
        }
        logger.log(Priority.DEBUG, "handleSelectionMessage(): '" + unchangedElements + "'");
        notifyViewReady();
    }

    /**
     * Filter out any nodes that meet the given criteria.
     */
    public synchronized void handleViewFilterMessage(ViewFilterMessage m) {
        logger.log(Priority.DEBUG, "handleViewFilterMessage() from '" + m.getSource() + "'");
        logger.log(Priority.DEBUG, "handleViewFilterMessage(): " + m);
        sendMessage(m);
        m.setPreviousFilter((Selector) this.properties.getProperty(VIEW_FILTER));
        Selector filter = m.getSelector();
        this.properties.setProperty(VIEW_FILTER, filter);
        ((ViewPanel) thePanel).refresh();
        notifyViewReady();
    }

    /**
    * Open (or close) any metanodes contained in the given pick data.    
    * Generates a LayoutMessage with every node's OPENED property updated,
    * and then gives the LayoutMessage to the LayoutManager to compute
    * positions (e.g., in subgraphs in newly opened nodes).  
    *
    * @exception NoApplicableElementsException if the message will not 
    * change the status of any elements
    * @see OpenCloseMessage 
    * @see LayoutMessage 
    * @see LayoutManager
    */
    public void handleOpenCloseMessage(OpenCloseMessage ocm) throws NoApplicableElementsException {
        logger.log(Priority.DEBUG, "handleOpenCloseMessage(), from '" + ocm.getSource() + "'");
        sendMessage(ocm);
        PickData data = ocm.getPickData();
        logger.log(Priority.DEBUG, "handleOpenCloseMessage(): '" + data + "'");
        if (!validatePickData(data)) {
            throw new NoApplicableElementsException();
        }
        boolean open = (ocm.getAction() == OpenCloseMessage.ACTION_OPEN);
        int metanodeCount = 0;
        ArrayList unchangedElements = new ArrayList();
        if (data.theNodes != null && data.theNodes.length > 0) {
            for (int i = 0; i < data.theNodes.length; i++) {
                Node theNode = (Node) data.theNodes[i].e;
                if (theNode instanceof Graph) {
                    boolean opened = data.theLayout.isPropertyTrue(Keys.OPENED, (Graph) theNode);
                    if (open == opened) {
                        logger.log(Priority.DEBUG, "handleOpenCloseMessage(): Ignoring node '" + theNode + "'");
                        unchangedElements.add(theNode);
                    } else {
                        if (data.theLayout instanceof CompoundLayout) {
                            boolean recursive = true;
                            ((CompoundLayout) data.theLayout).setProperty(Keys.OPENED, (Graph) theNode, new Boolean(open), recursive);
                        } else {
                            data.theLayout.setProperty(Keys.OPENED, (Graph) theNode, new Boolean(open));
                        }
                        metanodeCount++;
                        logger.log(Priority.DEBUG, "handleOpenCloseMessage(): " + " Set node '" + theNode + "' " + (open ? "OPENED" : "CLOSED") + " for layout " + data.theLayout);
                    }
                }
            }
            try {
                ocm.discardUnchangedElements(unchangedElements);
            } catch (NoApplicableElementsException naee) {
                throw naee;
            }
            logger.log(Priority.DEBUG, "handleOpenCloseMessage(): " + metanodeCount + " metanodes");
            if (metanodeCount > 0) {
                redoLayout(data.theGraph, data.theLayout.getName());
            } else {
                throw new NoApplicableElementsException();
            }
        }
    }

    /**
     * Force a re-layout of the given graph by sending a 
     * LayoutMessage through the pipeline.
     */
    public void redoLayout(Graph graph, String layoutName) {
        logger.log(Priority.DEBUG, "redoLayout(" + graph + ", " + layoutName + ")");
        becomeViewMessageHandler();
        LayoutMessage message = new LayoutMessage(this, graph, layoutName);
        sendMessage(message);
    }

    /**
    * Add (or delete) any elements contained in the given pick data.    
    * Simply forwards the AddDeleteMessage to the appopriate module
    * in the pipeline, typically the GraphManager.
    *
    * @exception NoApplicableElementsException if the message will not 
    * change the status of any elements
    * @see AddDeleteMessage 
    * @see LayoutManager
    */
    public void handleAddDeleteMessage(AddDeleteMessage adm) throws NoApplicableElementsException {
        logger.log(Priority.DEBUG, "handleAddDeleteMessage(), from '" + adm.getSource() + "'");
        PickData data = adm.getPickData();
        logger.log(Priority.DEBUG, "handleAddDeleteMessage(): '" + data + "'");
        if (!validatePickData(data)) {
            throw new NoApplicableElementsException();
        }
        becomeViewMessageHandler();
        logger.log(Priority.DEBUG, "handleAddDeleteMessage(): Sending " + adm);
        sendMessage(adm);
    }

    public boolean validatePickData(PickData data) {
        if (!validationOn) {
            return true;
        }
        if (data == null || data.isEmpty()) {
            return false;
        }
        if (data.theLayout == null) {
            logger.log(Priority.DEBUG, "validatePickData(): PickData has a null layout; using current one");
            data.theLayout = thePanel.theLayout;
        }
        if (data.theLayout == null) {
            logger.log(Priority.ERROR, "validatePickData(): PickData has a null layout");
            return false;
        }
        if (data.theGraph == null) {
            logger.log(Priority.DEBUG, "validatePickData(): PickData has a null graph; using current one");
            data.theGraph = thePanel.theGraph;
        }
        if (data.theGraph == null) {
            logger.log(Priority.ERROR, "validatePickData(): PickData has a null graph");
            return false;
        }
        return true;
    }

    /**
     * Explicitly re-register as a ViewMessageHandler for the LayoutManager, 
     * because the ViewManager typically removes a View as a ViewMessageHandler
     * immediately after the View handles a ViewMessage.  In other words,
     * we were removed after the last ViewMessage we handled, and we need
     * to be ready for subsequent ViewMessages.
     *
     * @see LayoutManager
     * @see royere.cwi.appl.ViewManager.Dismantler#handleViewMessage
     */
    public void becomeViewMessageHandler() {
        LayoutManager layoutManager = (LayoutManager) Royere.ApplicationProperties.getProperty(Royere.LAYOUT);
        if (layoutManager == null) {
            return;
        }
        layoutManager.addViewMessageHandler(this);
    }

    /**
    * Recolor a node or subgraph.
    *
    * @see ColourMessage
    */
    public void handleColourMessage(ColourMessage cm) {
        logger.log(Priority.DEBUG, "handleColourMessage(): " + cm);
        sendMessage(cm);
        PickData data = cm.getPickData();
        logger.log(Priority.DEBUG, "handleColourMessage(): '" + data + "'");
        if (!validatePickData(data)) {
            throw new NoApplicableElementsException();
        }
        Node node = (Node) data.theNodes[0].e;
        Color color = (Color) ElementDrawing.getPredefinedColours().get(cm.getColour());
        if (color == null) {
            logger.log(Priority.ERROR, "handleColourMessage(): Unrecognized colour '" + cm.getColour() + "'");
            throw new NoChangeException();
        }
        if (node instanceof Graph && data.theLayout.isPropertyTrue(Keys.OPENED, node)) {
            Graph graph = (Graph) node;
            logger.log(Priority.DEBUG, "handleColourMessage(): " + "Colouring graph " + graph + " with colour scheme '" + color + "'");
            ClassVisuals graphVisuals = (ClassVisuals) graph.getProperty(Keys.CLASS_VISUALS);
            if (graphVisuals == null) {
                graphVisuals = new ClassVisuals();
            }
            if (graphVisuals.defaultNodeVisuals == null) {
                graphVisuals.defaultNodeVisuals = new Visuals();
            }
            graphVisuals.defaultNodeVisuals.fillColour = color.getRGBComponents(null);
            graphVisuals.defaultNodeVisuals.lineColours = new float[][] { color.getRGBComponents(null) };
            if (graphVisuals.defaultEdgeVisuals == null) {
                graphVisuals.defaultEdgeVisuals = new Visuals();
            }
            graphVisuals.defaultEdgeVisuals.lineColours = new float[][] { color.getRGBComponents(null) };
            graph.setProperty(Keys.CLASS_VISUALS, graphVisuals);
        } else {
            node.setProperty(FILL_COLOUR, cm.getColour());
        }
        ViewPanel panel = (ViewPanel) getPanel();
        panel.setGraphChanged(true);
        panel.displayGraph();
        panel.refresh();
        notifyViewReady();
    }

    /**
    * Multiply the size of a node by the specified factor.
    *
    * @see ResizeMessage
    */
    public void handleResizeMessage(ResizeMessage rm) {
        logger.log(Priority.DEBUG, "handleResizeMessage(): " + rm);
        sendMessage(rm);
        if (Math.abs(rm.getMultiplier() - 1.0) < 0.0001) {
            throw new NoChangeException();
        }
        PickData data = rm.getPickData();
        if (data == null) return;
        Node node = (Node) data.theNodes[0].e;
        double multiplier = node.hasProperty(Keys.NODE_SIZE_MULTIPLIER) ? node.getPropertyAsDouble(Keys.NODE_SIZE_MULTIPLIER) : 1.0;
        multiplier *= rm.getMultiplier();
        node.setProperty(Keys.NODE_SIZE_MULTIPLIER, new Double(multiplier));
        redoLayout(data.theGraph, data.theLayout.getName());
        notifyViewReady();
    }

    /**
    * Tell the ViewPanel to zoom by the specified factor.
    *
    * @see ZoomPanMessage
    */
    public void handleZoomPanMessage(ZoomPanMessage zm) {
        logger.log(Priority.DEBUG, "handleZoomPanMessage(): " + zm);
        sendMessage(zm);
        if (Math.abs(zm.getFactor() - 1.0) < 0.0001 && Math.abs(zm.getDPanX() - 1.0) < 0.0001 && Math.abs(zm.getDPanY() - 1.0) < 0.0001) {
            throw new NoChangeException();
        }
        ViewPanel panel = (ViewPanel) getPanel();
        panel.multiplyZoomFactor(zm.getFactor());
        panel.updatePanXPanY(zm.getDPanX(), zm.getDPanY());
        panel.repaintGraph();
        notifyViewReady();
    }

    /**
    * Get the most recent history message.  If it's a ZoomPanMessage
    * not marked as complete, return it.  Otherwise, create a new 
    * ZoomPanMessage, put it at the end of the history, and return it.
    */
    public ZoomPanMessage getOrCreateLatestZoomPanMessage() {
        EditMessage em = getHistory().getLatest();
        if (em != null && (em instanceof ZoomPanMessage) && !((ZoomPanMessage) em).isComplete()) {
            return (ZoomPanMessage) em;
        }
        em = new ZoomPanMessage(this);
        getHistory().handleEditMessage(em);
        return (ZoomPanMessage) em;
    }

    /**
    * The latest ZoomPanMessage, which may have been constructed from
    * multiple "micro-zoom" or "micro-pan" actions, is now complete
    * and will be treated as atomic for the purposes of undo.  Any 
    * subsequent zoom or pan attempt will have to create a new 
    * ZoomPanMessage, now that this one is complete.
    */
    public void setLatestZoomPanMessageComplete() {
        EditMessage em = getHistory().getLatest();
        if (em != null && (em instanceof ZoomPanMessage)) {
            ((ZoomPanMessage) em).setComplete();
        }
    }

    /**
    * Tell the ViewPanel to move a node.
    *
    * @see MoveMessage
    */
    public void handleMoveMessage(MoveMessage mm) {
        logger.log(Priority.DEBUG, "handleMoveMessage(): " + mm);
        sendMessage(mm);
        Layout layout = thePanel.getGraphLayout();
        Iterator nodeIter = mm.getNodeIterator();
        if (!nodeIter.hasNext()) {
            throw new NoChangeException();
        }
        layout.position(layout.getGraph());
        while (nodeIter.hasNext()) {
            Node node = (Node) nodeIter.next();
            Coordinates newCoord = mm.getNewCoordinates(node);
            if (layout instanceof CompoundLayout) {
                ((CompoundLayout) layout).setCoordinatesRecursive(node, layout.getGraph(), newCoord);
            } else {
                layout.setCoordinates(node, layout.getGraph(), newCoord);
            }
            if (layout instanceof CompoundLayout) {
                ((CompoundLayout) layout).initializeNodeSizeRecursive(node, layout.getGraph());
            }
        }
        HashSet compressableNodes = new HashSet();
        Iterator iter1 = mm.getNodeIterator();
        Iterator iter2 = mm.getNodeIterator();
        while (iter1.hasNext()) {
            Node n1 = (Node) iter1.next();
            boolean skipOver = false;
            while (iter2.hasNext()) {
                Node n2 = (Node) iter2.next();
                if (n2 instanceof Graph && n1.isMember((Graph) n2)) {
                    skipOver = true;
                }
            }
            if (!skipOver) {
                compressableNodes.add(n1);
            }
        }
        if (mm.acquiringGraph != null) {
            double ratio = ((CompoundLayout) layout).getMaxRatio(mm.fixedNode, compressableNodes.iterator(), mm.acquiringGraph);
            ((CompoundLayout) layout).compress(mm.fixedNode, compressableNodes.iterator(), ratio * 1.1);
        }
        thePanel.setGraphChanged(true);
        thePanel.displayGraph();
        thePanel.refresh();
        notifyViewReady();
    }

    /**
    * Helper method so calling methods don't have to do 
    * instanceof.  In particular, History thinks
    * in terms of EditMessages and shouldn't have to
    * check the object type before calling into us.
    *
    * @see History 
    * @see EditMessage 
    */
    public void handleEditMessage(EditMessage m) {
        handleEditMessage(m, true);
    }

    /**
    * Helper method so calling methods don't have to do 
    * instanceof.  In particular, History thinks
    * in terms of EditMessages and shouldn't have to
    * check the object type before calling into us.
    *
    * @see History 
    * @see EditMessage 
    */
    public void handleEditMessage(EditMessage m, boolean store) {
        logger.log(Priority.DEBUG, "handleEditMessage(" + m + "," + store + ") ");
        boolean success = false;
        try {
            if (m instanceof EditBundleMessage) {
                handleEditBundleMessage((EditBundleMessage) m);
                success = true;
            } else {
                synchronized (this) {
                    if (m instanceof StartOfBundleMessage) {
                        handleStartOfBundleMessage((StartOfBundleMessage) m);
                        success = true;
                    }
                    if (m instanceof EndOfBundleMessage) {
                        handleEndOfBundleMessage((EndOfBundleMessage) m);
                        success = true;
                    } else if (m instanceof AddDeleteMessage) {
                        handleAddDeleteMessage((AddDeleteMessage) m);
                        success = true;
                    } else if (m instanceof ColourMessage) {
                        handleColourMessage((ColourMessage) m);
                        success = true;
                    } else if (m instanceof MoveMessage) {
                        handleMoveMessage((MoveMessage) m);
                        success = true;
                    } else if (m instanceof OpenCloseMessage) {
                        handleOpenCloseMessage((OpenCloseMessage) m);
                        success = true;
                    } else if (m instanceof ResizeMessage) {
                        handleResizeMessage((ResizeMessage) m);
                        success = true;
                    } else if (m instanceof SelectionMessage) {
                        handleSelectionMessage((SelectionMessage) m);
                        success = true;
                    } else if (m instanceof ViewFilterMessage) {
                        handleViewFilterMessage((ViewFilterMessage) m);
                        success = true;
                    } else if (m instanceof ZoomPanMessage) {
                        handleZoomPanMessage((ZoomPanMessage) m);
                        success = true;
                    }
                }
            }
        } catch (NoChangeException nce) {
        }
        if (store && success) {
            logger.log(Priority.DEBUG, "handleEditMessage(): storing " + m);
            getHistory().handleEditMessage(m);
        }
    }

    /**
    * Handle a batch of edit actions.
    *
    * @see EditMessage 
    * @see EditBundleMessage 
    */
    public void handleEditBundleMessage(EditBundleMessage ebm) {
        logger.log(Priority.DEBUG, "handleEditBundleMessage() ");
        ArrayList messages = ebm.getMessages();
        if (messages.size() == 0) {
            throw new NoApplicableElementsException();
        }
        Iterator msgIter = messages.iterator();
        while (msgIter.hasNext()) {
            EditMessage m = (EditMessage) msgIter.next();
            handleEditMessage(m, false);
        }
        EndOfBundleMessage eobm = new EndOfBundleMessage(this);
        eobm.setGraph(ebm.getGraph());
        sendMessage(eobm);
    }

    /**
    * Handle the end of a batch of edit actions.
    *
    * @see EditBundleMessage 
    * @see EndOfBundleMessage 
    */
    public void handleEndOfBundleMessage(EndOfBundleMessage m) {
        logger.log(Priority.DEBUG, "handleEndOfBundleMessage()");
        sendMessage(m);
    }

    /**
    * Handle the beginning of a batch of edit actions.
    *
    * @see EditBundleMessage 
    * @see StartOfBundleMessage 
    */
    public void handleStartOfBundleMessage(StartOfBundleMessage m) {
        logger.log(Priority.DEBUG, "handleStartOfBundleMessage()");
        getHistory().handleEditMessage(m);
    }

    /**
    * This is just the hook to add EditMessage handlers.
    */
    public void addEditMessageHandler(EditMessageHandler handler) {
        addMessageHandler(handler, "royere.cwi.framework.edit.EditMessage");
    }

    /**
    * This is just the hook to remove EditMessage handlers.
    */
    public void removeEditMessageHandler(EditMessageHandler handler) {
        removeMessageHandler(handler, "royere.cwi.framework.edit.EditMessage");
    }

    /**
    * This is just the hook to add SelectionMessage handlers.
    */
    public void addSelectionMessageHandler(SelectionMessageHandler handler) {
        addMessageHandler(handler, "royere.cwi.framework.edit.SelectionMessage");
    }

    /**
    * This is just the hook to remove SelectionMessage handlers.
    */
    public void removeSelectionMessageHandler(SelectionMessageHandler handler) {
        removeMessageHandler(handler, "royere.cwi.framework.edit.SelectionMessage");
    }

    /**
    * This is just the hook to add ViewFilterMessage handlers.
    */
    public void addViewFilterMessageHandler(ViewFilterMessageHandler handler) {
        addMessageHandler(handler, "royere.cwi.framework.edit.ViewFilterMessage");
    }

    /**
    * This is just the hook to remove ViewFilterMessage handlers.
    */
    public void removeViewFilterMessageHandler(ViewFilterMessageHandler handler) {
        removeMessageHandler(handler, "royere.cwi.framework.edit.ViewFilterMessage");
    }

    /**
    * This is just the hook to add AddDeleteMessage handlers.
    */
    public void addAddDeleteMessageHandler(AddDeleteMessageHandler handler) {
        logger.log(Priority.DEBUG, "addAddDeleteMessageHandler(): " + handler);
        addMessageHandler(handler, "royere.cwi.framework.edit.AddDeleteMessage");
    }

    /**
    * This is just the hook to remove AddDeleteMessage handlers.
    */
    public void removeAddDeleteMessageHandler(AddDeleteMessageHandler handler) {
        removeMessageHandler(handler, "royere.cwi.framework.edit.AddDeleteMessage");
    }

    /**
    * This is just the hook to add EditBundleMessage handlers.
    */
    public void addEditBundleMessageHandler(EditBundleMessageHandler handler) {
        logger.log(Priority.DEBUG, "addEditBundleMessageHandler(): " + handler);
        addMessageHandler(handler, "royere.cwi.framework.edit.EditBundleMessage");
    }

    /**
    * This is just the hook to remove EditBundleMessage handlers.
    */
    public void removeEditBundleMessageHandler(EditBundleMessageHandler handler) {
        removeMessageHandler(handler, "royere.cwi.framework.edit.EditBundleMessage");
    }

    /**
    * This is just the hook to add EndOfBundleMessage handlers.
    */
    public void addEndOfBundleMessageHandler(EndOfBundleMessageHandler handler) {
        logger.log(Priority.DEBUG, "addEndOfBundleMessageHandler(): " + handler);
        addMessageHandler(handler, "royere.cwi.framework.edit.EndOfBundleMessage");
    }

    /**
    * This is just the hook to remove EndOfBundleMessage handlers.
    */
    public void removeEndOfBundleMessageHandler(EndOfBundleMessageHandler handler) {
        removeMessageHandler(handler, "royere.cwi.framework.edit.EndOfBundleMessage");
    }

    /**
    * This is just the hook to add StartOfBundleMessage handlers.
    */
    public void addStartOfBundleMessageHandler(StartOfBundleMessageHandler handler) {
        logger.log(Priority.DEBUG, "addStartOfBundleMessageHandler(): " + handler);
        addMessageHandler(handler, "royere.cwi.framework.edit.StartOfBundleMessage");
    }

    /**
    * This is just the hook to remove StartOfBundleMessage handlers.
    */
    public void removeStartOfBundleMessageHandler(StartOfBundleMessageHandler handler) {
        removeMessageHandler(handler, "royere.cwi.framework.edit.StartOfBundleMessage");
    }

    /**
    * This is just the hook to add OpenCloseMessage handlers.
    */
    public void addOpenCloseMessageHandler(OpenCloseMessageHandler handler) {
        logger.log(Priority.DEBUG, "addOpenCloseMessageHandler(): " + handler);
        addMessageHandler(handler, "royere.cwi.framework.edit.OpenCloseMessage");
    }

    /**
    * This is just the hook to remove OpenCloseMessage handlers.
    */
    public void removeOpenCloseMessageHandler(OpenCloseMessageHandler handler) {
        removeMessageHandler(handler, "royere.cwi.framework.edit.OpenCloseMessage");
    }

    /**
    * This is just the hook to add ResizeMessage handlers.
    */
    public void addResizeMessageHandler(ResizeMessageHandler handler) {
        logger.log(Priority.DEBUG, "addResizeMessageHandler(): " + handler);
        addMessageHandler(handler, "royere.cwi.framework.edit.ResizeMessage");
    }

    /**
    * This is just the hook to remove ResizeMessage handlers.
    */
    public void removeResizeMessageHandler(ResizeMessageHandler handler) {
        removeMessageHandler(handler, "royere.cwi.framework.edit.ResizeMessage");
    }

    /**
    * This is just the hook to add ColourMessage handlers.
    */
    public void addColourMessageHandler(ColourMessageHandler handler) {
        logger.log(Priority.DEBUG, "addColourMessageHandler(): " + handler);
        addMessageHandler(handler, "royere.cwi.framework.edit.ColourMessage");
    }

    /**
    * This is just the hook to remove ColourMessage handlers.
    */
    public void removeColourMessageHandler(ColourMessageHandler handler) {
        removeMessageHandler(handler, "royere.cwi.framework.edit.ColourMessage");
    }

    /**
    * This is just the hook to add ZoomPanMessage handlers.
    */
    public void addZoomPanMessageHandler(ZoomPanMessageHandler handler) {
        logger.log(Priority.DEBUG, "addZoomPanMessageHandler(): " + handler);
        addMessageHandler(handler, "royere.cwi.framework.edit.ZoomPanMessage");
    }

    /**
    * This is just the hook to remove ZoomPanMessage handlers.
    */
    public void removeZoomPanMessageHandler(ZoomPanMessageHandler handler) {
        removeMessageHandler(handler, "royere.cwi.framework.edit.ZoomPanMessage");
    }

    /**
    * This is just the hook to add MoveMessage handlers.
    */
    public void addMoveMessageHandler(MoveMessageHandler handler) {
        logger.log(Priority.DEBUG, "addMoveMessageHandler(): " + handler);
        addMessageHandler(handler, "royere.cwi.framework.edit.MoveMessage");
    }

    /**
    * This is just the hook to remove MoveMessage handlers.
    */
    public void removeMoveMessageHandler(MoveMessageHandler handler) {
        removeMessageHandler(handler, "royere.cwi.framework.edit.MoveMessage");
    }

    /**
    * This is just the hook to add LayoutMessage handlers.
    */
    public void addLayoutMessageHandler(LayoutMessageHandler handler) {
        logger.log(Priority.DEBUG, "addLayoutMessageHandler(): " + handler);
        addMessageHandler(handler, "royere.cwi.layout.LayoutMessage");
    }

    /**
    * This is just the hook to remove LayoutMessage handlers.
    */
    public void removeLayoutMessageHandler(LayoutMessageHandler handler) {
        removeMessageHandler(handler, "royere.cwi.layout.LayoutMessage");
    }

    /**
    * This is just the hook to add FeedbackTextMessage handlers.
    */
    public void addFeedbackTextMessageHandler(FeedbackTextMessageHandler handler) {
        logger.log(Priority.DEBUG, "addFeedbackTextMessageHandler(): " + handler);
        addMessageHandler(handler, "royere.cwi.framework.FeedbackTextMessage");
    }

    /**
    * This is just the hook to remove FeedbackTextMessage handlers.
    */
    public void removeFeedbackTextMessageHandler(FeedbackTextMessageHandler handler) {
        removeMessageHandler(handler, "royere.cwi.framework.FeedbackTextMessage");
    }

    /**
     * Handle undo, redo, clear history, etc.
     */
    public void handleHistoryActionMessage(HistoryActionMessage ham) {
        try {
            getHistory().handleHistoryActionMessage(ham);
        } catch (NoChangeException nce) {
            notifyViewReady();
        }
    }

    /**
    * Handle a control message. This is a switch using the control message
    * code and the calls in the view panel. The command codes are defined in 
    * the ControlViewMessage class.
    *
    * @see ControlViewMessage
    * @see ViewPanel
    * @param message the control message itself
    */
    public void handleControlViewMessage(ControlViewMessage cvm) {
        int code = cvm.getCode();
        logger.log(Priority.DEBUG, "handleControlViewMessage(): '" + code + "'");
        HistoryActionMessage ham = null;
        AddDeleteMessage adm = null;
        OpenCloseMessage ocm = null;
        Graph g = null;
        Layout layout = null;
        switch(code) {
            case ControlViewMessage.KILL:
                if (thePanel != null) thePanel.destroy();
                thePanel = null;
                break;
            case ControlViewMessage.REGENERATE:
                if (thePanel != null) thePanel.regenerate();
                break;
            case ControlViewMessage.RECENTER:
                if (thePanel != null) thePanel.recenter();
                break;
            case ControlViewMessage.RESET:
                if (thePanel != null) thePanel.reset();
                break;
            case ControlViewMessage.REFRESH:
                g = cvm.getGraph();
                getSelection().setSearchScope(g);
                logger.log(Priority.DEBUG, "handleControlViewMessage(): REFRESH '" + thePanel + "', '" + g + "'");
                if (thePanel != null) {
                    thePanel.refresh(g);
                }
                break;
            case ControlViewMessage.PRINT:
                if (thePanel != null) thePanel.print();
                break;
            case ControlViewMessage.EXPORT:
                if (thePanel != null) {
                    String name = cvm.getName();
                    thePanel.export(name);
                }
                break;
            case ControlViewMessage.CLEAN:
                if (thePanel != null) {
                    thePanel.cleanScreen();
                }
                break;
            case ControlViewMessage.UNDO:
                logger.log(Priority.DEBUG, "handleControlViewMessage(): UNDO");
                ham = new HistoryActionMessage(this, null, HistoryActionMessage.ACTION_UNDO);
                handleHistoryActionMessage(ham);
                break;
            case ControlViewMessage.REDO:
                logger.log(Priority.DEBUG, "handleControlViewMessage(): REDO");
                ham = new HistoryActionMessage(this, null, HistoryActionMessage.ACTION_REDO);
                handleHistoryActionMessage(ham);
                break;
            case ControlViewMessage.DELETE_SELECTION:
                g = cvm.getGraph();
                logger.log(Priority.DEBUG, "handleControlViewMessage(): DELETE selection against '" + g + "'");
                EditBundleMessage ebm = new EditBundleMessage(this, g);
                ebm.setGraph(g);
                layout = thePanel.theLayout;
                HashSet scopes = layout.getGraphs();
                Iterator graphIter = scopes.iterator();
                while (graphIter.hasNext()) {
                    Graph scope = (Graph) graphIter.next();
                    HashSet scopeSet = new HashSet();
                    scopeSet.add(scope);
                    Graph visibleSelection = getSelection().getVisible(scopeSet);
                    if (!visibleSelection.isEmpty()) {
                        adm = new AddDeleteMessage(this, null, AddDeleteMessage.ACTION_DELETE);
                        adm.setGraph(g);
                        adm.setApplicableGraphs(scopeSet);
                        adm.setPickData(PickData.fromElementArray(visibleSelection.toElementArray()));
                        adm.setFinal(false);
                        ebm.addMessage(adm);
                    }
                }
                logger.log(Priority.DEBUG, "handleControlViewMessage(): DELETE selection: '" + ebm.getGraph() + "'");
                handleEditMessage(ebm);
                break;
            case ControlViewMessage.OPEN_SELECTION:
                g = cvm.getGraph();
                logger.log(Priority.DEBUG, "handleControlViewMessage(): OPEN selection against '" + g + "'");
                ocm = new OpenCloseMessage(this, null, OpenCloseMessage.ACTION_OPEN);
                ocm.setGraph(g);
                layout = thePanel.theLayout;
                ocm.setPickData(PickData.fromElementArray(getSelection().getVisible(layout.getGraphs()).toElementArray()));
                handleEditMessage(ocm);
                break;
            case ControlViewMessage.CLOSE_SELECTION:
                g = cvm.getGraph();
                logger.log(Priority.DEBUG, "handleControlViewMessage(): CLOSE selection against '" + g + "'");
                ocm = new OpenCloseMessage(this, null, OpenCloseMessage.ACTION_CLOSE);
                ocm.setGraph(g);
                layout = thePanel.theLayout;
                ocm.setPickData(PickData.fromElementArray(getSelection().getVisible(layout.getGraphs()).toElementArray()));
                handleEditMessage(ocm);
                break;
            case ControlViewMessage.ADD_EDGE:
                thePanel.interactor.mouseDragMode = ViewPanelInteractor.ADD_EDGE;
                break;
        }
    }

    public void catchPropertyChange(PropertyChange event) {
        thePanel.catchPropertyChange(event);
    }

    /**
    * Alert any threads waiting on this View
    *
    * @see CommandLine#execute
    */
    protected synchronized void notifyViewReady() {
        logger.log(Priority.DEBUG, "notifyViewReady()");
        this.notify();
    }
}
