package com.gwtaf.portal.client.view;

public interface IViewNavigator {

    void updateActions(IViewNavigatorItem item);

    void updateTitle(IViewNavigatorItem item);

    void activate(IViewNavigatorItem item);
}
