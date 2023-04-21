package interfaces.menu;

import org.fenggui.util.Dimension;
import org.fenggui.util.Point;
import gameStates.absGamesStates.gui.AbsMenuState;
import interfaces.AbsInterfaceElement;

public interface AbsMenu extends AbsInterfaceElement {

    public boolean gameInitialized();

    public AbsMenuState getMenuState();

    public void enableContinueButton(boolean b);

    public void setGameStarted(boolean gameStarted);

    public Dimension getMenuContentSize();

    public Point getMenuContentPosition();
}
