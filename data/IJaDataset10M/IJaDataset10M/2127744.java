package net.sourceforge.obschet.model;

import java.util.Map;
import net.sourceforge.obschet.billing.Currency;

/**
 * Account state.
 * @author Aliaksandr Zinevich
 */
public interface AccountState {

    public Map<Currency, Number> getAccountState();

    public void setAccountState(Map<Currency, Number> state);
}
