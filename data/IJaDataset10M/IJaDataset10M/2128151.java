package org.xmlcml.cmlimpl.jumbo;

import java.net.URL;
import java.io.File;
import java.util.Vector;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.Toolkit;
import javax.swing.BorderFactory;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSplitPane;
import javax.swing.UIManager;
import javax.swing.SwingUtilities;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import uk.co.demon.ursus.dom.PMRDocumentImpl;
import uk.co.demon.ursus.dom.PMRDelegate;
import org.xmlcml.cml.CMLAtom;
import org.xmlcml.cml.CMLDocument;
import org.xmlcml.cml.CMLException;
import org.xmlcml.cml.CMLMolecule;
import org.xmlcml.cml.normalise.NormalMolecule;
import org.xmlcml.cmlimpl.CMLImpl;
import org.xmlcml.cmlimpl.CMLDocumentImpl;
import org.xmlcml.cmlimpl.style.Molecule2DPanel;
import org.xmlcml.cmlimpl.subset.AtomSetImpl;
import org.xmlcml.noncml.NonCMLDocument;
import org.xmlcml.noncml.NonCMLDocumentImpl;
import org.xmlcml.noncml.SMILES;
import org.xmlcml.noncml.SMILESImpl;
import jumbo.euclid.IntRange;
import jumbo.xml.util.Util;
import uk.co.demon.ursus.plot.PMRPlotBaseImpl;
import uk.co.demon.ursus.plot.PMRPlotterImpl;
import uk.co.demon.ursus.plot.PMRPlotterPanel;
import uk.co.demon.ursus.plot.PMRPlotScaler;
import uk.co.demon.ursus.util.Selector;
import uk.co.demon.ursus.util.SelectorImpl;
import uk.co.demon.ursus.util.PMRTrace;

public class BrowserNew {

    public static final String DEFAULT_JUMBO = "DefaultJumboPanel";

    public static final String STATUS_PANEL = "StatusPanel";

    public static final String TOP = "Top";

    public static final String FILE_SELECTOR = "FileSelector";

    public static final String MOLECULE_EDITOR = "MoleculeEditor";

    public static final String SEARCH = "SearchPanel";

    public static final String SMILES_SEARCH = "SMILESSearch";

    public static final String BOTTOM = "Bottom";

    public static final String DISPLAY2D = "Display2D";

    public static final String DISPLAY3D = "Display3D";

    public static final String DISPLAY2D3D = "Display2D3D";

    public static final int DEFAULT_FRAME_WIDTH = 400;

    public static final int DEFAULT_FRAME_HEIGHT = 400;

    protected JFrame jFrame = null;

    protected JumboController jumboController;

    protected Jumbo2D3DPanel jumbo2d3dPanel;

    protected JumboFilePanel filePanel;

    String inFile;

    String parseFile = null;

    ;

    public BrowserNew() throws Exception {
        super();
        init();
    }

    void init() throws Exception {
        JumboModel jumboModel = new JumboModel();
        jumboController = new JumboController(jumboModel);
        (new CMLDocumentImpl()).setMoleculeFactory("org.xmlcml.cmlimpl.jumbo3.JUMBOMoleculeFactory");
    }

    public void setTraceVector(Vector traceVector) {
        CMLDocumentImpl d = new CMLDocumentImpl();
        Class dClass = null;
        try {
            dClass = Class.forName("org.xmlcml.cml.CMLDocument");
        } catch (Exception e) {
            Util.bug(e);
        }
        CMLImpl c = new CMLImpl();
        Class cClass = null;
        try {
            cClass = Class.forName("org.xmlcml.cml.AbstractBase");
        } catch (Exception e) {
            Util.bug(e);
        }
        JumboPanel jp = new JumboPanel();
        Class jpClass = jp.getClass();
        PMRPlotterImpl pb = new PMRPlotterImpl();
        Class pbClass = pb.getClass();
        try {
            pbClass = Class.forName("uk.co.demon.ursus.plot.PMRPlotterImpl");
        } catch (Exception e) {
            Util.bug(e);
        }
        PMRPlotterPanel pp = new PMRPlotterPanel();
        Class ppClass = pp.getClass();
        try {
            ppClass = Class.forName("uk.co.demon.ursus.plot.PMRPlotterPanel");
        } catch (Exception e) {
            Util.bug(e);
        }
        PMRPlotScaler ps = new PMRPlotScaler();
        Class psClass = ps.getClass();
        try {
            psClass = Class.forName("uk.co.demon.ursus.plot.PMRPlotScaler");
        } catch (Exception e) {
            Util.bug(e);
        }
        for (int i = 0; i < traceVector.size(); i++) {
            String s = (String) traceVector.elementAt(i);
            try {
                addTrace(s, jpClass, jp);
                addTrace(s, pbClass, pb);
                addTrace(s, ppClass, pp);
                addTrace(s, psClass, ps);
                addTrace(s, cClass, c);
                addTrace(s, dClass, d);
            } catch (Exception e) {
                System.out.println("" + e);
            }
        }
    }

