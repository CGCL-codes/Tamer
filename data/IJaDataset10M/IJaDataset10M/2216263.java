package org.gcli.commands.file;

import java.io.File;
import org.gcli.ExecutionContext;
import org.gcli.annotations.Context;
import org.gcli.annotations.Description;
import org.gcli.annotations.Details;
import org.gcli.annotations.ExecutionMethod;
import org.gcli.annotations.Output;
import org.gcli.annotations.Parameter;
import org.gcli.annotations.Usage;
import org.gcli.io.OutputInterface;

public class ListDirectory {

    @Description
    public static final String description = "Displays a list of files and subdirectories in a directory";

    @Details
    public static final String details = "Display a list of files and subdirectories in the current or specified directory";

    @Usage
    public static final String usage = "dir [<directory>]";

    @Context
    private ExecutionContext context;

    @Parameter(index = 0)
    private String dir;

    @Output
    private OutputInterface output;

    @ExecutionMethod
    public void execute() {
        String workingDir = (String) context.get(Files.CURRENT_DIR);
        if (dir != null) {
            workingDir = dir;
        }
        final File file = new File(workingDir);
        if (file.isDirectory()) {
            final String[] files = file.list();
            for (final String f : files) {
                output.println(f);
            }
        } else {
            output.error(workingDir + " is not a valid directory");
        }
    }
}
