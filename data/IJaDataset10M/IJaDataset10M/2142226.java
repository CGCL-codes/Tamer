package edu.xtec.jclic.skins;

import edu.xtec.jclic.Constants;
import edu.xtec.jclic.PlayStation;
import edu.xtec.util.*;
import edu.xtec.util.StreamIO;
import java.awt.Component;
import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.event.*;
import javax.swing.text.html.HTMLDocument;
import java.util.Vector;
import edu.xtec.util.ResourceBridge;
import edu.xtec.util.StrUtils;
import java.awt.Rectangle;

/**
 *
 * @author Francesc Busquets (fbusquets@xtec.net)
 * @version 1.0
 */
public class AboutWindow extends edu.xtec.util.ExtendedJDialog implements HyperlinkListener {

    protected ResourceBridge rb;

    protected JTabbedPane tPane;

    public AboutWindow(Component parent, ResourceBridge rb, Dimension setSize) {
        super(parent, rb.getMsg("about_window_caption"), true);
        this.rb = rb;
        JPanel mainPane = new JPanel();
        mainPane.setLayout(new BorderLayout());
        mainPane.setPreferredSize(setSize);
        tPane = new JTabbedPane();
        mainPane.add(tPane, BorderLayout.CENTER);
        JPanel btPanel = new JPanel();
        btPanel.setLayout(new java.awt.GridLayout(1, 2));
        JButton btClose = new JButton(rb.getMsg("about_window_close_button"), ResourceManager.getImageIcon("icons/exit_small.gif"));
        btClose.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent e) {
                setVisible(false);
            }
        });
        btPanel.add(btClose);
        mainPane.add(btPanel, BorderLayout.SOUTH);
        getContentPane().add(mainPane);
        pack();
        centerOver(parent);
    }

    public JTabbedPane getTabbedPane() {
        return tPane;
    }

    public void addUrlDocumentTab(java.net.URL url, String tabTitle, Icon icon) {
        try {
            final JEditorPane ep = new JEditorPane(url);
            ep.setEditable(false);
            ep.addHyperlinkListener(this);
            tPane.addTab(tabTitle, icon, new JScrollPane(ep));
            javax.swing.SwingUtilities.invokeLater(new Runnable() {

                public void run() {
                    ep.scrollRectToVisible(new Rectangle(0, 0));
                }
            });
        } catch (java.io.IOException ex) {
            System.err.println("Error building about window:\n" + ex);
            tPane.addTab(tabTitle, new JLabel("ERROR"));
        }
    }

    public void addStrDocumentTab(String src, String tabTitle, java.net.URL base, Icon icon) {
        final JEditorPane ep = new JEditorPane();
        ep.setEditorKit(ep.getEditorKitForContentType("text/html"));
        if (base == null) base = getAboutUrlBase();
        if (base != null) ((HTMLDocument) ep.getDocument()).setBase(base);
        ep.setText(src);
        ep.setEditable(false);
        ep.addHyperlinkListener(this);
        tPane.addTab(tabTitle, icon, new JScrollPane(ep));
        javax.swing.SwingUtilities.invokeLater(new Runnable() {

            public void run() {
                ep.scrollRectToVisible(new Rectangle(0, 0));
            }
        });
    }

    public void hyperlinkUpdate(HyperlinkEvent e) {
        if (e.getEventType() == HyperlinkEvent.EventType.ACTIVATED) {
            final String sUrl = e.getURL().toString();
            SwingUtilities.invokeLater(new Runnable() {

                public void run() {
                    rb.displayUrl(sUrl, true);
                }
            });
        }
    }

    private static java.net.URL urlBase;

    public static java.net.URL getAboutUrlBase() {
        if (urlBase == null) {
            try {
                java.net.URL rootDocUrl = ResourceManager.getResource("about/about.html");
                String s = rootDocUrl.toString();
                urlBase = new java.net.URL(s.substring(0, s.lastIndexOf('/') + 1));
            } catch (Exception ex) {
                System.err.println("Resource missing: about.html");
            }
        }
        return urlBase;
    }

    public void buildAboutTab(String appName, String appVersion, String logoIcon, String creditsDoc, String otherCreditsDoc, String licenseDoc, String sponsorsDoc) throws Exception {
        String doc = ResourceManager.getResourceText("about/about.html", false);
        if (appName == null) appName = "JClic";
        if (logoIcon == null) logoIcon = "SANTi mini Nino.png";
        if (creditsDoc == null) creditsDoc = "about/credits.html";
        String credits = ResourceManager.getResourceText(creditsDoc, false);
        if (otherCreditsDoc == null) otherCreditsDoc = rb.getMsg("about_otherCreditsDoc");
        String otherCredits = ResourceManager.getResourceText(otherCreditsDoc, false);
        String license = rb.getMsg("html_gpl_license");
        if (licenseDoc != null) license = ResourceManager.getResourceText(licenseDoc, false);
        if (sponsorsDoc == null) sponsorsDoc = rb.getMsg("about_sponsorsDoc");
        String sponsors = ResourceManager.getResourceText(sponsorsDoc, false);
        java.net.URL iconUrl = ResourceManager.getResource("about/" + logoIcon);
        doc = StrUtils.replace(doc, "%LOGO", iconUrl.toExternalForm());
        StringBuffer sb = new StringBuffer(appName);
        if (appVersion != null) sb.append(" ").append(rb.getMsg("VERSION")).append(" ").append(appVersion);
        doc = StrUtils.replace(doc, "%APPVERSION", sb.substring(0));
        doc = StrUtils.replace(doc, "%CREDITS", credits);
        sb.setLength(0);
        sb.append(rb.getMsg("about_otherCreditsCaption")).append("<BR>&nbsp;<BR>").append(otherCredits);
        doc = StrUtils.replace(doc, "%OTHERCREDITS", sb.substring(0));
        doc = StrUtils.replace(doc, "%LICENSE", license);
        sb.setLength(0);
        if (sponsors != null && sponsors.length() > 0) sb.append(rb.getMsg("about_sponsorsCaption")).append("<BR>").append(sponsors);
        doc = StrUtils.replace(doc, "%SPONSORS", sb.substring(0));
        addStrDocumentTab(doc, rb.getMsg("ABOUT"), getAboutUrlBase(), ResourceManager.getImageIcon("icons/about_small.gif"));
    }

    public void buildStandardTab(String htmlContent, String titleKey, String labelKey, String icon) {
        String htmlBgColor = rb.getMsg("about_window_html_bgcolor");
        String htmlStyle = rb.getMsg("about_window_html_style");
        String header = rb.getMsg("about_window_html_header");
        StringBuffer sb = new StringBuffer(4096);
        sb.append(header);
        if (titleKey != null) {
            sb.append("<br><b>").append(rb.getMsg(titleKey)).append("</b><hr>");
        }
        sb.append(htmlContent);
        String s = Html.table(sb.substring(0), "100%", 0, 5, -1, htmlStyle, true);
        addStrDocumentTab(Html.getHtmlDoc(s, htmlBgColor), rb.getMsg(labelKey), getAboutUrlBase(), ResourceManager.getImageIcon(icon));
    }

    public String getHtmlSystemInfo() {
        Messages msg = rb.getOptions().getMessages();
        Html html = new Html(3000);
        html.doubleCell(msg.get("about_window_lb_os"), true, System.getProperty("os.name") + " " + System.getProperty("os.version") + " - " + System.getProperty("os.arch"), false);
        html.doubleCell(msg.get("about_window_lb_java_version"), true, System.getProperty("java.version"), false);
        html.doubleCell(msg.get("about_window_lb_java_vm"), true, System.getProperty("java.vm.name") + " " + System.getProperty("java.vm.version") + "\n" + System.getProperty("java.vm.vendor"), false);
        html.doubleCell(msg.get("about_window_lb_java_home"), true, System.getProperty("java.home"), false);
        html.doubleCell(msg.get("about_window_lb_free_mem"), true, msg.kValue(Runtime.getRuntime().freeMemory()), false);
        html.doubleCell(msg.get("about_window_lb_total_mem"), true, msg.kValue(Runtime.getRuntime().totalMemory()), false);
        Object o = rb.getOptions().get(Constants.MEDIA_SYSTEM);
        String s = Constants.JMF.equals(o) ? "Java Media Framework" : Constants.QT61.equals(o) ? "QuickTime for Java 6.1+" : "-";
        html.doubleCell(msg.get("about_window_lb_mediaSystem"), true, s, false);
        if (rb.getOptions().getBoolean(Constants.TRACE)) {
            java.util.Iterator it = System.getProperties().keySet().iterator();
            while (it.hasNext()) {
                String k = (String) it.next();
                html.doubleCell(k, true, System.getProperty(k), false);
            }
        }
        return Html.table(html.toString(), null, 0, 5, -1, null, false);
    }
}
