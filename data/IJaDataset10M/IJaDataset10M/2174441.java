package org.daisy.braille.ui;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import org.daisy.braille.pef.PEFGenerator;

/**
 * Provides a UI for generating PEF-files. Not for public use.
 * This class is a package class. Use BasicUI
 * @author Joel Håkansson
 */
class GeneratePEF extends AbstractUI {

    private final List<Argument> reqArgs;

    private final List<OptionalArgument> optionalArgs;

    public GeneratePEF() {
        reqArgs = new ArrayList<Argument>();
        reqArgs.add(new Argument("path_to_file", "Path to the output file"));
        optionalArgs = new ArrayList<OptionalArgument>();
        optionalArgs.add(newOptionalArgument(PEFGenerator.KEY_VOLUMES, "Number of volumes to generate"));
        optionalArgs.add(newOptionalArgument(PEFGenerator.KEY_PPV, "Number of pages in each volume"));
        optionalArgs.add(newOptionalArgument(PEFGenerator.KEY_EIGHT_DOT, "Set to true to generate 8-dot braille"));
        optionalArgs.add(newOptionalArgument(PEFGenerator.KEY_ROWS, "Maximum number of rows on a page"));
        optionalArgs.add(newOptionalArgument(PEFGenerator.KEY_COLS, "Maximum number of cols on a row"));
        optionalArgs.add(newOptionalArgument(PEFGenerator.KEY_DUPLEX, "Set the duplex property"));
    }

    private OptionalArgument newOptionalArgument(String key, String desc) {
        return new OptionalArgument(key, desc, PEFGenerator.getDefaultValue(key));
    }

    public static void main(String[] args) throws FileNotFoundException {
        GeneratePEF ui = new GeneratePEF();
        if (args.length < 1) {
            System.out.println("Expected at least one more argument.");
            System.out.println();
            ui.displayHelp(System.out);
            System.exit(-ExitCode.MISSING_ARGUMENT.ordinal());
        }
        Map<String, String> p = ui.toMap(args);
        File output = new File("" + p.remove(ARG_PREFIX + 0));
        PEFGenerator generator = new PEFGenerator(p);
        System.out.println("Generating test book...");
        generator.generateTestBook(output);
        System.out.println("Done!");
    }

    @Override
    public List<Argument> getRequiredArguments() {
        return reqArgs;
    }

    @Override
    public List<OptionalArgument> getOptionalArguments() {
        return optionalArgs;
    }

    @Override
    public String getName() {
        return BasicUI.generate;
    }
}
