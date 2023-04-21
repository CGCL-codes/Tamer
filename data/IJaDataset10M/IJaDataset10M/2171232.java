package org.openconcerto.erp.model;

import org.openconcerto.erp.config.ComptaPropsConfiguration;
import org.openconcerto.erp.core.common.ui.SQLJavaEditor;
import org.openconcerto.erp.core.humanresources.payroll.element.PeriodeValiditeSQLElement;
import org.openconcerto.erp.core.humanresources.payroll.element.VariablePayeSQLElement;
import org.openconcerto.sql.Configuration;
import org.openconcerto.sql.model.SQLBase;
import org.openconcerto.sql.model.SQLRow;
import org.openconcerto.sql.model.SQLRowValues;
import org.openconcerto.sql.model.SQLSelect;
import org.openconcerto.sql.model.SQLTable;
import org.openconcerto.sql.model.Where;
import java.sql.SQLException;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Vector;
import javax.swing.table.AbstractTableModel;
import org.apache.commons.dbutils.handlers.ArrayListHandler;

public class FichePayeModel extends AbstractTableModel {

    private Vector<SQLRowValues> vectRubrique;

    private Vector<SQLRowValues> vectRowValsToDelete;

    private final Map<String, SQLTable> mapTableSource = new HashMap<String, SQLTable>();

    private int idFiche;

    private String[] title;

    private static final SQLBase base = ((ComptaPropsConfiguration) Configuration.getInstance()).getSQLBaseSociete();

    private static final SQLTable tableProfilElt = Configuration.getInstance().getBase().getTable("PROFIL_PAYE_ELEMENT");

    private static final SQLTable tableFichePayeElt = base.getTable("FICHE_PAYE_ELEMENT");

    private static final SQLTable tableFichePaye = base.getTable("FICHE_PAYE");

    private static final SQLTable tableValidite = Configuration.getInstance().getBase().getTable("PERIODE_VALIDITE");

    private SQLJavaEditor javaEdit = new SQLJavaEditor(VariablePayeSQLElement.getMapTree());

    private float salBrut, cotPat, cotSal, netImp, netAPayer, csg;

    private Map<Integer, String> mapField;

    public FichePayeModel(int idFiche) {
        System.err.println("NEW FICHE PAYE MODEL");
        this.idFiche = idFiche;
        this.vectRubrique = new Vector<SQLRowValues>();
        this.vectRowValsToDelete = new Vector<SQLRowValues>();
        this.title = new String[9];
        this.title[0] = "Libellé";
        this.title[1] = "Base";
        this.title[2] = "Taux sal.";
        this.title[3] = "Montant sal. à ajouter";
        this.title[4] = "Montant sal. à déduire";
        this.title[5] = "Taux pat.";
        this.title[6] = "Montant pat.";
        this.title[7] = "Impression";
        this.title[8] = "Dans la Période";
        SQLTable tableNet = Configuration.getInstance().getBase().getTable("RUBRIQUE_NET");
        SQLTable tableBrut = Configuration.getInstance().getBase().getTable("RUBRIQUE_BRUT");
        SQLTable tableCotis = Configuration.getInstance().getBase().getTable("RUBRIQUE_COTISATION");
        SQLTable tableComm = Configuration.getInstance().getBase().getTable("RUBRIQUE_COMM");
        this.mapTableSource.put(tableNet.getName(), tableNet);
        this.mapTableSource.put(tableBrut.getName(), tableBrut);
        this.mapTableSource.put(tableCotis.getName(), tableCotis);
        this.mapTableSource.put(tableComm.getName(), tableComm);
        this.mapField = new HashMap<Integer, String>();
        this.mapField.put(new Integer(0), "NOM");
        this.mapField.put(new Integer(1), "NB_BASE");
        this.mapField.put(new Integer(2), "TAUX_SAL");
        this.mapField.put(new Integer(3), "MONTANT_SAL_AJ");
        this.mapField.put(new Integer(4), "MONTANT_SAL_DED");
        this.mapField.put(new Integer(5), "TAUX_PAT");
        this.mapField.put(new Integer(6), "MONTANT_PAT");
        this.mapField.put(new Integer(7), "IMPRESSION");
        this.mapField.put(new Integer(8), "IN_PERIODE");
    }

