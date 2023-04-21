package org.streets.eis.component.model;

import java.util.List;
import org.streets.eis.EisApplication;
import org.streets.context.definition.ContextDefinition;
import org.streets.context.definition.FunctionDefinition;
import org.streets.context.definition.ModuleDefinition;
import org.streets.eis.EisWebSession;
import org.streets.eis.entity.User;
import org.streets.eis.setting.auth.UserPermitAuthorizer;

public class MenuModel extends DataModel<MenuItem> {

    private static final long serialVersionUID = 1L;

    private UserPermitAuthorizer authorizer;

    public MenuModel(ContextDefinition definition) {
        super();
        authorizer = EisApplication.get().getRegistry().service(UserPermitAuthorizer.class);
        List<ModuleDefinition> mds = definition.getModules();
        User user = EisWebSession.get().getUser();
        for (ModuleDefinition md : mds) {
            if (md.getType() != 1) continue;
            init(this, md, user);
        }
    }

    private void init(DataModel<MenuItem> dm, ModuleDefinition def, User user) {
        if (authorizer != null) {
            boolean b = authorizer.authorized(user, "module:" + def.getId());
            if (!b) return;
        }
        DataModel<MenuItem> _dm = new DataModel<MenuItem>(new MenuItem(def.getId(), "", def.getName(), def.getPortal(), def.getHint()).setIcon(def.getIcon()));
        dm.addChild(_dm);
        List<FunctionDefinition> funcs = def.getFunctions();
        for (FunctionDefinition func : funcs) {
            if (authorizer != null) {
                boolean b = authorizer.authorized(user, "func:" + func.getId());
                if (!b) {
                    continue;
                }
            }
            _dm.addChild(new DataModel<MenuItem>(new MenuItem(func.getId(), "", func.getName(), func.getPortal(), func.getHint()).setIcon(def.getIcon())));
        }
        List<ModuleDefinition> mds = def.getSubModules();
        for (ModuleDefinition md : mds) {
            if (md.getType() != 1) continue;
            init(_dm, md, user);
        }
    }
}
