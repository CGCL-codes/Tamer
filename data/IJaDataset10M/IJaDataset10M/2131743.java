package dot.chimera.registry;

import dot.exceptions.ProgrammingErrorException;
import java.util.*;

/**
 * A directory node is simply a regular node whose contents is a directory 
 * table.  A directory table is an immutable table, which maps node names to 
 * nodes.  A directory table can only be created empty, and only way to 
 * mutate one (ie. add or remove a child) is by using the {@link #add} or 
 * {@link #remove} methods, which return a new directory table.  The table
 * also implements {@link #notIn} to compare two directory tables and 
 * determine the differences between the two.  (Note the {@link #notIn}
 * comparision does not need to be made between successive versions of a
 * directory table, but can be made between arbitrary directory tables.)
 * <p>
 * The directory table is mainly used internally by the registry.
 * <p>
 * Note that the current table implementation isn't particularly clever
 * and basically all operations are O(n) where n is number of children,
 * except {@link #notIn} which is O(n^2).
 * 
 * @author ;Rob Clark;a0873619;San Diego;;
 * @version 0.1
 */
public class DirectoryTable {

    private String[] names;

    private Node[] nodes;

    /**
   * Class Constructor, create an empty directory table.
   */
    public DirectoryTable() {
        this(new Node[0], new String[0]);
    }

    private DirectoryTable(Node[] nodes, String[] names) {
        if (nodes.length != names.length) throw new ProgrammingErrorException("this shouldn't happen!");
        this.nodes = nodes;
        this.names = names;
    }

    /**
   * Get an iterator of child names of the contents of this directory.
   * 
   * @return an iterator of child names
   */
    public Iterator getChildNames() {
        return new Iterator() {

            private int idx = 0;

            public boolean hasNext() {
                return idx < names.length;
            }

            public Object next() {
                return names[idx++];
            }

            public void remove() {
                throw new UnsupportedOperationException("remove");
            }
        };
    }

    /**
   * Get the number of children of this directory node.
   * 
   * @return the number of children
   * @see #getChildName
   * @see #getChildNode
   */
    public int getChildCount() {
        return nodes.length;
    }

    /**
   * Get the name of the child at the specified index.
   * 
   * @param idx          the index
   * @return the child name
   */
    String getChildName(int idx) {
        return names[idx];
    }

    /**
   * Get the node of the child at the specified index.
   * 
   * @param idx          the index
   * @return the child name
   */
    Node getChildNode(int idx) {
        return nodes[idx];
    }

    /**
   * Get the node in this table with the specified name.  Returns
   * <code>null</code> if none.
   * 
   * @param name         name of node to find
   * @return the requested node, or <code>null</code> if none
   */
    public Node get(String name) {
        for (int i = 0; i < names.length; i++) if (names[i].equals(name)) return nodes[i];
        return null;
    }

    /**
   * Construct a directory table whose contents is the same as the
   * current table, with the specified node added.  Called by the
   * registry when linking a new node into the tree, this should
   * not be called anywhere else.  (Nothing to see here, move
   * along.)
   * 
   * @param node         the node to add
   * @param name         name of node to add
   * @return the new directory table
   * @throws RegistryException already contains node with same name
   */
    DirectoryTable add(Node node, String name) throws RegistryException {
        if (get(name) != null) throw new RegistryException("child already exists: " + name);
        Node[] newNodes = new Node[nodes.length + 1];
        String[] newNames = new String[names.length + 1];
        System.arraycopy(nodes, 0, newNodes, 0, nodes.length);
        System.arraycopy(names, 0, newNames, 0, names.length);
        newNodes[nodes.length] = node;
        newNames[names.length] = name;
        return new DirectoryTable(newNodes, newNames);
    }

    /**
   * Construct a directory table whose contents is the same as the
   * current table, with the specified node removed.  If an attempt
   * is made to remove a child who is a non-empty directory, this
   * will throw an exception, because much depends on being able to
   * detect a node being removed by detecting a change in the node's
   * parent.  Called by the registry when unlinking a node from the
   * tree, this should not be called by anyone else.
   * 
   * @param name         name of node to remove
   * @param lastLink     if this link to the node is the last
   * @throws RegistryException does not contain node with same name
   *    or if said node is the last link to a directory node that 
   *    still has children
   */
    DirectoryTable remove(String name, boolean lastLink) throws RegistryException {
        int idx = -1;
        for (int i = 0; (i < names.length) && (idx == -1); i++) if (names[i].equals(name)) idx = i;
        if (idx == -1) throw new RegistryException("child doesn't exists: " + name);
        if ((nodes[idx].getValue() instanceof DirectoryTable) && ((DirectoryTable) (nodes[idx].getValue())).getChildNames().hasNext() && lastLink) throw new RegistryException("child still has children");
        Node[] newNodes = new Node[nodes.length - 1];
        String[] newNames = new String[names.length - 1];
        System.arraycopy(nodes, 0, newNodes, 0, idx);
        System.arraycopy(names, 0, newNames, 0, idx);
        System.arraycopy(nodes, idx + 1, newNodes, idx, newNodes.length - idx);
        System.arraycopy(names, idx + 1, newNames, idx, newNames.length - idx);
        return new DirectoryTable(newNodes, newNames);
    }

    /**
   * Determine the differences between directory tables, by returning
   * an array of files that are only contained in this table.  For
   * example:
   * <pre>
   *   Iterator added   = newDirTable.notIn(oldDirTable);
   *   Iterator removed = oldDirTable.notIn(newDirTable);
   * </pre>
   * If <code>otherTable</code> is <code>null</code>, this returns
   * the same thing as {@link #getChildNames}.
   * 
   * @param otherTable   the other table to compare to, or <code>null</code>
   * @return an iterator of names of children that exist in this table
   *    but not <code>otherTable</code>
   */
    public Iterator notIn(DirectoryTable otherTable) {
        if (otherTable == null) return getChildNames();
        LinkedList l = new LinkedList();
        for (int i = 0; i < names.length; i++) l.add(names[i]);
        for (int i = 0; i < otherTable.names.length; i++) l.remove(otherTable.names[i]);
        return l.iterator();
    }

    /**
   * for debug...
   */
    public String toString() {
        StringBuffer sb = new StringBuffer();
        sb.append("[directory table: ");
        boolean first = true;
        for (Iterator itr = getChildNames(); itr.hasNext(); ) {
            if (first) first = false; else sb.append(", ");
            sb.append(itr.next());
        }
        return sb.append("]").toString();
    }
}
