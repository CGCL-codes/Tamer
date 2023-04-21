package net.sourceforge.squirrel_sql.client.session.mainpanel.objecttree.tabs.procedure;

import net.sourceforge.squirrel_sql.fw.sql.IProcedureInfo;
import net.sourceforge.squirrel_sql.client.session.mainpanel.objecttree.tabs.BaseDataSetTab;

/**
 * Base class for tabs to the added to <TT>ProcedurePanel</TT>. If you are
 * writing a class for a tab to be added to <TT>ProcedurePanel</TT> you don't
 * have to inherit from this class (only implement <TT>IProcedurePanelTab</TT>)
 * but it has convenience methods.
 *
 * @author  <A HREF="mailto:colbell@users.sourceforge.net">Colin Bell</A>
 */
public abstract class BaseProcedureTab extends BaseDataSetTab implements IProcedureTab {

    /**
	 * Set the <TT>IProcedureInfo</TT> object that specifies the procedure that
	 * is to have its information displayed.
	 *
	 * @param	value	<TT>IProcedureInfo</TT> object that specifies the currently
	 *					selected procedure. This can be <TT>null</TT>.
	 */
    public void setProcedureInfo(IProcedureInfo value) {
        setDatabaseObjectInfo(value);
    }

    /**
	 * Retrieve the current <TT>IProcedureInfo</TT> object.
	 *
	 * @return	Current <TT>IProcedureInfo</TT> object.
	 */
    public final IProcedureInfo getProcedureInfo() {
        return (IProcedureInfo) getDatabaseObjectInfo();
    }
}
