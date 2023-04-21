package dbaccess.iono2;

import java.util.*;
import java.sql.*;
import dbaccess.util2.*;
import org.apache.commons.logging.LogFactory;
import org.apache.commons.logging.Log;

/**
 * This program loads ionospheric data in Iuwds format.
 * Data is read from the input file using the <i>IuwdsFormat</i> class
 * to read and parse the IUWDS format data.
 * <p>
 * <h3>Program Usage</h3>
 * <p>
 * <B>iuwdsLoad</B> [Release Properties] [JDBC Properties] [Load Properties] [Other Properties] [<I>inputFile</I>]
 * <UL>
 * <TABLE>
 * <TR><TD valign=top>--release</TD>
 *     <TD>means the data should be loaded and released for immediate
 * user availability.
 * <TR><TD valign=top>--release=XX</TD>
 *     <TD>means the data should be loaded and released but should remain
 * unavailable to users until <i>XX</i> days have passed.</TD></TR>
 * <TR><TD valign=top>--prerelease</TD>
 *     <TD>means the data should be loaded but should be unavailable to
 * users until it is manually released.</TD></TR>
 * <TR><TD valign=top>--restricted</TD>
 *     <TD>means the data should be loaded but should only be available
 * to data administrators.</TD></TR>
 * <TR><TD valign=top>--replace</TD>
 *     <TD>there are multiple variation on this property.  See the
 * description for the <i>IonoLoad.setReplaceOpt</i> method.</TD></TR>
 * <TR><TD valign=top>--variance=XX</TD>
 *     <TD>this property is used together with <i>--replace</i>.  See the
 * description for the {@link IonoLoad#setReplaceOpt IonoLoad.setReplaceOpt} method.</TD></TR>
 * <TR><TD valign=top>{@link DBConnect JDBC Properties}</TD>
 *     <TD>Properties for establishing a JDBC connection to the database.</TD></TR>
 * <TR><TD valign=top>{@link DBProperties Other Properties}</TD>
 *     <TD>Other properties for the ionospheric load programs.</TD></TR>
 * <TR><TD valign=top>{@link DataFile InputFile}</TD>
 *     <TD>The name of the input file.</TD></TR>
 * </TABLE>
 * </UL>
 * <p>
 * The program uses objects of these classes:
 * <UL>
 * <li>{@link DBProperties DBProperties} : Collects properties from a
 * configuration file, if defined, or from the command line.
 * <li>{@link Logging Logging} : Configures and starts up logging.
 * <li>{@link DBConnect DBConnect} : Connects to the database if the
 * <i>--db</i> option was specified.
 * <li>{@link DataFile DataFile} : Opens and reads from the IUWDS data file.
 * <li>{@link IuwdsFormat IuwdsFormat} : Parses lines from the IUWDS file and
 * checks for errors.
 * <li>{@link Ursi Ursi} : Converts each IUWDS characteristic code into the URSI characteristic code.
 * <li>{@link IonoObs IonoObs} : Holds information about a single observation.
 * <li>{@link IonoObsList IonoObsList} : Holds a series of observations for a single day.
 * <li>{@link IonoRow IonoRow} : Holds all data for a database row (1 station/day/characteristic)
 * <li>{@link IonoLoad IonoLoad} : Controls the loading of the data into the database.
 * </ul>
 * <p>
 * @see DBProperties#getOptions
 * @see IonoLoad#setRelease
 * @see IonoLoad#setReplaceOpt
 */
public class IuwdsLoad {

    LoadProperties prop;

    DBConnect connection;

    Statement stmt;

    DataFile file;

    StationList smap;

    UrsiList umap;

    IuwdsFormat iuwds;

    IonoLoad load;

    protected boolean updateInventory = true;

    protected boolean Verbose = false;

    protected boolean Debug = false;

    static final String PROGNAME = "iuwdsLoad";

    static final String PROGDESC = "Load IUWDS version of new URSI format ionospheric data to the database";

    static final String SOURCE = "IUWDS";

    private static Log logger = LogFactory.getLog(IuwdsLoad.class);

    /**
   * Constructor
   * Doesn't do much other than instantiating the object
   */
    public IuwdsLoad() {
    }

    /**
   * Add in logging during initialization.
   */
    public boolean initialize(String[] args) {
        prop = new LoadProperties(PROGNAME, PROGDESC);
        prop.setProperty("propPrefix", "Iono");
        if (!prop.getProperties(args)) {
            prop.usage();
            return false;
        }
        Logging log = new Logging(prop);
        connection = new DBConnect(prop, "Iono");
        if (!connection.connect()) {
            logger.fatal("Database connection failed.");
            return false;
        }
        smap = new StationList(connection);
        smap.getAllStations();
        umap = new UrsiList(connection);
        umap.get();
        file = new DataFile();
        file.setFile(prop);
        if (!file.open()) return false;
        iuwds = new IuwdsFormat();
        iuwds.setSmap(smap);
        iuwds.setUmap(umap);
        iuwds.setPrintIUWDS(false);
        iuwds.setFile(file);
        load = new IonoLoad(connection);
        load.initialize(prop);
        Verbose = prop.verbose();
        Debug = prop.debug();
        if (Verbose) logger.info("Starting " + PROGNAME + "...");
        logger.info("Loading IUWDS file=" + file.getFileName());
        if (Verbose) {
            logger.info(load.Delete() ? "Delete" : "Do not delete");
            logger.info(load.Insert() ? "Insert" : "Do not insert");
            if (!updateInventory) logger.info("Do not update inventory");
        }
        return true;
    }

    /**
   *  Main program loop:
   *  <ol>
   *  <li> get one months ionospheric data from the input file
   *  <li> print the data if requested
   *  <li> for each parameter, for each day of the month:
   *  <ul>
   *    <li> delete the data from the database, if requested
   *    <li> insert/update the data into the database, if requested
   *  </ul>
   *  </ol>
   */
    public void loop() {
        iuwds.getIUWDS();
        int Nerror = iuwds.getNerror();
        int Nwarn = iuwds.getNwarn();
        if (Nerror > 0) {
            Nerror += file.getNerror();
            int Nread = file.getNread();
            logger.fatal("IUWDS format contains errors.\n  Lines read.." + Nread + "\n  Warnings...." + Nwarn + "\n  Errors......" + Nerror);
            return;
        } else if (Nerror == 0 && Nwarn > 0) {
            Nerror += file.getNerror();
            int Nread = file.getNread();
            logger.warn("IUWDS format contains warning errors.\n  Lines read.." + Nread + "\n  Warnings...." + Nwarn);
        } else if (Verbose) logger.info("Load Successful.");
        ArrayList iuwdsrows = iuwds.getIuwdsRows();
        for (int i = 0; i < iuwdsrows.size(); i++) {
            IonoRow ionorow = (IonoRow) iuwdsrows.get(i);
            load.setRow(ionorow);
            if (load.Delete()) load.del();
            if (load.Insert()) load.put();
        }
        if (updateInventory) load.updInv();
        file.close();
        connection.close();
    }
}
