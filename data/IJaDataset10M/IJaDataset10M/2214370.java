package semantic.SQL;

import generate.CBaseLanguageExporter;
import semantic.CBaseActionEntity;
import semantic.CDataEntity;
import utils.CObjectCatalog;

public abstract class CEntitySQLOpenStatement extends CBaseActionEntity {

    public CEntitySQLOpenStatement(int line, CObjectCatalog cat, CBaseLanguageExporter out, CEntitySQLCursor cur) {
        super(line, cat, out);
        m_cursor = cur;
    }

    protected CEntitySQLCursor m_cursor = null;

    protected CDataEntity m_VariableStatement = null;

    public boolean ignore() {
        return false;
    }

    /**
	 * @param var
	 */
    public void setVariableStatement(CDataEntity var) {
        m_VariableStatement = var;
    }
}
