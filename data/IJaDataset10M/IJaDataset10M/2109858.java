package weka.gui.treevisualizer;

import weka.core.Instances;
import weka.gui.visualize.PrintablePanel;
import weka.gui.visualize.VisualizePanel;
import java.awt.Color;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.io.FileReader;
import java.io.IOException;
import java.io.StringReader;
import javax.swing.BorderFactory;
import javax.swing.ButtonGroup;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPopupMenu;
import javax.swing.JRadioButton;
import javax.swing.JRadioButtonMenuItem;
import javax.swing.JTextField;
import javax.swing.Timer;

/**
 * Class for displaying a Node structure in Swing. <p>
 *
 * To work this class simply create an instance of it.<p>
 *
 * Assign it to a window or other such object.<p>
 *
 * Resize it to the desired size.<p>
 *
 *
 * When using the Displayer hold the left mouse button to drag the 
 * tree around. <p>
 *
 * Click the left mouse button with ctrl to shrink the size of the tree 
 * by half. <p>
 *
 * Click and drag with the left mouse button and shift to draw a box,
 * when the left mouse button is released the contents of the box 
 * will be magnified 
 * to fill the screen. <p> <p>
 *
 * Click the right mouse button to bring up a menu. <p>
 * Most options are self explanatory.<p>
 *
 * Select Auto Scale to set the tree to it's optimal display size.
 *
 * @author Malcolm Ware (mfw4@cs.waikato.ac.nz)
 * @version $Revision: 4725 $
 */
public class TreeVisualizer extends PrintablePanel implements MouseMotionListener, MouseListener, ActionListener, ItemListener {

    /** for serialization */
    private static final long serialVersionUID = -8668637962504080749L;

    /** The placement algorithm for the Node structure. */
    private NodePlace m_placer;

    /** The top Node. */
    private Node m_topNode;

    /** The postion of the view relative to the tree. */
    private Dimension m_viewPos;

    /** The size of the tree in pixels. */
    private Dimension m_viewSize;

    /** The font used to display the tree. */
    private Font m_currentFont;

    /** The size information for the current font. */
    private FontMetrics m_fontSize;

    /** The number of Nodes in the tree. */
    private int m_numNodes;

    /** The number of levels in the tree. */
    private int m_numLevels;

    /** An array with the Nodes sorted into it and display information 
   * about the Nodes. */
    private NodeInfo[] m_nodes;

    /** An array with the Edges sorted into it and display information 
   * about the Edges. */
    private EdgeInfo[] m_edges;

    /** A timer to keep the frame rate constant. */
    private Timer m_frameLimiter;

    /** Describes the action the user is performing. */
    private int m_mouseState;

    /** A variable used to tag the start pos of a user action. */
    private Dimension m_oldMousePos;

    /** A variable used to tag the most current point of a user action. */
    private Dimension m_newMousePos;

    /** A variable used to determine for the clicked method if any other 
   * mouse state has already taken place. */
    private boolean m_clickAvailable;

    /** A variable used to remember the desired view pos. */
    private Dimension m_nViewPos;

    /** A variable used to remember the desired tree size. */
    private Dimension m_nViewSize;

    /** The number of frames left to calculate. */
    private int m_scaling;

    /** A right (or middle) click popup menu. */
    private JPopupMenu m_winMenu;

    /** An option on the win_menu */
    private JMenuItem m_topN;

    /** An option on the win_menu*/
    private JMenuItem m_fitToScreen;

    /** An option on the win_menu */
    private JMenuItem m_autoScale;

    /** A ub group on the win_menu */
    private JMenu m_selectFont;

    /** A grouping for the font choices */
    private ButtonGroup m_selectFontGroup;

    /** A font choice. */
    private JRadioButtonMenuItem m_size24;

    /** A font choice. */
    private JRadioButtonMenuItem m_size22;

    /** A font choice. */
    private JRadioButtonMenuItem m_size20;

    /** A font choice. */
    private JRadioButtonMenuItem m_size18;

    /** A font choice. */
    private JRadioButtonMenuItem m_size16;

    /** A font choice. */
    private JRadioButtonMenuItem m_size14;

    /** A font choice. */
    private JRadioButtonMenuItem m_size12;

    /** A font choice. */
    private JRadioButtonMenuItem m_size10;

    /** A font choice. */
    private JRadioButtonMenuItem m_size8;

    /** A font choice. */
    private JRadioButtonMenuItem m_size6;

    /** A font choice. */
    private JRadioButtonMenuItem m_size4;

    /** A font choice. */
    private JRadioButtonMenuItem m_size2;

    /** A font choice. */
    private JRadioButtonMenuItem m_size1;

    /** An option on the win menu. */
    private JMenuItem m_accept;

    /** A right or middle click popup menu for nodes. */
    private JPopupMenu m_nodeMenu;

    /** A visualize choice for the node, may not be available. */
    private JMenuItem m_visualise;

    /** 
   * An add children to Node choice, This is only available if the tree
   * display has a treedisplay listerner added to it.
   */
    private JMenuItem m_addChildren;

    /** Similar to add children but now it removes children. */
    private JMenuItem m_remChildren;

    /** Use this to have J48 classify this node. */
    private JMenuItem m_classifyChild;

    /** Use this to dump the instances from this node to the vis panel. */
    private JMenuItem m_sendInstances;

    /** The subscript for the currently selected node (this is an internal 
   * thing, so the user is unaware of this). */
    private int m_focusNode;

    /**
   * The Node the user is currently focused on , this is similar to 
   * focus node except that it is used by other 
   * classes rather than this one.
   */
    private int m_highlightNode;

    private TreeDisplayListener m_listener;

    private JTextField m_searchString;

    private JDialog m_searchWin;

    private JRadioButton m_caseSen;

