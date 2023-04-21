package gov.usda.gdpc.pedigree;

import edu.uci.ics.jung.graph.*;
import edu.uci.ics.jung.graph.decorators.EdgeShape;
import edu.uci.ics.jung.graph.impl.*;
import edu.uci.ics.jung.utils.*;
import edu.uci.ics.jung.visualization.*;
import edu.uci.ics.jung.visualization.control.*;
import gov.usda.gdpc.*;
import gov.usda.gdpc.browser.*;
import gov.usda.gdpc.gui.*;
import gov.usda.gdpc.util.ResourceLocator;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.util.*;
import javax.swing.*;
import org.apache.commons.collections.functors.TruePredicate;

/**
 *
 * @author terryc
 */
public class PedigreeViewer extends JPanel {

    private Graph myGraph = new DirectedSparseGraph();

    private TaxonParentGroup myData = null;

    private PhenotypeGroup myPhenotypeData = null;

    private java.util.List myPhenotypeCheckBoxes = new ArrayList();

    private VisualizationViewer myVV = null;

    private Layout myLayout = null;

    private Dimension myDimension = new Dimension(800, 800);

    private JMenuBar myMenuBar = new JMenuBar();

    private JMenu myPedigreeMenu = null;

    private JMenu myFileMenu = null;

    private JMenu myViewMenu = null;

    private JMenu myHelpMenu = null;

    private Browser myBrowser = null;

    private final JComboBox myTaxaChoices = new JComboBox();

    private JUNGVertexIconFunction myIconFunction = null;

    private JPanel myPlotChoicesPanel = null;

    private FixedTextField myNodeSizeField = null;

    private JTable myKey = null;

    private JPanel myDataInfoPanel = null;

    /** Creates a new instance of PedigreeViewer */
    public PedigreeViewer(Browser browser) {
        if (browser == null) {
            myBrowser = new Browser();
            init(false);
        } else {
            myBrowser = browser;
            init(true);
        }
    }

    public PedigreeViewer() {
        this(null);
    }

    /**
     * Initializes the Pedigree viewer.
     */
    private void init(boolean browserProvided) {
        getData();
        createMenus(browserProvided);
        setLayout(new BorderLayout());
        JTabbedPane tabPane = new JTabbedPane();
        tabPane.insertTab("Pedigree", null, getMainWorkspace(), null, 0);
        if (!browserProvided) {
            tabPane.insertTab("GDPC", null, myBrowser, null, 1);
        }
        add(tabPane, BorderLayout.CENTER);
        reorganize();
    }

    public void newWindow() {
    }

    private JPanel getMainWorkspace() {
        JPanel result = new JPanel();
        getDataInfoPanel();
        result.setLayout(new BorderLayout());
        myGraph = getSingleTreeGraph((String) null);
        DefaultSettableVertexLocationFunction vertexLocations = new DefaultSettableVertexLocationFunction();
        myLayout = new JUNGPedigreeLayout(myGraph);
        ((AbstractLayout) myLayout).initialize(myDimension, vertexLocations);
        PluggableRenderer renderer = new PluggableRenderer();
        renderer.setEdgeArrowPredicate(TruePredicate.getInstance());
        renderer.setVertexStringer(JUNGVertexStringer.getInstance());
        renderer.setVertexLabelCentering(false);
        renderer.setEdgeShapeFunction(new EdgeShape.Line());
        renderer.setEdgeStringer(JUNGEdgeStringer.getInstance());
        myIconFunction = new JUNGVertexIconFunction(myGraph, renderer);
        GDPCPedigreeConstants.ROLE_KEY.setShown(true);
        myVV = new VisualizationViewer(myLayout, renderer, myDimension);
        EditingModalGraphMouse graphMouse = new EditingModalGraphMouse();
        graphMouse.setVertexLocations(vertexLocations);
        ShapePickSupport shapePickSupport = new ShapePickSupport();
        shapePickSupport.setHasShapes(myIconFunction);
        myVV.setPickSupport(shapePickSupport);
        myVV.setGraphMouse(graphMouse);
        graphMouse.add(new JUNGPedigreePopupGraphMousePlugin(vertexLocations, this));
        graphMouse.setMode(ModalGraphMouse.Mode.PICKING);
        JToolBar toolBar = new JToolBar();
        toolBar.setFloatable(false);
        JPanel top = new JPanel(new FlowLayout(FlowLayout.LEFT));
        top.setAlignmentY(JComponent.CENTER_ALIGNMENT);
        top.add(getTaxaChoicesPanel());
        top.add(getNodeSizePanel());
        top.add(getGlobalToolBar(toolBar));
        result.add(top, BorderLayout.NORTH);
        result.add(myVV, BorderLayout.CENTER);
        result.add(myDataInfoPanel, BorderLayout.EAST);
        return result;
    }

