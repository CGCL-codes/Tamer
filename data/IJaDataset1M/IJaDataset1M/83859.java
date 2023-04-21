package freestyleLearning.homeCore.learningUnitsManager.unitmap;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Font;
import java.awt.Toolkit;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.geom.Rectangle2D;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Hashtable;
import java.util.List;
import java.util.Map;
import java.util.Set;
import javax.swing.AbstractButton;
import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.UIManager;
import javax.swing.border.MatteBorder;
import org.jgraph.JGraph;
import org.jgraph.event.GraphSelectionEvent;
import org.jgraph.event.GraphSelectionListener;
import org.jgraph.graph.CellView;
import org.jgraph.graph.DefaultCellViewFactory;
import org.jgraph.graph.DefaultEdge;
import org.jgraph.graph.DefaultGraphCell;
import org.jgraph.graph.DefaultPort;
import org.jgraph.graph.GraphConstants;
import org.jgraph.graph.GraphLayoutCache;
import org.jgraph.graph.GraphModel;
import freestyleLearning.homeCore.learningUnitsManager.data.xmlBindingSubclasses.FSLLearningUnitDescriptor;
import freestyleLearning.learningUnitViewAPI.elementInteractionPanel.FSLLearningUnitViewElementInteractionButton;
import freestyleLearningGroup.independent.gui.FLGEffectPanel;
import freestyleLearningGroup.independent.gui.FLGImageUtility;
import freestyleLearningGroup.independent.util.FLGInternationalization;

public class UnitmapProgressFrame extends JFrame implements GraphSelectionListener {

    FLGInternationalization internationalization;

    UnitmapFrame unitmapFrame = null;

    DefaultGraphCell[] prerequisites;

    protected JGraph graph;

    AbstractButton showLearningUnitButton;

    JLabel overallProgress;

    private int maxCellWidth;

    /**
	 * creates the UnitmapProgressFrame
	 * @param model on which the learning progress is based
	 * @param frame
	 * @param internationalization
	 */
    public UnitmapProgressFrame(GraphModel model, UnitmapFrame frame, FLGInternationalization internationalization) {
        this.unitmapFrame = frame;
        this.internationalization = internationalization;
        setTitle(internationalization.getString("progressView.title"));
        URL jgraphUrl = Unitmap.class.getClassLoader().getResource("freestyleLearning/homeCore/learningUnitsManager/unitmap/images/unitmap_icon.gif");
        if (jgraphUrl != null) {
            ImageIcon jgraphIcon = new ImageIcon(jgraphUrl);
            this.setIconImage(jgraphIcon.getImage());
        }
        setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
        addWindowListener(new WindowAdapter() {

            public void windowClosing(WindowEvent e) {
                showUnitmapFrame();
            }
        });
        GraphLayoutCache view = new GraphLayoutCache(model, new DefaultCellViewFactory(), true);
        graph = new JGraph(model, view);
        graph.setMoveable(false);
        graph.setAntiAliased(true);
        graph.getSelectionModel().addGraphSelectionListener(this);
        graph.addMouseListener(new GraphMouseListener());
        populateContentPane();
    }

