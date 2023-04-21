package de.psychomatic.mp3db.gui.utils;

import java.awt.Component;
import javax.swing.JTable;
import javax.swing.table.DefaultTableCellRenderer;
import de.psychomatic.mp3db.core.interfaces.AlbumType;

public class AlbumTypeCellRenderer extends DefaultTableCellRenderer {

    @Override
    public Component getTableCellRendererComponent(final JTable arg0, final Object arg1, final boolean arg2, final boolean arg3, final int arg4, final int arg5) {
        super.getTableCellRendererComponent(arg0, arg1, arg2, arg3, arg4, arg5);
        if (arg1 instanceof AlbumType) {
            setText(GuiStrings.getInstance().getString(((AlbumType) arg1).getLangKey()));
        }
        return this;
    }
}
