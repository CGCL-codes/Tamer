package org.fudaa.ebli.palette;

import java.awt.Component;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.FontMetrics;
import java.util.ArrayList;
import java.util.HashMap;
import javax.swing.DefaultCellEditor;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.ScrollPaneConstants;
import javax.swing.SwingConstants;
import javax.swing.table.AbstractTableModel;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.TableCellEditor;
import javax.swing.table.TableCellRenderer;
import org.fudaa.ctulu.CtuluCommandContainer;
import org.fudaa.ctulu.CtuluCommandManager;
import org.fudaa.ctulu.table.CtuluTableCellEditorProxy;
import org.fudaa.ctulu.editor.CtuluValueEditorI;
import org.fudaa.ebli.commun.EbliLib;
import com.memoire.bu.BuBorderLayout;
import com.memoire.bu.BuPanel;
import com.memoire.bu.BuScrollPane;
import com.memoire.bu.BuTextField;

/**
 * Un panel affichant les infos envoyes.
 * 
 * @author deniger
 * @version $Id: BPaletteInfo.java 6189 2011-03-30 12:30:13Z bmarchan $
 */
public abstract class BPaletteInfo extends BuPanel {

    /**
   * @author fred deniger
   * @version $Id: BPaletteInfo.java 6189 2011-03-30 12:30:13Z bmarchan $
   */
    public interface InfoData {

        /**
     * Permet d'ajouter une propri�t� non �ditable.
     * @param _key le nom de la propri�t�
     * @param _value la valeur de la propri�t�
     */
        void put(String _key, String _value);

        /**
     * Permet d'ajouter une information �ditable au tableau.
     * @param _key le nom de la propri�t�, doit �tre non vide
     * @param _value la valeur de la propri�t�, peut �tre null
     * @param _editor l'�diteur, si null l'�diteur par defaut sera utilis�
     * @param _renderer le renderer, si null le renderer par defaut sera utilis�
     * @param _owner l'object qui sera appel� (via modifyProperty) pour que 
     *                la modification soit prise en compte. Si null l'attribut est
     *                consid�r� comme non modifiable.
     * @param _index un tableau de int contenant les index des g�om�tries � modifier.
     */
        void put(String _key, Object _value, CtuluValueEditorI _editor, TableCellRenderer _renderer, ModifyPropertyInfo _owner, int[] _data);

        void setTitle(String _title);

        /**
     * Indique si le tableau contient des valeurs agr�g�es (donc
     *          venant de plusieurs g�om�tries) ou des valeurs unique (venant
     *          d'une seul g�om�trie). Selon le cas createCommonEditorComponent
     *          ou createEditorComponent sera appell�.
     * @param _value
     */
        public void setOnlyOneGeometry(boolean _value);

        boolean isTitleSet();
    }

    /**
   * Interface allant de pair avec InfoData. Elle indique que la m�thode modifyProperty
   * est impl�ment�, permettant ainsi de r�percuter les modification du tableau sur
   * l'objet concern�.
   * @author Emmanuel MARTIN
   * @version $Id: BPaletteInfo.java 6189 2011-03-30 12:30:13Z bmarchan $
   */
    public interface ModifyPropertyInfo {

        /**
     * M�thode appell�e lors de la modification de la valeur de la propri�t�
     * fournie � InfoData.
     * @param _key le nom de la propri�t�
     * @param _newValue la nouvelle valeur de la prorpi�t�
     * @param _index l'information pass� en param�tre du put
     * @param _cmd le gestionnaire de commande pour le undo/redo
     */
        void modifyProperty(String _key, Object _newValue, int[] _index, CtuluCommandContainer _cmd);
    }

    /**
   * @author Fred Deniger, Emmanuel Martin
   * @version $Id: BPaletteInfo.java 6189 2011-03-30 12:30:13Z bmarchan $
   */
    public final class PanelTableModel extends AbstractTableModel implements InfoData {

        /**
     * Chaque ligne est organis� suivant ce model : nom (string), valeur
     * (object), editor (TableCellEditor), renderer (TableCellRenderer), owner
     * (ModifyPropertyInfo), data (Object).
     */
        private ArrayList<ArrayList<Object>> rows_;

