package net.sourceforge.javabits.options;

import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.Options;

public interface OptionsInfo {

    public abstract void initializeOptions(Options options);

    public abstract Object create(CommandLine commandLine);
}
