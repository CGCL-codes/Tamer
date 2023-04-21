package org.starobjects.wicket.viewer.common.cssmenu;

import org.apache.wicket.Component;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.model.IModel;
import org.starobjects.wicket.viewer.base.PanelAbstract;

abstract class CssMenuItemPanelAbstract<T extends IModel<?>> extends PanelAbstract<T> {

    private static final long serialVersionUID = 1L;

    public CssMenuItemPanelAbstract(String id, T model) {
        super(id, model);
        setRenderBodyOnly(true);
    }

    protected void addSubMenuItems(WebMarkupContainer markupContainer, CssMenuItem cssMenuItem) {
        Component linkComponent = Utils.addLink(markupContainer, cssMenuItem);
        Utils.addSubMenuItemsIfAny(markupContainer, cssMenuItem);
        Utils.addCssClassAttributesIfRequired(cssMenuItem, linkComponent);
    }
}
