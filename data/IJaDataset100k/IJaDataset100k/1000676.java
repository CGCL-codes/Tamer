package org.openbandy.log;

import java.io.IOException;
import java.io.PrintStream;
import java.util.Vector;
import javax.microedition.io.Connector;
import javax.microedition.io.file.FileConnection;
import javax.microedition.lcdui.Alert;
import javax.microedition.lcdui.AlertType;
import javax.microedition.lcdui.Canvas;
import javax.microedition.lcdui.Command;
import javax.microedition.lcdui.CommandListener;
import javax.microedition.lcdui.Display;
import javax.microedition.lcdui.Displayable;
import javax.microedition.lcdui.Font;
import javax.microedition.lcdui.Graphics;
import javax.microedition.lcdui.Image;
import javax.microedition.midlet.MIDlet;
import org.openbandy.service.Service;
import org.openbandy.ui.BandyDisplayable;
import org.openbandy.util.CollectionsUtil;
import org.openbandy.util.StringUtil;

/**
 * Implementation of the Log interface for use with CLDC and MIDP on mobile
 * devices. This class can write log message to a canvas (default), the devices
 * persistent RMS, or a file.
 * 
 * <br>
 * <br>
 * (c) Copyright P. Bolliger 2007, ALL RIGHTS RESERVED.
 * 
 * @author Philipp Bolliger (philipp@bolliger.name)
 * @version 2.2
 */
public class LogImpl extends Canvas implements Log, BandyDisplayable, CommandListener, Runnable {

    /** String value to delimit values */
    public static final String VALUE_DELIMITER = " , ";

    /** String value to delimit different variables */
    public static final String VARIABLE_DELIMITER = " ; ";

    /** The pixel width and height respectively of the scroll bar */
    private static final int SCROLL_BAR_SIZE = 2;

    /** The number of pixels to move with one scroll step */
    private static final int SCROLL_STEP = 50;

    /** The configuration of the canvas as a screen */
    public LogConfiguration config;

    /** The log storage used to persistently store log messages */
    private LogStorage logStorage;

    /** The stream used to write log messages to the local file system */
    private PrintStream logPrintStream;

    /** Indicates whether the print stream has been opened */
    private boolean openedLogPrintStream = false;

    /** The list of log entries */
    private Vector logEntries = new Vector();

    /**
	 * The (temporary) list of log entries that are displayed (after applying a
	 * filter)
	 */
    private Vector logEntriesToDisplay = new Vector();

    /** Indicator for 'auto scroll' function, ON by default. */
    private boolean funcAutoScroll = true;

    /** Indicates that the scrolling is triggered by keys. */
    private boolean keyScroll = true;

    /** Indicator for 'beep on error' function, OFF by default. */
    private boolean funcBeepOnError = false;

    /** Indicator for 'show status box' function, OFF by default. */
    private boolean funcShowStatusBox = false;

    /** Font to be used for the log messages */
    private static Font logMessageFont;

    /** Height of log messages font in pixels */
    private static int logMessageFontHeight;

    /**
	 * Width of one char of the log message font in pixels, always the same as
	 * we use a mono space font. This is not always correct on all phones, some
	 * return a too big values, but overall this saves a lot of cpu time!
	 */
    private static int logMessageFontWidthPerChar;

    /** Font to be used for the status box */
    private static Font statusBoxFont;

    /** Height of status box font in pixels */
    private static int statusBoxFontHeight = 0;

    /** top left corner of log canvas */
    private int x0 = 0;

    /** top left corner of log canvas */
    private int y0 = 0;

    /** bottom line of log canvas */
    private int yMax = 0;

    /** right border of log canvas */
    private int xMax = 0;

    /** top left corner of level status box */
    private int yStatus = 0;

    /** width of graphic canvas */
    private int width = 0;

    /** height of graphic canvas */
    private int height = 0;

    /** number of pixels to scroll at once on the y-axis */
    private int scrollStepY = SCROLL_STEP;

    /** number of pixels to scroll at once on the x-axis */
    private int scrollStepX = SCROLL_STEP;

