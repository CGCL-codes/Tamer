package com.quikj.ace.web.client.view;

import com.google.gwt.user.client.ui.Widget;
import com.quikj.ace.web.client.presenter.UserPanelPresenter;

/**
 * @author beckie
 * 
 */
public interface UserPanel {

    public void setPresenter(UserPanelPresenter presenter);

    public void showTab(int index);

    public void attach(Widget w, String label);

    public void replaceWidget(int index, String tabText, Widget w);

    public void detachWidget(Widget w);

    public void highlight(int index, boolean highlight, String image);

    public int getSelectedTab();

    public int getWidgetCount();

    public Widget getWidget(int index);

    public void insertWidget(int index, String text, Widget w);
}