    void addTrace(String className, Class trClass, PMRTrace tr) throws ClassNotFoundException {
        Class theClass = Class.forName(className);
        if (trClass.isAssignableFrom(theClass)) {
            tr.addTracer(theClass);
            System.out.println("Tracing:..." + theClass);
        } else {
        }
    }

    void showPanes() {
        jFrame = new JFrame("Jumbo3");
        String lnfName = "com.sun.java.swing.plaf.windows.WindowsLookAndFeel";
        try {
            UIManager.setLookAndFeel(lnfName);
            SwingUtilities.updateComponentTreeUI(jFrame);
        } catch (Exception e) {
            e.printStackTrace();
        }
        jFrame.setSize(400, 750);
        JumboWindowListener.addWindow(jFrame);
        JumboPanel defaultJumboPanel = new JumboPanel(DEFAULT_JUMBO, JumboRole.CONTAINER, jumboController);
        jFrame.getContentPane().add(defaultJumboPanel);
        defaultJumboPanel.setLayout(new BorderLayout());
        filePanel = new JumboFilePanel(FILE_SELECTOR, JumboRole.FILE_SELECTOR, jumboController, new File("."));
        jumboController.jumboFilePanel = filePanel;
        JPanel borderPanel = new JPanel();
        defaultJumboPanel.add(borderPanel, BorderLayout.NORTH);
        borderPanel.setLayout(new BorderLayout());
        borderPanel.setBorder(BorderFactory.createTitledBorder("Document Panel"));
        borderPanel.add(filePanel, BorderLayout.CENTER);
        jumboController.jumboTree = new JumboTree();
        borderPanel = new JPanel();
        borderPanel.setLayout(new BorderLayout());
        borderPanel.add(jumboController.jumboTree, BorderLayout.CENTER);
        jumbo2d3dPanel = new Jumbo2D3DPanel(DISPLAY2D3D, DISPLAY2D, DISPLAY3D, JumboRole.MOLECULE_DISPLAY, jumboController);
        jumboController.jumbo2d3dPanel = jumbo2d3dPanel;
        JPanel borderPanel1 = new JPanel();
        borderPanel1.setLayout(new BorderLayout());
        borderPanel1.add(jumbo2d3dPanel, BorderLayout.CENTER);
        jumboController.jumboTree.setMinimumSize(new Dimension(50, 200));
        jumbo2d3dPanel.setMinimumSize(new Dimension(300, 200));
        jumbo2d3dPanel.setPreferredSize(new Dimension(500, 400));
        JSplitPane mainPanel1 = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, jumboController.jumboTree, jumbo2d3dPanel);
        mainPanel1.setDividerSize(3);
        JPanel bottomPanel = new JPanel();
        bottomPanel.setLayout(new BorderLayout());
        jumboController.jumboPlot = new JumboPlotPanel();
        borderPanel = new JPanel();
        bottomPanel.add(borderPanel, BorderLayout.CENTER);
        borderPanel.setLayout(new BorderLayout());
        borderPanel.setBorder(BorderFactory.createTitledBorder("Plot/Spectrum"));
        borderPanel.add(jumboController.jumboPlot, BorderLayout.CENTER);
        JSplitPane mainPanel = new JSplitPane(JSplitPane.VERTICAL_SPLIT, mainPanel1, bottomPanel);
        mainPanel.setDividerSize(3);
        defaultJumboPanel.add(mainPanel, BorderLayout.CENTER);
        jFrame.setSize(850, 700);
        jFrame.setVisible(true);
        jFrame.repaint();
    }

    public void processArgs(String[] args) throws Exception {
        boolean gui = false;
        Vector traceVector = null;
        int i = 0;
        while (i < args.length) {
            if (false) {
            } else if (args[i].equalsIgnoreCase("-GUI")) {
                i++;
                gui = true;
            } else if (args[i].equalsIgnoreCase("-HELP")) {
                i++;
                System.out.println("Usage: org.xmlcml.cmlimpl.jumbo.Jumbo <options>");
                System.out.println("	(prototypes; in process of being robustified)");
                System.out.println("           -CONTROL <controlscriptfile> [single argument; provides all options] NYI");
                System.out.println("           -HILITE [highlight matched atoms]");
                System.out.println("           -HYDROGENS (SUBSUME|MIXED) [process Hs on molecule(s)]");
                System.out.println("           -INCLUDERANGE i1-i2 ...[i3-i4] [ranges of serial numbers to include]");
                System.out.println("           -INCLUDEENTRY e1 ... [e2] [entry ids to include]");
                System.out.println("           -INDIR <inputdirectory> [processes all *.xml or -INTYPE]");
                System.out.println("           -INFILE <inputfile> [single molecules or script]");
                System.out.println("           -INTYPE <inputtype> [type of single molecules (default=CML) or multiple (LINKS)]");
                System.out.println("           -NORMALISE [normalise structure] NYI");
                System.out.println("           -OUTFILE <outputfile> [single molecules or script]");
                System.out.println("           -OUTTYPE <outputtype> [type of all/single molecules]");
                System.out.println("           -PARSEFILE <parsefile> [XML parse script for nonCML legacy]");
                System.out.println("           -SMILES <inputsmiles> [no file]");
                System.out.println("           -SEARCHFILE <searchfile> [search molecule (CML) in single file]");
                System.out.println("           -SEARCHSMILES <searchsmiles> [smiles string to search with]");
                System.out.println("           -SEARCHMETHOD <ALLMATCH | FIRSTMATCH | NOOVERLAP> [search method]");
                System.out.println("           -SHOWHCOUNT [show hydrogen count]");
                System.out.println("           -SKIPRANGE i1-i2 ...[i3-i4] [ranges of serial numbers to skip]");
                System.out.println("           -SKIPENTRY e1 ... [e2] [entry ids to skip]");
                System.out.println("           -TRACE class1 [class2]... [Short class names (e.g. CMLAtom) of objects to trace]");
            } else if (args[i].equalsIgnoreCase("-INFILE")) {
                if (++i >= args.length) throw new CMLException("Missing argument after -INFILE");
                inFile = args[i++];
            } else if (args[i].equalsIgnoreCase("-PARSEFILE")) {
                if (++i >= args.length) throw new CMLException("Missing argument after -PARSEFILE");
                parseFile = args[i++];
            } else if (args[i].equalsIgnoreCase("-TRACE")) {
                ++i;
                traceVector = new Vector();
                while (i < args.length && !(args[i].startsWith("-"))) {
                    traceVector.addElement(args[i++]);
                }
            } else {
                System.out.println("Unknown argument: " + args[i++]);
            }
        }
        if (traceVector != null) this.setTraceVector(traceVector);
        if (parseFile != null) {
            NonCMLDocumentImpl.setParseFile(parseFile);
        }
        if (gui) {
            this.showPanes();
            if (inFile != null) {
                CMLDocument cmlDoc = NonCMLDocumentImpl.createCMLDocument(new URL(Util.makeAbsoluteURL(inFile)));
                filePanel.addCMLDocument(cmlDoc, inFile);
            }
        }
    }

    public static void main(String[] args) {
        if (args.length == 0) {
            System.out.println("Usage: org.xmlcml.cmlimpl.jumbo.BrowserNew <options>");
            try {
                (new BrowserNew()).processArgs(new String[] { "-HELP" });
            } catch (Exception e) {
            }
            System.exit(0);
        } else {
            try {
                BrowserNew browser = new BrowserNew();
                browser.processArgs(args);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}
