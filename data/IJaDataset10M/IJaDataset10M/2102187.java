package ca.whu.taxman.control.listener;

import ca.whu.taxman.control.event.AmountChangeEvent;
import java.util.EventListener;

/**
 *
 * @author Peter Wu <peterwu@users.sf.net>
 */
public interface AmountChangeEventListener extends EventListener {

    public void amountChanged(AmountChangeEvent evt);
}
