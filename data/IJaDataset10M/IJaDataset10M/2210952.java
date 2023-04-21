package org.openconcerto.erp.core.finance.payment.component;

import static org.openconcerto.utils.CollectionUtils.createSet;
import org.openconcerto.erp.config.ComptaPropsConfiguration;
import org.openconcerto.erp.core.finance.payment.element.TypeReglementSQLElement;
import org.openconcerto.erp.model.BanqueModifiedListener;
import org.openconcerto.sql.Configuration;
import org.openconcerto.sql.element.BaseSQLComponent;
import org.openconcerto.sql.element.SQLElement;
import org.openconcerto.sql.model.SQLBackgroundTableCache;
import org.openconcerto.sql.model.SQLRow;
import org.openconcerto.sql.model.SQLRowAccessor;
import org.openconcerto.sql.model.SQLRowValues;
import org.openconcerto.sql.model.Where;
import org.openconcerto.sql.request.ComboSQLRequest;
import org.openconcerto.sql.sqlobject.ElementComboBox;
import org.openconcerto.sql.sqlobject.SQLRequestComboBox;
import org.openconcerto.sql.sqlobject.SQLSearchableTextCombo;
import org.openconcerto.sql.sqlobject.SQLTextCombo;
import org.openconcerto.ui.DefaultGridBagConstraints;
import org.openconcerto.ui.JDate;
import org.openconcerto.ui.component.ITextCombo;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.HashMap;
import java.util.Map;
import javax.swing.ButtonGroup;
import javax.swing.JCheckBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JTextField;
import javax.swing.event.EventListenerList;

public class ModeDeReglementSQLComponent extends BaseSQLComponent {

    private ElementComboBox boxBanque = new ElementComboBox(true, 20);

    private JPanel panelBanque = new JPanel(new GridBagLayout());

    private JPanel panelEcheance = new JPanel(new GridBagLayout());

    private final EventListenerList banqueModifiedListenerList = new EventListenerList();

    private final SQLRequestComboBox comboTypeReglement = new SQLRequestComboBox();

    private ITextCombo comboA;

    private ITextCombo comboLe = new SQLTextCombo();

    private SQLSearchableTextCombo comboBanque = new SQLSearchableTextCombo();

    private JRadioButton buttonFinMois = new JRadioButton("fin de mois");

    private JRadioButton buttonDateFacture = new JRadioButton("date de facturation");

    private JRadioButton buttonLe = new JRadioButton("le");

    private JCheckBox checkboxComptant = new JCheckBox("Comptant");

    private JDate dateDepot = new JDate(false);

    private JDate dateVirt = new JDate(false);

    private JDate dateCheque = new JDate(false);

    private JTextField numeroChq = new JTextField();

    private JTextField nom = new JTextField(30);

    private static final int MODE_VIRT = 3;

    private static final int MODE_CHEQUE = 2;

    private static final int MODE_ECHEANCE = 1;

    private JPanel panelActive = null;

    private Map<Integer, JPanel> m = new HashMap<Integer, JPanel>();

    private int rowIdMode;

    private void setComponentModeEnabled(SQLRow rowTypeRegl) {
        if (rowTypeRegl != null && rowIdMode != rowTypeRegl.getID()) {
            rowIdMode = rowTypeRegl.getID();
            System.err.println("ModeDeReglementNGSQLComponent.setComponentModeEnabled() " + rowIdMode);
        } else {
            return;
        }
        ButtonGroup group = new ButtonGroup();
        group.add(this.buttonFinMois);
        group.add(this.buttonDateFacture);
        group.add(this.buttonLe);
        final Boolean boolean1 = rowTypeRegl.getBoolean("ECHEANCE");
        final Boolean boolean2 = rowTypeRegl.getBoolean("COMPTANT");
        this.checkboxComptant.setEnabled(!(boolean1 || boolean2));
        if (boolean1) {
            this.checkboxComptant.setSelected(false);
        }
        if (boolean2) {
            this.checkboxComptant.setSelected(true);
        }
        setEcheanceEnabled(!this.checkboxComptant.isSelected());
        boolean chequeComptant = (rowTypeRegl.getID() == TypeReglementSQLElement.CHEQUE && this.checkboxComptant.isSelected());
        int mode = MODE_ECHEANCE;
        if (chequeComptant) {
            mode = MODE_CHEQUE;
        } else {
            boolean virtComptant = (rowTypeRegl.getID() == TypeReglementSQLElement.TRAITE && this.checkboxComptant.isSelected());
            if (virtComptant) {
                mode = MODE_VIRT;
            }
        }
        replacePanel(mode);
    }

