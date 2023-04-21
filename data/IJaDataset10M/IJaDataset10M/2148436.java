package datadog.db.model;

import datadog.util.Validate;

/**
 * Joins two columns by creating a Reference between them.
 * <p/>
 *
 * @author Justin Tomich
 * @version $Id: Reference.java 201 2006-05-24 02:22:30Z tomichj $
 */
public class Reference {

    private final Column sourceColumn;

    private final Column targetColumn;

    public Reference(Column sourceColumn, Column targetColumn) {
        Validate.notNull(sourceColumn);
        Validate.notNull(targetColumn);
        this.sourceColumn = sourceColumn;
        this.targetColumn = targetColumn;
    }

    public boolean contains(Column c) {
        if (sourceColumn.equals(c)) return true;
        if (targetColumn.equals(c)) return true;
        return false;
    }

    public Column getSourceColumn() {
        return sourceColumn;
    }

    public Column getTargetColumn() {
        return targetColumn;
    }

    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        final Reference reference = (Reference) o;
        if (sourceColumn != null ? !sourceColumn.equals(reference.sourceColumn) : reference.sourceColumn != null) {
            return false;
        }
        if (targetColumn != null ? !targetColumn.equals(reference.targetColumn) : reference.targetColumn != null) {
            return false;
        }
        return true;
    }

    public int hashCode() {
        int result;
        result = (sourceColumn != null ? sourceColumn.hashCode() : 0);
        result = 29 * result + (targetColumn != null ? targetColumn.hashCode() : 0);
        return result;
    }

    public String toString() {
        return "Reference{" + "sourceColumnName='" + sourceColumn.getFullName() + "'" + ", targetColumnName='" + targetColumn.getFullName() + "'" + "}";
    }
}
