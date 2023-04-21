package jsynoptic.plugins.java3d.tree;

import java.awt.Component;
import java.awt.Point;
import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.Transferable;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Enumeration;
import java.util.Stack;
import javax.media.j3d.Node;
import javax.media.j3d.SceneGraphObject;
import javax.swing.JComponent;
import javax.swing.JMenu;
import javax.swing.JMenuItem;
import javax.swing.JPopupMenu;
import javax.swing.JSeparator;
import javax.swing.JTree;
import javax.swing.KeyStroke;
import javax.swing.TransferHandler;
import javax.swing.event.TreeSelectionEvent;
import javax.swing.event.TreeSelectionListener;
import javax.swing.event.UndoableEditListener;
import javax.swing.tree.DefaultTreeCellRenderer;
import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.TreePath;
import javax.swing.undo.UndoManager;
import jsynoptic.base.Plugin;
import jsynoptic.plugins.java3d.Java3dPlugin;
import jsynoptic.plugins.java3d.Java3dShape;
import jsynoptic.plugins.java3d.NodeSelector;
import jsynoptic.plugins.java3d.Universe;
import jsynoptic.plugins.java3d.UniversePool;
import jsynoptic.plugins.java3d.panels.SceneGraphObjectDialog;
import jsynoptic.ui.ShapeCreator;
import jsynoptic.ui.ShapesContainer;
import simtools.diagram.undo.UndoHandler;
import simtools.shapes.AbstractShape;
import simtools.ui.MenuResourceBundle;
import simtools.ui.ResourceFinder;

public class Tree extends JTree implements ActionListener, NodeSelector {

    public static MenuResourceBundle resources = ResourceFinder.getMenu(Tree.class);

    static {
        RootNode.loadResources();
    }

    /** <b>(DefaultTreeModel)</b> _treeModel: The treeModel used */
    final DefaultTreeModel _treeModel;

    /** <b>(DefaultMutableTreeNode)</b> _rootNode: The root node */
    private final AbstractNode _rootNode;

    private final UniversePool _pool;

    private final Java3dPlugin _plugin;

    private final ClipBoard _clipBoard;

    /** The <code>UndoHandler</code> catches undoable edits and
     * sends them to the <code>UndoManager</code> */
    private final UndoHandler _undoHandler;

    private final UndoManager _undoManager;

    public Tree(UniversePool pool, Java3dPlugin plugin) {
        _pool = pool;
        _plugin = plugin;
        _clipBoard = new ClipBoard();
        _undoManager = new UndoManager();
        _undoHandler = new UndoHandler(_undoManager);
        _rootNode = new RootNode(this, null, false);
        _treeModel = new DefaultTreeModel(_rootNode);
        setModel(_treeModel);
        setCellRenderer(new TreeCellRenderer());
        setRootVisible(false);
        setShowsRootHandles(true);
        _rootNode.refresh();
        setTransferHandler(new ShapeTransferHandler());
        setDragEnabled(true);
        KeyStroke ks;
        ks = KeyStroke.getKeyStroke(KeyEvent.VK_Z, KeyEvent.CTRL_MASK);
        registerKeyboardAction(this, "z", ks, WHEN_FOCUSED);
        ks = KeyStroke.getKeyStroke(KeyEvent.VK_Y, KeyEvent.CTRL_MASK);
        registerKeyboardAction(this, "y", ks, WHEN_FOCUSED);
        ks = KeyStroke.getKeyStroke(KeyEvent.VK_C, KeyEvent.CTRL_MASK);
        registerKeyboardAction(this, "c", ks, WHEN_FOCUSED);
        ks = KeyStroke.getKeyStroke(KeyEvent.VK_COPY, 0);
        registerKeyboardAction(this, "c", ks, WHEN_FOCUSED);
        ks = KeyStroke.getKeyStroke(KeyEvent.VK_X, KeyEvent.CTRL_MASK);
        registerKeyboardAction(this, "x", ks, WHEN_FOCUSED);
        ks = KeyStroke.getKeyStroke(KeyEvent.VK_CUT, 0);
        registerKeyboardAction(this, "x", ks, WHEN_FOCUSED);
        ks = KeyStroke.getKeyStroke(KeyEvent.VK_V, KeyEvent.CTRL_MASK);
        registerKeyboardAction(this, "v", ks, WHEN_FOCUSED);
        ks = KeyStroke.getKeyStroke(KeyEvent.VK_PASTE, 0);
        registerKeyboardAction(this, "v", ks, WHEN_FOCUSED);
        ks = KeyStroke.getKeyStroke(KeyEvent.VK_DELETE, 0);
        registerKeyboardAction(this, "d", ks, WHEN_FOCUSED);
        ks = KeyStroke.getKeyStroke(KeyEvent.VK_BACK_SPACE, 0);
        registerKeyboardAction(this, "d", ks, WHEN_FOCUSED);
        ks = KeyStroke.getKeyStroke(KeyEvent.VK_ENTER, 0);
        registerKeyboardAction(this, "p", ks, WHEN_FOCUSED);
        addTreeSelectionListener(new TreeSelectionListener() {

            @Override
            public void valueChanged(TreeSelectionEvent e) {
                TreePath tp = e.getPath();
                if (tp.getPathCount() > 1) {
                    AbstractNode an = (AbstractNode) tp.getPathComponent(1);
                    if (an.getGraphObject() instanceof Universe) {
                        UniversePool.getGlobal().setCurrentUniverse((Universe) an.getGraphObject());
                    }
                }
            }
        });
        MouseListener ml = new MouseAdapter() {

            public void mousePressed(MouseEvent e) {
                if (e.getButton() == MouseEvent.BUTTON3) {
                    TreePath selPath = getPathForLocation(e.getX(), e.getY());
                    AbstractNode n;
                    if (selPath != null) {
                        n = (AbstractNode) selPath.getLastPathComponent();
                    } else {
                        n = _rootNode;
                    }
                    doPopup(n, e.getX(), e.getY());
                }
            }
        };
        addMouseListener(ml);
    }

