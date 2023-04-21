package org.vfsutils.shell.commands;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import org.apache.commons.vfs.Capability;
import org.apache.commons.vfs.FileObject;
import org.apache.commons.vfs.FileSystemException;
import org.apache.commons.vfs.FileType;
import org.vfsutils.shell.Arguments;
import org.vfsutils.shell.CommandException;
import org.vfsutils.shell.CommandInfo;
import org.vfsutils.shell.Engine;

public class Touch extends AbstractCommand {

    public Touch() {
        super("touch", new CommandInfo("Resets the modify date", "[--date=<date> [--pattern=<datepattern>]] <path>"));
    }

    public void execute(Arguments args, Engine engine) throws IllegalArgumentException, CommandException, FileSystemException {
        args.assertSize(1);
        String path = args.getArgument(0);
        final FileObject[] files = engine.pathToFiles(path);
        long time = java.lang.System.currentTimeMillis();
        if (args.hasOption("date")) {
            String dateString = args.getOption("date");
            SimpleDateFormat dateFormat;
            if (args.hasOption("pattern")) {
                String patternString = args.getOption("pattern");
                dateFormat = new SimpleDateFormat(patternString);
            } else {
                dateFormat = new SimpleDateFormat();
            }
            Date date;
            try {
                date = dateFormat.parse(dateString);
            } catch (ParseException e) {
                throw new IllegalArgumentException("Invalid format of date " + dateString);
            }
            time = date.getTime();
        }
        touch(files, time, engine);
    }

    protected void touch(FileObject[] files, long time, Engine engine) throws FileSystemException {
        boolean canTouchFile = false;
        boolean canTouchFolder = false;
        if (files.length == 0) {
            return;
        } else {
            FileObject file = files[0];
            canTouchFile = file.getFileSystem().hasCapability(Capability.SET_LAST_MODIFIED_FILE);
            canTouchFolder = file.getFileSystem().hasCapability(Capability.SET_LAST_MODIFIED_FOLDER);
            if (!canTouchFile && !canTouchFolder) {
                engine.error("The filesystem does not support changing the last modified date");
                return;
            }
        }
        int countTouchedFiles = 0;
        int countTouchedFolders = 0;
        for (int i = 0; i < files.length; i++) {
            FileObject file = files[i];
            if (file.getType().equals(FileType.FILE)) {
                if (canTouchFile) {
                    file.getContent().setLastModifiedTime(time);
                    countTouchedFiles++;
                }
            } else {
                if (canTouchFolder) {
                    file.getContent().setLastModifiedTime(time);
                    countTouchedFolders++;
                }
            }
        }
        engine.println("Touched " + countTouchedFiles + " files and " + countTouchedFolders + " folders");
    }
}
