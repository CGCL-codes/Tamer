package com.hifi.plugin.library.api.ui;

import javax.swing.JComponent;
import javax.swing.JFrame;
import com.hifi.core.api.ui.modules.IModulePlaf;

public interface ILibraryUIModule {

    public LibraryUIEnum getId();

    public void init(JFrame frame, IModulePlaf plaf) throws Exception;

    public JComponent getComponent();

    public void terminate();
}
