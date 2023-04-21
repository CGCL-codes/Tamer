package swarm.gui;

import swarm.Selector;
import swarm.objectbase.Droppable;

public interface ZoomRaster extends Droppable {

    void setColormap(Colormap cm);

    void enableDestroyNotification$notificationMethod(Object o, Selector sel);

    void setZoomFactor(int zoomFactor);

    void setWidth$Height(int tamanhoX, int tamanhoY);

    void setWindowTitle(String string);

    void pack();

    void setButton$Client$Message(int i, Object o, Selector sel);

    void drawSelf();
}
