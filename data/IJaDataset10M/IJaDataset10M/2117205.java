package net.sourceforge.javabits.options;

import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.Options;

/**
 * @author Jochen Kuhnle
 */
public interface OptionsDescriptor<T> {

    public void initializeOptions(Options options);

    public T create(CommandLine commandLine);

    public void configure(T object, CommandLine commandLine);
}
