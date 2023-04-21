package jm2pc.client.devices.out;

public class Zoom {

    public static final int ZOOM_MAX = 1;

    public static final int ZOOM_MIDLE = 2;

    public static final int ZOOM_LOW = 3;

    public static final int ZOOM_LOWEST = 4;

    private int zoomImg = ZOOM_MIDLE;

    public int getZoomImg() {
        return zoomImg;
    }

    public void setZoomImg(int zoomImg) {
        if (zoomImg > ZOOM_LOWEST) {
            this.zoomImg = ZOOM_MAX;
        } else if (zoomImg < ZOOM_MAX) {
            this.zoomImg = ZOOM_MAX;
        } else {
            this.zoomImg = zoomImg;
        }
    }
}
