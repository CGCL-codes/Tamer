package consciouscode.bonsai.components;

import consciouscode.bonsai.channels.Channel;
import consciouscode.bonsai.channels.ChannelEvent;
import consciouscode.bonsai.channels.ChannelListener;
import java.util.List;
import javax.swing.ComboBoxModel;
import javax.swing.DefaultListModel;

/**
   A {@link ComboBoxModel} that uses channels bound to its selected item and
   list.

   The list channel may contain either {@link List} or <code>Object[]</code>
   values; any other type will result in an empty drop-down.

   @see BComboBox
*/
public class BComboBoxModel extends DefaultListModel implements ChannelListener, ComboBoxModel {

    public BComboBoxModel(Channel selectedItemChannel, Channel listChannel) {
        myListChannel = listChannel;
        mySelectedItemChannel = selectedItemChannel;
        listChanged();
        selectedItemChannel.addChannelListener(this);
        listChannel.addChannelListener(this);
    }

    public Object getSelectedItem() {
        return mySelectedItemChannel.getValue();
    }

    public void setSelectedItem(Object item) {
        mySelectedItemChannel.setValue(item);
    }

    public void channelUpdate(ChannelEvent event) {
        Object source = event.getSource();
        if (source == mySelectedItemChannel) {
            selectedItemChanged();
        } else if (source == myListChannel) {
            listChanged();
        }
    }

    private void selectedItemChanged() {
        fireContentsChanged(this, -1, -1);
    }

    private void listChanged() {
        Object listObject = myListChannel.getValue();
        removeAllElements();
        if (listObject instanceof List<?>) {
            List<?> list = (List<?>) listObject;
            ensureCapacity(list.size());
            for (int i = 0, count = list.size(); i < count; i++) {
                addElement(list.get(i));
            }
        } else if (listObject instanceof Object[]) {
            Object[] array = (Object[]) listObject;
            ensureCapacity(array.length);
            for (int i = 0, count = array.length; i < count; i++) {
                addElement(array[i]);
            }
        }
    }

    private Channel mySelectedItemChannel;

    private Channel myListChannel;
}
