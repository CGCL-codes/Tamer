package dbaccess.iono2;

import java.sql.*;
import java.util.*;
import java.io.*;
import dbaccess.util2.*;
import org.apache.commons.logging.LogFactory;
import org.apache.commons.logging.Log;

/**
 * Helper class to load data into the ionospheric database.  This class relies
 * on several others to perform necessary database operations.  Other important
 * classes are:
 * <ul>
 * <li>IonoObs - this defines an object that represents a single observation.
 * <li>IonoObsList - This is a list of <i>IonoObs</i> objects.  This will be
 * converted into a blob when inserted into the database and vica versa.
 * <li>IonoRow - A single row from the database is represented by objects 
 * defined by this class.  A row will consist of the station, date, ursi
 * characteristic code, the data in an <i>IonoObsList</i> object and other
 * data elements.
 * </ul>
 * <p>
 * The <i>IonoRow</i> class contains the methods that will actually retrieve
 * from the database and write to the database.  However, <i>IonoRow</i>
 * cannot alter the contents of an existing <i>IonoObsList</i> object based 
 * on data being loaded (or removed or updated) from an input file.
 * <p>
 * The methods to modify alter the contents of <i>IonoObsList</i> are included
 * in this class, <i>IonoLoad</i>.  This is achieved by comparing the
 * <i>IonoObsList</i> object that exists in the database with another
 * <i>IonoObsList</i> object that was created from the data in the input file.
 * These methods create the resulting version of the <i>IonoObsList</i> that
 * will be written back to the database with methods in <i>IonoRow</i>:
 * <ul>
 * <li>del() - delete observations from the database <i>IonoObsList</i> object that match to observations in the <i>IonoObsList</i> object from the input file.
 * <li>upd() - update observations from the database <i>IonoObsList</i> object that match observations in the <i>IonoObsList</i> object from the input file.
 * <li>put() - insert observations into the database <i>IonoObsList</i> object from observations in the <i>IonoObsList</i> object from the input file that don't already exist.
 * </ul>
 */
public class IonoLoad {

    boolean ErrorCheckOnly;

    protected LoadProperties prop;

    protected BufferedReader in;

    protected DBConnect c;

    protected Statement stmt;

    protected int verboseLevel;

    protected boolean Verbose;

    protected boolean Debug = false;

    protected boolean Delete = false;

    protected boolean Insert = false;

    protected boolean Update = false;

    protected boolean UpdateInventory = true;

    protected Ionosonde iscon;

    protected Station stncon;

    protected IonoInventory invcon;

    protected byte replaceOpt;

    protected int variance;

    protected String field;

    protected IonoRow row;

    protected String stn;

    protected int year;

    protected int month;

    protected int day;

    protected String ursi;

    protected String release;

    protected DateTime releaseDate;

    protected byte ionosondeID;

    protected float scaling;

    protected int offset;

    protected IonoObsList obsList;

    protected int Nread;

    protected int Ninsert_ionosonde;

    protected int Ninsert_stn;

    protected int Ninsert_udd;

    protected int Ninsert_data;

    protected int Ninsert_elem;

    protected int Nupdate_stn;

    protected int Nupdate_data;

    protected int Nupdate_inv;

    protected int Nupdate_elem;

    protected int Ndelete_data;

    protected int Ndelete_elem;

    protected int Nskip_elem;

    protected int Nwarn;

    protected int Nerror;

    static final float SCALING_FACTOR = 1.000f;

    static final int OFFSET = 0;

    static final int MaxMeasPerDay = 96;

    private static Log logger = LogFactory.getLog(IonoLoad.class);

    /**
   * Constructor
   */
    public IonoLoad() {
        replaceOpt = -1;
        variance = 0;
        release = "R";
        releaseDate = null;
        scaling = SCALING_FACTOR;
        offset = OFFSET;
        stn = "";
        obsList = new IonoObsList(MaxMeasPerDay);
    }

