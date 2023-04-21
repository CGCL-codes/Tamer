package net.sourceforge.squirrel_sql.client.session.action;

import java.awt.event.ActionEvent;
import net.sourceforge.squirrel_sql.fw.gui.CursorChanger;
import net.sourceforge.squirrel_sql.client.IApplication;
import net.sourceforge.squirrel_sql.client.action.SquirrelAction;
import net.sourceforge.squirrel_sql.client.session.ISQLPanelAPI;

/**
 * This <CODE>Action</CODE> allows the user to close all the SQL
 * result tabs for the current session.
 *
 * @author <A HREF="mailto:colbell@users.sourceforge.net">Colin Bell</A>
 */
public class ToolsPopupAction extends SquirrelAction implements ISQLPanelAction {

    private ISQLPanelAPI _panel;

    /**
	 * Ctor.
	 *
	 * @param	app		Application API.
	 */
    public ToolsPopupAction(IApplication app) {
        super(app);
    }

    public void setSQLPanel(ISQLPanelAPI panel) {
        _panel = panel;
        setEnabled(null != panel);
    }

    /**
	 * Perform this action. Use the <TT>CloseAllSQLResultTabsCommand</TT>.
	 * 
	 * @param	evt		The current event.
	 */
    public void actionPerformed(ActionEvent evt) {
        if (null == _panel) {
            return;
        }
        _panel.showToolsPopup();
    }
}