    public UndoableEditListener getUndoableEditListener() {
        return _undoHandler;
    }

    public ClipBoard getClipBoard() {
        return _clipBoard;
    }

    public UniversePool getPool() {
        return _pool;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        String cmd = e.getActionCommand();
        if (cmd.equals("z")) {
            if (_undoHandler.getUndoAction().isEnabled()) _undoHandler.getUndoAction().actionPerformed(e);
            return;
        }
        if (cmd.equals("y")) {
            if (_undoHandler.getRedoAction().isEnabled()) _undoHandler.getRedoAction().actionPerformed(e);
            return;
        }
        TreePath tp = Tree.this.getSelectionPath();
        AbstractNode s = (AbstractNode) tp.getLastPathComponent();
        if (s == null || !(s instanceof SceneGraphTreeNode)) {
            return;
        }
        SceneGraphTreeNode n = (SceneGraphTreeNode) s;
        if (cmd.equals("c")) {
            n.copy(false);
        } else if (cmd.equals("v")) {
            if (n.canPaste()) {
                n.paste(false);
            }
        } else if (cmd.equals("x")) {
            if (n.canRemove()) {
                n.cut();
            }
        } else if (cmd.equals("d")) {
            if (n.canRemove()) {
                n.remove();
            }
        } else if (cmd.equals("p")) {
            Point p = getMousePosition();
            if (p != null) {
                Point p2 = getLocationOnScreen();
                p.x += p2.x - 10;
                p.y += p2.y - 10;
            }
            SceneGraphObjectDialog.createDialog(n, p, null);
        }
    }

    class TreeCellRenderer extends DefaultTreeCellRenderer {

        @Override
        public Component getTreeCellRendererComponent(JTree tree, Object value, boolean sel, boolean expanded, boolean leaf, int row, boolean hasFocus) {
            Component result = super.getTreeCellRendererComponent(tree, value, sel, expanded, leaf, row, hasFocus);
            if (value instanceof AbstractNode) {
                AbstractNode n = (AbstractNode) value;
                setText(n.getName());
                setIcon(n._icon);
                setToolTipText(n._description);
            }
            return result;
        }
    }

    void doPopup(AbstractNode n, int x, int y) {
        Object[] actions = null;
        if (n instanceof ReferenceNode) {
            actions = ((ReferenceNode) n).getActions();
        } else {
            actions = AbstractNodeAction.getActions(n.getGraphObject(), null);
        }
        if (actions == null) {
            return;
        }
        JPopupMenu popup = new JPopupMenu();
        for (Object c : actions) {
            if (c == null) {
                popup.add(new JSeparator());
            } else if (c instanceof String) {
                popup.add(createSubMenu(n, (String) c, x, y));
            } else if (c instanceof Class) {
                AbstractNodeAction aa = AbstractNodeAction.createAction((Class<?>) c, n);
                aa.setLocation(this, x, y);
                JMenuItem jmi = new JMenuItem(aa);
                popup.add(jmi);
            }
        }
        popup.show(this, x, y);
    }

    private JMenu createSubMenu(AbstractNode n, String name, int x, int y) {
        JMenu subMenu = new JMenu(name);
        Object[] actions = AbstractNodeAction.getActions(n.getGraphObject(), name);
        if (actions != null) {
            for (Object c : actions) {
                if (c == null) {
                    subMenu.add(new JSeparator());
                } else if (c instanceof String) {
                    subMenu.add(createSubMenu(n, (String) c, x, y));
                } else if (c instanceof Class) {
                    AbstractNodeAction aa = AbstractNodeAction.createAction((Class<?>) c, n);
                    aa.setLocation(this, x, y);
                    JMenuItem jmi = new JMenuItem(aa);
                    subMenu.add(jmi);
                }
            }
        }
        return subMenu;
    }

