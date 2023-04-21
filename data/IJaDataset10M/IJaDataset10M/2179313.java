package org.posper.subcore;

import javax.swing.JFrame;
import java.awt.Dimension;
import java.awt.Font;
import org.posper.graphics.JFlowPanel;
import org.posper.graphics.GraphicsResources;
import org.posper.graphics.SplashManager;
import org.posper.graphics.ScreenManager;
import org.posper.datautils.Formats;
import org.posper.datautils.AppLocal;
import org.osgi.framework.BundleActivator;
import org.osgi.framework.BundleContext;
import org.osgi.framework.Bundle;
import org.osgi.framework.ServiceReference;
import org.apache.felix.ipojo.IPojoContext;
import java.net.URL;
import java.net.MalformedURLException;
import java.awt.Toolkit;
import java.util.ResourceBundle;
import java.util.Dictionary;
import java.util.Hashtable;
import java.awt.SplashScreen;
import org.osgi.service.cm.ManagedService;

/**
* Cut down to absolute pre-minimalist size, chances are the version of this
* you're seeing doesn't yet have enough to be useful, but I'm working my
* way there :)
 */
public class JFrmTPV extends JFrame implements BundleActivator, ManagedService {

    private static final long serialVersionUID = 8295741168592338342L;

    private String configuration_pid = "org.posper.subcore";

    private boolean startNeeded;

    private BundleContext bundleContext;

    private void createAppConfigIfNeeded(Dictionary props) {
        SubcoreConfig appConfig = SubcoreConfig.getInstance();
        if (appConfig == null) {
            SubcoreConfig.getInstance(props, configuration_pid, bundleContext);
        }
    }

    public void updated(Dictionary props) {
        createAppConfigIfNeeded(props);
        SubcoreConfig.getInstance().updateProps(props);
        if (startNeeded) {
            startNeeded = false;
            startTPV();
        }
    }

    public void stop(BundleContext context) {
        System.out.println("JFrmTPV bundle stop is happening");
    }

    public void start(BundleContext context) throws MalformedURLException {
        bundleContext = context;
        System.out.println("JFrmTPV bundle start is happening");
        startNeeded = true;
        AppLocal appLocal = AppLocal.getInstance();
        ResourceBundle messages = ResourceBundle.getBundle("org/posper/subcore/messages");
        appLocal.addResourceBundle(messages);
        GraphicsResources graphRes = GraphicsResources.getInstance();
        try {
            graphRes.addURL(JFrmTPV.class.getResource("/org/posper/subcore/images/display.png"));
            graphRes.addURL(JFrmTPV.class.getResource("/org/posper/subcore/images/exit.png"));
            graphRes.addURL(JFrmTPV.class.getResource("/org/posper/subcore/images/splash.png"));
        } catch (Exception e) {
            System.out.println("Oops, exception: " + e);
        }
        Dictionary properties = new Hashtable();
        properties.put("service.pid", configuration_pid);
        context.registerService(ManagedService.class.getName(), this, properties);
    }

    public JFrmTPV() {
    }

    /** Arrancamos la aplicacion de TPV
     * @return true if successfully started
     */
    public boolean startTPV() {
        GraphicsResources graphRes = GraphicsResources.getInstance();
        AppLocal appLocal = AppLocal.getInstance();
        String[] messages = new String[] { appLocal.getIntString("message.welcome"), appLocal.getIntString("splash.started") };
        Font fontToUse = new Font("Dialog", Font.ITALIC, 24);
        SplashManager splash = SplashManager.getInstance(SplashScreen.getSplashScreen(), messages, 150, 330, fontToUse, graphRes.getURL("/org/posper/subcore/images/splash.png"));
        try {
            splash.renderNextFrame();
            Thread.sleep(4000);
            splash.renderNextFrame();
            Thread.sleep(4000);
            splash.renderNextFrame();
            Thread.sleep(4000);
        } catch (Exception e) {
            System.out.println("Exception while trying to sleep and render splash stuff: " + e);
        }
        initComponents();
        jScrollPane1.getVerticalScrollBar().setPreferredSize(new Dimension(35, 35));
        m_jHostPanel.setVisible(false);
        String screenMode = (String) SubcoreConfig.getInstance().getValue("machine.screenmode");
        if ((screenMode != null) && (screenMode.equals("fullscreen"))) {
            ScreenManager.changeDisplayMode(this, true, 1024, 768);
        } else {
            ScreenManager.changeDisplayMode(this, false, 1024, 768);
        }
        setVisible(true);
        return true;
    }

