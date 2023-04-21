package org.vfsutils.shell.commands;

import java.util.Stack;
import org.apache.commons.vfs.FileObject;
import org.apache.commons.vfs.FileSystemException;
import org.apache.commons.vfs.FileType;
import org.vfsutils.shell.Arguments;
import org.vfsutils.shell.CommandException;
import org.vfsutils.shell.CommandInfo;
import org.vfsutils.shell.Engine;

public class Pushd extends AbstractDirManip {

    public Pushd() {
        super("pushd", new CommandInfo("Push the current directory on the stack and change dir", "[[-n] <path>]"));
    }

    public void execute(Arguments args, Engine engine) throws IllegalArgumentException, CommandException, FileSystemException {
        boolean noCd = args.hasFlag("n");
        if (args.size() == 0) {
            push(engine.getCwd(), engine);
        } else {
            String path = args.getArgument(0);
            pushd(path, noCd, engine);
            if (!noCd) {
                printCwd(engine);
            }
        }
    }

    protected void push(FileObject file, Engine engine) throws FileSystemException {
        Stack stack = getStack(engine);
        if ((stack.size() == 0 || !stack.peek().equals(file)) && stack.size() <= maxStackSize) {
            stack.push(file);
        }
    }

    protected void pushd(String path, boolean noCd, Engine engine) throws FileSystemException {
        FileObject tmp = engine.pathToFile(path);
        pushd(tmp, noCd, engine);
    }

    protected void pushd(FileObject file, boolean noCd, Engine engine) throws FileSystemException {
        if (file.exists() && !file.getType().equals(FileType.FILE)) {
            if (noCd) {
                push(file, engine);
            } else {
                push(engine.getCwd(), engine);
                engine.getContext().setCwd(file);
            }
        }
    }
}
