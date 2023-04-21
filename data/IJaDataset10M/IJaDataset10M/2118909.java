package de.mguennewig.pobjects;

import de.mguennewig.pobjects.metadata.*;

/**
 * Represents a table expression within an {@link Query#addTableExpr SQL FROM}.
 *
 * @author Michael G�nnewig
 */
public class TableRef {

    private final TableExpr te;

    private final String alias;

    private final int fromIdx;

    /** Creates a new table reference. */
    public TableRef(final TableExpr te, final String alias, final int fromIdx) {
        this.te = te;
        this.alias = alias;
        this.fromIdx = fromIdx;
    }

    public final String getColumnRef(final Column col) {
        return alias + "." + col.getSchemaName();
    }

    public final String getIdColumnRef() {
        return alias + "." + getIdField().getSchemaName();
    }

    public final int getFromIndex() {
        return fromIdx;
    }

    public final String getPClassColumnRef() {
        if (!(te instanceof ClassDecl)) throw new IllegalStateException("referenced table is not a class");
        if (!((ClassDecl) te).isExtensible()) throw new IllegalStateException("referenced class is not extensible");
        return alias + "." + Pclass.cdeclPclass.getSchemaName();
    }

    public final String getAlias() {
        return alias;
    }

    public final TableExpr getTableExpr() {
        return te;
    }

    public final IdField getIdField() {
        if (!(te instanceof ClassDecl)) throw new IllegalStateException("referenced table is not a class");
        return ((ClassDecl) te).getIdField();
    }

    /** {@inheritDoc} */
    @Override
    public String toString() {
        return getTableExpr().getName() + " " + getAlias();
    }
}