    private void resetValueFiche() {
        this.salBrut = 0.0F;
        this.cotPat = 0.0F;
        this.cotSal = 0.0F;
        this.netAPayer = 0.0F;
        this.netImp = 0.0F;
        this.csg = 0.0F;
    }

    public void loadAllElements() {
        System.err.println("Start At " + new Date());
        if (this.idFiche <= 1) {
            System.err.println("Aucune fiche associée");
            return;
        }
        resetValueFiche();
        this.vectRubrique = new Vector<SQLRowValues>();
        SQLRow rowFiche = tableFichePaye.getRow(this.idFiche);
        this.javaEdit.setSalarieID(rowFiche.getInt("ID_SALARIE"));
        SQLSelect selAllIDFicheElt = new SQLSelect(base);
        selAllIDFicheElt.addSelect(tableFichePayeElt.getField("ID"));
        selAllIDFicheElt.addSelect(tableFichePayeElt.getField("POSITION"));
        selAllIDFicheElt.setWhere(new Where(tableFichePayeElt.getField("ID_FICHE_PAYE"), "=", this.idFiche));
        selAllIDFicheElt.setDistinct(true);
        selAllIDFicheElt.addRawOrder("\"FICHE_PAYE_ELEMENT\".\"POSITION\"");
        String reqAllIDFichelElt = selAllIDFicheElt.asString();
        System.err.println("Request " + reqAllIDFichelElt);
        Object[] objIDFicheElt = ((List) base.getDataSource().execute(reqAllIDFichelElt, new ArrayListHandler())).toArray();
        System.err.println(objIDFicheElt.length + " elements to load");
        for (int i = 0; i < objIDFicheElt.length; i++) {
            SQLRow row = tableFichePayeElt.getRow(Integer.parseInt(((Object[]) objIDFicheElt[i])[0].toString()));
            String source = row.getString("SOURCE");
            int idSource = row.getInt("IDSOURCE");
            if (source.trim().length() != 0) {
                if (this.mapTableSource.get(source) != null) {
                    SQLRow rowSource = this.mapTableSource.get(source).getRow(idSource);
                    if (rowSource.getTable().getName().equalsIgnoreCase("RUBRIQUE_BRUT")) {
                        loadElementBrut(rowSource, row);
                    }
                    if (rowSource.getTable().getName().equalsIgnoreCase("RUBRIQUE_COTISATION")) {
                        loadElementCotisation(rowSource, row);
                    }
                    if (rowSource.getTable().getName().equalsIgnoreCase("RUBRIQUE_NET")) {
                        loadElementNet(rowSource, row);
                    }
                    if (rowSource.getTable().getName().equalsIgnoreCase("RUBRIQUE_COMM")) {
                        loadElementComm(rowSource, row);
                    }
                } else {
                    System.err.println("Table " + source + " non référencée");
                }
            }
        }
        System.err.println(this.vectRubrique.size() + " elements ADDed ");
        fireTableDataChanged();
        System.err.println("End At " + new Date());
    }

    public String getColumnName(int column) {
        return this.title[column];
    }

    public int getRowCount() {
        return this.vectRubrique.size();
    }

    public int getColumnCount() {
        return this.title.length;
    }

    public Object getValueAt(int rowIndex, int columnIndex) {
        SQLRowValues row = this.vectRubrique.get(rowIndex);
        Object o = null;
        if (row != null) {
            o = row.getObject(this.mapField.get(new Integer(columnIndex)).toString());
        }
        return o;
    }

    public Class<?> getColumnClass(int columnIndex) {
        Class<?> cl = tableFichePayeElt.getField(this.mapField.get(new Integer(columnIndex))).getType().getJavaType();
        return cl;
    }

