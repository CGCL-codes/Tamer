package com.knitml.tools.runner;

import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.GnuParser;
import org.apache.commons.cli.HelpFormatter;
import org.apache.commons.cli.Option;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import com.knitml.core.common.Parameters;
import com.knitml.tools.runner.support.RunnerUtils;
import com.knitml.validation.ValidationProgram;
import com.knitml.validation.context.KnittingContextFactory;
import com.knitml.validation.context.impl.DefaultKnittingContextFactory;
import com.knitml.validation.visitor.instruction.impl.SpringVisitorFactory;

public class ValidatePattern {

    @SuppressWarnings("unused")
    private static final Logger log = LoggerFactory.getLogger(RenderPattern.class);

    private static final Options options = new Options();

    static {
        Option checksyntax = new Option("checksyntax", false, "check the syntax against the KnitML schema");
        Option output = new Option("output", true, "File name to output the results");
        output.setType("file");
        options.addOption(checksyntax);
        options.addOption(output);
    }

    public static void main(String[] args) {
        CommandLineParser parser = new GnuParser();
        try {
            CommandLine line = parser.parse(options, args);
            Parameters parameters = RunnerUtils.toParameters(line);
            KnittingContextFactory contextFactory = new DefaultKnittingContextFactory();
            ValidationProgram validator = new ValidationProgram(contextFactory, new SpringVisitorFactory());
            validator.validate(parameters);
        } catch (ParseException exp) {
            System.err.println("Parsing failed.  Reason: " + exp.getMessage());
            HelpFormatter help = new HelpFormatter();
            help.printHelp("renderPattern [options] filename", options);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }
}
