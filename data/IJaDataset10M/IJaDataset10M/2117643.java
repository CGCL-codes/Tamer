package net.sourceforge.squirrel_sql.client.mainframe.action;

import java.awt.Frame;
import net.sourceforge.squirrel_sql.fw.persist.ValidationException;
import net.sourceforge.squirrel_sql.fw.sql.ISQLDriver;
import net.sourceforge.squirrel_sql.fw.util.ICommand;
import net.sourceforge.squirrel_sql.client.IApplication;
import net.sourceforge.squirrel_sql.client.db.DriverMaintDialog;
import net.sourceforge.squirrel_sql.client.db.DataCache;
import net.sourceforge.squirrel_sql.client.util.IdentifierFactory;
import net.sourceforge.squirrel_sql.fw.util.Logger;

/**
 * This <CODE>ICommand</CODE> allows the user to copy an existing
 * <TT>ISQLDriver</TT> to a new one and then maintain the new one.
 *
 * @author  <A HREF="mailto:colbell@users.sourceforge.net">Colin Bell</A>
 */
public class CopyDriverCommand implements ICommand {

    /** Application API. */
    private final IApplication _app;

    /** Owner of the maintenance dialog. */
    private final Frame _frame;

    /** <TT>ISQLDriver</TT> to be copied. */
    private final ISQLDriver _sqlDriver;

    /**
     * Ctor.
     *
     * @param   app         Application API.
     * @param   frame       Owning <TT>Frame</TT>.
     * @param   sqlDriver   <TT>ISQLDriver</TT> to be copied.
     *
     * @throws  IllegalArgumentException
     *              Thrown if a <TT>null</TT> <TT>ISQLDriver</TT> or
     *              <TT>IApplication</TT> passed.
     */
    public CopyDriverCommand(IApplication app, Frame frame, ISQLDriver sqlDriver) throws IllegalArgumentException {
        super();
        if (app == null) {
            throw new IllegalArgumentException("Null IApplication passed");
        }
        if (sqlDriver == null) {
            throw new IllegalArgumentException("Null ISQLDriver passed");
        }
        _app = app;
        _frame = frame;
        _sqlDriver = sqlDriver;
    }

    /**
     * Copy the current >ISQLDriver</TT> and then display a dialog allowing user
     * to maintain the new one.
     */
    public void execute() {
        final DataCache cache = _app.getDataCache();
        final IdentifierFactory factory = IdentifierFactory.getInstance();
        ISQLDriver newDriver = cache.createDriver(factory.createIdentifier());
        try {
            newDriver.assignFrom(_sqlDriver);
        } catch (ValidationException ex) {
            _app.getLogger().showMessage(Logger.ILogTypes.ERROR, ex);
        }
        new DriverMaintDialog(_app, _frame, newDriver, DriverMaintDialog.MaintenanceType.COPY).show();
    }
}
