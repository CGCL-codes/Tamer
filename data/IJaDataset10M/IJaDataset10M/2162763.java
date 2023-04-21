package db.coted.model.general;

import net.jadoth.sqlengine.sql.columntypes.VARCHAR;
import db.coted.model.AbstractCTDSchema;

public abstract class AbstractTblNamedEntity<T extends AbstractTblNamedEntity<T>> extends AbstractTblEntity<T> {

    public final VARCHAR name = VARCHAR(50, NOT_NULL);

    protected AbstractTblNamedEntity(final AbstractCTDSchema schema) {
        super(schema);
    }

    protected AbstractTblNamedEntity(final AbstractCTDSchema schema, final String name) {
        super(schema, name);
    }
}
