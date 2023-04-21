package uk.ac.rothamsted.ovtk.Filter.combination;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.Image;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.Vector;
import java.util.concurrent.ConcurrentLinkedQueue;
import javax.swing.BoxLayout;
import javax.swing.ButtonGroup;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JEditorPane;
import javax.swing.JFileChooser;
import javax.swing.JInternalFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JScrollPane;
import javax.swing.JSplitPane;
import javax.swing.JTabbedPane;
import javax.swing.JTree;
import javax.swing.ToolTipManager;
import javax.swing.border.EmptyBorder;
import javax.swing.event.HyperlinkEvent;
import javax.swing.event.HyperlinkListener;
import javax.swing.event.TreeSelectionEvent;
import javax.swing.event.TreeSelectionListener;
import javax.swing.tree.TreePath;
import javax.swing.tree.TreeSelectionModel;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;
import uk.ac.rothamsted.ovtk.Console.Console;
import uk.ac.rothamsted.ovtk.Filter.GeneralFilter;
import uk.ac.rothamsted.ovtk.GUI.FilterPropertiesFrame;
import uk.ac.rothamsted.ovtk.GUI.LayoutPropertiesFrame;
import uk.ac.rothamsted.ovtk.GUI.MainDesktop;
import uk.ac.rothamsted.ovtk.GUI.MainFrame;
import uk.ac.rothamsted.ovtk.Graph.Concept;
import uk.ac.rothamsted.ovtk.Graph.Concept_Acc;
import uk.ac.rothamsted.ovtk.Graph.Concept_Name;
import uk.ac.rothamsted.ovtk.Graph.ONDEXGraph;
import uk.ac.rothamsted.ovtk.Graph.Relation;
import uk.ac.rothamsted.ovtk.GraphLibraryAdapter.yfiles.ExportAsGraphMLAction;
import uk.ac.rothamsted.ovtk.GraphLibraryAdapter.yfiles.SetEdgeLabelAction;
import uk.ac.rothamsted.ovtk.GraphLibraryAdapter.yfiles.SetNodeLabelAction;
import uk.ac.rothamsted.ovtk.GraphLibraryAdapter.yfiles.YGraphLibraryAdapter;
import uk.ac.rothamsted.ovtk.IO.ExportAsBeans;
import uk.ac.rothamsted.ovtk.Layouter.GeneralLayouter;
import uk.ac.rothamsted.ovtk.Util.CloseableTabbedPane;
import uk.ac.rothamsted.ovtk.Util.CustomFileFilter;
import uk.ac.rothamsted.ovtk.Util.JarResources;
import y.io.GMLIOHandler;

public class CombinationGUI extends JInternalFrame implements ActionListener, TreeSelectionListener, HyperlinkListener {

    static final int windowHeight = 460;

    static final int leftWidth = 300;

    static final int rightWidth = 340;

    static final int windowWidth = leftWidth + rightWidth;

    static final int ACTUAL = 1;

    static final int SELECTED = 2;

    static final int ALL = 3;

    MainDesktop desktop;

    MainFrame mainFrame;

    ONDEXGraph ondexGraph;

    Container contentPane;

    JComboBox filterBox;

    Vector activeFilters;

    JComboBox layoutBox;

    Vector activeLayouts;

    CombinationGUI gui;

    JTree tree;

    JEditorPane htmlPane;

    JTabbedPane tabPane;

    JPanel pipeline;

    JScrollPane pipelineTab;

    OndexInfoPage infoPage;

    int process = ACTUAL;

    y.io.IOHandler iohGML;

    public CombinationGUI(MainFrame mainframe) {
        super("CombinationFilter");
        this.mainFrame = mainframe;
        this.desktop = mainframe.getDesktop();
        this.ondexGraph = mainframe.getONDEXGraph();
        this.setName("CombinationFrame");
        this.setResizable(true);
        this.setIconifiable(true);
        this.setMaximizable(true);
        contentPane = this.getContentPane();
        contentPane.setLayout(new BorderLayout());
        buildGUI();
        this.pack();
        this.desktop.getJDesktopPane().add(this);
        this.setVisible(true);
        this.gui = this;
        iohGML = new GMLIOHandler();
    }

    public void buildGUI() {
        tabPane = new JTabbedPane();
        pipeline = new JPanel();
        BoxLayout box = new BoxLayout(pipeline, BoxLayout.Y_AXIS);
        pipeline.setLayout(box);
        pipelineTab = new JScrollPane(pipeline);
        tabPane.add("Pipeline", pipelineTab);
        contentPane.add(BorderLayout.NORTH, buildNorth());
        contentPane.add(BorderLayout.CENTER, tabPane);
        contentPane.add(BorderLayout.SOUTH, buildSouth());
    }

