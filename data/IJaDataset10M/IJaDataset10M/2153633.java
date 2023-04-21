package jmax.ui;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import jmax.registry.*;

/**
 * The interface to access the to UI management.
 * There is only one instance of a UIStyleManager for each running jMax.
 * This objects now about the UI structure and style and knox 
 * Details later.
 */
public interface UIStyleManager {

    public void startUI(RegistryElement layoutElement) throws UIException;

    public void addMainWindowListener(WindowListener listener);

    public UIContext getDefaultUIContext();

    public JFrame getMainFrame();

    public UIContext getUIContext(UIContext uiContext, String where);
}
