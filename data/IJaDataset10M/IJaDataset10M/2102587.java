package de.syfy.project.engine.hud;

import de.syfy.project.engine.hud.interfaces.UI_Event;
import de.syfy.project.engine.hud.interfaces.UI_Item;
import java.awt.geom.Point2D;

/**
 *
 * @author Timo
 */
public class Event implements UI_Event {

    private String name;

    private UI_Item sender;

    private Point2D.Float p;

    public Event(String name, Point2D.Float p, UI_Item sender) {
        this.name = name;
        this.p = p;
        this.sender = sender;
    }

    @Override
    public String getEventName() {
        return name;
    }

    @Override
    public UI_Item getFiringItem() {
        return sender;
    }

    public Point2D.Float getEventPosition() {
        return p;
    }

    @Override
    public int getPriority() {
        return -1;
    }
}
