package net.tourbook.srtm;

public class ElevationLayer {

    private static ElevationBase[] fLayer = new ElevationBase[4];

    private static ElevationEtopo fEtopo = new ElevationEtopo();

    private static ElevationGlobe fGlobe = new ElevationGlobe();

    private static ElevationSRTM3 fSrtm3 = new ElevationSRTM3();

    private static ElevationSRTM1 fSrtm1 = new ElevationSRTM1();

    private static int zoom;

    private static int fileTypIndexStart;

    public ElevationLayer() {
        fLayer[0] = fEtopo;
        fLayer[1] = fGlobe;
        fLayer[2] = fSrtm3;
        fLayer[3] = fSrtm1;
        zoom = 0;
    }

    public float getElevation(final GeoLat lat, final GeoLon lon) {
        int layerIndex = fileTypIndexStart;
        while (layerIndex >= 0) {
            try {
                final float hoehe = fLayer[layerIndex].getElevation(lat, lon);
                if (fLayer[layerIndex].isValid(hoehe)) {
                    return hoehe;
                } else {
                    layerIndex--;
                }
            } catch (final Exception e) {
                layerIndex--;
            }
        }
        layerIndex = 0;
        return -500;
    }

    private int getElevationType() {
        if (zoom <= 4) {
            return Constants.ELEVATION_TYPE_ETOPO;
        }
        if (zoom <= 8) {
            return Constants.ELEVATION_TYPE_GLOBE;
        }
        return Constants.ELEVATION_TYPE_SRTM3;
    }

    public String getName() {
        return fLayer[getElevationType()].getName();
    }

    public short getSekDiff() {
        return fLayer[getElevationType()].getSecDiff();
    }

    private void setFileTypIndexStart() {
        fileTypIndexStart = getElevationType();
    }

    public void setZoom(final int z) {
        zoom = z;
        setFileTypIndexStart();
    }
}