    private JPanel getDataInfoPanel() {
        myDataInfoPanel = new JPanel();
        myDataInfoPanel.setLayout(new BoxLayout(myDataInfoPanel, BoxLayout.Y_AXIS));
        myDataInfoPanel.add(getPlotChoices());
        return myDataInfoPanel;
    }

    private JPanel getNodeSizePanel() {
        JPanel result = new JPanel();
        result.setLayout(new BoxLayout(result, BoxLayout.Y_AXIS));
        JLabel label = new JLabel("Node Size:", JLabel.LEFT);
        JPanel labelPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        labelPanel.add(label);
        result.add(labelPanel);
        myNodeSizeField = new FixedTextField(String.valueOf(JUNGVertexIconFunction.DEFAULT_NODE_SIZE), 7);
        myNodeSizeField.addActionListener(new AbstractAction() {

            public void actionPerformed(ActionEvent e) {
                String str = myNodeSizeField.getText();
                int size = 100;
                try {
                    size = Integer.parseInt(str);
                } catch (Exception ex) {
                    size = -1;
                }
                myIconFunction.setNodeSize(size);
                myVV.restart();
            }
        });
        result.add(myNodeSizeField);
        return result;
    }

    private JPanel getTaxaChoicesPanel() {
        JPanel choices = new JPanel();
        choices.setLayout(new BoxLayout(choices, BoxLayout.Y_AXIS));
        initTaxaComboBox();
        JLabel label = new JLabel("Choose Taxon:", JLabel.LEFT);
        JPanel labelPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        labelPanel.add(label);
        choices.add(labelPanel);
        choices.add(myTaxaChoices);
        return choices;
    }

    private void createMenus(boolean browserProvided) {
        myFileMenu = new JMenu("File");
        myFileMenu.setMnemonic('F');
        myFileMenu.add(ExitAction.getInstance(this));
        myMenuBar.add(myFileMenu);
        myViewMenu = new JMenu("View");
        myViewMenu.setMnemonic('V');
        myViewMenu.add(ShowHideAction.getInstance(this, GDPCPedigreeConstants.TAXON_KEY));
        myViewMenu.add(ShowHideAction.getInstance(this, GDPCPedigreeConstants.ROLE_KEY));
        myViewMenu.add(ShowHideAction.getInstance(this, GDPCPedigreeConstants.RECURRENT_KEY));
        myViewMenu.add(ReorganizeAction.getInstance(this));
        myViewMenu.add(ClearGraphAction.getInstance(this));
        myMenuBar.add(myViewMenu);
        if (!browserProvided) {
            myMenuBar.add(myBrowser.getBrowserMenu());
        }
        myHelpMenu = new JMenu("Help");
        myHelpMenu.setMnemonic('H');
        myHelpMenu.add(AboutAction.getInstance(this));
        myMenuBar.add(myHelpMenu);
    }

    public JMenuBar getMenuBar() {
        return myMenuBar;
    }

    public JMenu getPedigreeMenu() {
        if (myPedigreeMenu == null) {
            myPedigreeMenu = new JMenu("Pedigree");
            myPedigreeMenu.add(myFileMenu);
            myPedigreeMenu.add(myViewMenu);
            myPedigreeMenu.add(myHelpMenu);
            myPedigreeMenu.setMnemonic('P');
        }
        return myPedigreeMenu;
    }

