package net.jwde;

import jargs.gnu.CmdLineParser;
import jargs.gnu.CmdLineParser.Option;
import java.util.*;
import java.io.*;
import java.net.MalformedURLException;
import java.net.URL;
import java.text.*;
import org.apache.log4j.Logger;
import org.jdom.Document;
import net.jwde.database.*;
import net.jwde.object.JWDE;

public class JWDEMain extends CmdLineParser {

    private Logger log = Logger.getLogger(this.getClass().getName());

    private List<String> optionHelpStrings = new ArrayList<String>();

    private void printUsage() {
        System.err.println("Usage: JWDERunner [options]");
        for (String msg : optionHelpStrings) {
            System.err.println(msg);
        }
    }

    private static void printMissingFile(String fileName) {
        System.err.println("Missing File " + fileName);
    }

    public Option addHelp(Option option, String helpString) {
        optionHelpStrings.add(" -" + option.shortForm() + "\t--" + option.longForm() + ": " + helpString);
        return option;
    }

    /**
	 * @param args
	 */
    public static void main(String[] args) {
        Date start = new Date();
        long time = start.getTime();
        System.out.println("Start time: " + start.toString());
        JWDEMain parser = new JWDEMain();
        CmdLineParser.Option configOption = parser.addStringOption('c', "config");
        CmdLineParser.Option transformOption = parser.addStringOption('t', "transform");
        CmdLineParser.Option verboseOption = parser.addBooleanOption('v', "verbose");
        System.out.println(configOption);
        System.out.println(transformOption);
        System.out.println(verboseOption);
        try {
            parser.parse(args);
            String configFileNameValue = (String) parser.getOptionValue(configOption);
            Boolean verboseValue = (Boolean) parser.getOptionValue(verboseOption);
            String transformValue = (String) parser.getOptionValue(transformOption);
            Vector<String> remainingValues = new Vector<String>(Arrays.asList(parser.getRemainingArgs()));
            JWDERunner jwde = new JWDERunner();
            if (configFileNameValue != null) {
                try {
                    jwde.run(new URL(configFileNameValue));
                } catch (MalformedURLException malEx) {
                    malEx.printStackTrace();
                }
            }
            if (transformValue != null && remainingValues.size() == 2) {
                jwde.extract(new URL(transformValue), new URL(remainingValues.get(0)), new URL(remainingValues.get(1)));
            }
        } catch (CmdLineParser.OptionException e) {
            System.err.println(e.getMessage());
            parser.printUsage();
            System.exit(1);
        } catch (MalformedURLException malEx) {
            malEx.printStackTrace();
            System.out.println("Malformed URL");
            System.exit(1);
        }
        time = new Date().getTime() - time;
        System.out.println("time taken (s): " + time / 1000);
    }
}
