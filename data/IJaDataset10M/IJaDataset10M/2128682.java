package org.fudaa.ebli.component;

import java.awt.Color;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import javax.swing.table.AbstractTableModel;
import org.fudaa.ebli.palette.BPalettePlageAbstract;

/**
 * @author Fred Deniger
 * @version $Id: PaletteManagerTableModel.java,v 1.6 2006-09-19 14:55:52 deniger Exp $
 */
public class PaletteManagerTableModel extends AbstractTableModel {

    ArrayList color_;

    /**
   * @param _color
   */
    public PaletteManagerTableModel(final Color[] _color) {
        super();
        if (_color == null) {
            color_ = new ArrayList();
        } else {
            color_ = new ArrayList(Arrays.asList(_color));
        }
    }

    public int getColumnCount() {
        return 10;
    }

    public int getRowCount() {
        return (int) Math.ceil(color_.size() / 10d) + 3;
    }

    public Object getValueAt(final int _rowIndex, final int _columnIndex) {
        final int r = _rowIndex * 10 + _columnIndex;
        if (r >= color_.size()) {
            return null;
        }
        return color_.get(r);
    }

    public boolean isCellEditable(final int _rowIndex, final int _columnIndex) {
        return true;
    }

    public void removeValue(final int _rowIndex, final int _columnIndex) {
        final int r = getListIdx(_rowIndex, _columnIndex);
        if (r < color_.size()) {
            color_.remove(r);
            fireTableDataChanged();
        }
    }

    public Color getColor(final int _rowIndex, final int _columnIndex) {
        return getColor(getListIdx(_rowIndex, _columnIndex));
    }

    public Color getColor(final int _genIdx) {
        if (_genIdx < 0 || _genIdx >= color_.size()) {
            return null;
        }
        return (Color) color_.get(_genIdx);
    }

    public void insertColor(final int _rowIndex, final int _columnIndex) {
        final int r = getListIdx(_rowIndex, _columnIndex);
        if (color_.size() == 0) {
            return;
        }
        if (r < color_.size()) {
            Color cToAdd = Color.BLUE;
            if (r == 0) {
                cToAdd = getColor(0).brighter();
            }
            if (r == color_.size() - 1) {
                cToAdd = getColor(color_.size() - 1).darker();
            } else if (color_.size() > 2) {
                cToAdd = BPalettePlageAbstract.getCouleur(getColor(r - 1), getColor(r), 0.5);
            }
            color_.add(r, cToAdd);
            fireTableDataChanged();
        }
    }

    public int getColorNb() {
        return color_.size();
    }

    public int getListIdx(final int _rowIndex, final int _columnIndex) {
        return _rowIndex * 10 + _columnIndex;
    }

    public void remove(final Collection _idx) {
        color_.removeAll(_idx);
        fireTableDataChanged();
    }

    public void setValueAt(final Object _value, final int _rowIndex, final int _columnIndex) {
        if (!(_value instanceof Color)) {
            return;
        }
        final int r = getListIdx(_rowIndex, _columnIndex);
        if (r >= color_.size()) {
            Color first = Color.BLUE;
            final Color max = (Color) _value;
            if (color_.size() > 0) {
                first = (Color) color_.get(color_.size() - 1);
            }
            final int nbToAdd = r - color_.size();
            for (int i = 0; i < nbToAdd; i++) {
                color_.add(BPalettePlageAbstract.getCouleur(first, max, ((double) (i + 1)) / ((double) (nbToAdd + 2))));
            }
            color_.add(_value);
            fireTableDataChanged();
        }
        color_.set(r, _value);
        fireTableCellUpdated(_rowIndex, _columnIndex);
    }
}
