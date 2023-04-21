package calclipse.caldron.gui.locale;

import java.awt.Component;
import javax.swing.Icon;
import javax.swing.JTabbedPane;
import calclipse.caldron.gui.tab.Tab;
import calclipse.caldron.gui.tab.TabManager;
import calclipse.caldron.gui.tab.TabState;

/**
 * This class represents a localized tab in a tabbed pane.
 * Subclasses should override the methods
 * {@link #setDefaultState(String)},
 * {@link #setIcon(Icon)},
 * {@link #setToolTipText(String)},
 * {@link #setTitle(String)} and
 * {@link #setMnemonic(String)},
 * and put
 * {@link calclipse.Resource} tags on them.
 * This class is localized on construction.
 * 
 * @author T. Sommerland
 */
public class LocalizedTab {

    private final JTabbedPane pane;

    private final Component component;

    private TabState defaultState;

    private String title;

    private Icon icon;

    private String toolTipText;

    private int mnemonic;

    private Tab tab;

    protected LocalizedTab(final JTabbedPane pane, final Component component, final TabState defaultState, final String title, final Icon icon, final String toolTipText) {
        this.pane = pane;
        this.component = component;
        this.defaultState = defaultState;
        this.title = title;
        this.icon = icon;
        this.toolTipText = toolTipText;
        Localizer.getInstance().localize(this);
    }

    protected LocalizedTab(final JTabbedPane pane, final Component component, final TabState defaultState, final String title, final Icon icon) {
        this(pane, component, defaultState, title, icon, null);
    }

    protected LocalizedTab(final JTabbedPane pane, final Component component, final TabState defaultState, final String title) {
        this(pane, component, defaultState, title, null, null);
    }

    /**
     * The default implementation of this method returns the class name.
     */
    protected String getKey() {
        return getClass().getName();
    }

    /**
     * Creates the tab.
     */
    public void create() {
        tab = TabManager.createTab(getKey(), pane, component, defaultState, title, icon, toolTipText);
        updateTabMnemonic();
    }

    private void updateTabMnemonic() {
        if (tab != null) {
            tab.setMnemonic(mnemonic);
        }
    }

    /**
     * Destroys the tab.
     * The tab can be recreated afterwards.
     * The tab should have been created before this method is called
     * ({@link #getTab()} should not return null).
     */
    public void destroy(final boolean clearSavedState) {
        if (tab == null) {
            throw new IllegalStateException("Tab not created.");
        }
        TabManager.destroy(tab, clearSavedState);
        tab = null;
    }

    /**
     * The encapsulated tab.
     * @return null if it has not been {@link #create() created},
     * or if it has been {@link #destroy(boolean) destroyed}.
     */
    public Tab getTab() {
        return tab;
    }

    /**
     * "ADDED", "REMOVED" or "SELECTED".
     */
    public void setDefaultState(final String defaultState) {
        final TabState state = Enum.valueOf(TabState.class, defaultState);
        this.defaultState = state;
        if (tab != null) {
            tab.setDefaultState(state);
        }
    }

    public void setIcon(final Icon icon) {
        this.icon = icon;
        if (tab != null) {
            tab.setIcon(icon);
        }
    }

    public void setTitle(final String title) {
        this.title = title;
        if (tab != null) {
            tab.setTitle(title);
        }
    }

    public void setToolTipText(final String toolTipText) {
        this.toolTipText = toolTipText;
        if (tab != null) {
            tab.setToolTipText(toolTipText);
        }
    }

    public void setMnemonic(final String keyStroke) {
        if (keyStroke == null) {
            mnemonic = 0;
        } else {
            mnemonic = Localizer.getKeyStroke(keyStroke).getKeyCode();
        }
        updateTabMnemonic();
    }
}