    /**
   * Constructs Displayer to display a tree provided in a dot format.
   * Uses the NodePlacer to place the Nodes.
   * @param tdl listener 
   * @param dot string containing the dot representation of the tree to
   * display
   * @param p the algorithm to be used to position the nodes.
   */
    public TreeVisualizer(TreeDisplayListener tdl, String dot, NodePlace p) {
        super();
        setBorder(BorderFactory.createTitledBorder("Tree View"));
        m_listener = tdl;
        TreeBuild builder = new TreeBuild();
        Node n = null;
        NodePlace arrange = new PlaceNode2();
        n = builder.create(new StringReader(dot));
        m_highlightNode = 5;
        m_topNode = n;
        m_placer = p;
        m_placer.place(m_topNode);
        m_viewPos = new Dimension(0, 0);
        m_viewSize = new Dimension(800, 600);
        m_nViewPos = new Dimension(0, 0);
        m_nViewSize = new Dimension(800, 600);
        m_scaling = 0;
        m_numNodes = m_topNode.getCount(m_topNode, 0);
        m_numLevels = m_topNode.getHeight(m_topNode, 0);
        m_nodes = new NodeInfo[m_numNodes];
        m_edges = new EdgeInfo[m_numNodes - 1];
        arrayFill(m_topNode, m_nodes, m_edges);
        changeFontSize(12);
        m_mouseState = 0;
        m_oldMousePos = new Dimension(0, 0);
        m_newMousePos = new Dimension(0, 0);
        m_frameLimiter = new Timer(120, this);
        m_winMenu = new JPopupMenu();
        m_topN = new JMenuItem("Center on Top Node");
        m_topN.setActionCommand("Center on Top Node");
        m_fitToScreen = new JMenuItem("Fit to Screen");
        m_fitToScreen.setActionCommand("Fit to Screen");
        m_selectFont = new JMenu("Select Font");
        m_selectFont.setActionCommand("Select Font");
        m_autoScale = new JMenuItem("Auto Scale");
        m_autoScale.setActionCommand("Auto Scale");
        m_selectFontGroup = new ButtonGroup();
        m_accept = new JMenuItem("Accept The Tree");
        m_accept.setActionCommand("Accept The Tree");
        m_winMenu.add(m_topN);
        m_winMenu.addSeparator();
        m_winMenu.add(m_fitToScreen);
        m_winMenu.add(m_autoScale);
        m_winMenu.addSeparator();
        m_winMenu.addSeparator();
        m_winMenu.add(m_selectFont);
        m_winMenu.addSeparator();
        if (m_listener != null) {
            m_winMenu.add(m_accept);
        }
        m_topN.addActionListener(this);
        m_fitToScreen.addActionListener(this);
        m_autoScale.addActionListener(this);
        m_accept.addActionListener(this);
        m_size24 = new JRadioButtonMenuItem("Size 24", false);
        m_size22 = new JRadioButtonMenuItem("Size 22", false);
        m_size20 = new JRadioButtonMenuItem("Size 20", false);
        m_size18 = new JRadioButtonMenuItem("Size 18", false);
        m_size16 = new JRadioButtonMenuItem("Size 16", false);
        m_size14 = new JRadioButtonMenuItem("Size 14", false);
        m_size12 = new JRadioButtonMenuItem("Size 12", true);
        m_size10 = new JRadioButtonMenuItem("Size 10", false);
        m_size8 = new JRadioButtonMenuItem("Size 8", false);
        m_size6 = new JRadioButtonMenuItem("Size 6", false);
        m_size4 = new JRadioButtonMenuItem("Size 4", false);
        m_size2 = new JRadioButtonMenuItem("Size 2", false);
        m_size1 = new JRadioButtonMenuItem("Size 1", false);
        m_size24.setActionCommand("Size 24");
        m_size22.setActionCommand("Size 22");
        m_size20.setActionCommand("Size 20");
        m_size18.setActionCommand("Size 18");
        m_size16.setActionCommand("Size 16");
        m_size14.setActionCommand("Size 14");
        m_size12.setActionCommand("Size 12");
        m_size10.setActionCommand("Size 10");
        m_size8.setActionCommand("Size 8");
        m_size6.setActionCommand("Size 6");
        m_size4.setActionCommand("Size 4");
        m_size2.setActionCommand("Size 2");
        m_size1.setActionCommand("Size 1");
        m_selectFontGroup.add(m_size24);
        m_selectFontGroup.add(m_size22);
        m_selectFontGroup.add(m_size20);
        m_selectFontGroup.add(m_size18);
        m_selectFontGroup.add(m_size16);
        m_selectFontGroup.add(m_size14);
        m_selectFontGroup.add(m_size12);
        m_selectFontGroup.add(m_size10);
        m_selectFontGroup.add(m_size8);
        m_selectFontGroup.add(m_size6);
        m_selectFontGroup.add(m_size4);
        m_selectFontGroup.add(m_size2);
        m_selectFontGroup.add(m_size1);
        m_selectFont.add(m_size24);
        m_selectFont.add(m_size22);
        m_selectFont.add(m_size20);
        m_selectFont.add(m_size18);
        m_selectFont.add(m_size16);
        m_selectFont.add(m_size14);
        m_selectFont.add(m_size12);
        m_selectFont.add(m_size10);
        m_selectFont.add(m_size8);
        m_selectFont.add(m_size6);
        m_selectFont.add(m_size4);
        m_selectFont.add(m_size2);
        m_selectFont.add(m_size1);
        m_size24.addItemListener(this);
        m_size22.addItemListener(this);
        m_size20.addItemListener(this);
        m_size18.addItemListener(this);
        m_size16.addItemListener(this);
        m_size14.addItemListener(this);
        m_size12.addItemListener(this);
        m_size10.addItemListener(this);
        m_size8.addItemListener(this);
        m_size6.addItemListener(this);
        m_size4.addItemListener(this);
        m_size2.addItemListener(this);
        m_size1.addItemListener(this);
        m_nodeMenu = new JPopupMenu();
        m_visualise = new JMenuItem("Visualize The Node");
        m_visualise.setActionCommand("Visualize The Node");
        m_visualise.addActionListener(this);
        m_nodeMenu.add(m_visualise);
        if (m_listener != null) {
            m_remChildren = new JMenuItem("Remove Child Nodes");
            m_remChildren.setActionCommand("Remove Child Nodes");
            m_remChildren.addActionListener(this);
            m_nodeMenu.add(m_remChildren);
            m_classifyChild = new JMenuItem("Use Classifier...");
            m_classifyChild.setActionCommand("classify_child");
            m_classifyChild.addActionListener(this);
            m_nodeMenu.add(m_classifyChild);
        }
        m_focusNode = -1;
        m_highlightNode = -1;
        addMouseMotionListener(this);
        addMouseListener(this);
        m_frameLimiter.setRepeats(false);
        m_frameLimiter.start();
    }

