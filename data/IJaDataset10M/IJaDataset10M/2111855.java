package jaxlib.persistence.model.binding;

import java.util.Arrays;
import java.util.List;
import java.util.Locale;
import javax.swing.event.TableModelEvent;
import javax.swing.event.TableModelListener;
import javax.swing.table.AbstractTableModel;
import javax.swing.table.TableModel;
import jaxlib.persistence.model.EntityAttribute;
import jaxlib.persistence.model.EntityClass;
import jaxlib.lang.Objects;
import jaxlib.util.CheckArg;

/**
 * @author  jw
 * @since   JaXLib 1.0
 * @version $Id: EntityPropertyTableModel.java 2269 2007-03-16 08:40:10Z joerg_wassmer $
 */
public class EntityPropertyTableModel<E> extends AbstractTableModel {

    /**
   * @since JaXLib 1.0
   */
    private static final long serialVersionUID = 1L;

    private final E entity;

    private final List<EntityAttribute<E, ?>> rows;

    protected String nameColumnLabel;

    protected String valueColumnLabel;

    public EntityPropertyTableModel(final E entity, final List<EntityAttribute<E, ?>> rows) {
        super();
        CheckArg.notNull(entity, "entity");
        CheckArg.notNull(rows, "rows");
        this.nameColumnLabel = "Name";
        this.valueColumnLabel = "Value";
        this.entity = entity;
        this.rows = rows;
    }

    public EntityPropertyTableModel(final E entity, final EntityAttribute<E, ?>... rows) {
        this(entity, Arrays.asList(rows));
    }

    @Override
    public Class getColumnClass(final int column) {
        return (column == 0) ? String.class : Object.class;
    }

    @Override
    public int getColumnCount() {
        return 2;
    }

    @Override
    public String getColumnName(final int column) {
        return (column == 0) ? this.nameColumnLabel : (column == 1) ? this.valueColumnLabel : super.getColumnName(column);
    }

    public E getEntity() {
        return this.entity;
    }

    public EntityAttribute<E, ?> getEntityAttribute(final int rowIndex) {
        return this.rows.get(rowIndex);
    }

    public String getNameColumnLabel() {
        return this.nameColumnLabel;
    }

    public String getValueColumnLabel() {
        return this.valueColumnLabel;
    }

    @Override
    public int getRowCount() {
        return this.rows.size();
    }

    @Override
    public Object getValueAt(final int row, final int column) {
        final EntityAttribute<E, ?> a = getEntityAttribute(row);
        if (a == null) return null; else if (column == 0) return a.getDisplayName(); else if (column == 1) return a.get(this.entity); else return null;
    }

    @Override
    public boolean isCellEditable(final int row, final int column) {
        if (column != 1) return false;
        final EntityAttribute<E, ?> a = getEntityAttribute(row);
        return (a != null) && a.updatable;
    }

    public void setNameColumnLabel(final String v) {
        if (!Objects.equals(this.nameColumnLabel, v)) {
            this.nameColumnLabel = v;
            fireTableCellUpdated(TableModelEvent.HEADER_ROW, 0);
        }
    }

    public void setValueColumnLabel(final String v) {
        if (!Objects.equals(this.valueColumnLabel, v)) {
            this.valueColumnLabel = v;
            fireTableCellUpdated(TableModelEvent.HEADER_ROW, 1);
        }
    }

    @Override
    public final void setValueAt(final Object newValue, final int row, final int column) {
        if (column != 1) throw new IllegalArgumentException("column is not editable: " + column);
        @SuppressWarnings("unchecked") final EntityAttribute<E, Object> a = (EntityAttribute) getEntityAttribute(row);
        if (a == null) {
            throw new IllegalStateException("attribute at row " + row + " is null");
        } else {
            a.set(this.entity, newValue);
            fireTableCellUpdated(row, column);
        }
    }
}
