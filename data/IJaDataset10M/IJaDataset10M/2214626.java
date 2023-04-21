package com.marce.remiseria.web.paneles.main;

import java.util.List;
import org.apache.wicket.authorization.Action;
import org.apache.wicket.authorization.strategies.role.annotations.AuthorizeAction;
import org.apache.wicket.extensions.markup.html.tabs.AbstractTab;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.IModel;
import com.marce.remiseria.core.roles.Rol;
import com.marce.remiseria.web.paneles.root.RootPanel;

@AuthorizeAction(action = Action.RENDER, roles = "root")
public class RootTab extends AbstractTab implements RolesTab {

    public RootTab(IModel model) {
        super(model);
    }

    /**
	 * 
	 */
    private static final long serialVersionUID = 5200590340151268198L;

    @Override
    public Panel getPanel(String id) {
        return new RootPanel(id);
    }

    @Override
    public boolean hasAnyRoles(List<Rol> roles) {
        for (Rol rol : roles) {
            if (rol.hasRoles("root")) return true;
        }
        return false;
    }
}
