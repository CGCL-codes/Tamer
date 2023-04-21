package net.sourceforge.squirrel_sql.client.mainframe.action;

import java.awt.event.ActionEvent;
import net.sourceforge.squirrel_sql.fw.sql.ISQLAlias;
import net.sourceforge.squirrel_sql.client.action.SquirrelAction;
import net.sourceforge.squirrel_sql.client.IApplication;
import net.sourceforge.squirrel_sql.client.mainframe.AliasesList;

/**
 * This <CODE>Action</CODE> allows the user to connect to an alias.
 *
 * @author  <A HREF="mailto:colbell@users.sourceforge.net">Colin Bell</A>
 */
public class ConnectToAliasAction extends SquirrelAction {

    /**
     * List of all the users aliases.
     */
    private AliasesList _aliases;

    /**
     * Ctor specifying the list of aliases.
     *
     * @param   app     Application API.
     * @param   list    List of <TT>ISQLAlias</TT> objects.
     */
    public ConnectToAliasAction(IApplication app, AliasesList list) {
        super(app);
        _aliases = list;
    }

    /**
     * Perform this action. Retrieve the current alias from this list and
     * connect to it.
     *
     * @param   evt     The current event.
     */
    public void actionPerformed(ActionEvent evt) {
        final ISQLAlias alias = _aliases.getSelectedAlias();
        if (alias != null) {
            new ConnectToAliasCommand(getApplication(), getParentFrame(evt), alias).execute();
        }
    }
}
