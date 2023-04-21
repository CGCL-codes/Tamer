package net.bpfurtado.tas.builder.combat;

import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.lang.reflect.Method;
import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.ListCellRenderer;
import javax.swing.ListSelectionModel;
import javax.swing.SpringLayout;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import net.bpfurtado.tas.AdventureException;
import net.bpfurtado.tas.builder.AdventureNeedsSavingController;
import net.bpfurtado.tas.builder.RandomNPCGenerator;
import net.bpfurtado.tas.model.combat.Combat;
import net.bpfurtado.tas.model.combat.CombatType;
import net.bpfurtado.tas.model.combat.Fighter;
import net.bpfurtado.tas.view.SpringUtilities;
import net.bpfurtado.tas.view.Util;
import org.apache.log4j.Logger;

public class BuilderCombatPanelManager {

    @SuppressWarnings("unused")
    private static Logger logger = Logger.getLogger(BuilderCombatPanelManager.class);

    private static final Dimension DEFAULT_VERTICAL_SPACE = new Dimension(0, 7);

    private static RandomNPCGenerator npcGen = new RandomNPCGenerator();

    private JPanel panel;

    private JTextField[] tfs;

    private JList list;

    private JButton upBt = new JButton(Util.getImage("Up24.gif"));

    private JButton downBt = new JButton(Util.getImage("Down24.gif"));

    private JButton cloneBt = new JButton("Clone");

    private JButton deleteBt = new JButton("Delete");

    private AdventureNeedsSavingController adv;

    private Combat combat;

