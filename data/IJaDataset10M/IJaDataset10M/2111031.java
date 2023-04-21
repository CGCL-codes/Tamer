package pl.rzarajczyk.breaktime.menuitems;

import java.awt.Point;
import java.awt.event.MouseEvent;
import org.springframework.beans.factory.annotation.Autowired;
import pl.rzarajczyk.breaktime.ScreenCorner;
import pl.rzarajczyk.breaktime.gui.TimerWindow;
import pl.rzarajczyk.breaktime.utils.Utils;

/**
 *
 * @author Rafal
 */
public class ShowTimerMenuItem extends SimpleMenuItem {

    @Autowired
    private TimerWindow timerWindow;

    public ShowTimerMenuItem() {
        super("Show timer");
    }

    @Override
    public void execute(MouseEvent e) {
        Point point = e.getLocationOnScreen();
        ScreenCorner corner = Utils.getScreenCorner(point);
        timerWindow.setScreenCorner(corner);
        timerWindow.setVisible(!timerWindow.isVisible());
    }

    @Override
    public String getIcon() {
        return "/icons/Show timer.png";
    }
}
