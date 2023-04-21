package db;

public class Cust_style_measure_x {

    private long cust_style_measure_x_id;

    private long cust_id;

    private long style_id;

    private long measure_id;

    private String note;

    private String measure_note;

    private String style_name;

    public String getMeasure_note() {
        return measure_note;
    }

    public void setMeasure_note(String measure_note) {
        this.measure_note = measure_note;
    }

    public String getStyle_name() {
        return style_name;
    }

    public void setStyle_name(String style_name) {
        this.style_name = style_name;
    }

    public long getCust_style_measure_x_id() {
        return this.cust_style_measure_x_id;
    }

    public void setCust_style_measure_x_id(long param) {
        this.cust_style_measure_x_id = param;
    }

    public long getCust_id() {
        return this.cust_id;
    }

    public void setCust_id(long param) {
        this.cust_id = param;
    }

    public long getStyle_id() {
        return this.style_id;
    }

    public void setStyle_id(long param) {
        this.style_id = param;
    }

    public long getMeasure_id() {
        return this.measure_id;
    }

    public void setMeasure_id(long param) {
        this.measure_id = param;
    }

    public String getNote() {
        return this.note;
    }

    public void setNote(String param) {
        this.note = param;
    }

    public String toString() {
        return " [cust_style_measure_x_id] " + cust_style_measure_x_id + " [cust_id] " + cust_id + " [style_id] " + style_id + " [measure_id] " + measure_id + " [note] " + note;
    }
}
