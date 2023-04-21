package net.sourceforge.schemaspy.view;

import net.sourceforge.schemaspy.model.Table;
import net.sourceforge.schemaspy.model.TableColumn;
import net.sourceforge.schemaspy.util.Dot;

/**
 * Represents Graphvis dot's concept of an edge.  That is, a connector between two nodes.
 *
 * @author John Currier
 */
public class DotConnector implements Comparable<DotConnector> {

    private final TableColumn parentColumn;

    private final Table parentTable;

    private final TableColumn childColumn;

    private final Table childTable;

    private final boolean implied;

    private final boolean bottomJustify;

    private String parentPort;

    private String childPort;

    /**
     * Create an edge that logically connects a child column to a parent column.
     *
     * @param parentColumn TableColumn
     * @param childColumn TableColumn
     * @param implied boolean
     */
    public DotConnector(TableColumn parentColumn, TableColumn childColumn, boolean implied) {
        this.parentColumn = parentColumn;
        this.childColumn = childColumn;
        this.implied = implied;
        parentPort = parentColumn.getName();
        parentTable = parentColumn.getTable();
        childPort = childColumn.getName();
        childTable = childColumn.getTable();
        bottomJustify = !Dot.getInstance().supportsCenteredEastWestEdges();
    }

    /**
     * Returns true if this edge logically "points to" the specified table
     *
     * @param possibleParentTable Table
     * @return boolean
     */
    public boolean pointsTo(Table possibleParentTable) {
        return possibleParentTable.equals(parentTable);
    }

    public boolean isImplied() {
        return implied;
    }

    /**
     * By default a parent edge connects to the column name...this lets you
     * connect it the parent's type column instead (e.g. for detailed parents)
     *
     * Yes, I need to find a more appropriate name/metaphor for this method....
     */
    public void connectToParentDetails() {
        parentPort = parentColumn.getName() + ".type";
    }

    public void connectToParentTitle() {
        parentPort = "elipses";
    }

    public void connectToChildTitle() {
        childPort = "elipses";
    }

    @Override
    public String toString() {
        StringBuilder edge = new StringBuilder();
        edge.append("  \"");
        if (childTable.isRemote()) {
            edge.append(childTable.getContainer());
            edge.append('.');
        }
        edge.append(childTable.getName());
        edge.append("\":\"");
        edge.append(childPort);
        edge.append("\":");
        if (bottomJustify) edge.append("s");
        edge.append("w -> \"");
        if (parentTable.isRemote()) {
            edge.append(parentTable.getContainer());
            edge.append('.');
        }
        edge.append(parentTable.getName());
        edge.append("\":\"");
        edge.append(parentPort);
        edge.append("\":");
        if (bottomJustify) edge.append("s");
        edge.append("e ");
        final boolean fullErNotation = false;
        if (fullErNotation) {
            edge.append("[arrowhead=");
            if (childColumn.isNullable()) edge.append("odottee"); else edge.append("teetee");
            edge.append(" dir=both");
        } else {
            edge.append("[arrowhead=none");
            edge.append(" dir=back");
        }
        edge.append(" arrowtail=");
        if (childColumn.isUnique()) edge.append("teeodot"); else edge.append("crowodot");
        if (implied) edge.append(" style=dashed");
        edge.append("];");
        return edge.toString();
    }

    public int compareTo(DotConnector other) {
        int rc = childTable.compareTo(other.childTable);
        if (rc == 0) rc = childColumn.getName().compareToIgnoreCase(other.childColumn.getName());
        if (rc == 0) rc = parentTable.compareTo(other.parentTable);
        if (rc == 0) rc = parentColumn.getName().compareToIgnoreCase(other.parentColumn.getName());
        if (rc == 0 && implied != other.implied) rc = implied ? 1 : -1;
        return rc;
    }

    @Override
    public boolean equals(Object other) {
        if (!(other instanceof DotConnector)) return false;
        return compareTo((DotConnector) other) == 0;
    }

    @Override
    public int hashCode() {
        int p = parentTable == null ? 0 : parentTable.getName().hashCode();
        int c = childTable == null ? 0 : childTable.getName().hashCode();
        return (p << 16) & c;
    }

    public TableColumn getParentColumn() {
        return parentColumn;
    }

    public Table getParentTable() {
        return parentTable;
    }

    public TableColumn getChildColumn() {
        return childColumn;
    }

    public Table getChildTable() {
        return childTable;
    }
}
