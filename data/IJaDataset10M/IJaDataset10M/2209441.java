package risk.console;

/**
 * QuitMenuItem-luokka sis�ltyy jokaiseen {@link Menu} -luokkaan. T�m� luokka toteuttaa nk. lopetus-komennon,
 * jossa palataan komentohierarkiassa yksi askel yl�sp�in eli edelliseen valikkoon.
 */
public final class QuitMenuItem extends MenuItem {

    public QuitMenuItem() {
        super(MenuItemType.COMMAND, "Quit");
    }

    public final MenuItemAction call() {
        return MenuItemAction.QUIT;
    }
}
