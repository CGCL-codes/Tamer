package org.itsnat.impl.comp.list;

import org.itsnat.comp.list.ItsNatComboBox;
import java.awt.event.ItemEvent;

/**
 *
 * @author jmarranz
 */
public interface ItsNatComboBoxInternal extends ItsNatListInternal, ItsNatComboBox {

    public ItsNatComboBoxSharedImpl getItsNatComboBoxShared();

    public Object getSelectedItemReminder();

    public void setSelectedItemReminder(Object obj);

    public void fireItemStateChanged(ItemEvent e);

    public boolean hasItemListeners();

    public void setUISelectedIndex(int index);
}
