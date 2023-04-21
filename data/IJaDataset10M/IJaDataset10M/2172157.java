package org.custommonkey.xmlunit.examples;

import org.custommonkey.xmlunit.Difference;
import org.custommonkey.xmlunit.DifferenceConstants;
import org.custommonkey.xmlunit.DifferenceListener;
import org.w3c.dom.Node;

/**
 * Base class that delegates all differences to another DifferenceListener.
 *
 * <p>Subclasses get a chance to hook into special methods that will
 * be invoked for differences in textual values of attributes, CDATA
 * sections, Text or comment nodes.</p>
 */
public abstract class TextDifferenceListenerBase implements DifferenceListener {

    private final DifferenceListener delegateTo;

    protected TextDifferenceListenerBase(DifferenceListener delegateTo) {
        this.delegateTo = delegateTo;
    }

    /**
     * Delegates to the nested DifferenceListener unless the
     * Difference is of type {@link DifferenceConstants#ATTR_VALUE_ID
     * ATTR_VALUE_ID}, {@link DifferenceConstants#CDATA_VALUE_ID
     * CDATA_VALUE_ID}, {@link DifferenceConstants#COMMENT_VALUE_ID
     * COMMENT_VALUE_ID} or {@link DifferenceConstants#TEXT_VALUE_ID
     * TEXT_VALUE_ID} - for those special differences {@link
     * #attributeDifference attributeDifference}, {@link
     * #cdataDifference cdataDifference}, {@link #commentDifference
     * commentDifference} or {@link #textDifference textDifference}
     * are invoked respectively.
     */
    public int differenceFound(Difference difference) {
        switch(difference.getId()) {
            case DifferenceConstants.ATTR_VALUE_ID:
                return attributeDifference(difference);
            case DifferenceConstants.CDATA_VALUE_ID:
                return cdataDifference(difference);
            case DifferenceConstants.COMMENT_VALUE_ID:
                return commentDifference(difference);
            case DifferenceConstants.TEXT_VALUE_ID:
                return textDifference(difference);
        }
        return delegateTo.differenceFound(difference);
    }

    /**
     * Delegates to {@link #textualDifference textualDifference}.
     */
    protected int attributeDifference(Difference d) {
        return textualDifference(d);
    }

    /**
     * Delegates to {@link #textualDifference textualDifference}.
     */
    protected int cdataDifference(Difference d) {
        return textualDifference(d);
    }

    /**
     * Delegates to {@link #textualDifference textualDifference}.
     */
    protected int commentDifference(Difference d) {
        return textualDifference(d);
    }

    /**
     * Delegates to {@link #textualDifference textualDifference}.
     */
    protected int textDifference(Difference d) {
        return textualDifference(d);
    }

    /**
     * Delegates to the nested DifferenceListener.
     */
    protected int textualDifference(Difference d) {
        return delegateTo.differenceFound(d);
    }

    public void skippedComparison(Node control, Node test) {
        delegateTo.skippedComparison(control, test);
    }
}
