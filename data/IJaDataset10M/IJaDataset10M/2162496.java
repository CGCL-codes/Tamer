package org.openconcerto.sql.users.rights;

import org.openconcerto.sql.Configuration;
import org.openconcerto.sql.model.SQLRowValues;
import org.openconcerto.sql.model.SQLTable;
import org.openconcerto.ui.DefaultGridBagConstraints;
import org.openconcerto.ui.component.ITextArea;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.Arrays;
import javax.swing.AbstractAction;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JPopupMenu;
import javax.swing.JScrollPane;
import javax.swing.JSplitPane;
import javax.swing.JTable;
import javax.swing.SwingUtilities;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.event.TableModelEvent;
import javax.swing.event.TableModelListener;

public class UserRightsManagerPanel extends JPanel implements ActionListener, ListSelectionListener {

    JListSQLTablePanel list;

    SQLTable table = Configuration.getInstance().getRoot().findTable("USER_COMMON");

    JButton buttonApply, buttonClose, buttonRevert;

    final UserRightsManagerModel model = new UserRightsManagerModel();

    final JTable tableDroits = new JTable(this.model);

    ITextArea infos = new ITextArea();

    public UserRightsManagerPanel() {
        super(new GridBagLayout());
        JPanel listePanel = new JPanel(new GridBagLayout());
        GridBagConstraints c = new DefaultGridBagConstraints();
        c.weightx = 1;
        c.gridwidth = GridBagConstraints.REMAINDER;
        listePanel.add(new JLabel("Liste des utilisateurs"), c);
        c.weightx = 1;
        c.weighty = 1;
        c.gridy++;
        c.fill = GridBagConstraints.BOTH;
        this.list = new JListSQLTablePanel(this.table, Arrays.asList("NOM", "PRENOM"), "Droits par défaut");
        listePanel.add(this.list, c);
        JPanel panelDroits = new JPanel(new GridBagLayout());
        GridBagConstraints c2 = new DefaultGridBagConstraints();
        c2.gridwidth = GridBagConstraints.REMAINDER;
        panelDroits.add(new JLabel("Droits"), c2);
        c2.gridy++;
        c2.weightx = 1;
        c2.weighty = 0.7;
        c2.fill = GridBagConstraints.BOTH;
        this.tableDroits.getTableHeader().setReorderingAllowed(false);
        this.tableDroits.getColumnModel().getColumn(0).setResizable(false);
        this.tableDroits.getColumnModel().getColumn(0).setMaxWidth(40);
        this.tableDroits.getColumnModel().getColumn(0).setWidth(40);
        this.tableDroits.getModel().addTableModelListener(new TableModelListener() {

            @Override
            public void tableChanged(TableModelEvent e) {
                UserRightsManagerPanel.this.tableDroits.getColumnModel().getColumn(0).setResizable(false);
                UserRightsManagerPanel.this.tableDroits.getColumnModel().getColumn(0).setMaxWidth(40);
                UserRightsManagerPanel.this.tableDroits.getColumnModel().getColumn(0).setWidth(40);
                UserRightsManagerPanel.this.buttonRevert.setEnabled(true);
                UserRightsManagerPanel.this.buttonApply.setEnabled(true);
            }
        });
        panelDroits.add(new JScrollPane(this.tableDroits), c2);
        c2.gridy++;
        c2.weightx = 0;
        c2.weighty = 0;
        panelDroits.add(new JLabel("Informations"), c2);
        c2.weightx = 1;
        c2.weighty = 0.3;
        c2.gridy++;
        this.infos.setEditable(false);
        panelDroits.add(this.infos, c2);
        JPanel panelButton = new JPanel();
        this.buttonApply = new JButton("Appliquer");
        panelButton.add(this.buttonApply);
        this.buttonRevert = new JButton("Annuler");
        panelButton.add(this.buttonRevert);
        this.buttonClose = new JButton("Fermer");
        panelButton.add(this.buttonClose);
        c2.gridy++;
        c2.weightx = 0;
        c2.weighty = 0;
        c2.fill = GridBagConstraints.NONE;
        c2.gridwidth = 1;
        c2.anchor = GridBagConstraints.EAST;
        panelDroits.add(panelButton, c2);
        this.buttonClose.addActionListener(this);
        this.buttonApply.addActionListener(this);
        this.buttonRevert.addActionListener(this);
        JSplitPane pane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, listePanel, panelDroits);
        GridBagConstraints c3 = new GridBagConstraints();
        c3.weightx = 1;
        c3.weighty = 1;
        c3.fill = GridBagConstraints.BOTH;
        this.add(pane, c3);
        this.list.addListSelectionListener(this);
        this.tableDroits.getSelectionModel().addListSelectionListener(this);
        this.tableDroits.addMouseListener(new MouseAdapter() {

            @Override
            public void mousePressed(MouseEvent e) {
                if (e.getButton() == MouseEvent.BUTTON3) {
                    JPopupMenu menu = new JPopupMenu();
                    menu.add(new AbstractAction("Tout cocher") {

                        @Override
                        public void actionPerformed(ActionEvent e) {
                            for (int i = 0; i < UserRightsManagerPanel.this.tableDroits.getRowCount(); i++) {
                                UserRightsManagerPanel.this.model.setValueAt(Boolean.TRUE, i, 0);
                            }
                            UserRightsManagerPanel.this.model.fireTableDataChanged();
                        }
                    });
                    menu.add(new AbstractAction("Tout décocher") {

                        @Override
                        public void actionPerformed(ActionEvent e) {
                            for (int i = 0; i < UserRightsManagerPanel.this.tableDroits.getRowCount(); i++) {
                                UserRightsManagerPanel.this.model.setValueAt(Boolean.FALSE, i, 0);
                            }
                            UserRightsManagerPanel.this.model.fireTableDataChanged();
                        }
                    });
                    final int[] selectedRows = UserRightsManagerPanel.this.tableDroits.getSelectedRows();
                    if (selectedRows.length > 0) {
                        menu.add(new AbstractAction("Cocher la sélection") {

                            @Override
                            public void actionPerformed(ActionEvent e) {
                                for (int i : selectedRows) {
                                    UserRightsManagerPanel.this.model.setValueAt(Boolean.TRUE, i, 0);
                                }
                                UserRightsManagerPanel.this.model.fireTableDataChanged();
                            }
                        });
                        menu.add(new AbstractAction("Décocher la sélection") {

                            @Override
                            public void actionPerformed(ActionEvent e) {
                                for (int i : selectedRows) {
                                    UserRightsManagerPanel.this.model.setValueAt(Boolean.FALSE, i, 0);
                                }
                                UserRightsManagerPanel.this.model.fireTableDataChanged();
                            }
                        });
                    }
                    menu.pack();
                    menu.show(e.getComponent(), e.getPoint().x, e.getPoint().y);
                    menu.setVisible(true);
                }
            }
        });
        this.buttonRevert.setEnabled(false);
        this.buttonApply.setEnabled(false);
    }

    @Override
    public void valueChanged(ListSelectionEvent e) {
        if (e.getSource() == this.tableDroits.getSelectionModel()) {
            if (this.tableDroits.getSelectedRow() >= 0) {
                SQLRowValues rowVals = UserRightsManagerPanel.this.model.getRowValuesAt(this.tableDroits.getSelectedRow());
                UserRightsManagerPanel.this.infos.setText(rowVals.getString("DESCRIPTION"));
            } else {
                UserRightsManagerPanel.this.infos.setText("");
            }
        } else {
            if (e.getSource() == this.list.getJList()) {
                final int selectedIndex = UserRightsManagerPanel.this.list.getSelectedIndex();
                final boolean b = selectedIndex >= 0;
                if (b) {
                    UserRightsManagerPanel.this.model.loadRightsForUser(UserRightsManagerPanel.this.list.getModel().getRowAt(selectedIndex).getID());
                } else {
                    UserRightsManagerPanel.this.model.loadRightsForUser(-1);
                }
                UserRightsManagerPanel.this.buttonRevert.setEnabled(false);
                UserRightsManagerPanel.this.buttonApply.setEnabled(false);
            }
        }
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == this.buttonApply) {
            this.model.commitData();
            this.buttonRevert.setEnabled(false);
            this.buttonApply.setEnabled(false);
        } else {
            if (e.getSource() == this.buttonClose) {
                ((JFrame) SwingUtilities.getRoot(this)).dispose();
            } else {
                if (e.getSource() == this.buttonRevert) {
                    final int selectedIndex = UserRightsManagerPanel.this.list.getSelectedIndex();
                    if (selectedIndex >= 0) {
                        UserRightsManagerPanel.this.model.loadRightsForUser(UserRightsManagerPanel.this.list.getModel().getRowAt(selectedIndex).getID());
                    } else {
                        UserRightsManagerPanel.this.model.loadRightsForUser(-1);
                    }
                    this.buttonRevert.setEnabled(false);
                    this.buttonApply.setEnabled(false);
                }
            }
        }
    }
}
