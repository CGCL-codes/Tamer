package it.enricod.jcontextfree.gui;

import it.enricod.jcontextfree.gui.impl.MainFrame;
import com.google.inject.ImplementedBy;
import it.enricod.jcontextfree.engine.samples.IProgramsCatalog;

@ImplementedBy(MainFrame.class)
public interface IMainFrame {

    public void setVisible(boolean flag);

    public IProgramsTabbedPane getProgramsTabbedPane();

    public IProgramCodePanel getProgramPanel();

    public IProgramsCatalog getProgramsCatalog();
}