    public JPanel buildNorth() {
        JPanel north = new JPanel();
        north.setLayout(new BoxLayout(north, BoxLayout.LINE_AXIS));
        JarResources jar = new JarResources("jlfgr-1_0.jar");
        Image loadimg = Toolkit.getDefaultToolkit().createImage(jar.getResource("toolbarButtonGraphics/general/Open16.gif"));
        Image refreshimg = Toolkit.getDefaultToolkit().createImage(jar.getResource("toolbarButtonGraphics/general/Refresh16.gif"));
        Image addimg = Toolkit.getDefaultToolkit().createImage(jar.getResource("toolbarButtonGraphics/general/Add16.gif"));
        Image saveimg = Toolkit.getDefaultToolkit().createImage(jar.getResource("toolbarButtonGraphics/general/Save16.gif"));
        JLabel label1 = new JLabel("Load XML");
        label1.setBorder(new EmptyBorder(5, 5, 5, 5));
        north.add(label1);
        JButton xmlbutton = new JButton(new ImageIcon(loadimg));
        xmlbutton.setActionCommand("loadxml");
        xmlbutton.addActionListener(this);
        north.add(xmlbutton);
        JLabel label2 = new JLabel("Active Filters");
        label2.setBorder(new EmptyBorder(5, 5, 5, 5));
        north.add(label2);
        filterBox = new JComboBox();
        updateFilterBox();
        north.add(filterBox);
        JButton refreshfilter = new JButton(new ImageIcon(refreshimg));
        refreshfilter.setActionCommand("refreshfilter");
        refreshfilter.addActionListener(this);
        north.add(refreshfilter);
        JButton addfilter = new JButton(new ImageIcon(addimg));
        addfilter.setActionCommand("addfilter");
        addfilter.addActionListener(this);
        north.add(addfilter);
        JLabel label3 = new JLabel("Active Layouts");
        label3.setBorder(new EmptyBorder(5, 5, 5, 5));
        north.add(label3);
        layoutBox = new JComboBox();
        updateLayoutBox();
        north.add(layoutBox);
        JButton refreshlayout = new JButton(new ImageIcon(refreshimg));
        refreshlayout.setActionCommand("refreshlayout");
        refreshlayout.addActionListener(this);
        north.add(refreshlayout);
        JButton addlayout = new JButton(new ImageIcon(addimg));
        addlayout.setActionCommand("addlayout");
        addlayout.addActionListener(this);
        north.add(addlayout);
        JLabel label4 = new JLabel("Pipeline Config");
        label4.setBorder(new EmptyBorder(5, 5, 5, 5));
        north.add(label4);
        JButton loadbutton = new JButton(new ImageIcon(loadimg));
        loadbutton.setActionCommand("loadconfig");
        loadbutton.addActionListener(this);
        north.add(loadbutton);
        JButton savebutton = new JButton(new ImageIcon(saveimg));
        savebutton.setActionCommand("saveconfig");
        savebutton.addActionListener(this);
        north.add(savebutton);
        return north;
    }

