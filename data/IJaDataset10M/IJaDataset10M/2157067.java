package org.xtophe.jam.swing;

import org.xtophe.jam.controller.JamController;

public interface JamView<T> {

    public JamController<T> getController();

    public void setTitle(String title);

    public String getTitle();
}
