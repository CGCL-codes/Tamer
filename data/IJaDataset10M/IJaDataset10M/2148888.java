package de.miethxml.gui.project.plugin;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import com.jgoodies.forms.layout.CellConstraints;
import com.jgoodies.forms.layout.FormLayout;
import de.miethxml.jetty.JettyController;
import de.miethxml.toolkit.conf.LocaleImpl;
import de.miethxml.toolkit.gui.LocaleLabel;
import de.miethxml.toolkit.gui.LocaleSeparator;
import de.miethxml.toolkit.ui.GradientLabel;
import de.miethxml.toolkit.ui.SmallShadowBorder;
import org.mortbay.util.Frame;
import org.mortbay.util.Log;
import org.mortbay.util.LogSink;

/**
 * @author <a href="mailto:simon.mieth@gmx.de">Simon Mieth</a>
 *
 *
 *
 */
public class ServletViewBuilder implements ActionListener, LogSink {

    private JButton button;

    private JTextArea log;

    private JLabel info;

    private JettyController jettyController;

    private JTextField port;

    private JTextField sessionMax;

    private JTextField webappPath;

    private String webappLocation = "";

    private JPanel panel;

    private LocaleLabel maininfo;

    /**
     *
     */
    public ServletViewBuilder() {
        super();
    }

    public void init() {
    }

    public void initNewView() {
        panel = new JPanel();
        FormLayout layout = new FormLayout("3dlu,right:pref,2dlu,pref,9dlu,fill:pref:grow,3dlu", "0dlu,p,9dlu,p,3dlu,p,2dlu,p,2dlu,p,3dlu,p,3dlu,fill:10dlu:grow,3dlu,p,fill:3dlu:grow");
        panel.setLayout(layout);
        panel.setBorder(new SmallShadowBorder());
        CellConstraints cc = new CellConstraints();
        panel.add(new GradientLabel("Jetty"), cc.xywh(1, 2, 7, 1));
        panel.add(new LocaleSeparator("jetty.view.panel.separator.settings"), cc.xywh(2, 4, 5, 1));
        panel.add(new LocaleLabel("jetty.view.panel.label.port"), cc.xy(2, 6));
        port = new JTextField(6);
        port.setText("" + jettyController.getPort());
        panel.add(port, cc.xy(4, 6));
        panel.add(new LocaleLabel("jetty.view.panel.label.sessioninterval"), cc.xy(2, 8));
        sessionMax = new JTextField(6);
        sessionMax.setText("" + jettyController.getSessionMaxInterval());
        panel.add(sessionMax, cc.xy(4, 8));
        panel.add(new LocaleLabel("jetty.view.panel.label.webapplocation"), cc.xy(2, 10));
        webappPath = new JTextField(30);
        webappPath.setText("");
        panel.add(webappPath, cc.xywh(4, 10, 3, 1, CellConstraints.DEFAULT, CellConstraints.CENTER));
        panel.add(new LocaleSeparator("jetty.view.panel.separator.log"), cc.xywh(2, 12, 5, 1));
        log = new JTextArea(4, 10);
        JScrollPane sp = new JScrollPane(log);
        log.setEditable(false);
        panel.add(sp, cc.xywh(4, 14, 3, 1));
        button = new JButton(LocaleImpl.getInstance().getString("jetty.view.panel.button.start"));
        button.setActionCommand("start");
        button.addActionListener(this);
        panel.add(button, cc.xy(4, 16));
        info = new JLabel("");
        panel.add(info, cc.xy(6, 16));
    }

    public void actionPerformed(ActionEvent e) {
        if (e.getActionCommand().equals("start")) {
            Runnable r = new Runnable() {

                public void run() {
                    log.setText("");
                    button.setEnabled(false);
                    button.setActionCommand("stop");
                    info.setText(LocaleImpl.getInstance().getString("jetty.view.info.starting"));
                    jettyController.setWebappPath(webappPath.getText());
                    jettyController.setPort(Integer.parseInt(port.getText()));
                    jettyController.setSessionMaxInterval(Integer.parseInt(sessionMax.getText()));
                    jettyController.startJetty();
                    button.setEnabled(true);
                    button.setText(LocaleImpl.getInstance().getString("jetty.view.panel.button.stop"));
                    info.setText(LocaleImpl.getInstance().getString("jetty.view.info.running"));
                }
            };
            Thread.currentThread().setContextClassLoader(this.getClass().getClassLoader());
            Thread t = new Thread(r);
            t.start();
        } else if (e.getActionCommand().equals("stop")) {
            Runnable r = new Runnable() {

                public void run() {
                    button.setEnabled(false);
                    jettyController.stopJetty();
                    info.setText(LocaleImpl.getInstance().getString("jetty.view.info.shutdown"));
                    button.setActionCommand("start");
                    info.setText("");
                    button.setText(LocaleImpl.getInstance().getString("jetty.view.panel.button.start"));
                    button.setEnabled(true);
                }
            };
            Thread.currentThread().setContextClassLoader(this.getClass().getClassLoader());
            Thread t = new Thread(r);
            t.start();
        }
    }

    /**
     * @param jetty The jetty to set.
     */
    public void setJettyController(JettyController jetty) {
        this.jettyController = jetty;
        Log.instance().add(this);
    }

    public String getOptions() {
        return null;
    }

    public void log(String arg0, Object arg1, Frame arg2, long arg3) {
        log.append(arg0 + " " + arg1.toString() + "\n");
    }

    public void log(String arg0) {
        log.append(arg0 + "\n");
    }

    public void setOptions(String arg0) {
    }

    public boolean isStarted() {
        return true;
    }

    public void start() throws Exception {
    }

    public void stop() throws InterruptedException {
    }

    /**
     * @return Returns the webappLocation.
     */
    public String getWebappLocation() {
        return webappLocation;
    }

    /**
     * @param webappLocation The webappLocation to set.
     */
    public void setWebappLocation(String webappLocation) {
        this.webappLocation = webappLocation;
        if (webappPath != null) {
            webappPath.setText(webappLocation);
        }
    }

    public JComponent getView() {
        return panel;
    }
}