    /** Thread to animate fade-in/-out of the status box */
    private Thread animateStatusBox;

    /** The string that is displayed as status */
    private String statusMessage = "";

    /** The width of the string that is displayed as status */
    private int statusMessageWidth = 0;

    /**
	 * Reference to the MIDlet, needed if run with this as only screen, i.e.,
	 * without a MainMenu
	 */
    private MIDlet midlet;

    /** Reference to the MIDlets display */
    protected Display display;

    /** Reference to the MIDlets previous displayable */
    protected Displayable previousDisplayable;

    /** The image that is drawn as log background */
    private static Image bandyLogoBackgroundImage;

    /** Command to go back to the previous screen */
    private Command cmdBack;

    /**
	 * Command to exit the MIDlet. This is used when LogCanvas is the only
	 * screen as for example in the
	 * 
	 * @see bandy.log.LogStorageViewerMIDlet.
	 */
    private Command cmdExit;

    /** Commands to change the log and filter level */
    private Command cmdSelectLogLevel;

    private Command cmdSelectFilterLevel;

    /** Commands to change mode */
    private Command cmdSelectLogMode;

    /** Command to show the SelectBtDeviceForm */
    private Command cmdSelectBtDevice;

    /** Command to clean the log canvas */
    private Command cmdClearCanvas;

    /** Command to clear the persistent log */
    private Command cmdClearLogStorage;

    /** Commands to the turn 'auto scroll' on and off */
    private Command cmdAutoScrollOn;

    private Command cmdAutoScrollOff;

    /** Commands to the turn 'beep on error' on and off */
    private Command cmdBeepOnErrorOn;

    private Command cmdBeepOnErrorOff;

    /** Commands to show to log and filter level status */
    private Command cmdShowStatus;

    private Command cmdHideStatus;

    /**
	 * Create a new LogImple instance and configure it accordingly
	 * 
	 * @param config The (initial) log config that shall apply to this instance
	 */
    public LogImpl(LogConfiguration config) {
        setTitle("Log");
        this.setCommandListener(this);
        this.config = config;
        logStorage = new LogStorage(this);
        if (config.logModeFileEnabled()) {
            if (!openedLogPrintStream) {
                PrintStreamOpener printStreamOpener = new PrintStreamOpener();
                printStreamOpener.start();
            }
        }
        if (config.showCommandClearCanvas) {
            cmdClearCanvas = new Command("Clear Log", Command.SCREEN, 1);
            addCommand(cmdClearCanvas);
        }
        if (config.showCommandClearLogStorage) {
            cmdClearLogStorage = new Command("Clear Persistent Log", Command.SCREEN, 1);
            addCommand(cmdClearLogStorage);
        }
        if (config.showStatusBox) {
            cmdShowStatus = new Command("Show Levels", Command.SCREEN, 2);
            cmdHideStatus = new Command("Hide Levels", Command.SCREEN, 2);
            if (funcShowStatusBox) {
                addCommand(cmdHideStatus);
            } else {
                addCommand(cmdShowStatus);
            }
        }
        if (config.showCommandSelectLogLevel) {
            cmdSelectLogLevel = new Command("Log Level", Command.SCREEN, 3);
            addCommand(cmdSelectLogLevel);
        }
        if (config.showCommandSelectFilter) {
            cmdSelectFilterLevel = new Command("Filter Level", Command.SCREEN, 4);
            addCommand(cmdSelectFilterLevel);
        }
        if (config.showCommandSelectMode) {
            cmdSelectLogMode = new Command("Log Modes", Command.SCREEN, 5);
            addCommand(cmdSelectLogMode);
        }
        if (config.showCommandAutoScroll) {
            cmdAutoScrollOn = new Command("Auto Scroll", Command.SCREEN, 5);
            cmdAutoScrollOff = new Command("Lock Scroll", Command.SCREEN, 5);
            if (funcAutoScroll) {
                addCommand(cmdAutoScrollOff);
            } else {
                addCommand(cmdAutoScrollOn);
            }
        }
        if (config.showCommandBeep) {
            cmdBeepOnErrorOn = new Command("Beep On Error", Command.SCREEN, 6);
            cmdBeepOnErrorOff = new Command("Dont Beep", Command.SCREEN, 6);
            if (funcBeepOnError) {
                addCommand(cmdBeepOnErrorOff);
            } else {
                addCommand(cmdBeepOnErrorOn);
            }
        }
        if (config.showCommandSendLog) {
            cmdSelectBtDevice = new Command("Send Log", Command.SCREEN, 7);
            addCommand(cmdSelectBtDevice);
        }
        if (config.showCommandBack) {
            cmdBack = new Command("Back", Command.BACK, 8);
            addCommand(cmdBack);
        } else {
            cmdExit = new Command("Exit", Command.EXIT, 8);
            addCommand(cmdExit);
        }
        logMessageFont = Font.getFont(Font.FACE_MONOSPACE, Font.STYLE_PLAIN, Font.SIZE_SMALL);
        logMessageFontHeight = logMessageFont.getHeight();
        logMessageFontWidthPerChar = logMessageFont.charWidth('A');
        if (config.showStatusBox) {
            statusBoxFont = Font.getFont(Font.FACE_PROPORTIONAL, Font.STYLE_PLAIN, Font.SIZE_SMALL);
            statusBoxFontHeight = statusBoxFont.getHeight();
        }
        try {
            bandyLogoBackgroundImage = Image.createImage("/img/BandyLogoBackground.png");
        } catch (IOException e) {
        }
    }

