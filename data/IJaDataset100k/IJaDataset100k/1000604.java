package com.lake.pim.api;

public class GUI_Settings {

    private int frame_x = 50;

    private int frame_y = 50;

    private int frame_width = 700;

    private int slot_height = 20;

    private int decor_height = 20;

    private long number_of_rendered_hours = 7 * 24;

    private String gui_settings_file_name = "";

    private boolean ext_view = false;

    private boolean show_row_labels = false;

    public GUI_Settings() {
    }

    public GUI_Settings(int x, int y, int width, int sh, int dh, long nor_hours, boolean eview, boolean rl) {
        frame_x = x;
        frame_y = y;
        frame_width = width;
        slot_height = sh;
        decor_height = dh;
        number_of_rendered_hours = nor_hours;
        ext_view = eview;
        show_row_labels = rl;
    }

    /**
     *  Get stored x position of main frame 
     *
     * @return    x position of frame
     */
    public int get_frame_x() {
        return frame_x;
    }

    /**
     *  Set stored x position of main frame 
     *
     */
    public void set_frame_x(int x) {
        frame_x = x;
        return;
    }

    /**
     *  Get stored y position of main frame 
     *
     * @return    y position of frame
     */
    public int get_frame_y() {
        return frame_y;
    }

    /**
     *  Set stored y position of main frame 
     */
    public void set_frame_y(int y) {
        frame_y = y;
        return;
    }

    /**
     *  Get stored main frame width 
     *
     * @return    frame width
     */
    public int get_frame_width() {
        return frame_width;
    }

    /**
     *  Set stored main frame width 
     */
    public void set_frame_width(int width) {
        frame_width = width;
        return;
    }

    /**
     *  Get stored slot height 
     *
     * @return    slot height 
     */
    public int get_slot_height() {
        return slot_height;
    }

    /**
     *  Set stored slot height 
     */
    public void set_slot_height(int height) {
        slot_height = height;
        return;
    }

    /**
     *  Get stored decor height of main frame
     *
     * @return    decor height 
     */
    public int get_decor_height() {
        return decor_height;
    }

    /**
     *  Set stored decor height of main frame
     */
    public void set_decor_height(int height) {
        decor_height = height;
        return;
    }

    /**
     *  Get stored number of rendererd hours in main frame
     *
     * @return    number of rendererd hours
     */
    public long get_number_of_rendered_hours() {
        return number_of_rendered_hours;
    }

    /**
     *  Set stored number of rendererd hours in main frame
     */
    public void set_number_of_rendered_hours(long hours) {
        number_of_rendered_hours = hours;
        return;
    }

    /**
     *  Get stored view mode
     *
     * @return    false - simple view<br>
     *            true - extended view
     */
    public boolean get_ext_view() {
        return ext_view;
    }

    /**
     *  Set stored view mode
     *
     * @          false - simple view<br>
     *            true - extended view
     */
    public void set_ext_view(boolean mode) {
        ext_view = mode;
        return;
    }

    /**
     *  Get whether row labels are to be show
     *
     * @return    false - row labels not shown<br>
     *            true - row labels are shown
     */
    public boolean get_show_row_labels() {
        return show_row_labels;
    }

    /**
     *  Set whether row labels are to be show
     *
     * @          false - row labels not shown<br>
     *            true - row labels are shown
     */
    public void set_show_row_labels(boolean show) {
        show_row_labels = show;
        return;
    }
}
