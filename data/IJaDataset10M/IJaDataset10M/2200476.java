package com.etnetera.midlet.gps.ui;

import javax.microedition.lcdui.CommandListener;
import com.etnetera.midlet.gps.Context;
import com.etnetera.midlet.gps.GPSForm;

public class LoggerScreen extends GPSForm {

    public LoggerScreen(Context ctx, String text, CommandListener cl) {
        super(ctx, text, cl);
    }
}