    /**
   * Constructor
   * @param con Database JDBC connection
   */
    public IonoLoad(DBConnect con) {
        this.c = con;
        replaceOpt = -1;
        variance = 0;
        release = "R";
        releaseDate = null;
        scaling = SCALING_FACTOR;
        offset = OFFSET;
        stn = "";
        obsList = new IonoObsList(MaxMeasPerDay);
    }

    /**
    * Get replace option flag.
    */
    public byte getReplaceOpt() {
        return replaceOpt;
    }

    /**
    * Should we delete data?
    * @return true if should delete; false if should not.
    */
    public boolean Delete() {
        return Delete;
    }

    /**
    * Should we insert data?
    * @return true if should insert; false if should not.
    */
    public boolean Insert() {
        return Insert;
    }

    /**
    * Should we update data?
    * @return true if should update; false if should not.
    */
    public boolean Update() {
        return Update;
    }

    /**
    * See if inventory should be updated.
    * @return True if inventory should be updated; false otherwise
    */
    public boolean UpdateInventory() {
        return UpdateInventory;
    }

    /**
    * Set database JDBC connection.
    * @param con database connection.
    */
    public void setConnection(DBConnect con) {
        this.c = con;
    }

    /**
    * Set station.
    * @param s station.
    */
    public void setStn(String s) {
        this.stn = s;
    }

    /**
    * Set year.
    * @param y year.
    */
    public void setYear(int y) {
        this.year = y;
    }

    /**
    * Set month.
    * @param m month.
    */
    public void setMonth(int m) {
        this.month = m;
    }

    /**
    * Set day.
    * @param d day.
    */
    public void setDay(int d) {
        this.day = d;
    }

    /**
    * Set ursi characteristic code.
    * @param u characteristic code.
    */
    public void setUrsi(String u) {
        this.ursi = u;
    }

    /**
    * Set release.
    * @param r release
    */
    public void setRelease(String r) {
        this.release = r;
    }

    /**
    * Set release date.
    * @param r release date
    */
    public void setReleaseDate(DateTime r) {
        this.releaseDate = r;
    }

    /**
    * Set ionosonde ID.
    * @param id ionosonde ID
    */
    public void setIonosondeID(byte id) {
        this.ionosondeID = id;
    }

    /**
    * Set ionosonde ID when given the ionosonde description.
    * The database stores an ionosonde ID, not the description itself.  Use
    * the ionosondes table which contains IDs and descriptions to figure out
    * the correct ionosonde ID.
    * @param ionosonde ionosonde
    */
    public void setIonosondeID(String ionosonde) {
        ionosondeID = iscon.getId(ionosonde);
        if (ionosondeID < 0) {
            if (iscon.put(iscon.getNextId(), ionosonde)) {
                ionosondeID = iscon.getId(ionosonde);
                Ninsert_ionosonde++;
            }
        }
    }

    /**
    * Set variance in seconds.
    * @param s Seconds.
    */
    public void setVariance(int s) {
        this.variance = s;
    }

    /**
    * Set scaling factor.
    * @param factor Scaling factor
    */
    public void setScaling(float factor) {
        this.scaling = factor;
    }

    /**
    * Set offset amount.
    * @param amt Offset amount
    */
    public void setOffset(int amt) {
        this.offset = amt;
    }

    /**
    * Set list of observation data.
    * @param ol List of observation values
    */
    public void setObsList(IonoObsList ol) {
        this.obsList = ol;
    }

    /**
    * Set data with a IonoRow object.
    * @param ionorow IonoRow object
    */
    public void setRow(IonoRow ionorow) {
        setStn(ionorow.getStn());
        DateTime obsdate = ionorow.getObsdate();
        setYear(obsdate.get(Calendar.YEAR));
        setMonth((obsdate.get(Calendar.MONTH)) + 1);
        setDay(obsdate.get(Calendar.DAY_OF_MONTH));
        setUrsi(ionorow.getUrsi());
        setRelease(ionorow.getRelease());
        setReleaseDate(ionorow.getReleaseDate());
        setIonosondeID(ionorow.getIonosondeID());
        setScaling(ionorow.getScaling());
        setOffset(ionorow.getOffset());
        setObsList(ionorow.getObsList());
    }

