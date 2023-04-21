package saadadb.admin.relation;

import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.SwingUtilities;
import saadadb.admin.SaadaDBAdmin;
import saadadb.admin.dialogs.NameInput;
import saadadb.admin.dialogs.SelectRelation;
import saadadb.admin.threads.RelationCreate;
import saadadb.admin.threads.RelationEmpty;
import saadadb.admin.threads.RelationIndexation;
import saadadb.admin.threads.RelationPopulate;
import saadadb.admin.threads.RelationRemove;
import saadadb.configuration.RelationConf;
import saadadb.database.Database;
import saadadb.exceptions.SaadaException;
import saadadb.relationship.RelationManager;
import saadadb.sqltable.SQLTable;
import saadadb.sqltable.TransactionMaker;
import saadadb.util.Messenger;

/**
 * @author laurentmichel
 * * @version $Id: RelationConfPanel.java 118 2012-01-06 14:33:51Z laurent.mistahl $

 */
public class RelationConfPanel extends JPanel {

    public MappingRelationPanel mapping_panel;

    public JFrame frame;

    protected JScrollPane config_scroller;

    public JButton empty_button;

    public JButton remove_button;

    public JButton index_button;

    protected JButton new_button;

    protected JButton load_button;

    public JButton create_button;

    public JButton populate_button;

    /**
	 * 
	 */
    private static final long serialVersionUID = 1L;

