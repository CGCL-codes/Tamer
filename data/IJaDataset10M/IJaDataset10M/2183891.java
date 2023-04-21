package consciouscode.bonsai.components;

import consciouscode.bonsai.channels.Channel;
import javax.swing.JComboBox;
import javax.swing.event.ListDataEvent;

/**
    A combo box that uses channels bound to its selected item and list.
*/
public class BComboBox extends JComboBox {

    public BComboBox(Channel selectionChannel, Channel optionsChannel) {
        super(new BComboBoxModel(selectionChannel, optionsChannel));
    }

    /**
       Handles a <code>ListDataEvent</code> indicating that this component's
       list data has changed.
       <p>
       This is a WORKAROUND For Java 1.3.1.
       Sun clearly documents that this method is not to be overridden.  However,
       it is necessary to keep the <code>JComboBox</code> from resetting the
       selected item when the new selection is not in the list.
    */
    @Override
    public void contentsChanged(ListDataEvent e) {
        boolean wasEditable = isEditable;
        isEditable = true;
        super.contentsChanged(e);
        isEditable = wasEditable;
    }
}
