package osdep.swing;

import osdep.console.ConsoleProxyAuthenticationSource;
import osdep.proxy.IProxyAuthenticationSource;

/**
 * Uses ProxyUserIdPasswordPanel to authenticate the user against the proxy
 * @author SHZ Jun 17, 2010
 */
public class SwingProxyAuthenticationSource implements IProxyAuthenticationSource {

    /**
	 * The stack panel to which we need to add the StatusPanel
	 */
    private CardPanel parent;

    /**
	 * The panel with the 'Next' button
	 */
    private NextPanel pnlNext;

    /**
	 * The real panel that gets userid and password of the user to authenticate to the proxy
	 */
    private ProxyUserIdPasswordPanel pnlProxyUserIdPassword;

    /**
	 * @param pnlTop
	 * @param pnlNext
	 */
    public SwingProxyAuthenticationSource(CardPanel parent, NextPanel pnlNext) {
        this.parent = parent;
        this.pnlNext = pnlNext;
    }

    public String getProxyPassword() {
        return pnlProxyUserIdPassword.getProxyUserId();
    }

    public String getProxyUserId() {
        return pnlProxyUserIdPassword.getProxyPassword();
    }

    /**
	 * Shows the YesNoPanel to see if the proxy needs authentication.
	 * In this case shows the ProxyUserIdPasswordPanel to get userid and password
	 */
    public boolean needsProxyAuthentication() {
        YesNoPanel pnlTop = new YesNoPanel(ConsoleProxyAuthenticationSource.NEED_AUTHENTICATION);
        parent.add(pnlTop, "yesNo");
        pnlNext.waitUntilNextHasBeenPressed();
        boolean isYes = pnlTop.isYes();
        if (isYes) {
            pnlProxyUserIdPassword = new ProxyUserIdPasswordPanel();
            parent.add(pnlProxyUserIdPassword, "proxyUserIdPassword");
            pnlNext.waitUntilNextHasBeenPressed();
        }
        return isYes;
    }
}