    public JSplitPane buildCenter(Document document) {
        int option = JOptionPane.showInternalOptionDialog(this, "Look up target id in Concept accessions?", "Target ID lookup", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE, null, null, null);
        if (option == 0) {
            Hashtable accs = new Hashtable();
            Iterator it = ondexGraph.getConceptIDs().keySet().iterator();
            while (it.hasNext()) {
                Concept c = (Concept) ondexGraph.getConceptIDs().get(it.next());
                String id = c.getId().toLowerCase();
                Iterator it2 = c.getConcept_accs().iterator();
                while (it2.hasNext()) {
                    Concept_Acc ca = (Concept_Acc) it2.next();
                    String acc = ca.getConcept_accession().toLowerCase();
                    if (!accs.containsKey(acc)) {
                        accs.put(acc, new HashSet());
                    }
                    ((HashSet) accs.get(acc)).add(id);
                }
            }
            Element root = document.getDocumentElement();
            NodeList nl = root.getChildNodes();
            for (int i = 0; i < nl.getLength(); i++) {
                Node qNode = nl.item(i);
                NodeList nl2 = qNode.getChildNodes();
                for (int j = 0; j < nl2.getLength(); j++) {
                    Node tNode = nl2.item(j);
                    if (tNode.hasAttributes()) {
                        String tONDEXid = tNode.getAttributes().getNamedItem("ondexid").getNodeValue().toLowerCase();
                        if (accs.containsKey(tONDEXid)) {
                            HashSet set = (HashSet) accs.get(tONDEXid);
                            if (set.size() > 0) {
                                Object[] array = set.toArray();
                                Arrays.sort(array);
                                for (int l = 0; l < array.length; l++) {
                                    Element tElem = document.createElement("target");
                                    tElem.setAttribute("ondexid", (String) array[l]);
                                    tElem.setAttribute("taxid", tNode.getAttributes().getNamedItem("taxid").getNodeValue());
                                    tElem.setAttribute("cv", tNode.getAttributes().getNamedItem("cv").getNodeValue());
                                    tElem.setAttribute("length", tNode.getAttributes().getNamedItem("length").getNodeValue());
                                    qNode.appendChild(tElem);
                                    Element mElem = document.createElement("match");
                                    mElem.setAttribute("score", "0");
                                    mElem.setAttribute("length", "0");
                                    tElem.appendChild(mElem);
                                }
                            }
                        }
                    }
                }
            }
        }
        JarResources jar = new JarResources("jlfgr-1_0.jar");
        Image aboutimg = Toolkit.getDefaultToolkit().createImage(jar.getResource("toolbarButtonGraphics/general/Properties16.gif"));
        tree = new JTree(new DomToTreeModelAdapter(document));
        tree.getSelectionModel().setSelectionMode(TreeSelectionModel.SINGLE_TREE_SELECTION);
        ToolTipManager.sharedInstance().registerComponent(tree);
        ImageIcon icon = new ImageIcon(aboutimg);
        if (icon != null) {
            tree.setCellRenderer(new CustomTreeCellRenderer(ondexGraph, icon));
        }
        JScrollPane treeView = new JScrollPane(tree);
        treeView.setPreferredSize(new Dimension(leftWidth, windowHeight));
        htmlPane = new JEditorPane("text/html", "");
        htmlPane.addHyperlinkListener(this);
        htmlPane.setEditable(false);
        JScrollPane htmlView = new JScrollPane(htmlPane);
        htmlView.setPreferredSize(new Dimension(rightWidth, windowHeight));
        tree.addTreeSelectionListener(this);
        JSplitPane splitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, treeView, htmlView);
        splitPane.setContinuousLayout(true);
        splitPane.setDividerLocation(leftWidth);
        splitPane.setPreferredSize(new Dimension(windowWidth + 10, windowHeight + 10));
        return splitPane;
    }

    public JPanel buildSouth() {
        JPanel south = new JPanel();
        south.setLayout(new BoxLayout(south, BoxLayout.PAGE_AXIS));
        JPanel radioButtons = new JPanel();
        radioButtons.setLayout(new BoxLayout(radioButtons, BoxLayout.LINE_AXIS));
        JLabel label1 = new JLabel("Apply operations to");
        label1.setBorder(new EmptyBorder(5, 5, 5, 5));
        radioButtons.add(label1);
        JRadioButton actualButton = new JRadioButton("actual");
        actualButton.setMnemonic(KeyEvent.VK_C);
        actualButton.setActionCommand("actual");
        actualButton.setSelected(true);
        radioButtons.add(actualButton);
        JRadioButton selectedButton = new JRadioButton("selected");
        selectedButton.setMnemonic(KeyEvent.VK_S);
        selectedButton.setActionCommand("selected");
        radioButtons.add(selectedButton);
        JRadioButton allButton = new JRadioButton("all");
        allButton.setMnemonic(KeyEvent.VK_A);
        allButton.setActionCommand("all");
        radioButtons.add(allButton);
        ButtonGroup group = new ButtonGroup();
        group.add(actualButton);
        group.add(selectedButton);
        group.add(allButton);
        actualButton.addActionListener(this);
        selectedButton.addActionListener(this);
        allButton.addActionListener(this);
        south.add(radioButtons);
        JPanel operations = new JPanel();
        operations.setLayout(new BoxLayout(operations, BoxLayout.LINE_AXIS));
        JButton saveSpreadsheet = new JButton("Spreadsheet Export");
        saveSpreadsheet.setActionCommand("spreadsheet");
        saveSpreadsheet.addActionListener(this);
        operations.add(saveSpreadsheet);
        JButton saveInfoPage = new JButton("InfoPage Export");
        saveInfoPage.setActionCommand("infopage");
        saveInfoPage.addActionListener(this);
        operations.add(saveInfoPage);
        JButton saveGML = new JButton("GML Export");
        saveGML.setActionCommand("gml");
        saveGML.addActionListener(this);
        operations.add(saveGML);
        JButton saveGraphML = new JButton("GraphML Export");
        saveGraphML.setActionCommand("graphml");
        saveGraphML.addActionListener(this);
        operations.add(saveGraphML);
        JButton saveOXL = new JButton("OXL Export");
        saveOXL.setActionCommand("oxl");
        saveOXL.addActionListener(this);
        operations.add(saveOXL);
        south.add(operations);
        return south;
    }

    public void valueChanged(TreeSelectionEvent e) {
        TreePath p = e.getNewLeadSelectionPath();
        if (p != null) {
            AdapterNode adpNode = (AdapterNode) p.getLastPathComponent();
            htmlPane.setText(adpNode.content());
            if (adpNode.domNode.getNodeName().equals("match")) {
                htmlPane.setText(infoPage.generateInfo(adpNode.domNode));
            }
            CustomTreeCellRenderer render = (CustomTreeCellRenderer) tree.getCellRenderer();
            render.alterCheckBox(adpNode);
        }
    }

    public void hyperlinkUpdate(HyperlinkEvent event) {
        if (event.getEventType() == HyperlinkEvent.EventType.ACTIVATED) {
            try {
                htmlPane.setPage(event.getURL());
            } catch (IOException ioe) {
                JOptionPane.showInternalMessageDialog(gui, "Something went wrong while retrieving URL", "URL error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    public void actionPerformed(ActionEvent ae) {
        if (ae.getActionCommand().equals("actual")) process = ACTUAL;
        if (ae.getActionCommand().equals("selected")) process = SELECTED;
        if (ae.getActionCommand().equals("all")) process = ALL;
        if (ae.getActionCommand().equals("loadxml")) {
            JFileChooser fc = new JFileChooser(new File(System.getProperty("user.dir")));
            CustomFileFilter filter = new CustomFileFilter("xml", "XML Files");
            fc.setFileFilter(filter);
            int returnVal = fc.showOpenDialog(this);
            if (returnVal == JFileChooser.APPROVE_OPTION) {
                File file = fc.getSelectedFile();
                loadXML(file);
            } else {
                return;
            }
        }
        if (ae.getActionCommand().equals("addfilter")) {
            GeneralFilter gf = (GeneralFilter) activeFilters.get(filterBox.getSelectedIndex());
            pipeline.add(new FilterPanel(gf));
            tabPane.setSelectedComponent(pipelineTab);
            gui.pack();
            gui.revalidate();
        }
        if (ae.getActionCommand().equals("refreshfilter")) {
            updateFilterBox();
            gui.pack();
            gui.revalidate();
        }
        if (ae.getActionCommand().equals("addlayout")) {
            GeneralLayouter gl = (GeneralLayouter) activeLayouts.get(layoutBox.getSelectedIndex());
            pipeline.add(new LayouterPanel(gl));
            tabPane.setSelectedComponent(pipelineTab);
            gui.pack();
            gui.revalidate();
        }
        if (ae.getActionCommand().equals("refreshlayout")) {
            updateLayoutBox();
            gui.pack();
            gui.revalidate();
        }
        if (ae.getActionCommand().equals("spreadsheet")) {
            if (tree != null) exportSpreadsheet();
        }
        if (ae.getActionCommand().equals("infopage")) {
            if (tree != null) infoPage.saveInfo(process);
        }
        if (ae.getActionCommand().equals("gml")) {
            if (tree != null) exportGML();
        }
        if (ae.getActionCommand().equals("graphml")) {
            if (tree != null) exportGraphML();
        }
        if (ae.getActionCommand().equals("oxl")) {
            if (tree != null) exportOXL();
        }
        if (ae.getActionCommand().equals("loadconfig")) {
            JFileChooser fc = new JFileChooser();
            CustomFileFilter filter = new CustomFileFilter("xml", "XML Files");
            fc.setFileFilter(filter);
            int returnVal = fc.showOpenDialog(this);
            if (returnVal == JFileChooser.APPROVE_OPTION) {
                File file = fc.getSelectedFile();
                loadConfig(file);
            }
        }
        if (ae.getActionCommand().equals("saveconfig")) {
            JFileChooser fc = new JFileChooser();
            CustomFileFilter filter = new CustomFileFilter("xml", "XML Files");
            fc.setFileFilter(filter);
            int returnVal = fc.showSaveDialog(this);
            if (returnVal == JFileChooser.APPROVE_OPTION) {
                File file = fc.getSelectedFile();
                saveConfig(file);
            }
        }
    }

    public void loadXML(File file) {
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        try {
            DocumentBuilder builder = factory.newDocumentBuilder();
            Document document = builder.parse(file);
            Component[] comps = tabPane.getComponents();
            for (int i = 0; i < comps.length; i++) {
                if (comps[i] instanceof JSplitPane) {
                    tabPane.remove(comps[i]);
                }
            }
            Component c = buildCenter(document);
            tabPane.add("XML", c);
            tabPane.setSelectedComponent(c);
            infoPage = new OndexInfoPage(this, tree, ondexGraph);
            gui.pack();
            gui.revalidate();
        } catch (SAXException sxe) {
            Exception x = sxe;
            if (sxe.getException() != null) x = sxe.getException();
            x.printStackTrace();
        } catch (ParserConfigurationException pce) {
            pce.printStackTrace();
        } catch (IOException ioe) {
            ioe.printStackTrace();
        }
    }

    public void updateFilterBox() {
        filterBox.removeAllItems();
        activeFilters = new Vector();
        CloseableTabbedPane tab = desktop.getTabbedPaneTop();
        Component[] comps = tab.getComponents();
        for (int i = 0; i < comps.length; i++) {
            if (comps[i] instanceof FilterPropertiesFrame) {
                FilterPropertiesFrame props = (FilterPropertiesFrame) comps[i];
                GeneralFilter gf = props.getFilter();
                if (!gf.name.equals("CombinationFilter")) {
                    activeFilters.add(gf);
                    filterBox.addItem(gf.name);
                }
            }
        }
        filterBox.revalidate();
    }

    public void updateLayoutBox() {
        layoutBox.removeAllItems();
        activeLayouts = new Vector();
        CloseableTabbedPane tab = desktop.getTabbedPaneTop();
        Component[] comps = tab.getComponents();
        for (int i = 0; i < comps.length; i++) {
            if (comps[i] instanceof LayoutPropertiesFrame) {
                LayoutPropertiesFrame props = (LayoutPropertiesFrame) comps[i];
                GeneralLayouter gl = props.getLayouter();
                activeLayouts.add(gl);
                layoutBox.addItem(gl.name);
            }
        }
        layoutBox.revalidate();
    }

    public void exportSpreadsheet() {
        JFileChooser fc = new JFileChooser(new File(System.getProperty("user.dir")));
        CustomFileFilter filter = new CustomFileFilter("txt", "TXT Files");
        fc.setFileFilter(filter);
        int returnVal = fc.showSaveDialog(this);
        CustomTreeCellRenderer render = (CustomTreeCellRenderer) tree.getCellRenderer();
        if (returnVal == JFileChooser.APPROVE_OPTION) {
            String filename = fc.getSelectedFile().getPath();
            if (!filename.endsWith(".txt")) filename = filename + ".txt";
            DomToTreeModelAdapter adp = (DomToTreeModelAdapter) tree.getModel();
            String[] array = GeneralFilter.getVisibleCCs();
            String[] ccs = new String[array.length + 1];
            for (int i = 0; i < array.length; i++) {
                ccs[i] = array[i];
            }
            ccs[array.length] = "none";
            String inputValue = (String) JOptionPane.showInternalInputDialog(GeneralFilter.getMainFrame().getDesktop().getJDesktopPane(), "Seondary Concept Class for output:\n", "Concept Class Choose", JOptionPane.QUESTION_MESSAGE, null, ccs, "none");
            String cc = null;
            if ((inputValue != null) && (inputValue.length() > 0)) {
                cc = inputValue;
            }
            Element root = adp.document.getDocumentElement();
            NodeList nl = root.getChildNodes();
            try {
                BufferedWriter writer = new BufferedWriter(new FileWriter(filename));
                writer.write("Microarray ID\tScore\tEvalue\tONDEX ID\tDescription\tTaxonomy ID\tURL\tConcept names\tConcept accessions");
                if (cc != null && !cc.equals("none")) {
                    writer.write("\tONDEX ID2\tDescription2\tTaxonomy ID2\tURL2\tConcept names2\tConcept accessions2\n");
                } else {
                    writer.write("\n");
                }
                for (int i = 0; i < nl.getLength(); i++) {
                    Node qNode = nl.item(i);
                    String qName = qNode.getAttributes().getNamedItem("name").getNodeValue();
                    NodeList nl2 = qNode.getChildNodes();
                    for (int j = 0; j < nl2.getLength(); j++) {
                        Node tNode = nl2.item(j);
                        if (tNode.hasAttributes()) {
                            String tONDEXid = tNode.getAttributes().getNamedItem("ondexid").getNodeValue();
                            String score = tNode.getChildNodes().item(0).getAttributes().getNamedItem("score").getNodeValue();
                            String evalue = tNode.getChildNodes().item(0).getAttributes().getNamedItem("evalue").getNodeValue();
                            if (render.getConceptIndex().containsKey(tONDEXid.toLowerCase())) {
                                Concept c = ondexGraph.getConcept(render.getConceptIndex().getInt(tONDEXid.toLowerCase()));
                                HashSet additional = null;
                                if (cc != null && !cc.equals("none")) {
                                    additional = searchCC(c, cc);
                                }
                                if (additional != null && additional.size() > 0) {
                                    Iterator iter = additional.iterator();
                                    while (iter.hasNext()) {
                                        String desc = null;
                                        if (c.getDescription() != null) desc = c.getDescription().replaceAll("[\\t\\n\\r\\f]", " ");
                                        writer.write(qName + "\t" + score + "\t" + evalue + "\t" + tONDEXid + "\t" + desc + "\t" + c.getTaxid() + "\t" + c.getUrl() + "\t");
                                        Iterator it = c.getConcept_names().iterator();
                                        while (it.hasNext()) {
                                            Concept_Name cn = (Concept_Name) it.next();
                                            writer.write(cn.getName() + "; ");
                                        }
                                        writer.write("\t");
                                        it = c.getConcept_accs().iterator();
                                        while (it.hasNext()) {
                                            Concept_Acc ca = (Concept_Acc) it.next();
                                            writer.write(ca.getConcept_accession() + " (" + ca.getElement_of().getId() + "); ");
                                        }
                                        writer.write("\t");
                                        Concept a = (Concept) iter.next();
                                        desc = null;
                                        if (a.getDescription() != null) desc = a.getDescription().replaceAll("[\\t\\n\\r\\f]", " ");
                                        writer.write(a.getId() + "\t" + desc + "\t" + a.getTaxid() + "\t" + a.getUrl() + "\t");
                                        it = a.getConcept_names().iterator();
                                        while (it.hasNext()) {
                                            Concept_Name cn = (Concept_Name) it.next();
                                            writer.write(cn.getName() + "; ");
                                        }
                                        writer.write("\t");
                                        it = a.getConcept_accs().iterator();
                                        while (it.hasNext()) {
                                            Concept_Acc ca = (Concept_Acc) it.next();
                                            writer.write(ca.getConcept_accession() + " (" + ca.getElement_of().getId() + "); ");
                                        }
                                        writer.write("\n");
                                    }
                                } else {
                                    String desc = null;
                                    if (c.getDescription() != null) desc = c.getDescription().replaceAll("[\\t\\n\\r\\f]", " ");
                                    writer.write(qName + "\t" + score + "\t" + evalue + "\t" + tONDEXid + "\t" + desc + "\t" + c.getTaxid() + "\t" + c.getUrl() + "\t");
                                    Iterator it = c.getConcept_names().iterator();
                                    while (it.hasNext()) {
                                        Concept_Name cn = (Concept_Name) it.next();
                                        writer.write(cn.getName() + "; ");
                                    }
                                    writer.write("\t");
                                    it = c.getConcept_accs().iterator();
                                    while (it.hasNext()) {
                                        Concept_Acc ca = (Concept_Acc) it.next();
                                        writer.write(ca.getConcept_accession() + " (" + ca.getElement_of().getId() + "); ");
                                    }
                                    writer.write("\n");
                                }
                            }
                        }
                    }
                }
                writer.flush();
                writer.close();
            } catch (IOException ioe) {
                ioe.printStackTrace();
            }
        }
    }

    private HashSet searchCC(Concept c, String cc) {
        HashSet s = new HashSet();
        ConcurrentLinkedQueue queue = new ConcurrentLinkedQueue();
        HashSet visited = new HashSet();
        boolean found = false;
        queue.add(c);
        while (!queue.isEmpty()) {
            Concept u = (Concept) queue.poll();
            visited.add(u);
            if (u.getOf_type_FK().getId().equals(cc)) {
                s.add(u);
                found = true;
            }
            if (!found) {
                Iterator it = u.getOutgoing_relations().iterator();
                while (it.hasNext()) {
                    Relation r = (Relation) it.next();
                    if (r.isVisible()) {
                        Concept v = r.getTo_concept();
                        if (!visited.contains(v)) {
                            queue.add(v);
                        }
                    }
                }
                it = u.getIncoming_relations().iterator();
                while (it.hasNext()) {
                    Relation r = (Relation) it.next();
                    if (r.isVisible()) {
                        Concept v = r.getFrom_concept();
                        if (!visited.contains(v)) {
                            queue.add(v);
                        }
                    }
                }
            }
        }
        return s;
    }

    public void exportGML() {
        if (process == ACTUAL) {
            Object o = tree.getLastSelectedPathComponent();
            if (o != null) {
                AdapterNode adpNode = (AdapterNode) o;
                if (adpNode.domNode.getNodeName().equals("match")) {
                    Node tNode = adpNode.domNode.getParentNode();
                    Node qNode = tNode.getParentNode();
                    String qName = qNode.getAttributes().getNamedItem("name").getNodeValue();
                    qName = qName.substring(0, qName.indexOf(" "));
                    String tONDEXid = tNode.getAttributes().getNamedItem("ondexid").getNodeValue();
                    qName = qName.replaceAll("\\W", "");
                    tONDEXid = tONDEXid.replaceAll("\\W", "");
                    String filename = qName + "-" + tONDEXid + ".gml";
                    JFileChooser fc = new JFileChooser(new File(System.getProperty("user.dir")));
                    fc.setSelectedFile(new File(filename));
                    CustomFileFilter filter = new CustomFileFilter("gml", "GML Files");
                    fc.setFileFilter(filter);
                    int returnVal = fc.showSaveDialog(this);
                    if (returnVal == JFileChooser.APPROVE_OPTION) {
                        executePipeline(tNode.getAttributes().getNamedItem("ondexid").getNodeValue());
                        String name = fc.getSelectedFile().toString();
                        if (!name.endsWith(".gml")) name = name + ".gml";
                        saveGML(name);
                    }
                }
            }
        } else if (process == SELECTED) {
            JFileChooser fc = new JFileChooser();
            fc.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
            CustomFileFilter filter = new CustomFileFilter("gml", "GML Files");
            fc.setFileFilter(filter);
            int returnVal = fc.showSaveDialog(this);
            if (returnVal == JFileChooser.APPROVE_OPTION) {
                File file = fc.getSelectedFile();
                CustomTreeCellRenderer render = (CustomTreeCellRenderer) tree.getCellRenderer();
                Iterator it = render.getCheckBoxes().keySet().iterator();
                while (it.hasNext()) {
                    AdapterNode adpNode = (AdapterNode) it.next();
                    JCheckBox check = (JCheckBox) render.getCheckBoxes().get(adpNode);
                    if (check.isSelected()) {
                        Node tNode = adpNode.domNode.getParentNode();
                        Node qNode = tNode.getParentNode();
                        String qName = qNode.getAttributes().getNamedItem("name").getNodeValue();
                        qName = qName.substring(0, qName.indexOf(" "));
                        String tONDEXid = tNode.getAttributes().getNamedItem("ondexid").getNodeValue();
                        qName = qName.replaceAll("\\W", "");
                        tONDEXid = tONDEXid.replaceAll("\\W", "");
                        String filename = file.getPath() + System.getProperty("file.separator") + qName + "-" + tONDEXid + ".gml";
                        executePipeline(tNode.getAttributes().getNamedItem("ondexid").getNodeValue());
                        saveGML(filename);
                    }
                }
            }
        } else if (process == ALL) {
            JFileChooser fc = new JFileChooser();
            fc.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
            CustomFileFilter filter = new CustomFileFilter("gml", "GML Files");
            fc.setFileFilter(filter);
            int returnVal = fc.showSaveDialog(this);
            CustomTreeCellRenderer render = (CustomTreeCellRenderer) tree.getCellRenderer();
            if (returnVal == JFileChooser.APPROVE_OPTION) {
                File file = fc.getSelectedFile();
                DomToTreeModelAdapter adp = (DomToTreeModelAdapter) tree.getModel();
                Element root = adp.document.getDocumentElement();
                NodeList nl = root.getChildNodes();
                for (int i = 0; i < nl.getLength(); i++) {
                    Node qNode = nl.item(i);
                    NodeList nl2 = qNode.getChildNodes();
                    for (int j = 0; j < nl2.getLength(); j++) {
                        Node tNode = nl2.item(j);
                        if (tNode.hasAttributes()) {
                            String tONDEXid = tNode.getAttributes().getNamedItem("ondexid").getNodeValue();
                            if (render.getConceptIndex().containsKey(tONDEXid.toLowerCase())) {
                                String qName = qNode.getAttributes().getNamedItem("name").getNodeValue();
                                qName = qName.substring(0, qName.indexOf(" "));
                                qName = qName.replaceAll("\\W", "");
                                tONDEXid = tONDEXid.replaceAll("\\W", "");
                                String filename = file.getPath() + System.getProperty("file.separator") + qName + "-" + tONDEXid + ".gml";
                                executePipeline(tNode.getAttributes().getNamedItem("ondexid").getNodeValue());
                                saveGML(filename);
                            }
                        }
                    }
                }
            }
        }
    }

    public void saveGML(String name) {
        double start = System.currentTimeMillis();
        Console.println(0, "Saving gml file " + name + ".");
        Console.startProgress("Saving gml file.");
        try {
            YGraphLibraryAdapter yFiles = (YGraphLibraryAdapter) mainFrame.getGraphLibraryAdapter();
            iohGML.write(yFiles.getView().getGraph2D(), name);
        } catch (java.io.IOException ioe) {
            y.util.D.show(ioe);
        }
        Console.println(0, "Saving gml finished. - " + (System.currentTimeMillis() - start) / 1000 + " s");
        Console.stopProgress();
    }

    public void exportGraphML() {
        if (process == ACTUAL) {
            Object o = tree.getLastSelectedPathComponent();
            if (o != null) {
                AdapterNode adpNode = (AdapterNode) o;
                if (adpNode.domNode.getNodeName().equals("match")) {
                    Node tNode = adpNode.domNode.getParentNode();
                    Node qNode = tNode.getParentNode();
                    String qName = qNode.getAttributes().getNamedItem("name").getNodeValue();
                    qName = qName.substring(0, qName.indexOf(" "));
                    String tONDEXid = tNode.getAttributes().getNamedItem("ondexid").getNodeValue();
                    qName = qName.replaceAll("\\W", "");
                    tONDEXid = tONDEXid.replaceAll("\\W", "");
                    String filename = qName + "-" + tONDEXid + ".graphml";
                    JFileChooser fc = new JFileChooser(new File(System.getProperty("user.dir")));
                    fc.setSelectedFile(new File(filename));
                    CustomFileFilter filter = new CustomFileFilter("graphml", "GraphML Files");
                    fc.setFileFilter(filter);
                    int returnVal = fc.showSaveDialog(this);
                    if (returnVal == JFileChooser.APPROVE_OPTION) {
                        executePipeline(tNode.getAttributes().getNamedItem("ondexid").getNodeValue());
                        String name = fc.getSelectedFile().toString();
                        if (!name.endsWith(".graphml")) name = name + ".graphml";
                        saveGraphML(name);
                    }
                }
            }
        } else if (process == SELECTED) {
            JFileChooser fc = new JFileChooser();
            fc.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
            CustomFileFilter filter = new CustomFileFilter("graphml", "GraphML Files");
            fc.setFileFilter(filter);
            int returnVal = fc.showSaveDialog(this);
            if (returnVal == JFileChooser.APPROVE_OPTION) {
                File file = fc.getSelectedFile();
                CustomTreeCellRenderer render = (CustomTreeCellRenderer) tree.getCellRenderer();
                Iterator it = render.getCheckBoxes().keySet().iterator();
                while (it.hasNext()) {
                    AdapterNode adpNode = (AdapterNode) it.next();
                    JCheckBox check = (JCheckBox) render.getCheckBoxes().get(adpNode);
                    if (check.isSelected()) {
                        Node tNode = adpNode.domNode.getParentNode();
                        Node qNode = tNode.getParentNode();
                        String qName = qNode.getAttributes().getNamedItem("name").getNodeValue();
                        qName = qName.substring(0, qName.indexOf(" "));
                        String tONDEXid = tNode.getAttributes().getNamedItem("ondexid").getNodeValue();
                        qName = qName.replaceAll("\\W", "");
                        tONDEXid = tONDEXid.replaceAll("\\W", "");
                        String filename = file.getPath() + System.getProperty("file.separator") + qName + "-" + tONDEXid + ".graphml";
                        executePipeline(tNode.getAttributes().getNamedItem("ondexid").getNodeValue());
                        saveGraphML(filename);
                    }
                }
            }
        } else if (process == ALL) {
            JFileChooser fc = new JFileChooser();
            fc.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
            CustomFileFilter filter = new CustomFileFilter("graphml", "GraphML Files");
            fc.setFileFilter(filter);
            int returnVal = fc.showSaveDialog(this);
            CustomTreeCellRenderer render = (CustomTreeCellRenderer) tree.getCellRenderer();
            if (returnVal == JFileChooser.APPROVE_OPTION) {
                File file = fc.getSelectedFile();
                DomToTreeModelAdapter adp = (DomToTreeModelAdapter) tree.getModel();
                Element root = adp.document.getDocumentElement();
                NodeList nl = root.getChildNodes();
                for (int i = 0; i < nl.getLength(); i++) {
                    Node qNode = nl.item(i);
                    NodeList nl2 = qNode.getChildNodes();
                    for (int j = 0; j < nl2.getLength(); j++) {
                        Node tNode = nl2.item(j);
                        if (tNode.hasAttributes()) {
                            String tONDEXid = tNode.getAttributes().getNamedItem("ondexid").getNodeValue();
                            if (render.getConceptIndex().containsKey(tONDEXid.toLowerCase())) {
                                String qName = qNode.getAttributes().getNamedItem("name").getNodeValue();
                                qName = qName.substring(0, qName.indexOf(" "));
                                qName = qName.replaceAll("\\W", "");
                                tONDEXid = tONDEXid.replaceAll("\\W", "");
                                String filename = file.getPath() + System.getProperty("file.separator") + qName + "-" + tONDEXid + ".graphml";
                                executePipeline(tNode.getAttributes().getNamedItem("ondexid").getNodeValue());
                                saveGraphML(filename);
                            }
                        }
                    }
                }
            }
        }
    }

    public void saveGraphML(String name) {
        YGraphLibraryAdapter yFiles = (YGraphLibraryAdapter) mainFrame.getGraphLibraryAdapter();
        SetNodeLabelAction node = new SetNodeLabelAction(yFiles.getView(), true);
        node.run();
        SetEdgeLabelAction edge = new SetEdgeLabelAction(yFiles.getView(), true);
        edge.run();
        ExportAsGraphMLAction export = new ExportAsGraphMLAction(yFiles.getView());
        export.setFilename(name);
        File file = new File(name);
        name = file.getName();
        export.setTitle(name.substring(0, name.lastIndexOf('.')));
        export.run();
    }

    public void exportOXL() {
        if (process == ACTUAL) {
            Object o = tree.getLastSelectedPathComponent();
            if (o != null) {
                AdapterNode adpNode = (AdapterNode) o;
                if (adpNode.domNode.getNodeName().equals("match")) {
                    Node tNode = adpNode.domNode.getParentNode();
                    Node qNode = tNode.getParentNode();
                    String qName = qNode.getAttributes().getNamedItem("name").getNodeValue();
                    qName = qName.substring(0, qName.indexOf(" "));
                    String tONDEXid = tNode.getAttributes().getNamedItem("ondexid").getNodeValue();
                    qName = qName.replaceAll("\\W", "");
                    tONDEXid = tONDEXid.replaceAll("\\W", "");
                    String filename = qName + "-" + tONDEXid + ".oxl";
                    JFileChooser fc = new JFileChooser(new File(System.getProperty("user.dir")));
                    fc.setSelectedFile(new File(filename));
                    CustomFileFilter filter = new CustomFileFilter("oxl", "OVTk Java Beans XML Graph Files");
                    fc.setFileFilter(filter);
                    int returnVal = fc.showSaveDialog(this);
                    if (returnVal == JFileChooser.APPROVE_OPTION) {
                        executePipeline(tNode.getAttributes().getNamedItem("ondexid").getNodeValue());
                        String name = fc.getSelectedFile().toString();
                        if (!name.endsWith(".oxl")) name = name + ".oxl";
                        saveOXL(name);
                    }
                }
            }
        } else if (process == SELECTED) {
            JFileChooser fc = new JFileChooser();
            fc.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
            CustomFileFilter filter = new CustomFileFilter("oxl", "OVTk Java Beans XML Graph Files");
            fc.setFileFilter(filter);
            int returnVal = fc.showSaveDialog(this);
            if (returnVal == JFileChooser.APPROVE_OPTION) {
                File file = fc.getSelectedFile();
                CustomTreeCellRenderer render = (CustomTreeCellRenderer) tree.getCellRenderer();
                Iterator it = render.getCheckBoxes().keySet().iterator();
                while (it.hasNext()) {
                    AdapterNode adpNode = (AdapterNode) it.next();
                    JCheckBox check = (JCheckBox) render.getCheckBoxes().get(adpNode);
                    if (check.isSelected()) {
                        Node tNode = adpNode.domNode.getParentNode();
                        Node qNode = tNode.getParentNode();
                        String qName = qNode.getAttributes().getNamedItem("name").getNodeValue();
                        qName = qName.substring(0, qName.indexOf(" "));
                        String tONDEXid = tNode.getAttributes().getNamedItem("ondexid").getNodeValue();
                        qName = qName.replaceAll("\\W", "");
                        tONDEXid = tONDEXid.replaceAll("\\W", "");
                        String filename = file.getPath() + System.getProperty("file.separator") + qName + "-" + tONDEXid + ".oxl";
                        executePipeline(tNode.getAttributes().getNamedItem("ondexid").getNodeValue());
                        saveOXL(filename);
                    }
                }
            }
        } else if (process == ALL) {
            JFileChooser fc = new JFileChooser();
            fc.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
            CustomFileFilter filter = new CustomFileFilter("oxl", "OVTk Java Beans XML Graph Files");
            fc.setFileFilter(filter);
            int returnVal = fc.showSaveDialog(this);
            CustomTreeCellRenderer render = (CustomTreeCellRenderer) tree.getCellRenderer();
            if (returnVal == JFileChooser.APPROVE_OPTION) {
                File file = fc.getSelectedFile();
                DomToTreeModelAdapter adp = (DomToTreeModelAdapter) tree.getModel();
                Element root = adp.document.getDocumentElement();
                NodeList nl = root.getChildNodes();
                for (int i = 0; i < nl.getLength(); i++) {
                    Node qNode = nl.item(i);
                    NodeList nl2 = qNode.getChildNodes();
                    for (int j = 0; j < nl2.getLength(); j++) {
                        Node tNode = nl2.item(j);
                        if (tNode.hasAttributes()) {
                            String tONDEXid = tNode.getAttributes().getNamedItem("ondexid").getNodeValue();
                            if (render.getConceptIndex().containsKey(tONDEXid.toLowerCase())) {
                                String qName = qNode.getAttributes().getNamedItem("name").getNodeValue();
                                qName = qName.substring(0, qName.indexOf(" "));
                                qName = qName.replaceAll("\\W", "");
                                tONDEXid = tONDEXid.replaceAll("\\W", "");
                                String filename = file.getPath() + System.getProperty("file.separator") + qName + "-" + tONDEXid + ".oxl";
                                executePipeline(tNode.getAttributes().getNamedItem("ondexid").getNodeValue());
                                saveOXL(filename);
                            }
                        }
                    }
                }
            }
        }
    }

    public void saveOXL(String name) {
        ExportAsBeans beans = new ExportAsBeans(mainFrame);
        beans.setPacked(true);
        beans.setOnlyVisible(true);
        beans.process(name);
    }

    public void executePipeline(String ondexId) {
        Component[] comps = pipeline.getComponents();
        for (int i = 0; i < comps.length; i++) {
            if (comps[i] instanceof FilterPanel) {
                FilterPanel filterPanel = (FilterPanel) comps[i];
                filterPanel.execute(ondexId);
            } else if (comps[i] instanceof LayouterPanel) {
                LayouterPanel layouterPanel = (LayouterPanel) comps[i];
                layouterPanel.execute();
            }
        }
    }

    public void loadConfig(File file) {
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        try {
            DocumentBuilder builder = factory.newDocumentBuilder();
            Document document = builder.parse(file);
            pipeline.removeAll();
            NodeList childs = document.getDocumentElement().getChildNodes();
            for (int i = 0; i < childs.getLength(); i++) {
                Node child = childs.item(i);
                if (child.getNodeName().equals("filter")) {
                    boolean found = false;
                    for (int j = 0; j < filterBox.getItemCount(); j++) {
                        if (((String) filterBox.getItemAt(j)).equals(child.getAttributes().getNamedItem("name").getNodeValue())) {
                            GeneralFilter gf = (GeneralFilter) activeFilters.get(j);
                            FilterPanel panel = new FilterPanel(gf);
                            panel.setJustToVisible(Boolean.parseBoolean(child.getAttributes().getNamedItem("justtovisible").getNodeValue()));
                            panel.setTargetAs(child.getAttributes().getNamedItem("targetas").getNodeValue());
                            if (child.getChildNodes().getLength() > 0) {
                                NodeList attrs = child.getChildNodes();
                                for (int l = 0; l < attrs.getLength(); l++) {
                                    boolean success = panel.setAttrib(attrs.item(l).getAttributes().getNamedItem("name").getNodeValue(), attrs.item(l).getAttributes().getNamedItem("value").getNodeValue());
                                    if (!success) JOptionPane.showInternalMessageDialog(this, "Filterattribute with name " + attrs.item(l).getAttributes().getNamedItem("name").getNodeValue() + " for Filter with name " + child.getAttributes().getNamedItem("name").getNodeValue() + " was not found. It is ignored.", "Filter error", JOptionPane.ERROR_MESSAGE);
                                }
                            }
                            pipeline.add(panel);
                            found = true;
                        }
                    }
                    if (!found) {
                        JOptionPane.showInternalMessageDialog(this, "Filter with name " + child.getAttributes().getNamedItem("name").getNodeValue() + " was not found in list of active filters. It is ignored.", "Filter error", JOptionPane.ERROR_MESSAGE);
                    }
                } else if (child.getNodeName().equals("layouter")) {
                    boolean found = false;
                    for (int j = 0; j < layoutBox.getItemCount(); j++) {
                        if (((String) layoutBox.getItemAt(j)).equals(child.getAttributes().getNamedItem("name").getNodeValue())) {
                            GeneralLayouter gl = (GeneralLayouter) activeLayouts.get(j);
                            pipeline.add(new LayouterPanel(gl));
                            found = true;
                        }
                    }
                    if (!found) {
                        JOptionPane.showInternalMessageDialog(this, "Layouter with name " + child.getAttributes().getNamedItem("name").getNodeValue() + " was not found in list of active layouter. It is ignored.", "Layouter error", JOptionPane.ERROR_MESSAGE);
                    }
                }
            }
            tabPane.setSelectedComponent(pipelineTab);
            gui.pack();
            gui.revalidate();
        } catch (SAXException sxe) {
            Exception x = sxe;
            if (sxe.getException() != null) x = sxe.getException();
            x.printStackTrace();
        } catch (ParserConfigurationException pce) {
            pce.printStackTrace();
        } catch (IOException ioe) {
            ioe.printStackTrace();
        }
    }

    public void saveConfig(File file) {
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        try {
            DocumentBuilder builder = factory.newDocumentBuilder();
            Document document = builder.newDocument();
            Element root = (Element) document.createElement("config");
            document.appendChild(root);
            Component[] comp = pipeline.getComponents();
            for (int i = 0; i < comp.length; i++) {
                if (comp[i] instanceof FilterPanel) {
                    FilterPanel filter = (FilterPanel) comp[i];
                    Element fElem = document.createElement("filter");
                    fElem.setAttribute("name", filter.getName());
                    fElem.setAttribute("justtovisible", filter.getJustToVisible().toString());
                    fElem.setAttribute("targetas", filter.getTargetAs());
                    root.appendChild(fElem);
                    Object[] attribs = filter.getAttributes();
                    for (int j = 0; j < attribs.length; j++) {
                        String[] temp = (String[]) attribs[j];
                        Element aElem = document.createElement("attribute");
                        aElem.setAttribute("name", temp[0]);
                        aElem.setAttribute("value", temp[1]);
                        fElem.appendChild(aElem);
                    }
                } else if (comp[i] instanceof LayouterPanel) {
                    LayouterPanel layouter = (LayouterPanel) comp[i];
                    Element lElem = document.createElement("layouter");
                    lElem.setAttribute("name", layouter.getName());
                    root.appendChild(lElem);
                }
            }
            TransformerFactory tFactory = TransformerFactory.newInstance();
            Transformer transformer = tFactory.newTransformer();
            FileOutputStream stream = new FileOutputStream(file);
            DOMSource source = new DOMSource(document);
            StreamResult result = new StreamResult(stream);
            transformer.transform(source, result);
        } catch (ParserConfigurationException pce) {
            pce.printStackTrace();
        } catch (TransformerConfigurationException tce) {
            System.out.println("\n** Transformer Factory error");
            System.out.println("   " + tce.getMessage());
            Throwable x = tce;
            if (tce.getException() != null) x = tce.getException();
            x.printStackTrace();
        } catch (TransformerException te) {
            System.out.println("\n** Transformation error");
            System.out.println("   " + te.getMessage());
            Throwable x = te;
            if (te.getException() != null) x = te.getException();
            x.printStackTrace();
        } catch (IOException ioe) {
            ioe.printStackTrace();
        }
    }
}
