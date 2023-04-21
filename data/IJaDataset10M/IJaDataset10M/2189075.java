package iono2;

import dbaccess.iono2.*;

/**
 * This program loads ionospheric data in Ionka format.
 * Data is read from the input file using the <i>IonkaFormat</i> class
 * to read and parse the IONKA format data.
 * <p>
 * <h3>Program Usage</h3>
 * <p>
 * <B>ionkaLoad</B> [Release Properties] [JDBC Properties] [Load Properties] [Other Properties] [<I>inputFile</I>]
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
 * description for the {@link dbaccess.iono2.IonoLoad#setReplaceOpt IonoLoad.setReplaceOpt} method.</TD></TR>
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
 * <UL>
 * <li>{@link dbaccess.util2.DBProperties DBProperties} : Collects properties from a
 * configuration file, if defined, or from the command line.
 * <li>{@link dbaccess.util2.Logging Logging} : Configures and starts up logging.
 * <li>{@link dbaccess.util2.DBConnect DBConnect} : Connects to the database if the
 * <i>--db</i> option was specified.
 * <li>{@link dbaccess.util2.DataFile DataFile} : Opens and reads from the IONKA data file.
 * <li>{@link dbaccess.iono2.IonkaFormat IonkaFormat} : Parses lines from the IONKA file and
 * checks for errors.
 * <li>{@link dbaccess.iono2.Ursi Ursi} : Converts each IONKA characteristic code into the URSI characteristic code.
 * <li>{@link dbaccess.iono2.IonoObs IonoObs} : Holds information about a single observation.
 * <li>{@link dbaccess.iono2.IonoObsList IonoObsList} : Holds a series of observations for a single day.
 * <li>{@link dbaccess.iono2.IonoRow IonoRow} : Holds all data for a database row (1 station/day/characteristic)
 * <li>{@link dbaccess.iono2.IonoLoad IonoLoad} : Controls the loading of the data into the database.
 * </ul>
 * <p>
 * @see dbaccess.util2.DBProperties#getOptions
 * @see dbaccess.iono2.IonoLoad#setRelease
 * @see dbaccess.iono2.IonoLoad#setReplaceOpt
 */
public class ionkaLoad {

    public static void main(String[] args) {
        IonkaLoad i = new IonkaLoad();
        if (!i.initialize(args)) System.exit(1);
        i.loop();
    }
}
