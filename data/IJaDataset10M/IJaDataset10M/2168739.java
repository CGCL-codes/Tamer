package net.sourceforge.squirrel_sql.client.mainframe.action;

import java.awt.Frame;
import java.text.MessageFormat;
import net.sourceforge.squirrel_sql.fw.gui.Dialogs;
import net.sourceforge.squirrel_sql.fw.persist.ValidationException;
import net.sourceforge.squirrel_sql.fw.sql.ISQLAlias;
import net.sourceforge.squirrel_sql.fw.util.ICommand;
import net.sourceforge.squirrel_sql.client.IApplication;
import net.sourceforge.squirrel_sql.client.db.AliasMaintDialog;
import net.sourceforge.squirrel_sql.client.db.DataCache;
import net.sourceforge.squirrel_sql.client.util.IdentifierFactory;
import net.sourceforge.squirrel_sql.fw.util.Logger;

/**
 * This <CODE>ICommand</CODE> allows the user to delete an existing
 * <TT>ISQLAlias</TT>.
 *
 * @author  <A HREF="mailto:colbell@users.sourceforge.net">Colin Bell</A>
 */
public class DeleteAliasCommand implements ICommand {

    /**
     * This interface defines locale specific strings. This should be
     * replaced with a property file.
     */
    private interface i18n {

        String MSG_CONFIRM = "Are you sure to want to delete the alias \"{0}\"?";
    }

    /** Application API. */
    private final IApplication _app;

    /** Owner of the maintenance dialog. */
    private Frame _frame;

    /** <TT>ISQLAlias</TT> to be deleted. */
    private ISQLAlias _sqlAlias;

    /**
     * Ctor.
     *
     * @param   app         Application API.
     * @param   frame       Owning <TT>Frame</TT>.
     * @param   sqlAlias    <ISQLAlias</TT> to be deleted.
     *
     * @throws  IllegalArgumentException
     *              Thrown if a <TT>null</TT> <TT>ISQLAlias</TT> or
     *              <TT>IApplication</TT> passed.
     */
    public DeleteAliasCommand(IApplication app, Frame frame, ISQLAlias sqlAlias) throws IllegalArgumentException {
        super();
        if (app == null) {
            throw new IllegalArgumentException("Null IApplication passed");
        }
        if (sqlAlias == null) {
            throw new IllegalArgumentException("Null ISQLAlias passed");
        }
        _app = app;
        _frame = frame;
        _sqlAlias = sqlAlias;
    }

    /**
     * Delete the current <TT>ISQLAlias</TT> after confirmation.
     */
    public void execute() {
        Object[] args = { _sqlAlias.getName() };
        String msg = MessageFormat.format(i18n.MSG_CONFIRM, args);
        if (Dialogs.showYesNo(_frame, msg)) {
            _app.getDataCache().removeAlias(_sqlAlias);
        }
    }
}
