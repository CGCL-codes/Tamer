package edu.byu.ece.edif.util.jsap;

import java.io.PrintStream;
import com.martiansoftware.jsap.FlaggedOption;
import com.martiansoftware.jsap.JSAP;
import com.martiansoftware.jsap.JSAPResult;

/**
 * This class is used represent the command line options for specifying the
 * input file and output file. TODO
 * <ul>
 * <li> Allow the use/specification of STDIN (input option)
 * <li> Allow the use/specification of STDOUT (output option)
 * <li> Allow a modification of the help for each option
 * <li> Provide mechanism for parsing the extension and automatically inferring
 * a filename from the parsed extension (i.e. allow a default output filename
 * based on the input filename: input.edf -> input.jedif)
 * <li> Provide mechanism for handling stdin and stderr messages
 * <li> Should these classes be made static?
 * </ul>
 */
public class InputOutputFileCommandGroup extends InputFileCommandGroup {

    public InputOutputFileCommandGroup() {
        super();
        FlaggedOption output_file = new FlaggedOption(OUTPUT_OPTION);
        output_file.setStringParser(JSAP.STRING_PARSER);
        output_file.setRequired(JSAP.REQUIRED);
        output_file.setShortFlag(OUTPUT_OPTION_SHORT);
        output_file.setLongFlag(OUTPUT_OPTION);
        output_file.setUsageName("output_file");
        output_file.setHelp("Filename and path to the triplicated EDIF output file created by the BL-TMR tool.");
        this.addCommand(output_file);
    }

    public static String getOutputFileName(JSAPResult result) {
        return result.getString(OUTPUT_OPTION);
    }

    /**
     * Serializes an object
     * 
     * @param out: PrintStream for errors/info
     * @param result: JSAP commandline results
     * @param obj: Object to serialize
     */
    public static void serializeObject(PrintStream out, JSAPResult result, Object obj) {
        OutputFileCommandGroup.serializeObject(out, result, obj);
    }

    public static final String OUTPUT_OPTION = "output";

    public static final char OUTPUT_OPTION_SHORT = 'o';
}
