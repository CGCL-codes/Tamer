package de.flingelli.scrum.datastructure.gui;

import java.awt.Color;
import de.flingelli.scrum.datastructure.appointment.EAppointmentType;

public class AppointmentItem implements Cloneable {

    private EAppointmentType type;

    private int rgb;

    AppointmentItem() {
    }

    private AppointmentItem(EAppointmentType type) {
        this();
        this.type = type;
    }

    public AppointmentItem(EAppointmentType type, Color color) {
        this(type);
        this.rgb = color.getRGB();
    }

    public AppointmentItem(EAppointmentType type, int rgb) {
        this(type);
        this.rgb = rgb;
    }

    public EAppointmentType getType() {
        return type;
    }

    public void setType(EAppointmentType type) {
        this.type = type;
    }

    public int getRgb() {
        return rgb;
    }

    public void setRgb(int rgb) {
        this.rgb = rgb;
    }
}
