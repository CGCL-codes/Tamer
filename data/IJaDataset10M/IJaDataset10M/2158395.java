package net.sourceforge.squirrel_sql.client.action;

import net.sourceforge.squirrel_sql.fw.gui.action.BaseAction;
import net.sourceforge.squirrel_sql.fw.util.Resources;
import net.sourceforge.squirrel_sql.client.IApplication;

public abstract class SquirrelAction extends BaseAction {

    private IApplication _app;

    protected SquirrelAction(IApplication app) {
        this(app, app.getResources());
    }

    protected SquirrelAction(IApplication app, Resources rsrc) {
        super();
        if (app == null) {
            throw new IllegalArgumentException("Null IApplication passed");
        }
        if (rsrc == null) {
            throw new IllegalArgumentException("No Resources object in IApplication");
        }
        _app = app;
        rsrc.setupAction(this);
    }

    protected IApplication getApplication() {
        return _app;
    }
}
