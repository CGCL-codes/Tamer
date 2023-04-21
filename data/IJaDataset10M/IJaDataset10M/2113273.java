package gui.windows;

import java.awt.BorderLayout;
import java.awt.GraphicsConfiguration;
import java.awt.HeadlessException;
import java.awt.Dimension;
import java.awt.Color;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JMenuBar;
import javax.swing.JMenu;
import javax.swing.JToolBar;
import java.io.IOException;
import java.util.HashMap;
import java.io.File;
import java.awt.image.BufferedImage;
import java.awt.Graphics2D;
import javax.imageio.ImageIO;
import java.awt.print.Printable;
import java.awt.print.PrinterJob;
import java.util.ArrayList;
import edu.uci.ics.jung.graph.Graph;
import org.wilmascope.file.FileHandler;
import com.rapidace.visualization.GraphHelper;
import com.rapidace.visualization.XREFGraphHelper;
import edu.uci.ics.jung.visualization.VisualizationViewer;
import ftm.FromToObject;
import gui.tree.*;
import gui.dialogs.*;
import sao.DML.*;
import opt.Options;
import utils.*;

@SuppressWarnings({ "unused", "rawtypes" })
public class Vis2DWindow extends BaseJFrame implements Printable {

    private HashMap<String, JFrame> globalWindowMap;

    private HashMap<String, VisualizationViewer> globalVVMap;

    private VisualizationViewer globalVV;

    private String globalWindowKey;

    private Graph globalGraph;

    private XREFGraphHelper globalXGH;

    private GraphHelper globalGH;

    private TreeObject globalDMLNode;

    private TreeObject globalMainCluster;

    private TreeObject globalCurCluster;

    private int globalGraphSwitch;

    private static final int FULLGRAPH = 0;

    private static final int ALL_SRCS = 1;

    private static final int ALL_TGTS = 2;

    /**
	 * 
	 */
    private static final long serialVersionUID = 6736550703293193020L;

    /**
	 * @throws HeadlessException
	 */
    public Vis2DWindow() throws HeadlessException {
        super();
    }

    /**
	 * @param arg0
	 */
    public Vis2DWindow(GraphicsConfiguration arg0, Options opts) {
        super(arg0, opts);
    }

    /**
	 * @param arg0
	 * @throws HeadlessException
	 */
    public Vis2DWindow(String arg0, Options opts) throws HeadlessException {
        super(arg0, opts);
    }

    /**
	 * @param arg0
	 * @param arg1
	 */
    public Vis2DWindow(String arg0, GraphicsConfiguration arg1, Options opts) {
        super(arg0, arg1, opts);
    }

    protected void init() {
        super.init();
        globalXGH = null;
        globalGH = null;
    }

    public void setGraph(Graph gg) {
        globalGraph = gg;
    }

    public void setCommand(String cmd) {
        if (cmd.equals("FULL")) globalGraphSwitch = FULLGRAPH; else if (cmd.equals("ALLSRCS")) globalGraphSwitch = ALL_SRCS; else if (cmd.equals("ALLTGTS")) globalGraphSwitch = ALL_TGTS;
    }

    public void setupFTOGraph(DynamicTree gTree, TreeObject node, FromToObject fto, Dimension d) {
        globalXGH = new XREFGraphHelper(globalGraphSwitch);
        globalVV = globalXGH.buildVV(gTree, node, fto, d);
    }

    public void setupGraph(DynamicTree rootTreeNode, TreeObject mainDMLNode, TreeObject curClusterNode, Dimension d) {
        globalGH = new GraphHelper();
        globalVV = globalGH.buildVV(rootTreeNode, curClusterNode, d);
        globalDMLNode = mainDMLNode;
        globalMainCluster = mainDMLNode.findChildByDisplayString("Main Cluster");
        globalCurCluster = curClusterNode;
    }

    public void setMasterMap(HashMap<String, JFrame> masterMap, String masterKey) {
        globalWindowMap = masterMap;
        globalWindowKey = masterKey;
    }

    public VisualizationViewer getVisualizationViewer() {
        return (globalVV);
    }

