package ua.org.hatu.daos.gwt.client.view;

import java.util.logging.Level;
import java.util.logging.Logger;
import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiTemplate;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.Widget;
import ua.org.hatu.daos.gwt.client.DaosMVP;
import ua.org.hatu.daos.gwt.client.presenters.BasicHeaderPresenter;
import ua.org.hatu.daos.gwt.client.ui.LogoutPanel;

/**
 * @author zeus (alex.pogrebnyuk@gmail.com)
 * @author dmytro (pogrebniuk@gmail.com)
 *
 */
public class LogoutViewImpl extends Composite implements LogOutView {

    private static final Logger logger = Logger.getLogger(DaosMVP.class.getName());

    private BasicHeaderPresenter presenter;

    private static BasicHeaderViewUiBinder uiBinder = GWT.create(BasicHeaderViewUiBinder.class);

    @UiTemplate("LogOutView.ui.xml")
    interface BasicHeaderViewUiBinder extends UiBinder<Widget, LogoutViewImpl> {
    }

    @UiField
    LogoutPanel logoutPanel;

    public LogoutViewImpl() {
        getLogger().log(Level.FINE, "Initialize [BasicHeaderView] view");
        initWidget(uiBinder.createAndBindUi(this));
        logoutPanel.getLogoutAnchor().addClickHandler(new ClickHandler() {

            @Override
            public void onClick(ClickEvent event) {
                getLogger().log(Level.FINER, "Logout anchor was clicked");
                if (presenter != null) {
                    presenter.onLogoutAnchorClicked();
                }
            }
        });
    }

    public void setPresenter(BasicHeaderPresenter presenter) {
        this.presenter = presenter;
    }

    public LogoutPanel getLogoutPanel() {
        return logoutPanel;
    }

    Logger getLogger() {
        return logger;
    }
}
