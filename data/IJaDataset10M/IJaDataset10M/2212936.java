package org.pescuma.jfg;

import java.util.Collection;
import java.util.Comparator;
import org.pescuma.jfg.gui.WidgetValidator;

public interface AttributeValueRange {

    Comparator<?> getComparator();

    Object getMax();

    Object getMin();

    Collection<?> getPossibleValues();

    boolean canBeNull();

    WidgetValidator[] getValidators();
}
