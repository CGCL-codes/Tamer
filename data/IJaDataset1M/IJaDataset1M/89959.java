package com.peterhi.player;

import static com.peterhi.player.ResourceLocator.*;
import java.awt.Dimension;
import java.awt.GridBagLayout;
import java.awt.GridBagConstraints;
import java.awt.Insets;
import javax.swing.JLabel;
import javax.swing.JTextField;
import javax.swing.JButton;
import com.peterhi.player.actions.EnterChannelAction;

public class ChannelDialog extends BaseDialog {

    private static final ChannelDialog channelDialog = new ChannelDialog();

    public static ChannelDialog getChannelDialog() {
        return channelDialog;
    }

    private JLabel lblInst = new JLabel();

    private JLabel lblName = new JLabel();

    private JTextField txtName = new JTextField();

    private JButton cmdEnter = new JButton();

    protected ChannelDialog() {
        super();
        setLayout(new GridBagLayout());
        lblInst.setText(getString(this, "PLEASE"));
        lblName.setText(getString(this, "CHANNEL"));
        Dimension dim = txtName.getMinimumSize();
        dim.width = 160;
        txtName.setMinimumSize(dim);
        txtName.setPreferredSize(dim);
        cmdEnter.setAction(EnterChannelAction.getInstance());
        getRootPane().setDefaultButton(cmdEnter);
        GridBagConstraints c = new GridBagConstraints();
        c.gridwidth = 2;
        c.anchor = GridBagConstraints.EAST;
        c.insets = new Insets(2, 2, 2, 2);
        add(lblInst, c);
        c = new GridBagConstraints();
        c.anchor = GridBagConstraints.EAST;
        c.gridy = 1;
        c.insets = new Insets(2, 2, 2, 2);
        add(lblName, c);
        c = new GridBagConstraints();
        c.gridx = 1;
        c.gridy = 1;
        c.insets = new Insets(2, 2, 2, 2);
        add(txtName, c);
        c = new GridBagConstraints();
        c.gridwidth = 2;
        c.gridy = 2;
        c.anchor = GridBagConstraints.EAST;
        c.insets = new Insets(2, 2, 2, 2);
        add(cmdEnter, c);
        putFrozenItem(txtName);
        putFrozenItem(cmdEnter);
        putResetItem(txtName);
        setPreferredSize(new Dimension(300, 160));
        setMinimumSize(new Dimension(300, 160));
        pack();
    }

    public String getName() {
        return txtName.getText();
    }

    public void setStatusText(String text) {
        lblInst.setText(text);
    }
}
