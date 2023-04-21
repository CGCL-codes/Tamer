package org.fudaa.ebli.controle;

import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import javax.swing.JComponent;
import javax.swing.JList;
import javax.swing.JTable;
import javax.swing.ListCellRenderer;
import javax.swing.UIManager;
import javax.swing.table.TableCellRenderer;
import com.memoire.bu.BuLabel;
import org.fudaa.ctulu.CtuluLibString;
import org.fudaa.ctulu.gui.CtuluCellRenderer;
import org.fudaa.ebli.commun.EbliLib;
import org.fudaa.ebli.trace.TraceLigne;
import org.fudaa.ebli.trace.TraceLigneModel;

/**
 * Un renderer pour les trait (TraceLigne).
 * @author Fred Deniger
 * @version $Id: BTraitRenderer.java,v 1.12 2006-09-19 14:55:52 deniger Exp $
 */
public final class BTraitRenderer extends JComponent implements TableCellRenderer, ListCellRenderer {

    private final TraceLigne l_;

    BuLabel lbNo_;

    /**
   * Initialisation variables internes.
   */
    public BTraitRenderer() {
        l_ = new TraceLigne();
    }

    protected void paintComponent(final Graphics _g) {
        if (l_.getEpaisseur() <= 0) {
            return;
        }
        final Color old = _g.getColor();
        _g.setColor(getBackground());
        ((Graphics2D) _g).fill(_g.getClip());
        final Dimension d = getSize();
        l_.dessineTrait((Graphics2D) _g, 0, d.height / 2, d.width, d.height / 2);
        _g.setColor(old);
    }

    private void buildLb() {
        if (lbNo_ == null) {
            lbNo_ = new BuLabel();
            lbNo_.setOpaque(true);
        }
    }

    public Component getListCellRendererComponent(final JList _list, final Object _value, final int _index, final boolean _isSelected, final boolean _cellHasFocus) {
        JComponent r = this;
        if (_value == null) {
            buildLb();
            lbNo_.setText(CtuluLibString.ESPACE);
            r = lbNo_;
        } else {
            l_.setModel((TraceLigneModel) _value);
            if (l_.getEpaisseur() == 0 || l_.getTypeTrait() == TraceLigne.INVISIBLE) {
                buildLb();
                lbNo_.setText(EbliLib.getS("Aucun"));
                r = lbNo_;
            }
        }
        if (_isSelected) {
            r.setForeground(_list.getSelectionForeground());
            r.setBackground(_list.getSelectionBackground());
        } else {
            r.setBackground(_list.getBackground());
            r.setForeground(_list.getForeground());
        }
        r.setBorder((_cellHasFocus) ? UIManager.getBorder("List.focusCellHighlightBorder") : CtuluCellRenderer.BORDER_NO_FOCUS);
        return r;
    }

    public Dimension getPreferredSize() {
        final Dimension d = super.getPreferredSize();
        final int s = (int) l_.getEpaisseur() + 11;
        d.width = 15;
        if (d.height < s) {
            d.height = s;
        }
        return d;
    }

    public Component getTableCellRendererComponent(final JTable _table, final Object _value, final boolean _isSelected, final boolean _hasFocus, final int _row, final int _column) {
        JComponent r = this;
        if (_value == null) {
            if (lbNo_ == null) {
                lbNo_ = new BuLabel(EbliLib.getS("Aucun"));
                lbNo_.setOpaque(true);
            }
            r = lbNo_;
        }
        if (_isSelected) {
            r.setForeground(_table.getSelectionForeground());
            r.setBackground(_table.getSelectionBackground());
        }
        if (_hasFocus) {
            r.setBorder(UIManager.getBorder("Table.focusCellHighlightBorder"));
            r.setForeground(UIManager.getColor("Table.focusCellForeground"));
            r.setBackground(UIManager.getColor("Table.focusCellBackground"));
        } else {
            r.setBorder(CtuluCellRenderer.BORDER_NO_FOCUS);
            r.setForeground(_table.getForeground());
            r.setBackground(_table.getBackground());
        }
        if (_value != null) {
            l_.setModel((TraceLigneModel) _value);
        }
        return r;
    }
}
