package jebl.gui.trees.treeviewer_dev;

import jebl.evolution.graphs.Node;
import jebl.evolution.taxa.Taxon;
import jebl.evolution.trees.*;
import jebl.gui.trees.treeviewer_dev.treelayouts.TreeLayout;
import javax.swing.*;
import java.awt.*;
import java.awt.print.*;

/**
 * @author Andrew Rambaut
 * @version $Id: TreeViewer.java 301 2006-04-17 15:35:01Z rambaut $
 */
public class TreeViewer extends JPanel implements Printable {

    private static final double MAX_ZOOM = 20;

    private static final double MAX_VERTICAL_EXPANSION = 20;

    public enum SearchType {

        CONTAINS("Contains"), STARTS_WITH("Starts with"), ENDS_WITH("Ends with"), MATCHES("Matches");

        SearchType(String name) {
            this.name = name;
        }

        public String toString() {
            return name;
        }

        private final String name;
    }

    /**
	 * Creates new TreeViewer
	 */
    public TreeViewer() {
        this(new TreePane());
    }

    /**
	 * Creates new TreeViewer
	 */
    public TreeViewer(TreePane treePane) {
        setOpaque(false);
        setLayout(new BorderLayout());
        this.treePane = treePane;
        treePane.setAutoscrolls(true);
        JScrollPane scrollPane = new JScrollPane(treePane, JScrollPane.VERTICAL_SCROLLBAR_ALWAYS, JScrollPane.HORIZONTAL_SCROLLBAR_ALWAYS);
        scrollPane.setMinimumSize(new Dimension(150, 150));
        scrollPane.setBorder(null);
        viewport = scrollPane.getViewport();
        add(scrollPane, BorderLayout.CENTER);
        treePaneSelector = new TreePaneSelector(treePane);
    }

    public void setTree(Tree tree) {
        if (tree != null && !(tree instanceof RootedTree)) {
            treePane.setTree(Utils.rootTheTree(tree));
        }
        treePane.setTree((RootedTree) tree);
    }

    public void setTreeLayout(TreeLayout treeLayout) {
        treePane.setTreeLayout(treeLayout);
    }

    public Tree getTree() {
        return treePane.getTree();
    }

    public TreePane getTreePane() {
        return treePane;
    }

    private boolean zoomPending = false;

    private double zoom = 0.0, verticalExpansion = 0.0;

    public void setZoom(double zoom) {
        this.zoom = zoom * MAX_ZOOM;
        refreshZoom();
    }

    public void setVerticalExpansion(double verticalExpansion) {
        this.verticalExpansion = verticalExpansion * MAX_VERTICAL_EXPANSION;
        refreshZoom();
    }

    public boolean verticalExpansionAllowed() {
        return !treePane.maintainAspectRatio();
    }

    private void refreshZoom() {
        setZoom(zoom, zoom + verticalExpansion);
    }

    private void setZoom(double xZoom, double yZoom) {
        Dimension viewportSize = viewport.getViewSize();
        Point position = viewport.getViewPosition();
        Dimension extentSize = viewport.getExtentSize();
        double w = extentSize.getWidth() * (1.0 + xZoom);
        double h = extentSize.getHeight() * (1.0 + yZoom);
        Dimension newSize = new Dimension((int) w, (int) h);
        treePane.setPreferredSize(newSize);
        double cx = position.getX() + (0.5 * extentSize.getWidth());
        double cy = position.getY() + (0.5 * extentSize.getHeight());
        double rx = ((double) newSize.getWidth()) / viewportSize.getWidth();
        double ry = ((double) newSize.getHeight()) / viewportSize.getHeight();
        double px = (cx * rx) - (extentSize.getWidth() / 2.0);
        double py = (cy * ry) - (extentSize.getHeight() / 2.0);
        Point newPosition = new Point((int) px, (int) py);
        viewport.setViewPosition(newPosition);
        treePane.revalidate();
    }

    public void selectTaxa(SearchType searchType, String searchString, boolean caseSensitive) {
        treePane.clearSelection();
        if (searchType == SearchType.MATCHES && !caseSensitive) {
            throw new IllegalArgumentException("Regular expression matching cannot be case-insensitive");
        }
        String query = (caseSensitive ? searchString : searchString.toUpperCase());
        Tree tree = treePane.getTree();
        for (Taxon taxon : tree.getTaxa()) {
            String target = (caseSensitive ? taxon.getName() : taxon.getName().toUpperCase());
            switch(searchType) {
                case CONTAINS:
                    if (target.contains(query)) {
                        treePane.addSelectedTaxon(taxon);
                    }
                    break;
                case STARTS_WITH:
                    if (target.startsWith(query)) {
                        treePane.addSelectedTaxon(taxon);
                    }
                    break;
                case ENDS_WITH:
                    if (target.endsWith(query)) {
                        treePane.addSelectedTaxon(taxon);
                    }
                    break;
                case MATCHES:
                    if (target.matches(query)) {
                        treePane.addSelectedTaxon(taxon);
                    }
                    break;
            }
        }
    }

    public void selectNodes(String attribute, SearchType searchType, String searchString, boolean caseSensitive) {
        treePane.clearSelection();
        if (searchType == SearchType.MATCHES && !caseSensitive) {
            throw new IllegalArgumentException("Regular expression matching cannot be case-insensitive");
        }
        String query = (caseSensitive ? searchString : searchString.toUpperCase());
        Tree tree = treePane.getTree();
        for (Node node : tree.getNodes()) {
            Object value = node.getAttribute(attribute);
            if (value != null) {
                String target = (caseSensitive ? value.toString() : value.toString().toUpperCase());
                switch(searchType) {
                    case CONTAINS:
                        if (target.contains(query)) {
                            treePane.addSelectedNode(node);
                        }
                        break;
                    case STARTS_WITH:
                        if (target.startsWith(query)) {
                            treePane.addSelectedNode(node);
                        }
                        break;
                    case ENDS_WITH:
                        if (target.endsWith(query)) {
                            treePane.addSelectedNode(node);
                        }
                        break;
                    case MATCHES:
                        if (target.matches(query)) {
                            treePane.addSelectedNode(node);
                        }
                        break;
                }
            }
        }
    }

    public void clearSelectedTaxa() {
        treePane.clearSelection();
    }

    public void setSelectionMode(TreePaneSelector.SelectionMode selectionMode) {
        treePaneSelector.setSelectionMode(selectionMode);
    }

    public void setDragMode(TreePaneSelector.DragMode dragMode) {
        treePaneSelector.setDragMode(dragMode);
    }

    public JComponent getExportableComponent() {
        return treePane;
    }

    public void paint(Graphics g) {
        if (zoomPending) {
            refreshZoom();
            zoomPending = false;
        }
        super.paint(g);
    }

    public int print(Graphics g, PageFormat pageFormat, int pageIndex) throws PrinterException {
        return treePane.print(g, pageFormat, pageIndex);
    }

    protected TreePane treePane;

    protected TreePaneSelector treePaneSelector;

    protected JViewport viewport;
}