    private JToolBar getGlobalToolBar(JToolBar toolBar) {
        Insets margin = new Insets(0, 5, 0, 5);
        JButton reorgButton = toolBar.add(ReorganizeAction.getInstance(this));
        reorgButton.setText(ReorganizeAction.TEXT);
        reorgButton.setMargin(margin);
        JButton clearButton = toolBar.add(ClearGraphAction.getInstance(this));
        clearButton.setText(ClearGraphAction.TEXT);
        clearButton.setMargin(margin);
        return toolBar;
    }

    public JComponent getPlotChoices() {
        myPlotChoicesPanel = new JPanel();
        myPlotChoicesPanel.setLayout(new BoxLayout(myPlotChoicesPanel, BoxLayout.Y_AXIS));
        addPlotChoices();
        JScrollPane scroll = new JScrollPane(myPlotChoicesPanel);
        scroll.setPreferredSize(new Dimension(200, 200));
        scroll.setMinimumSize(new Dimension(200, 0));
        scroll.setMaximumSize(new Dimension(200, 500));
        scroll.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
        scroll.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
        return scroll;
    }

    private void addPlotChoices() {
        myPhenotypeCheckBoxes.clear();
        if (myPhenotypeData != null) {
            PhenotypeOntologyGroup group = myPhenotypeData.getPhenotypeOntologyGroup();
            Iterator itr = group.iterator();
            while (itr.hasNext()) {
                PhenotypeOntology current = (PhenotypeOntology) itr.next();
                PlotCheckBox newBox = new PlotCheckBox(this, current, "Trait: " + current.toString());
                myPhenotypeCheckBoxes.add(newBox);
                myPlotChoicesPanel.add(newBox);
            }
        }
    }

    private void updatePlotChoices() {
        myPlotChoicesPanel.removeAll();
        addPlotChoices();
    }

    private void updateDataToPlot() {
        boolean noDataToPlot = true;
        Iterator itr = myPhenotypeCheckBoxes.iterator();
        while (itr.hasNext()) {
            PlotCheckBox current = (PlotCheckBox) itr.next();
            if (current.isSelected()) {
                PhenotypeOntology ontology = (PhenotypeOntology) current.getPlotData();
                myIconFunction.setDataToPlot(myPhenotypeData, ontology);
                noDataToPlot = false;
            }
        }
        if (noDataToPlot) {
            ((PluggableRenderer) myVV.getRenderer()).setVertexIconFunction(null);
            if (myKey != null) {
                myDataInfoPanel.remove(myKey);
            }
            myDataInfoPanel.revalidate();
        } else {
            ((PluggableRenderer) myVV.getRenderer()).setVertexIconFunction(myIconFunction);
            if (myKey != null) {
                myDataInfoPanel.remove(myKey);
            }
            myKey = myIconFunction.getKey();
            myDataInfoPanel.add(myKey);
            myDataInfoPanel.revalidate();
        }
    }

    public void reorganize() {
        myDimension = myVV.getSize();
        ((AbstractLayout) myLayout).initialize(myDimension);
        updateDataToPlot();
        myVV.restart();
    }

    public void restartVV() {
        myVV.restart();
    }

    public void repaintVV() {
        myVV.repaint();
    }

    /**
     * Temporary method to get data into pedigree
     * viewer for testing.
     */
    private void getData() {
        try {
        } catch (Exception e) {
            e.printStackTrace();
        }
        myData = myBrowser.getTaxonParentGroup();
        myBrowser.addGDPCListener(new AbstractGDPCListener() {

            public void phenotypeTableChanged(GDPCEvent event) {
                myPhenotypeData = ((PhenotypeTable) myBrowser.getPhenotypeData()).getPhenotypeGroup();
                updatePlotChoices();
            }
        });
    }

