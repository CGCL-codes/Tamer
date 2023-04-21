package antirashka.ui;

import antirashka.ballistics.IWeapon;
import antirashka.engine.*;
import antirashka.icons.IconManager;
import antirashka.map.items.IPC;
import antirashka.map.items.Team;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

final class ControlPanel implements IRoundListener {

    private final JToggleButton btnMove = new JToggleButton(new AbstractAction("Move (M)") {

        public void actionPerformed(ActionEvent e) {
            controller.moveCurrentPlayer(!btnMove.isSelected(), null);
        }
    });

    private final JToggleButton btnFire = new JToggleButton(new AbstractAction("Fire (F)") {

        public void actionPerformed(ActionEvent e) {
            controller.fireCurrentPlayer(!btnFire.isSelected(), null);
        }
    });

    private final JButton btnSkip = new JButton(new AbstractAction("Skip (K)") {

        public void actionPerformed(ActionEvent e) {
            controller.skipCurrentPlayer();
        }
    });

    private final JButton btnCenter = new JButton(new AbstractAction("Center") {

        public void actionPerformed(ActionEvent e) {
            controller.centerCurrentPlayer();
        }
    });

    private final JToggleButton btnPin = new JToggleButton(new AbstractAction("Pin") {

        public void actionPerformed(ActionEvent e) {
            controller.pinCurrentPlayer(btnPin.isSelected());
        }
    });

    private final JComboBox chWeapon = new JComboBox();

    private final JComboBox chFireMode = new JComboBox();

    private final JTextField tfWho = new JTextField(10);

    private final JTextField tfHP = new JTextField(4);

    private final JTextField tfActionPoints = new JTextField(6);

    private final JLabel userPic = new JLabel();

    private final JPanel main = new JPanel();

    private final Map<String, JLabel> lblPlayer = new HashMap<String, JLabel>();

    private final Map<String, HPDisplay> hpPlayer = new HashMap<String, HPDisplay>();

    private String shownPlayer = null;

    private final IController controller;

    private final Action saveAction;

    ControlPanel(final IController controller, List<IPC> pcs, Action saveAction) {
        this.controller = controller;
        this.saveAction = saveAction;
        JPanel userPanel = new JPanel(new BorderLayout());
        userPic.setPreferredSize(new Dimension(100, 100));
        userPanel.add(userPic, BorderLayout.CENTER);
        userPanel.add(tfWho, BorderLayout.SOUTH);
        userPic.setHorizontalAlignment(JLabel.CENTER);
        userPic.setVerticalAlignment(JLabel.CENTER);
        tfWho.setHorizontalAlignment(JTextField.CENTER);
        tfWho.setEditable(false);
        tfActionPoints.setEditable(false);
        tfHP.setEditable(false);
        JPanel players = new JPanel(new GridBagLayout());
        Icon centerIcon = new ImageIcon(IconManager.turnWhiteToTransparent(getClass().getResource("center.png")));
        for (int i = 0; i < pcs.size(); i++) {
            final IPC pc = pcs.get(i);
            JLabel label = new JLabel(pc.getName());
            players.add(label, new GridBagConstraints(0, i, 1, 1, 0.0, 0.0, GridBagConstraints.EAST, GridBagConstraints.NONE, new Insets(0, 0, 3, 5), 0, 0));
            lblPlayer.put(pc.getName(), label);
            HPDisplay hp = new HPDisplay();
            hp.setValue(pc.getHP(), pc.getInitialHP());
            players.add(hp, new GridBagConstraints(1, i, 1, 1, 0.0, 0.0, GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(0, 0, 3, 5), 0, 0));
            hpPlayer.put(pc.getName(), hp);
            JButton button = new JButton(centerIcon);
            button.addActionListener(new ActionListener() {

                public void actionPerformed(ActionEvent e) {
                    controller.showPlayer(pc);
                }
            });
            players.add(button, new GridBagConstraints(2, i, 1, 1, 0.0, 0.0, GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(0, 0, 3, 10), 0, 0));
        }
        JPanel actions1 = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        actions1.add(chWeapon);
        actions1.add(chFireMode);
        actions1.add(btnFire);
        JPanel actions2 = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        actions2.add(btnMove);
        actions2.add(btnSkip);
        JPanel actions3 = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        actions3.add(btnCenter);
        actions3.add(btnPin);
        JPanel actions = new JPanel(new GridLayout(3, 1));
        actions.add(actions1);
        actions.add(actions2);
        actions.add(actions3);
        JPanel info = new JPanel(new GridBagLayout());
        info.add(new JLabel("Life points:"), new GridBagConstraints(0, 0, 1, 1, 0.0, 0.0, GridBagConstraints.EAST, GridBagConstraints.NONE, new Insets(0, 0, 0, 5), 0, 0));
        info.add(tfHP, new GridBagConstraints(1, 0, 1, 1, 1.0, 0.0, GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL, new Insets(0, 0, 0, 0), 0, 0));
        info.add(new JLabel("Action points:"), new GridBagConstraints(0, 1, 1, 1, 0.0, 0.0, GridBagConstraints.EAST, GridBagConstraints.NONE, new Insets(0, 0, 0, 5), 0, 0));
        info.add(tfActionPoints, new GridBagConstraints(1, 1, 1, 1, 1.0, 0.0, GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL, new Insets(0, 0, 0, 0), 0, 0));
        main.setLayout(new ControlLayout(players, actions, userPanel, info));
        main.add(players);
        main.add(actions);
        main.add(userPanel);
        main.add(info);
        main.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
        chFireMode.addItemListener(new ItemListener() {

            public void itemStateChanged(ItemEvent e) {
                if (e.getStateChange() == ItemEvent.SELECTED) {
                    setFireMode();
                }
            }
        });
        chWeapon.addItemListener(new ItemListener() {

            public void itemStateChanged(ItemEvent e) {
                if (e.getStateChange() == ItemEvent.SELECTED) {
                    setWeapon();
                }
            }
        });
        enableDisable(false, 0);
        controller.addRoundListener(this);
    }

