package be.lassi.ui.group;

import javax.swing.table.AbstractTableModel;
import be.lassi.domain.Channel;

/**
 * Instances of this class can be interrogated by JTable to find the
 * information needed to display variables in tabular format.
 *
 */
public class NotInGroupTableModel extends AbstractTableModel {

    private GroupDefinitionFrame frame;

    public NotInGroupTableModel(final GroupDefinitionFrame frame) {
        this.frame = frame;
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
                name = "Nr";
                break;
            case 1:
                name = "Channel";
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
        return frame.getChannelsNotInGroupCount();
    }

    /**
     * {@inheritDoc}
     */
    public Object getValueAt(final int row, final int col) {
        Channel channel = getChannel(row);
        Object value;
        switch(col) {
            case 0:
                value = channel.getId() + 1;
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
            Channel channel = getChannel(row);
            channel.setName((String) value);
        }
    }

    private Channel getChannel(final int row) {
        int[] channelIndexes = frame.getChannelsNotInGroup();
        int channelIndex = channelIndexes[row];
        Channel channel = frame.getContext().getShow().getChannels().get(channelIndex);
        return channel;
    }
}
