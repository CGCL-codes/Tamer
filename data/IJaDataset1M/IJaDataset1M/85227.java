package org.monet.reportgenerator.client.presenter;

import org.monet.reportgenerator.client.base.presenter.ContainerPresenter;
import org.monet.reportgenerator.client.base.presenter.Presenter;
import org.monet.reportgenerator.shared.model.UserCredentials;
import com.google.gwt.event.dom.client.HasAllKeyHandlers;
import com.google.gwt.event.dom.client.HasClickHandlers;

public interface DesktopPresenter extends Presenter<DesktopPresenter.Display> {

    public interface Display extends ContainerPresenter.Display {

        HasClickHandlers getPreferencesHandler();

        HasClickHandlers getLoginHandler();

        UserCredentials getCredentials();

        HasClickHandlers getLogoutHandler();

        HasAllKeyHandlers getPanelHandler();

        void logout();

        void showError();

        void hideError();

        void hideSignDialog();

        void hideBody();

        void showBody();

        void showWaitMassage();

        void hideWaitMassage();

        void setUrlLogos(String urlLogo, String urlLoad);
    }

    void refreshDisplay();
}
