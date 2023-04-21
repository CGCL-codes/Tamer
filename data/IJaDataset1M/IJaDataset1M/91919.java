package org.jactr.core.slot;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public interface ISlotOwner {

    public void valueChanged(ISlot slot, Object oldValue, Object newValue);
}
