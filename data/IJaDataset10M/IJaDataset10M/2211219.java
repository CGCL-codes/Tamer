package iono2;

import java.sql.*;
import java.util.*;
import java.io.*;
import dbaccess.util2.*;
import dbaccess.iono2.*;
import org.apache.commons.logging.LogFactory;
import org.apache.commons.logging.Log;

/**
 * Sample program using the ionospheric database access classes to retrieve
 * data.  This program collects parameters (currently hardcoded) for the
 * station, date range, and a list of ursi codes.  It then retrieves the
 * data for these parameters from the database and prints pieces of the
 * resulting data array.
 * <p>
 * This sample program accepts these properties:
 * <p>
 * <ul>
 * <li>iono.stn
 * <li>iono.ursi
 * <li>iono.yr
 * <li>iono.mo
 * <li>iono.day
 * </ul>
 * 
 * NOTE: Properties are only used in part of the program.  Most things are 
 * still hard-coded.
 */
class ionoPrint {

    private static Log logger = LogFactory.getLog(ionoPrint.class);

    private static final String PROGNAME = "ionoPrint";

    private static final String PROGDESC = "Print data for a station/ursi/day";

    private static final String PREFIX = "Iono";

    private static final String DATABASE = "ionodb_blank";

    private static final String DEFAULT_STN = "AS237";

    private static final String DEFAULT_URSI = "00";

    private static final String DEFAULT_YR = "2003";

    private static final String DEFAULT_MO = "01";

    private static final String DEFAULT_DAY = "01";

    public ionoPrint() {
    }

    public void run(String[] args) {
        DBProperties prop = new DBProperties(PROGNAME, PROGDESC);
        prop.setProperty("propPrefix", PREFIX);
        prop.setProperty("database", DATABASE);
        if (!prop.getProperties(args)) {
            prop.usage();
            return;
        }
        Logging log = new Logging(prop);
        DBConnect c = new DBConnect(prop, "Iono");
        if (!c.connect()) logger.fatal("Database connection failed.");
        String stn = prop.getProperty("stn", DEFAULT_STN);
        Station s = new Station(c);
        if (s.get(stn)) logger.info("Station: " + stn + " " + s.getName()); else logger.fatal("Invalid station: " + stn);
        String ursi = prop.getProperty("ursi", DEFAULT_URSI);
        logger.debug("Ursi: " + ursi);
        if (ursi != null && ursi != DEFAULT_URSI) {
            Ursi u = new Ursi(c);
            u.get(ursi);
            if (u.get(ursi)) {
                logger.info("Ursi characteristic: " + ursi + " " + u.getName());
            } else {
                logger.fatal("Invalid ursi characteristic code: " + ursi);
                return;
            }
        } else {
            logger.info("Ursi characteristic: " + DEFAULT_URSI);
        }
        int yr = Integer.parseInt(prop.getProperty("yr", DEFAULT_YR));
        if (yr < 1920 || yr > 2006) {
            logger.fatal("Invalid year: " + yr);
            return;
        }
        int mo = Integer.parseInt(prop.getProperty("mo", DEFAULT_MO));
        if (mo < 1 || mo > 12) {
            logger.fatal("Invalid month: " + mo);
            return;
        }
        int dy = Integer.parseInt(prop.getProperty("day", DEFAULT_DAY));
        if (dy < 1 || dy > 31) {
            logger.fatal("Invalid day of month: " + dy);
            return;
        }
        DateTime dt = new DateTime(yr, mo, dy);
        logger.info("Date: " + dt.toDateString());
        logger.debug("Get data for station=" + stn + " ursi=" + ursi + " date=" + dt);
        if (ursi.equals(DEFAULT_URSI)) {
            try {
                IonoRowList rowlist = new IonoRowList(c);
                rowlist.get(stn, yr, mo, dy);
                rowlist.printlong();
            } catch (Exception e) {
                logger.error(e);
            }
        } else {
            IonoRow row = new IonoRow(c);
            row.get(stn, ursi, dt);
            row.print();
        }
        c.close();
    }

    public static void main(String[] args) {
        ionoPrint p = new ionoPrint();
        p.run(args);
    }
}
