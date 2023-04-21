package main;

import java.awt.BorderLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTabbedPane;
import javax.swing.JTextField;

/**
 * The ConfigDialog allows editing of the data saved in Config.
 * 
 * @author Sven Schneider
 * @since 0.1.22
 */
public class ConfigDialog extends JDialog {

    /** The height of the dialog-window. */
    private static final int DIALOG_HEIGHT = 450;

    /** The width of the dialog-window. */
    private static final int DIALOG_WIDTH = 700;

    /** A generated serialVersionUID. */
    private static final long serialVersionUID = -1283734715002382016L;

    /**
	 * This textfield shows Config#browserPath.
	 * 
	 * @see Config#browserPath
	 */
    private JTextField misc_tfBrowser = new JTextField();

    /**
	 * This textfield shows Config#intialFileChooserPath.
	 * 
	 * @see Config#intialFileChooserPath
	 */
    private JTextField misc_tfInitial = new JTextField(Config.getIntialFileChooserPath().getAbsolutePath());

    /**
	 * Contains the paths from Config#changedMachinePath.
	 * 
	 * @see Config#changedMachinePath
	 */
    private DefaultListModel mp_dlmPaths = new DefaultListModel();

    /**
	 * Shows the mp_dlmPaths-DefaultListModel.
	 * 
	 * @see #mp_dlmPaths
	 */
    private JList mp_lPaths = new JList(mp_dlmPaths);

    /** The user can enter a machine-path he wants to add. */
    private JTextField mp_tfInput = new JTextField();

    /**
	 * Contains the plugins from Config#plugins.
	 * <p>
	 * The defaultplugin is also marked.
	 * 
	 * @see Config#plugins
	 * @see Config#defaultPlugin
	 */
    private DefaultListModel plug_dlmPlugins = new DefaultListModel();

    /**
	 * Shows the plug_dlmPlugins-DefaultListModel.
	 * 
	 * @see #plug_dlmPlugins
	 */
    private JList plug_lPlugins = new JList(plug_dlmPlugins);

    /** The user can enter a plugin-java-path he wants to add. */
    private JTextField plug_tfInput = new JTextField();