    /**
   * Constructs Displayer with the specified Node as the top 
   * of the tree, and uses the NodePlacer to place the Nodes.
   * @param tdl listener.
   * @param n the top Node of the tree to be displayed.
   * @param p the algorithm to be used to position the nodes.
   */
    public TreeVisualizer(TreeDisplayListener tdl, Node n, NodePlace p) {
        super();
        setBorder(BorderFactory.createTitledBorder("Tree View"));
        m_listener = tdl;
        m_topNode = n;
        m_placer = p;
        m_placer.place(m_topNode);
        m_viewPos = new Dimension(0, 0);
        m_viewSize = new Dimension(800, 600);
        m_nViewPos = new Dimension(0, 0);
        m_nViewSize = new Dimension(800, 600);
        m_scaling = 0;
        m_numNodes = m_topNode.getCount(m_topNode, 0);
        m_numLevels = m_topNode.getHeight(m_topNode, 0);
        m_nodes = new NodeInfo[m_numNodes];
        m_edges = new EdgeInfo[m_numNodes - 1];
        arrayFill(m_topNode, m_nodes, m_edges);
        changeFontSize(12);
        m_mouseState = 0;
        m_oldMousePos = new Dimension(0, 0);
        m_newMousePos = new Dimension(0, 0);
        m_frameLimiter = new Timer(120, this);
        m_winMenu = new JPopupMenu();
        m_topN = new JMenuItem("Center on Top Node");
        m_topN.setActionCommand("Center on Top Node");
        m_fitToScreen = new JMenuItem("Fit to Screen");
        m_fitToScreen.setActionCommand("Fit to Screen");
        m_selectFont = new JMenu("Select Font");
        m_selectFont.setActionCommand("Select Font");
        m_autoScale = new JMenuItem("Auto Scale");
        m_autoScale.setActionCommand("Auto Scale");
        m_selectFontGroup = new ButtonGroup();
        m_accept = new JMenuItem("Accept The Tree");
        m_accept.setActionCommand("Accept The Tree");
        m_winMenu.add(m_topN);
        m_winMenu.addSeparator();
        m_winMenu.add(m_fitToScreen);
        m_winMenu.add(m_autoScale);
        m_winMenu.addSeparator();
        m_winMenu.addSeparator();
        m_winMenu.add(m_selectFont);
        m_winMenu.addSeparator();
        if (m_listener != null) {
            m_winMenu.add(m_accept);
        }
        m_topN.addActionListener(this);
        m_fitToScreen.addActionListener(this);
        m_autoScale.addActionListener(this);
        m_accept.addActionListener(this);
        m_size24 = new JRadioButtonMenuItem("Size 24", false);
        m_size22 = new JRadioButtonMenuItem("Size 22", false);
        m_size20 = new JRadioButtonMenuItem("Size 20", false);
        m_size18 = new JRadioButtonMenuItem("Size 18", false);
        m_size16 = new JRadioButtonMenuItem("Size 16", false);
        m_size14 = new JRadioButtonMenuItem("Size 14", false);
        m_size12 = new JRadioButtonMenuItem("Size 12", true);
        m_size10 = new JRadioButtonMenuItem("Size 10", false);
        m_size8 = new JRadioButtonMenuItem("Size 8", false);
        m_size6 = new JRadioButtonMenuItem("Size 6", false);
        m_size4 = new JRadioButtonMenuItem("Size 4", false);
        m_size2 = new JRadioButtonMenuItem("Size 2", false);
        m_size1 = new JRadioButtonMenuItem("Size 1", false);
        m_size24.setActionCommand("Size 24");
        m_size22.setActionCommand("Size 22");
        m_size20.setActionCommand("Size 20");
        m_size18.setActionCommand("Size 18");
        m_size16.setActionCommand("Size 16");
        m_size14.setActionCommand("Size 14");
        m_size12.setActionCommand("Size 12");
        m_size10.setActionCommand("Size 10");
        m_size8.setActionCommand("Size 8");
        m_size6.setActionCommand("Size 6");
        m_size4.setActionCommand("Size 4");
        m_size2.setActionCommand("Size 2");
        m_size1.setActionCommand("Size 1");
        m_selectFontGroup.add(m_size24);
        m_selectFontGroup.add(m_size22);
        m_selectFontGroup.add(m_size20);
        m_selectFontGroup.add(m_size18);
        m_selectFontGroup.add(m_size16);
        m_selectFontGroup.add(m_size14);
        m_selectFontGroup.add(m_size12);
        m_selectFontGroup.add(m_size10);
        m_selectFontGroup.add(m_size8);
        m_selectFontGroup.add(m_size6);
        m_selectFontGroup.add(m_size4);
        m_selectFontGroup.add(m_size2);
        m_selectFontGroup.add(m_size1);
        m_selectFont.add(m_size24);
        m_selectFont.add(m_size22);
        m_selectFont.add(m_size20);
        m_selectFont.add(m_size18);
        m_selectFont.add(m_size16);
        m_selectFont.add(m_size14);
        m_selectFont.add(m_size12);
        m_selectFont.add(m_size10);
        m_selectFont.add(m_size8);
        m_selectFont.add(m_size6);
        m_selectFont.add(m_size4);
        m_selectFont.add(m_size2);
        m_selectFont.add(m_size1);
        m_size24.addItemListener(this);
        m_size22.addItemListener(this);
        m_size20.addItemListener(this);
        m_size18.addItemListener(this);
        m_size16.addItemListener(this);
        m_size14.addItemListener(this);
        m_size12.addItemListener(this);
        m_size10.addItemListener(this);
        m_size8.addItemListener(this);
        m_size6.addItemListener(this);
        m_size4.addItemListener(this);
        m_size2.addItemListener(this);
        m_size1.addItemListener(this);
        m_nodeMenu = new JPopupMenu();
        m_visualise = new JMenuItem("Visualize The Node");
        m_visualise.setActionCommand("Visualize The Node");
        m_visualise.addActionListener(this);
        m_nodeMenu.add(m_visualise);
        if (m_listener != null) {
            m_remChildren = new JMenuItem("Remove Child Nodes");
            m_remChildren.setActionCommand("Remove Child Nodes");
            m_remChildren.addActionListener(this);
            m_nodeMenu.add(m_remChildren);
            m_classifyChild = new JMenuItem("Use Classifier...");
            m_classifyChild.setActionCommand("classify_child");
            m_classifyChild.addActionListener(this);
            m_nodeMenu.add(m_classifyChild);
            m_sendInstances = new JMenuItem("Add Instances To Viewer");
            m_sendInstances.setActionCommand("send_instances");
            m_sendInstances.addActionListener(this);
            m_nodeMenu.add(m_sendInstances);
        }
        m_focusNode = -1;
        m_highlightNode = -1;
        addMouseMotionListener(this);
        addMouseListener(this);
        m_frameLimiter.setRepeats(false);
        m_frameLimiter.start();
    }

    /**
   * Fits the tree to the current screen size. Call this after
   * window has been created to get the entrire tree to be in view
   * upon launch.
   */
    public void fitToScreen() {
        getScreenFit(m_viewPos, m_viewSize);
        repaint();
    }

    /**
   * Calculates the dimensions needed to fit the entire tree into view.
   */
    private void getScreenFit(Dimension np, Dimension ns) {
        int leftmost = 1000000, rightmost = -1000000;
        int leftCenter = 1000000, rightCenter = -1000000, rightNode = 0;
        int highest = -1000000, highTop = -1000000;
        for (int noa = 0; noa < m_numNodes; noa++) {
            calcScreenCoords(noa);
            if (m_nodes[noa].m_center - m_nodes[noa].m_side < leftmost) {
                leftmost = m_nodes[noa].m_center - m_nodes[noa].m_side;
            }
            if (m_nodes[noa].m_center < leftCenter) {
                leftCenter = m_nodes[noa].m_center;
            }
            if (m_nodes[noa].m_center + m_nodes[noa].m_side > rightmost) {
                rightmost = m_nodes[noa].m_center + m_nodes[noa].m_side;
            }
            if (m_nodes[noa].m_center > rightCenter) {
                rightCenter = m_nodes[noa].m_center;
                rightNode = noa;
            }
            if (m_nodes[noa].m_top + m_nodes[noa].m_height > highest) {
                highest = m_nodes[noa].m_top + m_nodes[noa].m_height;
            }
            if (m_nodes[noa].m_top > highTop) {
                highTop = m_nodes[noa].m_top;
            }
        }
        ns.width = getWidth();
        ns.width -= leftCenter - leftmost + rightmost - rightCenter + 30;
        ns.height = getHeight() - highest + highTop - 40;
        if (m_nodes[rightNode].m_node.getCenter() != 0 && leftCenter != rightCenter) {
            ns.width /= m_nodes[rightNode].m_node.getCenter();
        }
        if (ns.width < 10) {
            ns.width = 10;
        }
        if (ns.height < 10) {
            ns.height = 10;
        }
        np.width = (leftCenter - leftmost + rightmost - rightCenter) / 2 + 15;
        np.height = (highest - highTop) / 2 + 20;
    }

