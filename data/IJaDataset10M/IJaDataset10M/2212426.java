package org.gnu.fileutils.du;

import java.io.*;
import java.text.*;
import java.util.*;
import org.gnu.common.*;

/** Differences from the Unix du:
	<ul>
	<li>Doesn't simply ignore the names of regular files given as arguments
		when -a is given.</li>
	<li>Additional options:</li>
<pre>
   -l		Count the size of all files, even if they have appeared
		    already in another hard link.
   -x		Do not cross file-system boundaries during the recursion.
   -c		Write a grand total of all of the arguments after all
		    arguments have been processed.  This can be used to find
		    out the disk usage of a directory, with some files excluded.
   -h		Print sizes in human readable format (1k 234M 2G, etc).
   -k		Print sizes in kilobytes instead of 512 byte blocks
		    (the default required by POSIX).
   -m		Print sizes in megabytes instead of 512 byte blocks
   -b		Print sizes in bytes.
   -S		Count the size of each directory separately, not including
		    the sizes of subdirectories.
   -D		Dereference only symbolic links given on the command line.
   -L		Dereference all symbolic links.
</pre>
	<p>
	Written by tege@sics.se, Torbjorn Granlund,
	and djm@ai.mit.edu, David MacKenzie.
	Variable blocks added by lm@sgi.com.
	<p>
	Ported to Java by Kevin Raulerson<br>
	http://www.gjt.org/~kevinr/
	<p>
	@version 1.0
	@since 1.0
 */
public class DU extends AbstractCommand {

    /** Same as DU(), but lets you set the program name.
	 */
    public DU(String programName) {
        setProgramName(programName);
    }

    /** Creates a DU.  Afterwards you can make any desired adjustments 
		to properties and then optionally call start with a String[].
	 */
    public DU() {
    }

    public static void main(String[] args) {
        new DU().start(args);
    }

    public GetOpt start(String[] args) {
        int c;
        String bs;
        if ("human".equals(System.getProperty("blocksize"))) {
            setOptHumanReadable(true);
            setOutputSize(SIZE_BYTES);
        }
        GetOpt opts = super.start(args);
        if (isOptAll() && isOptSummarizeOnly()) {
            getError().error(GNUError.OK, getString("cannotBothSummarize"));
            usage(GNUError.FAILURE);
        }
        int optind = opts.getOptInd();
        int argc = args.length;
        String[] filenames;
        if (optind == argc) {
            filenames = new String[] { System.getProperty("user.dir") };
        } else {
            argc -= optind;
            filenames = new String[argc];
            System.arraycopy(args, optind, filenames, 0, argc);
        }
        duFiles(filenames);
        System.exit(getExitStatus());
        return opts;
    }

    /** Switches on c.  Returns true if an option was selected.
	 */
    protected void decodeSwitch(int c, GetOpt opts) {
        switch(c) {
            case 0:
                break;
            case 'a':
                setOptAll(true);
                break;
            case 'b':
                setOutputSize(SIZE_BYTES);
                setOptHumanReadable(false);
                break;
            case 'c':
                setOptCombinedArguments(true);
                break;
            case 'h':
                setOutputSize(SIZE_BYTES);
                setOptHumanReadable(true);
                break;
            case 'k':
                setOutputSize(SIZE_KILOBYTES);
                setOptHumanReadable(false);
                break;
            case 'm':
                setOutputSize(SIZE_MEGABYTES);
                setOptHumanReadable(false);
                break;
            case 'l':
                break;
            case 's':
                setOptSummarizeOnly(true);
                break;
            case 'x':
                break;
            case 'D':
                break;
            case 'L':
                break;
            case 'S':
                setOptSeparateDirs(true);
                break;
            default:
                usage(GNUError.FAILURE);
        }
    }

    /** For SIZE == SIZE_KILOBYTES converts BYTES to kilobytes;
		for SIZE == SIZE_MEGABYTES converts BYTES to megabytes;
		and for other values of SIZE, does not convert BYTES.
	 */
    protected long convertBytes(long bytes, int size) {
        return (size == SIZE_KILOBYTES ? bytes / 1024 : size == SIZE_MEGABYTES ? bytes / (1024 * 1024) : bytes);
    }

