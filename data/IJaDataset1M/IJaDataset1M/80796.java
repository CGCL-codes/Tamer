package org.formaria.swing.docking;

/**
 * A listener for docking panel state changes
 */
public interface DockingPanelListener {

    /** 
   * The panel has been closed
   */
    public void panelClosed();

    /** 
   * The panel has been maximized or zoomed
   */
    public void panelMaximized();

    /** 
   * The panel has been minimized or docked to the sidebar
   */
    public void panelMimimized();

    /**
   * The panel has been restored to its normal state either from the sidebar or
   * from being zoomed
   */
    public void panelRestored();

    /**
   * The panel preview has been opened
   */
    public void panelPreviewOpened();

    /**
   * The panel preview has been closed
   */
    public void panelPreviewClosed();
}
