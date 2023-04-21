package com.mobileares.midp.widgets.client.utils;

import com.google.gwt.user.client.ui.Widget;

/**
 * Created by IntelliJ IDEA.
 * User: Tom
 * Date: 2011-4-8
 * Time: 13:58:25
 * To change this template use File | Settings | File Templates.
 */
public interface IWidgetProvider<T> {

    Widget getWidget(T ob);

    Object getUserObject(T ob);
}