        /** L'editor du tableau. */
        private CtuluTableCellEditorProxy editor_;

        /** vrai si le model ne d�crit qu'une seul g�om�trie en ce moment. */
        private boolean onlyOneGeometry_;

        private class PanelTableRenderer extends DefaultTableCellRenderer {

            public Component getTableCellRendererComponent(JTable _table, Object _value, boolean _isSelected, boolean _hasFocus, int _row, int _column) {
                if (rows_.get(_row).get(3) != null) return ((TableCellRenderer) rows_.get(_row).get(3)).getTableCellRendererComponent(_table, _value, _isSelected, _hasFocus, _row, _column); else {
                    Component comp = super.getTableCellRendererComponent(_table, _value, _isSelected, _hasFocus, _row, _column);
                    if (rows_.get(_row).get(4) == null) comp.setEnabled(false); else comp.setEnabled(true);
                    return comp;
                }
            }
        }

        public TableCellRenderer getRenderer() {
            return new PanelTableRenderer();
        }

        public TableCellEditor getEditor() {
            if (editor_ == null) {
                HashMap<Integer, TableCellEditor> editors = new HashMap<Integer, TableCellEditor>();
                for (int i = 0; i < rows_.size(); i++) if (rows_.get(i).get(4) != null) {
                    if (rows_.get(i).get(3) != null) if (onlyOneGeometry_) editors.put(i, ((CtuluValueEditorI) rows_.get(i).get(2)).createTableEditorComponent()); else editors.put(i, ((CtuluValueEditorI) rows_.get(i).get(2)).createCommonTableEditorComponent());
                } else editors.put(i, null);
                editors.put(-1, new DefaultCellEditor(new BuTextField()));
                editor_ = new CtuluTableCellEditorProxy(editors, -1);
            }
            return editor_;
        }

        public boolean isCellEditable(int rowIndex, int columnIndex) {
            if (columnIndex == 1) return true; else return false;
        }

        public PanelTableModel() {
            rows_ = new ArrayList<ArrayList<Object>>();
            onlyOneGeometry_ = false;
        }

        public void setOnlyOneGeometry(boolean _value) {
            if (onlyOneGeometry_ != _value) {
                onlyOneGeometry_ = _value;
                editor_.clear();
                for (int i = 0; i < rows_.size(); i++) if (rows_.get(i).get(4) != null) {
                    if (rows_.get(i).get(3) != null) if (onlyOneGeometry_) editor_.putEditor(i, ((CtuluValueEditorI) rows_.get(i).get(2)).createTableEditorComponent()); else editor_.putEditor(i, ((CtuluValueEditorI) rows_.get(i).get(2)).createCommonTableEditorComponent());
                } else editor_.putEditor(i, null);
                editor_.putEditor(-1, new DefaultCellEditor(new BuTextField()));
            }
        }

        public void clear() {
            rows_.clear();
        }

        public int getColumnCount() {
            return 2;
        }

        public String getColumnName(final int _column) {
            if (_column == 0) {
                return EbliLib.getS("Nom");
            }
            return EbliLib.getS("Valeur");
        }

        public int getRowCount() {
            return rows_.size();
        }

        public Object getValueAt(final int _row, final int _col) {
            if (_row < 0 || _col < 0 || _row >= rows_.size() || _col > getColumnCount()) return null; else return rows_.get(_row).get(_col);
        }

        public void put(final String _name, final String _value) {
            ArrayList<Object> row = new ArrayList<Object>();
            row.add(0, _name);
            row.add(1, _value);
            row.add(2, null);
            row.add(3, null);
            row.add(4, null);
            row.add(5, null);
            rows_.add(row);
            editor_.putEditor(rows_.size() - 1, null);
        }

        public void put(String _key, Object _value, CtuluValueEditorI _editor, TableCellRenderer _renderer, ModifyPropertyInfo _owner, int[] _index) {
            if (_key == null) throw new IllegalArgumentException("_key et _value doivent �tre non null.");
            ArrayList<Object> row = new ArrayList<Object>();
            row.add(0, _key);
            row.add(1, _value);
            row.add(2, _editor);
            row.add(3, _renderer);
            row.add(4, _owner);
            row.add(5, _index);
            rows_.add(row);
            if (onlyOneGeometry_) editor_.putEditor(rows_.size() - 1, _editor.createTableEditorComponent()); else editor_.putEditor(rows_.size() - 1, _editor.createCommonTableEditorComponent());
        }

