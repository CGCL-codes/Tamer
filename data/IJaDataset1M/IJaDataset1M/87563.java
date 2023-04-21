package net.sourceforge.squirrel_sql.client.session.action;

import java.awt.event.ActionEvent;
import net.sourceforge.squirrel_sql.client.IApplication;
import net.sourceforge.squirrel_sql.client.action.SquirrelAction;
import net.sourceforge.squirrel_sql.client.session.SessionSheet;

public class SessionSheetPropertiesAction extends SquirrelAction implements ISessionSheetAction {

    private SessionSheet _sheet;

    public SessionSheetPropertiesAction(IApplication app) {
        super(app);
    }

    public void setSessionSheet(SessionSheet sheet) {
        _sheet = sheet;
    }

    public void actionPerformed(ActionEvent evt) {
        if (_sheet != null) {
            new SessionSheetPropertiesCommand(this.getParentFrame(evt), _sheet.getSession()).execute();
        }
    }
}
