package org.viewer.gui;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.*;
import java.util.Vector;
import java.util.ArrayList;
import java.util.Properties;
import javax.swing.*;
import javax.vecmath.Point3f;
import javax.vecmath.Vector3f;
import javax.media.j3d.*;
import javax.media.MediaLocator;
import org.viewer.control.GraphControl;
import org.viewer.control.GraphControl.Cluster;
import org.viewer.file.FileHandler;
import org.viewer.forcelayout.ForceLayout;
import org.viewer.graph.NodeList;
import org.viewer.graph.EdgeList;
import org.viewer.centernode.CenterNode;
import org.viewer.viewplugin.ListFrame;
import org.viewer.viewplugin.FilterComboBox;
import org.viewer.view.ClusterView;
import ProteinIGUtil.DBEnterPHP;
import ProteinIGUtil.GetNodeInfo;
import ProteinIGUtil.GraphMLLoad;
import ProteinIGUtil.JpegImagesToMovie;
import com.sun.image.codec.jpeg.JPEGImageEncoder;
import com.sun.image.codec.jpeg.JPEGCodec;
import com.sun.image.codec.jpeg.JPEGEncodeParam;
import jm.*;
import jm.util.*;
import jm.music.data.*;
import jm.midi.*;
import jm.music.tools.*;
import ProteinIGUtil.*;

public class Actions {

    ActionMap actionMap = new ActionMap();

    FileHandler fileHandler;

    Vector<JFrame> openFrames = new Vector<JFrame>();

    Action fileNewAction;

    Action addNodeAction;

    Action addEdgeAction;

    JToggleButton addClusterAction;

    Action pickableClusterAction;

    JButton showHiddenAction;

    JButton layoutAction;

    JToggleButton rotateAction;

    jm.audio.Instrument[] instrs;

    Action saveMusicAction;

    Action saveXmlAction;

    Action harmonyAction;

    Action nodeIndexAction;

    Score theScore;

    Action lightingAction;

    Action centreAction;

    JButton graphGeneratorsAction;

    JButton graphAnalysisAction;

    JButton graphModifiersAction;

    Action fileOpenAction;

    Action fileOpenDBAction;

    Action fileSaveAction;

    Action fileSaveAsAction;

    Action screenshotAction;

    JButton addNode;

    JButton musNode;

    JTextField con;

    JTextField depth;

    JTextField radius;

    public JCheckBox doMusic;

    JTextField musicFile;

    double harmony = 1.0;

    private GetNodeInfo gni;

    private JProgressBar monitor;

    private AddNodeButton anb;

    private NextKey nk;

    private BackKey bk;

    private ControlPanel cp;

    private GraphControl gc;

    private NodeOptionsMenu nom;

    private JButton help;

    private FilterComboBox single;

    private JTextField nlimit;

    private JTextField elimit;

    private JButton slimit;

    private int nodenum;

    private String filename;

    private VRKeyListener keyl;

    private boolean done;

    private boolean removed;

    private PointSound sound;

    public BranchGroup soundbg;

    private int soundidx;

    private AutoRun ar;

    public void setAutoRun(AutoRun val) {
        ar = val;
    }

    public AutoRun getAutoRun() {
        return ar;
    }

    public JButton getShowHiddenAction() {
        return showHiddenAction;
    }

    public AddNodeButton getAnb() {
        return anb;
    }

    public void setInstrs(jm.audio.Instrument[] val) {
        instrs = val;
    }

    public boolean getRemoved() {
        return removed;
    }

    public void setRemoved(boolean val) {
        removed = val;
    }

    public JButton getRemoveButton() {
        return layoutAction;
    }

    public void setDone(boolean val) {
        done = val;
    }

    public void setDiss(double val) {
        harmony = val;
    }

    public boolean getDone() {
        return done;
    }

    public void setEnabled(boolean enabled) {
        addNodeAction.setEnabled(enabled);
        addEdgeAction.setEnabled(enabled);
        addClusterAction.setEnabled(enabled);
        pickableClusterAction.setEnabled(enabled);
        showHiddenAction.setEnabled(enabled);
        fileNewAction.setEnabled(enabled);
        graphModifiersAction.setEnabled(enabled);
    }

    protected Actions(JTextField con, GetNodeInfo gni, JProgressBar monitor, NodeOptionsMenu nom) {
        this.nom = nom;
        this.gni = gni;
        this.monitor = monitor;
        this.con = con;
        depth = new JTextField(3);
        depth.setText("1");
        doMusic = new JCheckBox("Music?");
        radius = new JTextField(3);
    }

    public JButton getAddNode() {
        return addNode;
    }

    public void setNodeNum(int val) {
        nodenum = val;
    }

    public int getNodeNum() {
        return nodenum;
    }

    public void setSoundIdx(int idx) {
        soundidx = idx;
    }

    public void setSound(PointSound ps) {
        sound = ps;
    }

    public void setFileName(String val) {
        filename = val;
    }

    public String getFileName() {
        return filename;
    }

    public FileHandler getFileHandler() {
        return fileHandler;
    }

    public JTextField getDepth() {
        return depth;
    }

    public JCheckBox getDoMusic() {
        return doMusic;
    }

    public JTextField getRadius() {
        return radius;
    }

    public void setDepthText(String val) {
        depth.setText(val);
    }

    public FilterComboBox getSingle() {
        return single;
    }

    public void diabledMods() {
        addNode.setEnabled(false);
        showHiddenAction.setEnabled(false);
        layoutAction.setEnabled(false);
        addClusterAction.setEnabled(false);
        con.setEditable(false);
        graphGeneratorsAction.setEnabled(false);
        graphModifiersAction.setEnabled(false);
        graphAnalysisAction.setEnabled(false);
        nom.getAddNodeMenuItem().setEnabled(false);
        nom.getExpandNodeMenu().setEnabled(false);
        nom.getDeleteMenuItem().setEnabled(false);
        single.setEnabled(false);
    }

    public void enabledMods() {
        addNode.setEnabled(true);
        musNode.setEnabled(true);
        showHiddenAction.setEnabled(true);
        layoutAction.setEnabled(true);
        addClusterAction.setEnabled(true);
        con.setEditable(true);
        graphGeneratorsAction.setEnabled(true);
        graphModifiersAction.setEnabled(true);
        graphAnalysisAction.setEnabled(true);
        nom.getAddNodeMenuItem().setEnabled(true);
        nom.getExpandNodeMenu().setEnabled(true);
        nom.getDeleteMenuItem().setEnabled(true);
        single.setEnabled(true);
    }

    public JTextField getNLimit() {
        return nlimit;
    }

    public JTextField getELimit() {
        return elimit;
    }

    public void loadGraphML(String filename) {
        GraphControl graphControl = this.gc;
        GetNodeInfo enddata = new GraphMLLoad(new File(filename));
        setData(enddata);
        cp.setData(enddata);
        enabledMods();
        graphControl.getRootCluster().deleteAll();
        graphControl.reset();
        Cluster r = graphControl.getRootCluster();
        r.setLayoutEngine(ForceLayout.createDefaultForceLayout(r.getCluster()));
        single.refresh();
    }

