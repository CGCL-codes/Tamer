package com.expertria.glex.view.layout;

import com.google.gwt.user.client.ui.ComplexPanel;
import com.google.gwt.user.client.ui.Widget;

public interface ILayout {

    ComplexPanel getLayoutBase();

    void onAddChild(Widget widget);

    void updateDisplayList();

    void setGap(int pixel);
}