    /** Convert BYTES to a more readable string than %d would.
		Most people visually process strings of 3-4 digits effectively,
		but longer strings of digits are more prone to misinterpretation.
		Hence, converting to an abbreviated form usually improves readability.
		Use a suffix indicating multiples of 1024 (K), 1024*1024 (M), and
		1024*1024*1024 (G).  For example, 8500 would be converted to 8.3K,
		133456345 to 127M, 56990456345 to 53G, and so on.  Numbers smaller
		than 1024 aren't modified.
	 */
    protected String humanReadable(double bytes) {
        String suffix;
        if (bytes >= 1024 * 1024 * 1024) {
            bytes /= (1024 * 1024 * 1024);
            suffix = "G";
        } else if (bytes >= 1024 * 1024) {
            bytes /= (1024 * 1024);
            suffix = "M";
        } else if (bytes >= 1024) {
            bytes /= 1024;
            suffix = "K";
        } else {
            suffix = "";
        }
        int frac = (bytes >= 10 || bytes == 0) ? 0 : 1;
        NumberFormat nf = NumberFormat.getInstance();
        nf.setMinimumFractionDigits(frac);
        nf.setMaximumFractionDigits(frac);
        nf.setGroupingUsed(false);
        return nf.format(bytes) + suffix;
    }

    /** Recursively print the sizes of the directories (and, if selected, files)
		named in FILES, the last entry of which is NULL.
	 */
    protected void duFiles(String[] filenames) {
        boolean optCombinedArguments = isOptCombinedArguments();
        int length = filenames.length;
        for (int i = 0; i < length; i++) {
            countEntry(filenames[i], true);
        }
        if (optCombinedArguments) {
            PrintStream out = getOut();
            long totalSize = getTotalSize();
            if (isOptHumanReadable()) {
                out.println(humanReadable(totalSize) + getString("tabTotal"));
            } else {
                out.println(convertBytes(totalSize, getOutputSize()) + getString("tabTotal"));
            }
            out.flush();
        }
    }

    /** Print (if appropriate) and return the size
		(in units determined by `outputSize') of file or directory ENT.
		TOP is true for external calls, false for recursive calls.
	 */
    protected long countEntry(String path, boolean top) {
        File f = new File(path);
        if (!f.exists()) {
            getError().error(GNUError.OK, path);
            setExitStatus(1);
            return 0;
        }
        long size = f.length();
        setTotalSize(getTotalSize() + size);
        boolean optHumanReadable = isOptHumanReadable();
        int outputSize = getOutputSize();
        if (f.isDirectory()) {
            String[] files = f.list();
            int length = files.length;
            for (int i = 0; i < length; i++) {
                size += countEntry(files[i], false);
            }
            if (!isOptSummarizeOnly() || top) {
                PrintStream out = getOut();
                if (optHumanReadable) {
                    getOut().println(humanReadable(size) + '\t' + path);
                } else {
                    getOut().println(convertBytes(size, outputSize) + '\t' + path);
                }
                out.flush();
            }
            return isOptSeparateDirs() ? 0 : size;
        } else if (isOptAll() || top) {
            boolean printOnlyDirSize = false;
            if (!printOnlyDirSize) {
                PrintStream out = getOut();
                if (optHumanReadable) {
                    out.println(humanReadable(size) + '\t' + path);
                } else {
                    out.println(convertBytes(size, outputSize) + '\t' + path);
                }
                out.flush();
            }
        }
        return size;
    }

    /** Truncate the string SB to have length LENGTH.
	 */
    protected void strTrunc(StringBuffer sb, int length) {
        if (sb.length() > length) {
            sb.setLength(length);
        }
    }

    protected String getOptString() {
        return super.getOptString() + "abchklmsxDLS";
    }

    private boolean optSummarizeOnly;

    /** If true, display only a total for each argument.
	 */
    protected boolean isOptSummarizeOnly() {
        return this.optSummarizeOnly;
    }

