package org.fudaa.ctulu.gis.shapefile;

import com.vividsolutions.jts.geom.Envelope;

/**
 * Bean of idinfo element of shp.xml.
 * @source $URL: http://svn.geotools.org/geotools/tags/2.2-RC3/plugin/shapefile/src/org/geotools/data/shapefile/shp/xml/IdInfo.java $
 */
public class IdInfo {

    /** spdom/bounding represents. */
    Envelope bounding_;

    /** spdom/lbounding represents. */
    Envelope lbounding_;

    /**
     * @return Returns the bounding.
     */
    public Envelope getBounding() {
        return bounding_;
    }

    /**
     * @param _bounding The bounding to set.
     */
    public void setBounding(final Envelope _bounding) {
        this.bounding_ = _bounding;
    }

    /**
     * @return Returns the lbounding.
     */
    public Envelope getLbounding() {
        return lbounding_;
    }

    /**
     * @param _lbounding The lbounding to set.
     */
    public void setLbounding(final Envelope _lbounding) {
        this.lbounding_ = _lbounding;
    }
}