    public void setMIDlet(MIDlet midlet) {
        this.midlet = midlet;
    }

    /**
	 * Logs a message with log level LEVEL_ERROR
	 * 
	 * @param origin
	 *            The object reporting
	 * @param message
	 *            The message to log
	 * @param exception
	 *            The exception that was caused with this error
	 */
    public void error(Object origin, String message, Throwable exception) {
        doLog(LogLevel.ERROR, message, exception, origin);
        if (config.showCommandBeep && funcBeepOnError && (display != null)) {
            AlertType.INFO.playSound(display);
        }
    }

    /**
	 * Logs a message with log level LEVEL_ERROR
	 * 
	 * @param originName
	 *            The class name of the object reporting
	 * @param message
	 *            The message to log
	 * @param exception
	 *            The exception that was caused with this error
	 */
    public void error(String originName, String message, Throwable exception) {
        doLog(LogLevel.ERROR, message, exception, originName);
        if (config.showCommandBeep && funcBeepOnError && (display != null)) {
            AlertType.INFO.playSound(display);
        }
    }

    /**
	 * Logs a message with log level LEVEL_WARNING
	 * 
	 * @param origin
	 *            The object reporting
	 * @param message
	 *            The message to log
	 */
    public void warn(Object origin, String message) {
        doLog(LogLevel.WARNING, message, (Throwable) null, origin);
    }

    /**
	 * Logs a message with log level LEVEL_WARNING
	 * 
	 * @param originName
	 *            The class name of the object reporting
	 * @param message
	 *            The message to log
	 */
    public void warn(String originName, String message) {
        doLog(LogLevel.WARNING, message, (Throwable) null, originName);
    }

    /**
	 * Logs a message with log level LEVEL_INFO
	 * 
	 * @param origin
	 *            The object reporting
	 * @param message
	 *            The message to log
	 */
    public void info(Object origin, String message) {
        doLog(LogLevel.INFO, message, (Throwable) null, origin);
    }

    /**
	 * Logs a message with log level LEVEL_INFO
	 * 
	 * @param originName
	 *            The class name of the object reporting
	 * @param message
	 *            The message to log
	 */
    public void info(String originName, String message) {
        doLog(LogLevel.INFO, message, (Throwable) null, originName);
    }

    /**
	 * Logs a message with log level LEVEL_DEBUG
	 * 
	 * @param origin
	 *            The object reporting
	 * @param message
	 *            The message to log
	 */
    public void debug(Object origin, String message) {
        doLog(LogLevel.DEBUG, message, (Throwable) null, origin);
    }

