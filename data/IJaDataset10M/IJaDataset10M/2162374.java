package net.sourceforge.xhsi.panel;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;
import java.awt.Component;
import net.sourceforge.xhsi.XHSIPreferences;
import net.sourceforge.xhsi.model.Avionics;
import net.sourceforge.xhsi.model.ModelFactory;

public class HeadingLabel extends NDSubcomponent {

    private static final long serialVersionUID = 1L;

    AffineTransform original_at = null;

    int old_hdg_text_length = 0;

    float old_hdg_text_width = 0.0f;

    BufferedImage hdg_label_decoration_buf_img;

    public HeadingLabel(ModelFactory model_factory, NDGraphicsConfig hsi_gc, Component parent_component) {
        super(model_factory, hsi_gc, parent_component);
        this.hdg_label_decoration_buf_img = null;
    }

    public void paint(Graphics2D g2) {
        if (!nd_gc.mode_plan) {
            int heading_box_bottom_y = nd_gc.border_top + nd_gc.line_height_large;
            int rose_top_y = this.nd_gc.map_center_y - nd_gc.rose_radius;
            int center_x = this.nd_gc.map_center_x;
            int center_y = this.nd_gc.map_center_y;
            int plane_width = (int) (30 * nd_gc.shrink_scaling_factor);
            int plane_height = (int) (plane_width * 1.5);
            String up_label;
            int mag_value;
            float map_up;
            float hdg_pointer;
            float trk_line;
            if (nd_gc.hdg_up) {
                mag_value = Math.round(this.aircraft.heading());
                map_up = this.aircraft.heading() - this.aircraft.magnetic_variation();
                up_label = "HDG";
                hdg_pointer = 0.0f;
                trk_line = this.aircraft.track() - this.aircraft.heading();
            } else if (nd_gc.trk_up) {
                mag_value = Math.round(this.aircraft.track());
                map_up = this.aircraft.track() - this.aircraft.magnetic_variation();
                up_label = "TRK";
                hdg_pointer = this.aircraft.heading() - this.aircraft.track();
                trk_line = 0.0f;
            } else {
                mag_value = 999;
                map_up = 0.0f;
                up_label = " N ";
                hdg_pointer = this.aircraft.heading() - this.aircraft.magnetic_variation();
                trk_line = this.aircraft.track() - this.aircraft.magnetic_variation();
            }
            int x_points_heading_box[] = { nd_gc.map_center_x - 36, nd_gc.map_center_x - 36, nd_gc.map_center_x + 36, nd_gc.map_center_x + 36 };
            int y_points_heading_box[] = { nd_gc.border_top + 0, nd_gc.border_top + 30, nd_gc.border_top + 30, nd_gc.border_top + 0 };
            g2.setColor(nd_gc.heading_labels_color);
            g2.setFont(nd_gc.font_medium);
            g2.drawString(up_label, nd_gc.map_center_x - 43 - nd_gc.get_text_width(g2, nd_gc.font_medium, up_label), nd_gc.border_top + nd_gc.line_height_medium);
            g2.drawString("MAG", nd_gc.map_center_x + 43, nd_gc.border_top + nd_gc.line_height_medium);
            g2.setColor(nd_gc.top_text_color);
            g2.drawPolyline(x_points_heading_box, y_points_heading_box, 4);
            g2.clearRect(center_x - 34, nd_gc.border_top, 68, heading_box_bottom_y - nd_gc.border_top);
            g2.setFont(nd_gc.font_large);
            String text = "" + mag_value;
            if (text.length() != old_hdg_text_length) {
                old_hdg_text_width = nd_gc.get_text_width(g2, nd_gc.font_large, text) / 2;
                old_hdg_text_length = text.length();
            }
            g2.drawString(text, center_x - old_hdg_text_width, heading_box_bottom_y - 3);
            if (!nd_gc.mode_classic_hsi) {
                int hdg_pointer_height = (int) Math.min(16, 18 * nd_gc.shrink_scaling_factor);
                int hdg_pointer_width = (int) (10.0f * nd_gc.shrink_scaling_factor);
                int x_points_hdg_pointer[] = { center_x, center_x - hdg_pointer_width, center_x + hdg_pointer_width };
                int y_points_hdg_pointer[] = { nd_gc.rose_y_offset - 1, nd_gc.rose_y_offset - hdg_pointer_height, nd_gc.rose_y_offset - hdg_pointer_height };
                rotate(g2, hdg_pointer);
                g2.setColor(nd_gc.aircraft_color);
                g2.drawPolygon(x_points_hdg_pointer, y_points_hdg_pointer, 3);
                unrotate(g2);
            }
            g2.setColor(nd_gc.aircraft_color);
            if (nd_gc.mode_classic_hsi) {
                g2.drawLine(center_x - plane_width / 4, center_y - plane_height, center_x - plane_width / 4, center_y + plane_height);
                g2.drawLine(center_x + plane_width / 4, center_y - plane_height, center_x + plane_width / 4, center_y + plane_height);
                g2.drawLine(center_x - plane_height, center_y, center_x - plane_width / 4, center_y);
                g2.drawLine(center_x + plane_width / 4, center_y, center_x + plane_height, center_y);
                g2.drawLine(center_x - plane_width / 2 - plane_width / 4, center_y + plane_height, center_x - plane_width / 4, center_y + plane_height);
                g2.drawLine(center_x + plane_width / 4, center_y + plane_height, center_x + plane_width / 2 + plane_width / 4, center_y + plane_height);
            } else {
                int x_points_airplane_symbol[] = { center_x, center_x - (plane_width / 2), center_x + (plane_width / 2) };
                int y_points_airplane_symbol[] = { center_y, center_y + plane_height, center_y + plane_height };
                g2.drawPolygon(x_points_airplane_symbol, y_points_airplane_symbol, 3);
            }
            rotate(g2, trk_line);
            if (nd_gc.mode_classic_hsi) {
                int pointer_height = (int) (nd_gc.big_tick_length / 2);
                int pointer_width = (int) (nd_gc.big_tick_length / 3);
                g2.setColor(nd_gc.aircraft_color);
                g2.drawLine(nd_gc.map_center_x, nd_gc.map_center_y - nd_gc.rose_radius, nd_gc.map_center_x + pointer_width, nd_gc.map_center_y - nd_gc.rose_radius + pointer_height);
                g2.drawLine(nd_gc.map_center_x + pointer_width, nd_gc.map_center_y - nd_gc.rose_radius + pointer_height, nd_gc.map_center_x, nd_gc.map_center_y - nd_gc.rose_radius + 2 * pointer_height);
                g2.drawLine(nd_gc.map_center_x, nd_gc.map_center_y - nd_gc.rose_radius + 2 * pointer_height, nd_gc.map_center_x - pointer_width, nd_gc.map_center_y - nd_gc.rose_radius + pointer_height);
                g2.drawLine(nd_gc.map_center_x - pointer_width, nd_gc.map_center_y - nd_gc.rose_radius + pointer_height, nd_gc.map_center_x, nd_gc.map_center_y - nd_gc.rose_radius);
            } else {
                g2.setColor(nd_gc.rose_color);
                int tick_halfwidth = (int) (5 * nd_gc.scaling_factor);
                g2.drawLine(nd_gc.map_center_x, nd_gc.map_center_y - (nd_gc.rose_radius * 3 / 16), nd_gc.map_center_x, rose_top_y + 2);
                if (!XHSIPreferences.get_instance().get_draw_range_arcs()) {
                    g2.drawLine(nd_gc.map_center_x - tick_halfwidth, nd_gc.map_center_y - (nd_gc.rose_radius * 3 / 4), nd_gc.map_center_x + tick_halfwidth, nd_gc.map_center_y - (nd_gc.rose_radius * 3 / 4));
                    g2.drawLine(nd_gc.map_center_x - tick_halfwidth, nd_gc.map_center_y - (nd_gc.rose_radius / 2), nd_gc.map_center_x + tick_halfwidth, nd_gc.map_center_y - (nd_gc.rose_radius / 2));
                    g2.drawLine(nd_gc.map_center_x - tick_halfwidth, nd_gc.map_center_y - (nd_gc.rose_radius / 4), nd_gc.map_center_x + tick_halfwidth, nd_gc.map_center_y - (nd_gc.rose_radius / 4));
                }
                g2.setFont(nd_gc.font_medium);
                int range = nd_gc.map_range;
                if (nd_gc.mode_centered) {
                    range /= 2;
                }
                String range_text = "";
                if (range == 2) range_text += "1.25"; else if (range == 5) range_text += "2.5"; else range_text += range / 2;
                g2.drawString(range_text, nd_gc.map_center_x - nd_gc.get_text_width(g2, nd_gc.font_medium, range_text) - 4, nd_gc.map_center_y - (nd_gc.rose_radius / 2) - (nd_gc.get_text_height(g2, g2.getFont()) / 2) + 5);
            }
            unrotate(g2);
        }
    }

    private void rotate(Graphics2D g2, double angle) {
        this.original_at = g2.getTransform();
        AffineTransform rotate = AffineTransform.getRotateInstance(Math.toRadians(angle), nd_gc.map_center_x, nd_gc.map_center_y);
        g2.transform(rotate);
    }

    private void unrotate(Graphics2D g2) {
        g2.setTransform(original_at);
    }
}
