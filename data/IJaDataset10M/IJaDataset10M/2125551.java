package org.jowidgets.common.widgets.controller;

public interface IComponentObservable {

    void addComponentListener(IComponentListener componentListener);

    void removeComponentListener(IComponentListener componentListener);
}