    /**
   * Set the release code and date.  User access to data in the database
   * is controlled by the release status code which can be set as either:
   * <ul>
   * <li><b>R</b> : Released (users can access the data)
   * <li><b>P</b> : Pre-released (users cannot access the data until the
   *                status is manually changed to "released".
   * <li><b>X</b> : Restricted (users cannot access the data).  This data is
   *                not meant for users to use - only the data administrators.
   * </ul>
   * <p>
   * Released data can be requested 2 different ways from the command line:
   *    --release : Data is released immediately
   *    --release=XX : Data is automatically released after XX days
   * The release status can also be set as:
   *    --prerelease  : Data is not released.  It must be manually released
   *                    at a future date.
   *    --restricted  : Data is not available ever for general use
   * <p>
   * If none of the release flags are used the release status for the data
   * is set to "released" immediately.
   * @param prop List of properties from the command line or config file.
   */
    public void setRelease(LoadProperties prop) {
        String rel = prop.getProperty("release", "false");
        if (rel.equals("true")) {
            this.release = "R";
            this.releaseDate = new DateTime();
            if (Verbose) logger.info("This data will be loaded as released now.");
        } else if (!rel.equals("false")) {
            int rdays = Integer.parseInt(rel);
            this.release = "R";
            this.releaseDate = new DateTime();
            this.releaseDate.add(Calendar.DATE, rdays);
            logger.info("This data will be loaded now but not released until " + releaseDate);
        } else if (rel.equals("false")) {
            if (prop.getProperty("prerelease", "false").equals("true")) {
                this.release = "P";
                logger.info("This data will be loaded as pre-release.");
            } else if (prop.getProperty("restricted", "false").equals("true")) {
                this.release = "X";
                logger.info("This data will be loaded as restricted.");
            } else {
                this.release = "R";
                this.releaseDate = new DateTime();
            }
        }
    }