    public void saveGraphML(String filename) {
        GraphControl graphControl = this.gc;
        try {
            FileWriter fw = new FileWriter(filename);
            fw.write("<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n" + "<graphml xmlns=\"http://graphml.graphdrawing.org/xmlns\" xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\" xsi:schemaLocation=\"http://graphml.graphdrawing.org/xmlns http://graphml.graphdrawing.org/xmlns/1.0/graphml.xsd\">\n" + "<key id=\"labelid\" for=\"node\" attr.name=\"label\" attr.type=\"string\"/>\n" + "<key id=\"extdescid\" for=\"node\" attr.name=\"extdesc\" attr.type=\"string\"/>\n" + "<key id=\"sizeid\" for=\"node\" attr.name=\"size\" attr.type=\"float\"/>\n" + "<key id=\"rcolorid\" for=\"node\" attr.name=\"rcolor\" attr.type=\"float\"/>\n" + "<key id=\"gcolorid\" for=\"node\" attr.name=\"gcolor\" attrtype=\"float\"/>\n" + "<key id=\"bcolorid\" for=\"node\" attr.name=\"bcolor\" attr.type=\"float\"/>\n" + "<key id=\"listintid\" for=\"node\" attr.name=\"listint\" attr.type=\"string\"/>\n" + "<key id=\"sizeid\" for=\"edge\" attr.name=\"sizeid\" attr.type=\"float\"/>\n" + "<key id=\"rcolorid\" for=\"edge\" attr.name=\"rcolor\" attr.type=\"float\"/>\n" + "<key id=\"gcolorid\" for=\"edge\" attr.name=\"gcolor\" attr.type=\"float\"/>\n" + "<key id=\"bcolorid\" for=\"edge\" attr.name=\"bcolor\" attr.type=\"float\"/>\n" + "<key id=\"directionid\" for=\"edge\" attr.name=\"direction\" attr.type=\"int\"/>\n" + "<key id=\"extdescid\" for=\"edge\" attr.name=\"extdesc\" attr.type=\"string\"/>\n" + "<key id=\"pcaval\" for=\"node\" attr.name=\"pcaval\" attr.type=\"string\"/>\n" + "<key id=\"basenote\" for=\"node\" attr.name=\"basenote\" attr.type=\"float\"/>\n" + "<key id=\"times\" for=\"node\" attr.name=\"times\" attr.type=\"string\"/>\n" + "<key id=\"cpcaval\" for=\"node\" attr.name=\"cpcaval\" attr.type=\"string\"/>\n" + "<key id=\"control\" for=\"pca2\" attr.name=\"control\" attr.type=\"string\" />\n" + "<key id=\"compare\" for=\"pca2\" attr.name=\"compare\" attr.type=\"string\" />\n" + "<key id=\"instrument\" for=\"pca2\" attr.name=\"instrument\" attr.type=\"int\" />\n" + "<key id=\"basenote\" for=\"pca2\" attr.name=\"basenote\" attr.type=\"float\" />\n" + "<key id=\"times\" for=\"pca2\" attr.name=\"times\" attr.type=\"string\" />\n" + "<graph id=\"G\" edgedefault=\"undirected\">\n");
            Cluster r = graphControl.getRootCluster();
            GraphControl.Node[] nla = r.getNodes();
            for (int i = 0; i < nla.length; i++) {
                String id = nla[i].getProperties().getProperty("Identity");
                String labelid = nla[i].getLabel();
                String extdescid = nla[i].getProperties().getProperty("Detail");
                float sizeid = nla[i].getRadius() / anb.getStdNodeSize();
                java.awt.Color col = nla[i].getColour();
                float rc = col.getRed() / 255.0f;
                float gc = col.getGreen() / 255.0f;
                float bc = col.getBlue() / 255.0f;
                String listintid = "";
                EdgeList el = nla[i].getNode().getEdges();
                ArrayList<org.viewer.graph.Node> neighbors = nla[i].getNode().getNeighbours();
                for (int j = 0; j < el.size(); j++) {
                    listintid = listintid + el.get(j).getProperties().getProperty("Identity") + " ";
                }
                listintid = listintid.trim();
                int basenote = nla[i].getBaseNote();
                String pcaval = "";
                float[] pca = nla[i].getPCAval();
                for (int k = 0; k < pca.length; k++) {
                    pcaval = pcaval + pca[k] + " ";
                }
                pcaval = pcaval.trim();
                String cpcaval = "";
                float[] cpca = nla[i].getCPCAval();
                for (int m = 0; m < cpca.length; m++) {
                    cpcaval = cpcaval + cpca[m] + " ";
                }
                cpcaval = cpcaval.trim();
                String times = "";
                float[] tm = nla[i].getTimes();
                for (int n = 0; n < tm.length; n++) {
                    times = times + tm[n] + " ";
                }
                times = times.trim();
                int instrument = nla[i].getInstrument();
                fw.write("<node id=\"" + id + "\">\n" + "<data key=\"labelid\">" + labelid + "</data>\n" + "<data key=\"extdescid\">" + extdescid + "</data>\n" + "<data key=\"sizeid\">" + sizeid + "</data>\n" + "<data key=\"rcolorid\">" + rc + "</data>\n" + "<data key=\"gcolorid\">" + gc + "</data>\n" + "<data key=\"bcolorid\">" + bc + "</data>\n" + "<data key=\"listintid\">" + listintid + "</data>\n" + "<data key=\"basenote\">" + basenote + "</data>\n" + "<data key=\"pcaval\">" + pcaval + "</data>\n" + "<data key=\"cpcaval\">" + cpcaval + "</data>\n" + "<data key=\"times\">" + times + "</data>\n" + "<data key=\"instrument\">" + instrument + "</data>\n" + "</node>\n");
            }
            GraphControl.Edge[] ela = r.getEdges();
            for (int p = 0; p < ela.length; p++) {
                String id = ela[p].getEdge().getProperties().getProperty("Identity");
                String sid = ela[p].getStartNode().getProperties().getProperty("Identity");
                String eid = ela[p].getEndNode().getProperties().getProperty("Identity");
                float sizeid = ((org.viewer.view.EdgeView) ela[p].getEdge().getView()).getRadius() / anb.getStdEdgeSize();
                java.awt.Color col = ela[p].getColour();
                float rc = col.getRed() / 255.0f;
                float gc = col.getGreen() / 255.0f;
                float bc = col.getBlue() / 255.0f;
                String extdescid = ela[p].getEdge().getProperties().getProperty("Detail");
                fw.write("<edge id=\"" + id + "\" source=\"" + sid + "\" target=\"" + eid + "\">\n" + "<data key=\"sizeid\">" + sizeid + "</data>\n" + "<data key=\"rcolorid\">" + rc + "</data>\n" + "<data key=\"gcolorid\">" + gc + "</data>\n" + "<data key=\"bcolorid\">" + bc + "</data>\n" + "<data key=\"directionid\">2</data>\n" + "<data key=\"extdescid\">" + extdescid + "</data>\n" + "</edge>\n");
            }
            fw.write("</graph></graphml>");
            fw.flush();
            fw.close();
            JOptionPane.showMessageDialog(null, "Finished saving custom network to GraphML file!");
        } catch (Exception ex) {
            try {
                PrintWriter pw = new PrintWriter("C:\\stk.txt");
                ex.printStackTrace(pw);
                pw.flush();
                pw.close();
            } catch (Exception ex1) {
            }
            JOptionPane.showMessageDialog(null, "Error occurred: " + ex.getClass().getName() + ": " + ex.getMessage());
        }
    }

