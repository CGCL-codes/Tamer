package org.fudaa.fudaa.tr.data;

import java.awt.event.ActionEvent;
import javax.swing.JTable;
import javax.swing.table.AbstractTableModel;
import javax.swing.table.TableCellEditor;
import javax.swing.table.TableModel;
import com.memoire.bu.BuBorderLayout;
import com.memoire.bu.BuScrollPane;
import com.vividsolutions.jts.geom.LineString;
import org.fudaa.ctulu.CtuluLib;
import org.fudaa.ctulu.editor.CtuluValueEditorDouble;
import org.fudaa.ctulu.gis.GISAttributeModelDoubleArray;
import org.fudaa.ctulu.gis.GISLib;
import org.fudaa.ctulu.gui.CtuluDialogPanel;
import org.fudaa.ebli.commun.EbliActionSimple;
import org.fudaa.ebli.commun.EbliListeSelectionMultiInterface;
import org.fudaa.fudaa.tr.common.TrResource;

class TrSiInterpolAction extends EbliActionSimple {

    /**
   * @author fred deniger
   * @version $Id: TrSiInterpolAction.java,v 1.7 2007-05-04 14:01:51 deniger Exp $
   */
    protected static final class SiTableModel extends AbstractTableModel {

        /**
     * 
     */
        private final int[] select_;

        /**
     * 
     */
        private final Double[] values_;

        /**
     * 
     */
        private final LineString str_;

        /**
     * 
     */
        private final int select2_;

        /**
     * @param _select
     * @param _values
     * @param _str
     * @param _select2
     */
        protected SiTableModel(final int[] _select, final Double[] _values, final LineString _str, final int _select2) {
            select_ = _select;
            values_ = _values;
            str_ = _str;
            select2_ = _select2;
        }

        public Object getValueAt(final int _rowIndex, final int _columnIndex) {
            if (_columnIndex == 0) {
                return new Integer(select_[_rowIndex] + 1);
            } else if (_columnIndex == 1) {
                return CtuluLib.getDouble(str_.getCoordinateSequence().getX(select_[_rowIndex]));
            } else if (_columnIndex == 2) {
                return CtuluLib.getDouble(str_.getCoordinateSequence().getY(select_[_rowIndex]));
            }
            return values_[_rowIndex];
        }

        @Override
        public boolean isCellEditable(final int _rowIndex, final int _columnIndex) {
            return _columnIndex > 2;
        }

        @Override
        public Class getColumnClass(final int _columnIndex) {
            if (_columnIndex == 0) {
                return Integer.class;
            }
            return Double.class;
        }

        @Override
        public void setValueAt(final Object _value, final int _rowIndex, final int _columnIndex) {
            if (_value != null && _columnIndex == 3) {
                values_[_rowIndex] = CtuluLib.getDouble(((Double) _value).doubleValue());
            }
        }

        @Override
        public String getColumnName(final int _column) {
            if (_column == 0) {
                return TrResource.getS("Indices");
            }
            if (_column == 1) {
                return "X";
            }
            if (_column == 2) {
                return "Y";
            }
            return TrResource.getS("Valeurs");
        }

        public int getRowCount() {
            return select2_;
        }

        public int getColumnCount() {
            return 4;
        }
    }

    private final TrSiProfilLayer layer_;

    protected TrSiInterpolAction(final TrSiProfilLayer _layer) {
        super(CtuluLib.getS("Interpoler les valeurs"), null, "INTERPOL_PROFIL");
        layer_ = _layer;
        setDefaultToolTip(TrResource.getS("Interpolation de la surface libre sur une s�rie de points"));
    }

    @Override
    public void actionPerformed(final ActionEvent _e) {
        if (layer_.isSelectionEmpty()) {
            return;
        }
        int[] selec = null;
        int profilIdx = -1;
        if (layer_.isAtomicMode()) {
            profilIdx = layer_.getLayerSelectionMulti().isSelectionInOneBloc();
            if (profilIdx < 0 || !layer_.getProfilModel().isProfile(profilIdx) || layer_.getLayerSelectionMulti().getSelection(profilIdx).getNbSelectedIndex() < 2) {
                return;
            }
            selec = layer_.getLayerSelectionMulti().getSelection(profilIdx).getSelectedIndex();
        } else if (layer_.isOnlyOneObjectSelected()) {
            final int idx = layer_.getLayerSelection().getMinIndex();
            if (!layer_.getProfilModel().isProfile(idx)) {
                return;
            }
            profilIdx = idx;
            final LineString str = layer_.getProfilModel().getLine(idx);
            selec = new int[] { 0, str.getNumPoints() - 1 };
        }
        if (profilIdx < 0 || selec == null) {
            return;
        }
        final int[] idxSelect = selec;
        final int nbSelect = selec.length;
        final GISAttributeModelDoubleArray data = (GISAttributeModelDoubleArray) layer_.getProfilModel().getCoteModel(profilIdx);
        final CtuluValueEditorDouble editor = new CtuluValueEditorDouble();
        final Double[] values = new Double[nbSelect];
        final LineString str = layer_.getProfilModel().getLine(profilIdx);
        for (int i = values.length - 1; i >= 0; i--) {
            values[i] = CtuluLib.getDouble(data.getValue(idxSelect[i]));
        }
        final TableModel model = new SiTableModel(idxSelect, values, str, nbSelect);
        final JTable table = new JTable(model);
        final CtuluDialogPanel pn = new CtuluDialogPanel() {

            @Override
            public boolean valide() {
                final TableCellEditor cellEditor = table.getCellEditor();
                if (cellEditor != null) {
                    cellEditor.stopCellEditing();
                }
                return true;
            }
        };
        pn.setLayout(new BuBorderLayout());
        table.getColumnModel().getColumn(3).setCellEditor(editor.createTableEditorComponent());
        pn.add(new BuScrollPane(table));
        if (CtuluDialogPanel.isOkResponse(pn.afficheModale(layer_.editor_.getFrame(), super.getTitle()))) {
            final double[] oldVal = data.getValues();
            final LineString ls = layer_.getProfilModel().getLine(profilIdx);
            for (int i = 1; i < nbSelect; i++) {
                final int idxMin = selec[i - 1];
                final int idxMax = selec[i];
                final double vali = values[i].doubleValue();
                final double valiPrec = values[i - 1].doubleValue();
                final double[] vals = GISLib.interpolate(ls, idxMin, idxMax, valiPrec, vali);
                for (int k = idxMin; k <= idxMax; k++) {
                    oldVal[k] = vals[k - idxMin];
                }
            }
            data.setAll(oldVal, layer_.getProfilModel().cmd_);
        }
    }

    @Override
    public String getEnableCondition() {
        return TrResource.getS("S�lectionner au moins un profil") + "<br>" + CtuluLib.getS("ou") + TrResource.getS("s�lectionner au moins deux points d'un profil en long");
    }

    @Override
    public void updateStateBeforeShow() {
        if (layer_.isSelectionEmpty()) {
            super.setEnabled(false);
        } else if (layer_.isAtomicMode()) {
            final EbliListeSelectionMultiInterface multi = layer_.getLayerSelectionMulti();
            final int idx = multi.isSelectionInOneBloc();
            super.setEnabled(idx >= 0 && layer_.getProfilModel().isProfile(idx) && multi.getSelection(idx).getNbSelectedIndex() >= 2);
        } else {
            super.setEnabled(layer_.isOnlyOneObjectSelected());
        }
    }
}
