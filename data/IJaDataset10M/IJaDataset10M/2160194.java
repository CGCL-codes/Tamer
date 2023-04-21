package org.illico.common.widget;

import org.illico.common.component.Component;
import org.illico.common.display.DisplayModifiable;
import org.illico.common.display.MultiDisplayable;

public interface Widget extends Component, MultiDisplayable, DisplayModifiable {

    Widget clone();

    ParentWidget<? extends Widget> getParent();

    void setParent(ParentWidget<? extends Widget> parent);

    int getChildIndex();

    int getDepth();

    boolean isEnabled();

    void setEnabled(boolean enabled);

    boolean isVisible();

    void setVisible(boolean visible);
}