    /**
   * Set the replace option and time variance.  This option specifies which
   * data (depending on data source) can be overridden if it already exists.
   * The variance specifies the number of seconds +/- from the existing 
   * observation time that is considered a match.  For example, if these
   * observations are already in the databases:
   * <p>
   * <pre>
   * Obstime  Value QD Source
   * 14:30:02  5.20 // IUWDS
   * 14:45:02  5.40 // IIWG
   * 15:00:52  5.50 // IIWG
   * </pre>
   * <p>
   * and you are loading this data:
   * <pre>
   * Obstime  Value QD Source
   * 14:30:00  5.30  S IIWG
   * 14:45:00  5.30 /T IIWG
   * 15:00:00  5.60 I/ IIWG
   * </pre>
   * <p>
   * and you load with these options:
   * <pre>
   * --replaceIIWG --variance=30
   * </pre>
   * <p>
   * The database will look like this when the load is complete:
   * <pre>
   * Obstime  Value QD Source
   * 14:30:02  5.20 // IUWDS
   * 14:45:00  5.30 /T IIWG
   * 15:00:52  5.50 // IIWG
   * </pre>
   * <p>
   * <ul>
   * <li>The observation at 14:30:00 did not replace the one at 14:30:02
   * because it did not have an IIWG data source.
   * <li>The observation at 14:45:00 replaced the one at 14:45:02 because
   * it had an IIWG data source and was within 30 seconds of 14:45:00.
   * <li>The observation at 15:00:00 did not replace the one at 15:00:52
   * because it was not within 30 seconds of 15:00:00.
   * </ul>
   * <p>
   * But if you load with these options:
   * <pre>
   * --replaceAll --variance=60
   * </pre>
   * <p>
   * The database will look like this when the load is complete:
   * <pre>
   * Obstime  Value QD Source
   * 14:30:00  5.30  S IIWG
   * 14:45:00  5.30 /T IIWG
   * 15:00:00  5.60 I/ IIWG
   * </pre>
   * <p>
   * <ul>
   * <li>The observation at 14:30:00 replaces the one at 14:30:02 because
   * it was within 60 seconds of 14:30:00 and --replaceAll was specified.
   * <li>The observation at 14:45:00 replaced the one at 14:45:02 because
   * it was within 60 seconds of 14:45:00 and --replaceAll was specified.
   * <li>The observation at 15:00:00 replaced the one at 15:00:52 because
   * it was within 60 seconds of 15:00:00 and --replaceAll was specified.
   * </ul>
   * <p>
   * @param prop List of properties from the command line or config file.
   */
    public void setReplaceOpt(LoadProperties prop) {
        if (prop.getProperty("replaceAll", "false").equals("true")) {
            this.replaceOpt = 9;
        } else if (prop.getProperty("replaceIIWG", "false").equals("true")) {
            this.replaceOpt = 0;
        } else if (prop.getProperty("replaceSAO", "false").equals("true")) {
            this.replaceOpt = 1;
        } else if (prop.getProperty("replaceIUWDS", "false").equals("true")) {
            this.replaceOpt = 2;
        } else if (prop.getProperty("replaceCDMP", "false").equals("true")) {
            this.replaceOpt = 3;
        } else if (prop.getProperty("replaceFSU", "false").equals("true")) {
            this.replaceOpt = 4;
        } else if (prop.getProperty("replaceIONKA", "false").equals("true")) {
            this.replaceOpt = 5;
        } else if (prop.getProperty("replaceURSI", "false").equals("true")) {
            this.replaceOpt = 6;
        } else if (prop.getProperty("replaceIPS", "false").equals("true")) {
            this.replaceOpt = 7;
        }
        this.variance = Integer.parseInt(prop.getProperty("variance", "0"));
        logger.debug("replaceOpt=" + replaceOpt + "; variance=" + variance);
    }

    /**
   * Initialize:
   * <ol>
   * <li> Get command line options
   * <li> Open the input file
   * <li> Establish the database connection via JDBC
   * <li> Initialize some global variables
   * </ol>
   * @param prop List of properties from the command line or config file.
   */
    public void initialize(LoadProperties prop) {
        setRelease(prop);
        setReplaceOpt(prop);
        stmt = c.getStatement();
        Verbose = prop.verbose();
        Delete = prop.delete();
        Insert = prop.insert();
        Update = prop.update();
        ErrorCheckOnly = prop.errorCheck();
        UpdateInventory = !prop.Noinv();
        if (getReplaceOpt() >= 0) {
            Delete = true;
            Insert = true;
        }
        iscon = new Ionosonde(c);
        stncon = new Station(c);
        invcon = new IonoInventory(c);
        row = new IonoRow(c);
        if (variance >= 0) {
            obsList.setVariance(variance);
        }
    }

    /**
   * Print the current record retrieved
   */
    public void printHeader() {
        System.out.print("Station=" + stn + "  Ursi=" + ursi);
        System.out.println("  Date=" + month + "/" + day + "/" + year);
        System.out.print("  Ionosonde=" + ionosondeID);
        System.out.print("  Release=" + release + "  Release date=" + releaseDate);
        System.out.print("  Scaling=" + scaling + "  Offset=" + offset);
        System.out.flush();
    }

    /**
   * Check to see if data exists for a station/ursi/date for a particular
   * data source (ie: IIWG,SAO,IUWDS,CDMP,FSU,IONKA,URSI,IPS).  This method
   * does not affect the current IonoRow object, even if it's for the same
   * station, ursi characteristic code, and date.
   * @param stn station id
   * @param ursiCode ursi code
   * @param y year of date
   * @param m month of date
   * @param d day of date
   * @return True if data exists; false otherwise.
   */
    public boolean dataExists(String stn, String ursiCode, int y, int m, int d) {
        IonoRow row = new IonoRow(this.c);
        row.setStn(stn);
        row.setUrsi(ursiCode);
        row.setObsdate(new DateTime(y, m, d));
        boolean exist = row.get();
        row = null;
        return exist;
    }

