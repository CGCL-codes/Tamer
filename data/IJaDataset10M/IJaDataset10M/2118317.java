package net.sf.cclearly.ui.panels.prefs;

import javax.swing.Icon;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPasswordField;
import javax.swing.JProgressBar;
import javax.swing.JTextField;
import javax.swing.WindowConstants;
import net.sf.cclearly.conn.reflector.ReflectorException;
import net.sf.cclearly.conn.reflector.ReflectorMonitor;
import net.sf.cclearly.prefs.Prefs;
import net.sf.cclearly.resources.Icons;
import net.sf.cclearly.util.GuiUtil;
import za.dats.util.injection.Dependant;
import za.dats.util.injection.Inject;
import za.dats.util.injection.Injector;
import com.jgoodies.forms.layout.CellConstraints;
import com.jgoodies.forms.layout.FormLayout;

@Dependant
public class ReflectorPanel extends OptionsPanel {

    @Inject
    Prefs prefs;

    @Inject
    ReflectorMonitor reflector;

    private JTextField reflectorHostField;

    private JTextField reflectorPortField;

    private JPasswordField reflectorPwdField;

    private JTextField proxyHostField;

    private JTextField proxyPortField;

    private JDialog progressFrame;

    public ReflectorPanel() {
        super("Reflector");
        Injector.inject(this);
        FormLayout layout = new FormLayout("5dlu, right:pref, 5dlu, 100dlu, pref:grow, 5dlu", "5dlu, pref, 2dlu, pref, 2dlu, pref, 2dlu, pref, 2dlu, pref, 2dlu, pref, 2dlu, pref, 2dlu, pref, 2dlu, 5dlu");
        setLayout(layout);
        CellConstraints cc = new CellConstraints();
        int y = 2;
        add(new JLabel("Reflector URL:"), cc.xy(2, y));
        reflectorHostField = new JTextField(prefs.getReflectorURL());
        add(reflectorHostField, cc.xy(4, y));
        y += 2;
        add(new JLabel("Reflector Port:"), cc.xy(2, y));
        reflectorPortField = new JTextField("" + prefs.getReflectorPort());
        add(reflectorPortField, cc.xy(4, y));
        y += 2;
        add(new JLabel("Reflector Password:"), cc.xy(2, y));
        reflectorPwdField = new JPasswordField(prefs.getReflectorPassword());
        add(reflectorPwdField, cc.xy(4, y));
        y += 2;
        y += 2;
        y += 2;
        y += 2;
        add(new JLabel("Proxy Host:"), cc.xy(2, y));
        proxyHostField = new JTextField(prefs.getProxyHost());
        add(proxyHostField, cc.xy(4, y));
        y += 2;
        add(new JLabel("Proxy Port:"), cc.xy(2, y));
        proxyPortField = new JTextField("" + prefs.getProxyPort());
        add(proxyPortField, cc.xy(4, y));
    }

    @Override
    public Icon getIcon() {
        return Icons.OPTIONS_REFLECTOR;
    }

    @Override
    public boolean apply() {
        Integer proxyPort = 0;
        try {
            proxyPort = Integer.valueOf(proxyPortField.getText());
        } catch (NumberFormatException e) {
        }
        Integer portValue;
        try {
            portValue = Integer.valueOf(reflectorPortField.getText());
        } catch (NumberFormatException e) {
            portValue = 80;
        }
        boolean test;
        if ((!reflectorHostField.getText().equals(prefs.getReflectorURL())) || (prefs.getReflectorPort() != portValue) || (!String.copyValueOf(reflectorPwdField.getPassword()).equals(prefs.getReflectorPassword())) || (!proxyHostField.getText().equals(prefs.getProxyHost())) || (prefs.getProxyPort() != proxyPort)) {
            test = true;
        } else {
            test = false;
        }
        if (reflectorHostField.getText().length() == 0) {
            test = false;
        }
        prefs.setProxy(proxyHostField.getText(), proxyPort);
        prefs.setReflector(reflectorHostField.getText(), portValue, String.copyValueOf(reflectorPwdField.getPassword()));
        if (test) {
            showProgressFrame();
            try {
                reflector.testSettings();
                GuiUtil.invokeSafely(new Runnable() {

                    public void run() {
                        hideProgressFrame();
                    }
                });
                return true;
            } catch (ReflectorException e) {
                GuiUtil.invokeSafely(new Runnable() {

                    public void run() {
                        hideProgressFrame();
                    }
                });
                int confirm = JOptionPane.showConfirmDialog(getTopLevelAncestor(), e.getMessage() + ", continue anyway?", "Reflector Problem", JOptionPane.YES_NO_OPTION, JOptionPane.ERROR_MESSAGE);
                if (confirm == JOptionPane.YES_OPTION) {
                    return true;
                } else {
                    return false;
                }
            }
        } else {
            return true;
        }
    }

    private void showProgressFrame() {
        progressFrame = new JDialog((JDialog) getTopLevelAncestor());
        progressFrame.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
        progressFrame.setLayout(new FormLayout("5dlu, 200dlu, 5dlu", "5dlu, pref, 20dlu, pref, 5dlu"));
        CellConstraints cc = new CellConstraints();
        progressFrame.add(new JLabel("Testing Reflector"), cc.xy(2, 2));
        JProgressBar progressBar = new JProgressBar();
        progressBar.setIndeterminate(true);
        progressFrame.add(progressBar, cc.xy(2, 4));
        progressFrame.pack();
        progressFrame.setLocationRelativeTo(this);
        progressFrame.setVisible(true);
    }

    private void hideProgressFrame() {
        if (progressFrame == null) {
            return;
        }
        progressFrame.setVisible(false);
        progressFrame.dispose();
        progressFrame = null;
    }
}