    public boolean containValueAt(int rowIndex, int columnIndex) {
        if (columnIndex == 0) {
            return true;
        }
        SQLRowValues rowVals = this.vectRubrique.get(rowIndex);
        Object ob = rowVals.getObject("SOURCE");
        String source = (ob == null) ? "" : ob.toString();
        Object obId = rowVals.getObject("IDSOURCE");
        int idSource = (obId == null) ? 1 : rowVals.getInt("IDSOURCE");
        if ((source.trim().length() != 0) && (!source.equalsIgnoreCase("RUBRIQUE_COTISATION"))) {
            if (columnIndex > 4) {
                return false;
            } else {
                SQLRow row = this.mapTableSource.get(source).getRow(idSource);
                if (source.equalsIgnoreCase("RUBRIQUE_BRUT")) {
                    if ((row.getInt("ID_TYPE_RUBRIQUE_BRUT") == 2) && (columnIndex == 4)) {
                        return false;
                    }
                    if ((row.getInt("ID_TYPE_RUBRIQUE_BRUT") == 3) && (columnIndex == 3)) {
                        return false;
                    }
                } else {
                    if (source.equalsIgnoreCase("RUBRIQUE_NET")) {
                        if ((row.getInt("ID_TYPE_RUBRIQUE_NET") == 2) && (columnIndex == 4)) {
                            return false;
                        }
                        if ((row.getInt("ID_TYPE_RUBRIQUE_NET") == 3) && (columnIndex == 3)) {
                            return false;
                        }
                    } else {
                        return false;
                    }
                }
            }
        } else {
            if (columnIndex == 3) {
                return false;
            }
        }
        return true;
    }

    public void loadFromProfil(final int idProfil) {
        System.err.println("Load from profil");
        resetValueFiche();
        while (this.vectRubrique.size() > 0) {
            this.vectRowValsToDelete.add(this.vectRubrique.remove(0));
        }
        SQLSelect selAllIDProfilElt = new SQLSelect(Configuration.getInstance().getBase());
        selAllIDProfilElt.addSelect(tableProfilElt.getField("ID"));
        selAllIDProfilElt.addSelect(tableProfilElt.getField("POSITION"));
        selAllIDProfilElt.setWhere(new Where(tableProfilElt.getField("ID_PROFIL_PAYE"), "=", idProfil));
        selAllIDProfilElt.addRawOrder("\"PROFIL_PAYE_ELEMENT\".\"POSITION\"");
        String reqAllIDProfilElt = selAllIDProfilElt.asString();
        Object[] objIDProfilElt = ((List) Configuration.getInstance().getBase().getDataSource().execute(reqAllIDProfilElt, new ArrayListHandler())).toArray();
        for (int i = 0; i < objIDProfilElt.length; i++) {
            SQLRow rowTmp = tableProfilElt.getRow(Integer.parseInt(((Object[]) objIDProfilElt[i])[0].toString()));
            String source = rowTmp.getString("SOURCE");
            int idSource = rowTmp.getInt("IDSOURCE");
            if (this.mapTableSource.get(source) != null) {
                SQLRow row = this.mapTableSource.get(source).getRow(idSource);
                if (row.getTable().getName().equalsIgnoreCase("RUBRIQUE_BRUT")) {
                    loadElementBrut(row, null);
                }
                if (row.getTable().getName().equalsIgnoreCase("RUBRIQUE_COTISATION")) {
                    loadElementCotisation(row, null);
                }
                if (row.getTable().getName().equalsIgnoreCase("RUBRIQUE_NET")) {
                    loadElementNet(row, null);
                }
                if (row.getTable().getName().equalsIgnoreCase("RUBRIQUE_COMM")) {
                    loadElementComm(row, null);
                }
            } else {
                System.err.println("FichePayeModel.java --> Table non référencée dans la Map. Table name : " + source);
            }
        }
        fireTableDataChanged();
    }

    public String getSourceAt(int rowIndex) {
        return this.vectRubrique.get(rowIndex).getString("SOURCE");
    }

    public int upRow(int rowIndex) {
        if ((this.vectRubrique.size() > 1) && (rowIndex > 0)) {
            System.err.println("UP");
            SQLRowValues tmp = this.vectRubrique.get(rowIndex);
            this.vectRubrique.set(rowIndex, this.vectRubrique.get(rowIndex - 1));
            this.vectRubrique.set(rowIndex - 1, tmp);
            this.fireTableDataChanged();
            return rowIndex - 1;
        }
        System.err.println("can't up!!");
        return rowIndex;
    }