    /**
   * Performs the action associated with the ActionEvent.
   *
   * @param e the action event.
   */
    public void actionPerformed(ActionEvent e) {
        if (e.getActionCommand() == null) {
            if (m_scaling == 0) {
                repaint();
            } else {
                animateScaling(m_nViewPos, m_nViewSize, m_scaling);
            }
        } else if (e.getActionCommand().equals("Fit to Screen")) {
            Dimension np = new Dimension();
            Dimension ns = new Dimension();
            getScreenFit(np, ns);
            animateScaling(np, ns, 10);
        } else if (e.getActionCommand().equals("Center on Top Node")) {
            int tpx = (int) (m_topNode.getCenter() * m_viewSize.width);
            int tpy = (int) (m_topNode.getTop() * m_viewSize.height);
            Dimension np = new Dimension(getSize().width / 2 - tpx, getSize().width / 6 - tpy);
            animateScaling(np, m_viewSize, 10);
        } else if (e.getActionCommand().equals("Auto Scale")) {
            autoScale();
        } else if (e.getActionCommand().equals("Visualize The Node")) {
            if (m_focusNode >= 0) {
                Instances inst;
                if ((inst = m_nodes[m_focusNode].m_node.getInstances()) != null) {
                    VisualizePanel pan = new VisualizePanel();
                    pan.setInstances(inst);
                    JFrame nf = new JFrame();
                    nf.setSize(400, 300);
                    nf.getContentPane().add(pan);
                    nf.setVisible(true);
                } else {
                    JOptionPane.showMessageDialog(this, "Sorry, there is no " + "available Instances data for " + "this Node.", "Sorry!", JOptionPane.WARNING_MESSAGE);
                }
            } else {
                JOptionPane.showMessageDialog(this, "Error, there is no " + "selected Node to perform " + "this operation on.", "Error!", JOptionPane.ERROR_MESSAGE);
            }
        } else if (e.getActionCommand().equals("Create Child Nodes")) {
            if (m_focusNode >= 0) {
                if (m_listener != null) {
                    m_listener.userCommand(new TreeDisplayEvent(TreeDisplayEvent.ADD_CHILDREN, m_nodes[m_focusNode].m_node.getRefer()));
                } else {
                    JOptionPane.showMessageDialog(this, "Sorry, there is no " + "available Decision Tree to " + "perform this operation on.", "Sorry!", JOptionPane.WARNING_MESSAGE);
                }
            } else {
                JOptionPane.showMessageDialog(this, "Error, there is no " + "selected Node to perform this " + "operation on.", "Error!", JOptionPane.ERROR_MESSAGE);
            }
        } else if (e.getActionCommand().equals("Remove Child Nodes")) {
            if (m_focusNode >= 0) {
                if (m_listener != null) {
                    m_listener.userCommand(new TreeDisplayEvent(TreeDisplayEvent.REMOVE_CHILDREN, m_nodes[m_focusNode].m_node.getRefer()));
                } else {
                    JOptionPane.showMessageDialog(this, "Sorry, there is no " + "available Decsion Tree to " + "perform this operation on.", "Sorry!", JOptionPane.WARNING_MESSAGE);
                }
            } else {
                JOptionPane.showMessageDialog(this, "Error, there is no " + "selected Node to perform this " + "operation on.", "Error!", JOptionPane.ERROR_MESSAGE);
            }
        } else if (e.getActionCommand().equals("classify_child")) {
            if (m_focusNode >= 0) {
                if (m_listener != null) {
                    m_listener.userCommand(new TreeDisplayEvent(TreeDisplayEvent.CLASSIFY_CHILD, m_nodes[m_focusNode].m_node.getRefer()));
                } else {
                    JOptionPane.showMessageDialog(this, "Sorry, there is no " + "available Decsion Tree to " + "perform this operation on.", "Sorry!", JOptionPane.WARNING_MESSAGE);
                }
            } else {
                JOptionPane.showMessageDialog(this, "Error, there is no " + "selected Node to perform this " + "operation on.", "Error!", JOptionPane.ERROR_MESSAGE);
            }
        } else if (e.getActionCommand().equals("send_instances")) {
            if (m_focusNode >= 0) {
                if (m_listener != null) {
                    m_listener.userCommand(new TreeDisplayEvent(TreeDisplayEvent.SEND_INSTANCES, m_nodes[m_focusNode].m_node.getRefer()));
                } else {
                    JOptionPane.showMessageDialog(this, "Sorry, there is no " + "available Decsion Tree to " + "perform this operation on.", "Sorry!", JOptionPane.WARNING_MESSAGE);
                }
            } else {
                JOptionPane.showMessageDialog(this, "Error, there is no " + "selected Node to perform this " + "operation on.", "Error!", JOptionPane.ERROR_MESSAGE);
            }
        } else if (e.getActionCommand().equals("Accept The Tree")) {
            if (m_listener != null) {
                m_listener.userCommand(new TreeDisplayEvent(TreeDisplayEvent.ACCEPT, null));
            } else {
                JOptionPane.showMessageDialog(this, "Sorry, there is no " + "available Decision Tree to " + "perform this operation on.", "Sorry!", JOptionPane.WARNING_MESSAGE);
            }
        }
    }

    /**
   * Performs the action associated with the ItemEvent.
   *
   * @param e the item event.
   */
    public void itemStateChanged(ItemEvent e) {
        JRadioButtonMenuItem c = (JRadioButtonMenuItem) e.getSource();
        if (c.getActionCommand().equals("Size 24")) {
            changeFontSize(24);
        } else if (c.getActionCommand().equals("Size 22")) {
            changeFontSize(22);
        } else if (c.getActionCommand().equals("Size 20")) {
            changeFontSize(20);
        } else if (c.getActionCommand().equals("Size 18")) {
            changeFontSize(18);
        } else if (c.getActionCommand().equals("Size 16")) {
            changeFontSize(16);
        } else if (c.getActionCommand().equals("Size 14")) {
            changeFontSize(14);
        } else if (c.getActionCommand().equals("Size 12")) {
            changeFontSize(12);
        } else if (c.getActionCommand().equals("Size 10")) {
            changeFontSize(10);
        } else if (c.getActionCommand().equals("Size 8")) {
            changeFontSize(8);
        } else if (c.getActionCommand().equals("Size 6")) {
            changeFontSize(6);
        } else if (c.getActionCommand().equals("Size 4")) {
            changeFontSize(4);
        } else if (c.getActionCommand().equals("Size 2")) {
            changeFontSize(2);
        } else if (c.getActionCommand().equals("Size 1")) {
            changeFontSize(1);
        } else if (c.getActionCommand().equals("Hide Descendants")) {
        }
    }