    private void updateTaxaChoices() {
        myTaxaChoices.removeAllItems();
        myData = myBrowser.getTaxonParentGroup();
        java.util.List group = myData.getTaxa();
        Taxon[] array = new Taxon[group.size()];
        group.toArray(array);
        Arrays.sort(array);
        for (int i = 0; i < array.length; i++) {
            myTaxaChoices.addItem(array[i]);
        }
    }

    /**
     */
    private void initTaxaComboBox() {
        myTaxaChoices.addActionListener(new AbstractAction() {

            public void actionPerformed(ActionEvent e) {
                JComboBox source = (JComboBox) e.getSource();
                Taxon taxon = (Taxon) source.getSelectedItem();
                updateGraph(taxon);
            }
        });
        updateTaxaChoices();
        myBrowser.addGDPCListener(new AbstractGDPCListener() {

            public void taxonParentWorkingGroupChanged(GDPCEvent event) {
                updateTaxaChoices();
            }
        });
    }

    /**
     * Creates graph of all parents of the
     * given taxon and replaces old graph..
     *
     * @param taxon taxon
     */
    public void updateGraph(Taxon taxon) {
        myDimension = myVV.getSize();
        getSingleTreeGraph(taxon);
        DefaultSettableVertexLocationFunction vertexLocations = new DefaultSettableVertexLocationFunction();
        myLayout = new JUNGPedigreeLayout(myGraph);
        ((AbstractLayout) myLayout).initialize(myDimension, vertexLocations);
        myVV.stop();
        myVV.setGraphLayout(myLayout);
        myVV.restart();
    }

    /**
     * Creates graph of all parents of the
     * given accession and replaces old graph.
     *
     * @param accession accession
     */
    public void updateGraph(String accession) {
        TaxonFilter taxonFilter = new TaxonFilter();
        taxonFilter.addValue(new FilterSingleValue(TaxonProperty.ACCESSION, accession));
        TaxonGroup taxonGroup = new DefaultTaxonGroup(myData.getTaxa());
        TaxonGroup individualGroup = taxonGroup.getTaxonGroup(taxonFilter);
        if (individualGroup.size() == 0) {
            updateGraph((Taxon) null);
        }
        Taxon taxon = (Taxon) individualGroup.get(0);
        updateGraph(taxon);
    }

    /**
     * Removes given taxon from graph.
     *
     * @param taxon taxon to remove
     */
    public void removeTaxon(Taxon taxon) {
        Vertex vertex = (Vertex) myGraph.getUserDatum(taxon);
        if (vertex != null) {
            myGraph.removeVertex(vertex);
            myGraph.removeUserDatum(taxon);
        }
    }

    /**
     * Adds relationship to graph specified by
     * TaxonParent instance.
     *
     * @param relation relationship
     *
     * @return two element array of Taxon.  If vertex
     * for child taxon created by this method, then index zero will hold
     * child.   If vertex for parent created by this method, then
     * index one will hold parent.
     */
    public Taxon[] addRelationship(TaxonParent relation) {
        Taxon child = relation.getChild();
        Taxon parent = relation.getParent();
        String role = relation.getRole();
        Integer recurrent = relation.getRecurrent();
        Taxon[] result = new Taxon[2];
        JUNGTaxonVertex childVertex = (JUNGTaxonVertex) myGraph.getUserDatum(child);
        if (childVertex == null) {
            childVertex = new JUNGTaxonVertex();
            childVertex.setUserDatum(GDPCPedigreeConstants.TAXON_KEY, child, UserData.SHARED);
            myGraph.setUserDatum(child, childVertex, UserData.SHARED);
            myGraph.addVertex(childVertex);
            result[0] = child;
        }
        JUNGTaxonVertex parentVertex = (JUNGTaxonVertex) myGraph.getUserDatum(parent);
        if (parentVertex == null) {
            parentVertex = new JUNGTaxonVertex();
            parentVertex.setUserDatum(GDPCPedigreeConstants.TAXON_KEY, parent, UserData.SHARED);
            myGraph.setUserDatum(parent, parentVertex, UserData.SHARED);
            myGraph.addVertex(parentVertex);
            result[1] = parent;
        }
        try {
            Edge edge = myGraph.addEdge(new DirectedSparseEdge(parentVertex, childVertex));
            edge.addUserDatum(GDPCPedigreeConstants.ROLE_KEY, role, UserData.SHARED);
            if (recurrent.intValue() > 0) {
                edge.addUserDatum(GDPCPedigreeConstants.RECURRENT_KEY, GDPCPedigreeConstants.RECURRENT_STRING, UserData.SHARED);
            }
        } catch (Exception e) {
        }
        return result;
    }