    public BuilderCombatPanelManager(AdventureNeedsSavingController adv, Combat combat) {
        this.adv = adv;
        this.combat = combat;
        panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.PAGE_AXIS));
        panel.setBorder(BorderFactory.createEmptyBorder(5, 8, 5, 8));
        panel.add(Box.createRigidArea(DEFAULT_VERTICAL_SPACE));
        panel.add(createCombo());
        panel.add(createList());
        panel.add(createToolbar());
        JPanel bottonPn = createBottonPanel();
        panel.add(bottonPn);
    }

    private JPanel createBottonPanel() {
        JPanel p = new JPanel();
        p.setAlignmentX(Component.LEFT_ALIGNMENT);
        p.setLayout(new BoxLayout(p, BoxLayout.LINE_AXIS));
        p.add(createEditPn());
        JPanel helpPn = new JPanel();
        helpPn.setLayout(new BoxLayout(helpPn, BoxLayout.PAGE_AXIS));
        helpPn.setBorder(BorderFactory.createTitledBorder("Help"));
        JLabel l = new JLabel("Why only one Path?");
        l.setAlignmentX(Component.LEFT_ALIGNMENT);
        helpPn.add(l);
        helpPn.add(Box.createRigidArea(new Dimension(0, 3)));
        JTextArea ta = new JTextArea("Combat scenes can have only one path. " + "In case of victory that's the scene the player will be taken to.");
        ta.setLineWrap(true);
        ta.setWrapStyleWord(true);
        ta.setAlignmentX(Component.LEFT_ALIGNMENT);
        ta.setBackground(l.getBackground());
        helpPn.add(ta);
        p.add(helpPn);
        return p;
    }

    private JPanel createToolbar() {
        JPanel tb = new JPanel(new FlowLayout(FlowLayout.LEFT));
        tb.setMaximumSize(new Dimension(240, 40));
        tb.setAlignmentX(Component.LEFT_ALIGNMENT);
        JButton newBt = new JButton("New");
        newBt.setMnemonic('e');
        newBt.setToolTipText("Creates a new Enemy");
        newBt.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent e) {
                addNewFighterAction(npcGen.generateFighter());
            }
        });
        tb.add(newBt);
        cloneBt.setMnemonic('c');
        cloneBt.setEnabled(false);
        cloneBt.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent e) {
                Fighter enemy = (Fighter) list.getSelectedValue();
                Fighter clone = enemy.createCopy();
                combat.add(clone);
                int idx = list.getSelectedIndex();
                DefaultListModel m = (DefaultListModel) list.getModel();
                m.add(idx, clone);
                list.setSelectedIndex(idx);
                adv.markAsDirty();
            }
        });
        tb.add(cloneBt);
        tb.add(Box.createRigidArea(new Dimension(5, 0)));
        deleteBt.setMnemonic('d');
        deleteBt.setEnabled(false);
        deleteBt.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent e) {
                Fighter enemy = (Fighter) list.getSelectedValue();
                combat.remove(enemy);
                int deletedIdx = list.getSelectedIndex();
                DefaultListModel m = ((DefaultListModel) list.getModel());
                m.remove(deletedIdx);
                if (list.getModel().getSize() > 0) {
                    list.setSelectedIndex(deletedIdx == m.getSize() ? deletedIdx - 1 : deletedIdx);
                }
                adv.markAsDirty();
                list.repaint();
            }
        });
        tb.add(deleteBt);
        return tb;
    }

    private JPanel createEditPn() {
        Font font = new Font("Franklin Gothic Medium Cond", 0, 18);
        JPanel editPn = new JPanel(new SpringLayout());
        editPn.setBorder(BorderFactory.createTitledBorder("Enemy"));
        Dimension d = new Dimension(240, 160);
        editPn.setMaximumSize(d);
        editPn.setMinimumSize(d);
        editPn.setAlignmentX(Component.LEFT_ALIGNMENT);
        final String[] labels = { "Name: ", "Skill: ", "Stamina: ", "Damage: " };
        tfs = new JTextField[labels.length];
        for (int i = 0; i < labels.length; i++) {
            JLabel l = new JLabel(labels[i], JLabel.TRAILING);
            editPn.add(l);
            JTextField tf = new JTextField(i == 0 ? 10 : 4);
            tf.setFont(font);
            tf.setEnabled(false);
            final int idx = i;
            tf.getDocument().addDocumentListener(new DocumentListener() {

                public void changedUpdate(DocumentEvent e) {
                }

                public void insertUpdate(DocumentEvent e) {
                    if (list.isSelectionEmpty()) {
                        return;
                    }
                    Fighter f = ((Fighter) list.getSelectedValue());
                    String propertyName = labels[idx].substring(0, labels[idx].length() - 2);
                    try {
                        String text = e.getDocument().getText(0, e.getDocument().getLength());
                        Method m = null;
                        if (idx == 0) {
                            m = Fighter.class.getMethod("set" + propertyName, new Class[] { String.class });
                            m.invoke(f, text);
                        } else {
                            m = Fighter.class.getMethod("set" + propertyName, new Class[] { Integer.class });
                            boolean failed = false;
                            try {
                                int value = Integer.parseInt(text);
                                if (value < 1) {
                                    failed = true;
                                } else {
                                    m.invoke(f, text.trim().length() == 0 ? 0 : value);
                                }
                            } catch (NumberFormatException nfe) {
                                failed = true;
                            }
                            tfs[idx].setForeground(failed ? Color.red : Color.gray);
                        }
                        list.repaint();
                    } catch (Exception e1) {
                        throw new AdventureException(e1);
                    }
                }

                public void removeUpdate(DocumentEvent e) {
                    insertUpdate(e);
                }
            });
            l.setLabelFor(tf);
            editPn.add(tf);
            tfs[i] = tf;
        }
        SpringUtilities.makeCompactGrid(editPn, labels.length, 2, 6, 6, 6, 6);
        return editPn;
    }

    private JComboBox createCombo() {
        final JComboBox cb = new JComboBox(new Object[] { "One at a time", "All at the same time" });
        Dimension d = new Dimension(240, 30);
        cb.setMaximumSize(d);
        cb.setAlignmentX(Component.LEFT_ALIGNMENT);
        cb.setBorder(BorderFactory.createTitledBorder("Combat rounds order: "));
        cb.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent e) {
                String selectedItem = (String) cb.getSelectedItem();
                if (selectedItem == "One at a time") {
                    combat.setType(CombatType.oneAtATime);
                } else {
                    combat.setType(CombatType.allAtTheSameTime);
                }
                adv.markAsDirty();
            }
        });
        return cb;
    }

    private JPanel createList() {
        panel.add(Box.createRigidArea(DEFAULT_VERTICAL_SPACE));
        JPanel p = new JPanel();
        p.setBorder(BorderFactory.createTitledBorder("Enemies: "));
        p.setLayout(new BoxLayout(p, BoxLayout.LINE_AXIS));
        p.setAlignmentX(Component.LEFT_ALIGNMENT);
        p.add(Box.createRigidArea(new Dimension(4, 0)));
        list = new JList();
        Dimension d = new Dimension(240, 140);
        DefaultListModel m = new DefaultListModel();
        for (Fighter f : combat.getEnemies()) {
            m.addElement(f);
        }
        list.setModel(m);
        list.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        list.setCellRenderer(new FighterCellRenderer());
        JScrollPane sp = new JScrollPane(list);
        sp.setMaximumSize(d);
        sp.setAlignmentX(Component.LEFT_ALIGNMENT);
        list.addListSelectionListener(new ListSelectionListener() {

            public void valueChanged(ListSelectionEvent e) {
                boolean enabled = !list.isSelectionEmpty();
                cloneBt.setEnabled(enabled);
                deleteBt.setEnabled(enabled);
                upBt.setEnabled(enabled);
                downBt.setEnabled(enabled);
                if (list.isSelectionEmpty()) {
                    for (JTextField tf : tfs) {
                        tf.setEnabled(false);
                        tf.setText("");
                    }
                    return;
                }
                for (JTextField tf : tfs) {
                    tf.setEnabled(true);
                }
                Fighter f = ((Fighter) list.getSelectedValue());
                tfs[0].setText(f.getName());
                tfs[1].setText(String.valueOf(f.getCombatSkillLevel()));
                tfs[2].setText(String.valueOf(f.getStamina()));
                tfs[3].setText(String.valueOf(f.getDamage()));
            }
        });
        p.add(sp);
        p.add(Box.createRigidArea(new Dimension(4, 0)));
        p.add(createListLateralToolbar());
        p.add(Box.createRigidArea(new Dimension(4, 0)));
        return p;
    }

    private JPanel createListLateralToolbar() {
        JPanel buttonsPanel = new JPanel();
        buttonsPanel.setLayout(new BoxLayout(buttonsPanel, BoxLayout.PAGE_AXIS));
        upBt.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent e) {
                upOrDownListElement(-1);
            }
        });
        downBt.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent e) {
                upOrDownListElement(1);
            }
        });
        buttonsPanel.add(upBt);
        buttonsPanel.add(Box.createRigidArea(new Dimension(0, 5)));
        buttonsPanel.add(downBt);
        return buttonsPanel;
    }

    private void upOrDownListElement(int upOrDown) {
        int i = list.getSelectedIndex();
        if (i == 0 && upOrDown == -1) {
            return;
        } else if (i == list.getModel().getSize() - 1 && upOrDown == 1) {
            return;
        }
        Object v = list.getSelectedValue();
        DefaultListModel m = (DefaultListModel) list.getModel();
        Object vOld = m.get(i + upOrDown);
        m.set(i + upOrDown, v);
        m.set(i, vOld);
        list.setSelectedIndex(i + upOrDown);
        adv.markAsDirty();
    }

    public JPanel getPanel() {
        return panel;
    }

    public void addNewFighterAction(Fighter fighter) {
        combat.add(fighter);
        DefaultListModel m = (DefaultListModel) list.getModel();
        m.addElement(fighter);
        int idx = m.getSize() - 1;
        list.setSelectedIndex(idx);
        list.ensureIndexIsVisible(idx);
        adv.markAsDirty();
    }
}

class FighterCellRenderer extends JLabel implements ListCellRenderer {

    private static final long serialVersionUID = 8894768471326962320L;

    @SuppressWarnings("unused")
    private static Logger logger = Logger.getLogger(FighterCellRenderer.class);

    public FighterCellRenderer() {
        setOpaque(true);
    }

    public Component getListCellRendererComponent(JList list, Object value, int index, boolean isSelected, boolean cellHasFocus) {
        Fighter f = (Fighter) value;
        setText(f.getName() + ", sk[" + f.getCombatSkillLevel() + "] st[" + f.getStamina() + "] dm[" + f.getDamage() + "]");
        setBackground(isSelected ? Util.oceanColor : Color.white);
        setForeground(Color.black);
        return this;
    }
}