    /**
	 * Logs a message with log level LEVEL_DEBUG
	 * 
	 * @param originName
	 *            The class name of the object reporting
	 * @param message
	 *            The message to log
	 */
    public void debug(String originName, String message) {
        doLog(LogLevel.DEBUG, message, (Throwable) null, originName);
    }

    public void setLogLevel(int level) {
        try {
            config.setLogLevel(level);
        } catch (Exception e) {
            error(this, "Could not set log level", e);
        }
    }

    /**
	 * Logs a message with any given log level. In order to have a nicely human
	 * readable log message this method constructs a short string of the class
	 * name of the object that is reporting.
	 * 
	 * @param level
	 *            The log level that should be applied
	 * @param message
	 *            The message to log
	 * @param exception
	 *            The exception that was caused with this error
	 * @param origin
	 *            The object reporting
	 */
    private void doLog(int level, Object message, Throwable exception, Object origin) {
        doLog(level, message, exception, StringUtil.getShortClassName(origin));
    }

    /**
	 * Logs a message with any given log level.
	 * 
	 * @param level
	 *            The log level that should be applied
	 * @param message
	 *            The message to log
	 * @param exception
	 *            The exception that was caused with this error
	 * @param originClassName
	 *            The short class name (i.e. without package indication) of the
	 *            class that logs
	 */
    private void doLog(int level, Object message, Throwable exception, String originClassName) {
        if (level <= config.getLogLevel()) {
            String logMessage = LogHelper.createLogMessage(level, message, originClassName, exception);
            if (config.logModeCanvasEnabled()) {
                logOnCanvas(logMessage);
            }
            if (config.logModePersistentEnabled()) {
                logStorage.storeLogMessage(logMessage);
            }
            if (config.logModeFileEnabled()) {
                logToFile(logMessage);
            }
            if (config.logModeRemoteEnabled()) {
            }
            if (exception != null) {
                System.out.print("Bandy LogService caught an exception! Stack trace: ");
                exception.printStackTrace();
            }
        }
    }

    /**
	 * Print a log message to the canvas.
	 * 
	 * @param logMessage
	 *            The message to show on the canvas
	 */
    protected void logOnCanvas(String logMessage) {
        if (logEntries.size() == config.maxCanvasLogEntries) {
            logEntries.removeElementAt(0);
        }
        logEntries.addElement(logMessage);
        keyScroll = false;
        repaint();
    }

    /**
	 * Print a log message to the file
	 * <code>LogConfiguration.LOG_FILE_NAME</code>
	 * 
	 * @param logMessage
	 *            The message to write to the file
	 */
    protected void logToFile(String logMessage) {
        if (!openedLogPrintStream) {
            PrintStreamOpener printStreamOpener = new PrintStreamOpener();
            printStreamOpener.start();
        }
        if (logPrintStream != null) {
            logPrintStream.println(logMessage);
        } else {
            logOnCanvas(LogHelper.createLogMessage(LogLevel.WARNING, "Can not write to file the message: " + logMessage, "Log", null));
        }
    }

    /**
	 * Print all persistently stored log messages to the canvas.
	 */
    public void displayStoredLogMessages() {
        logStorage.displayStoredLogMessagesOnCanvas();
    }

    /**
	 * Removes all log entries from canvas
	 */
    public void clearCanvas() {
        logEntries.removeAllElements();
        resetCanvas();
        info(this, "Log cleaned");
    }

    public void show(Display display, Displayable previousDisplayable) {
        this.display = display;
        this.previousDisplayable = previousDisplayable;
        display.setCurrent(this);
    }