    protected void windowClosed() {
        super.windowClosed();
        if (globalWindowMap != null) globalWindowMap.remove(globalWindowKey);
        if (globalXGH != null) globalXGH.dispose();
        if (globalGH != null) globalGH.dispose();
        globalWindowMap = null;
        globalWindowKey = null;
        globalVV = null;
        globalXGH = null;
        globalGH = null;
        garbageCollect();
    }

    protected void createToolBars() {
        JToolBar jb = new JToolBar();
        jb.add(this.createIconButton("/images/window_application.png", "Navigation", "TRANSFORMING", "GRAPH"));
        jb.add(this.createIconButton("/images/window_edit.png", "Selecting", "PICKING", "GRAPH"));
        jb.add(this.createIconButton("/images/zoom_in.png", "Zoom In", "ZOOMIN", "GRAPH"));
        jb.add(this.createIconButton("/images/zoom_out.png", "Zoom Out", "ZOOMOUT", "GRAPH"));
        this.add(jb, BorderLayout.NORTH);
    }

    private static final String OPTION_MENU = "Options";

    private static final String SAVE_AS_JPEG = "Save as JPEG";

    private static final String PRINT_IMAGE = "Print Image";

    private static final String FIND_OPT = "Find...";

    private static final String FREEZE_GRAPH = "Freeze Graph";

    private static final String UNFREEZE_GRAPH = "Unfreeze Graph";

    private static final String BLACK_BG_COLOR = "Set Black Background";

    private static final String WHITE_BG_COLOR = "Set White Background";

    private static final String CLUSTER_MENU = "Clusters";

    private static final String CLUSTER_ADD = "Convert Selected Tables To Cluster";

    protected void constructMenuBar() {
        globalMenuBar = new JMenuBar();
        JMenu tMenu;
        tMenu = new JMenu(OPTION_MENU);
        tMenu.add(createMenuItem(SAVE_AS_JPEG, OPTION_MENU, 'J', true));
        tMenu.add(createMenuItem(PRINT_IMAGE, OPTION_MENU, 'P', true));
        tMenu.add(createMenuItem(FIND_OPT, OPTION_MENU, 'F', true));
        tMenu.add(createMenuItem(FREEZE_GRAPH, OPTION_MENU, 'Z', true));
        tMenu.add(createMenuItem(UNFREEZE_GRAPH, OPTION_MENU, 'U', true));
        tMenu.add(createMenuItem(BLACK_BG_COLOR, OPTION_MENU, 'B', true));
        tMenu.add(createMenuItem(WHITE_BG_COLOR, OPTION_MENU, 'W', true));
        tMenu.getPopupMenu().setLightWeightPopupEnabled(false);
        JMenu cMenu = MenuUtils.createJMenu(CLUSTER_MENU, 'C');
        cMenu.add(createMenuItem(CLUSTER_ADD, CLUSTER_MENU, 'A', true));
        globalMenuBar.add(tMenu);
        globalMenuBar.add(cMenu);
    }

    protected void ExecMenuSelection(String cmd, String parentMenu) throws IOException {
        if (parentMenu.equals(OPTION_MENU)) execOptionMenu(cmd); else if (parentMenu.equals(CLUSTER_MENU)) execClusterMenu(cmd); else if (parentMenu.equals("GRAPH")) execGraphMenu(cmd);
    }

    private void execOptionMenu(String cmd) {
        if (cmd.equals(BLACK_BG_COLOR)) changeBGColor(Color.black); else if (cmd.equals(WHITE_BG_COLOR)) changeBGColor(Color.white); else if (cmd.equals(SAVE_AS_JPEG)) saveAsJPEG(); else if (cmd.equals(PRINT_IMAGE)) this.printImage(); else if (cmd.equals(FIND_OPT)) this.findVertex(); else if (cmd.equals(FREEZE_GRAPH)) lockAllVerticies(true); else if (cmd.equals(UNFREEZE_GRAPH)) lockAllVerticies(false);
    }

    private void execClusterMenu(String cmd) {
        if (cmd.equals(CLUSTER_ADD)) createClusterFromSelected();
    }

