package com.daohoangson.uml.gui;

import java.awt.Component;
import java.awt.Dimension;
import java.awt.Point;
import java.awt.Rectangle;
import java.util.Enumeration;
import java.util.Hashtable;
import java.util.Vector;
import javax.swing.Icon;
import javax.swing.JComponent;
import javax.swing.JScrollPane;
import javax.swing.JTree;
import javax.swing.event.TreeExpansionEvent;
import javax.swing.event.TreeExpansionListener;
import javax.swing.tree.DefaultTreeCellRenderer;
import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.TreeNode;
import javax.swing.tree.TreePath;
import com.daohoangson.uml.structures.Structure;
import com.tranvietson.uml.structures.StructureEvent;
import com.tranvietson.uml.structures.StructureListener;

/**
 * A tree list of structures with its children
 * 
 * @author Dao Hoang Son
 * @version 1.0
 * 
 */
public class Outline extends JTree implements TreeExpansionListener {

    private static final long serialVersionUID = -9149376828755778513L;

    /**
	 * The source diagram
	 */
    private Diagram diagram = null;

    /**
	 * The scroll-able component
	 * 
	 * @see #getScrollable()
	 */
    private JScrollPane scrollpane = null;

    /**
	 * Determines if we are in debug mode.
	 */
    public static boolean debugging = false;

    /**
	 * Constructors
	 * 
	 * @param diagram
	 *            the source diagram
	 */
    public Outline(Diagram diagram) {
        super(new TreeModelStructure(diagram));
        setCellRenderer(new TreeCellRendererStructure());
        addTreeExpansionListener(this);
        setScrollsOnExpand(true);
        this.diagram = diagram;
    }

    /**
	 * Get the scroll-able component. Use this for a better user experience.
	 * Anyway, this is optional. You can always
	 * {@linkplain JComponent#add(Component) add} directly this object.
	 * 
	 * @return a JScrollPane object of the diagram
	 */
    public Component getScrollable() {
        if (scrollpane == null) {
            scrollpane = new JScrollPane(this);
            scrollpane.setMaximumSize(new Dimension(Integer.MAX_VALUE, Integer.MAX_VALUE));
        }
        return scrollpane;
    }

    /**
	 * Gets the structure based on location (relative to the layout)
	 * 
	 * @param location
	 *            the location
	 * @return the found structure. It may be null
	 */
    public Structure getStructureForLocation(Point location) {
        Structure structure = null;
        TreePath path = getPathForLocation(location.x, location.y);
        if (path != null) {
            Object node = path.getLastPathComponent();
            if (node instanceof StructureBased) {
                structure = ((StructureBased) node).getStructure();
            }
        }
        return structure;
    }

    /**
	 * Tries its best to bring the requested structure into the viewport
	 * 
	 * @param structure
	 *            the structure needs bringing to visible area
	 */
    public void ensureStructureIsVisible(Structure structure) {
        Object root = getModel().getRoot();
        Object node = TreeNodeStructure.create(structure);
        TreePath path = new TreePath(new Object[] { root, node });
        expandPath(path);
        Rectangle rect = getPathBounds(path);
        if (rect != null) {
            Rectangle visible = getVisibleRect();
            rect.setLocation(0, rect.y);
            rect.setSize(visible.getSize());
            scrollRectToVisible(rect);
        }
    }

    @Override
    public void treeCollapsed(TreeExpansionEvent event) {
    }

    @Override
    public void treeExpanded(TreeExpansionEvent event) {
        Object last = event.getPath().getLastPathComponent();
        if (last instanceof TreeNodeStructure) {
            diagram.ensureStructureIsVisible(((TreeNodeStructure) last).getStructure());
        }
    }
}

/**
 * The renderer for the {@link Outline}
 * 
 * @author Dao Hoang Son
 * @version 1.0
 * 
 */
class TreeCellRendererStructure extends DefaultTreeCellRenderer {

    private static final long serialVersionUID = -6058394290812564302L;

    @Override
    public Component getTreeCellRendererComponent(JTree tree, Object value, boolean sel, boolean expanded, boolean leaf, int row, boolean hasFocus) {
        Component c = super.getTreeCellRendererComponent(tree, value, sel, expanded, leaf, row, hasFocus);
        if (value instanceof TreeNodeStructure) {
            Structure structure = ((TreeNodeStructure) value).getStructure();
            if (structure != null) {
                String structureName = structure.getStructureName();
                if (structureName.equals("Class")) {
                    setIcon("class");
                } else if (structureName.equals("Interface")) {
                    setIcon("interface");
                } else if (structureName.equals("Property") || structureName.equals("Method")) {
                    String prefix;
                    if (structureName.equals("Property")) {
                        prefix = "property_";
                    } else {
                        prefix = "method_";
                    }
                    String visibility = structure.getVisibility();
                    if (visibility.length() == 0) {
                        visibility = "default";
                    }
                    setIcon(prefix + visibility);
                } else {
                    setIcon((Icon) null);
                }
            } else {
                setIcon("root");
            }
        }
        return c;
    }

