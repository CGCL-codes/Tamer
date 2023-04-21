package com.softaspects.jsf.component.gMap;

/**
 * GMap Component
 */
public class GMap extends BaseGMap {

    public String getCenterY() {
        return getCenter() != null ? String.valueOf(getCenter().getY()) : super.getCenterY();
    }

    public String getCenterX() {
        return getCenter() != null ? String.valueOf(getCenter().getX()) : super.getCenterX();
    }

    public void setCenterX(java.lang.String centerX) {
        if (getCenter() == null) {
            setCenter(new GPoint());
        }
        getCenter().setX(Double.parseDouble(centerX));
    }

    public void setCenterY(java.lang.String centerY) {
        if (getCenter() == null) {
            setCenter(new GPoint());
        }
        getCenter().setY(Double.parseDouble(centerY));
    }
}
