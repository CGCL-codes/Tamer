package eu.fbk.hlt.edits.gui.frame;

import java.awt.Color;
import java.awt.event.InputEvent;
import java.awt.event.KeyEvent;
import java.io.File;
import javax.swing.Action;
import javax.swing.ActionMap;
import javax.swing.InputMap;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JScrollPane;
import javax.swing.JTree;
import javax.swing.KeyStroke;
import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.TreePath;
import eu.fbk.hlt.common.EDITSException;
import eu.fbk.hlt.edits.distance.cost.scheme.XmlCostScheme;
import eu.fbk.hlt.edits.distance.cost.scheme.data.XMLCostScheme;
import eu.fbk.hlt.edits.gui.EDITSEditor;
import eu.fbk.hlt.edits.gui.MemoryManager.Type;
import eu.fbk.hlt.edits.gui.tree.CostSchemeTreeNode;
import eu.fbk.hlt.edits.gui.tree.CustomNode;
import eu.fbk.hlt.edits.gui.tree.CustomTreeRenderer;

/**
 * @author Milen Kouylekov
 */
public class CostSchemeFrame extends CustomFrame {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;

    private CostSchemeTreeNode root;

    private XMLCostScheme scheme;

    private JTree tree;

    public CostSchemeFrame(String name, EDITSEditor manager, JFrame frame) {
        super(name, manager);
        setForeground(Color.WHITE);
        try {
            if (!new File(name).exists()) scheme = new XMLCostScheme(); else {
                scheme = XmlCostScheme.loadCostScheme(name);
                manager.memoryManager().setPath(Type.CostScheme, new File(name).getParentFile().getAbsolutePath());
            }
            init();
        } catch (EDITSException ex) {
            JOptionPane.showMessageDialog(frame, "Could not open cost scheme because:\n" + ex.getMessage());
            return;
        }
        initialized();
    }

    @Override
    public boolean canBeSaved() {
        return true;
    }

    @Override
    public void executeSave(String tempPath) throws EDITSException {
        root.populateData();
        XmlCostScheme.saveCostScheme(tempPath, scheme, true);
    }

    @Override
    public void handleCopy() {
        TreePath tp = tree.getSelectionPath();
        if (tp == null) return;
        CustomNode node = (CustomNode) tp.getLastPathComponent();
        if (node != null) node.handleCopy();
    }

    @Override
    public void handleCut() {
        TreePath tp = tree.getSelectionPath();
        if (tp == null) return;
        CustomNode node = (CustomNode) tp.getLastPathComponent();
        if (node != null) node.handleCut();
    }

    @Override
    public void handleInfo() {
        TreePath tp = tree.getSelectionPath();
        if (tp == null) return;
        CustomNode node = (CustomNode) tp.getLastPathComponent();
        if (node != null) node.handleInfo();
    }

    @Override
    public void handlePaste() {
        TreePath tp = tree.getSelectionPath();
        if (tp == null) return;
        CustomNode node = (CustomNode) tp.getLastPathComponent();
        if (node != null) node.handlePaste();
    }

    public void init() throws EDITSException {
        tree = new JTree();
        DefaultTreeModel model = new DefaultTreeModel(null);
        root = new CostSchemeTreeNode(tree, getEditor(), scheme);
        model.setRoot(root);
        tree.setModel(model);
        tree.setCellRenderer(new CustomTreeRenderer());
        InputMap inputMap = tree.getInputMap();
        ActionMap actionMap = tree.getActionMap();
        KeyStroke ks = null;
        Action a = null;
        ks = KeyStroke.getKeyStroke(KeyEvent.VK_X, InputEvent.CTRL_MASK);
        a = new KeyAction(this, "cut");
        inputMap.put(ks, a);
        actionMap.put(a, a);
        ks = KeyStroke.getKeyStroke(KeyEvent.VK_C, InputEvent.CTRL_MASK);
        a = new KeyAction(this, "copy");
        inputMap.put(ks, a);
        actionMap.put(a, a);
        ks = KeyStroke.getKeyStroke(KeyEvent.VK_V, InputEvent.CTRL_MASK);
        a = new KeyAction(this, "paste");
        inputMap.put(ks, a);
        actionMap.put(a, a);
        tree.setEditable(false);
        tree.addMouseListener(new CustomMouseListener(tree));
        root.update();
        JScrollPane pane = new JScrollPane(tree);
        setContentPane(pane);
        pack();
        setVisible(true);
    }

    @Override
    public boolean isModified() {
        return root.isModified();
    }

    public XMLCostScheme scheme() {
        return scheme;
    }

    /**
	 * @param b
	 */
    @Override
    public void setModified(boolean b) {
        root.setModified(b);
        root.update();
    }

    @Override
    public Type type() {
        return Type.CostScheme;
    }
}
