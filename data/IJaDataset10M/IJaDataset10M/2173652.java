package org.gnu.shellutils.date;

import java.io.*;
import java.text.*;
import java.util.*;
import org.gnu.common.*;

/** Print the system date and time.
	<p>
	I've left in the original code for setting the system date, but
	commented out, since I don't know any way to do this in
	Java.  I've left it there in case later Java gains this
	capability.
	<p>
	I'm not sure why the R option has to be present for the
	u option to be noticed, but that's how it seemed to be
	coded in the original.  Any thoughts?  kevinr@gjt.org
	<p>
	Instead of recognizing C/POSIX formatting codes in the +FORMAT
	argument, we expect the time format syntax as specified by
	java.text.SimpleDateFormat -- see its documentation for details.
	<p>
	As of Java 1.1, a default instance of DataFormat will only
	parse dates of the format MM/dd/yy h:mm [AP]M, even with DateFormat
	property "lenient" set.  Hopefully, the parser will come to
	support a wider range of formats in a future Java release.
	<p>
	David MacKenzie <djm@gnu.ai.mit.edu>
	<p>
	Ported to Java by Kevin Raulerson<br>
	http://www.gjt.org/~kevinr/
	
	@version 1.0
	@since 1.0
 */
public class Date extends AbstractCommand {

    /** Same as Date(), but lets you set the program name.
	 */
    public Date(String programName) {
        setProgramName(programName);
    }

    /** Creates a Date.  Afterwards you can make any desired adjustments 
		to properties and then optionally call start with a String[].
	 */
    public Date() {
    }

    public static void main(String[] args) {
        new Date().start(args);
    }

    public GetOpt start(String[] args) {
        GetOpt opts = super.start(args);
        int optc;
        String datestr = getDatestr();
        java.util.Date when;
        String format;
        String reference = getReference();
        int nArgs;
        int status = 0;
        int optionSpecifiedDateCount;
        boolean optionSpecifiedDate = false;
        int argc = args.length;
        int optind = opts.getOptInd();
        nArgs = argc - optind;
        String batchFile = getBatchFileName();
        optionSpecifiedDateCount = (datestr != null ? 1 : 0) + (batchFile != null ? 1 : 0) + (reference != null ? 1 : 0);
        if (optionSpecifiedDateCount > 1) {
            getError().error(GNUError.OK, getString("exclusivePrintingOptions"));
            usage(GNUError.FAILURE);
        } else if (optionSpecifiedDateCount == 1) optionSpecifiedDate = true;
        if (nArgs > 1) {
            getError().error(GNUError.OK, getString("tooManyNonOptions"));
            usage(GNUError.FAILURE);
        }
        if ((optionSpecifiedDate) && nArgs == 1 && !args[optind].startsWith("+")) {
            getError().error(GNUError.OK, getString("theArgument") + args[optind] + getString("lacksPlus") + getString("whenSpecifying"));
            usage(GNUError.FAILURE);
        }
        if (batchFile != null) {
            status = batchConvert(batchFile, (nArgs == 1 ? args[optind + 1] : null));
        } else {
            try {
                if (!optionSpecifiedDate) {
                    if (nArgs == 1 && !args[optind].startsWith("+")) {
                        datestr = args[optind];
                        DateFormat df = DateFormat.getInstance();
                        df.setLenient(true);
                        when = df.parse(datestr);
                        format = null;
                        getError().error(GNUError.FAILURE, getString("cannotSet"));
                    } else {
                        when = new java.util.Date();
                        format = (nArgs == 1 ? args[optind].substring(1) : null);
                    }
                } else {
                    if (reference != null) {
                        File f = new File(reference);
                        if (!f.exists()) getError().error(GNUError.FAILURE, reference);
                        when = new java.util.Date(f.lastModified());
                    } else {
                        DateFormat df = DateFormat.getInstance();
                        df.setLenient(true);
                        when = df.parse(datestr);
                    }
                    format = (nArgs == 1 ? args[optind].substring(1) : null);
                }
                showDate(format, when);
            } catch (ParseException ex) {
                getError().error(GNUError.FAILURE, getString("invalidDate1") + datestr + getString("invalidDate2"));
            }
        }
        System.exit(status);
        return opts;
    }

    /** Switches on c.  Returns true if an option was selected.
	 */
    protected void decodeSwitch(int c, GetOpt opts) {
        switch(c) {
            case 'd':
                setDatestr(opts.getOptArg());
                break;
            case 'f':
                setBatchFileName(opts.getOptArg());
                break;
            case 'r':
                setReference(opts.getOptArg());
                break;
            case 'R':
                setRfcFormat(true);
                break;
            case 's':
                break;
            case 'u':
                setUniversalTime(true);
                break;
            default:
                usage(GNUError.FAILURE);
        }
    }

