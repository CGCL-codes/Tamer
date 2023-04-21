package co.edu.unal.ungrid.client.controller;

import java.applet.AppletContext;
import java.net.MalformedURLException;
import java.net.URL;
import javax.swing.JApplet;

public class Applet extends JApplet {

    private static final long serialVersionUID = 7166759386917570183L;

    public Applet() {
        m_app = App.getInstance();
        m_app.setApplet(this);
    }

    @Override
    public void init() {
        m_sHomeDir = getParameter(HOME_DIR);
        if (m_sHomeDir == null) {
            m_sHomeDir = "market";
        }
        m_sHomePge = getParameter(HOME_PGE);
        if (m_sHomePge == null) {
            m_sHomePge = "index.html";
        }
    }

    @Override
    public void start() {
        m_app.start();
    }

    @Override
    public void stop() {
        m_app.stop();
    }

    public boolean isApplet() {
        boolean b = true;
        try {
            getAppletContext();
        } catch (Exception exc) {
            b = false;
        }
        return b;
    }

    protected void showPage(String sPage, String sWhere) {
        if (isApplet()) {
            AppletContext ac = getAppletContext();
            if (ac != null) {
                URL url = getDocumentBase();
                String sProtocol = url.getProtocol();
                String sAuthority = url.getAuthority();
                String sUrl = (sAuthority.length() == 0 ? App.DBG_URL : sProtocol + "://" + sAuthority);
                String sHome = sUrl + "/" + sPage;
                try {
                    ac.showDocument(new URL(sHome), sWhere);
                } catch (MalformedURLException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    public void exit() {
        showPage(m_sHomeDir + "/" + m_sHomePge, "_self");
    }

    private App m_app;

    protected String m_sHomeDir;

    protected String m_sHomePge;

    public static final String HOME_DIR = "home-dir";

    public static final String HOME_PGE = "home-pge";
}
