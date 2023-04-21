package com.volantis.mcs.themes.impl;

import com.volantis.synergetics.ObjectHelper;
import com.volantis.mcs.model.validation.SourceLocation;
import com.volantis.mcs.themes.StyleValue;
import com.volantis.mcs.themes.StylePair;
import com.volantis.mcs.themes.StyleValueType;
import com.volantis.mcs.themes.StyleValueVisitor;

/**
 * This class represents a pair of values of a style property.
 */
public final class StylePairImpl extends StyleValueImpl implements StylePair {

    /**
     * The first value.
     */
    private StyleValue first;

    /**
     * The second value.
     */
    private StyleValue second;

    /**
     * Package private constructor for use by JiBX.
     */
    StylePairImpl() {
    }

    /**
     * Initialise.
     *
     * @param first  The first value.
     * @param second The second value.
     */
    public StylePairImpl(StyleValue first, StyleValue second) {
        this(first, first, second);
    }

    /**
     * Initialise.
     *
     * @param first  The first value.
     * @param second The second value.
     */
    public StylePairImpl(SourceLocation location, StyleValue first, StyleValue second) {
        super(location);
        this.first = first;
        this.second = second;
    }

    public StyleValueType getStyleValueType() {
        return StyleValueType.PAIR;
    }

    /**
     * Override this method to call the correct method in the visitor.
     */
    public void visit(StyleValueVisitor visitor, Object object) {
        visitor.visit(this, object);
    }

    public StyleValue getFirst() {
        return first;
    }

    public StyleValue getSecond() {
        return second;
    }

    /**
     * This method is called by JiBX to figure out if the second part of the
     * pair is present. This is required to allow the marshalling to figure out
     * that it should not write an empty <second></second> if the second part of
     * the pair is not present.
     * <p>
     * ********* DO NOT REMOVE THIS METHOD - IT IS USED BY JIBX *************
     *
     * @return true if the pair contains the optional second part.
     */
    boolean jibxHasSecond() {
        return second != null;
    }

    protected boolean equalsImpl(Object o) {
        if (!(o instanceof StylePairImpl)) {
            return false;
        }
        StylePairImpl other = (StylePairImpl) o;
        return ObjectHelper.equals(first, other.first) && ObjectHelper.equals(second, other.second);
    }

    protected int hashCodeImpl() {
        int result = 0;
        result = 37 * result + first.hashCode();
        if (second != null) {
            result = 37 * result + second.hashCode();
        }
        return result;
    }

    public String getStandardCSS() {
        StringBuffer buffer = new StringBuffer();
        buffer.append(first);
        if (second != null) {
            buffer.append(" ");
            buffer.append(second);
        }
        return buffer.toString();
    }

    public int getStandardCost() {
        int cost = first.getStandardCost();
        if (second != null) {
            cost += second.getStandardCost();
        }
        return cost;
    }
}