    /** Display the date and/or time in WHEN according to the
		format specified in FORMAT, followed by a newline.  If
		FORMAT is NULL, use the standard output format (ctime 
		style but with a timezone inserted).
		<p>
		
	 */
    protected void showDate(String format, java.util.Date when) {
        String outStr = null;
        PrintStream out = getOut();
        if (format == null) {
            TimeZone tz = TimeZone.getDefault();
            String tzName = tz.getDisplayName(tz.inDaylightTime(new java.util.Date()), TimeZone.SHORT);
            format = (isRfcFormat() ? (isUniversalTime() ? "EEE, dd MMM yyyy HH:mm:ss 'GMT'" : "EEE, dd MMM yyyy HH:mm:ss '" + tzName + "'") : "EEE MMM dd HH:mm:ss '" + tzName + "' yyyy");
        } else if (format.length() == 0) {
            out.println();
            return;
        }
        SimpleDateFormat df = (SimpleDateFormat) DateFormat.getDateTimeInstance();
        df.applyPattern(format);
        out.println(df.format(when));
    }

    /** Parse each line in inputFilename as with --date and 
		display the each resulting time and date.  If the file 
		cannot be opened, tell why then exit.  Issue a diagnostic 
		for any lines that cannot be parsed.  If any line cannot 
		be parsed, return nonzero;  otherwise return zero.
	 */
    protected int batchConvert(String inputFilename, String format) {
        int status = 0;
        BufferedReader input = null;
        String line;
        java.util.Date when;
        final String systemInName = "-";
        try {
            if (inputFilename.equals(systemInName)) {
                input = new BufferedReader(new InputStreamReader(getIn()));
            } else {
                input = new BufferedReader(new FileReader(inputFilename));
            }
            while (true) {
                line = input.readLine();
                if (line == null) {
                    break;
                }
                try {
                    DateFormat df = DateFormat.getInstance();
                    df.setLenient(true);
                    when = df.parse(line);
                    showDate(format, when);
                } catch (ParseException ex) {
                    getError().error(GNUError.OK, getString("invalidDate1") + line + getString("invalidDate2"));
                    status = 1;
                }
            }
        } catch (FileNotFoundException ex) {
            getError().error(GNUError.FAILURE, "`" + inputFilename + "'");
        } catch (IOException ex) {
            getError().error(GNUError.OK, inputFilename + getString("readError") + ex.toString());
        } finally {
            if (input != null && !inputFilename.equals(systemInName)) {
                try {
                    input.close();
                } catch (IOException ex) {
                    getError().error(GNUError.FAILURE, getString("errorClosingFile") + inputFilename);
                }
            }
        }
        return status;
    }

    protected String getOptString() {
        return super.getOptString() + "d:f:r:Rs:u";
    }

    private String datestr;

    protected String getDatestr() {
        return this.datestr;
    }

    protected void setDatestr(String datestr) {
        this.datestr = datestr;
    }

    private String reference;

    protected String getReference() {
        return this.reference;
    }

    protected void setReference(String reference) {
        this.reference = reference;
    }

    private String batchFileName;

    protected String getBatchFileName() {
        return this.batchFileName;
    }

    protected void setBatchFileName(String batchFileName) {
        this.batchFileName = batchFileName;
    }

    private boolean rfcFormat;

    /** If true, display time in RFC-822 format for mail or news.
	 */
    protected boolean isRfcFormat() {
        return this.rfcFormat;
    }

    protected void setRfcFormat(boolean rfcFormat) {
        this.rfcFormat = rfcFormat;
    }

    private boolean universalTime;

    /** If true, print or set Coordinated Universal Time.
	 */
    protected boolean isUniversalTime() {
        return this.universalTime;
    }

    protected void setUniversalTime(boolean universalTime) {
        this.universalTime = universalTime;
    }

    private final GetOptConstants.Option[] longOptions = { new GetOptConstants.Option("date", REQUIRED_ARGUMENT, null, 'd'), new GetOptConstants.Option("file", REQUIRED_ARGUMENT, null, 'f'), new GetOptConstants.Option("reference", REQUIRED_ARGUMENT, null, 'r'), new GetOptConstants.Option("rfc-822", NO_ARGUMENT, null, 'R'), new GetOptConstants.Option("set", REQUIRED_ARGUMENT, null, 's'), new GetOptConstants.Option("uct", NO_ARGUMENT, null, 'u'), new GetOptConstants.Option("utc", NO_ARGUMENT, null, 'u'), new GetOptConstants.Option("universal", NO_ARGUMENT, null, 'u') };

    protected GetOptConstants.Option[] getLongOptions() {
        return longOptions;
    }

    protected String[] getOptionKeys() {
        return this.optionKeys == null ? this.optionKeys = new String[] { "-d, --date=STRING", "-f, --file=DATEFILE", "-r, --reference=FILE", "-R, --rfc-822", "-s, --set=STRING", "-u, --utc, --uct, --universal" } : this.optionKeys;
    }
}
