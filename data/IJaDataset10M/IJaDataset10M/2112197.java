package nat;

import gnu.getopt.Getopt;
import java.util.ArrayList;
import nat.GetOptNatException;

/**
 * Get nats command line attribute
 * ensuring consistency by throwing a GetOptNatException
 * when it's incorrect.
 * @author Vivien
 *
 */
public class GetOptNat {

    /** true if the command line asks for the gui */
    public boolean useTheGui = false;

    /** List of filenames to be transcribed as passed in the command line */
    public ArrayList<String> fromFiles = new ArrayList<String>();

    /** List of filenames of transcription results passed in the command line */
    public ArrayList<String> toFiles = new ArrayList<String>();

    /** filename of the configuration file  passed in the command line*/
    public ArrayList<String> configFiles = new ArrayList<String>();

    /**  no verbosity asked in the command line */
    public Boolean quiet = false;

    /**
     * get config file in command line with -c option
     * @return command line configuration filename
     * @throws GetOptNatException if no config file in {@link #configFiles}
     */
    public String getConfigFile() throws GetOptNatException {
        return configFiles.get(0);
    }

    /**
     * get command line usage text
     * @return command line usage string
     */
    public static String getUsage() {
        String usage = "Usage :" + "\n 1. nat -g [-f <sourcefile> ]" + "\n    for use with the graphical user interface " + "\n    -f <sourcefile> : source document filename " + "\n 2. nat -f <sourcefile> -t <destfile> -c <configfile> [-q]  " + "\n    -f <sourcefile> : source document filename ; use '-' for stdin" + "\n    -t <destfile>   : destination filename" + "\n    -c <configfile> : transcription configuration filename" + "\n    -q              : quiet " + "\n " + "\n  multiple conversions :" + "\n  nat -c <configfile> -f <source1> -t <dest1> -f <source2> -t <dest2> ..." + "\n";
        return usage;
    }

    /**
     * Constructor
     * Builds the GetOptNat object corresponding to the command line, if valid 
     * @param argv list of command line argument values
     * @throws GetOptNatException , if the command line is not valid
     * 
     */
    public GetOptNat(String argv[]) throws GetOptNatException {
        ArrayList<String> remedies = new ArrayList<String>();
        boolean inputIsStdin = false;
        boolean outputIsStdout = false;
        Getopt g = new Getopt("nat", argv, "qgc:f:t:");
        int c;
        String arg;
        while ((c = g.getopt()) != -1) {
            switch(c) {
                case 'g':
                    useTheGui = true;
                    break;
                case 'f':
                    arg = g.getOptarg();
                    if (arg != null) {
                        if (arg.equals("-")) {
                            inputIsStdin = true;
                        }
                        fromFiles.add(arg);
                    }
                    break;
                case 't':
                    arg = g.getOptarg();
                    if (arg != null) {
                        if (arg.equals("-")) {
                            outputIsStdout = true;
                        }
                        toFiles.add(arg);
                    }
                    break;
                case 'c':
                    arg = g.getOptarg();
                    if (arg != null) {
                        configFiles.add(arg);
                    }
                    break;
                case 'q':
                    quiet = true;
                case '?':
                    break;
                default:
                    System.out.print("getopt() returned " + c + "\n");
            }
        }
        if (configFiles.size() > 1) {
            remedies.add("cannot specify multiple configuration files\n");
        }
        if (inputIsStdin && (fromFiles.size() > 1)) {
            remedies.add("cannot specify filename(s) in conjonction with stdin as input\n");
        }
        if (outputIsStdout && (fromFiles.size() > 1)) {
            remedies.add("cannot specify filename(s) in conjonction with stdout as output\n");
        }
        if (useTheGui) {
            if ((fromFiles.size() > 1) || (toFiles.size() > 1)) {
                remedies.add("cannot specify multiple input/output files when using the GUI");
            }
            if (inputIsStdin) {
                remedies.add("cannot stream input from stdin when using the GUI");
            }
            if (outputIsStdout) {
                remedies.add("cannot stream input to stdout when using the GUI");
            }
        } else {
            if (fromFiles.size() < 1) {
                remedies.add("Either use -g , either specify a least an input file with -f <filename>");
            }
            if (toFiles.size() < 1) {
                remedies.add("Either use -g , either specify at least output file with -t <filename>");
            }
            if (toFiles.size() != fromFiles.size()) {
                remedies.add("the number of -f <input> and -t <output> files must match");
            }
            if (configFiles.size() < 1) {
                remedies.add("specify a configuration file with -c <filename>");
            }
        }
        if (remedies.size() > 0) {
            String message = "Cannot start :\n";
            int i;
            for (i = 0; i < remedies.size(); i++) {
                message += " * " + remedies.get(i) + "\n";
            }
            throw new GetOptNatException(message + "\n" + getUsage());
        }
    }
}
