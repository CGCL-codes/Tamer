package com.thirdnf.resourceScheduler.components;

import com.thirdnf.resourceScheduler.Appointment;
import org.jetbrains.annotations.NotNull;
import javax.swing.*;
import java.awt.*;

/**
 * This is the base class for an appointment component which are actually drawn on the panel.
 * At minimum it needs to be a JComponent but the drawing of the component is entirely up to
 * the implementation.
 *
 * @see BasicAppointmentComponent
 *
 * @author Joshua Gerth - jgerth@thirdnf.com
 */
public abstract class AbstractAppointmentComponent extends JComponent {

    protected final Appointment _appointment;

    /**
     * Constructor given an appointment to wrap.
     *
     * @param appointment (not null) The appointment to wrap
     */
    protected AbstractAppointmentComponent(@NotNull Appointment appointment) {
        _appointment = appointment;
    }

    /**
     * Get the appointment from the component.
     *
     * @return (not null) The appointment this component was wrapping.
     */
    @NotNull
    public Appointment getAppointment() {
        return _appointment;
    }

    /**
     * Print this component into the given area using the graphics handle.
     *
     * @param graphics (not null) Graphics handle to draw with
     * @param area (not null) Area for printing
     */
    public abstract void print(@NotNull Graphics2D graphics, Rectangle area);
}
