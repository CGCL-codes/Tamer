package modrcon;

import java.io.Serializable;

/**
 * A datatype for commands that can be sent to Q3 based servers.
 *
 * @author Pyrite[1up]
 */
public class Q3Command implements Serializable, Comparable {

    private String command;

    private String description;

    private String example;

    public Q3Command(String cmd, String desc, String ex) {
        this.command = cmd;
        this.description = desc;
        this.example = ex;
    }

    public String getCommand() {
        return this.command;
    }

    public String getDescription() {
        return this.description;
    }

    public String getExample() {
        return this.example;
    }

    public String[] toArray() {
        String[] cmd = { this.command, this.description, this.example };
        return cmd;
    }

    @Override
    public String toString() {
        return this.command;
    }

    public int compareTo(Object o) {
        Q3Command other = (Q3Command) o;
        return this.command.compareTo(other.getCommand());
    }
}
