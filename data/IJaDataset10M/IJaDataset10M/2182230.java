package org.fb4j.desktop;

import java.util.ArrayList;
import java.util.EventObject;
import java.util.List;
import org.eclipse.swt.SWT;
import org.eclipse.swt.browser.Browser;
import org.eclipse.swt.browser.LocationEvent;
import org.eclipse.swt.browser.LocationListener;
import org.eclipse.swt.browser.ProgressEvent;
import org.eclipse.swt.browser.ProgressListener;
import org.eclipse.swt.browser.StatusTextEvent;
import org.eclipse.swt.browser.StatusTextListener;
import org.eclipse.swt.events.DisposeEvent;
import org.eclipse.swt.events.DisposeListener;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.ProgressBar;
import org.fb4j.client.AuthenticationToken;

/**
 * @author Gino Miceli
 */
public class LoginBrowserComponent extends Composite {

    private static final String DEFAULT_LOGIN_SUCCESS_URL = "https://ssl.facebook.com/desktopapp.php";

    private List<LoginListener> loginListeners;

    private Browser browserComponent;

    private Label statusLabel;

    private ProgressBar progressBar;

    private String loginSuccessUrl;

    private boolean loginSucceeded;

    public LoginBrowserComponent(Composite parent) {
        super(parent, SWT.NONE);
        GridLayout gridLayout = new GridLayout();
        gridLayout.numColumns = 3;
        setLayout(gridLayout);
        loginListeners = new ArrayList<LoginListener>();
        loginSuccessUrl = DEFAULT_LOGIN_SUCCESS_URL;
        browserComponent = createBrowserComponent(this);
        statusLabel = createStatusLabel(this);
        progressBar = createProgressBar(this);
        browserComponent.addProgressListener(new LoginProgressListener());
        browserComponent.addDisposeListener(new LoginDisposeListener());
        browserComponent.addStatusTextListener(new LoginStatusTextListener());
        browserComponent.addLocationListener(new LoginLocationListener());
    }

    public void addLoginListener(LoginListener loginListener) {
        loginListeners.add(loginListener);
    }

    public String getLoginSuccessUrl() {
        return loginSuccessUrl;
    }

    public void setLoginSuccessUrl(String loginSuccessUrl) {
        this.loginSuccessUrl = loginSuccessUrl;
    }

    public void showForm(AuthenticationToken token) {
        String loginUrl = token.getLoginUrl() + "&popup=1";
        loginSucceeded = false;
        browserComponent.setUrl(loginUrl);
    }

    public boolean isLoginSucceeded() {
        return loginSucceeded;
    }

    private static Browser createBrowserComponent(Composite parent) {
        Browser browser = new Browser(parent, SWT.NONE);
        GridData data = new GridData();
        data = new GridData();
        data.horizontalAlignment = GridData.FILL;
        data.verticalAlignment = GridData.FILL;
        data.horizontalSpan = 3;
        data.grabExcessHorizontalSpace = true;
        data.grabExcessVerticalSpace = true;
        browser.setLayoutData(data);
        return browser;
    }

    private static Label createStatusLabel(Composite parent) {
        Label status = new Label(parent, SWT.NONE);
        GridData data = new GridData(GridData.FILL_HORIZONTAL);
        data.horizontalSpan = 2;
        status.setLayoutData(data);
        return status;
    }

    private static ProgressBar createProgressBar(Composite parent) {
        ProgressBar progressBar = new ProgressBar(parent, SWT.NONE);
        GridData data = new GridData();
        data.horizontalAlignment = GridData.END;
        progressBar.setLayoutData(data);
        return progressBar;
    }

    private class LoginProgressListener implements ProgressListener {

        public void changed(ProgressEvent event) {
            if (event.total == 0) return;
            int ratio = event.current * 100 / event.total;
            progressBar.setSelection(ratio);
        }

        public void completed(ProgressEvent event) {
            progressBar.setSelection(0);
        }
    }

    private class LoginStatusTextListener implements StatusTextListener {

        public void changed(StatusTextEvent event) {
            statusLabel.setText(event.text);
        }
    }

    private class LoginLocationListener implements LocationListener {

        public void changed(LocationEvent event) {
            String url = trimQueryString(event.location);
            if (url.equals(loginSuccessUrl)) {
                onLoginSuccess();
            }
        }

        /** Remove query string from URL */
        private String trimQueryString(String location) {
            int idx = location.indexOf("?");
            return idx >= 0 ? location.substring(0, idx) : location;
        }

        public void changing(LocationEvent event) {
        }
    }

    private class LoginDisposeListener implements DisposeListener {

        public void widgetDisposed(DisposeEvent event) {
            if (!loginSucceeded) {
                onLoginAborted();
            }
        }
    }

    private void onLoginSuccess() {
        loginSucceeded = true;
        for (LoginListener loginListener : loginListeners) {
            loginListener.loginSucceeded(new EventObject(this));
        }
    }

    private void onLoginAborted() {
        for (LoginListener loginListener : loginListeners) {
            loginListener.loginAborted(new EventObject(this));
        }
    }
}