        public void setTitle(final String _title) {
            txtTitle_.setText(_title);
        }

        public void setValueAt(Object _value, int _rowIndex, int _columnIndex) {
            if (_rowIndex < 0 || _rowIndex >= rows_.size() || _columnIndex < 0 || _columnIndex > getColumnCount()) return;
            if (rows_.get(_rowIndex).get(_columnIndex) != _value && rows_.get(_rowIndex).get(4) != null) {
                rows_.get(_rowIndex).set(_columnIndex, _value);
                if (rows_.get(_rowIndex).get(2) != null && ((CtuluValueEditorI) rows_.get(_rowIndex).get(2)).isValid(_value)) ((ModifyPropertyInfo) rows_.get(_rowIndex).get(4)).modifyProperty((String) rows_.get(_rowIndex).get(0), _value, (int[]) rows_.get(_rowIndex).get(5), cmd_);
            }
        }

        public boolean isTitleSet() {
            return txtTitle_.getText() != null && txtTitle_.getText().trim().length() > 0;
        }
    }

    protected boolean isAvailable_;

    protected Component parent_;

    protected JTable table_;

    protected transient PanelTableModel tableModel_;

    public PanelTableModel getTableModel() {
        return tableModel_;
    }

    protected JTextField txtTitle_;

    protected CtuluCommandManager cmd_;

    public BPaletteInfo(CtuluCommandManager _cmd) {
        cmd_ = _cmd;
        setLayout(new BuBorderLayout());
        txtTitle_ = new BuTextField();
        final Font f = txtTitle_.getFont();
        txtTitle_.setFont(new Font(f.getFamily(), Font.BOLD, f.getSize()));
        txtTitle_.setHorizontalAlignment(SwingConstants.CENTER);
        txtTitle_.setEditable(false);
        add(txtTitle_, BuBorderLayout.NORTH);
        setPreferredSize(new Dimension(300, 200));
        tableModel_ = new PanelTableModel();
        table_ = new JTable(tableModel_);
        table_.getColumnModel().getColumn(1).setCellRenderer(tableModel_.getRenderer());
        table_.getColumnModel().getColumn(1).setCellEditor(tableModel_.getEditor());
        table_.setAutoResizeMode(JTable.AUTO_RESIZE_ALL_COLUMNS);
        final JScrollPane sp = new BuScrollPane(table_);
        sp.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_AS_NEEDED);
        sp.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED);
        add(sp, BuBorderLayout.CENTER);
    }

    protected final void updateSize() {
        final int w = getFirstWidth();
        if (w > 0) {
            table_.getColumnModel().getColumn(0).setPreferredWidth(w);
            table_.getColumnModel().getColumn(0).setWidth(w);
        }
        table_.repaint();
    }

    public final int getFirstWidth() {
        final int max = this.tableModel_.getRowCount() - 1;
        if (max <= 0) {
            return 0;
        }
        int maxW = 0;
        final Font f = table_.getFont();
        if (getGraphics() == null) {
            return 0;
        }
        final FontMetrics m = getGraphics().getFontMetrics(f);
        int w;
        for (int i = max; i >= 0; i--) {
            w = m.stringWidth((String) tableModel_.getValueAt(i, 0));
            if (w > maxW) {
                maxW = w;
            }
        }
        return maxW;
    }

    public final void infoChanged() {
        if (isAvailable_) {
            updateState();
        }
    }

    /**
   * Retourne la validit� de la palette.
   * @return True si la palette est visible.
   */
    public final boolean isAvailable() {
        return isAvailable_;
    }

    public final void setAvailable(final boolean _isAvailable) {
        isAvailable_ = _isAvailable;
    }

    /**
   * Mise a jour de la palette lorsqu'elle est r�affich�e.
   */
    public abstract void updateState();
}
