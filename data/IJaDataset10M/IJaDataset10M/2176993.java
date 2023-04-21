package GUI;

import java.awt.image.BufferedImage;
import java.util.ConcurrentModificationException;
import java.util.Vector;

/**
 *
 * @author Snowangelic
 */
public class Layer {

    Vector<LayerEvent> eventList;

    BufferedImage layerImage;

    BufferedImage emptyImage;

    Layer() {
        eventList = new Vector<LayerEvent>();
        layerImage = new BufferedImage(640, 480, BufferedImage.TYPE_INT_ARGB);
        emptyImage = new BufferedImage(640, 480, BufferedImage.TYPE_INT_ARGB);
    }

    public void hide() {
        eventList.clear();
        layerImage = new BufferedImage(640, 480, BufferedImage.TYPE_INT_ARGB);
    }

    public void startNewLayer() {
        updateImage(emptyImage, 0, 0);
    }

    void addLayerEvent(LayerEvent e) {
        eventList.add(e);
    }

    BufferedImage getImage() {
        return layerImage;
    }

    void updateLayer() {
        try {
            for (LayerEvent event : eventList) {
                updateImage(event.getImage(), event.getX(), event.getY());
            }
            eventList.clear();
        } catch (ConcurrentModificationException e) {
        }
    }

    protected void updateImage(BufferedImage newElement, int xPosition, int yPosition) {
        for (int i = 0; i < newElement.getWidth(); i++) for (int j = 0; j < newElement.getHeight(); j++) layerImage.setRGB(xPosition + i, yPosition + j, newElement.getRGB(i, j));
    }
}
