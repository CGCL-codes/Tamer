package client.game.hud;

import java.awt.event.ActionListener;
import client.game.hud.components.AHudObject;
import com.jme.util.Timer;

public abstract class AHudManager {

    public AHudManager() {
        super();
    }

    public abstract void removeComponentFromState(HudStateType hudType, String componentName);

    public abstract void popUpMessageRelease(String popup);

    public abstract void popUpMessageAcquire(String popup);

    public abstract void removeComponentFromState(HudStateType hudType, AHudObject desktopElement);

    public abstract boolean addComponentToState(HudStateType hudType, AHudObject desktopElement);

    public abstract void displayConditionedMessage(String messageBoxTitle, String messageText, ActionListener leftAction);

    public abstract void displayMessage(String messageBoxTitle, String messageText, String buttonText);

    public abstract void initialize(Timer timer);

    public abstract void changeToState(HudStateType nextHudState);

    public abstract boolean isPopUpActive();
}