    public int downRow(int rowIndex) {
        if ((rowIndex >= 0) && (this.vectRubrique.size() > 1) && (rowIndex + 1 < this.vectRubrique.size())) {
            System.err.println("DOWN");
            SQLRowValues tmp = this.vectRubrique.get(rowIndex);
            this.vectRubrique.set(rowIndex, this.vectRubrique.get(rowIndex + 1));
            this.vectRubrique.set(rowIndex + 1, tmp);
            this.fireTableDataChanged();
            return rowIndex + 1;
        }
        System.err.println("can't down!!!");
        return rowIndex;
    }

    public void setLastRowAT(int rowIndex) {
        if ((rowIndex > 0) && (rowIndex < this.vectRubrique.size())) {
            this.vectRubrique.add(rowIndex, this.vectRubrique.remove(this.vectRubrique.size() - 1));
        }
        this.fireTableDataChanged();
    }

    public void setFicheID(int id) {
        this.idFiche = id;
        this.loadAllElements();
    }

    /***********************************************************************************************
     * Ajouter une ligne
     * 
     * @param row SQLRow RUBRIQUE_BRUT, RUBRIQUE_COTISATION, RUBRIQUE_NET, RUBRIQUE_COMM
     * @param index index ou doit etre insere la row
     */
    public void addRowAt(SQLRow row, int index) {
        int size = this.vectRubrique.size();
        if (row.getTable().getName().equalsIgnoreCase("RUBRIQUE_BRUT")) {
            this.loadElementBrut(row, null);
        } else {
            if (row.getTable().getName().equalsIgnoreCase("RUBRIQUE_COTISATION")) {
                this.loadElementCotisation(row, null);
            } else {
                if (row.getTable().getName().equalsIgnoreCase("RUBRIQUE_NET")) {
                    this.loadElementNet(row, null);
                } else {
                    if (row.getTable().getName().equalsIgnoreCase("RUBRIQUE_COMM")) {
                        this.loadElementComm(row, null);
                    }
                }
            }
        }
        if (size != this.vectRubrique.size()) {
            setLastRowAT(index);
        }
        if (!row.getTable().getName().equalsIgnoreCase("RUBRIQUE_COMM")) {
            calculValue();
        }
        this.fireTableDataChanged();
    }

    public void removeRow(int rowIndex) {
        if (rowIndex >= 0) {
            SQLRowValues rowVals = this.vectRubrique.remove(rowIndex);
            this.vectRowValsToDelete.add(rowVals);
            if (!rowVals.getString("SOURCE").equalsIgnoreCase("RUBRIQUE_COMM")) {
                calculValue();
            }
            this.fireTableDataChanged();
        }
    }

