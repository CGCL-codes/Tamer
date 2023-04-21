package net.sourceforge.squirrel_sql.client.session.objectstree.procedurepanel;

import net.sourceforge.squirrel_sql.fw.sql.IProcedureInfo;
import net.sourceforge.squirrel_sql.client.session.objectstree.objectpanel.IObjectPanelTab;

/**
 * This interface defines the behaviour for a tab in <TT>ProcedurePanel</TT>, the
 * panel displayed when a table is selected in the object tree.
 *
 * @author  <A HREF="mailto:colbell@users.sourceforge.net">Colin Bell</A>
 */
public interface IProcedurePanelTab extends IObjectPanelTab {

    /**
	 * Set the <TT>IProcedureInfo</TT> object that specifies the table that
	 * is to have its information displayed.
	 *
	 * @param	value	<TT>IProcedureInfo</TT> object that specifies the currently
	 *					selected procedure. This can be <TT>null</TT>.
	 */
    void setProcedureInfo(IProcedureInfo value);
}
