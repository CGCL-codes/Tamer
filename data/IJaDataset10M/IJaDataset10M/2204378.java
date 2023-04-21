package emailvis.hci.controlPanel;

import java.awt.BorderLayout;
import java.awt.Checkbox;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.Vector;
import javax.swing.Box;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.table.AbstractTableModel;
import javax.swing.table.DefaultTableModel;
import emailvis.data.Mbox;
import sun.awt.RepaintArea;
import java.awt.event.MouseAdapter;

public class AnonymizationContactControlPanel extends ControlPanel {

    /**
	 * 
	 */
    private static final long serialVersionUID = -8832669181537025223L;

    private JButton group;

    private JButton degroup;

    private JButton anonymize;

    private JButton cancel;

    private Checkbox aliasChB;

    private Checkbox mailingChB;

    private Checkbox spamChB;

    private Checkbox filterChB;

    private Box buttons;

    private JTable contactTable;

    private PanelAnonymize aliasPanel;

    private PanelAnonymize mailingPanel;

    private PanelAnonymize spamPanel;

    private PanelAnonymize filterPanel;

    private Vector<String> alias;

    private Vector<String> mailing;

    private Vector<String> spam;

    private Vector<String> filter;

    private JPanel checkBoxPanel;

    private JPanel thePanel;

    private JLabel ok;

    private Vector<PanelAnonymize> vectAliasPanel;

    private Vector<PanelAnonymize> vectMailingPanel;

    private Vector<PanelAnonymize> vectSpamPanel;

    private Vector<PanelAnonymize> vectFilterPanel;

    private JButton validAnon;

    private JButton cancelAnon;

    private Mbox theMbox;

    private JFrame fenC;

    public AnonymizationContactControlPanel(Mbox laMb) {
        setLayout(new BorderLayout());
        theMbox = laMb;
        contactTable = new JTable(new CheckBoxRendererModel());
        for (int i = 0; i < theMbox.getListContact().size(); i++) {
            Object[] line = { theMbox.getListContact().elementAt(i).getEmail(), new Boolean(false), new Boolean(false), new Boolean(false), new Boolean(false) };
            ((CheckBoxRendererModel) contactTable.getModel()).addRow(line);
        }
        contactTable.setPreferredScrollableViewportSize(new Dimension(500, 70));
        add(new JScrollPane(contactTable), BorderLayout.CENTER);
        aliasChB = new Checkbox("All");
        aliasChB.setState(false);
        aliasChB.addItemListener(new ItemListener() {

            public void itemStateChanged(ItemEvent arg0) {
                for (int i = 0; i < contactTable.getRowCount(); i++) if (aliasChB.getState()) {
                    contactTable.setValueAt(new Boolean(true), i, 1);
                } else {
                    contactTable.setValueAt(new Boolean(false), i, 1);
                }
            }
        });
        mailingChB = new Checkbox("All");
        mailingChB.setState(false);
        mailingChB.addItemListener(new ItemListener() {

            public void itemStateChanged(ItemEvent arg0) {
                for (int i = 0; i < contactTable.getRowCount(); i++) if (mailingChB.getState()) {
                    contactTable.setValueAt(new Boolean(true), i, 2);
                } else {
                    contactTable.setValueAt(new Boolean(false), i, 2);
                }
            }
        });
        spamChB = new Checkbox("All");
        spamChB.setState(false);
        spamChB.addItemListener(new ItemListener() {

            public void itemStateChanged(ItemEvent arg0) {
                for (int i = 0; i < contactTable.getRowCount(); i++) if (spamChB.getState()) {
                    contactTable.setValueAt(new Boolean(true), i, 3);
                } else {
                    contactTable.setValueAt(new Boolean(false), i, 3);
                }
            }
        });
        filterChB = new Checkbox("All");
        filterChB.setState(false);
        filterChB.addItemListener(new ItemListener() {

            public void itemStateChanged(ItemEvent arg0) {
                for (int i = 0; i < contactTable.getRowCount(); i++) if (filterChB.getState()) {
                    contactTable.setValueAt(new Boolean(true), i, 4);
                } else {
                    contactTable.setValueAt(new Boolean(false), i, 4);
                }
            }
        });
        checkBoxPanel = new JPanel();
        JPanel p0 = new JPanel();
        p0.setPreferredSize(new Dimension(200, 10));
        checkBoxPanel.add(p0);
        checkBoxPanel.add(aliasChB);
        JPanel p1 = new JPanel();
        p1.setPreferredSize(new Dimension(130, 10));
        checkBoxPanel.add(p1);
        checkBoxPanel.add(mailingChB);
        JPanel p2 = new JPanel();
        p2.setPreferredSize(new Dimension(130, 10));
        checkBoxPanel.add(p2);
        checkBoxPanel.add(spamChB);
        JPanel p3 = new JPanel();
        p3.setPreferredSize(new Dimension(130, 10));
        checkBoxPanel.add(p3);
        checkBoxPanel.add(filterChB);
        add(checkBoxPanel, BorderLayout.NORTH);
        group = new JButton("Group");
        group.addActionListener(this);
        degroup = new JButton("Degroup");
        degroup.addActionListener(this);
        anonymize = new JButton("Anonymize");
        anonymize.addActionListener(this);
        cancel = new JButton("Cancel");
        cancel.addActionListener(this);
        validAnon = new JButton("validate");
        cancelAnon = new JButton("cancel");
        buttons = Box.createHorizontalBox();
        buttons.add(group);
        buttons.add(degroup);
        buttons.add(anonymize);
        buttons.add(cancel);
        add(buttons, BorderLayout.SOUTH);
        ok = new JLabel("groupage effectu� !");
        buttons.add(ok);
        ok.setVisible(false);
        alias = new Vector<String>();
        mailing = new Vector<String>();
        spam = new Vector<String>();
        filter = new Vector<String>();
        vectAliasPanel = new Vector<PanelAnonymize>();
        vectMailingPanel = new Vector<PanelAnonymize>();
        vectSpamPanel = new Vector<PanelAnonymize>();
        vectFilterPanel = new Vector<PanelAnonymize>();
    }

