package de.annee.mosaix.model;

import java.util.List;
import java.util.Vector;
import de.annee.mosaix.util.Util;

public class DestImage extends ModelBase {

    public static final String PROPERTY_WIDTH_CM = "widthCm";

    public static final String PROPERTY_HEIGHT_CM = "heightCm";

    public static final String PROPERTY_ASPECTRATIO = "aspectRatio";

    public static final String PROPERTY_RESOLUTION = "resolution";

    public static final String PROPERTY_WIDTH = "width";

    public static final String PROPERTY_HEIGHT = "height";

    public static final String PROPERTY_NUM_TILES_X = "numTilesX";

    public static final String PROPERTY_NUM_TILES_Y = "numTilesY";

    private Integer widthCm;

    private Integer heightCm;

    private Integer width;

    private Integer height;

    private String aspectRatio;

    private Integer resolution;

    private Integer numTilesX;

    private Integer numTilesY;

    private List[][] bestImages;

    public List getBestImages(int x, int y) {
        List result;
        result = bestImages[x][y];
        if (result == null) {
            result = new Vector();
            bestImages[x][y] = result;
        }
        return result;
    }

    public void setBestImages(List[][] bestImages) {
        this.bestImages = bestImages;
    }

    public DestImage() {
        super();
        setWidthCm(new Integer(80));
        setHeightCm(new Integer(60));
        setResolution(new Integer(300));
        setNumTilesX(new Integer(80));
        setNumTilesY(new Integer(60));
    }

    public Integer getHeightCm() {
        return heightCm;
    }

    public Integer getHeight() {
        return height;
    }

    public void setHeightCm(Integer height) {
        Integer oldValue = this.heightCm;
        this.heightCm = height;
        firePropertyChange(PROPERTY_HEIGHT_CM, oldValue, height);
        updateAspectRatio();
        updateDimension();
    }

    public void setHeight(Integer height) {
        Integer oldValue = this.height;
        this.height = height;
        firePropertyChange(PROPERTY_HEIGHT, oldValue, height);
    }

    public Integer getWidthCm() {
        return widthCm;
    }

    public Integer getWidth() {
        return width;
    }

    public void setWidthCm(Integer width) {
        Integer oldValue = this.widthCm;
        this.widthCm = width;
        firePropertyChange(PROPERTY_WIDTH_CM, oldValue, width);
        updateAspectRatio();
        updateDimension();
    }

    public void setWidth(Integer width) {
        Integer oldValue = this.width;
        this.width = width;
        firePropertyChange(PROPERTY_WIDTH, oldValue, width);
    }

    public String getAspectRatio() {
        return this.aspectRatio;
    }

    protected void updateAspectRatio() {
        String oldValue = this.aspectRatio;
        String newValue = Util.INFO_INITIAL;
        if ((widthCm != null) && (heightCm != null)) {
            int x = widthCm.intValue();
            int y = heightCm.intValue();
            int ggt = Util.ggt(x, y);
            while (ggt > 1) {
                x /= ggt;
                y /= ggt;
                ggt = Util.ggt(x, y);
            }
            newValue = x + ":" + y;
        }
        this.aspectRatio = newValue;
        firePropertyChange(PROPERTY_ASPECTRATIO, oldValue, newValue);
    }

    public Integer getResolution() {
        return resolution;
    }

    public void setResolution(Integer resolution) {
        Integer oldValue = this.resolution;
        this.resolution = resolution;
        firePropertyChange(PROPERTY_RESOLUTION, oldValue, resolution);
        updateDimension();
    }

    protected void updateDimension() {
        if (resolution != null) {
            if (widthCm != null) {
                int x = (int) Math.round((double) widthCm.intValue() / 2.54 * resolution.intValue());
                setWidth(new Integer(x));
            } else setWidth(null);
            if (heightCm != null) {
                int y = (int) Math.round((double) heightCm.intValue() / 2.54 * resolution.intValue());
                setHeight(new Integer(y));
            } else setHeight(null);
        } else {
            setWidth(null);
            setHeight(null);
        }
    }

    public Integer getNumTilesX() {
        return numTilesX;
    }

    public void setNumTilesX(Integer numTilesX) {
        Integer oldValue = this.numTilesX;
        this.numTilesX = numTilesX;
        firePropertyChange(PROPERTY_NUM_TILES_X, oldValue, numTilesX);
    }

    public Integer getNumTilesY() {
        return numTilesY;
    }

    public void setNumTilesY(Integer numTilesY) {
        Integer oldValue = this.numTilesY;
        this.numTilesY = numTilesY;
        firePropertyChange(PROPERTY_NUM_TILES_Y, oldValue, numTilesY);
    }
}
