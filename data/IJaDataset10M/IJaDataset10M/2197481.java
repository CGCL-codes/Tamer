package net.sourceforge.rcontrol.swing.options;

import i18n.i18n;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Iterator;
import javax.swing.tree.DefaultMutableTreeNode;
import net.sourceforge.rcontrol.controller.Controller;
import net.sourceforge.rcontrol.model.Config;
import net.sourceforge.rcontrol.swing.base.ConfigAdapter;
import net.sourceforge.rcontrol.swing.base.MyPanelDialog;
import net.sourceforge.rcontrol.swing.libs.elop.ELOPOptionPanel;
import net.sourceforge.rcontrol.swing.libs.elop.OptionTree;
import net.sourceforge.rcontrol.swing.main.MainFrame;

public class OptionDialog {

    private ELOPOptionPanel optionPanel;

    private Controller controller;

    private Config config;

    private MyPanelDialog mDialog;

    public OptionDialog(Controller controller) {
        this.controller = controller;
        this.config = controller.getModel().getConfig();
        mDialog = new MyPanelDialog(i18n.getString("options_dialog_title"), MainFrame.getInstance(), true);
        optionPanel = new ELOPOptionPanel();
        optionPanel.addOKButtonActionListener(new OKButtonListener());
        optionPanel.addCancelButtonActionListener(new CancelButtonListener());
        optionPanel.insertOptionTreeNode(new GeneralOptions(config));
        optionPanel.insertOptionTreeNode(new LoggingOptions(config, mDialog));
        optionPanel.insertOptionTreeNode(new InterfaceOptions(controller));
        optionPanel.getTree().setSelectionInterval(0, 0);
        mDialog.getContentPane().add(optionPanel);
        mDialog.setSize(600, 400);
        mDialog.center(MainFrame.getInstance());
        mDialog.setVisible(true);
    }

    private void applyChanges() {
        OptionTree tree = optionPanel.getTree();
        DefaultMutableTreeNode next = (DefaultMutableTreeNode) tree.getModel().getRoot();
        while ((next = next.getNextNode()) != null) {
            if (next instanceof MyOptionTreeNode) {
                MyOptionTreeNode node = (MyOptionTreeNode) next;
                Iterator nodeIt = node.getOptions().iterator();
                while (nodeIt.hasNext()) {
                    ConfigAdapter ca = (ConfigAdapter) nodeIt.next();
                    if (ca.hasChanged()) {
                        config.setConfigValue(ca.getValue());
                    }
                }
            }
        }
    }

    private void close() {
        mDialog.dispose();
    }

    private class OKButtonListener implements ActionListener {

        public void actionPerformed(ActionEvent e) {
            applyChanges();
            close();
        }
    }

    private class CancelButtonListener implements ActionListener {

        public void actionPerformed(ActionEvent e) {
            close();
        }
    }
}