    /**
     * Recursively adds taxa and their relationships to
     * this graph.
     *
     * @param taxon current taxon
     * @param addChildren true if given taxon's children
     * should be added.  false if given taxon's parents
     * should be added.
     * @param recurse true if this should add taxa and
     * relationships until connections exhausted.   false
     * if only one level of children or parents should
     * be added.
     */
    private void addSingleTreeGraph(Taxon taxon, boolean addChildren, boolean recurse) {
        if (taxon == null) {
            return;
        }
        TaxonParentFilter filter = new TaxonParentFilter();
        if (addChildren) {
            filter.addValue(new FilterSingleValue(TaxonParentProperty.PARENT, taxon));
        } else {
            filter.addValue(new FilterSingleValue(TaxonParentProperty.CHILD, taxon));
        }
        TaxonParentGroup currentGroup = myData.getTaxonParentGroup(filter);
        Iterator itr = currentGroup.iterator();
        while (itr.hasNext()) {
            TaxonParent current = (TaxonParent) itr.next();
            Taxon[] addedToGraph = addRelationship(current);
            if (recurse) {
                if (addChildren) {
                    addSingleTreeGraph(current.getChild(), addChildren, recurse);
                } else {
                    addSingleTreeGraph(current.getParent(), addChildren, recurse);
                }
            }
        }
    }

    /**
     * Creates graph of all parents of the
     * given accession.
     *
     * @param accession accession
     *
     * @return graph
     */
    public Graph getSingleTreeGraph(String accession) {
        if (accession == null) {
            return getSingleTreeGraph((Taxon) null);
        }
        TaxonFilter taxonFilter = new TaxonFilter();
        taxonFilter.addValue(new FilterSingleValue(TaxonProperty.ACCESSION, accession));
        TaxonGroup taxonGroup = new DefaultTaxonGroup(myData.getTaxa());
        TaxonGroup individualGroup = taxonGroup.getTaxonGroup(taxonFilter);
        if (individualGroup.size() == 0) {
            clearGraph();
            return myGraph;
        }
        Taxon taxon = (Taxon) individualGroup.get(0);
        return getSingleTreeGraph(taxon);
    }

    /**
     * Recursively adds taxa and their relationships to
     * this graph.
     *
     * @param taxon current taxon
     * @param addChildren true if given taxon's children
     * should be added.  false if given taxon's parents
     * should be added.
     * @param recurse true if this should add taxa and
     * relationships until connections exhausted.   false
     * if only one level of children or parents should
     * be added.
     */
    public void addSingleTreeGraphwRoot(Taxon taxon, boolean addChildren, boolean recurse) {
        if (taxon == null) {
            return;
        }
        JUNGTaxonVertex rootVertex = (JUNGTaxonVertex) myGraph.getUserDatum(taxon);
        if (rootVertex == null) {
            rootVertex = new JUNGTaxonVertex();
            rootVertex.setUserDatum(GDPCPedigreeConstants.TAXON_KEY, taxon, UserData.SHARED);
            myGraph.setUserDatum(taxon, rootVertex, UserData.SHARED);
            myGraph.addVertex(rootVertex);
        }
        addSingleTreeGraph(taxon, addChildren, recurse);
        myIconFunction.resetIcons(myGraph);
        if (myKey != null) {
            myDataInfoPanel.remove(myKey);
        }
        myKey = myIconFunction.getKey();
        myDataInfoPanel.add(myKey);
        myDataInfoPanel.revalidate();
    }

