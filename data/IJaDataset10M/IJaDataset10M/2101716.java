package com.google.gwt.maeglin89273.game.mengine.page;

/**
 * @author Maeglin Liao
 *
 */
public class MaeglinStudiosPage extends FadingPage {

    public MaeglinStudiosPage(Page nextPage, String directoryPath) {
        super(directoryPath + "MStudios_landscape.png", Math.min(getGameWidth(), getGameHeight()), Math.min(getGameWidth(), getGameHeight()), nextPage);
    }
}