    /**
   * Delete data from the ionospheric data table.
   * @return True if successful delete; false if EOF or error.
   * @see IonoLoad#put
   * @see IonoLoad#upd
   */
    public boolean del() {
        if (Verbose) logger.debug("Delete data...");
        row.init();
        row.setStn(stn);
        row.setUrsi(ursi);
        row.setObsdate(new DateTime(year, month, day));
        row.setRelease(release);
        row.setReleaseDate(releaseDate);
        if (!row.get()) return false;
        IonoObsList obslistdb = row.getObsList();
        obslistdb.setReplaceSource(replaceOpt);
        if (variance >= 0) {
            obslistdb.setVariance(variance);
        }
        Ndelete_elem += obslistdb.remove(obsList);
        Nskip_elem += obslistdb.getNskip();
        row.setObsList(obslistdb);
        try {
            if (!ErrorCheckOnly) {
                if (obslistdb.size() > 0) {
                    Nupdate_data += row.update();
                } else {
                    Ndelete_data += row.delete();
                }
            }
        } catch (Exception e) {
            logger.error(e.getMessage());
            Nerror++;
        }
        return true;
    }

    /**
   * Put data to the data table.
   * @return True if successful; false if EOF or error.
   * @see IonoLoad#del
   * @see IonoLoad#upd
   */
    public boolean put() {
        if (Verbose) logger.debug("Insert data...");
        row.init();
        row.setStn(stn);
        row.setUrsi(ursi);
        row.setObsdate(new DateTime(year, month, day));
        row.setRelease(release);
        row.setReleaseDate(releaseDate);
        row.setIonosondeID(ionosondeID);
        row.setScaling(scaling);
        row.setOffset(offset);
        boolean rowExists = row.get();
        try {
            if (rowExists) {
                if (ursi.equals("00")) logger.debug("Row exists for " + stn + "(" + ursi + ") " + month + "/" + day + "/" + year + " +- " + variance);
                IonoObsList obslistdb = row.getObsList();
                obslistdb.setReplaceSource(replaceOpt);
                if (variance >= 0) {
                    obslistdb.setVariance(variance);
                }
                int num = obslistdb.insertReplace(obsList);
                Ninsert_elem += obslistdb.getNinsert();
                Nupdate_elem += obslistdb.getNreplace();
                Nskip_elem += obslistdb.getNskip();
                row.setObsList(obslistdb);
                if (!ErrorCheckOnly && num > 0) {
                    Nupdate_data += row.update();
                }
            } else {
                if (ursi.equals("00")) logger.debug("Row does not exist for " + stn + "(" + ursi + ") " + month + "/" + day + "/" + year + " +- " + variance);
                IonoObsList obslistdb = new IonoObsList(obsList.size());
                obslistdb.setReplaceSource(replaceOpt);
                if (variance >= 0) {
                    obsList.setVariance(variance);
                }
                int num = obslistdb.insertReplace(obsList);
                Ninsert_elem += obslistdb.getNinsert();
                Nupdate_elem += obslistdb.getNreplace();
                Nskip_elem += obslistdb.getNskip();
                row.setObsList(obslistdb);
                if (!ErrorCheckOnly && num > 0) {
                    Ninsert_data += row.insert();
                }
            }
        } catch (Exception e) {
            logger.error("Failed inserting data: " + e.getMessage());
            Nerror++;
        }
        return true;
    }

