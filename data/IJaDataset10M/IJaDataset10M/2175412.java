package com.desktopdeveloper.pendulum.core.controller;

import com.desktopdeveloper.pendulum.components.listeners.FeedbackListener;
import com.desktopdeveloper.pendulum.core.binder.ComponentBinder;

/**
 * Created by IntelliJ IDEA.
 * Open Source 23-Aug-2004 DesktopDeveloper.com
 * Created by: stuart_e
 * Date: 23-Aug-2004
 * Time: 11:13:03
 */
public interface Controller {

    String postData(String actionToExecute, FeedbackListener feedbackListener);

    String postData(String actionToExecute, FeedbackListener feedbackListener, ComponentBinder binder);

    Object findValue(String expression);

    void pushParameter(String actionProperty, Object componentValue);
}