    private void initComponents() {
        m_jPanelTitle = new javax.swing.JPanel();
        m_jLblTitle = new javax.swing.JLabel();
        m_jPanelDown = new javax.swing.JPanel();
        m_jHostPanel = new javax.swing.JPanel();
        m_jHost = new javax.swing.JLabel();
        jMenuButtonPanel = new javax.swing.JPanel();
        m_jMenuButton = new javax.swing.JButton();
        m_jPanelContainer = new javax.swing.JPanel();
        m_jPanelLogin = new javax.swing.JPanel();
        jLabelMain = new javax.swing.JLabel();
        jPanel5 = new javax.swing.JPanel();
        jScrollPane1 = new javax.swing.JScrollPane();
        m_jLogonName = new javax.swing.JPanel();
        jButton1 = new javax.swing.JButton();
        m_jClose = new javax.swing.JButton();
        GraphicsResources graphRes = GraphicsResources.getInstance();
        AppLocal appLocal = AppLocal.getInstance();
        setDefaultCloseOperation(javax.swing.WindowConstants.DO_NOTHING_ON_CLOSE);
        setTitle("This is the big title");
        addWindowListener(new java.awt.event.WindowAdapter() {

            public void windowClosed(java.awt.event.WindowEvent evt) {
                formWindowClosed(evt);
            }

            public void windowClosing(java.awt.event.WindowEvent evt) {
                formWindowClosing(evt);
            }
        });
        m_jPanelTitle.setBackground(javax.swing.UIManager.getDefaults().getColor("InternalFrame.activeTitleBackground"));
        m_jLblTitle.setForeground(javax.swing.UIManager.getDefaults().getColor("InternalFrame.activeTitleForeground"));
        m_jLblTitle.setText("This is the label title");
        m_jPanelTitle.add(m_jLblTitle);
        getContentPane().add(m_jPanelTitle, java.awt.BorderLayout.NORTH);
        m_jPanelDown.setFocusTraversalPolicyProvider(true);
        m_jPanelDown.setMinimumSize(new java.awt.Dimension(205, 45));
        m_jPanelDown.setPreferredSize(new java.awt.Dimension(241, 55));
        m_jPanelDown.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.LEFT, 3, 2));
        m_jHostPanel.setLayout(new java.awt.BorderLayout());
        m_jHost.setIcon(new javax.swing.ImageIcon(graphRes.getURL("/org/posper/subcore/images/display.png")));
        m_jHost.setText("Here's looking at you");
        m_jHostPanel.add(m_jHost, java.awt.BorderLayout.CENTER);
        m_jPanelDown.add(m_jHostPanel);
        jMenuButtonPanel.setBorder(javax.swing.BorderFactory.createEmptyBorder(1, 10, 1, 20));
        jMenuButtonPanel.setPreferredSize(new java.awt.Dimension(150, 45));
        jMenuButtonPanel.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.CENTER, 5, 2));
        m_jMenuButton.setFont(new java.awt.Font("Tahoma", 1, 14));
        m_jMenuButton.setText("menbut");
        m_jMenuButton.setAlignmentX(0.5F);
        m_jMenuButton.setMaximumSize(new java.awt.Dimension(90, 42));
        m_jMenuButton.setMinimumSize(new java.awt.Dimension(64, 32));
        m_jMenuButton.setPreferredSize(new java.awt.Dimension(90, 40));
        jMenuButtonPanel.add(m_jMenuButton);
        m_jPanelDown.add(jMenuButtonPanel);
        getContentPane().add(m_jPanelDown, java.awt.BorderLayout.SOUTH);
        m_jPanelContainer.setLayout(new java.awt.CardLayout());
        m_jPanelLogin.setLayout(new java.awt.BorderLayout());
        jLabelMain.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabelMain.setBorder(javax.swing.BorderFactory.createEmptyBorder(10, 100, 10, 100));
        jLabelMain.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        jLabelMain.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        m_jPanelLogin.add(jLabelMain, java.awt.BorderLayout.CENTER);
        jPanel5.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.CENTER, 0, 0));
        m_jPanelLogin.add(jPanel5, java.awt.BorderLayout.WEST);
        jScrollPane1.setHorizontalScrollBarPolicy(javax.swing.ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
        jScrollPane1.setVerticalScrollBarPolicy(javax.swing.ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
        jScrollPane1.setPreferredSize(new java.awt.Dimension(200, 600));
        m_jPanelLogin.add(jScrollPane1, java.awt.BorderLayout.WEST);
        m_jLogonName.setBorder(javax.swing.BorderFactory.createEmptyBorder(5, 5, 5, 5));
        m_jLogonName.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.RIGHT));
        jButton1.setText(appLocal.getIntString("button.about"));
        jButton1.setMaximumSize(new java.awt.Dimension(84, 30));
        jButton1.setMinimumSize(new java.awt.Dimension(84, 30));
        jButton1.setPreferredSize(new java.awt.Dimension(115, 35));
        m_jLogonName.add(jButton1);
        m_jClose.setText(appLocal.getIntString("button.close"));
        m_jClose.setIcon(new javax.swing.ImageIcon(graphRes.getURL("/org/posper/subcore/images/exit.png")));
        m_jClose.setFocusPainted(false);
        m_jClose.setFocusable(false);
        m_jClose.setPreferredSize(new java.awt.Dimension(115, 35));
        m_jClose.setRequestFocusEnabled(false);
        m_jClose.addActionListener(new java.awt.event.ActionListener() {

            public void actionPerformed(java.awt.event.ActionEvent evt) {
                m_jCloseActionPerformed(evt);
            }
        });
        m_jLogonName.add(m_jClose);
        m_jPanelLogin.add(m_jLogonName, java.awt.BorderLayout.PAGE_START);
        m_jPanelContainer.add(m_jPanelLogin, "login");
        getContentPane().add(m_jPanelContainer, java.awt.BorderLayout.CENTER);
    }

    private void formWindowClosed(java.awt.event.WindowEvent evt) {
        System.exit(0);
    }

    private void formWindowClosing(java.awt.event.WindowEvent evt) {
        dispose();
    }

    private void m_jCloseActionPerformed(java.awt.event.ActionEvent evt) {
        dispose();
    }

    private javax.swing.JButton jButton1;

    private javax.swing.JLabel jLabelMain;

    private javax.swing.JPanel jMenuButtonPanel;

    private javax.swing.JPanel jPanel5;

    private javax.swing.JScrollPane jScrollPane1;

    private javax.swing.JButton m_jClose;

    private javax.swing.JLabel m_jHost;

    private javax.swing.JPanel m_jHostPanel;

    private javax.swing.JLabel m_jLblTitle;

    private javax.swing.JPanel m_jLogonName;

    private javax.swing.JButton m_jMenuButton;

    private javax.swing.JPanel m_jPanelContainer;

    private javax.swing.JPanel m_jPanelDown;

    private javax.swing.JPanel m_jPanelLogin;

    private javax.swing.JPanel m_jPanelTitle;
}