    /**
   * Update data in the data table.
   * @return True if successful; false if EOF or error.
   * @see IonoLoad#del
   * @see IonoLoad#put
   */
    public boolean upd() {
        if (Verbose) logger.debug("Update data...");
        row.init();
        row.setStn(stn);
        row.setUrsi(ursi);
        row.setObsdate(new DateTime(year, month, day));
        boolean rowExists = row.get();
        if (!rowExists) logger.debug("Row does not exist for " + stn + "(" + ursi + ") " + month + "/" + day + "/" + year + " +- " + variance + " when attempting an update.");
        row.setRelease(release);
        row.setReleaseDate(releaseDate);
        row.setIonosondeID(ionosondeID);
        row.setScaling(scaling);
        row.setOffset(offset);
        IonoObsList obslistdb = row.getObsList();
        obslistdb.setReplaceSource(replaceOpt);
        if (variance >= 0) {
            obslistdb.setVariance(variance);
        }
        int num = obslistdb.insertReplace(obsList);
        Ninsert_elem += obslistdb.getNinsert();
        Nupdate_elem += obslistdb.getNreplace();
        Nskip_elem += obslistdb.getNskip();
        row.setObsList(obslistdb);
        try {
            if (!ErrorCheckOnly && num > 0) {
                Nupdate_data += row.update();
            }
        } catch (Exception e) {
            logger.error(e.getMessage());
            Nerror++;
        }
        return true;
    }

    /**
   * Update inventory for a station, year and month.
   * @param s station
   * @param y year
   * @param m month
   * @return True if successful; false if EOF or error or not run.
   */
    public boolean updInv(String s, int y, int m) {
        if (!UpdateInventory) return false;
        setStn(s);
        setYear(y);
        setMonth(m);
        return updInv();
    }

    /**
   * Update inventory for a station, year and month.
   * @return True if successful; false if EOF or error or not run.
   */
    public boolean updInv() {
        if (!UpdateInventory) return false;
        logger.debug("Update inventory for stn=" + stn + " month/year=" + month + "/" + year + "...");
        int num = Ninsert_elem + Nupdate_elem + Ndelete_elem;
        if (ErrorCheckOnly || num <= 0) return false;
        try {
            Nupdate_inv += invcon.updateInventory(stn, year, month);
        } catch (Exception e) {
            logger.error(e.getMessage());
            Nerror++;
        }
        return true;
    }

    /**
   * Print final summary Statistics.
   */
    public void printStats() {
        System.out.println("\nStation loaded: " + stn);
        System.out.println("Statistics:");
        System.out.println("  Records read.............. " + Nread);
        System.out.println("  Inserted ursi/days (rows). " + Ninsert_data);
        System.out.println("  Updated ursi/days (rows).. " + Nupdate_data);
        System.out.println("  Deleted ursi/days (rows).. " + Ndelete_data);
        System.out.println("  Inserted observations..... " + Ninsert_elem);
        System.out.println("  Replaced observations..... " + Nupdate_elem);
        System.out.println("  Deleted observations...... " + Ndelete_elem);
        System.out.println("  Skipped observations...... " + Nskip_elem);
        System.out.println("  Inventory updated......... " + Nupdate_inv);
        System.out.println("  New ionosondes inserted... " + Ninsert_ionosonde);
        System.out.println("  New stations inserted..... " + Ninsert_stn);
        System.out.println("  Stations updated.......... " + Nupdate_stn);
        System.out.println("  Warnings.................. " + Nwarn);
        System.out.println("  Errors.................... " + Nerror);
        System.out.println("\n*** End of Job ***\n");
    }

    /**
   * Cleanup routine for exceptions.
   */
    public void cleanup(SQLException e) {
        logger.error("SQLException: " + e.getMessage() + "\n *** " + e.getSQLState());
        Nerror++;
        close();
    }

    /**
   * Close the input file.
   */
    public void closeFile() {
        try {
            in.close();
        } catch (Exception e) {
            logger.error(e.getMessage());
            Nerror++;
        }
    }

    /**
   * Close routine
   */
    public void close() {
        try {
            c.close();
        } catch (Exception e) {
            logger.error(e.getMessage());
            Nerror++;
        }
        printStats();
    }
}
