package com.lake.pim.gui;

import java.awt.Color;
import java.util.GregorianCalendar;
import com.lake.pim.api.PIMEvent;
import com.lake.pim.api.Converter;

public class Event extends PIMEvent {

    private EventRenderer my_renderer;

    private EditorFrame my_editor_frame = null;

    public Event(long b, int rg) {
        super(b, rg);
        summary_encoding = EventMemory.get_instance(null).default_encoding;
        description_encoding = EventMemory.get_instance(null).default_encoding;
    }

    public Event() {
        super();
        summary_encoding = EventMemory.get_instance(null).default_encoding;
        description_encoding = EventMemory.get_instance(null).default_encoding;
    }

    public Event(PIMEvent pimEvent) {
        this.set_summary(pimEvent.get_summary());
        this.set_summary_encoding(pimEvent.get_summary_encoding());
        this.set_location(pimEvent.get_location());
        this.set_location_encoding(pimEvent.get_location_encoding());
        this.set_description(pimEvent.get_description());
        this.set_description_encoding(pimEvent.get_description_encoding());
        this.set_begin_UTC_ms(pimEvent.get_begin_UTC_ms());
        this.set_UID(pimEvent.get_UID());
        this.set_last_mod_UTC_ms(pimEvent.get_last_mod_UTC_ms());
        this.set_end_UTC_ms(pimEvent.get_end_UTC_ms());
        this.set_alarm_UTC_ms(pimEvent.get_alarm_UTC_ms());
        this.set_vcal_class(pimEvent.get_vcal_class());
        this.set_period(pimEvent.get_period());
        this.set_number_of_periods(pimEvent.get_number_of_periods());
        this.set_period_multiplier(pimEvent.get_period_multiplier());
        this.set_alarm_counter(pimEvent.get_alarm_counter());
        this.set_alarm_active(pimEvent.get_alarm_active());
        this.set_now_imported(pimEvent.get_now_imported());
        this.set_deleted_while_import(pimEvent.get_deleted_while_import());
        this.set_renderer_group(pimEvent.get_renderer_group());
        this.set_renderer_color(pimEvent.get_renderer_color());
    }

    /**
     *  Set summary encoding (e.g. "ISO-8859-15").
     *
     * @param  s  summary encoding
     */
    public synchronized void set_summary_encoding(String s) {
        if (s.length() == 0) {
            summary_encoding = EventMemory.get_instance(null).default_encoding;
        } else {
            summary_encoding = s;
        }
    }

    /**
     *  Set description encoding.
     *
     * @param  s  description encoding (e.g. "ISO-8859-15")
     */
    public synchronized void set_description_encoding(String s) {
        if ((s == null) || (s.length() == 0)) {
            description_encoding = EventMemory.get_instance(null).default_encoding;
        } else {
            description_encoding = s;
        }
    }

    /**
     *  Get my editor frame.
     *
     * @return    my editor frame or null.
     */
    public synchronized EditorFrame get_my_editor_frame() {
        return my_editor_frame;
    }

    /**
     *  Set my editor frame.<br>
     *  Sets the focus to the renderer if ef != null or otherwise.<br>
     *  removes the focus from the renderer.
     *
     * @param  ef  editor frame or null
     */
    public synchronized void set_my_editor_frame(EditorFrame ef) {
        my_editor_frame = ef;
        if (my_renderer != null) {
            if (ef != null) {
                my_renderer.set_focus(true);
            } else {
                my_renderer.set_focus(false);
            }
        }
    }

    /**
     *  Sets the event_renderer
     *
     * @param  tr  The new event_renderer value
     */
    public synchronized void set_event_renderer(EventRenderer tr) {
        my_renderer = tr;
    }

    /**
     *  Get my event renderer.
     *
     * @return    my event renderer
     */
    public synchronized EventRenderer get_event_renderer() {
        return my_renderer;
    }

    public synchronized void changed() {
        set_last_mod_UTC_ms(new GregorianCalendar().getTime().getTime());
        EventRenderer.changed();
        EventMemory.get_instance(null).changed();
    }

    public synchronized void deleted() {
        set_last_mod_UTC_ms(new GregorianCalendar().getTime().getTime());
        if (my_editor_frame != null) {
            my_editor_frame.dispose();
            my_editor_frame = null;
        }
        EventRenderer.changed();
        my_renderer = null;
    }

    /**
     *  Set alarm counter to next after now
     */
    public synchronized void set_alarm_counter_to_next_after_now() {
        long now_ms = new GregorianCalendar().getTime().getTime();
        alarm_counter = (int) Math.max(((now_ms - alarm_UTC_ms) / Converter.period2ms(period, period_multiplier)) - 1, 0);
        while (!(Converter.UTCplusPeriod2UTC(alarm_UTC_ms, period, alarm_counter, period_multiplier) > now_ms)) {
            alarm_counter++;
        }
    }

    /**
     *  Clone event.<br>
     *  But UID is a new one in the cloned event.
     *
     * @return    the cloned event
     */
    public synchronized Event clone2() {
        Event t = new Event();
        t.set_alarm_active(this.get_alarm_active());
        t.set_alarm_counter(this.get_alarm_counter());
        t.set_alarm_UTC_ms(this.get_alarm_UTC_ms());
        t.set_begin_UTC_ms(this.get_begin_UTC_ms());
        t.set_end_UTC_ms(this.get_end_UTC_ms());
        t.set_period(this.get_period());
        t.set_period_multiplier(this.get_period_multiplier());
        t.set_number_of_periods(this.get_number_of_periods());
        t.set_summary(this.get_summary());
        t.set_description(this.get_description());
        t.set_location(this.get_location());
        t.set_last_mod_UTC_ms(this.get_last_mod_UTC_ms());
        t.set_description_encoding(this.get_description_encoding());
        t.set_summary_encoding(this.get_summary_encoding());
        t.set_vcal_class(this.get_vcal_class());
        t.set_renderer_color(this.get_renderer_color());
        t.set_renderer_group(this.get_renderer_group());
        return t;
    }
}
