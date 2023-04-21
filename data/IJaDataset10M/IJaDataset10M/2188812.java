package org.openbandy.log;

import java.io.IOException;
import javax.microedition.lcdui.Choice;
import javax.microedition.lcdui.Command;
import javax.microedition.lcdui.CommandListener;
import javax.microedition.lcdui.Display;
import javax.microedition.lcdui.Displayable;
import javax.microedition.lcdui.Image;
import javax.microedition.lcdui.List;
import org.openbandy.service.LogService;
import org.openbandy.ui.BandyDisplayable;

/**
 * This form is used to set the level of filtering (i.e. which kind of log
 * messages are shown in the log canvas).
 * 
 * <br>
 * <br>
 * (c) Copyright P. Bolliger 2007, ALL RIGHTS RESERVED.
 * 
 * @author Philipp Bolliger (philipp@bolliger.name)
 * @version 1.0
 */
public class SelectFilterLevelForm extends List implements BandyDisplayable, CommandListener {

    private Command cmdSelect;

    private Command cmdOk;

    private Command cmdCancel;

    private Image imgChecked;

    private Image imgUnchecked;

    /** Reference to the MIDlets display */
    private Display display;

    /** Reference to the log canvas */
    private LogImpl logCanvas;

    /**
	 * Create a new select filter level form
	 */
    public SelectFilterLevelForm() {
        super("Filter Level", Choice.IMPLICIT);
        cmdSelect = new Command("Select", Command.SCREEN, 1);
        cmdOk = new Command("Ok", Command.OK, 1);
        cmdCancel = new Command("Cancel", Command.CANCEL, 1);
        addCommand(cmdSelect);
        addCommand(cmdOk);
        addCommand(cmdCancel);
        this.setSelectCommand(cmdSelect);
        try {
            imgChecked = Image.createImage("/img/checked.png");
            imgUnchecked = Image.createImage("/img/unchecked.png");
        } catch (IOException ioe) {
            LogService.error(this, ioe.getMessage(), ioe);
        }
        this.setCommandListener(this);
    }

    public void show(Display display, Displayable previousDisplayable) {
        this.display = display;
        try {
            this.logCanvas = (LogImpl) previousDisplayable;
            for (int i = 0; i < LogLevel.levels.length; i++) {
                if (logCanvas.config.filterLevels[i]) {
                    append(LogLevel.levels[i], imgChecked);
                } else {
                    append(LogLevel.levels[i], imgUnchecked);
                }
            }
        } catch (ClassCastException cce) {
            LogService.error(this, "Must have LogCanvas as previous screen", cce);
        }
        display.setCurrent(this);
    }

    public void commandAction(Command c, Displayable d) {
        if (c == cmdSelect) {
            try {
                int index = getSelectedIndex();
                logCanvas.config.setFilterLevel(index, (!logCanvas.config.filterLevels[index]));
                if (logCanvas.config.filterLevels[index]) {
                    set(index, LogLevel.levels[index], imgChecked);
                } else {
                    set(index, LogLevel.levels[index], imgUnchecked);
                }
            } catch (Exception e) {
                LogService.error(this, e.getMessage(), e);
            }
        } else if (c == cmdCancel) {
            display.setCurrent(logCanvas);
        } else if (c == cmdOk) {
            logCanvas.resetCanvas();
            display.setCurrent(logCanvas);
        }
    }
}
