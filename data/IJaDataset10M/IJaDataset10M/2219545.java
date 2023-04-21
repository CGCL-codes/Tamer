package com.phloc.commons.format.impl;

import javax.annotation.Nonnegative;
import javax.annotation.Nullable;
import com.phloc.commons.format.IFormatter;
import com.phloc.commons.hash.HashCodeGenerator;
import com.phloc.commons.string.StringHelper;
import com.phloc.commons.string.ToStringGenerator;

/**
 * A string formatter that ensures that a string has a minimum length by filling
 * the remaining chars with a custom character at front (leading).
 * 
 * @author philip
 */
public final class MinLengthAddLeadingFormatter extends AbstractStringFormatter {

    private final int m_nMinLength;

    private final char m_cFill;

    public MinLengthAddLeadingFormatter(@Nonnegative final int nMinLength, final char cFill) {
        this(nMinLength, cFill, null);
    }

    public MinLengthAddLeadingFormatter(@Nonnegative final int nMinLength, final char cFill, @Nullable final IFormatter aNestedFormatter) {
        super(aNestedFormatter);
        if (nMinLength < 1) throw new IllegalArgumentException("Passed min length is too small: " + nMinLength);
        m_nMinLength = nMinLength;
        m_cFill = cFill;
    }

    @Override
    protected String getFormattedValueAsString(@Nullable final Object aValue) {
        final String s = getValueAsString(aValue);
        if (s.length() >= m_nMinLength) return s;
        return StringHelper.getRepeated(m_cFill, m_nMinLength - s.length()) + s;
    }

    @Override
    public boolean equals(final Object o) {
        if (o == this) return true;
        if (!(o instanceof MinLengthAddLeadingFormatter)) return false;
        final MinLengthAddLeadingFormatter rhs = (MinLengthAddLeadingFormatter) o;
        return m_nMinLength == rhs.m_nMinLength && m_cFill == rhs.m_cFill;
    }

    @Override
    public int hashCode() {
        return new HashCodeGenerator(this).append(m_nMinLength).append(m_cFill).getHashCode();
    }

    @Override
    public String toString() {
        return ToStringGenerator.getDerived(super.toString()).append("minLength", m_nMinLength).append("fill", m_cFill).toString();
    }
}
