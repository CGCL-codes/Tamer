package org.fudaa.fudaa.oscar;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import javax.swing.JComponent;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;
import com.memoire.bu.BuButton;
import com.memoire.bu.BuLabel;
import com.memoire.bu.BuPanel;
import com.memoire.bu.BuTable;
import com.memoire.bu.BuTableCellRenderer;
import org.fudaa.dodico.corba.oscar.SPointCoteValeur;
import org.fudaa.dodico.corba.oscar.SResultatsPredimensionnement;
import org.fudaa.dodico.corba.oscar.VALEUR_NULLE;

/**
 * Description de l'onglet 'R�sultats de pr�dimensionnement' de la fen�tre 'r�sultats bruts'.
 * 
 * @version $Revision: 1.7 $ $Date: 2007-11-15 15:11:49 $ by $Author: hadouxad $
 * @author Olivier Hoareau
 */
public class OscarPredimensionnementResultatsBruts extends OscarAbstractOnglet {

    /**
   *
   */
    BuTable tb_predim_;

    /**
   *
   */
    BuButton bt_imprimer_;

    /**
   *
   */
    BuButton bt_sauvegarder_;

    /**
   *
   */
    DefaultTableModel dm_tb_predim_;

    /**
   *
   */
    public OscarPredimensionnementResultatsBruts(final OscarFilleResultatsBruts _resultats, final String _helpfile) {
        super(_resultats.getApplication(), _resultats, _helpfile);
    }

    /**
   *
   */
    protected JComponent construireGUI() {
        final BuPanel _p = new BuPanel();
        _p.setLayout(new BorderLayout());
        _p.setBorder(OscarLib.EmptyBorder());
        bt_imprimer_ = OscarLib.Button("Imprimer", "IMPRIMER", "IMPRIMER");
        bt_sauvegarder_ = OscarLib.Button("Exporter", "ENREGISTRER", "SAUVEGARDER");
        final BuPanel pn1 = OscarLib.InfoAidePanel(OscarMsg.LAB013, this);
        final BuPanel pn2 = new BuPanel();
        pn2.setLayout(new BorderLayout());
        pn2.setBorder(OscarLib.TitledBorder(OscarMsg.TITLELAB028));
        dm_tb_predim_ = new OscarPredimensionnementResultatsBrutsTableModel(new Object[] { "Param�tre", "Valeur", "Unit�" });
        tb_predim_ = OscarLib.Table(dm_tb_predim_);
        tb_predim_.setPreferredScrollableViewportSize(new Dimension(OscarLib.TABLE_PREFERRED_WIDTH, 187));
        final OscarPredimensionnementResultatsBrutsTableCellRenderer _cellrenderer = new OscarPredimensionnementResultatsBrutsTableCellRenderer();
        for (int i = 0; i < tb_predim_.getColumnModel().getColumnCount(); i++) {
            tb_predim_.getColumnModel().getColumn(i).setCellRenderer(_cellrenderer);
            tb_predim_.getColumnModel().getColumn(i).setPreferredWidth((i == 0) ? 100 : ((i == 2) ? 20 : 50));
        }
        final JScrollPane sp1 = new JScrollPane(tb_predim_);
        final BuPanel pn3 = OscarLib.FlowPanel();
        bt_imprimer_.addActionListener(this);
        bt_sauvegarder_.addActionListener(this);
        pn3.add(bt_imprimer_);
        pn3.add(bt_sauvegarder_);
        pn2.add(pn3, BorderLayout.NORTH);
        pn2.add(sp1, BorderLayout.CENTER);
        _p.add(pn1, BorderLayout.NORTH);
        _p.add(pn2, BorderLayout.CENTER);
        return _p;
    }

    /**
   * affichage de l'action en cours sur la console et execution de l'action
   */
    protected void action(final String _action) {
        if (_action.equals("IMPRIMER")) {
            imprimer();
        } else if (_action.equals("SAUVEGARDER")) {
            sauvegarder();
        }
    }