    public RelationConfPanel(JFrame frame, Dimension dim) {
        super();
        this.setPreferredSize(dim);
        mapping_panel = new MappingRelationPanel(this);
        this.frame = frame;
        config_scroller = new JScrollPane(mapping_panel);
        config_scroller.setPreferredSize(new Dimension((int) (dim.getWidth()) - 50, (int) (dim.getHeight()) - 100));
        GridBagConstraints c = new GridBagConstraints();
        this.setLayout(new GridBagLayout());
        c.gridx = 0;
        c.gridy = 0;
        c.fill = GridBagConstraints.BOTH;
        c.weightx = c.weighty = 0.5;
        this.add(config_scroller, c);
        JPanel btn_panel = new JPanel();
        new_button = new JButton("New");
        new_button.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent e) {
                NameInput ni = new NameInput(RelationConfPanel.this.frame, "Relation Name Input");
                String new_name = ni.getNew_name();
                if (new_name != null && new_name.length() > 0) {
                    RelationConfPanel.this.mapping_panel.reset();
                    RelationConfPanel.this.mapping_panel.name_field.setText(new_name);
                    RelationConfPanel.this.mapping_panel.setEditable(true);
                    RelationConfPanel.this.mapping_panel.query_panel.setEditable(false);
                    RelationConfPanel.this.create_button.setEnabled(true);
                    RelationConfPanel.this.create_button.setText("Create Relation");
                    RelationConfPanel.this.populate_button.setEnabled(false);
                    RelationConfPanel.this.index_button.setEnabled(false);
                    RelationConfPanel.this.empty_button.setEnabled(false);
                    RelationConfPanel.this.remove_button.setEnabled(false);
                    RelationConfPanel.this.mapping_panel.paintInBeige();
                    SaadaDBAdmin.showInfo(RelationConfPanel.this, "<HTML>To complete the relationship creation<UL><LI>Set both Primary and Secondary collection (drag and drop from the data tree)<LI>Declare qualifiers (optional)<LI>Click on create button");
                }
            }
        });
        new_button.setToolTipText("Setup a new Saada relationship");
        btn_panel.add(new_button);
        load_button = new JButton("Load");
        load_button.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent e) {
                SwingUtilities.invokeLater(new Runnable() {

                    public void run() {
                        if (Database.getCachemeta().getRelation_names().length == 0) {
                            SaadaDBAdmin.showInputError(RelationConfPanel.this.frame, "There is no relation in the SaadaDB");
                        } else {
                            SelectRelation sr = new SelectRelation(RelationConfPanel.this.frame);
                            try {
                                if (sr.getTyped_name() != null) {
                                    RelationManager rm = new RelationManager(sr.getTyped_name());
                                    RelationConfPanel.this.mapping_panel.paintInBeige();
                                    RelationConfPanel.this.mapping_panel.setEditable(false);
                                    RelationConfPanel.this.mapping_panel.query_panel.setEditable(true);
                                    RelationConfPanel.this.mapping_panel.query_panel.setBackground(SaadaDBAdmin.beige_color);
                                    RelationConfPanel.this.create_button.setEnabled(false);
                                    RelationConfPanel.this.populate_button.setEnabled(true);
                                    RelationConfPanel.this.index_button.setEnabled(true);
                                    RelationConfPanel.this.empty_button.setEnabled(true);
                                    RelationConfPanel.this.remove_button.setEnabled(true);
                                    RelationConfPanel.this.mapping_panel.load(rm.getRelation_conf());
                                }
                            } catch (SaadaException e1) {
                                SaadaDBAdmin.showFatalError(RelationConfPanel.this.frame, e1.getMessage());
                            }
                        }
                    }
                });
            }
        });
        load_button.setToolTipText("Display the configuration of an existing Saada relationship");
        btn_panel.add(load_button);
        create_button = new JButton("Create");
        create_button.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent e) {
                try {
                    String rel_name = mapping_panel.name_field.getText();
                    RelationConf rel_conf = null;
                    if (rel_name.length() == 0) {
                        SaadaDBAdmin.showFatalError(RelationConfPanel.this.frame, "No relation name given");
                    }
                    if ((rel_conf = RelationConfPanel.this.mapping_panel.getConfig()) == null) {
                    } else if (Database.getCachemeta().getRelation(rel_name) == null) {
                        RelationCreate ir = new RelationCreate(RelationConfPanel.this, rel_conf);
                        ir.start();
                    }
                } catch (Exception e1) {
                    Messenger.printStackTrace(e1);
                    SaadaDBAdmin.showFatalError(RelationConfPanel.this.frame, e1.getMessage());
                }
            }
        });
        create_button.setToolTipText("Create (but not populate) the Saada relationship.");
        btn_panel.add(create_button);
        populate_button = new JButton("Populate");
        populate_button.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent e) {
                try {
                    String rel_name = RelationConfPanel.this.mapping_panel.name_field.getText();
                    String correlator = RelationConfPanel.this.mapping_panel.query_panel.getCorrelator();
                    if (rel_name == null || rel_name.length() == 0) {
                        SaadaDBAdmin.showFatalError(RelationConfPanel.this.frame, "No relation given");
                    } else {
                        Messenger.printMsg(Messenger.TRACE, "saving the correlator");
                        SQLTable.beginTransaction();
                        RelationConfPanel.this.mapping_panel.saveCorrelator();
                        SQLTable.commitTransaction();
                        Database.getCachemeta().reload(true);
                        RelationPopulate rp = new RelationPopulate(RelationConfPanel.this.frame, rel_name, correlator, RelationConfPanel.this);
                        synchronized (rp) {
                            rp.setDaemon(true);
                            rp.start();
                            rp.setPriority(Thread.MIN_PRIORITY);
                        }
                    }
                } catch (Exception e1) {
                    Messenger.printStackTrace(e1);
                    SQLTable.abortTransaction();
                    SaadaDBAdmin.showFatalError(RelationConfPanel.this.frame, e1.getMessage());
                }
            }
        });
        populate_button.setToolTipText("Run the \"Join Query\" correlator - New links have been appended to the relation - Index are rebuilt.");
        btn_panel.add(populate_button);
        index_button = new JButton("Make Index");
        index_button.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent e) {
                RelationIndexation ir = new RelationIndexation(RelationConfPanel.this.frame, RelationConfPanel.this.mapping_panel.getName());
                synchronized (ir) {
                    ir.setDaemon(true);
                    ir.start();
                    ir.setPriority(Thread.MIN_PRIORITY);
                }
            }
        });
        index_button.setToolTipText("Build indexes of the Saada relationship");
        btn_panel.add(index_button);
        empty_button = new JButton("Empty");
        empty_button.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent e) {
                RelationEmpty ir = new RelationEmpty(RelationConfPanel.this.frame, RelationConfPanel.this.mapping_panel.getName());
                ir.start();
            }
        });
        empty_button.setToolTipText("Empty the Saada relationship - Update the indexes");
        btn_panel.add(empty_button);
        remove_button = new JButton("Remove");
        remove_button.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent e) {
                RelationRemove ir = new RelationRemove(RelationConfPanel.this, RelationConfPanel.this.mapping_panel.getName());
                ir.start();
            }
        });
        remove_button.setToolTipText("Remove the Saada relationship.");
        btn_panel.add(remove_button);
        c.gridx = 0;
        c.gridy = 1;
        c.fill = GridBagConstraints.NONE;
        c.weightx = c.weighty = 0.0;
        this.add(btn_panel, c);
        this.setPreferredSize(dim);
        create_button.setEnabled(false);
        populate_button.setEnabled(false);
        index_button.setEnabled(false);
        empty_button.setEnabled(false);
        remove_button.setEnabled(false);
        this.mapping_panel.paintInGray();
        this.mapping_panel.query_panel.setBackground(SaadaDBAdmin.gray_color);
    }

    public static void main(String[] args) {
        JFrame f = new JFrame();
        f.setPreferredSize(new Dimension(650, 500));
        RelationConfPanel rcp = new RelationConfPanel(f, new Dimension(650, 500));
        f.add(rcp);
        f.pack();
        f.setVisible(true);
    }
}
