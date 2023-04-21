package org.vexi.launcher;

import java.awt.Color;
import java.io.InputStream;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;
import org.vexi.launcher.vexi_org.ModuleVersion;
import org.vexi.security.CryptoUtil;

public class VexiLauncher extends Launcher {

    public Color getTextColor() {
        return Color.white;
    }

    public Color getBorderColor() {
        return Color.lightGray;
    }

    public Color getBarColor() {
        return Color.white;
    }

    public URL getSplashImageResource() {
        return getClass().getResource("vexi_splash.png");
    }

    public String getVersion() {
        return ModuleVersion.STRING;
    }

    public String[] getPermittedDomains() {
        return new String[] { "localhost", "127.0.0.1", "vexi.org", "vexi.sourceforge.net", "emanate-project.org" };
    }

    public Map getCerts() {
        try {
            Map organisationToPK = new HashMap();
            CryptoUtil.readCompactCADat(organisationToPK, getSignerCertsDat());
            return organisationToPK;
        } catch (Throwable e) {
            updateError("Error: applet unable to find signatory certificates");
            throw new Error(e);
        }
    }

    public InputStream getSignerCertsDat() {
        return getClass().getResourceAsStream("vexi_certs.dat");
    }
}
