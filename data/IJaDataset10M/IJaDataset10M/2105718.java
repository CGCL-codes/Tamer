package org.streets.eis.component.dropdown;

import org.apache.wicket.markup.html.IHeaderResponse;
import org.streets.eis.component.WebResources;
import org.streets.eis.component.model.DataModel;
import org.streets.eis.component.model.MenuItem;

public class IconToolBar extends MenuPanel {

    private static final long serialVersionUID = 1L;

    public IconToolBar(String id, DataModel<MenuItem> model) {
        super(id, model);
    }

    @Override
    protected void init() {
        for (DataModel<MenuItem> child : getMenuModel().getChildren()) {
            MenuItem item = child.getObject();
            item.setHint(item.getDisplayText() + "\r\n" + item.getHint());
            item.setDisplayText("&nbsp;");
        }
        super.init();
    }

    @Override
    protected void renderReference(IHeaderResponse response) {
        response.renderCSSReference(WebResources.Streets.Toolbar.ICONTOOLBAR_CSS);
    }

    @Override
    protected String getTheme() {
        return "icon-toolbar";
    }
}
