package com.hsbc.hbfr.ccf.at.logreader.ui;

import com.hsbc.hbfr.ccf.at.logreader.predicate.NegatableDateAfterPredicate;
import com.hsbc.hbfr.ccf.at.logreader.predicate.NegatableStringLikePredicate;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;

public class DateFilterInputPanel extends JPanel implements FilterInput {

    private class EventListener implements ActionListener, ItemListener {

        public void actionPerformed(ActionEvent e) {
            if (e.getActionCommand().equals("comboBoxChanged")) {
                JComboBox source = (JComboBox) e.getSource();
                DefaultComboBoxModel model = (DefaultComboBoxModel) source.getModel();
                Object selectedItem = source.getSelectedItem();
                if (model.getIndexOf(selectedItem) < 0) {
                    model.addElement(selectedItem);
                }
                if (source == filtreCb) {
                    predicate.fromString(model.getSelectedItem().toString());
                    fireActionEvent(new ActionEvent(this, 0, "filter changed"));
                } else if (source == patternCb) {
                    predicate.setDatePattern(patternCb.getSelectedItem().toString());
                }
            } else if (e.getActionCommand().equals("comboBoxEdited")) {
            }
        }

        public void itemStateChanged(ItemEvent e) {
            predicate.setNegated(negateBn.isSelected());
            fireActionEvent(new ActionEvent(this, 0, "filter changed"));
        }
    }

    private static final int historyCount = 10;

    private JLabel titleLb;

    private JComboBox filtreCb;

    private JComboBox patternCb;

    private MaxSizeComboBoxModel modelFiltre;

    private JToggleButton negateBn;

    private MaxSizeComboBoxModel modelPattern;

    private ActionListener listeners = null;

    private final NegatableDateAfterPredicate predicate;

    private final EventListener listener = new EventListener();

    public DateFilterInputPanel(NegatableDateAfterPredicate aPredicate, String title, int nbMaxChars) {
        this(aPredicate, title);
        setMaximumFilterLength(nbMaxChars);
    }

    public DateFilterInputPanel(NegatableDateAfterPredicate aPredicate, String title) {
        super(new BorderLayout());
        predicate = aPredicate;
        titleLb = new JLabel(title + ", pattern");
        modelFiltre = new MaxSizeComboBoxModel(historyCount);
        modelPattern = new MaxSizeComboBoxModel(historyCount);
        negateBn = new JToggleButton("!");
        negateBn.setMargin(new Insets(1, 1, 2, 1));
        patternCb = new JComboBox(modelPattern);
        patternCb.setEditable(true);
        filtreCb = new JComboBox(modelFiltre);
        filtreCb.setEditable(true);
        JPanel p = new JPanel();
        p.setLayout(new BoxLayout(p, BoxLayout.X_AXIS));
        p.add(titleLb);
        p.add(Box.createHorizontalGlue());
        p.add(negateBn);
        p.add(patternCb);
        add(p, BorderLayout.NORTH);
        add(filtreCb, BorderLayout.SOUTH);
        initEvents();
    }

    private void initEvents() {
        filtreCb.addActionListener(listener);
        patternCb.addActionListener(listener);
        negateBn.addItemListener(listener);
    }

    public String getText() {
        Object selectedItem = filtreCb.getSelectedItem();
        return (selectedItem == null) ? "" : selectedItem.toString();
    }

    public String getPattern() {
        Object selectedItem = patternCb.getSelectedItem();
        return (selectedItem == null) ? "" : selectedItem.toString();
    }

    public void setPattern(String pattern) {
        patternCb.setSelectedItem(pattern);
    }

    public void setText(String s) {
        filtreCb.setSelectedItem(s);
    }

    public void addActionListener(ActionListener l) {
        listeners = AWTEventMulticaster.add(listeners, l);
    }

    public void removeActionListener(ActionListener l) {
        listeners = AWTEventMulticaster.remove(listeners, l);
    }

    protected void fireActionEvent(ActionEvent event) {
        if (listeners != null) {
            listeners.actionPerformed(event);
        }
    }

    public String[] getRecentTexts() {
        String[] options = new String[filtreCb.getModel().getSize()];
        for (int i = 0, max = options.length; i < max; i++) {
            options[i] = filtreCb.getModel().getElementAt(i).toString();
        }
        return options;
    }

    public static void main(String[] args) {
        JFrame f = new JFrame();
        f.setSize(200, 200);
        f.getContentPane().setLayout(new BorderLayout());
        StringFilterInputPanel categoryFilter = new StringFilterInputPanel(new NegatableStringLikePredicate(), "categorie");
        StringFilterInputPanel threadFilter = new StringFilterInputPanel(new NegatableStringLikePredicate(), "thread");
        StringFilterInputPanel priorityFilter = new StringFilterInputPanel(new NegatableStringLikePredicate(), "priorit�");
        DateFilterInputPanel dateFilter = new DateFilterInputPanel(new NegatableDateAfterPredicate(), "date");
        StringFilterInputPanel messageFilter = new StringFilterInputPanel(new NegatableStringLikePredicate(), "message");
        ActionListener l = new ActionListener() {

            public void actionPerformed(ActionEvent e) {
                FilterInput filterInput = ((FilterInput) e.getSource());
                System.err.println("filter " + filterInput.getText());
            }
        };
        categoryFilter.addActionListener(l);
        threadFilter.addActionListener(l);
        priorityFilter.addActionListener(l);
        dateFilter.addActionListener(l);
        messageFilter.addActionListener(l);
        JPanel panelFiltres = new JPanel(new GridBagLayout());
        GridBagConstraints c = new GridBagConstraints(0, 0, 1, 1, 1, 1, GridBagConstraints.CENTER, GridBagConstraints.BOTH, new Insets(0, 2, 0, 2), 0, 0);
        panelFiltres.add(categoryFilter, c);
        c.gridx++;
        panelFiltres.add(threadFilter, c);
        c.gridx++;
        panelFiltres.add(priorityFilter, c);
        c.gridx++;
        panelFiltres.add(dateFilter, c);
        c.gridx++;
        panelFiltres.add(messageFilter, c);
        f.getContentPane().add(panelFiltres, BorderLayout.NORTH);
        f.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
        f.setVisible(true);
    }

    private void setMaximumFilterLength(int nbChars) {
        StringBuffer template = new StringBuffer(nbChars);
        for (int i = 0; i < nbChars; i++) {
            template.append("a");
        }
        filtreCb.setModel(new DefaultComboBoxModel(new String[] { template.toString() }));
        Dimension size = filtreCb.getPreferredSize();
        filtreCb.setModel(modelFiltre);
        filtreCb.setMaximumSize(size);
    }
}
