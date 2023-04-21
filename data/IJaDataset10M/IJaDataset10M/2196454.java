package clojure;

import clojure.ClojurePlugin;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.FilenameFilter;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JTextField;
import javax.swing.JSeparator;
import org.gjt.sp.jedit.AbstractOptionPane;
import org.gjt.sp.jedit.EditPlugin;
import org.gjt.sp.jedit.GUIUtilities;
import org.gjt.sp.jedit.MiscUtilities;
import org.gjt.sp.jedit.browser.VFSBrowser;
import org.gjt.sp.jedit.browser.VFSFileChooserDialog;
import org.gjt.sp.jedit.jEdit;

public class ClojureProviderOptionPane extends AbstractOptionPane {

    private JRadioButton coreIncluded;

    private JRadioButton coreCustom;

    private JTextField corePath;

    private JButton coreBrowse;

    private JRadioButton contribIncluded;

    private JRadioButton contribCustom;

    private JTextField contribPath;

    private JButton contribBrowse;

    private ClojurePlugin plugin;

    public ClojureProviderOptionPane() {
        super("clojure-provider");
        plugin = (ClojurePlugin) jEdit.getPlugin("clojure.ClojurePlugin");
    }

    protected void _init() {
        ButtonHandler handler = new ButtonHandler();
        JPanel corePanel = new JPanel();
        corePanel.setLayout(new BoxLayout(corePanel, BoxLayout.X_AXIS));
        corePanel.add(coreIncluded = new JRadioButton(jEdit.getProperty("options.clojure.included-core-label")));
        corePanel.add(coreCustom = new JRadioButton(jEdit.getProperty("options.clojure.choose-label")));
        ButtonGroup coreGroup = new ButtonGroup();
        coreGroup.add(coreIncluded);
        coreGroup.add(coreCustom);
        corePanel.add(new JSeparator(JSeparator.VERTICAL));
        corePanel.add(corePath = new JTextField());
        coreBrowse = new JButton(jEdit.getProperty("vfs.browser.browse.label"));
        coreBrowse.addActionListener(new BrowseHandler(corePath));
        corePanel.add(coreBrowse);
        String core = plugin.getClojureCore();
        if (core.equals(ClojurePlugin.INCLUDED_CORE)) {
            coreIncluded.setSelected(true);
            corePath.setEnabled(false);
            coreBrowse.setEnabled(false);
        } else {
            coreCustom.setSelected(true);
            corePath.setText(core);
        }
        coreIncluded.addActionListener(handler);
        coreCustom.addActionListener(handler);
        addComponent("Core:", corePanel);
        JPanel contribPanel = new JPanel();
        contribPanel.setLayout(new BoxLayout(contribPanel, BoxLayout.X_AXIS));
        contribPanel.add(contribIncluded = new JRadioButton("Included (1.1.0)"));
        contribPanel.add(contribCustom = new JRadioButton("Choose jar"));
        ButtonGroup contribGroup = new ButtonGroup();
        contribGroup.add(contribIncluded);
        contribGroup.add(contribCustom);
        contribPanel.add(new JSeparator(JSeparator.VERTICAL));
        contribPanel.add(contribPath = new JTextField());
        contribBrowse = new JButton(jEdit.getProperty("vfs.browser.browse.label"));
        contribBrowse.addActionListener(new BrowseHandler(contribPath));
        contribPanel.add(contribBrowse);
        String contrib = plugin.getClojureContrib();
        if (contrib.equals(ClojurePlugin.INCLUDED_CONTRIB)) {
            contribIncluded.setSelected(true);
            contribPath.setEnabled(false);
            contribBrowse.setEnabled(false);
        } else {
            contribCustom.setSelected(true);
            contribPath.setText(contrib);
        }
        contribIncluded.addActionListener(handler);
        contribCustom.addActionListener(handler);
        addComponent("Contrib:", contribPanel);
    }

    protected void _save() {
        if (coreIncluded.isSelected()) {
            plugin.setClojureCore(ClojurePlugin.INCLUDED_CORE);
        } else {
            plugin.setClojureCore(corePath.getText());
        }
        if (contribIncluded.isSelected()) {
            plugin.setClojureContrib(ClojurePlugin.INCLUDED_CONTRIB);
        } else {
            plugin.setClojureContrib(contribPath.getText());
        }
        plugin.setVars();
    }

    class ButtonHandler implements ActionListener {

        public void actionPerformed(ActionEvent e) {
            Object source = e.getSource();
            if (source == coreIncluded) {
                corePath.setEnabled(false);
                coreBrowse.setEnabled(false);
            } else if (source == coreCustom) {
                corePath.setEnabled(true);
                coreBrowse.setEnabled(true);
            } else if (source == contribIncluded) {
                contribPath.setEnabled(false);
                contribBrowse.setEnabled(false);
            } else if (source == contribCustom) {
                contribPath.setEnabled(true);
                contribBrowse.setEnabled(true);
            }
        }
    }

    class BrowseHandler implements ActionListener {

        private JTextField txt;

        public BrowseHandler(JTextField txt) {
            this.txt = txt;
        }

        public void actionPerformed(ActionEvent e) {
            VFSFileChooserDialog dialog = new VFSFileChooserDialog(jEdit.getActiveView(), System.getProperty("user.dir") + File.separator, VFSBrowser.OPEN_DIALOG, false, true);
            String[] files = dialog.getSelectedFiles();
            if (files != null && files.length == 1) {
                txt.setText(files[0]);
            }
        }
    }
}