    /**
	 * The constructor, initialising the window.
	 * 
	 * @param mainFrame
	 *            the dialogs parented JFrame.
	 * @param title
	 *            the title the dialog will have.
	 */
    protected ConfigDialog(JFrame mainFrame, String title) {
        super(mainFrame, title, true);
        setResizable(false);
        if (Config.getBrowser() != null) misc_tfBrowser.setText(Config.getBrowser().getAbsolutePath());
        JPanel panelTmp = new JPanel(new BorderLayout());
        JTabbedPane tp = new JTabbedPane();
        add(panelTmp);
        JButton b = new JButton("Done");
        JPanel panelTmp2 = new JPanel(new GridLayout());
        panelTmp.add(panelTmp2, BorderLayout.SOUTH);
        panelTmp.add(tp, BorderLayout.CENTER);
        b.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent arg0) {
                bDoneClick();
            }
        });
        panelTmp2.add(b);
        b = new JButton("Abort");
        b.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent arg0) {
                bAbortClick();
            }
        });
        panelTmp2.add(b);
        JPanel mainPanel = new JPanel(new BorderLayout());
        mainPanel.setName("MachinePath");
        tp.add(mainPanel);
        panelTmp = new JPanel(new GridLayout());
        mainPanel.add(panelTmp, BorderLayout.NORTH);
        b = new JButton("Add");
        b.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent arg0) {
                bMPAddClick();
            }
        });
        panelTmp.add(b);
        b = new JButton("Remove Selected");
        b.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent arg0) {
                bMPRemoveSelectedClick();
            }
        });
        panelTmp.add(b);
        b = new JButton("Remove All");
        b.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent arg0) {
                bMPRemoveAllClick();
            }
        });
        panelTmp.add(b);
        panelTmp = new JPanel(new BorderLayout());
        mainPanel.add(panelTmp, BorderLayout.CENTER);
        panelTmp.add(mp_tfInput, BorderLayout.NORTH);
        panelTmp.add(new JScrollPane(mp_lPaths), BorderLayout.CENTER);
        for (String s : Config.getChangedMachinePath()) mp_dlmPaths.addElement(s);
        mainPanel = new JPanel(new BorderLayout());
        mainPanel.setName("Plugins");
        tp.add(mainPanel);
        panelTmp = new JPanel(new GridLayout());
        mainPanel.add(panelTmp, BorderLayout.NORTH);
        b = new JButton("Add");
        b.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent arg0) {
                bPLUGAddClick();
            }
        });
        panelTmp.add(b);
        b = new JButton("Remove Selected");
        b.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent arg0) {
                bPLUGRemoveSelectedClick();
            }
        });
        panelTmp.add(b);
        b = new JButton("Remove All");
        b.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent arg0) {
                bPLUGRemoveAllClick();
            }
        });
        panelTmp.add(b);
        b = new JButton("Set Default");
        b.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent arg0) {
                bPLUGSetDefault();
            }
        });
        panelTmp.add(b);
        panelTmp = new JPanel(new BorderLayout());
        mainPanel.add(panelTmp, BorderLayout.CENTER);
        panelTmp.add(plug_tfInput, BorderLayout.NORTH);
        panelTmp.add(new JScrollPane(plug_lPlugins), BorderLayout.CENTER);
        for (String s : Config.getPlugins()) if (s.equals(Config.getDefaultPlugin())) plug_dlmPlugins.addElement("Default: " + s); else plug_dlmPlugins.addElement(s);
        mainPanel = new JPanel(new BorderLayout());
        GridLayout gl = new GridLayout(3, 2);
        panelTmp = new JPanel(gl);
        mainPanel.setName("Misc");
        tp.add(mainPanel);
        mainPanel.add(panelTmp, BorderLayout.NORTH);
        panelTmp.add(new JLabel("Browser Path"));
        panelTmp.add(misc_tfBrowser);
        panelTmp.add(new JLabel("Initial FileChooser Path"));
        panelTmp.add(misc_tfInitial);
        panelTmp.add(new JLabel("Recently Used History"));
        b = new JButton("Clear");
        b.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent arg0) {
                bMISCClearRecently();
            }
        });
        panelTmp.add(b);
        setBounds(mainFrame.getX() + (mainFrame.getWidth() - DIALOG_WIDTH) / 2, mainFrame.getY() + (mainFrame.getHeight() - DIALOG_HEIGHT) / 2, DIALOG_WIDTH, DIALOG_HEIGHT);
    }

    /**
	 * The user chooses abort.
	 * <p>
	 * The browser/initialFileChooserPath are <b>not</b> stored.
	 * 
	 * @see Config#browserPath
	 * @see Config#intialFileChooserPath
	 */
    private void bAbortClick() {
        dispose();
    }

    /**
	 * The user chooses sone.
	 * <p>
	 * The browser/initialFileChooserPath are stored.
	 * 
	 * @see Config#browserPath
	 * @see Config#intialFileChooserPath
	 */
    private void bDoneClick() {
        Config.setBrowser(new File(misc_tfBrowser.getText()));
        Config.setInitial(new File(misc_tfInitial.getText()));
        dispose();
    }

    /** The list of recently used files is purged. */
    private void bMISCClearRecently() {
        Config.recentClear();
    }

    /**
	 * The machine-path the user entered is added to the list of machine-paths.
	 */
    private void bMPAddClick() {
        Config.machinePathAdd(mp_tfInput.getText());
        mp_dlmPaths.removeAllElements();
        for (String s : Config.getChangedMachinePath()) mp_dlmPaths.addElement(s);
    }

    /**
	 * The list of machine-paths is cleared.
	 */
    private void bMPRemoveAllClick() {
        Config.machinePathRemoveAll();
        mp_dlmPaths.removeAllElements();
    }

    /**
	 * The selected machine-path is removed.
	 */
    private void bMPRemoveSelectedClick() {
        Config.machinePathRemove((String) mp_dlmPaths.elementAt(mp_lPaths.getSelectedIndex()));
        mp_dlmPaths.removeAllElements();
        for (String s : Config.getChangedMachinePath()) mp_dlmPaths.addElement(s);
    }

    /**
	 * The entered plugin-java-path is added to the list of plugins.
	 */
    private void bPLUGAddClick() {
        Config.pluginAdd(plug_tfInput.getText());
        plug_dlmPlugins.removeAllElements();
        for (String s : Config.getPlugins()) if (s.equals(Config.getDefaultPlugin())) plug_dlmPlugins.addElement("Default: " + s); else plug_dlmPlugins.addElement(s);
    }

    /**
	 * All plugin-java-paths are removed except the defaultPlugin.
	 */
    private void bPLUGRemoveAllClick() {
        Config.pluginRemoveAll();
        plug_dlmPlugins.removeAllElements();
        plug_dlmPlugins.addElement("Default: " + Config.getDefaultPlugin());
    }

    /**
	 * The selected plugin is removed from the list of plugins.<br>
	 * If the selected plugin was the default plugin the plugin is not removed.
	 */
    private void bPLUGRemoveSelectedClick() {
        String str = (String) plug_dlmPlugins.elementAt(plug_lPlugins.getSelectedIndex());
        if (str.startsWith("Default: ")) return;
        Config.pluginRemove(str);
        plug_dlmPlugins.removeAllElements();
        for (String s : Config.getPlugins()) if (s.equals(Config.getDefaultPlugin())) plug_dlmPlugins.addElement("Default: " + s); else plug_dlmPlugins.addElement(s);
    }

    /**
	 * The selected plugin becomes the default plugin.
	 */
    private void bPLUGSetDefault() {
        String str = (String) plug_dlmPlugins.elementAt(plug_lPlugins.getSelectedIndex());
        if (str.startsWith("Default: ")) return;
        Config.setDefault(str);
        plug_dlmPlugins.removeAllElements();
        for (String s : Config.getPlugins()) if (s.equals(Config.getDefaultPlugin())) plug_dlmPlugins.addElement("Default: " + s); else plug_dlmPlugins.addElement(s);
    }
}
