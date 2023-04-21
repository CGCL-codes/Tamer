package org.jpedal.objects.layers;

/**
 * used by JavaScript for access
 */
public class Layer {

    public static boolean debugLayer = false;

    private PdfLayerList layerList;

    public String name;

    Layer(String name, PdfLayerList layerList) {
        this.name = name;
        this.layerList = layerList;
    }

    public boolean getState() {
        return layerList.isVisible(name);
    }

    public void setState(boolean state) {
        boolean currentValue = layerList.isVisible(name);
        layerList.setVisiblity(name, state);
        if (currentValue != state) {
            if (debugLayer) System.out.println(name + " " + state);
            layerList.setChangesMade(true);
        }
    }
}