    protected void paint(Graphics g) {
        if (config.fullScreen) {
            setFullScreenMode(true);
        }
        width = this.getWidth();
        height = this.getHeight();
        logEntriesToDisplay.removeAllElements();
        if ((!config.showCommandSelectFilter) || noFilterIsSet()) {
            CollectionsUtil.copy(logEntries, logEntriesToDisplay);
        } else if (config.showCommandSelectFilter) {
            for (int i = 0; i < logEntries.size(); i++) {
                String logMessage = (String) logEntries.elementAt(i);
                int logLevel = LogHelper.getLogLevel(logMessage);
                if (config.filterLevels[logLevel]) {
                    logEntriesToDisplay.addElement(logMessage);
                }
            }
        }
        xMax = width;
        yMax = logEntriesToDisplay.size() * logMessageFontHeight + logMessageFontHeight;
        g.setColor(255, 255, 255);
        g.fillRect(0, 0, width, height);
        if (bandyLogoBackgroundImage != null) {
            g.drawImage(bandyLogoBackgroundImage, (width / 2), (height / 2), Graphics.HCENTER | Graphics.VCENTER);
        }
        if (funcAutoScroll && (!keyScroll)) {
            if (yMax > height) {
                y0 = yMax - height;
            }
        }
        g.setFont(logMessageFont);
        g.translate(-x0, -y0);
        int y = logMessageFontHeight;
        for (int i = 0; i < logEntriesToDisplay.size(); i++) {
            String logMessage = (String) logEntriesToDisplay.elementAt(i);
            int logLevel = Integer.parseInt(logMessage.substring(0, 1));
            g.setColor(LogLevel.colors[logLevel]);
            logMessage = logMessage.substring(1);
            g.drawString(logMessage, 0, y, Graphics.BASELINE | Graphics.LEFT);
            int lineWidth = getApproximateLineWidth(logMessage, logMessageFont);
            if (xMax < lineWidth) {
                xMax = lineWidth;
            }
            y += logMessageFontHeight;
        }
        int scrollStepYFactor = yMax / SCROLL_STEP;
        int scrollStepXFactor = xMax / SCROLL_STEP;
        if (scrollStepYFactor != 0) {
            scrollStepY = yMax / scrollStepYFactor;
        }
        if (scrollStepXFactor != 0) {
            scrollStepX = xMax / scrollStepXFactor;
        }
        if (config.showScrollBars) {
            g.translate(+x0, +y0);
            g.setColor(200, 200, 200);
            g.fillRect(0, height - SCROLL_BAR_SIZE, width, SCROLL_BAR_SIZE);
            g.fillRect(width - SCROLL_BAR_SIZE, 0, SCROLL_BAR_SIZE, height);
            double scaleFactorX = (((double) width) / (xMax - SCROLL_BAR_SIZE));
            double scaleFactorY = (((double) height) / (yMax - SCROLL_BAR_SIZE));
            int scrollBarWidthX = (new Double(width * scaleFactorX)).intValue();
            int scrollBarX0 = (new Double(x0 * scaleFactorX)).intValue();
            int scrollBarHeightY = (new Double(height * scaleFactorY)).intValue();
            int scrollBarY0 = (new Double(y0 * scaleFactorY)).intValue();
            g.setColor(4478583);
            g.fillRect(scrollBarX0, height - SCROLL_BAR_SIZE, scrollBarWidthX, SCROLL_BAR_SIZE);
            g.fillRect(width - SCROLL_BAR_SIZE, scrollBarY0, SCROLL_BAR_SIZE, scrollBarHeightY);
            g.setColor(0);
            g.fillRect(width - SCROLL_BAR_SIZE, height - SCROLL_BAR_SIZE, SCROLL_BAR_SIZE, SCROLL_BAR_SIZE);
        }
        if (config.showStatusBox) {
            if (funcShowStatusBox) {
                g.setColor(230, 230, 230);
                g.fillRect(0, yStatus, width, statusBoxFontHeight + 1);
                g.setColor(4550589);
                g.setFont(statusBoxFont);
                g.drawString(statusMessage, (width / 2) - (statusMessageWidth / 2), yStatus + statusBoxFontHeight, Graphics.BOTTOM | Graphics.LEFT);
            }
        }
    }

