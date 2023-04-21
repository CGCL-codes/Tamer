package com.desktopdeveloper.pendulum.core.binder;

import com.desktopdeveloper.pendulum.core.controller.Controller;

/**
 * Created by IntelliJ IDEA.
 * Open Source 27-Aug-2004 DesktopDeveloper.com
 * Created by: stuart_e
 * Date: 27-Aug-2004
 * Time: 11:17:33
 */
public interface Binding {

    String getComponentProperty();

    String getActionProperty();

    void refresh(Controller controller);

    void setComponent(Object component);

    Object getComponentValue();

    Object getComponent();

    Object load(Controller controller);
}