    public void updateFields(int idFiche) {
        for (int i = 0; i < this.vectRowValsToDelete.size(); i++) {
            SQLRowValues rowVals = this.vectRowValsToDelete.get(i);
            if (rowVals.getID() != SQLRow.NONEXISTANT_ID) {
                rowVals.put("ARCHIVE", 1);
                try {
                    rowVals.update();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        }
        this.vectRowValsToDelete = new Vector<SQLRowValues>();
        for (int i = 0; i < this.vectRubrique.size(); i++) {
            SQLRowValues rowVals = this.vectRubrique.get(i);
            rowVals.put("ID_FICHE_PAYE", idFiche);
            rowVals.put("POSITION", i);
            try {
                rowVals.commit();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    public void showData() {
        if (this.vectRubrique.size() == 0) {
            System.err.println("Vecteur contains no value.");
        }
        for (int i = 0; i < this.vectRubrique.size(); i++) {
            System.err.println(this.vectRubrique.get(i));
        }
    }

    public Object getVectorObjectAt(int index) {
        return this.vectRubrique.get(index);
    }

    private boolean isEltInPeriod(SQLRow rowSource) {
        SQLRow rowFiche = tableFichePaye.getRow(this.idFiche);
        int mois = rowFiche.getInt("ID_MOIS") - 1;
        Object ob = PeriodeValiditeSQLElement.mapTranslate().get(Integer.valueOf(mois));
        if (ob == null) {
            return false;
        }
        String moisName = ob.toString();
        SQLRow rowPeriodeValid = tableValidite.getRow(rowSource.getInt("ID_PERIODE_VALIDITE"));
        return (rowPeriodeValid.getBoolean(moisName));
    }

    private boolean isEltImprimable(SQLRow rowSource, SQLRowValues row) {
        int impression = rowSource.getInt("ID_IMPRESSION_RUBRIQUE");
        if (impression == 3) {
            return true;
        } else {
            if (impression == 4) {
                return false;
            } else {
                if (impression == 2) {
                    Object montantSalAjOb = row.getObject("MONTANT_SAL_AJ");
                    float montantSalAj = (montantSalAjOb == null) ? 0.0F : Float.valueOf(montantSalAjOb.toString()).floatValue();
                    Object montantSalDedOb = row.getObject("MONTANT_SAL_DED");
                    float montantSalDed = (montantSalDedOb == null) ? 0.0F : Float.valueOf(montantSalDedOb.toString()).floatValue();
                    Object montantPatOb = row.getObject("MONTANT_PAT");
                    float montantPat = (montantPatOb == null) ? 0.0F : Float.valueOf(montantPatOb.toString()).floatValue();
                    if (montantSalAj == 0 && montantSalDed == 0 && montantPat == 0) {
                        return false;
                    }
                    return true;
                }
            }
        }
        return true;
    }

    /**
     * charge un élément de la fiche dans rowVals, dont la rubrique source est rowSource et
     * l'élément row si l'élément existe déja
     * 
     * @param rowVals
     * @param rowSource
     * @param row
     * @return true si on doit calculer les valeurs
     */
    private boolean loadElement(SQLRowValues rowVals, SQLRow rowSource, SQLRow row) {
        if (row != null) {
            rowVals.loadAbsolutelyAll(row);
        }
        rowVals.put("IN_PERIODE", Boolean.valueOf(isEltInPeriod(rowSource)));
        rowVals.put("SOURCE", rowSource.getTable().getName());
        rowVals.put("IDSOURCE", rowSource.getID());
        Object ob = rowVals.getObject("VALIDE");
        boolean b = (ob == null) ? false : new Boolean(ob.toString()).booleanValue();
        return b;
    }

    private void updateValueFiche() {
        SQLRowValues rowValsFiche = new SQLRowValues(tableFichePaye);
        rowValsFiche.put("SAL_BRUT", Float.valueOf(this.salBrut));
        rowValsFiche.put("NET_IMP", Float.valueOf(this.netImp + this.salBrut));
        rowValsFiche.put("NET_A_PAYER", Float.valueOf(this.netAPayer + this.salBrut));
        rowValsFiche.put("COT_SAL", Float.valueOf(this.cotSal));
        rowValsFiche.put("COT_PAT", Float.valueOf(this.cotPat));
        rowValsFiche.put("CSG", Float.valueOf((this.salBrut + this.csg) * 0.97F));
        try {
            rowValsFiche.update(this.idFiche);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    /***********************************************************************************************
     * Charge un élément d'une rubrique de brut
     * 
     * @param rowSource row de la rubrique source
     * @param row row de l'élément de la fiche
     **********************************************************************************************/
    private void loadElementBrut(SQLRow rowSource, SQLRow row) {
        SQLRowValues rowVals = new SQLRowValues(tableFichePayeElt);
        if (!loadElement(rowVals, rowSource, row)) {
            Object baseOb = this.javaEdit.checkFormule(rowSource.getString("BASE"), "BASE");
            Object tauxSalOb = this.javaEdit.checkFormule(rowSource.getString("TAUX"), "TAUX");
            rowVals.put("NOM", rowSource.getString("NOM"));
            rowVals.put("NB_BASE", (baseOb == null) ? null : Float.valueOf(Math.round(Float.valueOf(baseOb.toString()).floatValue() * 100.0F) / 100.0F));
            rowVals.put("TAUX_SAL", (tauxSalOb == null) ? null : Float.valueOf(Float.valueOf(tauxSalOb.toString()).floatValue()));
        }
        calculBrut(rowSource, rowVals);
        boolean b = isEltImprimable(rowSource, rowVals);
        rowVals.put("IMPRESSION", Boolean.valueOf(b));
        this.vectRubrique.add(rowVals);
    }

    /**
     * Calcul le montant d'une rubrique de brut et met à jour les variables du salarié
     * 
     * @param rowSource
     * @param rowVals
     */
    private void calculBrut(SQLRow rowSource, SQLRowValues rowVals) {
        if (((Boolean) rowVals.getObject("IN_PERIODE")).booleanValue()) {
            Object baseOb = rowVals.getObject("NB_BASE");
            Object tauxSalOb = rowVals.getObject("TAUX_SAL");
            float base = 0.0F;
            if ((baseOb != null) && (baseOb.toString().trim().length() != 0)) {
                base = Float.valueOf(baseOb.toString()).floatValue();
            }
            float tauxSal = 0.0F;
            if ((tauxSalOb != null) && (tauxSalOb.toString().trim().length() != 0)) {
                tauxSal = Float.valueOf(tauxSalOb.toString()).floatValue();
            }
            String formuleMontant = rowSource.getString("MONTANT");
            float montant = 0.0F;
            if (formuleMontant.trim().length() == 0) {
                montant = Math.round(base * tauxSal * 100.0F) / 100.0F;
            } else {
                Object montantNet = this.javaEdit.checkFormule(rowSource.getString("MONTANT"), "MONTANT");
                String montantNetS = (montantNet == null) ? "0.0" : montantNet.toString();
                montant = Math.round(Float.valueOf(montantNetS).floatValue() * 100.0F) / 100.0F;
            }
            if (rowSource.getInt("ID_TYPE_RUBRIQUE_BRUT") == 3) {
                rowVals.put("MONTANT_SAL_DED", Float.valueOf(montant));
                this.salBrut -= montant;
            } else {
                rowVals.put("MONTANT_SAL_AJ", Float.valueOf(montant));
                this.salBrut += montant;
            }
            updateValueFiche();
        }
    }

    private void calculNet(SQLRow rowSource, SQLRowValues rowVals) {
        if (((Boolean) rowVals.getObject("IN_PERIODE")).booleanValue()) {
            Object baseOb = rowVals.getObject("NB_BASE");
            Object tauxSalOb = rowVals.getObject("TAUX_SAL");
            float base = 0.0F;
            if ((baseOb != null) && (baseOb.toString().trim().length() != 0)) {
                base = ((Float) baseOb).floatValue();
            }
            float tauxSal = 0.0F;
            if ((tauxSalOb != null) && (tauxSalOb.toString().trim().length() != 0)) {
                tauxSal = ((Float) tauxSalOb).floatValue();
            }
            String formuleMontant = rowSource.getString("MONTANT");
            float montant = 0.0F;
            if (formuleMontant.trim().length() == 0) {
                montant = Math.round(base * tauxSal * 100.0F) / 100.0F;
            } else {
                Object montantNet = this.javaEdit.checkFormule(rowSource.getString("MONTANT"), "MONTANT");
                if (montantNet != null) {
                    montant = Math.round(Float.valueOf(montantNet.toString()).floatValue() * 100.0F) / 100.0F;
                }
            }
            if (rowSource.getInt("ID_TYPE_RUBRIQUE_NET") == 3) {
                rowVals.put("MONTANT_SAL_DED", Float.valueOf(montant));
                if (rowSource.getBoolean("IMPOSABLE")) {
                    this.netAPayer -= montant;
                } else {
                    this.netAPayer -= montant;
                    this.netImp -= montant;
                }
            } else {
                rowVals.put("MONTANT_SAL_AJ", Float.valueOf(montant));
                if (rowSource.getBoolean("IMPOSABLE")) {
                    this.netAPayer += montant;
                } else {
                    this.netAPayer += montant;
                    this.netImp += montant;
                }
            }
            updateValueFiche();
        }
    }

    private void loadElementNet(SQLRow rowSource, SQLRow row) {
        SQLRowValues rowVals = new SQLRowValues(tableFichePayeElt);
        if (!loadElement(rowVals, rowSource, row)) {
            Object baseOb = this.javaEdit.checkFormule(rowSource.getString("BASE"), "BASE");
            Object tauxSalOb = this.javaEdit.checkFormule(rowSource.getString("TAUX"), "TAUX");
            rowVals.put("NOM", rowSource.getString("NOM"));
            rowVals.put("NB_BASE", (baseOb == null) ? null : Float.valueOf(Math.round(Float.valueOf(baseOb.toString()).floatValue() * 100.0F) / 100.0F));
            rowVals.put("TAUX_SAL", (tauxSalOb == null) ? null : Float.valueOf(Float.valueOf(tauxSalOb.toString()).floatValue()));
        }
        calculNet(rowSource, rowVals);
        boolean b = isEltImprimable(rowSource, rowVals);
        rowVals.put("IMPRESSION", Boolean.valueOf(b));
        this.vectRubrique.add(rowVals);
    }

    private void calculCotisation(SQLRow rowSource, SQLRowValues rowVals) {
        if (((Boolean) rowVals.getObject("IN_PERIODE")).booleanValue()) {
            Object baseOb = rowVals.getObject("NB_BASE");
            Object tauxSalOb = rowVals.getObject("TAUX_SAL");
            Object tauxPatOb = rowVals.getObject("TAUX_PAT");
            float base = 0.0F;
            if ((baseOb != null) && (baseOb.toString().trim().length() != 0)) {
                base = ((Number) baseOb).floatValue();
            }
            float tauxSal = 0.0F;
            if ((tauxSalOb != null) && (tauxSalOb.toString().trim().length() != 0)) {
                tauxSal = ((Number) tauxSalOb).floatValue() / 100.0F;
            }
            float tauxPat = 0.0F;
            if ((tauxPatOb != null) && (tauxPatOb.toString().trim().length() != 0)) {
                tauxPat = ((Number) tauxPatOb).floatValue() / 100.0F;
            }
            float montantSal = 0.0F;
            montantSal = Math.round(base * tauxSal * 100.0F) / 100.0F;
            float montantPat = 0.0F;
            montantPat = Math.round(base * tauxPat * 100.0F) / 100.0F;
            rowVals.put("MONTANT_SAL_DED", new Float(montantSal));
            rowVals.put("MONTANT_PAT", new Float(montantPat));
            if (rowSource.getBoolean("IMPOSABLE")) {
                this.netAPayer -= montantSal;
            } else {
                this.netAPayer -= montantSal;
                this.netImp -= montantSal;
            }
            if (rowSource.getBoolean("PART_CSG")) {
                this.csg += montantPat;
            }
            this.cotSal += montantSal;
            this.cotPat += montantPat;
            updateValueFiche();
        }
    }

    private void loadElementCotisation(SQLRow rowSource, SQLRow row) {
        SQLRowValues rowVals = new SQLRowValues(tableFichePayeElt);
        if (!loadElement(rowVals, rowSource, row)) {
            Object baseOb = this.javaEdit.checkFormule(rowSource.getString("BASE"), "BASE");
            Object tauxSalOb = this.javaEdit.checkFormule(rowSource.getString("TX_SAL"), "TX_SAL");
            Object tauxPatOb = this.javaEdit.checkFormule(rowSource.getString("TX_PAT"), "TX_PAT");
            rowVals.put("NOM", rowSource.getString("NOM"));
            rowVals.put("NB_BASE", (baseOb == null) ? null : Float.valueOf(Math.round(Float.valueOf(baseOb.toString()).floatValue() * 100.0F) / 100.0F));
            rowVals.put("TAUX_SAL", (tauxSalOb == null) ? null : Float.valueOf(Float.valueOf(tauxSalOb.toString()).floatValue()));
            rowVals.put("TAUX_PAT", (tauxPatOb == null) ? null : Float.valueOf(Float.valueOf(tauxPatOb.toString()).floatValue()));
        }
        calculCotisation(rowSource, rowVals);
        boolean b = isEltImprimable(rowSource, rowVals);
        rowVals.put("IMPRESSION", Boolean.valueOf(b));
        this.vectRubrique.add(rowVals);
    }

    public void loadElementComm(SQLRow rowSource, SQLRow row) {
        SQLRowValues rowVals = new SQLRowValues(tableFichePayeElt);
        if (loadElement(rowVals, rowSource, row)) {
            this.vectRubrique.add(rowVals);
            return;
        }
        Object baseOb = this.javaEdit.checkFormule(rowSource.getString("NB_BASE"), "BASE");
        Object tauxSalOb = this.javaEdit.checkFormule(rowSource.getString("TAUX_SAL"), "SAL");
        Object tauxPatOb = this.javaEdit.checkFormule(rowSource.getString("TAUX_PAT"), "PAT");
        Object montantPatOb = this.javaEdit.checkFormule(rowSource.getString("MONTANT_PAT"), "MONTANT");
        Object montantAdOb = this.javaEdit.checkFormule(rowSource.getString("MONTANT_SAL_AJ"), "MONTANT");
        Object montantDedOb = this.javaEdit.checkFormule(rowSource.getString("MONTANT_SAL_DED"), "MONTANT");
        rowVals.put("NOM", rowSource.getBoolean("NOM_VISIBLE") ? rowSource.getString("NOM") : "");
        rowVals.put("NB_BASE", baseOb);
        rowVals.put("TAUX_SAL", tauxSalOb);
        rowVals.put("TAUX_PAT", tauxPatOb);
        rowVals.put("MONTANT_PAT", montantPatOb);
        rowVals.put("MONTANT_SAL_AJ", montantAdOb);
        rowVals.put("MONTANT_SAL_DED", montantDedOb);
        boolean b = isEltImprimable(rowSource, rowVals);
        rowVals.put("IMPRESSION", Boolean.valueOf(b));
        this.vectRubrique.add(rowVals);
    }

    private void calculValue() {
        System.err.println("Start calculValue At " + new Date());
        resetValueFiche();
        Vector<SQLRowValues> vectTmp = new Vector<SQLRowValues>(this.vectRubrique);
        this.vectRubrique = new Vector<SQLRowValues>();
        for (int i = 0; i < vectTmp.size(); i++) {
            SQLRowValues rowVals = vectTmp.get(i);
            String source = rowVals.getString("SOURCE");
            int idSource = rowVals.getInt("IDSOURCE");
            SQLRow row = tableFichePayeElt.getRow(rowVals.getID());
            if (source.trim().length() != 0) {
                if (this.mapTableSource.get(source) != null) {
                    SQLRow rowSource = this.mapTableSource.get(source).getRow(idSource);
                    if (rowSource.getTable().getName().equalsIgnoreCase("RUBRIQUE_BRUT")) {
                        loadElementBrut(rowSource, row);
                    }
                    if (rowSource.getTable().getName().equalsIgnoreCase("RUBRIQUE_COTISATION")) {
                        loadElementCotisation(rowSource, row);
                    }
                    if (rowSource.getTable().getName().equalsIgnoreCase("RUBRIQUE_NET")) {
                        loadElementNet(rowSource, row);
                    }
                    if (rowSource.getTable().getName().equalsIgnoreCase("RUBRIQUE_COMM")) {
                        loadElementComm(rowSource, row);
                    }
                } else {
                    System.err.println("Table " + source + " non référencée");
                }
            }
        }
        System.err.println(this.vectRubrique.size() + " elements ADDed ");
        fireTableDataChanged();
        System.err.println("End calculValue At " + new Date());
    }

    public void validElt() {
        System.err.println("Validation des éléments de la fiche.");
        for (int i = 0; i < this.vectRubrique.size(); i++) {
            SQLRowValues rowVals = this.vectRubrique.get(i);
            rowVals.put("VALIDE", Boolean.valueOf(true));
            try {
                rowVals.commit();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        System.err.println("Validation terminée.");
    }
}
