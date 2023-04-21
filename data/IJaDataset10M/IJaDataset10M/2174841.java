package com.gft.larozanam.client.componentes;

import com.google.gwt.safehtml.shared.SafeHtml;
import com.google.gwt.user.client.ui.DialogBox;

public class CWindow extends DialogBox {

    private static final String CLOSE_BUTTON = "<input type=\"button\" class=\"dialogClose\" onclick=\"window.close_cwindow();\" value=\"X\"/>";

    public CWindow() {
        super(false, true);
        setStyleName("cwindow");
        setGlassEnabled(true);
        setGlassStyleName("cwindowglass");
        super.setHTML(CLOSE_BUTTON);
        setAutoHideEnabled(true);
        setSize("400px", "300px");
        setText("Clique no bot&atilde;o ao lado ou fora desse painel para fech&aacute;-lo");
    }

    @Override
    public void setText(String text) {
        setHTML(text);
    }

    public void showCenter() {
        center();
        show();
    }

    @Override
    public void setHTML(SafeHtml html) {
        setHTML(html.asString());
    }

    @Override
    public void setHTML(String html) {
        super.setHTML(html + CLOSE_BUTTON);
    }

    @Override
    public void show() {
        exportCloseMethod(this);
        super.show();
    }

    @Override
    public void hide() {
        this.clear();
        super.hide();
    }

    private native void exportCloseMethod(CWindow me);
}