    public void init(final Component parent, final GraphControl graphControl, final ControlPanel controlPanel, NextKey nk, BackKey bk) {
        this.gc = graphControl;
        this.nk = nk;
        this.bk = bk;
        this.cp = controlPanel;
        nlimit = new JTextField(4);
        elimit = new JTextField(4);
        nlimit.setText("3000");
        elimit.setText("4000");
        single = new FilterComboBox(graphControl);
        fileHandler = new FileHandler(graphControl);
        addNodeAction = new AbstractAction("Add Node") {

            public void actionPerformed(ActionEvent e) {
                Cluster r = graphControl.getRootCluster();
                GraphControl.Node n = r.addNode();
                r.unfreeze();
            }
        };
        help = new JButton("Help");
        help.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent e) {
                JOptionPane.showMessageDialog(null, "To begin, load a protein interactome as follows:\n" + "1. Select \"Open\".\n" + "   a. Select \"Open File\" if you wish to load the data from a GraphML format file, and choose the file through file browser dialog.\n" + "   b. Select \"Open DB Link\" if you wish to load the data from a database, and enter the URL of the database in the dialog.\n" + "2. When the data has been loaded, specify by index the node(s) you wish to view and then click \"Add\".\n" + "   a. If just a single node (and its interactome) then type the index number in the \"Single Node\" text field.\n" + "   b. If multiple nodes, then type the comma-separated indices in the \"Multiple Nodes\" text field. \n" + "3. If desired, modify Depth to be greater than 1. Depth specifies how many levels of interaction are shown.\n" + "4. Click \"Add\" to display the interactome for the node(s) and depth specified.\n\n" + "If you wish you have music generated for the interactome:\n" + "1. Make sure the box labeled \"Music?\" is checked BEFORE you click \"Add\".\n" + "2. If you wish to have the music written into a MIDI file, select \"Save\"->\"Music File\" and specify the file name in the dialog prompt.\n" + "3. For large interactomes, please allow time for the music to be generated before the final display appears and the music begins to play.\n\n" + "Once the interactome appears, you can do the following to browse it.\n" + "1. Rotate: Click left mouse button and drag mouse. \n" + "2. Translate: Click right mouse button and drag mouse.\n " + "3. Zoom: Hold Alt key, click left mouse button, and drag mouse.\n" + "4. Fly to any selected node in the interactome: Click on the node and then click \"Fly\".\n     The Fly feature centers upon and zooms straight into the selected node.\n" + "5. Delete nodes:\n" + "   a. One node at a time: enter its index in \"Single Node\" text field and click Delete.\n" + "   b. Multiple nodes at once: enter the comma-separated indices in the \"Multiple Nodes\" text field and click Delete.\n" + "   c. All nodes displayed onscreen: click \"Remove All\".\n" + "6. Save a screenshot of the interactome to a JPEG file: \"Save\"->\"Screen Shot\", and follow dialog prompts.\n" + "7. Save a movie of the interactome in motion to a .mov file: \"Save\"->\"Movie Pics\", and follow dialog prompts.\n");
            }
        });
        addNode = new JButton("Add");
        anb = new AddNodeButton(con, graphControl, gni, monitor, depth, controlPanel, this, single, nlimit, elimit);
        addNode.addActionListener(anb);
        musNode = new JButton("Generate Music");
        musNode.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent e) {
                doMusic.setSelected(true);
                anb.initMusic();
                int len;
                ArrayList<Integer> getInt = new ArrayList<Integer>();
                ArrayList<Integer> getNew = getInt;
                ArrayList<String> getString = new ArrayList<String>();
                Cluster r = graphControl.getRootCluster();
                System.out.println("adding PCA2 Sound. Actions.java");
                anb.addPCA2Sound1(r);
                anb.addCPCA2Sound1(r);
                String check = con.getText();
                String[] input = check.split(",");
                Object onest = single.getSelectedItem();
                String ones;
                if (onest == null) ones = ""; else ones = onest.toString();
                if (ones.compareTo("") == 0) {
                    check = con.getText().trim();
                    input = check.split(",");
                } else {
                    if (single.getSelectedItem() instanceof FilterComboBox.IdxItem) {
                        check = String.valueOf(((FilterComboBox.IdxItem) single.getSelectedItem()).getIdx());
                        input = new String[1];
                        input[0] = check;
                    } else {
                        check = single.getSelectedItem().toString();
                        input = new String[1];
                        input[0] = check;
                    }
                }
                len = input.length;
                int rn = 0;
                for (int i = 0; i < len; i++) {
                    if (input[i].trim().compareTo("") != 0) {
                        rn++;
                        if (input[i].matches("[0-9]*")) {
                            if (!getInt.contains(Integer.parseInt(input[i]))) getInt.add(Integer.parseInt(input[i]));
                        } else {
                            if (!getString.contains(input[i])) getString.add(input[i]);
                        }
                    }
                }
                GraphControl.Node[] nla = r.getNodes();
                for (int ii = 0; ii < nla.length; ii++) {
                    for (int jj = 0; jj < getInt.size(); jj++) {
                        GraphControl.Node n = nla[ii];
                        if (n.getProperties().getProperty("Identity").compareTo(String.valueOf(getInt.get(jj))) == 0) {
                            if (n.getPCAval() != null) {
                                anb.addSound1(n, n.getPCAval(), r, n.getTimes());
                            }
                            if (n.getCPCAval() != null) {
                                anb.addCSound1(n, n.getPCAval(), n.getCPCAval(), r, n.getTimes());
                            }
                        }
                    }
                }
                anb.addMusic(r);
            }
        });
        slimit = new JButton("Set Limits");
        slimit.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent e) {
                JDialog jf = new JDialog();
                JPanel jp = new JPanel();
                jp.setLayout(new FlowLayout());
                jp.add(new JLabel(" Limits - Nodes: "));
                jp.add(nlimit);
                jp.add(new JLabel(" Edges: "));
                jp.add(elimit);
                JButton cs = new JButton("Set");
                cs.addActionListener(new ActionListener() {

                    public void actionPerformed(ActionEvent e) {
                        controlPanel.setMessage("Limit Set!");
                    }
                });
                jp.add(cs);
                jp.setVisible(true);
                jf.getContentPane().add(jp);
                jf.setVisible(true);
                jf.pack();
            }
        });
        ActionListener aa2 = new ActionListener() {

            public void actionPerformed(ActionEvent e) {
                Runnable runt = new Runnable() {

                    public void run() {
                        try {
                            if (gc.getRootCluster().getNodes().length == 0) {
                                cp.setMessage("Can't Fly");
                                enabledMods();
                                addClusterAction.setSelected(false);
                                con.setEditable(true);
                                con.select(0, 0);
                            } else {
                                diabledMods();
                                addClusterAction.setEnabled(true);
                                con.setCaretPosition(0);
                                String check = null;
                                String[] input = null;
                                boolean tracking = false;
                                if (single.getSelectedItem().toString().compareTo("") == 0) {
                                    check = con.getText().trim();
                                    input = check.split(",");
                                    tracking = true;
                                } else {
                                    if (single.getSelectedItem() instanceof FilterComboBox.IdxItem) {
                                        check = String.valueOf(((FilterComboBox.IdxItem) single.getSelectedItem()).getIdx());
                                        input = new String[1];
                                        input[0] = check;
                                    } else {
                                        check = single.getSelectedItem().toString();
                                        input = new String[1];
                                        input[0] = check;
                                    }
                                }
                                if (input != null) {
                                    GraphControl.Cluster r = graphControl.getRootCluster();
                                    GraphControl.Node[] nla = r.getNodes();
                                    int len = nla.length;
                                    ArrayList<String> nl = new ArrayList(len);
                                    for (int i = 0; i < len; i++) {
                                        Properties c = nla[i].getProperties();
                                        nl.add(c.getProperty("Identity"));
                                    }
                                    len = input.length;
                                    ArrayList<Integer> getInt = new ArrayList();
                                    ArrayList<String> getString = new ArrayList();
                                    int[] idxs = new int[len];
                                    if (len > 0) {
                                        idxs[0] = 0;
                                    }
                                    Integer[] idxcb = null;
                                    Integer[] idxce = null;
                                    ArrayList<GraphControl.Node> subset = null;
                                    Vector<Color> vecol = null;
                                    try {
                                        Vector<Boolean> valid = new Vector<Boolean>();
                                        for (int i = 0; i < len; i++) {
                                            if (input[i].trim().compareTo("") != 0) {
                                                if (i > 0) idxs[i] = idxs[i - 1] + input[i - 1].length() + 1;
                                                if (input[i].matches("[0-9]*")) {
                                                    int ri = nl.indexOf(input[i]);
                                                    if (ri != -1) {
                                                        valid.add(true);
                                                    } else valid.add(false);
                                                    getInt.add(Integer.parseInt(input[i]));
                                                } else {
                                                    if (gni.getIdx(input[i]) != null) {
                                                        boolean got = false;
                                                        for (int c : gni.getIdx(input[i])) {
                                                            int ri = nl.indexOf(String.valueOf(c));
                                                            if (ri != -1) {
                                                                got = true;
                                                            }
                                                        }
                                                        valid.add(got);
                                                    } else valid.add(false);
                                                    getString.add(input[i]);
                                                }
                                            }
                                        }
                                        Vector<Integer> ridxb = new Vector<Integer>();
                                        Vector<Integer> ridxe = new Vector<Integer>();
                                        for (int s = 0; s < idxs.length; s++) {
                                            if (valid.get(s)) {
                                                ridxb.add(idxs[s]);
                                                if (s + 1 == idxs.length) ridxe.add(check.length()); else ridxe.add(idxs[s + 1] - 1);
                                            }
                                        }
                                        idxce = new Integer[ridxe.size()];
                                        ridxe.toArray(idxce);
                                        idxcb = new Integer[ridxb.size()];
                                        ridxb.toArray(idxcb);
                                        subset = new ArrayList<GraphControl.Node>();
                                        len = getInt.size();
                                        for (int i = 0; i < len; i++) {
                                            int curridx = getInt.get(i);
                                            int ri = nl.indexOf(String.valueOf(curridx));
                                            if (ri != -1) {
                                                subset.add(nla[ri]);
                                            }
                                        }
                                        len = getString.size();
                                        for (int i = 0; i < len; i++) {
                                            String label = getString.get(i);
                                            int[] cl = gni.getIdx(label);
                                            if (cl != null) {
                                                int cll = cl.length;
                                                for (int j = 0; j < cll; j++) {
                                                    int curridx = cl[j];
                                                    int ri = nl.indexOf(String.valueOf(curridx));
                                                    if (ri != -1) {
                                                        subset.add(nla[ri]);
                                                    }
                                                }
                                            }
                                        }
                                        graphControl.getRootCluster().unfreeze();
                                        graphControl.centreGraph3();
                                        vecol = new Vector<Color>();
                                        for (int nodl = 0; nodl < subset.size(); nodl++) {
                                            vecol.add(new Color(subset.get(nodl).getColour().getRed(), subset.get(nodl).getColour().getGreen(), subset.get(nodl).getColour().getBlue()));
                                            subset.get(nodl).setColour(1, 1, 1);
                                        }
                                    } catch (Exception ex1) {
                                        ex1.printStackTrace();
                                    }
                                    try {
                                        if (!addClusterAction.isSelected()) throw new Exception();
                                        int mv = 0;
                                        for (; (mv > -1) && (mv < subset.size()); ) {
                                            if (tracking) con.select(idxcb[mv], idxce[mv]);
                                            GraphControl.Node node = subset.get(mv);
                                            GraphControl.Node[] list = graphControl.getRootCluster().getNodes();
                                            graphControl.getRootCluster().unfreeze();
                                            Point3f newc = node.getPosition();
                                            Point3f mov = new Point3f();
                                            mov.negate(newc);
                                            for (GraphControl.Node each : list) {
                                                Point3f m = each.getPosition();
                                                m.add(mov);
                                            }
                                            graphControl.deleteObservers();
                                            Vector3f position = null;
                                            CenterNode cn = graphControl.getGraphCanvas().getCN();
                                            con.requestFocus();
                                            con.repaint();
                                            NodeList nodes = graphControl.getRootCluster().getCluster().getAllNodes();
                                            int canvasHeight = graphControl.getGraphCanvas().getHeight();
                                            int canvasWidth = graphControl.getGraphCanvas().getWidth();
                                            Point3f bottomLeft = new Point3f(), topRight = new Point3f(), centre = new Point3f();
                                            nodes.getVWorldBoundingBoxCorners(bottomLeft, topRight, centre);
                                            float sceneWidth = topRight.x - bottomLeft.x;
                                            float sceneHeight = topRight.y - bottomLeft.y;
                                            float aspectRatio = (float) canvasWidth / (float) canvasHeight;
                                            float diameter = sceneWidth / aspectRatio;
                                            if (sceneHeight * aspectRatio > diameter) {
                                                diameter = sceneHeight * aspectRatio;
                                            }
                                            position = new Vector3f(mov);
                                            cn.setEnable(false);
                                            cn.setOriginPosition(position, diameter);
                                            for (int i = 0; i < 40; i++) {
                                                try {
                                                    Thread.sleep(25);
                                                } catch (Exception ex) {
                                                    ex.printStackTrace();
                                                }
                                                cn.process();
                                            }
                                            con.requestFocus();
                                            con.repaint();
                                            position = new Vector3f(new Point3f(0, 0, 0));
                                            cn.setEnable(false);
                                            cn.setOriginPosition(position);
                                            for (int i = 0; i < 40; i++) {
                                                if (!addClusterAction.isSelected()) throw new Exception();
                                                try {
                                                    Thread.sleep(33);
                                                } catch (Exception ex) {
                                                    ex.printStackTrace();
                                                }
                                                cn.process();
                                            }
                                            con.requestFocus();
                                            setNK(true);
                                            setBK(true);
                                            boolean backward;
                                            while ((!(backward = toggleBK())) && (!(toggleNK()))) {
                                                if (!addClusterAction.isSelected()) throw new Exception();
                                            }
                                            ;
                                            if (backward) {
                                                mv--;
                                            } else mv++;
                                            con.requestFocus();
                                            con.repaint();
                                        }
                                        graphControl.centreGraph3();
                                        enabledMods();
                                        con.setEditable(true);
                                        con.select(0, 0);
                                        for (int nodl = 0; nodl < subset.size(); nodl++) {
                                            subset.get(nodl).setColour(vecol.get(nodl));
                                        }
                                        addClusterAction.setSelected(false);
                                        cp.setMessage("Ended Fly Mode");
                                    } catch (Exception e) {
                                        e.printStackTrace();
                                        cp.setMessage("Leaving Fly Mode Prematuring");
                                        enabledMods();
                                        con.setEditable(true);
                                        con.select(0, 0);
                                        for (int nodl = 0; nodl < subset.size(); nodl++) {
                                            subset.get(nodl).setColour(vecol.get(nodl));
                                        }
                                    }
                                }
                            }
                        } catch (Exception ex) {
                            ex.printStackTrace();
                        }
                    }
                };
                if (addClusterAction.isSelected()) {
                    new Thread(runt).start();
                }
            }
        };
        addClusterAction = new JToggleButton("Fly");
        addClusterAction.addActionListener(aa2);
        ;
        Action showAction = new AbstractAction("Show All") {

            public void actionPerformed(ActionEvent e) {
                if (ar != null) {
                    synchronized (ar) {
                        ar.pleaseWait = true;
                    }
                }
                anb.setAll(true);
                anb.actionPerformed(e);
                single.refresh();
                if (ar != null) {
                    synchronized (ar) {
                        ar.pleaseWait = false;
                        notify();
                    }
                }
            }
        };
        showHiddenAction = new JButton("Show All");
        showHiddenAction.addActionListener(showAction);
        Action layAction = new AbstractAction("Remove All") {

            public void actionPerformed(ActionEvent e) {
                Cluster r1 = graphControl.getRootCluster();
                r1.deleteAll();
                graphControl.reset();
                Cluster r = graphControl.getRootCluster();
                cp.setMessage("Deleted All");
                r.setLayoutEngine(ForceLayout.createDefaultForceLayout(r.getCluster()));
                if (sound != null) {
                    sound.setEnable(false);
                    sound.setSoundData(null);
                    sound = null;
                }
                single.refresh();
                keyl.notifyRemoved();
            }
        };
        layoutAction = new JButton("Remove All");
        layoutAction.addActionListener(layAction);
        Action roa = new AbstractAction("Auto-Rotate") {

            public void actionPerformed(ActionEvent e) {
                graphControl.getGraphCanvas().toggleRotator();
            }
        };
        rotateAction = new JToggleButton("Auto-Rotate");
        rotateAction.addActionListener(roa);
        rotateAction.setSelected(true);
        centreAction = new AbstractAction("Center Graph") {

            public void actionPerformed(ActionEvent e) {
                graphControl.centreGraph2();
            }
        };
        nodeIndexAction = new AbstractAction("Node Index") {

            public void actionPerformed(ActionEvent e) {
                try {
                    GetNodeInfo gni = anb.getTheData();
                    ListFrame lf = new ListFrame(gni, con);
                    lf.setVisible(true);
                } catch (Exception ex) {
                    try {
                        PrintWriter pw = new PrintWriter("C:\\errlog.txt");
                        ex.printStackTrace(pw);
                        pw.flush();
                        pw.close();
                    } catch (FileNotFoundException fnfe) {
                    }
                    JOptionPane.showMessageDialog(null, "Error occurred while getting node index. " + ex.getClass().getName() + "." + ex.getMessage());
                }
            }
        };
        harmonyAction = new AbstractAction("Harmony") {

            public void actionPerformed(ActionEvent e) {
                try {
                    if (theScore == null) {
                        JOptionPane.showMessageDialog(null, "There is no music to analyze.\nMusic must be generated first by clicking \"Generate Music\".");
                        return;
                    }
                    JOptionPane.showMessageDialog(null, "Harmony metric value = " + harmony);
                } catch (Exception ex) {
                    JOptionPane.showMessageDialog(null, "Error occurred while analyzing harmony of the music. " + ex.getClass().getName() + "." + ex.getMessage());
                }
            }
        };
        saveXmlAction = new AbstractAction("Network to GraphML") {

            public void actionPerformed(ActionEvent e) {
                String filename = "";
                try {
                    JFileChooser chooser = new JFileChooser();
                    File lastSelected = fileHandler.getLastFile();
                    if (lastSelected != null) {
                        chooser.setSelectedFile(lastSelected);
                    }
                    int returnVal = chooser.showOpenDialog(parent);
                    if (returnVal == JFileChooser.APPROVE_OPTION) {
                        filename = chooser.getSelectedFile().getAbsolutePath();
                    }
                    if (filename.compareTo("") == 0) {
                        JOptionPane.showMessageDialog(null, "No filename specified.");
                    }
                    saveGraphML(filename);
                } catch (Exception ex) {
                    try {
                        PrintWriter pw = new PrintWriter("C:\\stk.txt");
                        ex.printStackTrace(pw);
                        pw.flush();
                        pw.close();
                    } catch (Exception ex1) {
                    }
                    JOptionPane.showMessageDialog(null, "Error occurred: " + ex.getClass().getName() + ": " + ex.getMessage());
                }
            }
        };
        saveMusicAction = new AbstractAction("Music File") {

            public void actionPerformed(ActionEvent e) {
                try {
                    if (theScore == null) {
                        JOptionPane.showMessageDialog(null, "There is no music to save.\nMusic must be generated first by clicking \"Generate Music\".");
                        return;
                    }
                    JFileChooser chooser = new JFileChooser();
                    File lastSelected = fileHandler.getLastFile();
                    if (lastSelected != null) {
                        chooser.setSelectedFile(lastSelected);
                    }
                    int returnVal = chooser.showOpenDialog(parent);
                    if (returnVal == JFileChooser.APPROVE_OPTION) {
                        String path = chooser.getSelectedFile().getAbsolutePath();
                        int val = JOptionPane.showConfirmDialog(null, "Write " + path + ".mid and " + path + ".au?", "Confirm", JOptionPane.YES_NO_OPTION);
                        if (val == JOptionPane.NO_OPTION) {
                            return;
                        }
                        Write.midi(theScore, path + ".mid");
                        if (instrs != null) {
                            Write.au(theScore, path + ".au", instrs);
                        }
                        JOptionPane.showMessageDialog(null, "Finished saving music to MIDI and Audio files!");
                    }
                } catch (Exception ex) {
                    JOptionPane.showMessageDialog(null, "Error occurred: " + ex.getClass().getName() + ": " + ex.getMessage());
                }
            }
        };
        lightingAction = new AbstractAction("Movie Pics") {

            public void actionPerformed(ActionEvent e) {
                if ((monitor.getPercentComplete() != 1) && (monitor.getPercentComplete() != 0)) {
                } else {
                    Runnable rn = new Runnable() {

                        public void run() {
                            try {
                                gc.deleteObservers();
                                JFileChooser fc = new JFileChooser();
                                int r = fc.showSaveDialog(null);
                                if (r == JFileChooser.APPROVE_OPTION) {
                                    String path = fc.getSelectedFile().getAbsolutePath();
                                    String str = JOptionPane.showInputDialog(null, "How many sec?", "FramesCount", JOptionPane.QUESTION_MESSAGE);
                                    int c = Integer.parseInt(str);
                                    str = JOptionPane.showInputDialog(null, "Quality? 1-100", "Quality", JOptionPane.QUESTION_MESSAGE);
                                    int quality = Integer.parseInt(str);
                                    int end = c * 8;
                                    Vector<String> bi = new Vector<String>();
                                    int val = JOptionPane.showConfirmDialog(null, "Write " + end + " frames as " + path + "_x.jpg files and one " + path + ".mov file?", "Confirm", JOptionPane.YES_NO_OPTION);
                                    if (val == JOptionPane.NO_OPTION) {
                                        return;
                                    }
                                    monitor.setMaximum(end);
                                    monitor.setValue(0);
                                    cp.setMessage("Movie Making");
                                    BufferedImage bImage = null;
                                    for (int i = 0; i < end; i++) {
                                        graphControl.getGraphCanvas().setJpegQuality(quality * 0.01f);
                                        graphControl.getGraphCanvas().writeJPEG(path + "_" + i + ".jpg", 1.0f);
                                        bImage = createBufferedImageFromCanvas3D(graphControl.getGraphCanvas());
                                        FileOutputStream out = new FileOutputStream(path + "_" + i + ".jpg");
                                        bi.add(path + "_" + i + ".jpg");
                                        JPEGImageEncoder encoder = JPEGCodec.createJPEGEncoder(out);
                                        JPEGEncodeParam param = encoder.getDefaultJPEGEncodeParam(bImage);
                                        param.setQuality(quality * 0.01f, false);
                                        encoder.setJPEGEncodeParam(param);
                                        encoder.encode(bImage);
                                        out.close();
                                        monitor.setValue(i + 1);
                                        Thread.sleep(17);
                                    }
                                    MediaLocator oml = new MediaLocator("file://" + path + ".mov");
                                    JpegImagesToMovie imageToMovie = new JpegImagesToMovie();
                                    imageToMovie.doIt(bImage.getWidth(), bImage.getHeight(), 8, bi, oml);
                                    JOptionPane.showMessageDialog(null, "Finished saving movie file!");
                                }
                            } catch (Exception ex) {
                                cp.setMessage("Error occurred while generating Movie file! " + ex.getMessage());
                            }
                        }
                    };
                    (new Thread(rn)).start();
                }
            }
        };
        screenshotAction = new AbstractAction("Screen Shot") {

            public void actionPerformed(ActionEvent e) {
                if ((monitor.getPercentComplete() != 1) && (monitor.getPercentComplete() != 0)) {
                } else {
                    Runnable rn = new Runnable() {

                        public void run() {
                            try {
                                gc.deleteObservers();
                                JFileChooser fc = new JFileChooser();
                                fc.setFileFilter(FileHandler.getJPEGFileFilter());
                                int r = fc.showSaveDialog(null);
                                if (r == JFileChooser.APPROVE_OPTION) {
                                    String path = fc.getSelectedFile().getAbsolutePath();
                                    int val = JOptionPane.showConfirmDialog(null, "Save screen shot to " + path + ".jpg?", "Confirm", JOptionPane.YES_NO_OPTION);
                                    if (val == JOptionPane.NO_OPTION) {
                                        return;
                                    }
                                    String str = JOptionPane.showInputDialog(null, "Quality? 1-100", "Quality", JOptionPane.QUESTION_MESSAGE);
                                    int quality = Integer.parseInt(str);
                                    monitor.setMaximum(2);
                                    monitor.setValue(0);
                                    cp.setMessage("Making Pic");
                                    graphControl.getGraphCanvas().setJpegQuality(quality * 0.01f);
                                    graphControl.getGraphCanvas().writeJPEG(path + ".jpg", 1.0f);
                                    monitor.setValue(2);
                                    JOptionPane.showMessageDialog(null, "Finished saving screen shot!");
                                }
                            } catch (Exception ex) {
                                ex.printStackTrace();
                            }
                        }
                    };
                    (new Thread(rn)).start();
                }
            }
        };
        Action gpAA = new AbstractAction("Onscreen Nodes List") {

            public void actionPerformed(ActionEvent e) {
                ListFrame lf = new ListFrame(graphControl, con);
                lf.setVisible(true);
            }
        };
        graphAnalysisAction = new JButton("Onscreen Node List");
        graphAnalysisAction.addActionListener(gpAA);
        Action gma = new AbstractAction("Delete") {

            public void actionPerformed(ActionEvent e) {
                String check = con.getText();
                String[] input = check.split(",");
                if (single.getSelectedItem().toString().compareTo("") == 0) {
                    check = con.getText().trim();
                    input = check.split(",");
                } else {
                    if (single.getSelectedItem() instanceof FilterComboBox.IdxItem) {
                        check = String.valueOf(((FilterComboBox.IdxItem) single.getSelectedItem()).getIdx());
                        input = new String[1];
                        input[0] = check;
                    } else {
                        check = single.getSelectedItem().toString();
                        input = new String[1];
                        input[0] = check;
                    }
                }
                int len = input.length;
                ArrayList<Integer> getInt = new ArrayList();
                ArrayList<String> getString = new ArrayList();
                int[] idxs = new int[len];
                if (len > 0) {
                    idxs[0] = 0;
                }
                for (int i = 0; i < len; i++) {
                    if (input[i].trim().compareTo("") != 0) {
                        if (i > 0) idxs[i] = idxs[i - 1] + input[i - 1].length() + 1;
                        if (input[i].matches("[0-9]*")) {
                            getInt.add(Integer.parseInt(input[i]));
                        } else {
                            getString.add(input[i]);
                        }
                    }
                }
                GraphControl.Cluster r = graphControl.getRootCluster();
                GraphControl.Node[] nla = r.getNodes();
                len = nla.length;
                ArrayList<String> nl = new ArrayList(len);
                for (int i = 0; i < len; i++) {
                    Properties c = nla[i].getProperties();
                    nl.add(c.getProperty("Identity"));
                }
                ArrayList<GraphControl.Node> subset = new ArrayList<GraphControl.Node>();
                len = getInt.size();
                for (int i = 0; i < len; i++) {
                    int curridx = getInt.get(i);
                    int ri = nl.indexOf(String.valueOf(curridx));
                    if (ri != -1) {
                        subset.add(nla[ri]);
                    }
                }
                len = getString.size();
                for (int i = 0; i < len; i++) {
                    String label = getString.get(i);
                    int[] cl = gni.getIdx(label);
                    int cll = cl.length;
                    for (int j = 0; j < cll; j++) {
                        int curridx = cl[j];
                        int ri = nl.indexOf(String.valueOf(curridx));
                        if (ri != -1) {
                            subset.add(nla[ri]);
                        }
                    }
                }
                for (GraphControl.Node node : subset) {
                    node.delete();
                }
                graphControl.getRootCluster().unfreeze();
                if (graphControl.getRootCluster().getNodes().length == 0) {
                    graphControl.getRootCluster().deleteAll();
                    graphControl.reset();
                    r = graphControl.getRootCluster();
                    r.setLayoutEngine(ForceLayout.createDefaultForceLayout(r.getCluster()));
                }
                if (sound != null) {
                    sound.setEnable(false);
                    sound = null;
                }
                cp.setMessage("Deleted " + subset.size() + " nodes");
                single.refresh();
            }
        };
        graphModifiersAction = new JButton("Delete");
        graphModifiersAction.addActionListener(gma);
        Action clr = new AbstractAction("clr") {

            public void actionPerformed(ActionEvent e) {
                con.setText("");
            }
        };
        graphGeneratorsAction = new JButton("clear");
        graphGeneratorsAction.addActionListener(clr);
        fileOpenAction = new AbstractAction("Open File") {

            public void actionPerformed(ActionEvent e) {
                JFileChooser chooser = new JFileChooser();
                File lastSelected = fileHandler.getLastFile();
                if (lastSelected != null) {
                    chooser.setSelectedFile(lastSelected);
                }
                int returnVal = chooser.showOpenDialog(parent);
                if (returnVal == JFileChooser.APPROVE_OPTION) {
                    GetNodeInfo enddata = new GraphMLLoad(chooser.getSelectedFile());
                    setData(enddata);
                    cp.setData(enddata);
                    enabledMods();
                    graphControl.getRootCluster().deleteAll();
                    graphControl.reset();
                    Cluster r = graphControl.getRootCluster();
                    r.setLayoutEngine(ForceLayout.createDefaultForceLayout(r.getCluster()));
                    single.refresh();
                }
            }
        };
        fileOpenDBAction = new AbstractAction("Open DB Link") {

            public void actionPerformed(ActionEvent e) {
                String inputValue = JOptionPane.showInputDialog("Please input full URL for php\nthat accesses DB with SQL queries");
                GetNodeInfo enddata = new DBEnterPHP(inputValue);
                setData(enddata);
                cp.setData(enddata);
                enabledMods();
                graphControl.getRootCluster().deleteAll();
                graphControl.reset();
                Cluster r = graphControl.getRootCluster();
                r.setLayoutEngine(ForceLayout.createDefaultForceLayout(r.getCluster()));
                single.refresh();
            }
        };
        fileNewAction = new AbstractAction("New", new ImageIcon(org.viewer.images.Images.class.getResource("New24.gif"))) {

            public void actionPerformed(ActionEvent e) {
                graphControl.getRootCluster().deleteAll();
                graphControl.reset();
                Cluster r = graphControl.getRootCluster();
                r.setLayoutEngine(ForceLayout.createDefaultForceLayout(r.getCluster()));
            }
        };
        configAction(fileNewAction, "Create a new graph.", "control N", 'N', new ImageIcon(org.viewer.images.Images.class.getResource("New16.gif")));
        fileSaveAsAction = new AbstractAction("Save As", new ImageIcon(org.viewer.images.Images.class.getResource("SaveAs24.gif"))) {

            public void actionPerformed(ActionEvent e) {
                JFileChooser chooser = new JFileChooser(org.viewer.global.GlobalConstants.getInstance().getProperty("DefaultDataPath"));
                chooser.setFileFilter(fileHandler.getFileFilter());
                File lastSelected = fileHandler.getLastFile();
                if (lastSelected != null) {
                    chooser.setSelectedFile(lastSelected);
                }
                int returnVal = chooser.showSaveDialog(parent);
                if (returnVal == JFileChooser.APPROVE_OPTION) {
                    fileHandler.save(chooser.getSelectedFile().getAbsolutePath());
                }
            }
        };
        configAction(fileSaveAsAction, "Select a file name and save the graph.", "control A", 'A', new ImageIcon(org.viewer.images.Images.class.getResource("SaveAs16.gif")));
        fileSaveAction = new AbstractAction("Save", new ImageIcon(org.viewer.images.Images.class.getResource("Save24.gif"))) {

            public void actionPerformed(ActionEvent e) {
                fileSaveAsAction.actionPerformed(e);
            }
        };
        configAction(fileSaveAction, "Save the graph.", "control S", 'S', new ImageIcon(org.viewer.images.Images.class.getResource("Save16.gif")));
    }

    public JToolBar getToolPanel() {
        return getToolbar();
    }

    public void setKeyListener(VRKeyListener keyl_) {
        keyl = keyl_;
    }

    public VRKeyListener getKeyListener() {
        return keyl;
    }

    private void configAction(Action a, String desc, String acc, char mn, ImageIcon smallIcon) {
        a.putValue(Action.SHORT_DESCRIPTION, desc);
        a.putValue(Action.ACCELERATOR_KEY, KeyStroke.getKeyStroke(acc));
        a.putValue(Action.MNEMONIC_KEY, new Integer(Character.getNumericValue(mn) + 55));
        a.putValue(Action.SMALL_ICON, smallIcon);
    }

    public JPanel getJPanel() {
        JPanel jp = new JPanel();
        jp.setLayout(new BorderLayout());
        jp.add(getToolbar2(), BorderLayout.NORTH);
        jp.add(getToolbar(), BorderLayout.CENTER);
        jp.add(getToolbar3(), BorderLayout.SOUTH);
        jp.setSize(12345, 100);
        return jp;
    }

    public JToolBar getToolbar2() {
        JToolBar toolbar = new JToolBar();
        ToolTipManager.sharedInstance().setLightWeightPopupEnabled(false);
        FlowLayout fl = new FlowLayout();
        fl.setAlignment(FlowLayout.CENTER);
        fl.setHgap(1);
        toolbar.setLayout(fl);
        toolbar.add(slimit);
        toolbar.add(centreAction);
        toolbar.add(rotateAction);
        toolbar.add(graphAnalysisAction);
        toolbar.add(showHiddenAction);
        toolbar.add(layoutAction);
        toolbar.add(help);
        return toolbar;
    }

    public JToolBar getToolbar3() {
        JToolBar toolbar = new JToolBar();
        ToolTipManager.sharedInstance().setLightWeightPopupEnabled(false);
        FlowLayout fl = new FlowLayout();
        fl.setAlignment(FlowLayout.LEFT);
        toolbar.setLayout(fl);
        toolbar.add(new JLabel("   Multiple Nodes: "));
        toolbar.add(con);
        toolbar.add(graphGeneratorsAction);
        toolbar.add(new JLabel("   Single Node : "));
        single.setPreferredSize(new Dimension(100, (int) con.getPreferredSize().getHeight()));
        toolbar.add(single);
        return toolbar;
    }

    public void setData(GetNodeInfo gni) {
        anb.setData(gni);
        this.gni = gni;
    }

    public JToolBar getToolbar() {
        JToolBar toolbar = new JToolBar();
        ToolTipManager.sharedInstance().setLightWeightPopupEnabled(false);
        FlowLayout fl = new FlowLayout();
        fl.setAlignment(FlowLayout.LEFT);
        fl.setHgap(1);
        fl.setVgap(0);
        toolbar.setLayout(fl);
        toolbar.add(addClusterAction);
        toolbar.add(addNode);
        toolbar.add(graphModifiersAction);
        toolbar.add(new JLabel("  Depth: "));
        toolbar.add(depth);
        toolbar.add(musNode);
        doMusic.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent e) {
                if (doMusic.getSelectedObjects() != null) {
                    if (sound != null) {
                        sound.setEnable(true);
                    }
                } else {
                    if (sound != null) {
                        sound.setEnable(false);
                        sound = null;
                    }
                }
            }
        });
        toolbar.add(doMusic);
        toolbar.add(new JLabel("  Radius:  "));
        toolbar.add(radius);
        return toolbar;
    }

    public void setNK(boolean s) {
        nk.setreach(s);
    }

    public boolean toggleNK() {
        return nk.getToggle();
    }

    public void setBK(boolean s) {
        bk.setreach(s);
    }

    public boolean toggleBK() {
        return bk.getToggle();
    }

    private JToolBar getFilebar() {
        JToolBar filebar = new JToolBar();
        filebar.add(fileNewAction).setToolTipText("New Graph");
        filebar.add(fileOpenAction).setToolTipText("Open graph from file");
        filebar.add(fileSaveAction).setToolTipText("Save graph to a file");
        return filebar;
    }

    public JMenu getEditMenu() {
        JMenu editMenu = new JMenu();
        editMenu.setText("Edit");
        editMenu.setMnemonic('E');
        editMenu.add(addNodeAction);
        editMenu.add(addEdgeAction);
        editMenu.add(addClusterAction);
        editMenu.add(pickableClusterAction);
        editMenu.add(showHiddenAction);
        return editMenu;
    }

    public JMenu getFileMenu() {
        JMenu fileMenu = new JMenu();
        fileMenu.setText("Open");
        fileMenu.setMnemonic('O');
        fileMenu.add(fileOpenAction);
        fileMenu.add(fileOpenDBAction);
        return fileMenu;
    }

    public void setScore(Score val) {
        theScore = val;
    }

    public JMenu getStatsMenu() {
        JMenu statsMenu = new JMenu();
        statsMenu.setText("View");
        statsMenu.setMnemonic('t');
        statsMenu.add(harmonyAction);
        statsMenu.add(nodeIndexAction);
        return statsMenu;
    }

    public JMenu getSaveMenu() {
        JMenu saveMenu = new JMenu();
        saveMenu.setText("Save");
        saveMenu.setMnemonic('S');
        saveMenu.add(screenshotAction);
        saveMenu.add(lightingAction);
        saveMenu.add(saveMusicAction);
        saveMenu.add(saveXmlAction);
        return saveMenu;
    }

    public JMenu getViewMenu() {
        JMenu viewMenu = new JMenu();
        viewMenu.setText("View");
        viewMenu.setMnemonic('V');
        viewMenu.add(lightingAction);
        return viewMenu;
    }

    public JMenu getToolsMenu() {
        JMenu toolsMenu = new JMenu();
        toolsMenu.setText("Tools");
        toolsMenu.setMnemonic('T');
        toolsMenu.add(centreAction);
        toolsMenu.add(rotateAction);
        toolsMenu.add(layoutAction);
        toolsMenu.add(graphGeneratorsAction);
        toolsMenu.add(graphModifiersAction);
        toolsMenu.add(graphAnalysisAction);
        return toolsMenu;
    }

    private static Actions instance = new Actions(new JTextField(), null, new JProgressBar(), null);

    public static Actions getInstance(JTextField con, GetNodeInfo gni, JProgressBar monitor, NodeOptionsMenu nom) {
        return new Actions(con, gni, monitor, nom);
    }

    public static Actions getInstance() {
        return instance;
    }

    public void closeOpenFrames() {
        for (JFrame frame : openFrames) {
            frame.dispose();
        }
    }

    static void display(BufferedImage image) {
        final JFrame f = new JFrame("");
        f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        f.getContentPane().add(new JLabel(new ImageIcon(image)));
        f.pack();
        SwingUtilities.invokeLater(new Runnable() {

            public void run() {
                f.setLocationRelativeTo(null);
                f.setVisible(true);
            }
        });
    }

    public BufferedImage createBufferedImageFromCanvas3D(Canvas3D canvas) {
        canvas.waitForOffScreenRendering();
        GraphicsContext3D ctx = canvas.getGraphicsContext3D();
        int w = canvas.getWidth();
        int h = canvas.getHeight();
        BufferedImage bi = new BufferedImage(w, h, BufferedImage.TYPE_INT_RGB);
        ImageComponent2D im = new ImageComponent2D(ImageComponent.FORMAT_RGB, bi);
        Raster ras = new Raster(new Point3f(-1.0f, -1.0f, -1.0f), Raster.RASTER_COLOR, 0, 0, w, h, im, null);
        ctx.readRaster(ras);
        BufferedImage res = ras.getImage().getImage();
        return res;
    }

    public void saveNetworkToGraphML(GraphControl graphControl, final Component parent) {
        String filename = "";
        try {
            JFileChooser chooser = new JFileChooser();
            File lastSelected = fileHandler.getLastFile();
            if (lastSelected != null) {
                chooser.setSelectedFile(lastSelected);
            }
            int returnVal = chooser.showOpenDialog(parent);
            if (returnVal == JFileChooser.APPROVE_OPTION) {
                filename = chooser.getSelectedFile().getAbsolutePath();
            }
            if (filename.compareTo("") == 0) {
                JOptionPane.showMessageDialog(null, "No filename specified.");
            }
            FileWriter fw = new FileWriter(filename);
            fw.write("<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n" + "<graphml xmlns=\"http://graphml.graphdrawing.org/xmlns\" xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\" xsi:schemaLocation=\"http://graphml.graphdrawing.org/xmlns http://graphml.graphdrawing.org/xmlns/1.0/graphml.xsd\">\n" + "<key id=\"labelid\" for=\"node\" attr.name=\"label\" attr.type=\"string\"/>\n" + "<key id=\"extdescid\" for=\"node\" attr.name=\"extdesc\" attr.type=\"string\"/>\n" + "<key id=\"sizeid\" for=\"node\" attr.name=\"size\" attr.type=\"float\"/>\n" + "<key id=\"rcolorid\" for=\"node\" attr.name=\"rcolor\" attr.type=\"float\"/>\n" + "<key id=\"gcolorid\" for=\"node\" attr.name=\"gcolor\" attrtype=\"float\"/>\n" + "<key id=\"bcolorid\" for=\"node\" attr.name=\"bcolor\" attr.type=\"float\"/>\n" + "<key id=\"listintid\" for=\"node\" attr.name=\"listint\" attr.type=\"string\"/>\n" + "<key id=\"sizeid\" for=\"edge\" attr.name=\"sizeid\" attr.type=\"float\"/>\n" + "<key id=\"rcolorid\" for=\"edge\" attr.name=\"rcolor\" attr.type=\"float\"/>\n" + "<key id=\"gcolorid\" for=\"edge\" attr.name=\"gcolor\" attr.type=\"float\"/>\n" + "<key id=\"bcolorid\" for=\"edge\" attr.name=\"bcolor\" attr.type=\"float\"/>\n" + "<key id=\"directionid\" for=\"edge\" attr.name=\"direction\" attr.type=\"int\"/>\n" + "<key id=\"extdescid\" for=\"edge\" attr.name=\"extdesc\" attr.type=\"string\"/>\n" + "<key id=\"pcaval\" for=\"node\" attr.name=\"pcaval\" attr.type=\"string\"/>\n" + "<key id=\"basenote\" for=\"node\" attr.name=\"basenote\" attr.type=\"float\"/>\n" + "<key id=\"times\" for=\"node\" attr.name=\"times\" attr.type=\"string\"/>\n" + "<key id=\"cpcaval\" for=\"node\" attr.name=\"cpcaval\" attr.type=\"string\"/>\n" + "<graph id=\"G\" edgedefault=\"undirected\">\n");
            Cluster r = graphControl.getRootCluster();
            GraphControl.Node[] nla = r.getNodes();
            for (int i = 0; i < nla.length; i++) {
                String id = nla[i].getProperties().getProperty("Identity");
                String labelid = nla[i].getLabel();
                String extdescid = nla[i].getProperties().getProperty("Detail");
                float sizeid = nla[i].getRadius() / anb.getStdNodeSize();
                java.awt.Color col = nla[i].getColour();
                float rc = col.getRed() / 255.0f;
                float gc = col.getGreen() / 255.0f;
                float bc = col.getBlue() / 255.0f;
                String listintid = "";
                EdgeList el = nla[i].getNode().getEdges();
                ArrayList<org.viewer.graph.Node> neighbors = nla[i].getNode().getNeighbours();
                for (int j = 0; j < el.size(); j++) {
                    listintid = listintid + el.get(j).getProperties().getProperty("Identity") + " ";
                }
                listintid = listintid.trim();
                int basenote = nla[i].getBaseNote();
                String pcaval = "";
                float[] pca = nla[i].getPCAval();
                for (int k = 0; k < pca.length; k++) {
                    pcaval = pcaval + pca[k] + " ";
                }
                pcaval = pcaval.trim();
                String cpcaval = "";
                float[] cpca = nla[i].getCPCAval();
                for (int m = 0; m < cpca.length; m++) {
                    cpcaval = cpcaval + cpca[m] + " ";
                }
                cpcaval = cpcaval.trim();
                String times = "";
                float[] tm = nla[i].getTimes();
                for (int n = 0; n < tm.length; n++) {
                    times = times + tm[n] + " ";
                }
                times = times.trim();
                int instrument = nla[i].getInstrument();
                fw.write("<node id=\"" + id + "\">\n" + "<data key=\"labelid\">" + labelid + "</data>\n" + "<data key=\"extdescid\">" + extdescid + "</data>\n" + "<data key=\"sizeid\">" + sizeid + "</data>\n" + "<data key=\"rcolorid\">" + rc + "</data>\n" + "<data key=\"gcolorid\">" + gc + "</data>\n" + "<data key=\"bcolorid\">" + bc + "</data>\n" + "<data key=\"listintid\">" + listintid + "</data>\n" + "<data key=\"basenote\">" + basenote + "</data>\n" + "<data key=\"pcaval\">" + pcaval + "</data>\n" + "<data key=\"cpcaval\">" + cpcaval + "</data>\n" + "<data key=\"times\">" + times + "</data>\n" + "<data key=\"instrument\">" + instrument + "</data>\n" + "</node>\n");
            }
            GraphControl.Edge[] ela = r.getEdges();
            for (int p = 0; p < ela.length; p++) {
                String id = ela[p].getEdge().getProperties().getProperty("Identity");
                String sid = ela[p].getStartNode().getProperties().getProperty("Identity");
                String eid = ela[p].getEndNode().getProperties().getProperty("Identity");
                float sizeid = ((org.viewer.view.EdgeView) ela[p].getEdge().getView()).getRadius() / anb.getStdEdgeSize();
                java.awt.Color col = ela[p].getColour();
                float rc = col.getRed() / 255.0f;
                float gc = col.getGreen() / 255.0f;
                float bc = col.getBlue() / 255.0f;
                String extdescid = ela[p].getEdge().getProperties().getProperty("Detail");
                fw.write("<edge id=\"" + id + "\" source=\"" + sid + "\" target=\"" + eid + "\">\n" + "<data key=\"sizeid\">" + sizeid + "</data>\n" + "<data key=\"rcolorid\">" + rc + "</data>\n" + "<data key=\"gcolorid\">" + gc + "</data>\n" + "<data key=\"bcolorid\">" + bc + "</data>\n" + "<data key=\"directionid\">2</data>\n" + "<data key=\"extdescid\">" + extdescid + "</data>\n" + "</edge>\n");
            }
            fw.write("</graph></graphml>");
            fw.flush();
            fw.close();
            JOptionPane.showMessageDialog(null, "Finished saving custom network to GraphML file!");
        } catch (Exception ex) {
            try {
                PrintWriter pw = new PrintWriter("C:\\stk.txt");
                ex.printStackTrace(pw);
                pw.flush();
                pw.close();
            } catch (Exception ex1) {
            }
            JOptionPane.showMessageDialog(null, "Error occurred: " + ex.getClass().getName() + ": " + ex.getMessage());
        }
    }
}