    /**
   * Does nothing.
   * @param e the mouse event.
   */
    public void mouseClicked(MouseEvent e) {
        if (m_clickAvailable) {
            int s = -1;
            for (int noa = 0; noa < m_numNodes; noa++) {
                if (m_nodes[noa].m_quad == 18) {
                    calcScreenCoords(noa);
                    if (e.getX() <= m_nodes[noa].m_center + m_nodes[noa].m_side && e.getX() >= m_nodes[noa].m_center - m_nodes[noa].m_side && e.getY() >= m_nodes[noa].m_top && e.getY() <= m_nodes[noa].m_top + m_nodes[noa].m_height) {
                        s = noa;
                    }
                    m_nodes[noa].m_top = 32000;
                }
            }
            m_focusNode = s;
            if (m_focusNode != -1) {
                if (m_listener != null) {
                    actionPerformed(new ActionEvent(this, 32000, "Create Child Nodes"));
                } else {
                    actionPerformed(new ActionEvent(this, 32000, "Visualize The Node"));
                }
            }
        }
    }

    /**
   * Determines what action the user wants to perform.
   *
   * @param e the mouse event.
   */
    public void mousePressed(MouseEvent e) {
        m_frameLimiter.setRepeats(true);
        if ((e.getModifiers() & e.BUTTON1_MASK) != 0 && !e.isAltDown() && m_mouseState == 0 && m_scaling == 0) {
            if (((e.getModifiers() & e.CTRL_MASK) != 0) && ((e.getModifiers() & e.SHIFT_MASK) == 0)) {
                m_mouseState = 2;
            } else if (((e.getModifiers() & e.SHIFT_MASK) != 0) && ((e.getModifiers() & e.CTRL_MASK) == 0)) {
                m_oldMousePos.width = e.getX();
                m_oldMousePos.height = e.getY();
                m_newMousePos.width = e.getX();
                m_newMousePos.height = e.getY();
                m_mouseState = 3;
                Graphics g = getGraphics();
                g.setColor(Color.black);
                g.setXORMode(Color.white);
                g.drawRect(m_oldMousePos.width, m_oldMousePos.height, m_newMousePos.width - m_oldMousePos.width, m_newMousePos.height - m_oldMousePos.height);
                g.dispose();
            } else {
                m_oldMousePos.width = e.getX();
                m_oldMousePos.height = e.getY();
                m_newMousePos.width = e.getX();
                m_newMousePos.height = e.getY();
                m_mouseState = 1;
                m_frameLimiter.start();
            }
        } else if ((e.getButton() == MouseEvent.BUTTON1) && e.isAltDown() && e.isShiftDown() && !e.isControlDown()) {
            saveComponent();
        } else if (m_mouseState == 0 && m_scaling == 0) {
        }
    }

    /**
   * Performs the final stages of what the user wants to perform.
   *
   * @param e the mouse event.
   */
    public void mouseReleased(MouseEvent e) {
        if (m_mouseState == 1) {
            m_clickAvailable = true;
        } else {
            m_clickAvailable = false;
        }
        if (m_mouseState == 2 && mouseInBounds(e)) {
            m_mouseState = 0;
            Dimension ns = new Dimension(m_viewSize.width / 2, m_viewSize.height / 2);
            if (ns.width < 10) {
                ns.width = 10;
            }
            if (ns.height < 10) {
                ns.height = 10;
            }
            Dimension d = getSize();
            Dimension np = new Dimension((int) (d.width / 2 - ((double) d.width / 2 - m_viewPos.width) / 2), (int) (d.height / 2 - ((double) d.height / 2 - m_viewPos.height) / 2));
            animateScaling(np, ns, 10);
        } else if (m_mouseState == 3) {
            m_mouseState = 0;
            Graphics g = getGraphics();
            g.setColor(Color.black);
            g.setXORMode(Color.white);
            g.drawRect(m_oldMousePos.width, m_oldMousePos.height, m_newMousePos.width - m_oldMousePos.width, m_newMousePos.height - m_oldMousePos.height);
            g.dispose();
            int cw = m_newMousePos.width - m_oldMousePos.width;
            int ch = m_newMousePos.height - m_oldMousePos.height;
            if (cw >= 1 && ch >= 1) {
                if (mouseInBounds(e) && (getSize().width / cw) <= 6 && (getSize().height / ch) <= 6) {
                    Dimension ns = new Dimension();
                    Dimension np = new Dimension();
                    double nvsw = getSize().width / (double) (cw);
                    double nvsh = getSize().height / (double) (ch);
                    np.width = (int) ((m_oldMousePos.width - m_viewPos.width) * -nvsw);
                    np.height = (int) ((m_oldMousePos.height - m_viewPos.height) * -nvsh);
                    ns.width = (int) (m_viewSize.width * nvsw);
                    ns.height = (int) (m_viewSize.height * nvsh);
                    animateScaling(np, ns, 10);
                }
            }
        } else if (m_mouseState == 0 && m_scaling == 0) {
            m_mouseState = 0;
            setFont(new Font("A Name", 0, 12));
            int s = -1;
            for (int noa = 0; noa < m_numNodes; noa++) {
                if (m_nodes[noa].m_quad == 18) {
                    calcScreenCoords(noa);
                    if (e.getX() <= m_nodes[noa].m_center + m_nodes[noa].m_side && e.getX() >= m_nodes[noa].m_center - m_nodes[noa].m_side && e.getY() >= m_nodes[noa].m_top && e.getY() <= m_nodes[noa].m_top + m_nodes[noa].m_height) {
                        s = noa;
                    }
                    m_nodes[noa].m_top = 32000;
                }
            }
            if (s == -1) {
                m_winMenu.show(this, e.getX(), e.getY());
            } else {
                m_focusNode = s;
                m_nodeMenu.show(this, e.getX(), e.getY());
            }
            setFont(m_currentFont);
        } else if (m_mouseState == 1) {
            m_mouseState = 0;
            m_frameLimiter.stop();
            repaint();
        }
    }

    /**
   * Checks to see if the coordinates of the mouse lie on this JPanel.
   *
   * @param e the mouse event.
   * @return true if the mouse lies on this JPanel. 
   */
    private boolean mouseInBounds(MouseEvent e) {
        if (e.getX() < 0 || e.getY() < 0 || e.getX() > getSize().width || e.getY() > getSize().height) {
            return false;
        }
        return true;
    }

