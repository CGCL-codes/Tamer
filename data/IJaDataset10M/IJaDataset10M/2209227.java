package net.sf.xpontus.modules.gui.components;

import com.vlsolutions.swing.docking.DockGroup;
import com.vlsolutions.swing.docking.DockKey;
import com.vlsolutions.swing.docking.Dockable;
import net.sf.xpontus.parsers.TokenNode;
import net.sf.xpontus.utils.XPontusComponentsUtils;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.net.URL;
import javax.swing.ImageIcon;
import javax.swing.JScrollPane;
import javax.swing.JTree;
import javax.swing.text.Element;
import javax.swing.text.JTextComponent;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeCellRenderer;
import javax.swing.tree.DefaultTreeModel;

/**
 * Dockable for the outline view
 * @version 0.0.2
 * @author Yves Zoundi <yveszoundi at users dot sf dot net>
 */
public class OutlineViewDockable extends JScrollPane implements Dockable {

    private static final long serialVersionUID = 3603419421712967424L;

    private static final Dimension OUTLINE_SIZE = new Dimension(250, 200);

    private DockKey key = new DockKey("Outline ");

    private DefaultTreeModel outlineTreeModel;

    private DefaultMutableTreeNode outlineRootNode;

    private JScrollPane scrollPane;

    private JTree outlineTree;

    /**
     * Constructor
     */
    public OutlineViewDockable() {
        outlineRootNode = new DefaultMutableTreeNode("Document");
        outlineTreeModel = new DefaultTreeModel(outlineRootNode);
        outlineTree = new JTree(outlineTreeModel);
        outlineTree.setRootVisible(false);
        scrollPane = new JScrollPane(outlineTree);
        key.setResizeWeight(0.2f);
        key.setDockGroup(new DockGroup("Outline"));
        scrollPane.setPreferredSize(OUTLINE_SIZE);
        scrollPane.setMinimumSize(OUTLINE_SIZE);
        this.getViewport().add(scrollPane);
        DefaultTreeCellRenderer renderer = new DefaultTreeCellRenderer();
        URL iconURL = getClass().getResource("/net/sf/xpontus/icons/Element.png");
        final ImageIcon leafIcon = new ImageIcon(iconURL);
        renderer.setLeafIcon(leafIcon);
        renderer.setOpenIcon(leafIcon);
        renderer.setClosedIcon(leafIcon);
        renderer.setIcon(leafIcon);
        outlineTree.setCellRenderer(renderer);
        outlineTree.addMouseListener(new MouseAdapter() {

            public void mouseClicked(MouseEvent e) {
                DefaultMutableTreeNode node = (DefaultMutableTreeNode) outlineTree.getLastSelectedPathComponent();
                if (node == null) {
                    return;
                }
                if (e.getClickCount() == 1) {
                    if (node instanceof TokenNode) {
                        TokenNode nodeInfo = (TokenNode) node;
                        gotoLine(nodeInfo.line, nodeInfo.column);
                    }
                }
            }
        });
    }

    /**
     *
     * @return
     */
    public DefaultMutableTreeNode getRootNode() {
        return outlineRootNode;
    }

    /**
     * @param rootNode
     */
    public void updateAll(DefaultMutableTreeNode rootNode) {
        this.outlineRootNode = rootNode;
        outlineTreeModel = new DefaultTreeModel(rootNode);
        outlineTree.setModel(outlineTreeModel);
        outlineTree.revalidate();
        outlineTree.repaint();
        expandAllNodes();
    }

    /**
     * @param line
     * @param column
     */
    private void gotoLine(int line, int column) {
        JTextComponent textEditor = DefaultXPontusWindowImpl.getInstance().getDocumentTabContainer().getCurrentEditor();
        Element element = textEditor.getDocument().getDefaultRootElement();
        if (element.getElement(line) == null) {
            XPontusComponentsUtils.showErrorMessage("element location not found");
            return;
        }
        line = line - 1;
        column = column - 1;
        int lineOffset = element.getElement(line).getStartOffset();
        int tokenOffset = lineOffset + column;
        textEditor.grabFocus();
        textEditor.setCaretPosition(tokenOffset);
    }

    /**
     *
     * @return
     */
    public DefaultTreeModel getTreeModel() {
        return outlineTreeModel;
    }

    public void expandAllNodes() {
        for (int i = 0; i < outlineTree.getRowCount(); i++) {
            outlineTree.expandRow(i);
        }
    }

    /** implement Dockable
     * @return
     */
    public DockKey getDockKey() {
        return key;
    }

    /** implement Dockable
     * @return
     */
    public Component getComponent() {
        return scrollPane;
    }
}
