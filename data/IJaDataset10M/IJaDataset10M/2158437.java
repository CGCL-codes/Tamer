package be.lassi.ui.group;

import java.util.List;
import javax.swing.table.AbstractTableModel;
import be.lassi.domain.Channel;
import be.lassi.util.NLS;

/**
 * Instances of this class can be interrogated by JTable to find the
 * information needed to display channels in tabular format.
 *
 */
public class ChannelsTableModel extends AbstractTableModel {

    private final List<Channel> channels;

    public ChannelsTableModel(final List<Channel> channels) {
        this.channels = channels;
    }

    /**
     * {@inheritDoc}
     */
    public int getColumnCount() {
        return 2;
    }

    /**
     * {@inheritDoc}
     */
    public String getColumnName(final int col) {
        String name;
        switch(col) {
            case 0:
                name = NLS.get("groups.channels.table.number");
                break;
            case 1:
                name = NLS.get("groups.channels.table.channel");
                break;
            default:
                name = "?";
        }
        return name;
    }

    /**
     * {@inheritDoc}
     */
    public int getRowCount() {
        return channels.size();
    }

    /**
     * {@inheritDoc}
     */
    public Object getValueAt(final int row, final int col) {
        Channel channel = channels.get(row);
        Object value;
        switch(col) {
            case 0:
                value = row + 1;
                break;
            case 1:
                value = channel.getName();
                break;
            default:
                value = "?";
        }
        return value;
    }

    /**
     * {@inheritDoc}
     */
    public boolean isCellEditable(final int row, final int col) {
        return col == 1;
    }

    /**
     * {@inheritDoc}
     */
    public void setValueAt(final Object value, final int row, final int col) {
        if (col == 1) {
            Channel channel = channels.get(row);
            channel.setName((String) value);
        }
    }
}
