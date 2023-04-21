package com.shithead.ui.dialog;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import com.shithead.ShitheadConstants;
import com.shithead.bo.GameRules;
import com.shithead.enums.CardFace;
import com.shithead.ui.SHFrame;
import com.shithead.util.PropertiesUtil;

public class RulesDialog extends JDialog implements ActionListener {

    private static final long serialVersionUID = 3120386257450579896L;

    private static final Dimension JCOMBO_SIZE = new Dimension(80, 20);

    public static final Integer[] HAND_SIZES = { 2, 3, 4 };

    public static final Integer[] PLAYER_COUNT = { 2, 3, 4 };

    private JComboBox handSizeCombo;

    private JComboBox playerCountCombo;

    private JComboBox invisibleCardCombo;

    private JComboBox burnCardCombo;

    private JComboBox resetCardCombo;

    private JComboBox underCardCombo;

    private JComboBox reverseCardCombo;

    private JCheckBox swapCheckBox;

    private JButton cancelButton;

    private JButton okButton;

    private List<JComboBox> combos = new ArrayList<JComboBox>();

    private GameRules rules = null;

    private boolean readOnly = false;

    public RulesDialog(SHFrame shFrame, GameRules rules) {
        super(shFrame, "Game Rules", true);
        readOnly = false;
        init();
        centreDialogOnGame(shFrame);
        setRules(rules);
    }

    public RulesDialog(SHFrame shFrame, GameRules rules, boolean readOnly) {
        super(shFrame, "Game Rules", true);
        this.readOnly = readOnly;
        init();
        centreDialogOnGame(shFrame);
        setRules(rules);
        if (readOnly) setReadOnly();
    }

    private void centreDialogOnGame(SHFrame shFrame) {
        if (shFrame == null) {
            setLocationRelativeTo(shFrame);
            return;
        }
        int x = shFrame.getX();
        int y = shFrame.getY();
        x = x + (shFrame.getGc().boardWidth / 2) - (this.getWidth() / 2);
        y = y + (shFrame.getGc().boardHeight / 2) - (this.getHeight() / 2);
        this.setLocation(x, y);
    }

    private void init() {
        JPanel panel = new JPanel();
        panel.setLayout(new GridBagLayout());
        GridBagConstraints c = new GridBagConstraints();
        c.gridx = 0;
        c.gridy = 0;
        c.weightx = 1;
        c.weighty = 0;
        c.fill = GridBagConstraints.BOTH;
        c.insets = new Insets(5, 5, 5, 5);
        c.anchor = GridBagConstraints.NORTHWEST;
        panel.add(new JLabel("Hand Size:"), c);
        c.gridx++;
        handSizeCombo = createCombo(HAND_SIZES);
        panel.add(handSizeCombo, c);
        c.gridx++;
        panel.add(new JLabel("No of Players:"), c);
        c.gridx++;
        playerCountCombo = createCombo(PLAYER_COUNT);
        panel.add(playerCountCombo, c);
        c.gridy++;
        c.gridx = 0;
        panel.add(new JLabel("Burn Card:"), c);
        c.gridx++;
        burnCardCombo = createCardCombo();
        panel.add(burnCardCombo, c);
        c.gridx++;
        panel.add(new JLabel("Reset Card:"), c);
        c.gridx++;
        resetCardCombo = createCardCombo();
        panel.add(resetCardCombo, c);
        c.gridy++;
        c.gridx = 0;
        panel.add(new JLabel("Invisible Card:"), c);
        c.gridx++;
        invisibleCardCombo = createCardCombo();
        panel.add(invisibleCardCombo, c);
        c.gridx++;
        panel.add(new JLabel("Under Card:"), c);
        c.gridx++;
        underCardCombo = createCardCombo();
        panel.add(underCardCombo, c);
        c.gridy++;
        c.gridx = 0;
        panel.add(new JLabel("Reverse Card:"), c);
        c.gridx++;
        reverseCardCombo = createCardCombo();
        panel.add(reverseCardCombo, c);
        c.gridx++;
        c.gridwidth = GridBagConstraints.REMAINDER;
        swapCheckBox = new JCheckBox("Allow Card Swap");
        panel.add(swapCheckBox, c);
        c.gridy++;
        c.gridx = 0;
        c.gridy++;
        c.weighty = 1;
        panel.add(new JLabel(" "), c);
        c.gridy++;
        c.weighty = 0;
        panel.add(getButtonPanel(), c);
        panel.setBorder(BorderFactory.createTitledBorder(BorderFactory.createLineBorder(Color.BLACK), "Rules"));
        this.setContentPane(panel);
        this.pack();
    }

