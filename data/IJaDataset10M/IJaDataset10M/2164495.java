package org.proteinshader.gui.components.menubar;

import org.proteinshader.gui.*;
import javax.swing.*;
import javax.swing.event.*;
import java.awt.*;
import java.awt.event.*;

/*******************************************************************************
Provides the menu bar to place at the top of the ProteinShader
programs's main GUI window.  The menus are named "File", "Style",
"Visibility", "Orientation", and "Tools".
*******************************************************************************/
public class MainMenuBar extends JMenuBar {

    private VisibilityMenu m_visibilityMenu;

    private ToolsMenu m_toolsMenu;

    private StyleMenu m_styleMenu;

    private HelpMenu m_helpMenu;

    /***************************************************************************
    Constructs a ProteinShaderMenuBar.

    @param mediator  the centralized Mediator that most listeners need
    to call on to accomplish their task.
    ***************************************************************************/
    public MainMenuBar(Mediator mediator) {
        add(new FileMenu(mediator));
        add((m_styleMenu = new StyleMenu(mediator)));
        add((m_visibilityMenu = new VisibilityMenu(mediator)));
        add(new OrientationMenu(mediator));
        add(new BackgroundMenu(mediator, null));
        add((m_toolsMenu = new ToolsMenu(mediator)));
        add((m_helpMenu = new HelpMenu(mediator, mediator.getFrame())));
    }

    /***************************************************************************
    Returns the HelpMenu.

    @return The 'Help' menu.
    ***************************************************************************/
    public HelpMenu getHelpMenu() {
        return m_helpMenu;
    }

    /***************************************************************************
    Returns the ToolsMenu, which has methods for detecting and
    changing the currently selected Tools menu item ("Selector"
    or "Controls").

    @return ToolsMenu  the 'Tools' menu.
    ***************************************************************************/
    public ToolsMenu getToolsMenu() {
        return m_toolsMenu;
    }

    /***************************************************************************
    Returns the VisibilityMenu, which has methods for enabling/disabling
    or selecting/deselecting a menu item ('Amino Acids Allowed', 
    'Heterogens Allowed', or 'Water Allowed').

    @return VisibilityMenu  the 'Visibility' menu.
    ***************************************************************************/
    public VisibilityMenu getVisibilityMenu() {
        return m_visibilityMenu;
    }

    /***************************************************************************
    Returns the StyleMenu, which has a method for setting the style back to 
    its default.

    @return StyleMenu  the 'Style' menu.
    ***************************************************************************/
    public StyleMenu getStyleMenu() {
        return m_styleMenu;
    }
}
