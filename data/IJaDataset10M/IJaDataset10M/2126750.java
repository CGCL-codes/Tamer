package net.community.chest.ui.components.table.file;

import java.awt.Component;
import java.io.File;
import java.text.DateFormat;
import java.util.Date;
import javax.swing.JTable;
import net.community.chest.ui.components.table.renderer.DateTimeCellRenderer;

/**
 * <P>Copyright 2009 as per GPLv2</P>
 *
 * @author Lyor G.
 * @since Aug 4, 2009 4:34:12 PM
 */
public class FileDateTimeCellRenderer extends DateTimeCellRenderer {

    /**
	 * 
	 */
    private static final long serialVersionUID = -3597079502869619989L;

    public FileDateTimeCellRenderer(DateFormat f) {
        super(f);
    }

    public FileDateTimeCellRenderer() {
        this(DateFormat.getDateTimeInstance());
    }

    @Override
    public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
        if (value instanceof File) return super.getTableCellRendererComponent(table, new Date(((File) value).lastModified()), isSelected, hasFocus, row, column); else if (value instanceof String) return super.getTableCellRendererComponent(table, new Date(new File(value.toString()).lastModified()), isSelected, hasFocus, row, column); else return super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
    }
}
