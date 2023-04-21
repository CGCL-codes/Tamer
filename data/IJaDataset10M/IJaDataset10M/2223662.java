package org.gaugebook.gauges;

import java.awt.Graphics2D;
import java.awt.geom.Point2D;
import org.gaugebook.AbstractGauge;
import org.gaugebook.FSXVariable;
import org.gaugebook.GaugeBook;

/**
 * Radar Altimeter Gauge
 * 
 * 
 * @author Michael Nagler
 */
public class RadarAltimeter extends AbstractGauge {

    private FSXVariable radarAltitude;

    private double ialt;

    private double cialt;

    public void init(GaugeBook owner, String title) {
        super.init(owner, title);
        this.setMinValue(0);
        this.setMaxValue(50);
        this.setText("RADAR ALT");
        this.setDecPlaceCount(0);
        this.setMajorTickStep(10);
        this.setMinorTickStep(1);
        this.setAngleExtend(330);
        this.setAngleStart(120);
        this.setValue(1021);
        super.setTextPosition(0, -.25);
        radarAltitude = owner.registerFSXVariable(new FSXVariable("RADIO HEIGHT"));
    }

    public void update() {
        if (radarAltitude == null) return;
        ialt = (Double) radarAltitude.getValue();
        super.setValue(cialt = ialt);
        super.update();
    }

    @Override
    public void drawValue(final double r, final double needleR, final Point2D cp, Graphics2D g2) {
    }
}