    protected void setOptSummarizeOnly(boolean optSummarizeOnly) {
        this.optSummarizeOnly = optSummarizeOnly;
    }

    private boolean optAll;

    /** If true, display counts for all files, not just directories.
	 */
    protected boolean isOptAll() {
        return this.optAll;
    }

    protected void setOptAll(boolean optAll) {
        this.optAll = optAll;
    }

    private boolean optCombinedArguments;

    /** If true, print a grand total at the end.
	 */
    protected boolean isOptCombinedArguments() {
        return this.optCombinedArguments;
    }

    protected void setOptCombinedArguments(boolean optCombinedArguments) {
        this.optCombinedArguments = optCombinedArguments;
    }

    private boolean optSeparateDirs;

    /** If true, do not add sizes of subdirectories.
	 */
    protected boolean isOptSeparateDirs() {
        return this.optSeparateDirs;
    }

    protected void setOptSeparateDirs(boolean optSeparateDirs) {
        this.optSeparateDirs = optSeparateDirs;
    }

    private boolean optHumanReadable;

    /** human style output
	 */
    protected boolean isOptHumanReadable() {
        return this.optHumanReadable;
    }

    protected void setOptHumanReadable(boolean optHumanReadable) {
        this.optHumanReadable = optHumanReadable;
    }

    private int exitStatus;

    /** The exit status to use if we don't get any fatal errors.
	 */
    protected int getExitStatus() {
        return this.exitStatus;
    }

    protected void setExitStatus(int exitStatus) {
        this.exitStatus = exitStatus;
    }

    private long totalSize;

    /** Grand total size of all args.
	 */
    protected long getTotalSize() {
        return this.totalSize;
    }

    protected void setTotalSize(long totalSize) {
        this.totalSize = totalSize;
    }

    private final GetOptConstants.Option[] longOptions = { new GetOptConstants.Option("all", NO_ARGUMENT, null, 'a'), new GetOptConstants.Option("bytes", NO_ARGUMENT, null, 'b'), new GetOptConstants.Option("count-links", NO_ARGUMENT, null, 'l'), new GetOptConstants.Option("dereference", NO_ARGUMENT, null, 'L'), new GetOptConstants.Option("dereference-args", NO_ARGUMENT, null, 'D'), new GetOptConstants.Option("human-readable", NO_ARGUMENT, null, 'h'), new GetOptConstants.Option("kilobytes", NO_ARGUMENT, null, 'k'), new GetOptConstants.Option("megabytes", NO_ARGUMENT, null, 'm'), new GetOptConstants.Option("one-file-system", NO_ARGUMENT, null, 'x'), new GetOptConstants.Option("separate-dirs", NO_ARGUMENT, null, 'S'), new GetOptConstants.Option("summarize", NO_ARGUMENT, null, 's'), new GetOptConstants.Option("total", NO_ARGUMENT, null, 'c') };

    protected GetOptConstants.Option[] getLongOptions() {
        return this.longOptions;
    }

    private int outputSize = SIZE_KILOBYTES;

    /** The units to count in.  Default is SIZE_KILOBYTES, 1K blocks.
	 */
    protected int getOutputSize() {
        return this.outputSize;
    }

    protected void setOutputSize(int outputSize) {
        this.outputSize = outputSize;
    }

    /** 1K blocks. 
	 */
    public static final int SIZE_KILOBYTES = 3;

    /** 1024K blocks. 
	 */
    public static final int SIZE_MEGABYTES = 2;

    /** 1-byte blocks.
	 */
    public static final int SIZE_BYTES = 0;

    protected String[] getOptionKeys() {
        return this.optionKeys == null ? this.optionKeys = new String[] { "-a, --all", "-b, --bytes", "-c, --total", "-D, --dereference-args", "-h, --human-readable", "-k, --kilobytes", "-l, --count-links", "-L, --dereference", "-m, --megabytes", "-S, --separate-dirs", "-s, --summarize", "-x, --one-file-system" } : this.optionKeys;
    }
}