    class CheckBoxRendererModel extends AbstractTableModel {

        /**
		 * 
		 */
        private static final long serialVersionUID = 14L;

        private String[] columnNames = { "Contacts", "Alias", "Mailing list", "Spam", "Filter" };

        private Vector<Object[]> data = new Vector<Object[]>();

        public int getColumnCount() {
            return columnNames.length;
        }

        public int getRowCount() {
            return data.size();
        }

        public String getColumnName(int col) {
            return columnNames[col];
        }

        public Object getValueAt(int row, int col) {
            return data.get(row)[col];
        }

        public int addRow(Object[] row) {
            data.add(row);
            return data.size() - 1;
        }

        public Class getColumnClass(int c) {
            return getValueAt(0, c).getClass();
        }

        public boolean isCellEditable(int row, int col) {
            if (col < 1) {
                return false;
            } else {
                return true;
            }
        }

        public void setValueAt(Object value, int row, int col) {
            if (row > data.size() || col > columnNames.length) return;
            Object[] o = data.get(row);
            o[col] = value;
            fireTableCellUpdated(row, col);
        }
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == group) {
            thePanel = new JPanel();
            alias.removeAllElements();
            mailing.removeAllElements();
            spam.removeAllElements();
            filter.removeAllElements();
            for (int i = 1; i < contactTable.getColumnCount(); i++) {
                for (int j = 0; j < contactTable.getRowCount(); j++) {
                    switch(i) {
                        case 1:
                            if (((Boolean) contactTable.getValueAt(j, i)).booleanValue() == Boolean.TRUE) {
                                alias.addElement((contactTable.getModel().getValueAt(j, 0)).toString());
                            }
                            break;
                        case 2:
                            if (((Boolean) contactTable.getValueAt(j, i)).booleanValue() == Boolean.TRUE) {
                                mailing.addElement((contactTable.getModel().getValueAt(j, 0)).toString());
                            }
                            break;
                        case 3:
                            if (((Boolean) contactTable.getValueAt(j, i)).booleanValue() == Boolean.TRUE) {
                                spam.addElement((contactTable.getModel().getValueAt(j, 0)).toString());
                            }
                            break;
                        case 4:
                            if (((Boolean) contactTable.getValueAt(j, i)).booleanValue() == Boolean.TRUE) {
                                filter.addElement((contactTable.getModel().getValueAt(j, 0)).toString());
                            }
                            break;
                    }
                    contactTable.setValueAt(new Boolean(false), j, i);
                }
            }
            if (alias.size() > 0) {
                aliasPanel = new PanelAnonymize(alias, contactTable.getModel().getColumnName(1));
                thePanel.add(aliasPanel);
                vectAliasPanel.add(aliasPanel);
            }
            if (mailing.size() > 0) {
                mailingPanel = new PanelAnonymize(mailing, contactTable.getModel().getColumnName(2));
                thePanel.add(mailingPanel);
                vectMailingPanel.add(mailingPanel);
            }
            if (spam.size() > 0) {
                spamPanel = new PanelAnonymize(spam, contactTable.getModel().getColumnName(3));
                thePanel.add(spamPanel);
                vectSpamPanel.add(spamPanel);
            }
            if (filter.size() > 0) {
                filterPanel = new PanelAnonymize(filter, contactTable.getModel().getColumnName(4));
                thePanel.add(filterPanel);
                vectFilterPanel.add(filterPanel);
            }
            ok.setVisible(true);
        } else if (e.getSource() == anonymize) {
            ok.setVisible(false);
            fenC = new JFrame("test");
            fenC.setLayout(new FlowLayout());
            thePanel.setLayout(new GridLayout(0, 2));
            fenC.add(thePanel);
            JPanel bts = new JPanel();
            bts.add(validAnon);
            fenC.setVisible(true);
            validAnon.addActionListener(new ActionListener() {

                public void actionPerformed(ActionEvent arg0) {
                    if (vectAliasPanel.size() > 0) {
                        for (int i = 0; i < vectAliasPanel.size(); i++) {
                            if (vectAliasPanel.elementAt(i).getChoice().getText() != "") {
                                Object[] line = { vectAliasPanel.elementAt(i).getChoice().getText(), new Boolean(false), new Boolean(false), new Boolean(false), new Boolean(false), new Boolean(false) };
                                ((CheckBoxRendererModel) contactTable.getModel()).addRow(line);
                            }
                        }
                    }
                    if (vectMailingPanel.size() > 0) {
                        for (int i = 0; i < vectMailingPanel.size(); i++) {
                            if (vectMailingPanel.elementAt(i).getChoice().getText() != "") {
                                Object[] line = { vectMailingPanel.elementAt(i).getChoice().getText(), new Boolean(false), new Boolean(false), new Boolean(false), new Boolean(false), new Boolean(false) };
                                ((CheckBoxRendererModel) contactTable.getModel()).addRow(line);
                            }
                        }
                    }
                    if (vectSpamPanel.size() > 0) {
                        for (int i = 0; i < vectSpamPanel.size(); i++) {
                            if (vectSpamPanel.elementAt(i).getChoice().getText() != "") {
                                Object[] line = { vectSpamPanel.elementAt(i).getChoice().getText(), new Boolean(false), new Boolean(false), new Boolean(false), new Boolean(false), new Boolean(false) };
                                ((CheckBoxRendererModel) contactTable.getModel()).addRow(line);
                            }
                        }
                    }
                    if (vectFilterPanel.size() > 0) {
                        for (int i = 0; i < vectFilterPanel.size(); i++) {
                            if (vectFilterPanel.elementAt(i).getChoice().getText() != "") {
                                Object[] line = { vectFilterPanel.elementAt(i).getChoice().getText(), new Boolean(false), new Boolean(false), new Boolean(false), new Boolean(false), new Boolean(false) };
                                ((CheckBoxRendererModel) contactTable.getModel()).addRow(line);
                            }
                        }
                    }
                    cancelAnon.addActionListener(new ActionListener() {

                        public void actionPerformed(ActionEvent arg0) {
                            getFenC().setVisible(false);
                        }
                    });
                    System.out.println("valid anon bouton marche !");
                }
            });
            bts.add(cancelAnon);
            fenC.add(bts);
            fenC.pack();
            fenC.setVisible(true);
        } else if (e.getSource() == degroup) {
            if (alias.size() > 0) {
                alias.clear();
            }
            if (mailing.size() > 0) {
                mailing.clear();
            }
            if (spam.size() > 0) {
                spam.clear();
            }
            if (filter.size() > 0) {
                filter.clear();
            }
        } else if (e.getSource() == cancel) {
            System.exit(0);
        }
    }

    public JPanel getThePanel() {
        return thePanel;
    }

    public JButton getValidAnon() {
        return validAnon;
    }

    public void mouseClicked(MouseEvent e) {
        if (e.getClickCount() == 1) {
            ok.setVisible(false);
        }
    }

    public JFrame getFenC() {
        return fenC;
    }
}
