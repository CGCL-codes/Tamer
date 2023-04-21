package org.w3c.css.sac;

/**
 * @version $Revision: 1.1.1.1 $
 * @author  Philippe Le Hegaret
 * @see Selector#SAC_NEGATIVE_SELECTOR
 */
public interface NegativeSelector extends SimpleSelector {

    /**
     * Returns the simple selector.
     */
    public SimpleSelector getSimpleSelector();
}