    /**
	 * builds the UI.
	 * 
	 * It can be overwritten if there shall be more functionality
	 * available in the progress view.
	 */
    protected void populateContentPane() {
        getContentPane().setLayout(new BorderLayout());
        JScrollPane scrollPane = new javax.swing.JScrollPane(graph);
        scrollPane.setBorder(new javax.swing.border.EmptyBorder(new java.awt.Insets(6, 3, 6, 6)));
        scrollPane.setBackground((Color) UIManager.get("FSLMainFrameColor4"));
        getContentPane().add(scrollPane, BorderLayout.CENTER);
        FLGEffectPanel buttonPanel = new freestyleLearningGroup.independent.gui.FLGEffectPanel("FSLMainFrameColor1", "FSLMainFrameColor4", true);
        buttonPanel.setLayout(new java.awt.BorderLayout());
        java.awt.Image showLearningUnitButtonImage = FLGImageUtility.loadImageAndWait(getClass().getClassLoader().getResource("freestyleLearning/homeCore/learningUnitsManager/unitmap/images/showLearningUnit.gif"));
        showLearningUnitButton = new FSLLearningUnitViewElementInteractionButton(showLearningUnitButtonImage);
        showLearningUnitButton.setToolTipText(internationalization.getString("progressView.button.showLearningUnit"));
        showLearningUnitButton.addActionListener(new java.awt.event.ActionListener() {

            public void actionPerformed(java.awt.event.ActionEvent e) {
                loadSelectedLearningUnit();
            }
        });
        buttonPanel.add(showLearningUnitButton, BorderLayout.NORTH);
        showLearningUnitButton.setEnabled(false);
        java.awt.Image toUnitmapViewButtonImage = FLGImageUtility.loadImageAndWait(getClass().getClassLoader().getResource("freestyleLearning/homeCore/learningUnitsManager/unitmap/images/toUnitmapView.gif"));
        AbstractButton toUnitmapViewButton = new FSLLearningUnitViewElementInteractionButton(toUnitmapViewButtonImage);
        toUnitmapViewButton.setToolTipText(internationalization.getString("progressView.button.toUnitmapView"));
        toUnitmapViewButton.addActionListener(new java.awt.event.ActionListener() {

            public void actionPerformed(java.awt.event.ActionEvent e) {
                showUnitmapFrame();
            }
        });
        buttonPanel.add(toUnitmapViewButton, BorderLayout.CENTER);
        getContentPane().add(buttonPanel, BorderLayout.EAST);
        JPanel progressPanel = new JPanel();
        progressPanel.setLayout(new BorderLayout());
        progressPanel.setBackground(Color.WHITE);
        progressPanel.setBorder(new MatteBorder(new java.awt.Insets(6, 6, 6, 0), (Color) UIManager.get("FSLMainFrameColor4")));
        overallProgress = new JLabel();
        Font font = overallProgress.getFont();
        overallProgress.setFont(new Font(font.getName(), Font.BOLD, 32));
        progressPanel.add(overallProgress, BorderLayout.WEST);
        getContentPane().add(progressPanel, BorderLayout.WEST);
        setSize((int) Toolkit.getDefaultToolkit().getScreenSize().getWidth(), 200);
        setVisible(false);
    }

    /**
	 * displays the progress view making only the prerequisites given by
	 * <code>prerequisiteList</code> visible.
	 * 
	 * @param prerequisiteList containing prerequisites and target learning unit
	 */
    public void showProgressFrame(List prerequisiteList) {
        unitmapFrame.setVisible(false);
        this.prerequisites = (DefaultGraphCell[]) prerequisiteList.toArray(new DefaultGraphCell[0]);
        GraphLayoutCache view = new GraphLayoutCache(graph.getModel(), new DefaultCellViewFactory(), true);
        graph.setGraphLayoutCache(view);
        graph.getGraphLayoutCache().setAllAttributesLocal(true);
        graph.getGraphLayoutCache().setVisible(prerequisiteList.toArray(), true);
        maxCellWidth = 0;
        int height = getGraphHeight(new DefaultGraphCell[] { (DefaultGraphCell) prerequisiteList.get(0) }, -1);
        layoutNodes(new DefaultGraphCell[] { (DefaultGraphCell) prerequisiteList.get(0) }, height, 0);
        calculateOverallProgressStatus();
        setVisible(true);
    }

    private int getGraphHeight(DefaultGraphCell[] nodes, int height) {
        if (nodes.length > 0) height++;
        for (int i = 0; i < nodes.length; i++) {
            Rectangle2D bounds = graph.getCellBounds(nodes[i]);
            if (bounds.getWidth() > maxCellWidth) {
                maxCellWidth = (int) bounds.getWidth();
            }
            DefaultGraphCell[] subnodes = getSubnodes(nodes[i]);
            int newHeight = getGraphHeight(subnodes, height);
            if (newHeight > height) height = newHeight;
        }
        return height;
    }

    /**
	 * updates the label showing the overall progress status.
	 */
    public void calculateOverallProgressStatus() {
        double progressStatus = 0;
        for (int i = 0; i < prerequisites.length; i++) {
            if (prerequisites[i].getUserObject() instanceof FSLLearningUnitDescriptor) {
                FSLLearningUnitDescriptor learningUnit = (FSLLearningUnitDescriptor) prerequisites[i].getUserObject();
                double unitProgressStatus = unitmapFrame.getGraph().getLearningUnitsManager().getLearningUnitProgressStatus(learningUnit.getId());
                if (unitProgressStatus >= 0) progressStatus += unitmapFrame.getGraph().getLearningUnitsManager().getLearningUnitProgressStatus(learningUnit.getId());
            }
        }
        progressStatus /= prerequisites.length;
        overallProgress.setText(String.valueOf((int) (progressStatus * 100)) + "%");
    }

