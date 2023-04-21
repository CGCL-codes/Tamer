package db.coted.model.products;

import net.jadoth.sqlengine.sql.columntypes.BIGINT;
import net.jadoth.sqlengine.sql.columntypes.DOUBLE;
import db.coted.model.AbstractCTDSchema;
import db.coted.model.AbstractCTDTable;

public class TblPricing extends AbstractCTDTable<TblPricing> {

    public final BIGINT product = BIGINT(NOT_NULL);

    public final BIGINT priceGroup = BIGINT(NOT_NULL);

    public final DOUBLE price = DOUBLE();

    protected TblPricing(final AbstractCTDSchema schema) {
        super(schema);
    }
}