    public void setRules(GameRules rules) {
        handSizeCombo.setSelectedItem(rules.getHandSize());
        playerCountCombo.setSelectedItem(rules.getPlayerCount());
        invisibleCardCombo.setSelectedItem(new ListCardWrapper(rules.getInvisibleCard()));
        burnCardCombo.setSelectedItem(new ListCardWrapper(rules.getBurnCard()));
        resetCardCombo.setSelectedItem(new ListCardWrapper(rules.getResetCard()));
        underCardCombo.setSelectedItem(new ListCardWrapper(rules.getUnderCard()));
        reverseCardCombo.setSelectedItem(new ListCardWrapper(rules.getReverseCard()));
        swapCheckBox.setSelected(rules.isAllowSwapAtStart());
    }

    private void setReadOnly() {
        handSizeCombo.setEnabled(false);
        playerCountCombo.setEnabled(false);
        invisibleCardCombo.setEnabled(false);
        burnCardCombo.setEnabled(false);
        resetCardCombo.setEnabled(false);
        underCardCombo.setEnabled(false);
        reverseCardCombo.setEnabled(false);
        swapCheckBox.setEnabled(false);
    }

    private GameRules getRulesFromDialog() {
        GameRules rules = new GameRules();
        rules.setHandSize((Integer) handSizeCombo.getSelectedItem());
        rules.setPlayerCount((Integer) playerCountCombo.getSelectedItem());
        rules.setInvisibleCard(((ListCardWrapper) invisibleCardCombo.getSelectedItem()).getCard());
        rules.setBurnCard(((ListCardWrapper) burnCardCombo.getSelectedItem()).getCard());
        rules.setResetCard(((ListCardWrapper) resetCardCombo.getSelectedItem()).getCard());
        rules.setUnderCard(((ListCardWrapper) underCardCombo.getSelectedItem()).getCard());
        rules.setReverseCard(((ListCardWrapper) reverseCardCombo.getSelectedItem()).getCard());
        rules.setAllowSwapAtStart(swapCheckBox.isSelected());
        PropertiesUtil.INSTANCE.setInt(ShitheadConstants.PLAYER_COUNT, rules.getPlayerCount());
        PropertiesUtil.INSTANCE.setInt(ShitheadConstants.HAND_SIZE, rules.getHandSize());
        PropertiesUtil.INSTANCE.setCard(ShitheadConstants.INVISIBLE_CARD, rules.getInvisibleCard());
        PropertiesUtil.INSTANCE.setCard(ShitheadConstants.BURN_CARD, rules.getBurnCard());
        PropertiesUtil.INSTANCE.setCard(ShitheadConstants.RESET_CARD, rules.getResetCard());
        PropertiesUtil.INSTANCE.setCard(ShitheadConstants.UNDER_CARD, rules.getUnderCard());
        PropertiesUtil.INSTANCE.setCard(ShitheadConstants.REVERSE_CARD, rules.getReverseCard());
        PropertiesUtil.INSTANCE.setBoolean(ShitheadConstants.SWAP_AT_START, rules.isAllowSwapAtStart());
        return rules;
    }

