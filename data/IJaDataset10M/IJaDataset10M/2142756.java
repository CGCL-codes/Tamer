package com.volantis.mcs.themes;

import com.volantis.mcs.model.validation.ValidationContext;
import com.volantis.mcs.model.validation.SourceLocation;
import java.util.List;

/**
 * A SelectorSequence contains a list of selectors and will either be used as
 * the subject or context of a {@link CombinedSelector}. Selector sequences can
 * contain element, attribute, class, id, pseudo class and pseudo element
 * selectors. However, it is invalid for a selector sequence that is the context
 * of a combined selector to contain either pseudo element or stateful pseudo
 * class selectors.
 *
 * @mock.generate base="Selector"
 */
public interface SelectorSequence extends Selector, Subject {

    /**
     * Get the location of the definition of this in the source.
     *
     * @return The source location.
     */
    SourceLocation getSourceLocation();

    /**
     * Return the selectors that make up this selector sequence. May be null
     * if no selectors have been added.
     *
     * @return List of selectors that make up this selector sequence.
     */
    List getSelectors();

    /**
     * Set the list of selectors that make up this selector sequence.
     *
     * <p>The list is copied so changes to the list after invoking this method
     * will not affect this object.</p>
     *
     * @param selectors List of selectors that will make up this selector
     * sequence
     */
    void setSelectors(List selectors);

    /**
     * Add a selector to the list of selectors that make up this selector
     * sequence.
     *
     * @param selector to be added to the list of selectors that make up this
     * selector sequence
     */
    void addSelector(Selector selector);

    /**
     * Checks whether there are any selectors associated with this sequence
     *
     * @return True if this sequence has selectors, false otherwise
     */
    boolean hasSelector();

    /**
     * Visit all the children selectors.
     *
     * @param visitor The visitor that will be invoked for each child selector.
     */
    void visitChildren(SelectorVisitor visitor);

    /**
     * Perform validation of SelectorSequence, varying what is acceptable
     * depending on whether the sequence is the context or subject of a
     * combined selector.
     *
     * @param context       ValidationContext against which to report errors
     * @param isContext     true if this selector sequence is the context of a
     *                      combined selector and false if it is a subject
     *                      selector.
     */
    void validate(ValidationContext context, boolean isContext);
}
