package net.sourceforge.squirrel_sql.client.mainframe.action;

import net.sourceforge.squirrel_sql.fw.util.ICommand;
import net.sourceforge.squirrel_sql.client.IApplication;
import net.sourceforge.squirrel_sql.client.gui.ViewLogsSheet;

/**
 * This <CODE>ICommand</CODE> displays the Logs window.
 *
 * @author <A HREF="mailto:colbell@users.sourceforge.net">Colin Bell</A>
 */
public class ViewLogsCommand implements ICommand {

    /** Application API. */
    private IApplication _app;

    /**
	 * Ctor.
	 *
	 * @param	app		Application API.
	 *
	 * @throws	IllegalArgumentException
	 *			Thrown if a <TT>null</TT> <TT>IApplication</TT> passed.
	 */
    public ViewLogsCommand(IApplication app) {
        super();
        if (app == null) {
            throw new IllegalArgumentException("Null IApplication passed");
        }
        _app = app;
    }

    /**
	 * Display the Dialog
	 */
    public void execute() {
        ViewLogsSheet.showSheet(_app);
    }
}
