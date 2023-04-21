package net.sourceforge.xhsi.panel;

import java.awt.Graphics2D;
import java.awt.geom.AffineTransform;
import java.awt.geom.Arc2D;
import net.sourceforge.xhsi.model.Avionics;
import net.sourceforge.xhsi.model.ModelFactory;
import net.sourceforge.xhsi.util.RunningAverager;

public class PositionTrendVector extends NDSubcomponent {

    private static final long serialVersionUID = 1L;

    RunningAverager turn_speed_averager = new RunningAverager(20);

    public PositionTrendVector(ModelFactory model_factory, NDGraphicsConfig hsi_gc) {
        super(model_factory, hsi_gc);
    }

    public void paint(Graphics2D g2) {
        float turn_speed = turn_speed_averager.running_average(aircraft.turn_speed());
        if (!(nd_gc.mode_plan || nd_gc.mode_classic_hsi)) {
            if (Math.abs(aircraft.roll_angle()) >= 0.0f) {
                AffineTransform original_at = g2.getTransform();
                if (!nd_gc.mode_map) {
                    AffineTransform rotate = AffineTransform.getRotateInstance(Math.toRadians(-this.aircraft.drift()), nd_gc.map_center_x, nd_gc.map_center_y);
                    g2.transform(rotate);
                }
                long pixels_per_nm = (long) ((float) nd_gc.rose_radius / nd_gc.max_range);
                float ground_speed = aircraft.ground_speed();
                float turn_radius = turn_radius(turn_speed, ground_speed);
                int map_range = nd_gc.map_range;
                if (map_range > 20) {
                    draw_position_trend_vector_segment(g2, turn_radius, turn_speed, pixels_per_nm, 7.5f, 30.0f);
                    draw_position_trend_vector_segment(g2, turn_radius, turn_speed, pixels_per_nm, 37.5f, 60.0f);
                    draw_position_trend_vector_segment(g2, turn_radius, turn_speed, pixels_per_nm, 67.5f, 90.0f);
                } else if (map_range == 20) {
                    draw_position_trend_vector_segment(g2, turn_radius, turn_speed, pixels_per_nm, 5.0f, 30.0f);
                    draw_position_trend_vector_segment(g2, turn_radius, turn_speed, pixels_per_nm, 35.0f, 60.0f);
                } else {
                    draw_position_trend_vector_segment(g2, turn_radius, turn_speed, pixels_per_nm, 2.5f, 30.0f);
                }
                g2.setTransform(original_at);
            }
        }
    }

    public void draw_position_trend_vector_segment(Graphics2D g2, float turn_radius, float turn_speed, long pixels_per_nm, float vector_start, float vector_end) {
        g2.setColor(nd_gc.aircraft_color);
        float turn_radius_pixels = turn_radius * pixels_per_nm;
        if (turn_speed >= 0) {
            g2.draw(new Arc2D.Float((float) nd_gc.map_center_x, (float) (nd_gc.map_center_y - turn_radius_pixels), turn_radius_pixels * 2.0f, turn_radius_pixels * 2.0f, 180.0f - (vector_end * turn_speed), (vector_end - vector_start) * turn_speed, Arc2D.OPEN));
        } else {
            g2.draw(new Arc2D.Float((float) ((nd_gc.map_center_x) - (turn_radius_pixels * 2.0f)), (float) (nd_gc.map_center_y - turn_radius_pixels), turn_radius_pixels * 2.0f, turn_radius_pixels * 2.0f, vector_start * Math.abs(turn_speed), (vector_end - vector_start) * Math.abs(turn_speed), Arc2D.OPEN));
        }
    }

    public float turn_radius(float turn_speed, float speed) {
        return Math.abs(speed / (turn_speed * 20.0f * (float) Math.PI));
    }
}
