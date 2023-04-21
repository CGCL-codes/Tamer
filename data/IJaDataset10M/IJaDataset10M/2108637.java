package org.apptools.console;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * An implementation of the Command interface.
 * Provides an implementation of the Command.Option interface.
 * @author Johan Stenberg 
 *
 */
public class DefaultCommand implements Command {

    protected String command;

    protected String brief;

    protected Command.Option[] options;

    public DefaultCommand(String command, String brief) {
        this(command, brief, new Option[0]);
    }

    public DefaultCommand(String command, String brief, Command.Option[] options) {
        this.command = command;
        this.brief = brief;
        this.options = options;
    }

    public String getName() {
        return command;
    }

    public String getBrief() {
        return brief;
    }

    public Command.Option[] getOptions() {
        return options;
    }

    public Command.Option getOption(String id) {
        for (int i = 0; i < options.length; i++) {
            if (options[i].getName().equals(id)) return options[i];
        }
        return null;
    }

    public void addOption(Command.Option opt) {
        List old = new ArrayList(Arrays.asList(options));
        old.add(opt);
        options = (Command.Option[]) old.toArray(new Command.Option[old.size()]);
    }

    public static class Option implements Command.Option {

        protected String name;

        protected String brief;

        protected Option[] options;

        protected boolean strict;

        public Option(String name, String brief, boolean strict) {
            this.name = name;
            this.brief = brief;
            this.strict = strict;
            this.options = new Option[0];
        }

        public Option(String name, String brief, Option[] options) {
            this.name = name;
            this.brief = brief;
            this.strict = true;
            this.options = options;
        }

        public String getName() {
            return name;
        }

        public String getBrief() {
            return brief;
        }

        public Command.Option[] getOptions() {
            return options;
        }

        public boolean isStrict() {
            return strict;
        }

        public void addOption(Command.Option opt) {
            List old = new ArrayList(Arrays.asList(options));
            old.add(opt);
            options = (Command.Option[]) old.toArray(new Command.Option[old.size()]);
        }

        public void removeOption(Command.Option opt) {
            List old = new ArrayList(Arrays.asList(options));
            old.remove(opt);
            options = (Command.Option[]) old.toArray(new Command.Option[old.size()]);
        }

        public Option getOption(String id) {
            for (int i = 0; i < options.length; i++) {
                if (options[i].getName().equals(id)) return options[i];
            }
            return null;
        }

        public String toString() {
            return name;
        }
    }

    public void removeOption(Command.Option opt) {
        List old = new ArrayList(Arrays.asList(options));
        old.remove(opt);
        options = (Command.Option[]) old.toArray(new Command.Option[old.size()]);
    }
}
