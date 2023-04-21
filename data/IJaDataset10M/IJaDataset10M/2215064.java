package dnl.net.netclip;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Collection;
import java.util.List;
import javax.swing.ImageIcon;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;
import dnl.confij.ConfigException;
import dnl.confij.ConfigParam;
import dnl.confij.ConfigSection;
import dnl.util.ui.WindowUtils;

public class TrayUI {

    private static final String NET_CLIP_TRAY_NAME = "Network Clipboard on port: ";

    private TrayManager trayManager;

    private NetClipboard netClip;

    private JDialog dialog;

    private CollaborationsUI collaborationsUI;

    private ProgressDialog progressDialog;

    private JDialog aboutDialog;

    public TrayUI(NetClipboard netClip) {
        this.netClip = netClip;
        ImageIcon icon = new ImageIcon(ClassLoader.getSystemResource("netclip.gif"));
        trayManager = TrayManager.getTrayManager(NET_CLIP_TRAY_NAME + netClip.getPort(), icon);
        trayManager.addMenu("Get Clipboard");
        trayManager.addMenuItem("Configure Collaboration Hosts");
        trayManager.addSeperator();
        trayManager.addMenuItem("About");
        trayManager.addSeperator();
        trayManager.addDefaultExitMenu();
        trayManager.addActionListener("Configure Collaboration Hosts", new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                if (collaborationsUI == null) {
                    initCollaborationUI();
                }
                showDialog(collaborationsUI);
            }
        });
        trayManager.addActionListener("About", new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                WindowUtils.centerWindow(getAboutDialog());
                getAboutDialog().setVisible(true);
            }
        });
        refreshCollaborationsListUI();
    }

    public void setProgressText(String message) {
        progressDialog.setText(message);
    }

    public void showProgress(String message) {
        if (progressDialog == null) {
            progressDialog = new ProgressDialog();
        }
        progressDialog.setText(message);
        progressDialog.pack();
        WindowUtils.setWindowLocation(progressDialog, WindowUtils.Corner.BOTTOM_RIGHT);
        Runnable r = new Runnable() {

            @Override
            public void run() {
                progressDialog.setVisible(true);
            }
        };
        try {
            SwingUtilities.invokeLater(r);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void hideProgress() {
        progressDialog.setVisible(false);
    }

    public void displayErrorMessage(String caption, String message) {
        trayManager.displayErrorMessage(caption, message);
    }

    public void displayInfoMessage(String caption, String message) {
        trayManager.displayInfoMessage(caption, message);
    }

    public void displayMessage(String caption, String message) {
        trayManager.displayMessage(caption, message);
    }

    private JDialog getAboutDialog() {
        if (aboutDialog == null) {
            aboutDialog = new JDialog((JFrame) null, "About");
            aboutDialog.setContentPane(new AboutPanel());
            ImageIcon icon = new ImageIcon(ClassLoader.getSystemResource("netclip.gif"));
            aboutDialog.setIconImage(icon.getImage());
            aboutDialog.pack();
        }
        return aboutDialog;
    }

    /**
	 * Refreshes the hosts that appear under the 'Get Clipboard' menu.
	 * 
	 */
    private void refreshCollaborationsListUI() {
        trayManager.removeAllChildrenItems("Get Clipboard");
        ConfigSection section = netClip.getConfiguration().getOrCreateSection("collaborating.hosts");
        Collection<ConfigParam> configParameters = section.getConfigParameters();
        for (final ConfigParam configParam : configParameters) {
            trayManager.addMenuItem("Get Clipboard/" + configParam.getValue());
            trayManager.addActionListener("Get Clipboard/" + configParam.getValue(), new ActionListener() {

                @Override
                public void actionPerformed(ActionEvent e) {
                    netClip.getClipboardContents(configParam.getValue());
                }
            });
        }
    }

    private void initCollaborationUI() {
        collaborationsUI = new CollaborationsUI();
        collaborationsUI.getExitButton().addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent e) {
                dialog.setVisible(false);
                List<String> addresses = collaborationsUI.getAddresses();
                ConfigSection section = netClip.getConfiguration().getSection("collaborating.hosts");
                int i = 0;
                for (String addr : addresses) {
                    i++;
                    section.set("host" + i, addr);
                }
                try {
                    netClip.getConfiguration().saveConfiguration();
                } catch (ConfigException e1) {
                    e1.printStackTrace();
                }
                refreshCollaborationsListUI();
            }
        });
        refreshCollaborationsListUI();
    }

    private void showDialog(JPanel content) {
        JDialog dialog = getDialog(content);
        dialog.pack();
        WindowUtils.centerWindow(dialog);
        dialog.setVisible(true);
    }

    private JDialog getDialog(JPanel content) {
        if (dialog == null) {
            dialog = new JDialog();
        }
        dialog.setContentPane(content);
        return dialog;
    }
}
