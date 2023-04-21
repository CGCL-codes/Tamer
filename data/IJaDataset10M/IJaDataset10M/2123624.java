package preprocessing.newGUI;

import game.utils.DebugInfo;
import preprocessing.ConfigDialogCallback;
import preprocessing.Parameters.Parameter;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * Created by IntelliJ IDEA.
 * User: lagon
 * Date: May 25, 2008
 * Time: 2:12:55 PM
 * To change this template use File | Settings | File Templates.
 */
public class ParameterChangerGUI extends JDialog implements ActionListener {

    private Parameter dispParam;

    private boolean notYetShown;

    private JButton buttonOK;

    private JButton buttonCancel;

    private JPanel parameterField;

    private JLabel label1;

    private ConfigDialogCallback callback;

    public ParameterChangerGUI(JFrame owner, ConfigDialogCallback callback) {
        super(owner);
        notYetShown = true;
        this.callback = callback;
    }

    public void setParameter(Parameter param) {
        dispParam = param;
    }

    public void setVisible(boolean visible) {
        if ((visible) && (notYetShown)) {
            if (dispParam == null) {
                DebugInfo.putErrorMessage("Cannot show configuration panel because no parameter was set before.");
                throw new NullPointerException("ParameterChangerGUI::setVisible : Cannot show configuration panel because no parameter was set before.");
            }
            notYetShown = false;
            setContentPane(createGUI());
            parameterField.add(dispParam.getEditorComponent());
            buttonOK.addActionListener(this);
            buttonCancel.addActionListener(this);
            label1.setText("Set new value for parameter " + dispParam.getName());
            super.pack();
            super.setModal(true);
            super.setVisible(true);
        } else if (!visible) {
            super.setVisible(false);
            notYetShown = true;
        }
    }

    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == buttonOK) {
            dispParam.editingFinished();
            callback.configDialogEdittingFinished();
            super.setVisible(false);
            notYetShown = true;
            dispose();
        }
        if (e.getSource() == buttonCancel) {
            super.setVisible(false);
            notYetShown = true;
            dispose();
        }
    }

    private JPanel createGUI() {
        JPanel contentPane = new JPanel();
        contentPane.setLayout(new GridBagLayout());
        final JPanel panel1 = new JPanel();
        panel1.setLayout(new GridBagLayout());
        GridBagConstraints gbc;
        gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 1;
        gbc.weightx = 1.0;
        gbc.anchor = GridBagConstraints.EAST;
        gbc.fill = GridBagConstraints.VERTICAL;
        contentPane.add(panel1, gbc);
        final JPanel panel2 = new JPanel();
        panel2.setLayout(new GridBagLayout());
        gbc = new GridBagConstraints();
        gbc.gridx = 1;
        gbc.gridy = 0;
        gbc.weighty = 1.0;
        gbc.anchor = GridBagConstraints.EAST;
        gbc.fill = GridBagConstraints.VERTICAL;
        panel1.add(panel2, gbc);
        buttonOK = new JButton();
        buttonOK.setText("OK");
        gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.weightx = 1.0;
        gbc.weighty = 1.0;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        panel2.add(buttonOK, gbc);
        buttonCancel = new JButton();
        buttonCancel.setText("Cancel");
        gbc = new GridBagConstraints();
        gbc.gridx = 1;
        gbc.gridy = 0;
        gbc.weightx = 1.0;
        gbc.weighty = 1.0;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        panel2.add(buttonCancel, gbc);
        final JPanel panel3 = new JPanel();
        panel3.setLayout(new GridBagLayout());
        gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.weightx = 1.0;
        gbc.weighty = 1.0;
        gbc.fill = GridBagConstraints.BOTH;
        contentPane.add(panel3, gbc);
        label1 = new JLabel();
        label1.setText("Edit desired parameter called:");
        gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.weightx = 1.0;
        gbc.anchor = GridBagConstraints.WEST;
        panel3.add(label1, gbc);
        parameterField = new JPanel();
        parameterField.setLayout(new GridBagLayout());
        gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 1;
        gbc.weightx = 1.0;
        gbc.weighty = 1.0;
        gbc.fill = GridBagConstraints.BOTH;
        panel3.add(parameterField, gbc);
        return contentPane;
    }
}