    /**
   * Performs intermediate updates to what the user wishes to do.
   *
   * @param e the mouse event.
   */
    public void mouseDragged(MouseEvent e) {
        if (m_mouseState == 1) {
            m_oldMousePos.width = m_newMousePos.width;
            m_oldMousePos.height = m_newMousePos.height;
            m_newMousePos.width = e.getX();
            m_newMousePos.height = e.getY();
            m_viewPos.width += m_newMousePos.width - m_oldMousePos.width;
            m_viewPos.height += m_newMousePos.height - m_oldMousePos.height;
        } else if (m_mouseState == 3) {
            Graphics g = getGraphics();
            g.setColor(Color.black);
            g.setXORMode(Color.white);
            g.drawRect(m_oldMousePos.width, m_oldMousePos.height, m_newMousePos.width - m_oldMousePos.width, m_newMousePos.height - m_oldMousePos.height);
            m_newMousePos.width = e.getX();
            m_newMousePos.height = e.getY();
            g.drawRect(m_oldMousePos.width, m_oldMousePos.height, m_newMousePos.width - m_oldMousePos.width, m_newMousePos.height - m_oldMousePos.height);
            g.dispose();
        }
    }

    /**
   * Does nothing.
   *
   * @param e the mouse event.
   */
    public void mouseMoved(MouseEvent e) {
    }

    /**
   * Does nothing.
   *
   * @param e the mouse event.
   */
    public void mouseEntered(MouseEvent e) {
    }

    /**
   * Does nothing.
   * 
   * @param e the mouse event.
   */
    public void mouseExited(MouseEvent e) {
    }

    /**
   * Set the highlight for the node with the given id
   * @param id the id of the node to set the highlight for
   */
    public void setHighlight(String id) {
        for (int noa = 0; noa < m_numNodes; noa++) {
            if (id.equals(m_nodes[noa].m_node.getRefer())) {
                m_highlightNode = noa;
            }
        }
        repaint();
    }

    /**
   * Updates the screen contents.
   *
   * @param g the drawing surface.
   */
    public void paintComponent(Graphics g) {
        g.clearRect(0, 0, getSize().width, getSize().height);
        g.setClip(3, 7, getWidth() - 6, getHeight() - 10);
        painter(g);
        g.setClip(0, 0, getWidth(), getHeight());
    }

    /**
   * Draws the tree to the graphics context
   *
   * @param g the drawing surface.
   */
    private void painter(Graphics g) {
        double left_clip = (double) (-m_viewPos.width - 50) / m_viewSize.width;
        double right_clip = (double) (getSize().width - m_viewPos.width + 50) / m_viewSize.width;
        double top_clip = (double) (-m_viewPos.height - 50) / m_viewSize.height;
        double bottom_clip = (double) (getSize().height - m_viewPos.height + 50) / m_viewSize.height;
        Edge e;
        Node r, s;
        double ncent, ntop;
        int row = 0, col = 0, pq, cq;
        for (int noa = 0; noa < m_numNodes; noa++) {
            r = m_nodes[noa].m_node;
            if (m_nodes[noa].m_change) {
                ntop = r.getTop();
                if (ntop < top_clip) {
                    row = 8;
                } else if (ntop > bottom_clip) {
                    row = 32;
                } else {
                    row = 16;
                }
            }
            ncent = r.getCenter();
            if (ncent < left_clip) {
                col = 4;
            } else if (ncent > right_clip) {
                col = 1;
            } else {
                col = 2;
            }
            m_nodes[noa].m_quad = row | col;
            if (m_nodes[noa].m_parent >= 0) {
                pq = m_nodes[m_edges[m_nodes[noa].m_parent].m_parent].m_quad;
                cq = m_nodes[noa].m_quad;
                if ((cq & 8) == 8) {
                } else if ((pq & 32) == 32) {
                } else if ((cq & 4) == 4 && (pq & 4) == 4) {
                } else if ((cq & 1) == 1 && (pq & 1) == 1) {
                } else {
                    drawLine(m_nodes[noa].m_parent, g);
                }
            }
        }
        for (int noa = 0; noa < m_numNodes; noa++) {
            if (m_nodes[noa].m_quad == 18) {
                drawNode(noa, g);
            }
        }
        if (m_highlightNode >= 0 && m_highlightNode < m_numNodes) {
            if (m_nodes[m_highlightNode].m_quad == 18) {
                Color acol = m_nodes[m_highlightNode].m_node.getColor();
                g.setColor(new Color((acol.getRed() + 125) % 256, (acol.getGreen() + 125) % 256, (acol.getBlue() + 125) % 256));
                if (m_nodes[m_highlightNode].m_node.getShape() == 1) {
                    g.drawRect(m_nodes[m_highlightNode].m_center - m_nodes[m_highlightNode].m_side, m_nodes[m_highlightNode].m_top, m_nodes[m_highlightNode].m_width, m_nodes[m_highlightNode].m_height);
                    g.drawRect(m_nodes[m_highlightNode].m_center - m_nodes[m_highlightNode].m_side + 1, m_nodes[m_highlightNode].m_top + 1, m_nodes[m_highlightNode].m_width - 2, m_nodes[m_highlightNode].m_height - 2);
                } else if (m_nodes[m_highlightNode].m_node.getShape() == 2) {
                    g.drawOval(m_nodes[m_highlightNode].m_center - m_nodes[m_highlightNode].m_side, m_nodes[m_highlightNode].m_top, m_nodes[m_highlightNode].m_width, m_nodes[m_highlightNode].m_height);
                    g.drawOval(m_nodes[m_highlightNode].m_center - m_nodes[m_highlightNode].m_side + 1, m_nodes[m_highlightNode].m_top + 1, m_nodes[m_highlightNode].m_width - 2, m_nodes[m_highlightNode].m_height - 2);
                }
            }
        }
        for (int noa = 0; noa < m_numNodes; noa++) {
            m_nodes[noa].m_top = 32000;
        }
    }

    /**
   * Determines the attributes of the node and draws it.
   *
   * @param n A subscript identifying the node in <i>nodes</i> array
   * @param g The drawing surface
   */
    private void drawNode(int n, Graphics g) {
        g.setColor(m_nodes[n].m_node.getColor());
        g.setPaintMode();
        calcScreenCoords(n);
        int x = m_nodes[n].m_center - m_nodes[n].m_side;
        int y = m_nodes[n].m_top;
        if (m_nodes[n].m_node.getShape() == 1) {
            g.fill3DRect(x, y, m_nodes[n].m_width, m_nodes[n].m_height, true);
            drawText(x, y, n, false, g);
        } else if (m_nodes[n].m_node.getShape() == 2) {
            g.fillOval(x, y, m_nodes[n].m_width, m_nodes[n].m_height);
            drawText(x, y + (int) (m_nodes[n].m_height * .15), n, false, g);
        }
    }

