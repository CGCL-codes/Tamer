package view;

import java.awt.BorderLayout;
import java.util.Enumeration;
import java.util.HashMap;
import javax.swing.JPanel;
import javax.swing.JSplitPane;
import javax.swing.JTree;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.TreePath;
import plot.Plot;
import plot.PlotData;
import utils.TreeUtils;

@SuppressWarnings("serial")
public class PlotView extends JPanel {

    private JTree dataTree = null;

    private DefaultTreeModel dataTreeModel;

    private Plot plot = null;

    private DefaultMutableTreeNode rootNode;

    HashMap<String, DefaultMutableTreeNode> typeNodeMap = new HashMap<String, DefaultMutableTreeNode>();

    public PlotView(Plot plot) {
        this.plot = plot;
        this.setLayout(new BorderLayout());
        rootNode = new DefaultMutableTreeNode("Data");
        dataTreeModel = new DefaultTreeModel(rootNode);
        dataTree = new JTree(dataTreeModel);
        JSplitPane splitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, dataTree, plot);
        splitPane.setDividerLocation(150);
        splitPane.setOneTouchExpandable(true);
        this.add(splitPane, BorderLayout.CENTER);
        buildDataTree();
    }

    public void addData(PlotData plotData) {
        DefaultMutableTreeNode dataTypeNode = typeNodeMap.get(plotData.getDataType());
        DefaultMutableTreeNode dataNode = new DefaultMutableTreeNode(plotData);
        dataTreeModel.insertNodeInto(dataNode, dataTypeNode, dataTypeNode.getChildCount());
        TreePath typeNodePath = TreeUtils.getPath(dataTypeNode);
        dataTree.expandPath(typeNodePath);
        plot.addData(plotData);
        dataTree.repaint();
        plot.repaint();
    }

    private void buildDataTree() {
        String[] dataTypes = plot.getDataTypes();
        for (String dataType : dataTypes) {
            DefaultMutableTreeNode typeNode = new DefaultMutableTreeNode(dataType);
            typeNodeMap.put(dataType, typeNode);
            rootNode.add(typeNode);
        }
        PlotData[] plotDataArray = new PlotData[0];
        plotDataArray = plot.getPlotDataList().toArray(plotDataArray);
        for (PlotData plotData : plotDataArray) {
            DefaultMutableTreeNode dataTypeNode = typeNodeMap.get(plotData.getDataType());
            DefaultMutableTreeNode dataNode = new DefaultMutableTreeNode(plotData);
            dataTreeModel.insertNodeInto(dataNode, dataTypeNode, dataTypeNode.getChildCount());
        }
        dataTree.expandPath(TreeUtils.getPath(rootNode));
    }

    public DefaultMutableTreeNode findDataNode(PlotData plotData) {
        @SuppressWarnings("unchecked") Enumeration<DefaultMutableTreeNode> treeEnum = rootNode.breadthFirstEnumeration();
        while (treeEnum.hasMoreElements()) {
            DefaultMutableTreeNode node = treeEnum.nextElement();
            if (node.getUserObject() == plotData) {
                return node;
            }
        }
        return null;
    }

    public Plot getPlot() {
        return plot;
    }

    public PlotData getSelectedData() {
        PlotData selectedData = null;
        DefaultMutableTreeNode selectedNode = (DefaultMutableTreeNode) dataTree.getLastSelectedPathComponent();
        Object selectedObject = selectedNode.getUserObject();
        if (selectedNode != null) {
            if (selectedObject instanceof PlotData) {
                selectedData = (PlotData) selectedObject;
            }
        }
        return selectedData;
    }

    public void removeData(PlotData plotData) {
        DefaultMutableTreeNode dataNode = findDataNode(plotData);
        if (dataNode != null) {
            dataTreeModel.removeNodeFromParent(dataNode);
            plot.removeData(plotData);
            dataTree.repaint();
            plot.repaint();
        }
    }

    public void replaceData(PlotData oldData, PlotData newData) {
        DefaultMutableTreeNode dataNode = findDataNode(oldData);
        if (dataNode != null) {
            dataNode.setUserObject(newData);
            plot.replaceData(oldData, newData);
            dataTree.repaint();
            plot.repaint();
        }
    }
}
