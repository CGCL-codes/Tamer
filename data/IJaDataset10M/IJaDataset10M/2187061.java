package org.graphics;

import java.awt.Component;
import java.util.Collection;
import org.game.thyvin.graphics.DChanger;
import org.gui.GeneralInputListener;

public interface DrawerBase {

    public Component getComponent();

    public boolean isDone();

    public void render();

    public void addInputListenerThorough(GeneralInputListener inputListener);

    public void setAnimationSpeed(double animationSpeed);

    public void addDElement(DElement dElement);

    public void removeDElement(DElement dElement);

    public void addDChanger(DChanger positionChanger);

    public void resetStartTime();

    public <E extends DElement> void removeAllDElement(Collection<E> nodDEles);
}
