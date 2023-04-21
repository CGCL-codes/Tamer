package net.sourceforge.seqware.pipeline.module;

import java.io.File;
import java.io.IOException;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.List;
import joptsimple.OptionParser;
import net.sourceforge.seqware.common.module.ReturnValue;
import net.sourceforge.seqware.common.metadata.MetadataDB;

public abstract class Module implements ModuleInterface {

    MetadataDB metadata;

    public static final String VERSION = "0.7.0";

    String algorithm;

    File stdoutFile;

    File stderrFile;

    List<String> parameters = new ArrayList<String>();

    /**
     * Getter for the file where the stdout will be redirected. By default, the stdout will be redirected before
     * the do_run() method and turned off after do_run() method. However, this can be changed via the @StdoutRedirect
     * annotation on the module class
     *
     * @return the stdout file object
     */
    public File getStdoutFile() {
        return stdoutFile;
    }

    public void setStdoutFile(File stdoutFile) {
        this.stdoutFile = stdoutFile;
    }

    /**
     * Getter for the file where the stderr will be redirected. By default, the stderr will be redirected before the
     * do_run() method and turned off after the do_run(). To change the behavior, use @StderrRedirect annotation.
     *
     * @return the stderr file object
     */
    public File getStderrFile() {
        return stderrFile;
    }

    /**
     * Added by Xiaoshu Wang: Setter for the file that will be used to redirect stderr
     *
     * @param stderrFile the stderr file object
     */
    public void setStderrFile(File stderrFile) {
        this.stderrFile = stderrFile;
    }

    public MetadataDB getMetadata() {
        return metadata;
    }

    public String getAlgorithm() {
        return algorithm;
    }

    public void setAlgorithm(String algorithm) {
        this.algorithm = algorithm;
    }

    public void setMetadata(MetadataDB metadata) {
        this.metadata = metadata;
    }

    public List<String> getParameters() {
        return parameters;
    }

    /**
     * This method doesn't just copy the list, it actually
     * parses through the list and looks for values that start
     * with " or ' and then attempts to merge other parameters
     * separated by space until an param ending with " or ' is
     * found. This is to work around a limitation in JOpt. It
     * breaks apart arguments by space regarless if the arg is
     * a quoted string (and hence should be treated as one
     * arg)
     *
     * @param parameters the list of module parameters
     */
    public void setParameters(List<String> parameters) {
        System.out.println("Parsing Command Parameters:");
        this.parameters.clear();
        boolean readingQuoteString = false;
        String quoteString = null;
        StringBuffer buffer = new StringBuffer();
        for (String param : parameters) {
            param = param.replaceAll("&quot;", "\"");
            param = param.replaceAll("&apos;", "'");
            param = param.replaceAll("&gt;", ">");
            param = param.replaceAll("&lt;", "<");
            param = param.replaceAll("&amp;", "&");
            param = param.replaceAll("&#42;", "*");
            param = param.replaceAll("~quot;", "\"");
            param = param.replaceAll("~apos;", "'");
            param = param.replaceAll("~gt;", ">");
            param = param.replaceAll("~lt;", "<");
            param = param.replaceAll("~amp;", "&");
            param = param.replaceAll("~#42;", "*");
            param = param.replaceAll("~star;", "*");
            if (readingQuoteString && !param.endsWith(quoteString)) {
                buffer.append(" " + param);
            } else if (param.startsWith("\"") && !readingQuoteString) {
                quoteString = "\"";
                readingQuoteString = true;
                buffer.append(param.substring(1));
            } else if (param.startsWith("'") && !readingQuoteString) {
                quoteString = "'";
                readingQuoteString = true;
                buffer.append(param.substring(1));
            } else if (readingQuoteString && param.endsWith(quoteString)) {
                buffer.append(" " + param.substring(0, param.length() - 1));
                readingQuoteString = false;
                quoteString = null;
                System.out.println("  param: " + buffer.toString());
                this.parameters.add(buffer.toString());
                buffer = new StringBuffer();
            } else {
                System.out.println("  param: " + param);
                this.parameters.add(param);
            }
        }
    }

    protected OptionParser getOptionParser() {
        OptionParser parser = null;
        return (parser);
    }

    /**
     * A method used to return the syntax for this module
     *
     * @return a string describing the syntax
     */
    @Override
    public String get_syntax() {
        OptionParser parser = getOptionParser();
        if (parser == null) {
            return new String("Sorry, no help information available");
        }
        StringWriter output = new StringWriter();
        try {
            parser.printHelpOn(output);
        } catch (IOException e) {
            e.printStackTrace();
            return (e.getMessage());
        }
        return (output.toString());
    }

    /**
     * Output Galaxy definition files so you can use this module
     * with Galaxy. See http://bitbucket.org/galaxy/galaxy-central/wiki/AddToolTutorial
     */
    public String get_galaxy_xml() {
        OptionParser parser = getOptionParser();
        if (parser == null) {
            return new String("Sorry, no module paramater information available so I can't make a Galaxy XML.");
        }
        StringWriter output = new StringWriter();
        try {
            parser.printHelpOn(output);
        } catch (IOException e) {
            e.printStackTrace();
            return (e.getMessage());
        }
        return (output.toString());
    }

    public abstract ReturnValue do_run();

    public ReturnValue init() {
        return ReturnValue.featureNotImplemented();
    }

    public ReturnValue clean_up() {
        return ReturnValue.featureNotImplemented();
    }

    public abstract ReturnValue do_test();

    public abstract ReturnValue do_verify_input();

    public abstract ReturnValue do_verify_parameters();

    public abstract ReturnValue do_verify_output();
}
