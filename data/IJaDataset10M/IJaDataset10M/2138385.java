package edu.whitman.halfway.jigs.cmdline;

import edu.whitman.halfway.jigs.*;
import org.apache.log4j.Logger;
import java.util.Calendar;
import java.util.Date;

public class DateAdjuster extends AbstractCmdLine implements MediaItemFunction {

    private static Logger log = Logger.getLogger(DateAdjuster.class);

    static int[] opts = { LIST_ALBUM, LOG4J_FILE, HELP, VERBOSE, RECURSE_ALBUM, RECURSIVE };

    int hours = 0, days = 0, mins = 0;

    int years = 0, months = 0;

    public DateAdjuster() {
        super(opts);
    }

    public String getAdditionalParseArgs() {
        return "H:M:D:Y:N:";
    }

    public static void main(String[] args) {
        (new DateAdjuster()).mainDriver(args);
    }

    public void doMain() {
        if (hasOption('H')) hours = getInt(getOptionString('H'), "Invalid Hour: ");
        if (hasOption('M')) mins = getInt(getOptionString('M'), "Invalid Minute: ");
        if (hasOption('D')) days = getInt(getOptionString('D'), "Invalid Days: ");
        if (hasOption('Y')) years = getInt(getOptionString('Y'), "Invalid Years: ");
        if (hasOption('N')) months = getInt(getOptionString('N'), "Invalid Months: ");
        AlbumUtil.mediaItemWalk(getAlbum(), this, recurse);
    }

    public void process(MediaItem pic) {
        AlbumObjectDescriptionInfo di = pic.getDescriptionInfo();
        Date crntDate = (Date) di.getData(AlbumObjectDescriptionInfo.JIGS_DATE);
        if (verbose) {
            System.out.println("Picture: " + pic);
            System.out.println("\told Date " + crntDate);
        }
        if (crntDate == null) {
            if (verbose) System.out.println("Date is null for picture: " + pic);
        } else {
            Calendar cal = Calendar.getInstance();
            cal.setTime(crntDate);
            cal.add(cal.YEAR, years);
            cal.add(cal.MONTH, months);
            cal.add(cal.DAY_OF_YEAR, days);
            cal.add(cal.HOUR_OF_DAY, hours);
            cal.add(cal.MINUTE, mins);
            crntDate = cal.getTime();
        }
        if (verbose) System.out.println("\tnew Date = " + crntDate);
        di.setData(AlbumObjectDescriptionInfo.JIGS_DATE, crntDate);
        di.saveFile();
    }

    private int getInt(String arg, String errorMsg) {
        try {
            return Integer.parseInt(arg);
        } catch (NumberFormatException e) {
            System.out.println(errorMsg + " " + arg);
            usage();
        }
        return -1;
    }

    protected void specificUsage() {
        System.out.println(" usage:  DateAdjuster [options] [filelist]");
        System.out.println(" -D,H,M how many days, hours, or minutes to increment.  Can be negative.");
        System.out.println(" -Y, N how many years or months to increment.  Can be negative.");
    }
}
