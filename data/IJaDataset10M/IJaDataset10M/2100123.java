package uk.ac.kingston.aqurate.util;

public class HotspotMarker {

    private String markerLabel;

    private String myID;

    private int x_coord, y_coord, real_x_coord, real_y_coord;

    public HotspotMarker(int x, int y, String label, double scalingFactor) {
        x_coord = x;
        y_coord = y;
        real_x_coord = (int) (x / scalingFactor);
        real_y_coord = (int) (y / scalingFactor);
        markerLabel = label;
    }

    public String getID() {
        return myID;
    }

    public String getLabel() {
        return markerLabel;
    }

    public int getX() {
        return x_coord;
    }

    public int getY() {
        return y_coord;
    }

    public int getRealX() {
        return real_x_coord;
    }

    public int getRealY() {
        return real_y_coord;
    }

    public void setLabel(String newLabel) {
        markerLabel = newLabel;
    }
}