    /**
   * Determines the attributes of the edge and draws it.
   *
   * @param e A subscript identifying the edge in <i>edges</i> array.
   * @param g The drawing surface.
   */
    private void drawLine(int e, Graphics g) {
        int p = m_edges[e].m_parent;
        int c = m_edges[e].m_child;
        calcScreenCoords(c);
        calcScreenCoords(p);
        g.setColor(Color.black);
        g.setPaintMode();
        if (m_currentFont.getSize() < 2) {
            g.drawLine(m_nodes[p].m_center, m_nodes[p].m_top + m_nodes[p].m_height, m_nodes[c].m_center, m_nodes[c].m_top);
        } else {
            int e_width = m_nodes[c].m_center - m_nodes[p].m_center;
            int e_height = m_nodes[c].m_top - (m_nodes[p].m_top + m_nodes[p].m_height);
            int e_width2 = e_width / 2;
            int e_height2 = e_height / 2;
            int e_centerx = m_nodes[p].m_center + e_width2;
            int e_centery = m_nodes[p].m_top + m_nodes[p].m_height + e_height2;
            int e_offset = m_edges[e].m_tb;
            int tmp = (int) (((double) e_width / e_height) * (e_height2 - e_offset)) + m_nodes[p].m_center;
            drawText(e_centerx - m_edges[e].m_side, e_centery - e_offset, e, true, g);
            if (tmp > (e_centerx - m_edges[e].m_side) && tmp < (e_centerx + m_edges[e].m_side)) {
                g.drawLine(m_nodes[p].m_center, m_nodes[p].m_top + m_nodes[p].m_height, tmp, e_centery - e_offset);
                g.drawLine(e_centerx * 2 - tmp, e_centery + e_offset, m_nodes[c].m_center, m_nodes[c].m_top);
            } else {
                e_offset = m_edges[e].m_side;
                if (e_width < 0) {
                    e_offset *= -1;
                }
                tmp = (int) (((double) e_height / e_width) * (e_width2 - e_offset)) + m_nodes[p].m_top + m_nodes[p].m_height;
                g.drawLine(m_nodes[p].m_center, m_nodes[p].m_top + m_nodes[p].m_height, e_centerx - e_offset, tmp);
                g.drawLine(e_centerx + e_offset, e_centery * 2 - tmp, m_nodes[c].m_center, m_nodes[c].m_top);
            }
        }
    }

    /**
   * Draws the text for either an Edge or a Node.
   *
   * @param x1 the left side of the text area.
   * @param y1 the top of the text area.
   * @param s A subscript identifying either a Node or Edge.
   * @param e_or_n Distinguishes whether it is a node or edge.
   * @param g The drawing surface.
   */
    private void drawText(int x1, int y1, int s, boolean e_or_n, Graphics g) {
        g.setPaintMode();
        g.setColor(Color.black);
        String st;
        if (e_or_n) {
            Edge e = m_edges[s].m_edge;
            for (int noa = 0; (st = e.getLine(noa)) != null; noa++) {
                g.drawString(st, (m_edges[s].m_width - m_fontSize.stringWidth(st)) / 2 + x1, y1 + (noa + 1) * m_fontSize.getHeight());
            }
        } else {
            Node e = m_nodes[s].m_node;
            for (int noa = 0; (st = e.getLine(noa)) != null; noa++) {
                g.drawString(st, (m_nodes[s].m_width - m_fontSize.stringWidth(st)) / 2 + x1, y1 + (noa + 1) * m_fontSize.getHeight());
            }
        }
    }

    /**
   * Converts the internal coordinates of the node found from <i>n</i>
   * and converts them to the actual screen coordinates.
   *
   * @param n A subscript identifying the Node.
   */
    private void calcScreenCoords(int n) {
        if (m_nodes[n].m_top == 32000) {
            m_nodes[n].m_top = ((int) (m_nodes[n].m_node.getTop() * m_viewSize.height)) + m_viewPos.height;
            m_nodes[n].m_center = ((int) (m_nodes[n].m_node.getCenter() * m_viewSize.width)) + m_viewPos.width;
        }
    }

    /**
   * This Calculates the minimum size of the tree which will prevent any text
   * overlapping and make it readable, and then set the size of the tree to 
   * this.
   */
    private void autoScale() {
        int dist;
        Node ln, rn;
        Dimension temp = new Dimension(10, 10);
        if (m_numNodes <= 1) {
            return;
        }
        dist = (m_nodes[0].m_height + 40) * m_numLevels;
        if (dist > temp.height) {
            temp.height = dist;
        }
        for (int noa = 0; noa < m_numNodes - 1; noa++) {
            calcScreenCoords(noa);
            calcScreenCoords(noa + 1);
            if (m_nodes[noa + 1].m_change) {
            } else {
                dist = m_nodes[noa + 1].m_center - m_nodes[noa].m_center;
                if (dist <= 0) {
                    dist = 1;
                }
                dist = ((6 + m_nodes[noa].m_side + m_nodes[noa + 1].m_side) * m_viewSize.width) / dist;
                if (dist > temp.width) {
                    temp.width = dist;
                }
            }
            dist = (m_nodes[noa + 1].m_height + 40) * m_numLevels;
            if (dist > temp.height) {
                temp.height = dist;
            }
        }
        int y1, y2, xa, xb;
        y1 = m_nodes[m_edges[0].m_parent].m_top;
        y2 = m_nodes[m_edges[0].m_child].m_top;
        dist = y2 - y1;
        if (dist <= 0) {
            dist = 1;
        }
        dist = ((60 + m_edges[0].m_height + m_nodes[m_edges[0].m_parent].m_height) * m_viewSize.height) / dist;
        if (dist > temp.height) {
            temp.height = dist;
        }
        for (int noa = 0; noa < m_numNodes - 2; noa++) {
            if (m_nodes[m_edges[noa + 1].m_child].m_change) {
            } else {
                xa = m_nodes[m_edges[noa].m_child].m_center - m_nodes[m_edges[noa].m_parent].m_center;
                xa /= 2;
                xa += m_nodes[m_edges[noa].m_parent].m_center;
                xb = m_nodes[m_edges[noa + 1].m_child].m_center - m_nodes[m_edges[noa + 1].m_parent].m_center;
                xb /= 2;
                xb += m_nodes[m_edges[noa + 1].m_parent].m_center;
                dist = xb - xa;
                if (dist <= 0) {
                    dist = 1;
                }
                dist = ((12 + m_edges[noa].m_side + m_edges[noa + 1].m_side) * m_viewSize.width) / dist;
                if (dist > temp.width) {
                    temp.width = dist;
                }
            }
            y1 = m_nodes[m_edges[noa + 1].m_parent].m_top;
            y2 = m_nodes[m_edges[noa + 1].m_child].m_top;
            dist = y2 - y1;
            if (dist <= 0) {
                dist = 1;
            }
            dist = ((60 + m_edges[noa + 1].m_height + m_nodes[m_edges[noa + 1].m_parent].m_height) * m_viewSize.height) / dist;
            if (dist > temp.height) {
                temp.height = dist;
            }
        }
        Dimension e = getSize();
        Dimension np = new Dimension();
        np.width = (int) (e.width / 2 - (((double) e.width / 2) - m_viewPos.width) / ((double) m_viewSize.width) * (double) temp.width);
        np.height = (int) (e.height / 2 - (((double) e.height / 2) - m_viewPos.height) / ((double) m_viewSize.height) * (double) temp.height);
        for (int noa = 0; noa < m_numNodes; noa++) {
            m_nodes[noa].m_top = 32000;
        }
        animateScaling(np, temp, 10);
    }

