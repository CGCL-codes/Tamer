package org.jmlspecs.jir.jdt.dom.tool.verifier.option;

import ie.ucd.clops.runtime.options.BooleanOption;
import ie.ucd.clops.runtime.options.CLOPSErrorOption;
import ie.ucd.clops.runtime.options.FileListOption;
import ie.ucd.clops.runtime.options.InvalidOptionPropertyValueException;
import ie.ucd.clops.runtime.options.OptionGroup;
import ie.ucd.clops.runtime.options.OptionStore;
import ie.ucd.clops.runtime.options.StringOption;
import java.util.List;

public class JirInfoVerifierOptionStore extends OptionStore implements JirInfoVerifierOptionsInterface {

    private final StringOption ogClasspath;

    private final BooleanOption ogPrint;

    private final FileListOption ogClasses;

    private final BooleanOption ogVerbose;

    private final BooleanOption ogHelp;

    private final CLOPSErrorOption CLOPSERROROPTION;

    public JirInfoVerifierOptionStore() throws InvalidOptionPropertyValueException {
        this.ogClasspath = new StringOption("Classpath", "(?:--classpath)|(?:--cp)");
        addOption(this.ogClasspath);
        this.ogClasspath.setProperty("default", ".");
        this.ogClasspath.setProperty("aliases", "--classpath,--cp");
        this.ogPrint = new BooleanOption("Print", "(?:--print)|(?:-p)");
        addOption(this.ogPrint);
        this.ogPrint.setProperty("default", "false");
        this.ogPrint.setProperty("aliases", "--print,-p");
        this.ogClasses = new FileListOption("Classes", "");
        addOption(this.ogClasses);
        this.ogClasses.setProperty("between", "");
        this.ogClasses.setProperty("canbedir", "false");
        this.ogClasses.setProperty("allowMultiple", "false");
        this.ogClasses.setProperty("description", "Class names into which JIR information is embedded.");
        this.ogVerbose = new BooleanOption("Verbose", "(?:--verbose)|(?:--verbose)");
        addOption(this.ogVerbose);
        this.ogVerbose.setProperty("default", "false");
        this.ogVerbose.setProperty("aliases", "--verbose,--verbose");
        this.ogHelp = new BooleanOption("Help", "(?:--help)|(?:-h)");
        addOption(this.ogHelp);
        this.ogHelp.setProperty("aliases", "--help,-h");
        this.CLOPSERROROPTION = new ie.ucd.clops.runtime.options.CLOPSErrorOption();
        addOption(this.CLOPSERROROPTION);
        final OptionGroup ogOption = new OptionGroup("Option");
        addOptionGroup(ogOption);
        final OptionGroup ogAllOptions = new OptionGroup("AllOptions");
        addOptionGroup(ogAllOptions);
        ogOption.addOptionOrGroup(this.ogVerbose);
        ogOption.addOptionOrGroup(this.ogClasspath);
        ogOption.addOptionOrGroup(this.ogPrint);
        ogAllOptions.addOptionOrGroup(this.ogClasspath);
        ogAllOptions.addOptionOrGroup(this.ogPrint);
        ogAllOptions.addOptionOrGroup(this.ogClasses);
        ogAllOptions.addOptionOrGroup(this.ogVerbose);
        ogAllOptions.addOptionOrGroup(this.ogHelp);
    }

    /** {@inheritDoc} */
    public List<java.io.File> getClasses() {
        return this.ogClasses.getValue();
    }

    public FileListOption getClassesOption() {
        return this.ogClasses;
    }

    /** {@inheritDoc} */
    public String getClasspath() {
        return this.ogClasspath.getValue();
    }

    public StringOption getClasspathOption() {
        return this.ogClasspath;
    }

    /** {@inheritDoc} */
    public boolean getHelp() {
        return this.ogHelp.getValue();
    }

    public BooleanOption getHelpOption() {
        return this.ogHelp;
    }

    /** {@inheritDoc} */
    public boolean getPrint() {
        return this.ogPrint.getValue();
    }

    public BooleanOption getPrintOption() {
        return this.ogPrint;
    }

    /** {@inheritDoc} */
    public List<java.io.File> getRawClasses() {
        return this.ogClasses.getRawValue();
    }

    /** {@inheritDoc} */
    public String getRawClasspath() {
        return this.ogClasspath.getRawValue();
    }

    /** {@inheritDoc} */
    public boolean getRawHelp() {
        return this.ogHelp.getRawValue();
    }

    /** {@inheritDoc} */
    public boolean getRawPrint() {
        return this.ogPrint.getRawValue();
    }

    /** {@inheritDoc} */
    public boolean getRawVerbose() {
        return this.ogVerbose.getRawValue();
    }

    /** {@inheritDoc} */
    public boolean getVerbose() {
        return this.ogVerbose.getValue();
    }

    public BooleanOption getVerboseOption() {
        return this.ogVerbose;
    }

    /**
   * {@inheritDoc}
   */
    public boolean isClassesSet() {
        return this.ogClasses.hasValue();
    }

    /**
   * {@inheritDoc}
   */
    public boolean isClasspathSet() {
        return this.ogClasspath.hasValue();
    }

    /**
   * {@inheritDoc}
   */
    public boolean isHelpSet() {
        return this.ogHelp.hasValue();
    }

    /**
   * {@inheritDoc}
   */
    public boolean isPrintSet() {
        return this.ogPrint.hasValue();
    }

    /**
   * {@inheritDoc}
   */
    public boolean isVerboseSet() {
        return this.ogVerbose.hasValue();
    }
}
