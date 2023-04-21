package ch.jester.socialmedia.facebook.internal.authflow;

import org.eclipse.swt.SWT;
import org.eclipse.swt.browser.Browser;
import org.eclipse.swt.browser.StatusTextEvent;
import org.eclipse.swt.browser.StatusTextListener;
import org.eclipse.swt.widgets.Dialog;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.layout.GridData;
import org.eclipse.wb.swt.ResourceManager;
import ch.jester.socialmedia.facebook.internal.Activator;

/**
 * Browserbasierte Flow Dialog (User Einloggen)
 *
 */
public class FaceBookAuthorisationFlowDialog extends Dialog {

    protected StatusTextListener listener;

    protected String result;

    protected Shell shlFacebookAuthFlow;

    Browser browser;

    private String url;

    protected String code;

    private boolean mClearSession;

    /**
	 * Create the dialog.
	 * @param parent
	 * @param style
	 */
    public FaceBookAuthorisationFlowDialog(Shell parent, int style) {
        super(parent, style);
        setText("SWT Dialog");
    }

    public void setURL(String pURL) {
        url = pURL;
    }

    /**
	 * Open the dialog.
	 * @return the result
	 */
    public String open() {
        createContents();
        shlFacebookAuthFlow.open();
        shlFacebookAuthFlow.layout();
        Display display = getParent().getDisplay();
        while (!shlFacebookAuthFlow.isDisposed()) {
            if (!display.readAndDispatch()) {
                display.sleep();
            }
        }
        return result;
    }

    /**
	 * Create contents of the dialog.
	 */
    private void createContents() {
        shlFacebookAuthFlow = new Shell(getParent(), SWT.DIALOG_TRIM | SWT.RESIZE);
        shlFacebookAuthFlow.setImage(ResourceManager.getPluginImage("ch.jester.socialmedia.facebook", "icons/FaceBook-icon.png"));
        shlFacebookAuthFlow.setSize(653, 394);
        shlFacebookAuthFlow.setText("Facebook Auth Flow: Fetching Authorisation");
        shlFacebookAuthFlow.setLayout(new GridLayout(1, false));
        browser = new Browser(shlFacebookAuthFlow, SWT.NONE);
        browser.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 1, 1));
        if (mClearSession) {
            Browser.clearSessions();
        }
        browser.setUrl(url);
        browser.addStatusTextListener(listener = new StatusTextListener() {

            @Override
            public void changed(StatusTextEvent event) {
                if (browser.getUrl().contains("access_token=")) {
                    Activator.getDefault().getActivationContext().getLogger().info("url: " + browser.getUrl());
                    String start = "access_token=";
                    code = browser.getUrl().substring(browser.getUrl().indexOf(start) + start.length());
                    Activator.getDefault().getActivationContext().getLogger().info("access code: " + code);
                    browser.removeStatusTextListener(listener);
                    shlFacebookAuthFlow.dispose();
                }
            }
        });
    }

    public String getToken() {
        return code;
    }

    public void clearSession(boolean pClearSession) {
        mClearSession = pClearSession;
    }
}
