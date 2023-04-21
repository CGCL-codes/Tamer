package org.rdqlplus.command;

import java.io.File;

public class PWD extends Command {

    public PWD() {
        super("PWD", "PWD");
    }

    public String getGroup() {
        return "Files and Directories";
    }

    public String getOneLiner() {
        return "Echoes the current working directory.";
    }

    public String run(String cmd, CommandContext context) throws CommandException {
        try {
            return context.getDir().getCanonicalPath();
        } catch (Exception e) {
            return "Directory doesn't exist?";
        }
    }
}
