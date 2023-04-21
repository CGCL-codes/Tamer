package db.coted.model.corporate;

import net.jadoth.sqlengine.sql.columntypes.BIGINT;
import net.jadoth.sqlengine.sql.columntypes.DECIMAL;
import net.jadoth.sqlengine.sql.columntypes.SMALLINT;
import net.jadoth.sqlengine.sql.columntypes.TIMESTAMP;
import net.jadoth.sqlengine.sql.columntypes.TINYINT;
import net.jadoth.sqlengine.sql.syntax.FOREIGN_KEY;
import db.coted.model.AbstractCTDSchema;
import db.coted.model.general.ScmGeneral;
import db.coted.model.general.TblPerson;

public class TblPayroll extends AbstractTblCompanyPart<TblPayroll> {

    public final SMALLINT year = SMALLINT(NOT_NULL);

    public final TINYINT month = TINYINT(NOT_NULL);

    public final TIMESTAMP transferTime = TIMESTAMP(NOT_NULL);

    public final BIGINT employee = BIGINT(NOT_NULL);

    public final DECIMAL amount = DECIMAL(14, 2);

    FOREIGN_KEY fkRefEmployee = FOREIGN_KEY(this.employee).REFERENCES(table(ScmCorporate.class, TblEmployee.class), TblEmployee.ID);

    FOREIGN_KEY fkRefPersonTest = FOREIGN_KEY(this.employee).REFERENCES(table(ScmGeneral.class, TblPerson.class), TblPerson.ID);

    protected TblPayroll(final AbstractCTDSchema schema) {
        super(schema);
    }

    protected TblPayroll(final AbstractCTDSchema schema, final String name) {
        super(schema, name);
    }
}