    public ModeDeReglementSQLComponent(SQLElement elt) {
        super(elt);
    }

    private JPanel infosCheque = new JPanel(new GridBagLayout());

    private JPanel infosVirt = new JPanel(new GridBagLayout());

    final ItemListener listenerComptant = new ItemListener() {

        @Override
        public void itemStateChanged(ItemEvent e) {
            setEcheanceEnabled(!ModeDeReglementSQLComponent.this.checkboxComptant.isSelected());
        }
    };

    public void addViews() {
        this.setLayout(new GridBagLayout());
        GridBagConstraints c = new DefaultGridBagConstraints();
        c.fill = GridBagConstraints.NONE;
        c.anchor = GridBagConstraints.WEST;
        this.comboA = new SQLTextCombo(false);
        c.gridy++;
        c.gridheight = 1;
        this.add(new JLabel("Règlement par"), c);
        this.comboTypeReglement.setPreferredSize(new Dimension(80, new JTextField().getPreferredSize().height));
        DefaultGridBagConstraints.lockMinimumSize(this.comboTypeReglement);
        c.gridx++;
        c.fill = GridBagConstraints.HORIZONTAL;
        this.add(this.comboTypeReglement, c);
        c.gridheight = 1;
        c.gridx++;
        DefaultGridBagConstraints.lockMinimumSize(this.checkboxComptant);
        this.add(this.checkboxComptant, c);
        c.gridy++;
        c.gridx = 0;
        c.weightx = 1;
        c.gridwidth = 3;
        c.fill = GridBagConstraints.HORIZONTAL;
        c.gridheight = GridBagConstraints.REMAINDER;
        createPanelChequeComptant();
        this.add(this.infosCheque, c);
        createPanelVirementComptant();
        this.add(this.infosVirt, c);
        createPanelEcheance();
        this.add(this.panelEcheance, c);
        this.addSQLObject(this.comboBanque, "ETS");
        this.addSQLObject(this.dateCheque, "DATE");
        this.addSQLObject(this.numeroChq, "NUMERO");
        this.addSQLObject(this.nom, "NOM");
        this.addRequiredSQLObject(this.comboA, "AJOURS");
        this.addRequiredSQLObject(this.comboLe, "LENJOUR");
        this.addSQLObject(this.buttonFinMois, "FIN_MOIS");
        this.addSQLObject(this.buttonDateFacture, "DATE_FACTURE");
        this.addSQLObject(this.dateDepot, "DATE_DEPOT");
        this.addSQLObject(this.dateVirt, "DATE_VIREMENT");
        this.addSQLObject(this.checkboxComptant, "COMPTANT");
        this.addRequiredSQLObject(this.comboTypeReglement, "ID_TYPE_REGLEMENT");
        this.comboTypeReglement.addValueListener(new PropertyChangeListener() {

            @Override
            public void propertyChange(PropertyChangeEvent evt) {
                Integer id = ModeDeReglementSQLComponent.this.comboTypeReglement.getValue();
                if (id != null && id > 1) {
                    SQLRow ligneTypeReg = SQLBackgroundTableCache.getInstance().getCacheForTable(getTable().getBase().getTable("TYPE_REGLEMENT")).getRowFromId(id);
                    setComponentModeEnabled(ligneTypeReg);
                }
            }
        });
        this.buttonFinMois.addItemListener(new ItemListener() {

            @Override
            public void itemStateChanged(ItemEvent e) {
                if (ModeDeReglementSQLComponent.this.buttonFinMois.isSelected()) {
                    ModeDeReglementSQLComponent.this.comboLe.setValue("31");
                }
                boolean activeComboLe = !ModeDeReglementSQLComponent.this.buttonFinMois.isSelected() && !ModeDeReglementSQLComponent.this.checkboxComptant.isSelected();
                ModeDeReglementSQLComponent.this.comboLe.setEnabled(activeComboLe);
            }
        });
        this.buttonDateFacture.addItemListener(new ItemListener() {

            @Override
            public void itemStateChanged(ItemEvent e) {
                if (ModeDeReglementSQLComponent.this.buttonDateFacture.isSelected()) {
                    ModeDeReglementSQLComponent.this.comboLe.setValue("0");
                }
                boolean activeComboLe = !ModeDeReglementSQLComponent.this.buttonDateFacture.isSelected() && !ModeDeReglementSQLComponent.this.checkboxComptant.isSelected();
                ModeDeReglementSQLComponent.this.comboLe.setEnabled(activeComboLe);
            }
        });
        this.checkboxComptant.addItemListener(listenerComptant);
    }

