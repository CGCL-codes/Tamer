package net.sourceforge.xhsi.panel;

import java.awt.Component;
import java.awt.Graphics2D;
import java.awt.geom.Arc2D;
import java.util.logging.Logger;
import net.sourceforge.xhsi.model.ModelFactory;

public class AltitudeRangeArc extends NDSubcomponent {

    private static Logger logger = Logger.getLogger("net.sourceforge.xhsi");

    private static final long serialVersionUID = 1L;

    private static final float ARC = 5.0f;

    public AltitudeRangeArc(ModelFactory model_factory, NDGraphicsConfig hsi_gc, Component parent_component) {
        super(model_factory, hsi_gc, parent_component);
    }

    public void paint(Graphics2D g2) {
        if (nd_gc.mode_map) {
            float current_altitude = this.aircraft.indicated_altitude();
            float target_altitude = this.avionics.autopilot_altitude();
            float vertical_velocity = this.aircraft.indicated_vv();
            float groundspeed = this.aircraft.ground_speed();
            if ((Math.abs(target_altitude - current_altitude) > 100.0f) && (Math.abs(vertical_velocity) > 100.0f)) {
                float distance = (target_altitude - current_altitude) / vertical_velocity / 60.0f * groundspeed;
                if ((distance > 0.0f) && (distance < nd_gc.max_range)) {
                    float arc_radius = distance * nd_gc.pixels_per_nm;
                    float arc = ARC * nd_gc.max_range / distance;
                    if (arc > 45.0f) arc = 45.0f;
                    g2.setColor(nd_gc.altitude_arc_color);
                    g2.draw(new Arc2D.Float((float) nd_gc.map_center_x - arc_radius, (float) nd_gc.map_center_y - arc_radius, arc_radius * 2.0f, arc_radius * 2.0f, 90.0f - arc, 2.0f * arc, Arc2D.OPEN));
                }
            }
        }
    }
}
