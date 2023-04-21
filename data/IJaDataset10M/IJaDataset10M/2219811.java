package com.bluebrim.paint.impl.client;

import java.awt.*;
import javax.swing.*;
import com.bluebrim.gui.client.*;
import com.bluebrim.paint.shared.*;

/**
 * Ritar en f�rgad ruta + namnet f�r CoColors i popuper
 */
public class CoColorListCellRenderer extends CoListCellRenderer implements ListCellRenderer {

    private CoColorSampleIcon m_icon = new CoColorSampleIcon(null);

    /**
 * PresentableListCellRenderer constructor comment.
 */
    protected CoColorListCellRenderer() {
        super();
    }

    /**
 * Som default visas objekt som implementerar CoCatalogElementIF
 * upp med sin ikon och sin identitet.
 */
    public Component getListCellRendererComponent(JList list, Object value, int index, boolean isSelected, boolean cellHasFocus) {
        super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);
        if ((value != null) && (value instanceof CoColorIF)) {
            CoColorIF c = (CoColorIF) value;
            setText(c.getName());
            if (!isMutable(c, index)) {
                if ((m_font == null) || !list.getFont().equals(m_font)) {
                    m_font = list.getFont();
                    m_immutableFont = m_font.deriveFont(Font.ITALIC);
                }
                setFont(m_immutableFont);
            }
            m_icon.setColor(c);
            setIcon(m_icon);
        }
        return this;
    }

    private Font m_font;

    private Font m_immutableFont;

    protected boolean isMutable(CoColorIF color, int index) {
        return color.canBeEdited() && color.canBeDeleted();
    }
}
