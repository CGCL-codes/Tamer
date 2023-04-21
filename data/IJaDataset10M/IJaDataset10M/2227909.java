package de.paragon.explorer;

import java.awt.event.MouseEvent;

public interface Tool {

    public abstract void mouseClicked(MouseEvent event);

    public abstract void mouseEntered(MouseEvent event);

    public abstract void mouseExited(MouseEvent event);

    public abstract void mousePressed(MouseEvent event);

    public abstract void mouseReleased(MouseEvent event);
}