    private void createPanelEcheance() {
        GridBagConstraints c = new DefaultGridBagConstraints();
        c.fill = GridBagConstraints.NONE;
        this.panelEcheance.setOpaque(false);
        this.panelEcheance.add(new JLabel("A"), c);
        c.gridx++;
        c.gridwidth = 1;
        this.comboA.setMinimumSize(new Dimension(60, this.comboA.getMinimumSize().height));
        this.comboA.setPreferredSize(new Dimension(60, this.comboA.getMinimumSize().height));
        this.comboA.setMaximumSize(new Dimension(60, this.comboA.getMinimumSize().height));
        this.panelEcheance.add(this.comboA, c);
        c.gridx += 1;
        c.gridwidth = 1;
        this.panelEcheance.add(new JLabel("jours,"), c);
        c.gridx++;
        c.weightx = 1;
        c.gridwidth = 2;
        c.fill = GridBagConstraints.HORIZONTAL;
        this.buttonDateFacture.setOpaque(false);
        this.panelEcheance.add(this.buttonDateFacture, c);
        c.gridy++;
        c.weightx = 0;
        c.fill = GridBagConstraints.NONE;
        this.buttonFinMois.setOpaque(false);
        this.panelEcheance.add(this.buttonFinMois, c);
        c.gridy++;
        c.gridwidth = 1;
        this.buttonLe.setOpaque(false);
        this.panelEcheance.add(this.buttonLe, c);
        c.gridx++;
        this.comboLe.setMinimumSize(new Dimension(60, this.comboLe.getMinimumSize().height));
        this.comboLe.setPreferredSize(new Dimension(60, this.comboLe.getMinimumSize().height));
        this.comboLe.setMaximumSize(new Dimension(60, this.comboLe.getMinimumSize().height));
        this.panelEcheance.add(this.comboLe, c);
        this.panelActive = this.panelEcheance;
        this.m.put(MODE_ECHEANCE, this.panelEcheance);
        DefaultGridBagConstraints.lockMinimumSize(panelEcheance);
    }

    public void createPanelChequeComptant() {
        GridBagConstraints cCheque = new DefaultGridBagConstraints();
        this.infosCheque.add(new JLabel("Banque"), cCheque);
        cCheque.gridx++;
        this.infosCheque.add(this.comboBanque, cCheque);
        cCheque.gridx++;
        this.infosCheque.add(new JLabel("N°"), cCheque);
        cCheque.gridx++;
        DefaultGridBagConstraints.lockMinimumSize(this.numeroChq);
        this.infosCheque.add(this.numeroChq, cCheque);
        cCheque.gridy++;
        cCheque.gridx = 0;
        this.infosCheque.add(new JLabel("Daté du"), cCheque);
        cCheque.gridx++;
        this.infosCheque.add(this.dateCheque, cCheque);
        JLabel labelDepot = new JLabel("A déposer après le");
        cCheque.gridx++;
        DefaultGridBagConstraints.lockMinimumSize(this.infosCheque);
        this.infosCheque.add(labelDepot, cCheque);
        cCheque.gridx++;
        this.infosCheque.add(this.dateDepot, cCheque);
        this.m.put(MODE_CHEQUE, this.infosCheque);
        this.infosCheque.setVisible(false);
        DefaultGridBagConstraints.lockMinimumSize(infosCheque);
    }

    public void createPanelVirementComptant() {
        GridBagConstraints cCheque = new DefaultGridBagConstraints();
        cCheque.weightx = 1;
        this.infosVirt.add(new JLabel("Libellé"), cCheque);
        cCheque.gridx++;
        this.infosVirt.add(this.nom, cCheque);
        cCheque.gridy++;
        cCheque.gridx = 0;
        cCheque.fill = GridBagConstraints.NONE;
        cCheque.weightx = 0;
        this.infosVirt.add(new JLabel("Daté du"), cCheque);
        cCheque.gridx++;
        this.infosVirt.add(this.dateVirt, cCheque);
        this.m.put(MODE_VIRT, this.infosVirt);
        this.infosVirt.setVisible(false);
        DefaultGridBagConstraints.lockMinimumSize(infosVirt);
    }

