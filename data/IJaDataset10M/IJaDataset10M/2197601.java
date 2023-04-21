package net.sf.rottz.tv.client.rottzclient.screens;

import java.awt.Point;
import java.awt.event.KeyEvent;
import net.sf.rottz.tv.client.rottzclient.GameScreenTypes;
import net.sf.rottz.tv.client.rottzclient.ImageType;
import net.sf.rottz.tv.client.rottzclient.MouseButton;
import net.sf.rottz.tv.client.rottzclient.RottzClientMain;

public class GameScreenMainMenu extends GameScreenMenu {

    public GameScreenMainMenu(RottzClientMain rottzClient) {
        super(rottzClient, rottzClient.getImagePool().getImage(ImageType.MAIN_MENU));
    }

    @Override
    public GameScreenTypes getScreenType() {
        return GameScreenTypes.MAIN_MENU;
    }

    @Override
    public void keyPress(int keyCode) {
        RottzClientMain client = getRottzClient();
        switch(keyCode) {
            case KeyEvent.VK_1:
                client.changeMenuScreen(GameScreenTypes.MENU_SINGLE_OR_MULTI);
                break;
            case KeyEvent.VK_ESCAPE:
                client.changeMenuScreen(GameScreenTypes.MAIN_MENU);
                break;
        }
    }

    @Override
    public void mouseReleaseBase(Point p, MouseButton button) {
        getRottzClient().changeMenuScreen(GameScreenTypes.MENU_SINGLE_OR_MULTI);
    }

    @Override
    public void initialize() {
    }
}