    private void execGraphMenu(String cmd) {
        if (cmd.equals("TRANSFORMING")) this.globalGH.navigate(); else if (cmd.equals("PICKING")) this.globalGH.pickingGraph(); else if (cmd.equals("EDITING")) this.globalGH.editGraph(); else if (cmd.equals("ANNOTATING")) this.globalGH.annotateGraph(); else if (cmd.equals("ZOOMIN")) this.globalGH.zoomIn(); else if (cmd.equals("ZOOMOUT")) this.globalGH.zoomOut();
    }

    private void saveAsJPEG() {
        JFileChooser fc = new JFileChooser();
        fc.setFileFilter(FileHandler.getJPEGFileFilter());
        int r = fc.showSaveDialog(this);
        if (r == JFileChooser.APPROVE_OPTION) writeJPEGImage(fc.getSelectedFile());
    }

    private void writeJPEGImage(File file) {
        int width = globalVV.getWidth();
        int height = globalVV.getHeight();
        BufferedImage bi = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
        Graphics2D graphics = bi.createGraphics();
        globalVV.paint(graphics);
        graphics.dispose();
        bi.flush();
        try {
            ImageIO.write(bi, "jpeg", file);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void printImage() {
        PrinterJob printJob = PrinterJob.getPrinterJob();
        printJob.setPrintable(this);
        if (printJob.printDialog()) {
            try {
                printJob.print();
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }
    }

    public int print(java.awt.Graphics graphics, java.awt.print.PageFormat pageFormat, int pageIndex) throws java.awt.print.PrinterException {
        if (pageIndex > 0) {
            return (Printable.NO_SUCH_PAGE);
        } else {
            java.awt.Graphics2D g2d = (java.awt.Graphics2D) graphics;
            globalVV.setDoubleBuffered(false);
            g2d.translate(pageFormat.getImageableX(), pageFormat.getImageableY());
            globalVV.paint(g2d);
            globalVV.setDoubleBuffered(true);
            return (Printable.PAGE_EXISTS);
        }
    }

    private void changeBGColor(Color newColor) {
        globalVV.setBackground(newColor);
    }

    private void findVertex() {
        FindTableDialog ftd = new FindTableDialog(this, globalOpts);
        if (globalGH != null) ftd.setTableList(globalGH.listVertexes()); else ftd.setTableList(globalXGH.listVertexes());
        ftd.setVisible(true);
        if (ftd.cancel == true) {
            ftd.dispose();
            return;
        }
        DMLTable curTbl = ftd.getSelectedTable();
        ftd.setVisible(false);
        ftd.dispose();
        if (globalGH != null) globalGH.findVertex(curTbl); else globalXGH.findVertex(curTbl);
    }

    private void lockAllVerticies(boolean isLocked) {
        if (globalGH != null) this.globalGH.lockAllVerticies(isLocked); else if (globalXGH != null) this.globalXGH.lockAllVerticies(isLocked);
    }

    private void createClusterFromSelected() {
        if (globalGH != null && globalMainCluster != null) {
            ArrayList<DMLTable> tableList = this.globalGH.getSelectedTables();
            if (tableList.size() == 0) return;
            ArrayList<TreeObject> treeList = getChildrenForTables(tableList);
            CreateClusterDialog ccd = new CreateClusterDialog(this, true, globalOpts);
            ccd.loadFromTree(this.globalDMLNode, "");
            ccd.setVisible(true);
            if (ccd.cancel) return;
            TreeObject newCluster = ccd.getNewClusterNode();
            for (TreeObject childObj : treeList) newCluster.add(childObj.copy());
            this.globalOpts.globalTreeList.reload(newCluster);
            globalGH.collapseSelectedVerticies(newCluster.displayString());
        }
    }

    /**
     * Purpose: to scan down the globalMainCluster, grab all tree objects that
     * have DMLObjects in the TableList
     * @param tableList
     * @return
     */
    private ArrayList<TreeObject> getChildrenForTables(ArrayList<DMLTable> tableList) {
        ArrayList<TreeObject> treeList = new ArrayList<TreeObject>();
        for (TreeObject child : globalMainCluster.getAllChildren()) if (tableList.contains(child.getUserObject())) treeList.add(child);
        return (treeList);
    }
}