    private void replacePanel(int mode) {
        JPanel panel = this.m.get(mode);
        if (panel != this.panelActive) {
            this.panelActive.setVisible(false);
            clearFields();
            panel.setVisible(true);
            this.panelActive = panel;
        }
    }

    private void clearFields() {
        System.err.println("ModeDeReglementNGSQLComponent.clearFields()");
        this.dateCheque.setValue(null);
        this.dateDepot.setValue(null);
        this.dateVirt.setValue(null);
        this.nom.setText("");
        this.numeroChq.setText("");
        this.comboBanque.setValue("");
    }

    private void fireBanqueIdChange(int id) {
        BanqueModifiedListener[] l = this.banqueModifiedListenerList.getListeners(BanqueModifiedListener.class);
        for (BanqueModifiedListener banqueModifiedListener : l) {
            banqueModifiedListener.idChange(id);
        }
    }

    @Override
    public void select(SQLRowAccessor r) {
        if (r != null) {
            final SQLRowValues rVals = r.asRowValues();
            final SQLRowValues vals = new SQLRowValues(r.getTable());
            this.checkboxComptant.removeItemListener(this.listenerComptant);
            vals.load(rVals, createSet("ID_TYPE_REGLEMENT", "COMPTANT"));
            vals.setID(rVals.getID());
            super.select(vals);
            ModeDeReglementSQLComponent.this.comboTypeReglement.setValue(vals.getInt("ID_TYPE_REGLEMENT"));
            ModeDeReglementSQLComponent.this.checkboxComptant.setSelected(vals.getBoolean("COMPTANT"));
            setComponentModeEnabled(SQLBackgroundTableCache.getInstance().getCacheForTable(r.getTable().getTable("TYPE_REGLEMENT")).getRowFromId(vals.getInt("ID_TYPE_REGLEMENT")));
            setEcheanceEnabled(!vals.getBoolean("COMPTANT"), vals.getInt("ID_TYPE_REGLEMENT"));
            super.select(rVals);
            if (rVals.getInt("LENJOUR") != 0 && rVals.getInt("LENJOUR") != 31) {
                this.buttonLe.setSelected(true);
            }
            this.checkboxComptant.addItemListener(this.listenerComptant);
        } else {
            super.select(r);
        }
    }

    private void setEcheanceEnabled(boolean b) {
        setEcheanceEnabled(b, this.comboTypeReglement.getValue());
    }

    private void setEcheanceEnabled(boolean b, Integer typeReglt) {
        this.comboA.setEnabled(b);
        this.comboLe.setEnabled(b);
        this.buttonFinMois.setEnabled(b);
        this.buttonDateFacture.setEnabled(b);
        this.buttonLe.setEnabled(b);
        if (!b) {
            this.buttonLe.setSelected(true);
            this.comboA.setValue("0");
            this.comboLe.setValue("0");
        } else {
            this.comboA.setValue("30");
            this.buttonDateFacture.setSelected(true);
        }
        if (typeReglt != null) {
            boolean chequeComptant = (typeReglt == TypeReglementSQLElement.CHEQUE && (!b));
            int mode = MODE_ECHEANCE;
            if (chequeComptant) {
                mode = MODE_CHEQUE;
            } else {
                boolean virtComptant = (typeReglt == TypeReglementSQLElement.TRAITE && (!b));
                if (virtComptant) {
                    mode = MODE_VIRT;
                }
            }
            replacePanel(mode);
        }
        this.panelActive.revalidate();
        this.panelActive.repaint();
    }

    protected SQLRowValues createDefaults() {
        final SQLRowValues vals = new SQLRowValues(getTable());
        vals.put("AJOURS", 30);
        vals.put("LENJOUR", 31);
        this.buttonLe.setSelected(true);
        return vals;
    }

    public void setWhereBanque(Where w) {
        if (this.boxBanque != null && this.boxBanque.isShowing()) {
            final ComboSQLRequest request = this.boxBanque.getRequest();
            if (request != null) {
                request.setWhere(w);
                this.boxBanque.fillCombo();
            }
        }
    }

    public void setSelectedIdBanque(int id) {
        this.boxBanque.setValue(id);
    }

    public int getSelectedIdBanque() {
        return this.boxBanque.getSelectedId();
    }

    public void addBanqueModifiedListener(BanqueModifiedListener e) {
        this.banqueModifiedListenerList.add(BanqueModifiedListener.class, e);
    }
}