    /**
	 * Called when a key is pressed, this method handles the scrolling
	 * 
	 * @param key
	 *            number of the pressed key
	 */
    public void keyPressed(int key) {
        int gameAction = getGameAction(key);
        switch(gameAction) {
            case RIGHT:
                if (x0 < (xMax - width)) {
                    int remainingX = (xMax + 5) - width - x0;
                    if (remainingX > scrollStepX) {
                        x0 += scrollStepX;
                    } else {
                        x0 += remainingX;
                    }
                    keyScroll = true;
                    repaint();
                }
                break;
            case LEFT:
                if (x0 >= scrollStepX) {
                    x0 -= scrollStepX;
                    keyScroll = true;
                    repaint();
                } else if (x0 > 0) {
                    x0 = 0;
                    keyScroll = true;
                    repaint();
                }
                break;
            case DOWN:
                if (y0 < (yMax - height)) {
                    int remainingY = yMax - height - y0;
                    if (remainingY > scrollStepY) {
                        y0 += scrollStepY;
                    } else {
                        y0 += remainingY;
                    }
                    keyScroll = true;
                    repaint();
                }
                break;
            case UP:
                if (y0 >= scrollStepY) {
                    y0 -= scrollStepY;
                    keyScroll = true;
                    repaint();
                } else if (y0 > 0) {
                    y0 = 0;
                    keyScroll = true;
                    repaint();
                }
                break;
            default:
        }
    }

    /**
	 * Pass the handling of the scrolling on to <code>keyPressed</code>
	 * 
	 * @param key
	 *            number of the pressed key
	 */
    protected void keyRepeated(int key) {
        keyPressed(key);
    }

    public void commandAction(Command c, Displayable d) {
        if (c == cmdBack) {
            if ((display != null) && (previousDisplayable != null)) {
                display.setCurrent(previousDisplayable);
            } else {
                if (midlet != null) {
                    midlet.notifyDestroyed();
                }
            }
        } else if (c == cmdExit) {
            if (midlet != null) {
                midlet.notifyDestroyed();
            }
        } else if (c == cmdSelectFilterLevel) {
            SelectFilterLevelForm selectForm = new SelectFilterLevelForm();
            selectForm.show(display, this);
            display.setCurrent(selectForm);
        } else if (c == cmdSelectLogLevel) {
            SelectLogLevelForm selectLogLevelForm = new SelectLogLevelForm();
            selectLogLevelForm.show(display, this);
            display.setCurrent(selectLogLevelForm);
        } else if (c == cmdSelectLogMode) {
            SelectLogModeForm selectLogModeForm = new SelectLogModeForm();
            selectLogModeForm.show(display, this);
            display.setCurrent(selectLogModeForm);
        } else if (c == cmdSelectBtDevice) {
            if (config.fullScreen) {
                setFullScreenMode(false);
            }
            if (display != null) {
                String logAsString = "";
                if (config.runningAsViewer) {
                    logAsString = getPersistentLogAsString(this);
                } else {
                    logAsString = getLogAsString();
                }
                SelectBtDeviceForm selectBtDeviceForm = new SelectBtDeviceForm(logAsString);
                selectBtDeviceForm.show(display, this);
                display.setCurrent(selectBtDeviceForm);
            } else {
                warn(this, "Display not set");
            }
        } else if (c == cmdClearCanvas) {
            clearCanvas();
        } else if (c == cmdClearLogStorage) {
            logStorage.clearStoredMessages();
            logEntries.removeAllElements();
            resetCanvas();
            Alert alert = new Alert("", "Persistent log cleared", null, AlertType.CONFIRMATION);
            alert.setTimeout(2500);
            display.setCurrent(alert, this);
        } else if (c == cmdAutoScrollOn) {
            funcAutoScroll = true;
            removeCommand(cmdAutoScrollOn);
            addCommand(cmdAutoScrollOff);
        } else if (c == cmdAutoScrollOff) {
            funcAutoScroll = false;
            removeCommand(cmdAutoScrollOff);
            addCommand(cmdAutoScrollOn);
        } else if (c == cmdBeepOnErrorOn) {
            funcBeepOnError = true;
            removeCommand(cmdBeepOnErrorOn);
            addCommand(cmdBeepOnErrorOff);
        } else if (c == cmdBeepOnErrorOff) {
            funcBeepOnError = false;
            removeCommand(cmdBeepOnErrorOff);
            addCommand(cmdBeepOnErrorOn);
        } else if (c == cmdShowStatus) {
            setStatusMessage();
            yStatus = -statusBoxFontHeight;
            removeCommand(cmdShowStatus);
            addCommand(cmdHideStatus);
            funcShowStatusBox = true;
            animateStatusBox = new Thread(this);
            animateStatusBox.start();
        } else if (c == cmdHideStatus) {
            removeCommand(cmdHideStatus);
            addCommand(cmdShowStatus);
            animateStatusBox = new Thread(this);
            animateStatusBox.start();
        }
    }