    private void setFireMode() {
        FireDesc mode = (FireDesc) chFireMode.getSelectedItem();
        controller.setFireMode(mode.mode);
    }

    private void setWeapon() {
        IWeapon weapon = (IWeapon) chWeapon.getSelectedItem();
        controller.setWeapon(weapon);
    }

    JComponent getVisual() {
        return main;
    }

    public void roundChanged(RoundEvent event) {
        if (event instanceof RoundEnemyEvent) {
            actionPerformed(PrepareMode.NONE);
            enableDisable(false, 0);
            clearActive();
            tfWho.setText("Enemy");
            userPic.setIcon(IconManager.getInstance().getUserpic("rashka", true));
            tfActionPoints.setText("");
            tfHP.setText("");
        } else if (event instanceof RoundPlayerEvent) {
            RoundPlayerEvent e = (RoundPlayerEvent) event;
            if (e.active) {
                clearActive();
                shownPlayer = e.name;
                lblPlayer.get(e.name).setForeground(Color.red);
            }
            actionPerformed(PrepareMode.NONE);
            tfWho.setText(e.name);
            userPic.setIcon(IconManager.getInstance().getUserpic(e.name, e.alive));
            showHP(e.hp);
            btnPin.setSelected(e.pinned);
            chWeapon.setModel(new DefaultComboBoxModel(e.weapons));
            chWeapon.setSelectedItem(e.selectedWeapon);
            chFireMode.setModel(new DefaultComboBoxModel(e.modes));
            setMode(e.active, e.actionPoints, false, e.mode);
        } else if (event instanceof RoundAPEvent) {
            RoundAPEvent e = (RoundAPEvent) event;
            enableDisable(true, e.actionPoints);
            showAP(e.actionPoints);
        } else if (event instanceof RoundFireModeEvent) {
            RoundFireModeEvent e = (RoundFireModeEvent) event;
            setMode(true, e.actionPoints, e.preparation, e.mode);
        } else if (event instanceof RoundWeaponEvent) {
            RoundWeaponEvent e = (RoundWeaponEvent) event;
            chWeapon.setSelectedItem(e.selectedWeapon);
            chFireMode.setModel(new DefaultComboBoxModel(e.modes));
            setMode(true, e.actionPoints, e.preparation, e.mode);
        } else if (event instanceof RoundPrepareEvent) {
            RoundPrepareEvent e = (RoundPrepareEvent) event;
            showPrepare(e.actionPoints, e.delta);
        } else if (event instanceof RoundDamageEvent) {
            RoundDamageEvent e = (RoundDamageEvent) event;
            hpPlayer.get(e.name).setValue(e.hp, e.initialHP);
            if (shownPlayer != null && shownPlayer.equals(e.name)) {
                if (!e.alive) {
                    userPic.setIcon(IconManager.getInstance().getUserpic(e.name, e.alive));
                }
                showHP(e.hp);
            }
        } else if (event instanceof RoundGameOverEvent) {
            RoundGameOverEvent e = (RoundGameOverEvent) event;
            if (!e.win) {
                saveAction.setEnabled(false);
                enableDisable(false, 0);
            }
            StringBuilder buf = new StringBuilder();
            for (Map.Entry<Team, Integer> entry : e.deaths.entrySet()) {
                Team team = entry.getKey();
                Integer value = entry.getValue();
                if (buf.length() > 0) {
                    buf.append('\n');
                }
                buf.append(team + " - " + value + " dead");
            }
            JOptionPane.showMessageDialog(null, (e.win ? "YOU WON!" : "GAME OVER!") + "\n\n" + buf, "Game over", e.win ? JOptionPane.INFORMATION_MESSAGE : JOptionPane.ERROR_MESSAGE);
        }
    }

    private void clearActive() {
        if (shownPlayer != null) {
            lblPlayer.get(shownPlayer).setForeground(null);
            shownPlayer = null;
        }
    }

    private void setMode(boolean active, int ap, boolean preparation, FireDesc mode) {
        chFireMode.setSelectedItem(mode);
        enableDisable(active, ap);
        if (preparation) {
            showPrepare(ap, mode.apToFire);
        } else {
            showAP(ap);
        }
    }

    private void showAP(int ap) {
        tfActionPoints.setText(String.valueOf(ap));
    }

    private void showPrepare(int ap, int delta) {
        tfActionPoints.setText(ap + " - " + delta + " = " + (ap - delta));
    }

    private void showHP(int hp) {
        tfHP.setText(String.valueOf(hp));
    }

    public void actionPerformed(PrepareMode mode) {
        btnMove.setSelected(mode == PrepareMode.MOVE);
        btnFire.setSelected(mode == PrepareMode.TARGET);
    }

    private void enableDisable(boolean on, int actionPoints) {
        chWeapon.setEnabled(on);
        chFireMode.setEnabled(on);
        btnMove.setEnabled(on);
        FireDesc mode = (FireDesc) chFireMode.getSelectedItem();
        btnFire.setEnabled(on && mode != null && actionPoints >= mode.apToFire);
        btnSkip.setEnabled(on);
        btnCenter.setEnabled(on);
        btnPin.setEnabled(on);
    }
}
