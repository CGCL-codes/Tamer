package net.sourceforge.xhsi.panel;

import java.awt.Color;
import java.awt.Component;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.text.DecimalFormat;
import net.sourceforge.xhsi.XHSIStatus;
import net.sourceforge.xhsi.model.ModelFactory;
import net.sourceforge.xhsi.model.xplane.XPlaneSimDataRepository;
import net.sourceforge.xhsi.util.RunningAverager;

public class StatusBar extends XHSISubcomponent {

    private static final long serialVersionUID = 1L;

    long time_of_last_call;

    float[] last_frame_rates = new float[10];

    RunningAverager averager = new RunningAverager(30);

    BufferedImage buf_img;

    int frame_rate = 0;

    int fps_x;

    int fps_y;

    int fps_pixels_per_frame;

    int src_x;

    int src_y;

    int nav_db_status_x;

    int nav_db_status_y;

    int nav_db_text_width = 0;

    int utc_x;

    int utc_y;

    int utc_text_width = 0;

    public StatusBar(ModelFactory model_factory, XHSIGraphicsConfig hsi_gc, Component parent_component) {
        super(model_factory, hsi_gc, parent_component);
        this.time_of_last_call = System.currentTimeMillis();
    }

    public void paint(Graphics2D g2) {
        calculate_frame_rate();
        render_status_bar(g2);
    }

    private void calculate_frame_rate() {
        long current_time = System.currentTimeMillis();
        this.frame_rate = (int) this.averager.running_average(1000.0f / (current_time - this.time_of_last_call));
        this.time_of_last_call = current_time;
    }

    private void render_status_bar(Graphics2D g2) {
        g2.setColor(xhsi_gc.color_windowgray);
        g2.fillRect(0, 0, xhsi_gc.panel_size.width, XHSIGraphicsConfig.STATUS_BAR_HEIGHT);
        g2.setBackground(Color.BLACK);
        if (nav_db_text_width == 0) {
            nav_db_text_width = this.xhsi_gc.get_text_width(g2, this.xhsi_gc.font_statusbar, "NAV DB 2000.99");
        }
        if (utc_text_width == 0) {
            utc_text_width = this.xhsi_gc.get_text_width(g2, this.xhsi_gc.font_statusbar, "00:00:00");
        }
        fps_x = xhsi_gc.border_left + 120;
        fps_y = XHSIGraphicsConfig.STATUS_BAR_HEIGHT - 25;
        fps_pixels_per_frame = 2;
        src_x = xhsi_gc.border_left;
        src_y = XHSIGraphicsConfig.STATUS_BAR_HEIGHT - 21;
        nav_db_status_x = xhsi_gc.panel_size.width - xhsi_gc.border_right - nav_db_text_width;
        nav_db_status_y = XHSIGraphicsConfig.STATUS_BAR_HEIGHT - 25;
        utc_x = xhsi_gc.panel_size.width - xhsi_gc.border_right - nav_db_text_width - 65 - utc_text_width;
        utc_y = XHSIGraphicsConfig.STATUS_BAR_HEIGHT - 25;
        draw_data_source(g2);
        draw_frame_rate(g2);
        draw_nav_db_status(g2);
        draw_utc_clock(g2);
    }

