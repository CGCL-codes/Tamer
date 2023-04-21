package org.fudaa.ebli.commun;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import javax.swing.event.TableModelListener;
import javax.swing.table.AbstractTableModel;
import org.fudaa.ctulu.CtuluLibString;
import org.fudaa.ebli.ressource.EbliResource;

/**
 * Modele de tableau qui utilise une map <str, str>. Affiche des couple clef/valeurs de la map tri�es. Utilis� en
 * particulier pour les infos de creation des extends zeblicalquePanel. Prend en charge l'�dition dans la map. Organise
 * les infos par ordre lexicographique. Pour changer le trie a sa guise, faire son comparator et le placer dans le bon
 * constructeur.
 * 
 * @author Adrien Hadoux
 */
public class EbliModelInfos extends AbstractTableModel {

    private static final long serialVersionUID = -2080709568281587469L;

    private List<String> listeKey_;

    private List<String> listeValue_;

    private final List<String> colonnes_;

    /**
   * Les infos de la table.
   */
    private Map<String, String> infos_;

    public EbliModelInfos(List<String> liste, Map<String, String> map, List<String> colonnes) {
        super();
        listeKey_ = liste;
        infos_ = map;
        colonnes_ = colonnes;
        listeValue_ = new ArrayList<String>();
        for (String key : liste) listeValue_.add(map.get(key));
    }

    @Override
    public String getColumnName(int column) {
        return colonnes_.get(column);
    }

    public int getRowCount() {
        return infos_.keySet().size();
    }

    public Object getValueAt(int row, int column) {
        if (column == 0) return getKey(row); else return getValue(row);
    }

    public void setValueAt(Object value, int row, int column) {
        if (value == null) return;
        if (row == getRowCount() - 1 && !((String) value).equals(EbliResource.EBLI.getString("Editable"))) {
            if (column == 1) {
                String newKey = "Note " + (infos_.keySet().size() + 1);
                infos_.put(newKey, (String) value);
                listeKey_.add(newKey);
                listeValue_.add((String) value);
            } else {
                infos_.put((String) value, "");
                listeKey_.add((String) value);
                listeValue_.add("");
            }
            fireTableDataChanged();
        } else {
            if (column == 0) {
                String res = infos_.get(getKey(row));
                infos_.remove(getKey(row));
                infos_.put((String) value, res);
                listeKey_.set(row, (String) value);
            } else {
                infos_.put(getKey(row), (String) value);
                listeValue_.set(row, (String) value);
            }
            fireTableDataChanged();
        }
    }

    public String getKey(int row) {
        if (row < 0 || row >= infos_.size()) return "";
        return listeKey_.get(row);
    }

    public void fireTableDataChanged() {
        super.fireTableDataChanged();
    }

    public String getValue(int row) {
        if (row < 0 || row > listeValue_.size() - 1) return CtuluLibString.EMPTY_STRING;
        return listeValue_.get(row);
    }

    @Override
    public boolean isCellEditable(int row, int column) {
        return false;
    }

    @Override
    public void addTableModelListener(TableModelListener l) {
    }

    @Override
    public Class<?> getColumnClass(int columnIndex) {
        return String.class;
    }

    public int getColumnCount() {
        return colonnes_.size();
    }

    public void removeTableModelListener(TableModelListener l) {
    }
}
