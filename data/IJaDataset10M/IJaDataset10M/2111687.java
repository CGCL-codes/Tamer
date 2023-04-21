package ar.com.greeneuron.nzbgui.gui.settings;

import java.awt.Container;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Frame;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.HeadlessException;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JPanel;
import javax.swing.JTabbedPane;
import ar.com.greeneuron.nzbgui.core.NzbGuiController;
import ar.com.greeneuron.nzbgui.core.NzbGuiController.NzbGuiConfig;
import ar.com.greeneuron.nzbgui.gui.utilities.GuiUtilities;

public class SettingsGui extends JDialog {

    private static final long serialVersionUID = -6250802921872831970L;

    private ServerCfgPanel serverCfgPanel;

    private NzbGuiCfgPanel nzbGuiCfgPanel;

    public SettingsGui(Frame owner, boolean modal) throws HeadlessException {
        super(owner, modal);
        construct();
    }

    private void construct() {
        Container contentPane = getContentPane();
        serverCfgPanel = new ServerCfgPanel();
        nzbGuiCfgPanel = new NzbGuiCfgPanel();
        contentPane.setLayout(new GridBagLayout());
        JTabbedPane tabbedPane = new JTabbedPane();
        tabbedPane.addTab("Server", serverCfgPanel);
        tabbedPane.addTab("NzbGui", nzbGuiCfgPanel);
        contentPane.add(tabbedPane, new GridBagConstraints(0, 0, 1, 1, 1, 1, GridBagConstraints.CENTER, GridBagConstraints.BOTH, new Insets(2, 2, 2, 2), 0, 0));
        contentPane.add(getButtonPanel(), new GridBagConstraints(0, 1, 1, 1, 0, 0, GridBagConstraints.CENTER, GridBagConstraints.BOTH, new Insets(2, 2, 2, 2), 0, 0));
        setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
        setPreferredSize(new Dimension(250, 200));
        setTitle("NzbGui - Settings");
        loadConfig();
        pack();
        GuiUtilities.centerContainerOnScreen(this);
    }

    private JPanel getButtonPanel() {
        JPanel buttonPanel = new JPanel(new FlowLayout());
        buttonPanel.add(getApplyButton());
        buttonPanel.add(getCancelButton());
        return buttonPanel;
    }

    private JButton getCancelButton() {
        JButton btnCancel = new JButton("Cancel");
        btnCancel.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent e) {
                dispose();
            }
        });
        return btnCancel;
    }

    private JButton getApplyButton() {
        JButton btnApply = new JButton("Apply");
        btnApply.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent e) {
                saveConfig();
                dispose();
            }
        });
        return btnApply;
    }

    private void loadConfig() {
        NzbGuiConfig nzbGuiConfig = NzbGuiController.getInstance().getNzbGuiConfig();
        getNzbGuiCfgPanel().loadCfg(nzbGuiConfig);
        getServerCfgPanel().loadCfg(nzbGuiConfig);
    }

    private void saveConfig() {
        NzbGuiConfig nzbGuiConfig = NzbGuiController.getInstance().getNzbGuiConfig();
        getNzbGuiCfgPanel().storeCfg(nzbGuiConfig);
        getServerCfgPanel().storeCfgInPropertie(nzbGuiConfig);
        NzbGuiController.getInstance().storeNzbGuiConfig(nzbGuiConfig);
    }

    private NzbGuiCfgPanel getNzbGuiCfgPanel() {
        return nzbGuiCfgPanel;
    }

    private ServerCfgPanel getServerCfgPanel() {
        return serverCfgPanel;
    }
}
