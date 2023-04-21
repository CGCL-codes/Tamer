package semantic.Verbs;

import generate.CBaseLanguageExporter;
import semantic.CBaseActionEntity;
import semantic.CDataEntity;
import utils.CObjectCatalog;

public abstract class CEntitySortRelease extends CBaseActionEntity {

    public CEntitySortRelease(int line, CObjectCatalog cat, CBaseLanguageExporter out) {
        super(line, cat, out);
    }

    protected CDataEntity m_eSortField = null;

    public void setDataReference(CDataEntity e) {
        m_eSortField = e;
    }

    protected CDataEntity m_eDatReference = null;

    public void setDataReference(CDataEntity e, CDataEntity from) {
        m_eDatReference = from;
        m_eSortField = e;
    }
}
