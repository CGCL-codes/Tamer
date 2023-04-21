package org.fudaa.fudaa.albe;

import java.awt.BorderLayout;
import java.awt.Component;
import javax.swing.JTable;
import javax.swing.SwingConstants;
import javax.swing.UIManager;
import com.memoire.bu.BuLabel;
import com.memoire.bu.BuPanel;
import com.memoire.bu.BuTableCellRenderer;

/**
 * Gestionnaire de rendu de cellule pour les en-t�tes des tableaux situ�s dans
 * les onglets 'Param�tres de sol' et 'Coefficients partiels'.
 * 
 * @author Sabrina Delattre
 */
public class AlbeTableCellRenderer extends BuTableCellRenderer {

    public AlbeTableCellRenderer() {
        super();
    }

    public Component getTableCellRendererComponent(final JTable _table, final Object _value, final boolean _isSelected, final boolean _hasFocus, final int _row, final int _column) {
        Component r = null;
        BuLabel lb = null;
        if (_row < 0) {
            lb = new BuLabel();
            lb.setIcon(AlbeResource.ALBE.getIcon((String) _value + ".gif"));
            lb.setHorizontalAlignment(SwingConstants.CENTER);
            lb.setOpaque(false);
            final BuPanel _p = new BuPanel();
            _p.setLayout(new BorderLayout());
            _p.setBorder(UIManager.getBorder("TableHeader.cellBorder"));
            _p.setBackground(UIManager.getColor("TableHeader.background"));
            _p.setOpaque(true);
            _p.add(lb, BorderLayout.CENTER);
            r = _p;
        } else {
            lb = (BuLabel) super.getTableCellRendererComponent(_table, _value, _isSelected, _hasFocus, _row, _column);
            if (_value instanceof Double) {
            } else if (_value instanceof Integer) {
                lb.setText(((Integer) _value).toString());
            } else if (_value instanceof String) {
                lb.setText((String) _value);
            }
            r = lb;
        }
        return r;
    }
}
