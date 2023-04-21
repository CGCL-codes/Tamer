package joodin.impl.widgets.internal;

import joodin.impl.widgets.VaadinMenu;
import org.jowidgets.spi.widgets.IMainMenuSpi;
import com.vaadin.ui.MenuBar.MenuItem;

public class MainMenuImpl extends VaadinMenu implements IMainMenuSpi {

    private final MenuItemImpl menuItemDelegate;

    public MainMenuImpl(final MenuItem menu) {
        super(menu);
        this.menuItemDelegate = new MenuItemImpl(menu);
    }

    @Override
    public MenuItem getUiReference() {
        return super.getUiReference();
    }

    @Override
    public void setText(final String text) {
        menuItemDelegate.setText(text);
    }

    @Override
    public void setMnemonic(final char mnemonic) {
        menuItemDelegate.setMnemonic(mnemonic);
    }
}
