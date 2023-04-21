package org.jowidgets.common.widgets.controller;

public interface IActionObservable {

    void addActionListener(final IActionListener actionListener);

    void removeActionListener(final IActionListener actionListener);
}