    public void draw_data_source(Graphics2D g2) {
        int x_offs = src_x + 60;
        int x_points_caret[] = { x_offs + 4, x_offs + 10, x_offs + 16, x_offs + 10, x_offs + 4 };
        int y_points_caret[] = { src_y + 6, src_y, src_y + 6, src_y + 12, src_y + 6 };
        int x_points_arrowhead[] = { x_offs + 8, x_offs + 13, x_offs + 8, x_offs + 8 };
        int y_points_arrowhead[] = { src_y + 3, src_y + 6, src_y + 9, src_y + 3 };
        int x_points_playtriangle[] = { src_x + 18, src_x + 10, src_x + 10 };
        int y_points_playtriangle[] = { src_y + 7, src_y + 7 - 5, src_y + 7 + 5 };
        g2.setColor(Color.BLACK);
        g2.setFont(xhsi_gc.font_statusbar);
        g2.drawPolygon(x_points_caret, y_points_caret, 5);
        g2.drawLine(x_offs, src_y + 6, x_offs + 8, src_y + 6);
        g2.fillPolygon(x_points_arrowhead, y_points_arrowhead, 4);
        if (XPlaneSimDataRepository.source_is_recording) {
            g2.drawString("PLAY", src_x + 25, src_y + 11);
            g2.setColor(Color.BLUE);
            g2.fillPolygon(x_points_playtriangle, y_points_playtriangle, 3);
        } else {
            g2.drawString("X-Plane", src_x, src_y + 11);
            if (XHSIStatus.status.equals(XHSIStatus.STATUS_NO_RECEPTION)) {
                g2.setColor(Color.RED);
                g2.drawLine(src_x + 15, src_y + 12, src_x + 30, src_y);
                g2.drawLine(src_x + 15, src_y, src_x + 30, src_y + 12);
            }
        }
    }

    public void draw_nav_db_status(Graphics2D g2) {
        g2.setColor(Color.BLACK);
        g2.setFont(xhsi_gc.font_statusbar);
        g2.drawString("NAV DB " + XHSIStatus.nav_db_cycle, nav_db_status_x, nav_db_status_y + 13);
        if (XHSIStatus.nav_db_status.equals(XHSIStatus.STATUS_NAV_DB_NOT_FOUND)) {
            g2.setColor(Color.RED);
            g2.drawLine(nav_db_status_x + 15, nav_db_status_y + 14, nav_db_status_x + 30, nav_db_status_y + 2);
            g2.drawLine(nav_db_status_x + 15, nav_db_status_y + 2, nav_db_status_x + 30, nav_db_status_y + 14);
        }
    }

    public void draw_utc_clock(Graphics2D g2) {
        DecimalFormat hms_formatter = new DecimalFormat("00");
        float utc_time = this.aircraft.sim_time_zulu();
        long hh = (long) utc_time / 3600;
        long mm = (long) utc_time / 60 % 60;
        long ss = (long) utc_time % 60;
        String utc_text = hms_formatter.format(hh) + ":" + hms_formatter.format(mm) + ":" + hms_formatter.format(ss);
        g2.setColor(Color.WHITE);
        g2.setFont(xhsi_gc.font_medium);
        g2.drawString(utc_text, utc_x, utc_y + 15);
    }

    public void draw_frame_rate(Graphics2D g2) {
        if ((XHSIStatus.status.equals(XHSIStatus.STATUS_RECEIVING)) || (XHSIStatus.status.equals(XHSIStatus.STATUS_PLAYING_RECORDING))) {
            g2.setColor(Color.BLACK);
            g2.setFont(xhsi_gc.font_statusbar);
            g2.drawString("FPS", fps_x, fps_y + 11);
            for (int i = 0; i <= 40; i += 10) {
                g2.drawLine(fps_x + 30 + (i * fps_pixels_per_frame), fps_y + 10, fps_x + 30 + (i * fps_pixels_per_frame), fps_y + 13);
            }
            if (this.frame_rate > 40) {
                this.frame_rate = 40;
                g2.drawString("+", fps_x + 33 + (40 * fps_pixels_per_frame), fps_y + 11);
            }
            g2.setFont(xhsi_gc.font_statusbar);
            g2.drawString("0", fps_x + 28, fps_y + 23);
            g2.drawString("20", fps_x + 25 + (20 * fps_pixels_per_frame), fps_y + 23);
            g2.drawString("40", fps_x + 25 + (40 * fps_pixels_per_frame), fps_y + 23);
            if (this.frame_rate > 12) {
                g2.setColor(Color.GREEN.darker());
            } else if (this.frame_rate > 8) {
                g2.setColor(Color.YELLOW.darker());
            } else if (this.frame_rate > 4) {
                g2.setColor(Color.ORANGE);
            } else {
                g2.setColor(Color.RED);
            }
            g2.fillRect(fps_x + 30, fps_y, (this.frame_rate * fps_pixels_per_frame), 8);
        }
    }
}