    /**
     * Returns graph of give taxon and all
     * parents.
     *
     * @param taxon taxon
     *
     * @return graph
     */
    private Graph getSingleTreeGraph(Taxon taxon) {
        addSingleTreeGraphwRoot(taxon, false, true);
        return myGraph;
    }

    public void clearGraph() {
        myGraph.removeAllEdges();
        myGraph.removeAllVertices();
        boolean notDone = true;
        while (notDone) {
            try {
                Iterator itr = myGraph.getUserDatumKeyIterator();
                while (itr.hasNext()) {
                    Object current = itr.next();
                    if (current instanceof Taxon) {
                        myGraph.removeUserDatum(current);
                    }
                }
                notDone = false;
            } catch (Exception ex) {
                notDone = true;
            }
        }
        myVV.restart();
    }

    /**
     * Adds data to graph about given taxon.
     *
     * @param taxon taxon
     * @param key key
     * @param data data
     */
    public void addVertexUserDatum(Taxon taxon, JUNGUserDatumKey key, Object data) {
        JUNGTaxonVertex vertex = (JUNGTaxonVertex) myGraph.getUserDatum(taxon);
        vertex.addUserDatum(key, data, UserData.SHARED);
    }

    /**
     * Adds data to graph associated with edge relating
     * given child and parent.
     *
     * @param child child
     * @param parent parent
     * @param key key
     * @param data data
     */
    public void addEdgeUserDatum(Taxon child, Taxon parent, JUNGUserDatumKey key, Object data) {
        JUNGTaxonVertex childVertex = (JUNGTaxonVertex) myGraph.getUserDatum(child);
        JUNGTaxonVertex parentVertex = (JUNGTaxonVertex) myGraph.getUserDatum(parent);
        Iterator itr = parentVertex.getOutEdges().iterator();
        while (itr.hasNext()) {
            Edge current = (Edge) itr.next();
            JUNGTaxonVertex currentVertex = (JUNGTaxonVertex) current.getOpposite(parentVertex);
            if (current == childVertex) {
                current.addUserDatum(key, data, UserData.SHARED);
                return;
            }
        }
    }

    public void setCenterVertexLabels(boolean center) {
        ((PluggableRenderer) myVV.getRenderer()).setVertexLabelCentering(center);
    }

    public void saveSettings() {
        BrowserSettings.getInstance(myBrowser).saveSettings();
        myBrowser.closeConnections();
    }

    public Browser getBrowser() {
        return myBrowser;
    }

    /**
     * main for running stand alone.
     */
    public static void main(String[] args) {
        try {
            PedigreeViewer viewer = new PedigreeViewer();
            JFrame frame = new JFrame("Pedigree Viewer");
            frame.setContentPane(viewer);
            frame.setName("Pedigree Viewer");
            frame.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
            frame.setSize(881, 700);
            frame.setJMenuBar(viewer.getMenuBar());
            frame.addWindowListener(new BasicWindowMonitor(viewer));
            try {
                UIManager.setLookAndFeel(UIManager.getCrossPlatformLookAndFeelClassName());
            } catch (ClassNotFoundException e1) {
                System.err.println("look and feel class not found");
                e1.printStackTrace(System.out);
            } catch (UnsupportedLookAndFeelException e2) {
                System.err.println("unsupported look and feel");
                e2.printStackTrace(System.out);
            } catch (IllegalAccessException e3) {
                System.err.println("illegal access exception");
                e3.printStackTrace(System.out);
            } catch (InstantiationException e4) {
                System.err.println("instantiation exception");
                e4.printStackTrace(System.out);
            }
            SwingUtilities.updateComponentTreeUI(frame);
            frame.setVisible(true);
        } catch (Throwable exception) {
            System.err.println("Exception occurred in main() of gov.usda.gdpc.pedigree.PedigreeViewer");
            exception.printStackTrace(System.out);
        }
    }
}
