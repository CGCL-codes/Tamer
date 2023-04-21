package org.monet.reportgenerator.client.base.presenter;

import org.monet.reportgenerator.client.base.control.Token;
import com.extjs.gxt.ui.client.widget.LayoutContainer;

public interface Presenter<T extends Presenter.Display> {

    public interface Display {

        LayoutContainer asWidget();
    }

    void bind();

    void unbind();

    void refreshDisplay();

    void go(LayoutContainer container, Token token);

    void leave(LayoutContainer container);

    void getState(Token newStateToken);
}
