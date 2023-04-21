package pcgen.gui2.filter2;

import java.awt.Component;

/**
 *
 * @author Connor Petty <cpmeister@users.sourceforge.net>
 */
public interface DisplayableFilter<C, E> extends Filter<C, E> {

    /**
	 * returns a Component that will be used to display
	 * this filter
	 * @return a swing Component
	 */
    public Component getFilterComponent();

    /**
	 * sets the handler that will be used to toggle refiltering of a list
	 * when a change occurs to this filter
	 * @param handler
	 */
    public void setFilterHandler(FilterHandler handler);
}