    private boolean isValidInput() {
        if (handSizeCombo.getSelectedItem() == null) {
            JOptionPane.showMessageDialog(this, "Must select a hand size");
            return false;
        }
        if (playerCountCombo.getSelectedItem() == null) {
            JOptionPane.showMessageDialog(this, "Must select a player count");
            return false;
        }
        Set<CardFace> faces = new HashSet<CardFace>();
        for (JComboBox combo : combos) {
            CardFace face = ((ListCardWrapper) combo.getSelectedItem()).getCard();
            if (face == null) continue;
            if (faces.contains(face)) {
                JOptionPane.showMessageDialog(this, face.getName() + " has been used for two rules cards");
                return false;
            } else {
                faces.add(face);
            }
        }
        return true;
    }

    private JPanel getButtonPanel() {
        JPanel panel = new JPanel();
        panel.setLayout(new GridBagLayout());
        GridBagConstraints c = new GridBagConstraints();
        c.gridx = 0;
        c.gridy = 0;
        c.fill = GridBagConstraints.BOTH;
        c.weightx = 1;
        c.weighty = 0;
        c.weightx = 0;
        c.weighty = 0;
        cancelButton = new JButton("Cancel");
        cancelButton.setMnemonic('C');
        cancelButton.setPreferredSize(ShitheadConstants.BUTTON_SIZE);
        cancelButton.addActionListener(this);
        if (!readOnly) {
            panel.add(cancelButton, c);
        } else {
            panel.add(new JLabel(" "), c);
        }
        c.gridx++;
        c.weightx = 1;
        panel.add(new JLabel(" "), c);
        c.gridx++;
        c.weightx = 0;
        c.anchor = GridBagConstraints.NORTHEAST;
        okButton = new JButton("OK");
        okButton.setMnemonic('O');
        okButton.setPreferredSize(ShitheadConstants.BUTTON_SIZE);
        okButton.addActionListener(this);
        panel.add(okButton, c);
        return panel;
    }

    private JComboBox createCardCombo() {
        ListCardWrapper[] wrappers = new ListCardWrapper[CardFace.values().length + 1];
        int count = 0;
        wrappers[count] = new ListCardWrapper(null);
        for (CardFace face : CardFace.values()) {
            count++;
            wrappers[count] = new ListCardWrapper(face);
        }
        JComboBox combo = createCombo(wrappers);
        combo.addActionListener(this);
        combos.add(combo);
        return combo;
    }

    private <T> JComboBox createCombo(T[] items) {
        JComboBox combo = new JComboBox(items);
        combo.setPreferredSize(JCOMBO_SIZE);
        return combo;
    }

    @Override
    public void actionPerformed(ActionEvent event) {
        Object source = event.getSource();
        if (source == cancelButton) {
            rules = null;
            setVisible(false);
        } else if (source == okButton) {
            if (isValidInput()) {
                rules = getRulesFromDialog();
                setVisible(false);
            }
        } else if (combos.contains(source)) {
            CardFace face = ((ListCardWrapper) ((JComboBox) source).getSelectedItem()).getCard();
            ;
            if (face == null) return;
            for (JComboBox combo : combos) {
                if (combo == source) continue;
                CardFace comboFace = ((ListCardWrapper) combo.getSelectedItem()).getCard();
                if (comboFace == face) {
                    combo.setSelectedItem(new ListCardWrapper(null));
                }
            }
        }
    }

    public GameRules getRules() {
        return rules;
    }

    private class ListCardWrapper {

        private CardFace card;

        public ListCardWrapper(CardFace card) {
            super();
            this.card = card;
        }

        public String toString() {
            if (card == null) return "  ---  "; else return card.getName();
        }

        public CardFace getCard() {
            return card;
        }

        @Override
        public boolean equals(Object o) {
            if (!(o instanceof ListCardWrapper)) {
                return false;
            }
            ListCardWrapper other = (ListCardWrapper) o;
            return other.getCard() == this.getCard();
        }
    }
}
