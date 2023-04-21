package schemacrawler.crawl;

import schemacrawler.schema.Procedure;
import schemacrawler.schema.ProcedureColumn;
import schemacrawler.schema.ProcedureColumnType;

/**
 * Represents a column in a database procedure. Created from metadata
 * returned by a JDBC call.
 * 
 * @author Sualeh Fatehi
 */
final class MutableProcedureColumn extends AbstractColumn<Procedure> implements ProcedureColumn {

    private static final long serialVersionUID = 3546361725629772857L;

    private ProcedureColumnType procedureColumnType;

    MutableProcedureColumn(final Procedure parent, final String name) {
        super(parent, name);
    }

    /**
   * {@inheritDoc}
   * 
   * @see ProcedureColumn#getPrecision()
   */
    @Override
    public int getPrecision() {
        return getDecimalDigits();
    }

    /**
   * {@inheritDoc}
   * 
   * @see ProcedureColumn#getProcedureColumnType()
   */
    @Override
    public ProcedureColumnType getProcedureColumnType() {
        return procedureColumnType;
    }

    void setPrecision(final int precision) {
        setDecimalDigits(precision);
    }

    void setProcedureColumnType(final ProcedureColumnType procedureColumnType) {
        this.procedureColumnType = procedureColumnType;
    }
}
