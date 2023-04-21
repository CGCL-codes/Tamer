package net.sourceforge.mazix.cli.option.impl.verbose;

import static java.util.Arrays.asList;
import static java.util.Collections.unmodifiableSet;
import static net.sourceforge.mazix.cli.constants.log.ErrorConstants.COMMAND_LINE_NULL_ERROR;
import static net.sourceforge.mazix.cli.option.status.OptionStatus.ACTIVE_OPTIONAL_OPTION_STATUS;
import static net.sourceforge.mazix.components.utils.check.ParameterCheckerUtils.checkNull;
import java.util.Set;
import java.util.TreeSet;
import net.sourceforge.mazix.cli.command.CommandLine;
import net.sourceforge.mazix.cli.exception.CommandLineException;
import net.sourceforge.mazix.cli.option.AbstractOption;
import net.sourceforge.mazix.cli.option.status.OptionStatus;

/**
 * A default implementation managing the "-verbose" option, allowing to set the program debug
 * option. <i>Note : no option should have the same main or secondary names</i>.
 *
 * @author Benjamin Croizet (<a href="mailto:graffity2199@yahoo.fr>graffity2199@yahoo.fr</a>)
 *
 * @since 1.0
 * @version 1.0
 */
public class VerboseOption extends AbstractOption {

    /** Serial version UID. */
    private static final long serialVersionUID = -5847253494029333067L;

    /** Option main synopsis. */
    public static final String OPTION_MAIN_SYNOPSIS = "-v";

    /** Option synopsis alias. */
    public static final Set<String> OPTION_SYNOPSIS = unmodifiableSet(new TreeSet<String>(asList(new String[] { "--verbose" })));

    /**
     * Default constructor.
     *
     * @since 1.0
     */
    public VerboseOption() {
        this(new StringBuffer("To display log information."), ACTIVE_OPTIONAL_OPTION_STATUS);
    }

    /**
     * Full constructor.
     *
     * @param fullOptionDescription
     *            the full option usage description, explaining what the option does (used for
     *            helping message). <i>Note : a new {@link StringBuffer} is created.</i>
     * @param optionStatus
     *            the option status, telling if the option is active, inactive or hidden, mustn't be
     *            <code>null</code>.
     * @since 1.0
     */
    public VerboseOption(final StringBuffer fullOptionDescription, final OptionStatus optionStatus) {
        super("-v", unmodifiableSet(new TreeSet<String>(asList(new String[] { "--verbose" }))), fullOptionDescription, optionStatus);
    }

    /**
     * {@inheritDoc}
     *
     * @since 1.0
     */
    @Override
    public VerboseOption deepClone() {
        return (VerboseOption) super.deepClone();
    }

    /**
     * {@inheritDoc}
     *
     * @since 1.0
     */
    @Override
    protected StringBuffer getFullUsageAdditions() {
        return new StringBuffer();
    }

    /**
     * {@inheritDoc}
     *
     * @since 1.0
     */
    @Override
    protected StringBuffer getFullUsageDescriptionAdditions() {
        return new StringBuffer();
    }

    /**
     * {@inheritDoc}
     *
     * @since 1.0
     */
    @Override
    protected StringBuffer getMainUsageAdditions() {
        return new StringBuffer();
    }

    /**
     * Read and parses the command to see if the verbose option has been specified.
     *
     * @param commandLine
     *            the {@link CommandLine} instance to read and parse, mustn't be <code>null</code>.
     * @return <code>true</code> if the verbose option has been specified, <code>false</code>
     *         otherwise.
     * @throws CommandLineException
     *             if any error occurs when reading or parsing the command line.
     * @since 1.0
     */
    public boolean isVerboseModeActive(final CommandLine commandLine) throws CommandLineException {
        checkNull(commandLine, COMMAND_LINE_NULL_ERROR);
        return commandLine.isOptionActiveAndSpecified(this);
    }
}
