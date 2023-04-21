package uk.co.marcoratto.j2me.moonphase;

import java.io.IOException;
import java.util.Calendar;
import java.util.TimeZone;
import javax.microedition.midlet.*;
import javax.microedition.lcdui.*;
import uk.co.marcoratto.j2me.i18n.I18N;
import uk.co.marcoratto.j2me.info.Info;
import uk.co.marcoratto.j2me.log.Logger;
import uk.co.marcoratto.j2me.util.MyMath;
import uk.co.marcoratto.j2me.about.About;

public class MoonPhaseMIDlet extends MIDlet implements CommandListener {

    private static Logger log;

    private Display display;

    private Displayable currentDisplayable;

    private final Command aboutCommand;

    private final Command infoCommand;

    private final Command logCommand;

    private final Command yearCommand;

    private final Command exitCommand;

    private final List list;

    private Image[] images = new Image[4];

    String versStr;

    MoonPhase myMoonPhase;

    String Str[];

    double jde[];

    int locOffset;

    Calendar dat;

    double currentJD;

    double currentTime;

    double T[];

    double phase;

    public MoonPhaseMIDlet() {
        super();
        log = Logger.getLogger(this);
        this.display = Display.getDisplay(this);
        yearCommand = new Command("Year", Command.BACK, 1);
        logCommand = new Command("Log", Command.BACK, 2);
        exitCommand = new Command("Exit", Command.EXIT, 3);
        aboutCommand = new Command("About", Command.BACK, 4);
        infoCommand = new Command("Info", Command.BACK, 5);
        list = new List("Years 2007-2008", Choice.IMPLICIT);
        try {
            images[0] = Image.createImage("/images/moon_new.png");
            images[1] = Image.createImage("/images/moon_first_quarter.png");
            images[2] = Image.createImage("/images/moon_full.png");
            images[3] = Image.createImage("/images/moon_last_quarter.png");
        } catch (IOException e) {
            log.warn(e);
        }
        list.setTicker(new Ticker(I18N.getInstance().translate("appl.title")));
        list.addCommand(exitCommand);
        list.addCommand(yearCommand);
        list.addCommand(logCommand);
        list.addCommand(aboutCommand);
        list.addCommand(infoCommand);
        list.setCommandListener(this);
        this.calculate(Calendar.getInstance().get(Calendar.YEAR));
        currentDisplayable = list;
        log.trace("end");
    }

    /**
	 * Called when this MIDlet is started for the first time, or when it returns
	 * from paused mode. If a player is visible, and it was playing when the
	 * MIDlet was paused, call its playSound method.
	 */
    public void startApp() {
        display.setCurrent(currentDisplayable);
    }

    public void commandAction(Command c, Displayable s) {
        if (c == exitCommand) {
            destroyApp(true);
            notifyDestroyed();
        } else if (c == yearCommand) {
            YearForm.getInstance().show(this);
            if (YearForm.getInstance().getDate() == null) {
                return;
            }
            list.deleteAll();
            this.calculate(YearForm.getInstance().getDate().get(Calendar.YEAR));
        } else if (c == logCommand) {
            log.show(this);
        } else if (c == infoCommand) {
            Info.getInstance().show(this);
        } else if (c == aboutCommand) {
            About.getInstance().show(this);
        } else {
            log.debug("List=" + (this.list.getSelectedIndex() % 4));
            PictureForm.getInstance().show(this, images[(this.list.getSelectedIndex() % 4)], this.list.getString(this.list.getSelectedIndex()));
        }
    }

    public void destroyApp(boolean b) {
        display.setCurrent(null);
        notifyDestroyed();
    }

    /**
	 * Called when this MIDlet is paused. If the player GUI is visible, call its
	 * pauseSound method. For consistency across different VM's it's a good idea
	 * to stop the player if it's currently playing.
	 */
    public void pauseApp() {
    }

    public void calculate(int year) {
        log.debug("Year=" + year);
        T = new double[70];
        Calendar theDate = Calendar.getInstance();
        theDate.set(Calendar.DAY_OF_MONTH, 1);
        theDate.set(Calendar.MONTH, 0);
        int offset1 = -((TimeZone.getDefault().getOffset(1, theDate.get(Calendar.YEAR), theDate.get(Calendar.MONTH), theDate.get(Calendar.DAY_OF_MONTH), theDate.get(Calendar.DAY_OF_WEEK), theDate.get(Calendar.MILLISECOND)) + TimeZone.getDefault().getRawOffset()) / (60 * 1000)) / 60;
        theDate.set(Calendar.DAY_OF_MONTH, 1);
        theDate.set(Calendar.MONTH, 7);
        int offset2 = -((TimeZone.getDefault().getOffset(1, theDate.get(Calendar.YEAR), theDate.get(Calendar.MONTH), theDate.get(Calendar.DAY_OF_MONTH), theDate.get(Calendar.DAY_OF_WEEK), theDate.get(Calendar.MILLISECOND)) + TimeZone.getDefault().getRawOffset()) / (60 * 1000)) / 60;
        if (offset2 > offset1) {
            locOffset = offset1;
        } else {
            locOffset = offset2;
        }
        dat = Calendar.getInstance();
        dat.set(Calendar.YEAR, year - 1900);
        currentTime = (double) dat.get(Calendar.HOUR_OF_DAY) + (double) dat.get(Calendar.MINUTE) / 60D + (double) dat.get(Calendar.SECOND) / 3600D;
        currentJD = JD(dat.get(Calendar.DAY_OF_MONTH), dat.get(Calendar.MONTH) + 1, year, currentTime);
        int j = 0;
        double YEAR = (double) year - 0.1D;
        int kStart = (int) MyMath.round((YEAR - 2000D) * 12.3685D);
        for (int i = 0; i < 15; i++) {
            myMoonPhase = new MoonPhase(locOffset, false, kStart + i);
            list.append(myMoonPhase.newMoonStr(), null);
            list.append(myMoonPhase.firstQuarterStr(), null);
            list.append(myMoonPhase.fullMoonStr(), null);
            list.append(myMoonPhase.lastQuarterStr(), null);
            j += 4;
        }
    }

    public double JD(int date, int month, int year, double STD) {
        double A = 10000D * (double) year + 100D * (double) month + (double) date;
        if (month <= 2) {
            month += 12;
            year--;
        }
        double B = (year / 400 - year / 100) + year / 4;
        A = 365D * (double) year - 679004D;
        double MJD = A + B + (double) (int) (30.6D * (double) (month + 1)) + (double) date + STD / 24D;
        return MJD + 2400000D;
    }
}
