package iono2;

import dbaccess.iono2.*;

/**
 * Validate an IPS data file.  This program reads and parses a data file
 * in IPS format and checks for errors.  All error and warning messges are
 * sent to the log4j log.
 * <p>
 * <h3>Program Usage</h3>
 * <p>
 * <B>ipsValidate</B> [--db] [--print] [JDBC Properties] [Other Properties] [<I>inputFile</I>]
 * <UL>
 * <TABLE>
 * <TR><TD valign=top>--db</TD>
 *     <TD>means the IPS format should use the SPIDR ionospheric
 * database to validate data (e.g. check that the station code is valid).
 * By default data will be validated against database dumps packaged in
 * the jar file.  These become outdated as changes are made to the database
 * or until the jar file is refreshed.
 *     </TD></TR>
 * <TR><TD valign=top>--print</TD>
 *     <TD>means the parsed IPS data should be written to the log file.</TD></TR>
 * <TR><TD valign=top>{@link dbaccess.util2.DBConnect JDBC Properties}</TD>
 *     <TD>Properties for establishing a JDBC connection to the database.</TD></TR>
 * <TR><TD valign=top>{@link dbaccess.util2.DBProperties Other Properties}</TD>
 *     <TD>Other properties for the ionospheric load programs.</TD></TR>
 * <TR><TD valign=top>{@link dbaccess.util2.DataFile InputFile}</TD>
 *     <TD>The name of the input file.</TD></TR>
 * </TABLE>
 * </UL>
 * <p> 
 * The program uses objects of these classes:
 * <ul>
 * <li><b>DBProperties</b> : Collects properties from a configuration file,
 * if defined, or from the command line.
 * <li><b>Logging</b> : Configures and starts up logging.
 * <li><b>DBConnect</b> : Connects to the database if the <b>--db</b> option
 * was specified.
 * <li><b>DataFile</b> : Opens and reads from the IPS data file.
 * <li><b>IpsFormat</b> : Parses lines from the IPS file and checks for errors.
 * </ul>
 * The program uses objects of these classes:
 * <ul>
 * <li>{@link dbaccess.util2.DBProperties DBProperties} : Collects properties from a
 * configuration file, if defined, or from the command line.
 * <li>{@link dbaccess.util2.Logging Logging} : Configures and starts up logging.
 * <li>{@link dbaccess.util2.DBConnect DBConnect} : Connects to the database if the
 * <b>--db</b> option was specified.
 * <li>{@link dbaccess.util2.DataFile DataFile} : Opens and reads from the IPS data file.
 * <li>{@link dbaccess.iono2.IpsFormat IpsFormat} : Parses lines from the IPS file and
 * checks for errors.
 * </ul>
 */
public class ipsValidate {

    public static void main(String[] args) {
        IpsValidate v = new IpsValidate();
        if (!v.initialize(args)) System.exit(1);
        v.run();
    }
}
