package org.xblackcat.rojac.service.options;

import org.xblackcat.rojac.i18n.IDescribable;

import javax.swing.*;
import java.util.ArrayList;
import java.util.EnumSet;
import java.util.List;
import java.util.Set;

/**
 * @author xBlackCat
 */
class GeneralEnumChecker<T extends Enum<T>> implements IValueChecker<T> {
    private final Set<T> allowedValues;

    @SafeVarargs
    static <E extends Enum<E>> GeneralEnumChecker<E> only(E value, E... rest) {
        return new GeneralEnumChecker<>(EnumSet.of(value, rest));
    }

    @SafeVarargs
    static <E extends Enum<E>> GeneralEnumChecker<E> except(E value, E... rest) {
        return new GeneralEnumChecker<>(EnumSet.complementOf(EnumSet.of(value, rest)));
    }

    GeneralEnumChecker(Class<T> enumClass) {
        this(EnumSet.allOf(enumClass));
    }

    private GeneralEnumChecker(Set<T> allowedValues) {
        this.allowedValues = allowedValues;
    }

    @Override
    public List<T> getPossibleValues() {
        return new ArrayList<>(allowedValues);
    }

    @Override
    public String getValueDescription(T v) throws IllegalArgumentException {
        if (v == null) {
            return "(null)";
        } else if (v instanceof IDescribable) {
            return ((IDescribable) v).getLabel().get();
        }

        return v.name();
    }

    @Override
    public boolean isValueCorrect(T v) {
        return true;
    }

    @Override
    public Icon getValueIcon(T v) throws IllegalArgumentException {
        return null;
    }
}
