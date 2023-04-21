package com.lake.pim.gui;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import java.text.*;
import java.awt.geom.*;
import java.awt.font.*;

/**
 *  Rene's Component. Input-item painted on a RPanel.<br>
 *  Its value can be modified by mouse events.
 */
public class RComponent extends Renderer {

    private int x_relative;

    private int y_relative;

    private int witdh_relative;

    private int font_size_relative;

    private int value;

    private int max_value;

    private int min_value;

    private int step;

    private boolean selected = false;

    private String additional_string = "";

    private String[] string_aray;

    private Color bg_color;

    private Color selected_bg_color = Color.red;

    private Color high_light_color;

    private int last_panel_width = 0;

    private int last_panel_height = 0;

    private int last_value;

    private Font font;

    private String draw_string;

    private int x_pos;

    private int y_pos;

    private int height;

    private int ascent;

    private int width;

    private Color tmp_color;

    /**
   *  Constructor for the RComponent object
   *
   * @param  p    parent panel
   * @param  bg   back ground colour
   * @param  hl   high light colour
   * @param  i    value
   * @param  max  max value
   * @param  min  min value
   * @param  s    step value (value is in/decreased by this step on mouse event)
   * @param  sa   string area, contains a string for each possible value, this string is shown if the value is true or null
   * @param  a    additional shown fixed string (must not be null)
   * @param  x    relative position on panel x, range 0 - 1000, 0 - left, 1000 right border
   * @param  y    relative position on panel y, range 0 - 1000, 0 - top, 1000 bottom border
   * @param  w    relative width, range 0 - 1000, 0 - 0, 1000 - 100% of panel
   * @param  fs   relative font size (fs * panel_width / 1000 = real font size)
   */
    public RComponent(RPanel p, Color bg, Color hl, int i, int max, int min, int s, String[] sa, String a, int x, int y, int w, int fs) {
        super(p);
        x_relative = x;
        y_relative = y;
        witdh_relative = w;
        font_size_relative = fs;
        string_aray = sa;
        additional_string = a;
        bg_color = bg;
        high_light_color = hl;
        tmp_color = bg;
        value = i;
        max_value = max;
        min_value = min;
        step = s;
    }

    /**
   *  Draw the RComponent on panel
   *
   * @param  g2  Graphics2D object
   */
    public void draw(Graphics2D g2) {
        if (last_panel_width != super.panel.getWidth() || last_panel_height != super.panel.getHeight() || last_value != value) {
            int panel_width = super.panel.getWidth();
            int panel_height = super.panel.getHeight();
            x_pos = x_relative * panel_width / 1000;
            y_pos = y_relative * panel_height / 1000;
            width = witdh_relative * panel_width / 1000;
            font = new Font("SansSerif", Font.PLAIN, font_size_relative * panel_width / 1000);
            if (string_aray == null) {
                NumberFormat formatter = NumberFormat.getNumberInstance();
                formatter.setMinimumIntegerDigits(Integer.toString(max_value).length());
                formatter.setGroupingUsed(false);
                draw_string = formatter.format((long) value);
            } else {
                draw_string = string_aray[value];
            }
            Rectangle2D bounds = font.getStringBounds(draw_string, g2.getFontRenderContext());
            ascent = (int) -bounds.getY();
            height = (int) bounds.getHeight();
            if (bounds.getWidth() + height / 5 > width) {
                draw_string = draw_string.substring(0, draw_string.length() - 3) + "...";
                bounds = font.getStringBounds(draw_string, g2.getFontRenderContext());
            }
            while (bounds.getWidth() + height / 5 > width) {
                draw_string = draw_string.substring(0, draw_string.length() - 4) + "...";
                bounds = font.getStringBounds(draw_string, g2.getFontRenderContext());
            }
            last_panel_width = panel_width;
            last_panel_height = panel_height;
            last_value = value;
        }
        g2.setColor(tmp_color);
        g2.fillRoundRect(x_pos, y_pos - 1, width, height + 2, height + 2, height + 2);
        if (tmp_color.getGreen() + tmp_color.getRed() + tmp_color.getBlue() < 300) {
            g2.setColor(Color.white);
        } else {
            g2.setColor(Color.black);
        }
        g2.setFont(font);
        g2.drawString(draw_string + additional_string, x_pos + height / 5, y_pos + ascent);
    }

    /**
   *  Check whether the mouse click hits the RComponent
   *
   * @param  e  mouse event
   * @return    true - if RComponent meet<br>
   *            false - if RComponent not meet
   */
    public boolean mouse_clicked(MouseEvent e) {
        int x = e.getX();
        int y = e.getY();
        if (y > y_pos && y < y_pos + height && x > x_pos && x < x_pos + width) {
            if ((e.getModifiers() & InputEvent.BUTTON1_MASK) != 0 && (e.getModifiers() & InputEvent.BUTTON3_MASK) == 0) {
                value = value + step;
                if (value > max_value) {
                    value = min_value;
                }
            } else {
                value = value - step;
                if (value < min_value) {
                    value = max_value;
                }
            }
            return true;
        }
        return false;
    }

    /**
   *  Check whether the mouse is over the RComponent and control the RComponents colour.<br>
   *  If mouse is over the RComponent it switches to the highlight colour.
   *
   * @param  e  mouse event
   * @return    true - if button meet<br>
   *            false - if button not meet
   */
    public boolean mouse_over(MouseEvent e) {
        int x = e.getX();
        int y = e.getY();
        if (y > y_pos && y < y_pos + height && x > x_pos && x < x_pos + width) {
            tmp_color = high_light_color;
            return true;
        }
        if (selected) {
            tmp_color = selected_bg_color;
        } else {
            tmp_color = bg_color;
        }
        return false;
    }

    /**
   *  Check whether the mouse wheel rotates over the RComponent.<br>
   *  If true the value is changed.
   *
   * @param  e  mouse wheel event
   * @return    true - if button meet<br>
   *            false - if button not meet
   */
    public boolean mouse_wheel_rotate(MouseWheelEvent e) {
        int x = e.getX();
        int y = e.getY();
        if (y > y_pos && y < y_pos + height && x > x_pos && x < x_pos + width) {
            value = value - step * e.getWheelRotation();
            if (value > max_value) {
                value = min_value;
            } else if (value < min_value) {
                value = max_value;
            }
            return true;
        }
        return false;
    }

    /**
   *  Gets the value attribute of the RComponent object
   *
   * @return    The value value
   */
    public int get_value() {
        return value;
    }

    /**
   *  Set value
   *
   * @param  i  value
   */
    public void set_value(int i) {
        value = i;
    }

    /**
   *  Set backgroung colour
   *
   * @param  c  background colour
   */
    public void set_bg_colour(Color c) {
        bg_color = c;
        tmp_color = c;
    }

    /**
   *  Set selected
   *
   * @param  b  true - selected<br>
   *            false - not selected
   */
    public void set_selected(boolean b) {
        selected = b;
        if (tmp_color.equals(high_light_color)) {
            return;
        }
        if (b) {
            tmp_color = selected_bg_color;
        } else {
            tmp_color = bg_color;
        }
    }

    /**
   *  Set additional string
   *
   * @param  s  additional string
   */
    public void set_additional_string(String s) {
        additional_string = s;
    }

    /**
   *  Set string aray, contains a string for each possible value,<br>
   *  this string is shown if the value is true
   *
   * @param  sa  string aray
   */
    public void set_string_aray(String[] sa) {
        string_aray = sa;
        draw_string = string_aray[value];
    }
}
