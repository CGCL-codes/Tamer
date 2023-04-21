package net.sourceforge.javabits.options;

import org.apache.commons.cli.CommandLine;

public interface OptionsConstructor<T> extends OptionsElement<T> {

    public abstract T create(CommandLine commandLine);
}
