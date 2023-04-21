package utils;

import semantic.CBaseEntityFactory;

public abstract class CBaseRecusriveSpecialAction {

    protected CObjectCatalog m_ProgramCatalog = null;

    protected CBaseEntityFactory m_Factory = null;

    public CBaseRecusriveSpecialAction(CObjectCatalog programCatalog, CBaseEntityFactory factory) {
        m_ProgramCatalog = programCatalog;
        m_Factory = factory;
    }
}
