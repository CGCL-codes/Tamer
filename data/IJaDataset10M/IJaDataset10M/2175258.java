package com.clarkmultimedia.trustmaster.view;

import javax.swing.BorderFactory;
import javax.swing.JLabel;
import javax.swing.JPanel;

class TitlePanel extends JPanel {

    private static final long serialVersionUID = -3427975693470118442L;

    public TitlePanel() {
        initComponents();
    }

    private void initComponents() {
        super.add(getTitleLabel());
        super.setBorder(BorderFactory.createEmptyBorder());
    }

    private JLabel getTitleLabel() {
        if (titleLabel == null) {
            titleLabel = new JLabel(IconFactory.getTitleIcon());
        }
        return titleLabel;
    }

    private JLabel titleLabel = null;
}
