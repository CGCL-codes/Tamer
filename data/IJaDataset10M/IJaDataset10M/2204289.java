package preprocessing;

import preprocessing.methods.BasePreprocessorConfig;
import preprocessing.newGUI.ParameterChangerGUI;
import weka.core.FastVector;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

/**
 * Created by IntelliJ IDEA.
 * User: cepekm1
 * Date: 9.6.2007
 * Time: 18:21:27
 * To change this template use File | Settings | File Templates.
 */
public class ConfigurationPanel implements MouseListener, ConfigDialogCallback, WindowListener, ActionListener {

    private JList list1;

    private JButton saveChangesButton, closeDialogButton;

    private static JFrame fr = null;

    private static BasePreprocessorConfig config;

    private FastVector vals;

    private InputConfigStringDialog configStringDlg;

    public ConfigurationPanel(String configPanelName, BasePreprocessorConfig cfg) {
        if (fr != null) {
            fr.setVisible(true);
            fr.requestFocus();
        } else {
            fr = new JFrame(configPanelName + " Configuration");
            fr.getContentPane().add(constructPanel());
            config = cfg;
            fr.addWindowListener(this);
            fillConfigurationList();
            fr.setVisible(true);
            fr.pack();
        }
    }

    private void fillConfigurationList() {
        FastVector keys = config.getAllKeys();
        vals = config.getAllValues();
        String[] data = new String[keys.size()];
        for (int i = 0; i < keys.size(); i++) {
            data[i] = (String) keys.elementAt(i) + " => " + (String) vals.elementAt(i);
        }
        list1.setListData(data);
    }

    private JPanel constructPanel() {
        JPanel panel1 = new JPanel();
        panel1.setLayout(new GridBagLayout());
        panel1.setEnabled(true);
        panel1.setMinimumSize(new Dimension(400, 250));
        panel1.setPreferredSize(new Dimension(400, 350));
        final JPanel panel2 = new JPanel();
        panel2.setLayout(new GridBagLayout());
        GridBagConstraints gbc;
        gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.weightx = 1.0;
        gbc.weighty = 1.0;
        gbc.fill = GridBagConstraints.BOTH;
        panel1.add(panel2, gbc);
        final JScrollPane scrollPane1 = new JScrollPane();
        gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.weightx = 1.0;
        gbc.weighty = 1.0;
        gbc.fill = GridBagConstraints.BOTH;
        gbc.insets = new Insets(5, 5, 0, 5);
        panel2.add(scrollPane1, gbc);
        list1 = new JList();
        DefaultListSelectionModel lsm = new DefaultListSelectionModel();
        lsm.setSelectionMode(DefaultListSelectionModel.SINGLE_SELECTION);
        list1.setSelectionModel(lsm);
        list1.addMouseListener(this);
        scrollPane1.setViewportView(list1);
        final JPanel panel3 = new JPanel();
        panel3.setLayout(new GridBagLayout());
        gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 1;
        gbc.weightx = 1.0;
        gbc.fill = GridBagConstraints.BOTH;
        panel1.add(panel3, gbc);
        closeDialogButton = new JButton();
        closeDialogButton.setText("Close dialog");
        closeDialogButton.setMnemonic('C');
        closeDialogButton.setDisplayedMnemonicIndex(0);
        closeDialogButton.addActionListener(this);
        gbc = new GridBagConstraints();
        gbc.gridx = 1;
        gbc.gridy = 0;
        gbc.weightx = 1.05;
        gbc.weighty = 1.0;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.insets = new Insets(5, 2, 5, 5);
        panel3.add(closeDialogButton, gbc);
        return panel1;
    }

    public void mouseClicked(MouseEvent mouseEvent) {
        if ((mouseEvent.getButton() == MouseEvent.BUTTON1) && (mouseEvent.getClickCount() == 2)) {
            int selectedIndex = list1.locationToIndex(mouseEvent.getPoint());
            if (selectedIndex < 0) return;
            ParameterChangerGUI paramGUI = new ParameterChangerGUI(fr, this);
            paramGUI.setParameter(config.getParameterAt(selectedIndex));
            paramGUI.setModal(true);
            paramGUI.setVisible(true);
            fillConfigurationList();
        }
    }

    public void mousePressed(MouseEvent mouseEvent) {
    }

    public void mouseReleased(MouseEvent mouseEvent) {
    }

    public void mouseEntered(MouseEvent mouseEvent) {
    }

    public void mouseExited(MouseEvent mouseEvent) {
    }

    public void configDialogEdittingFinished() {
        fillConfigurationList();
    }

    public void windowOpened(WindowEvent windowEvent) {
    }

    public void windowClosing(WindowEvent windowEvent) {
        fr = null;
    }

    public void windowClosed(WindowEvent windowEvent) {
        fr = null;
    }

    public void windowIconified(WindowEvent windowEvent) {
    }

    public void windowDeiconified(WindowEvent windowEvent) {
    }

    public void windowActivated(WindowEvent windowEvent) {
    }

    public void windowDeactivated(WindowEvent windowEvent) {
    }

    public void actionPerformed(ActionEvent actionEvent) {
        if (actionEvent.getSource() == saveChangesButton) {
            for (int i = 0; i < vals.size(); i++) {
                String s = (String) vals.elementAt(i);
                config.setValueByIndex(i, s);
            }
            fr.dispose();
            fr.setVisible(false);
            fr = null;
            return;
        }
        if (actionEvent.getSource() == closeDialogButton) {
            fr.dispose();
            fr.setVisible(false);
            fr = null;
        }
    }
}
