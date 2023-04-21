package cn.myapps.core.table.ddlutil.db2;

import java.sql.Connection;
import java.sql.SQLException;
import cn.myapps.core.dynaform.form.ejb.Confirm;
import cn.myapps.core.table.alteration.ColumnDataTypeChange;
import cn.myapps.core.table.constants.ConfirmConstant;
import cn.myapps.core.table.ddlutil.AbstractValidator;
import cn.myapps.util.DbTypeUtil;
import cn.myapps.util.sequence.Sequence;
import cn.myapps.util.sequence.SequenceException;

/**
 * 
 * @author Chris
 * 
 */
public class DB2Validator extends AbstractValidator {

    public DB2Validator(Connection conn) {
        super(conn, new DB2Builder());
        this.schema = DbTypeUtil.getSchema(conn, DbTypeUtil.DBTYPE_DB2);
        _builder.setSchema(schema);
    }

    public void checkChange(ColumnDataTypeChange change) throws SQLException, SequenceException {
        Confirm confirm = new Confirm(change.getTable().getFormName(), ConfirmConstant.FIELD_TYPE_INCOMPATIBLE);
        confirm.setId(Sequence.getSequence());
        confirm.setNewFieldId(change.getTargetColumn().getId());
        confirm.setOldFieldId(change.getSourceColumn().getId());
        confirm.setFieldName(change.getSourceColumn().getFieldName());
        confirms.add(confirm);
    }

    protected String getCatalog() {
        return null;
    }

    protected String getSchemaPattern() {
        return schema;
    }
}