    /**
	 * recursively layouts the nodes in the progress view.
	 * 
	 * A level-based layout is used here, which is designed
	 * to use little space in the vertical axis. The nodes are
	 * arranged from right to left starting with the "root" node.
	 * Of course the method can be overwritten to provide another layout.
	 * 
	 * @param nodes to layout (or simply the root node)
	 * @param xLevel where to place the first node on the x-Axis
	 * @param yLevel where to place the first node on the y-Axis
	 * @return the y-Level of the next x-Level
	 */
    protected int layoutNodes(DefaultGraphCell[] nodes, int xLevel, int yLevel) {
        int xOffset = maxCellWidth + 60, yOffset = 90;
        int nextX_yLevel = yLevel;
        for (int i = 0; i < nodes.length; i++) {
            placeNode(nodes[i], xLevel, yLevel, xOffset, yOffset);
            DefaultGraphCell[] subnodes = getSubnodes(nodes[i]);
            nextX_yLevel = layoutNodes(subnodes, xLevel - 1, nextX_yLevel);
            yLevel++;
        }
        return yLevel;
    }

    private DefaultGraphCell[] getSubnodes(DefaultGraphCell node) {
        DefaultPort port = (DefaultPort) node.getFirstChild();
        DefaultEdge[] edges = (DefaultEdge[]) port.getEdges().toArray(new DefaultEdge[0]);
        ArrayList subnodes = new ArrayList();
        for (int k = 0; k < edges.length; k++) {
            DefaultGraphCell subnode = (DefaultGraphCell) graph.getModel().getParent(graph.getModel().getSource(edges[k]));
            if (!subnode.equals(node)) {
                subnodes.add(subnode);
            }
        }
        return (DefaultGraphCell[]) subnodes.toArray(new DefaultGraphCell[0]);
    }

    /**
	 * places the node at [(xLevel * xOffset + 10), (yLevel * yOffset + 10)].
	 * @param node node to place.
	 * @param xLevel 
	 * @param yLevel
	 * @param xOffset space between the nodes on x-axis.
	 * @param yOffset space between the nodes on y-axis.
	 */
    protected void placeNode(DefaultGraphCell node, int xLevel, int yLevel, int xOffset, int yOffset) {
        Map nested = new Hashtable();
        Map attributeMap = new Hashtable();
        Rectangle2D boundsNew = new Rectangle2D.Double(xLevel * xOffset + 10, yLevel * yOffset + 10, 0, 0);
        GraphConstants.setBounds(attributeMap, boundsNew);
        nested.put(node, attributeMap);
        graph.getGraphLayoutCache().edit(nested, null, null, null);
    }

    /**
	 * switches from progress view to unitmap view
	 */
    public void showUnitmapFrame() {
        setVisible(false);
        unitmapFrame.setVisible(true);
    }

    public void valueChanged(GraphSelectionEvent e) {
        boolean enabled = !graph.isSelectionEmpty();
        showLearningUnitButton.setEnabled(enabled);
        if (enabled) showLearningUnitButton.setEnabled(!graph.getModel().isEdge(e.getCell()) && !(((DefaultGraphCell) e.getCell()).getUserObject() instanceof String));
    }

    private class GraphMouseListener extends MouseAdapter {

        public void mouseReleased(MouseEvent e) {
            if (e.getClickCount() == 2) {
                loadSelectedLearningUnit();
            }
        }
    }

    /**
	 * loads the selected learning unit
	 * TODO the loading progress is not displayed correctly
	 */
    protected void loadSelectedLearningUnit() {
        if (!graph.isSelectionEmpty()) {
            new Thread(new Runnable() {

                public void run() {
                    DefaultGraphCell selectedCell = (DefaultGraphCell) graph.getSelectionCell();
                    if (!graph.getModel().isEdge(selectedCell) && !(selectedCell.getUserObject() instanceof String)) {
                        FSLLearningUnitDescriptor learningUnit = (FSLLearningUnitDescriptor) selectedCell.getUserObject();
                        unitmapFrame.getGraph().getLearningUnitsManager().setActiveLearningUnit(learningUnit.getId());
                    }
                }
            }).start();
        }
    }

    public JGraph getGraph() {
        return graph;
    }
}
