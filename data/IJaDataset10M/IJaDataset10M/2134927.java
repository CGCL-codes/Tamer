package ar.com.ktulu.util.command;

import java.util.Iterator;
import java.util.HashMap;
import java.util.NoSuchElementException;

/**
 *
 * @author  Luis Parravicini
 */
public class CommandGroup {

    private HashMap commands;

    public CommandGroup() {
        commands = new HashMap();
    }

    public boolean contains(String name) {
        return commands.containsKey(name.toLowerCase());
    }

    public boolean contains(CommandData cmd) {
        return commands.containsKey(cmd.word.toLowerCase());
    }

    public void add(String name, Command cmd) {
        if (cmd == null) throw new IllegalArgumentException("Command cannot be null");
        commands.put(name.toLowerCase(), cmd);
    }

    public void execute(CommandData data) throws CommandException {
        this.execute(null, data);
    }

    public void execute(String name) throws CommandException {
        this.execute(null, name, null);
    }

    public void execute(Context ctx, CommandData data) throws CommandException {
        this.execute(ctx, data.word, data.params);
    }

    public void execute(String name, String[] params) throws CommandException {
        this.execute(null, name, params);
    }

    public void execute(Context ctx, String name, String[] params) throws CommandException {
        if (!contains(name)) throw new NoSuchElementException("No such command (" + name + ")");
        ((Command) commands.get(name.toLowerCase())).execute(ctx, params);
    }

    public void remove(String name) {
        commands.remove(name.toLowerCase());
    }

    public Iterator iterator() {
        return commands.entrySet().iterator();
    }
}
