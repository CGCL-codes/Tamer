package jscorch;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

public class WeaponDialog extends JDialog {

    private Player player;

    private JLabel message = new JLabel("Choose your weapon sir!");

    private JComboBox weapons = new JComboBox();

    private JButton OKButton = new JButton("OK");

    private JButton CancelButton = new JButton("Cancel");

    public WeaponDialog(Frame par, Player p) {
        super(par, "Weapon Chooser", true);
        player = p;
        init();
        this.getRootPane().setDefaultButton(OKButton);
        pack();
        setResizable(false);
        setVisible(true);
    }

    public void init() {
        Font f = Preferences.FONT;
        message.setFont(f);
        CancelButton.setFont(f);
        CancelButton.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent e) {
                dispose();
            }
        });
        OKButton.setFont(f);
        OKButton.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent e) {
                player.getWeaponLocker().setCurrentOfInventory(weapons.getSelectedIndex());
                dispose();
            }
        });
        weapons = new JComboBox(player.getWeaponLocker().getInventory());
        weapons.setFont(f);
        weapons.setSelectedIndex(player.getWeaponLocker().getCurrentOfInventory());
        weapons.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent e) {
                OKButton.requestFocus();
            }
        });
        JPanel contentPane = new JPanel();
        Container messagePane = new Container();
        Container buttonPane = new Container();
        contentPane.setLayout(new BoxLayout(contentPane, BoxLayout.Y_AXIS));
        messagePane.setLayout(new BoxLayout(messagePane, BoxLayout.X_AXIS));
        buttonPane.setLayout(new BoxLayout(buttonPane, BoxLayout.X_AXIS));
        buttonPane.add(Box.createHorizontalGlue());
        buttonPane.add(Box.createRigidArea(OKButton.getPreferredSize()));
        buttonPane.add(CancelButton);
        buttonPane.add(Box.createRigidArea(new Dimension(5, 0)));
        buttonPane.add(OKButton);
        messagePane.add(Box.createHorizontalGlue());
        messagePane.add(message);
        messagePane.add(Box.createHorizontalGlue());
        contentPane.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
        contentPane.add(messagePane);
        contentPane.add(Box.createRigidArea(new Dimension(0, 10)));
        contentPane.add(weapons);
        contentPane.add(Box.createRigidArea(new Dimension(0, 15)));
        contentPane.add(buttonPane);
        this.setContentPane(contentPane);
    }
}