    public TreePath expand(ArrayList<SceneGraphObject> sgos, Universe u) {
        RootNode rootNode = (RootNode) getModel().getRoot();
        Enumeration<?> roots = rootNode.children();
        UniverseNode un = null;
        while (roots.hasMoreElements()) {
            Object o = roots.nextElement();
            if (o instanceof UniverseNode) {
                if (((UniverseNode) o).getGraphObject() == u) {
                    un = (UniverseNode) o;
                    break;
                }
            }
        }
        if (un == null) {
            throw new RuntimeException("Can not find universe node");
        }
        ArrayList<AbstractNode> tpn = new ArrayList<AbstractNode>();
        tpn.add(rootNode);
        tpn.add(un);
        Stack<SceneGraphObject> st = new Stack<SceneGraphObject>();
        st.addAll(sgos);
        _createChildren(tpn, st);
        TreePath tp = new TreePath(tpn.toArray());
        expandPath(tp);
        return tp;
    }

    private void _createChildren(ArrayList<AbstractNode> tpn, Stack<SceneGraphObject> sgos) {
        SceneGraphObject sg = sgos.pop();
        AbstractNode an = tpn.get(tpn.size() - 1);
        Enumeration<?> children = an.children();
        AbstractNode cn = null;
        while (children.hasMoreElements()) {
            Object o = children.nextElement();
            if (o instanceof AbstractNode) {
                AbstractNode acn = (AbstractNode) o;
                if (acn.getGraphObject() == sg) {
                    cn = acn;
                    break;
                }
            }
        }
        if (cn == null) {
            cn = an.createNode(sg);
        }
        tpn.add(cn);
        if (!sgos.empty()) {
            _createChildren(tpn, sgos);
        }
    }

    @Override
    public void select(Collection<Node> nodes) {
        clearSelection();
        for (Node n : nodes) {
            ArrayList<SceneGraphObject> sgos = new ArrayList<SceneGraphObject>();
            Universe u = Universe.getGraphPath(n, null, sgos);
            TreePath tp = expand(sgos, u);
            addSelectionPath(tp);
        }
    }

    protected class ShapeTransferHandler extends TransferHandler {

        protected Transferable createTransferable(JComponent c) {
            if (c == Tree.this) {
                return new ShapeTransferable();
            }
            return null;
        }

        public int getSourceActions(JComponent c) {
            return COPY;
        }
    }

    protected class ShapeTransferable implements Transferable {

        public DataFlavor[] getTransferDataFlavors() {
            TreePath tp = Tree.this.getSelectionPath();
            AbstractNode s = (AbstractNode) tp.getLastPathComponent();
            if (s == null || !(s instanceof UniverseNode)) {
                return new DataFlavor[0];
            }
            DataFlavor[] ret = new DataFlavor[1];
            ret[0] = ShapesContainer.SHAPE_FLAVOR;
            return ret;
        }

        public boolean isDataFlavorSupported(DataFlavor flavor) {
            TreePath tp = Tree.this.getSelectionPath();
            Object s = (AbstractNode) tp.getLastPathComponent();
            if (s == null) {
                return false;
            }
            if (s instanceof UniverseNode) {
                return ShapeCreator.class.equals(flavor.getRepresentationClass());
            }
            return false;
        }

        public Object getTransferData(DataFlavor flavor) {
            TreePath tp = Tree.this.getSelectionPath();
            Object s = (AbstractNode) tp.getLastPathComponent();
            if (s == null) {
                return null;
            }
            if ((s instanceof UniverseNode) && ShapeCreator.class.equals(flavor.getRepresentationClass())) {
                return new Shape3DCreator(Java3dPlugin._3DShape, _plugin, (UniverseNode) s);
            }
            return null;
        }
    }

    class Shape3DCreator extends ShapeCreator {

        final UniverseNode _node;

        public Shape3DCreator(String shape, Plugin plugin, UniverseNode node) {
            super(plugin, shape);
            _node = node;
        }

        @Override
        public AbstractShape create() {
            AbstractShape res = new Java3dShape(0, 0, 250, 250, (Universe) _node.getGraphObject());
            _node.refresh();
            return res;
        }
    }

    public class ClipBoard {

        ArrayList<SceneGraphTreeNode> _nodes = new ArrayList<SceneGraphTreeNode>();

        public void set(SceneGraphTreeNode... objs) {
            _nodes.clear();
            for (SceneGraphTreeNode o : objs) _nodes.add(o);
        }

        public ArrayList<SceneGraphTreeNode> get() {
            return _nodes;
        }
    }
}