    /**
   *
   */
    protected void sauvegarder() {
        final String _f = OscarLib.getFileChoosenByUser(this, "Exporter les donn�es brutes", "Exporter", OscarLib.USER_HOME + "predimensionnement.csv");
        String _txt = "";
        if (_f == null) {
            return;
        }
        final SResultatsPredimensionnement _sr = getResultatsPredimensionnement();
        _txt += "Cote de pression nulle;" + _sr.cotePressionNull + OscarLib.LINE_SEPARATOR;
        _txt += ((_sr.encastrement == VALEUR_NULLE.value) ? "Point de but�e simple" : "Point de contre-but�e") + ";" + _sr.pointButeeSimple + OscarLib.LINE_SEPARATOR;
        _txt += "Pied (20% de contre-but�e);" + _sr.pied20Pourcent + OscarLib.LINE_SEPARATOR;
        _txt += "Pied (contre-but�e calcul�e);" + _sr.piedContreButee + OscarLib.LINE_SEPARATOR;
        _txt += "Valeur du moment maximum;" + _sr.momentMaximum.valeur + OscarLib.LINE_SEPARATOR;
        _txt += "Cote du moment maximum;" + _sr.momentMaximum.cote + OscarLib.LINE_SEPARATOR;
        _txt += "Valeur du moment minimum;" + _sr.momentMinimum.valeur + OscarLib.LINE_SEPARATOR;
        _txt += "Cote du moment minimum;" + _sr.momentMinimum.cote + OscarLib.LINE_SEPARATOR;
        if (_sr.encastrement != VALEUR_NULLE.value) {
            _txt += "Encastrement demand�;" + _sr.encastrement + OscarLib.LINE_SEPARATOR;
            _txt += "R�action d'encrage;" + _sr.reactionAncrage + OscarLib.LINE_SEPARATOR;
            _txt += "Niveau d'encrage;" + _sr.niveauAncrage + OscarLib.LINE_SEPARATOR;
        }
        OscarLib.writeFile(_f, _txt);
    }

    /**
   * imprime la fen�tre.
   */
    protected void imprimer() {
        getImplementation().actionPerformed(new ActionEvent(this, 15, "IMPRIMER"));
    }

    /**
   * retourne une structure de donn�es contenant les informations de sur le pr�dimensionnement
   * 
   * @return une structure de donn�es <code>SResultatsPredimensionnement</code>
   */
    public SResultatsPredimensionnement getResultatsPredimensionnement() {
        SPointCoteValeur _p = null;
        final SResultatsPredimensionnement _sr = new SResultatsPredimensionnement();
        _sr.cotePressionNull = Double.parseDouble(dm_tb_predim_.getValueAt(0, 1).toString());
        _sr.pointButeeSimple = Double.parseDouble(dm_tb_predim_.getValueAt(1, 1).toString());
        _sr.pied20Pourcent = Double.parseDouble(dm_tb_predim_.getValueAt(2, 1).toString());
        _sr.piedContreButee = Double.parseDouble(dm_tb_predim_.getValueAt(3, 1).toString());
        _p = new SPointCoteValeur();
        _p.valeur = Double.parseDouble(dm_tb_predim_.getValueAt(4, 1).toString());
        _p.cote = Double.parseDouble(dm_tb_predim_.getValueAt(5, 1).toString());
        _sr.momentMaximum = _p;
        _p = new SPointCoteValeur();
        _p.valeur = Double.parseDouble(dm_tb_predim_.getValueAt(6, 1).toString());
        _p.cote = Double.parseDouble(dm_tb_predim_.getValueAt(7, 1).toString());
        _sr.momentMinimum = _p;
        if (tb_predim_.getRowCount() > 8) {
            _sr.encastrement = Double.parseDouble(dm_tb_predim_.getValueAt(8, 1).toString());
            _sr.reactionAncrage = Double.parseDouble(dm_tb_predim_.getValueAt(9, 1).toString());
            _sr.niveauAncrage = Double.parseDouble(dm_tb_predim_.getValueAt(10, 1).toString());
        } else {
            _sr.encastrement = VALEUR_NULLE.value;
            _sr.niveauAncrage = VALEUR_NULLE.value;
            _sr.reactionAncrage = VALEUR_NULLE.value;
        }
        return _sr;
    }