    public void run() {
        if (animateStatusBox != null) {
            Thread me = Thread.currentThread();
            if (yStatus < 0) {
                while ((me == animateStatusBox) && (yStatus < 0)) {
                    synchronized (this) {
                        try {
                            this.wait(10);
                            yStatus += 1;
                            keyScroll = true;
                            repaint();
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }
                }
            } else {
                while ((me == animateStatusBox) && (yStatus > (-statusBoxFontHeight))) {
                    synchronized (this) {
                        try {
                            this.wait(10);
                            yStatus -= 1;
                            keyScroll = true;
                            repaint();
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }
                }
                funcShowStatusBox = false;
            }
        }
        animateStatusBox = null;
    }

    protected String getLogAsString() {
        String logAsString = "";
        for (int i = 0; i < logEntries.size(); i++) {
            String logMessage = (String) logEntries.elementAt(i);
            int logLevel = Integer.parseInt(logMessage.substring(0, 1));
            logAsString = logAsString + LogLevel.levels[logLevel] + " " + logMessage.substring(1) + "\n";
        }
        return logAsString;
    }

    public String getPersistentLogAsString(Displayable currentDisplayable) {
        return logStorage.getPersistenLogAsString(display, currentDisplayable);
    }

    protected void resetCanvas() {
        x0 = 0;
        y0 = 0;
        if (config.showStatusBox && funcShowStatusBox) {
            setStatusMessage();
        }
    }

    private void setStatusMessage() {
        statusMessage = "Log: " + LogLevel.levels[config.getLogLevel()];
        if (config.showCommandSelectFilter) {
            statusMessage = statusMessage + " Filter: ";
            if (noFilterIsSet()) {
                statusMessage = statusMessage + "No Filter";
            } else {
                boolean filterSet = false;
                for (int i = 0; i < config.filterLevels.length; i++) {
                    if (config.filterLevels[i]) {
                        if (filterSet) {
                            statusMessage = statusMessage + "|";
                        }
                        statusMessage = statusMessage + LogLevel.levels[i].trim();
                        filterSet = true;
                    }
                }
            }
        }
        statusMessageWidth = getExactLineWidth(statusMessage, statusBoxFont);
    }

    private boolean noFilterIsSet() {
        for (int i = 0; i < config.filterLevels.length; i++) {
            if (!config.filterLevels[i]) {
                return false;
            }
        }
        return true;
    }

    private static int getExactLineWidth(String string, Font font) {
        char[] data = new char[string.length()];
        string.getChars(0, string.length(), data, 0);
        int lineWidth = 0;
        char ch;
        for (int ccnt = 0; ccnt < data.length; ccnt++) {
            ch = data[ccnt];
            lineWidth = lineWidth + font.charWidth(ch);
        }
        return lineWidth;
    }

    private static int getApproximateLineWidth(String string, Font font) {
        return string.length() * logMessageFontWidthPerChar;
    }

    private class PrintStreamOpener extends Thread {

        public void run() {
            openedLogPrintStream = true;
            String logFileUrl = "";
            if (Service.runningOnSunWTK()) {
                logFileUrl = "file:///root1/" + LogConfiguration.LOG_FILE_NAME;
            } else {
                logFileUrl = "file:///Memory card/Others/" + LogConfiguration.LOG_FILE_NAME;
            }
            try {
                FileConnection fc = (FileConnection) Connector.open(logFileUrl);
                if (!fc.exists()) {
                    fc.create();
                }
                logPrintStream = new PrintStream(fc.openOutputStream(fc.fileSize()));
            } catch (IOException ioe) {
            }
        }
    }
}