    /**
   * This will increment the size and position of the tree towards the 
   * desired size and position
   * a little (depending on the value of <i>frames</i>) everytime it is called.
   *
   * @param n_pos The final position of the tree wanted.
   * @param n_size The final size of the tree wanted.
   * @param frames The number of frames that shall occur before the final 
   * size and pos is reached.
   */
    private void animateScaling(Dimension n_pos, Dimension n_size, int frames) {
        if (frames == 0) {
            System.out.println("the timer didn't end in time");
            m_scaling = 0;
        } else {
            if (m_scaling == 0) {
                m_frameLimiter.start();
                m_nViewPos.width = n_pos.width;
                m_nViewPos.height = n_pos.height;
                m_nViewSize.width = n_size.width;
                m_nViewSize.height = n_size.height;
                m_scaling = frames;
            }
            int s_w = (n_size.width - m_viewSize.width) / frames;
            int s_h = (n_size.height - m_viewSize.height) / frames;
            int p_w = (n_pos.width - m_viewPos.width) / frames;
            int p_h = (n_pos.height - m_viewPos.height) / frames;
            m_viewSize.width += s_w;
            m_viewSize.height += s_h;
            m_viewPos.width += p_w;
            m_viewPos.height += p_h;
            repaint();
            m_scaling--;
            if (m_scaling == 0) {
                m_frameLimiter.stop();
            }
        }
    }

    /**
   * This will change the font size for displaying the tree to the one 
   * specified.
   *
   * @param s The new pointsize of the font.
   */
    private void changeFontSize(int s) {
        setFont(m_currentFont = new Font("A Name", 0, s));
        m_fontSize = getFontMetrics(getFont());
        Dimension d;
        for (int noa = 0; noa < m_numNodes; noa++) {
            d = m_nodes[noa].m_node.stringSize(m_fontSize);
            if (m_nodes[noa].m_node.getShape() == 1) {
                m_nodes[noa].m_height = d.height + 10;
                m_nodes[noa].m_width = d.width + 8;
                m_nodes[noa].m_side = m_nodes[noa].m_width / 2;
            } else if (m_nodes[noa].m_node.getShape() == 2) {
                m_nodes[noa].m_height = (int) ((d.height + 2) * 1.6);
                m_nodes[noa].m_width = (int) ((d.width + 2) * 1.6);
                m_nodes[noa].m_side = m_nodes[noa].m_width / 2;
            }
            if (noa < m_numNodes - 1) {
                d = m_edges[noa].m_edge.stringSize(m_fontSize);
                m_edges[noa].m_height = d.height + 8;
                m_edges[noa].m_width = d.width + 8;
                m_edges[noa].m_side = m_edges[noa].m_width / 2;
                m_edges[noa].m_tb = m_edges[noa].m_height / 2;
            }
        }
    }

    /**
   * This will fill two arrays with the Nodes and Edges from the tree
   * into a particular order.
   *
   * @param t The top Node of the tree.
   * @param l An array that has already been allocated, to be filled.
   * @param k An array that has already been allocated, to be filled.
   */
    private void arrayFill(Node t, NodeInfo[] l, EdgeInfo[] k) {
        if (t == null || l == null) {
            System.exit(1);
        }
        Edge e;
        Node r, s;
        l[0] = new NodeInfo();
        l[0].m_node = t;
        l[0].m_parent = -1;
        l[0].m_change = true;
        int floater;
        int free_space = 1;
        double height = t.getTop();
        for (floater = 0; floater < free_space; floater++) {
            r = l[floater].m_node;
            for (int noa = 0; (e = r.getChild(noa)) != null; noa++) {
                s = e.getTarget();
                l[free_space] = new NodeInfo();
                l[free_space].m_node = s;
                l[free_space].m_parent = free_space - 1;
                k[free_space - 1] = new EdgeInfo();
                k[free_space - 1].m_edge = e;
                k[free_space - 1].m_parent = floater;
                k[free_space - 1].m_child = free_space;
                if (height != s.getTop()) {
                    l[free_space].m_change = true;
                    height = s.getTop();
                } else {
                    l[free_space].m_change = false;
                }
                free_space++;
            }
        }
    }

    /**
   * Internal Class for containing display information about a Node. 
   */
    private class NodeInfo {

        /** The y pos of the node on screen. */
        int m_top = 32000;

        /** The x pos of the node on screen. */
        int m_center;

        /** The offset to get to the left or right of the node. */
        int m_side;

        /** The width of the node. */
        int m_width;

        /** The height of the node. */
        int m_height;

        /** True if the node is at the start (left) of a new level (not sibling 
     * group). */
        boolean m_change;

        /** The subscript number of the Nodes parent. */
        int m_parent;

        /** The rough position of the node relative to the screen. */
        int m_quad;

        /** The Node itself. */
        Node m_node;
    }

    /**
   * Internal Class for containing display information about an Edge. 
   */
    private class EdgeInfo {

        /** The parent subscript (for a Node). */
        int m_parent;

        /** The child subscript (for a Node). */
        int m_child;

        /** The distance from the center of the text to either side. */
        int m_side;

        /** The distance from the center of the text to top or bottom. */
        int m_tb;

        /** The width of the text. */
        int m_width;

        /** The height of the text. */
        int m_height;

        /** The Edge itself. */
        Edge m_edge;
    }

    /**
   * Main method for testing this class.
   * @param args first argument should be the name of a file that contains
   * a tree discription in dot format.
   */
    public static void main(String[] args) {
        try {
            weka.core.logging.Logger.log(weka.core.logging.Logger.Level.INFO, "Logging started");
            TreeBuild builder = new TreeBuild();
            Node top = null;
            NodePlace arrange = new PlaceNode2();
            top = builder.create(new FileReader(args[0]));
            int num = top.getCount(top, 0);
            TreeVisualizer a = new TreeVisualizer(null, top, arrange);
            a.setSize(800, 600);
            JFrame f;
            f = new JFrame();
            Container contentPane = f.getContentPane();
            contentPane.add(a);
            f.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
            f.setSize(800, 600);
            f.setVisible(true);
        } catch (IOException e) {
        }
    }
}
