package org.jfree.report.modules.gui.swing.preview;

import javax.swing.JComponent;
import javax.swing.JMenu;

/**
 * A report controler. This provides some means of configuring the
 * preview components.
 * <p>
 * The controler should use the propertyChange events provided by
 * the PreviewProxyBase and the ReportPane to update its state.
 * <p>
 * To force a new repagination, use the <code>refresh</code> method of
 * the PreviewProxyBase.
 *
 * @author Thomas Morgner
 */
public interface ReportController {

    /**
   * Returns the graphical representation of the controler.
   * This component will be added between the menu bar and
   * the toolbar.
   * <p>
   * Changes to this property are not detected automaticly,
   * you have to call "refreshController" whenever you want to
   * display a completly new control panel.
   *
   * @return the controler component.
   */
    public JComponent getControlPanel();

    /**
   * Returns the menus that should be inserted into the menubar.
   * <p>
   * Changes to this property are not detected automaticly,
   * you have to call "refreshControler" whenever the contents
   * of the menu array changed.
   *
   * @return the menus as array, never null.
   */
    public JMenu[] getMenus();

    /**
   * Defines, whether the controler component is placed between
   * the preview pane and the toolbar.
   *
   * @return true, if this is a inner component.
   */
    public boolean isInnerComponent();

    /**
   * Returns the location for the report controler, one of
   * BorderLayout.NORTH, BorderLayout.SOUTH, BorderLayout.EAST
   * or BorderLayout.WEST.
   *
   * @return the location;
   */
    public String getControllerLocation();

    public void initialize(PreviewPane pane);
}
