package net.sourceforge.glsof.common.main;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import static net.sourceforge.glsof.common.i18n.Messages.NLS;

public abstract class AbstractFilterBar {

    private static final String FILTER_BUTTON = "/icons/filter.png";

    private JLabel _label;

    protected JTextField _comboFilter;

    protected JComboBox _comboColumnsList;

    protected JCheckBox _buttonCase;

    private JButton _buttonFilter;

    protected abstract void search();

    protected abstract void initColumnsList();

    protected AbstractFilterBar(JPanel panel, int x, int y) {
        _label = new JLabel(NLS("Filter") + ":");
        panel.add(_label, createConstraints(x, y, GridBagConstraints.NONE, 0.0));
        _comboFilter = new JTextField(25);
        panel.add(_comboFilter, createConstraints(++x, y, GridBagConstraints.HORIZONTAL, 1.0));
        initColumnsList();
        panel.add(_comboColumnsList, createConstraints(++x, y, GridBagConstraints.NONE, 0.0));
        _buttonCase = new JCheckBox(NLS("Case_sensitive"));
        panel.add(_buttonCase, createConstraints(++x, y, GridBagConstraints.NONE, 0.0));
        _buttonFilter = new JButton(new ImageIcon(getClass().getResource(FILTER_BUTTON)));
        _buttonFilter.setToolTipText(NLS("Filter"));
        panel.add(_buttonFilter, createConstraints(++x, y, GridBagConstraints.HORIZONTAL, 0.0));
        _comboFilter.addKeyListener(new KeyAdapter() {

            public void keyPressed(KeyEvent event) {
                if (event.getKeyCode() == KeyEvent.VK_ENTER) search();
            }
        });
        _buttonFilter.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent actionEvent) {
                search();
            }
        });
        _buttonCase.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent actionEvent) {
                search();
            }
        });
    }

    private GridBagConstraints createConstraints(final int x, final int y, final int fill, final double weightx) {
        GridBagConstraints c = new GridBagConstraints();
        c.gridx = x;
        c.gridy = y;
        c.fill = fill;
        c.weightx = weightx;
        c.insets = new Insets(5, 5, 5, 5);
        return c;
    }

    protected void enableWidgets(final boolean enable) {
        _label.setEnabled(enable);
        _comboFilter.setEnabled(enable);
        _comboColumnsList.setEnabled(enable);
        _buttonCase.setEnabled(enable);
        _buttonFilter.setEnabled(enable);
    }
}
