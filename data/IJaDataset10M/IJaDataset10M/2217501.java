package org.jpox.samples.widget;

import java.util.Set;

public interface HasInverseSetField extends HasNormalSetField {

    Set getInverseSet();

    int getNumElementWidgets();
}
