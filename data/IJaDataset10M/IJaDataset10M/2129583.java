package vogar.commands;

import java.io.File;
import vogar.Log;

/**
 * A rm command.
 */
public final class Rm {

    private final Log log;

    public Rm(Log log) {
        this.log = log;
    }

    public void file(File file) {
        new Command(log, "rm", "-rf", file.getPath()).execute();
    }
}