    /**
	 * Shortcut to setIcon
	 * 
	 * @param icon_name
	 *            the icon name. It will be looked up using
	 *            {@link UMLGUI#getIcon(String)}
	 */
    private void setIcon(String icon_name) {
        Icon icon = UMLGUI.getIcon("tree_" + icon_name);
        if (icon != null) {
            setIcon(icon);
        }
    }
}

/**
 * The model for the {@link Outline}
 * 
 * @author Dao Hoang Son
 * @version 1.0
 * 
 */
class TreeModelStructure extends DefaultTreeModel implements StructureListener {

    private static final long serialVersionUID = 762088370610407170L;

    /**
	 * Constructor
	 * 
	 * @param diagram
	 *            the source diagram
	 */
    public TreeModelStructure(Diagram diagram) {
        super(new TreeNodeStructure(diagram));
        diagram.addStructureListener(this);
    }

    @Override
    public void structureChanged(StructureEvent e) {
        Structure structure = (Structure) e.getSource();
        reload(TreeNodeStructure.create(structure));
        if (Outline.debugging) {
            System.err.println("Outline reloaded for " + structure);
        }
    }
}

/**
 * A node in the Outline's model
 * 
 * @author Dao Hoang Son
 * @version 1.0
 * 
 */
class TreeNodeStructure implements TreeNode, StructureBased {

    /**
	 * The structure which this node represents. It may be null
	 */
    private Structure structure = null;

    /**
	 * The diagram which this node represents. It may be null
	 */
    private Diagram diagram;

    /**
	 * A self-managed hash table of structures and nodes
	 * 
	 * @see #create(Structure)
	 */
    private static Hashtable<Structure, TreeNodeStructure> nodes = new Hashtable<Structure, TreeNodeStructure>();

    /**
	 * Constructor for a structure node
	 * 
	 * @param structure
	 *            the structure
	 */
    private TreeNodeStructure(Structure structure) {
        this.structure = structure;
    }

    /**
	 * Constructor for a diagram node. It will be the root
	 * 
	 * @param diagram
	 *            the diagram
	 */
    public TreeNodeStructure(Diagram diagram) {
        this.diagram = diagram;
    }

    @Override
    public Structure getStructure() {
        return structure;
    }

    @Override
    public String toString() {
        if (structure != null) {
            return structure.toString("", "", true);
        } else {
            return "root";
        }
    }

    /**
	 * Creates node for a structure. Looks for created nodes before creating a
	 * new one
	 * 
	 * @param structure
	 *            the structure in need
	 * @return the node
	 */
    public static TreeNodeStructure create(Structure structure) {
        if (!TreeNodeStructure.nodes.containsKey(structure)) {
            TreeNodeStructure.nodes.put(structure, new TreeNodeStructure(structure));
        }
        return TreeNodeStructure.nodes.get(structure);
    }

    @SuppressWarnings("unchecked")
    @Override
    public Enumeration children() {
        Structure[] children;
        if (structure != null) {
            children = structure.getChildren();
        } else {
            children = diagram.getStructures();
        }
        Vector<TreeNodeStructure> v = new Vector<TreeNodeStructure>();
        for (int i = 0; i < children.length; i++) {
            v.add(TreeNodeStructure.create(children[i]));
        }
        return v.elements();
    }

    @Override
    public boolean getAllowsChildren() {
        if (structure != null) {
            return structure.checkCanHaveChildren();
        } else {
            return true;
        }
    }

    @Override
    public TreeNode getChildAt(int childIndex) {
        Structure[] children;
        if (structure != null) {
            children = structure.getChildren();
        } else {
            children = diagram.getStructures();
        }
        if (childIndex < children.length) {
            return TreeNodeStructure.create(children[childIndex]);
        } else {
            return null;
        }
    }

    @Override
    public int getChildCount() {
        if (structure != null) {
            return structure.getChildrenCount();
        } else {
            return diagram.getStructures().length;
        }
    }

    @Override
    public int getIndex(TreeNode node) {
        if (node instanceof TreeNodeStructure) {
            TreeNodeStructure tns = (TreeNodeStructure) node;
            Structure child = tns.getStructure();
            Structure[] children;
            if (structure != null) {
                children = structure.getChildren();
            } else {
                children = diagram.getStructures();
            }
            for (int i = 0; i < children.length; i++) {
                if (children[i] == child) {
                    return i;
                }
            }
        }
        return -1;
    }

    @Override
    public TreeNode getParent() {
        if (structure != null && structure.getContainer() != null) {
            return TreeNodeStructure.create(structure.getContainer());
        } else {
            return null;
        }
    }

    @Override
    public boolean isLeaf() {
        if (structure != null) {
            return structure.getChildrenCount() == 0;
        } else {
            return false;
        }
    }
}
