package co.fxl.gui.gwt;

import co.fxl.gui.api.IHyperlink;
import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.ui.HTML;

class GWTHyperlink extends GWTElement<HTML, IHyperlink> implements IHyperlink {

    private String text;

    private String uRI;

    GWTHyperlink(GWTContainer<HTML> container) {
        super(container);
    }

    @Override
    public IHyperlink text(String text) {
        this.text = text;
        update();
        return this;
    }

    @Override
    public IHyperlink uRI(String uRI) {
        this.uRI = uRI;
        update();
        return this;
    }

    @Override
    public IHyperlink localURI(String uRI) {
        return uRI(GWT.getModuleBaseURL() + uRI);
    }

    private void update() {
        if (text == null || uRI == null) return;
        container.widget.setHTML("<span style=\"font-size: 12px\"><a href=\"" + uRI + "\">" + text + "</a></span>");
    }

    @Override
    GWTClickHandler<IHyperlink> newGWTClickHandler(IClickListener clickListener) {
        return new GWTClickHandler<IHyperlink>(this, clickListener, false);
    }
}
