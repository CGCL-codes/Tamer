package com.volantis.mcs.protocols.widgets.attributes;

import com.volantis.mcs.protocols.ImageAttributes;
import java.util.ArrayList;
import java.util.List;

/**
 * Holds properties specific to MapElement.
 */
public final class MapAttributes extends WidgetAttributes {

    private double latitude = Double.NaN;

    private double longitude = Double.NaN;

    private int zoom = -1;

    private String query;

    /**
     * List of attibutes of input elements  
     */
    private final ArrayList inputs = new ArrayList();

    private ImageAttributes imageAttributes = null;

    public void addInput(InputAttributes inputAttributes) {
        inputs.add(inputAttributes);
    }

    public List getInputs() {
        return inputs;
    }

    public ImageAttributes getImageAttributes() {
        return imageAttributes;
    }

    public void setImageAttributes(ImageAttributes imageAttributes) {
        this.imageAttributes = imageAttributes;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public double getLatitude() {
        return latitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public void setZoom(int zoom) {
        this.zoom = zoom;
    }

    public int getZoom() {
        return this.zoom;
    }

    public void setQuery(String query) {
        this.query = query;
    }

    public String getQuery() {
        return query;
    }
}
