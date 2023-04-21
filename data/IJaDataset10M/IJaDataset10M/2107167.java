package schemacrawler.crawl;

import schemacrawler.crawl.JavaSqlType.JavaSqlTypeGroup;
import schemacrawler.schema.BaseColumn;
import schemacrawler.schema.ColumnDataType;
import schemacrawler.schema.DatabaseObject;
import schemacrawler.schema.NamedObject;

/**
 * Represents a column in a database for tables and procedures. Created
 * from metadata returned by a JDBC call.
 * 
 * @author Sualeh Fatehi
 */
abstract class AbstractColumn<P extends DatabaseObject> extends AbstractDependantObject<P> implements BaseColumn<P> {

    private static final long serialVersionUID = -8492662324895309485L;

    private ColumnDataType type;

    private int ordinalPosition;

    private int size;

    private int decimalDigits;

    private boolean nullable;

    AbstractColumn(final P parent, final String name) {
        super(parent, name);
    }

    /**
   * {@inheritDoc}
   */
    @Override
    public int compareTo(final NamedObject obj) {
        if (obj == null) {
            return -1;
        }
        final BaseColumn<P> other = (BaseColumn<P>) obj;
        int comparison = 0;
        if (comparison == 0) {
            comparison = ordinalPosition - other.getOrdinalPosition();
        }
        if (comparison == 0) {
            comparison = super.compareTo(other);
        }
        return comparison;
    }

    /**
   * {@inheritDoc}
   * 
   * @see schemacrawler.schema.BaseColumn#getDecimalDigits()
   */
    @Override
    public final int getDecimalDigits() {
        return decimalDigits;
    }

    /**
   * {@inheritDoc}
   * 
   * @see schemacrawler.schema.BaseColumn#getOrdinalPosition()
   */
    @Override
    public final int getOrdinalPosition() {
        return ordinalPosition;
    }

    /**
   * {@inheritDoc}
   * 
   * @see schemacrawler.schema.BaseColumn#getSize()
   */
    @Override
    public final int getSize() {
        return size;
    }

    /**
   * {@inheritDoc}
   * 
   * @see schemacrawler.schema.BaseColumn#getType()
   */
    @Override
    public final ColumnDataType getType() {
        return type;
    }

    /**
   * {@inheritDoc}
   * 
   * @see schemacrawler.schema.BaseColumn#getWidth()
   */
    @Override
    public final String getWidth() {
        final ColumnDataType columnDataType = getType();
        if (columnDataType == null) {
            return "";
        }
        if (size == 0 || size == Integer.MIN_VALUE || size == Integer.MAX_VALUE) {
            return "";
        }
        final JavaSqlTypeGroup sqlDataTypeGroup = JavaSqlTypesUtility.lookupSqlDataType(columnDataType.getType()).getJavaSqlTypeGroup();
        final boolean needWidth = sqlDataTypeGroup == JavaSqlTypeGroup.character || sqlDataTypeGroup == JavaSqlTypeGroup.real;
        final StringBuilder columnWidthBuffer = new StringBuilder();
        if (needWidth) {
            columnWidthBuffer.append('(');
            columnWidthBuffer.append(size);
            if (sqlDataTypeGroup == JavaSqlTypeGroup.real) {
                columnWidthBuffer.append(", ").append(getDecimalDigits());
            }
            columnWidthBuffer.append(')');
        }
        return columnWidthBuffer.toString();
    }

    /**
   * {@inheritDoc}
   * 
   * @see schemacrawler.schema.BaseColumn#isNullable()
   */
    @Override
    public final boolean isNullable() {
        return nullable;
    }

    final void setDecimalDigits(final int decimalDigits) {
        this.decimalDigits = decimalDigits;
    }

    final void setNullable(final boolean nullable) {
        this.nullable = nullable;
    }

    final void setOrdinalPosition(final int ordinalPosition) {
        this.ordinalPosition = ordinalPosition;
    }

    /**
   * Sets the column size.
   * 
   * @param size
   *        Size of the column
   */
    final void setSize(final int size) {
        this.size = size;
    }

    void setType(final ColumnDataType type) {
        this.type = type;
    }
}