    /**
   * charge les couples (cote, d�form�e) sp�cifi�es dans le tableau des couples
   * 
   * @param _p structure <code>SResultatsDeformees</code> contenant les couples (cote, d�form�e) � importer.
   */
    public void setResultatsPredimensionnement(SResultatsPredimensionnement _p) {
        viderTableau();
        if (_p == null) {
            _p = (SResultatsPredimensionnement) OscarLib.createIDLObject(SResultatsPredimensionnement.class);
        }
        for (int i = 0; i < ((_p.encastrement == VALEUR_NULLE.value) ? 8 : 11); i++) {
            dm_tb_predim_.addRow(OscarLib.EmptyRow(dm_tb_predim_.getColumnCount()));
        }
        tb_predim_.setValueAt("Cote de pression nulle", 0, 0);
        tb_predim_.setValueAt(OscarLib.formatterNombre(_p.cotePressionNull), 0, 1);
        tb_predim_.setValueAt("m", 0, 2);
        tb_predim_.setValueAt((_p.encastrement == VALEUR_NULLE.value) ? "Point de but�e simple" : "Point de contre-but�e", 1, 0);
        tb_predim_.setValueAt(OscarLib.formatterNombre(_p.pointButeeSimple), 1, 1);
        tb_predim_.setValueAt("m", 1, 2);
        tb_predim_.setValueAt("Pied (20% de contre-but�e)", 2, 0);
        tb_predim_.setValueAt(OscarLib.formatterNombre(_p.pied20Pourcent), 2, 1);
        tb_predim_.setValueAt("m", 2, 2);
        tb_predim_.setValueAt("Pied (contre-but�e calcul�e)", 3, 0);
        tb_predim_.setValueAt(OscarLib.formatterNombre(_p.piedContreButee), 3, 1);
        tb_predim_.setValueAt("m", 3, 2);
        tb_predim_.setValueAt("Valeur du moment maximum", 4, 0);
        tb_predim_.setValueAt(OscarLib.formatterNombre("moment", _p.momentMaximum.valeur), 4, 1);
        tb_predim_.setValueAt("kN.m/ml", 4, 2);
        tb_predim_.setValueAt("Cote du moment maximum", 5, 0);
        tb_predim_.setValueAt(OscarLib.formatterNombre(_p.momentMaximum.cote), 5, 1);
        tb_predim_.setValueAt("m", 5, 2);
        tb_predim_.setValueAt("Valeur du moment minimum", 6, 0);
        tb_predim_.setValueAt(OscarLib.formatterNombre("moment", _p.momentMinimum.valeur), 6, 1);
        tb_predim_.setValueAt("kN.m/ml", 6, 2);
        tb_predim_.setValueAt("Cote du moment minimum", 7, 0);
        tb_predim_.setValueAt(OscarLib.formatterNombre(_p.momentMinimum.cote), 7, 1);
        tb_predim_.setValueAt("m", 7, 2);
        if (_p.encastrement != VALEUR_NULLE.value) {
            tb_predim_.setValueAt("Encastrement demand�", 8, 0);
            tb_predim_.setValueAt(OscarLib.formatterNombre(_p.encastrement), 8, 1);
            tb_predim_.setValueAt("%", 8, 2);
            tb_predim_.setValueAt("R�action d'ancrage", 9, 0);
            tb_predim_.setValueAt(OscarLib.formatterNombre(_p.reactionAncrage), 9, 1);
            tb_predim_.setValueAt("kN/ml", 9, 2);
            tb_predim_.setValueAt("Niveau d'ancrage", 10, 0);
            tb_predim_.setValueAt(OscarLib.formatterNombre(_p.niveauAncrage), 10, 1);
            tb_predim_.setValueAt("m", 10, 2);
        }
    }

    /**
   * supprime tous les couples (cote, d�form�e) du tableau des couples
   */
    private void viderTableau() {
        if (tb_predim_.isEditing()) {
            tb_predim_.getCellEditor().stopCellEditing();
        }
        final int _max = tb_predim_.getRowCount();
        for (int i = 0; i < _max; i++) {
            dm_tb_predim_.removeRow(0);
        }
    }

    /**
   *
   */
    class OscarPredimensionnementResultatsBrutsTableCellRenderer extends BuTableCellRenderer {

        /**
     *
     */
        public Component getTableCellRendererComponent(final JTable table, final Object value, final boolean isSelected, final boolean hasFocus, final int row, final int column) {
            Component _r = null;
            BuLabel _lb = null;
            _lb = (BuLabel) super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
            _r = _lb;
            if (row >= 0) {
                if (value instanceof Double) {
                }
            }
            return _r;
        }
    }

    /**
   *
   */
    public class OscarPredimensionnementResultatsBrutsTableModel extends DefaultTableModel {

        /**
     *
     */
        public OscarPredimensionnementResultatsBrutsTableModel(final Object[] columnNames) {
            super(columnNames, 0);
        }

        /**
     *
     */
        public boolean isCellEditable(final int row, final int column) {
            return false;
        }

        /**
     *
     */
        public Object getValueAt(final int row, final int column) {
            Object _o = null;
            _o = super.getValueAt(row, column);
            return _o;
        }

        /**
     *
     */
        public void setValueAt(final Object _o, final int row, final int column) {
            super.setValueAt(_o, row, column);
        }
    }
}
