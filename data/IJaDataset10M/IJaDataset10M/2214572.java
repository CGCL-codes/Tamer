package com.softaspects.jsf.plugin.facelets.tag.calendar;

import com.sun.facelets.tag.AbstractTagLibrary;

public class CalendarTagLibrary extends AbstractTagLibrary {

    public static final String Namespace = "http://www.softaspects.com/wgf/facelets/calendar";

    public static final CalendarTagLibrary Instance = new CalendarTagLibrary();

    public CalendarTagLibrary() {
        super(Namespace);
        addComponent("calendar", "com.softaspects.jsf.component.calendar.Calendar", "com.softaspects.jsf.renderer.calendar.CalendarRenderer");
    }
}
